package com.quwenzhe.scuttlebutt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description Scuttlebutt存储实现
 * @Author quwenzhe
 * @Date 2020/7/22 7:37 PM
 */
public class Model extends Scuttlebutt {

    /**
     * 保存最新知识
     */
    private Map<String, Update> stores = new ConcurrentHashMap<>();

    public Model(String sourceId) {
        this.sourceId = sourceId;
    }

    @Override
    public Duplex createStream(StreamOptions streamOptions) {
        // 建立scuttlebutt拥有的duplex、duplex归属的scuttlebutt
        this.duplex = new Duplex(this, streamOptions);
        return duplex;
    }

    @Override
    public void shakeHand(Object object) {
        if (object instanceof Source) {
            Source source = (Source) object;
            // 记录对端的id、最新知识时钟
            sources.put(source.sourceId, source.timestamp);

            // 算出本节点和对端的知识差
            List<Update> deltaUpdate = history(source);
            deltaUpdate.forEach(update -> this.duplex.put(update));
        }
    }

    @Override
    public void applyUpdate(Update update) {
        Object object = update.data;
        if (object instanceof ModelValueItem) {
            ModelValueItem modelValueItem = (ModelValueItem) object;
            Update storeUpdate = stores.get(modelValueItem.key);
            if (null == storeUpdate || update.timestamp > storeUpdate.timestamp) {
                stores.put(modelValueItem.key, update);
                this.timestamp = update.timestamp;
            }
        }
    }

    @Override
    public List<Update> history(Source source) {
        List<Update> delta = new ArrayList<>();
        stores.forEach((key, update) -> {
            if (update.timestamp > source.timestamp) {
                delta.add(update);
            }
        });
        return delta;
    }

    public void set(Update update) {
        // 本地更新
        applyUpdate(update);

        // 通过duplex更新对端
        this.duplex.put(update);
    }

    public Update get(String key) {
        return stores.get(key);
    }
}
