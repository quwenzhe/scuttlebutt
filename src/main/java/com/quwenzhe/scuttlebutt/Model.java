package com.quwenzhe.scuttlebutt;

import com.quwenzhe.scuttlebutt.model.EventType;
import com.quwenzhe.scuttlebutt.model.ModelValueItem;
import com.quwenzhe.scuttlebutt.model.StreamOptions;
import com.quwenzhe.scuttlebutt.model.Update;
import com.quwenzhe.scuttlebutt.utils.Utils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description 知识内容存储
 * @Author quwenzhe
 * @Date 2020/7/22 7:37 PM
 */
@Slf4j
public class Model extends Scuttlebutt {

    public Model(String peerId) {
        this.peerId = peerId;
    }

    /**
     * 保存每个节点最新的知识
     * key:ModelValueItem的key value:节点最新知识
     */
    private Map<String, Update> stores = new ConcurrentHashMap<>();

    @Override
    protected Duplex createStream(StreamOptions streamOptions) {
        Duplex duplex = new Duplex(this, streamOptions);
        if (!this.duplexes.contains(duplex)) {
            this.duplexes.add(duplex);
        }

        // 监听对端的事件
        listenPeerEvents();

        return duplex;
    }

    @Override
    protected void applyUpdate(Update update) {
        ModelValueItem modelValueItem = (ModelValueItem) update.data;
        Update localUpdate = stores.getOrDefault(modelValueItem.key, new Update());
        if (update.timestamp > localUpdate.timestamp) {
            // 更新对端知识最新时钟
            sources.put(update.sourceId, update.timestamp);

            // 更新对端知识
            stores.put(modelValueItem.key, update);
        }

        this.emit(EventType.UPDATE, update);
    }

    @Override
    protected List<Update> history(Map<String, Long> sources) {
        List<Update> history = new ArrayList<>();

        stores.forEach((key, update) -> {
            if (Utils.filter(update, sources)) {
                history.add(update);
            }
        });

        // update根据时间、数据源排序，确保知识有旧到新送达
        Collections.sort(history);

        return history;
    }

    /**
     * 监听对端的事件
     */
    private void listenPeerEvents() {
        this.subscribe(EventType.UPDATE, update -> notifyAllPeerDuplex((Update) update));
    }

    /**
     * 通知所有对端的duplex
     *
     * @param update
     */
    private void notifyAllPeerDuplex(Update update) {
        this.duplexes.forEach(duplex -> duplex.put(update));
    }

    public void set(Update update) {
        this.applyUpdate(update);
    }

    public Update get(String key) {
        return this.stores.get(key);
    }
}
