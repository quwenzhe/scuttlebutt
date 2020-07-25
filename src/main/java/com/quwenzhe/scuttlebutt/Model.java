package com.quwenzhe.scuttlebutt;

import com.quwenzhe.scuttlebutt.model.EventType;
import com.quwenzhe.scuttlebutt.model.StreamOptions;
import com.quwenzhe.scuttlebutt.model.Update;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description 知识内容存储
 * @Author quwenzhe
 * @Date 2020/7/22 7:37 PM
 */
@Slf4j
public class Model extends Scuttlebutt {

    public Model(String sourceId) {
        this.sourceId = sourceId;
    }

    /**
     * 保存每个节点最新的知识
     * key:sourceId value:节点最新知识
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
        Update localUpdate = stores.getOrDefault(update.sourceId, new Update());
        if (update.timestamp > localUpdate.timestamp) {
            // 更新对端知识最新时钟
            sources.put(update.sourceId, update.timestamp);

            // 更新对端知识
            stores.put(update.sourceId, update);
        }

        this.emit(EventType.UPDATE, update);
    }

    @Override
    protected Map<String, Update> history(Map<String, Long> sources) {
        Map<String, Update> deltaUpdate = new ConcurrentHashMap<>();

        sources.forEach((sourceId, timestamp) -> {
            Update localUpdate = stores.getOrDefault(sourceId, new Update());
            if (localUpdate.timestamp > timestamp) {
                deltaUpdate.put(sourceId, localUpdate);
            }
        });

        return deltaUpdate;
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

    public Map<String, Update> get() {
        return this.stores;
    }
}
