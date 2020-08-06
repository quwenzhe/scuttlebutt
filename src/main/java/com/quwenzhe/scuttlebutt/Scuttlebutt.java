package com.quwenzhe.scuttlebutt;

import com.quwenzhe.pull.stream.Duplex;
import com.quwenzhe.scuttlebutt.model.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.quwenzhe.scuttlebutt.model.EventType.BROADCAST_UPDATE;
import static com.quwenzhe.scuttlebutt.model.EventType.LEGACY_TIMESTAMP_UPDATE;

/**
 * @Description 定义节点数据计算、存储标准
 * @Author quwenzhe
 * @Date 2020/7/22 5:34 PM
 */
public abstract class Scuttlebutt extends EventEmit {

    /**
     * 本节点id
     */
    public String id;

    /**
     * 本节点掌握的所有端的最新时钟
     * key:sourceId value:最新时钟
     */
    public Map<String, Long> sources = new HashMap<>();

    /**
     * 计算对端节点和本节点的知识差
     *
     * @param sources 对端节点掌握的知识最新时钟
     * @return 知识差
     */
    public abstract List<Update> history(Map<String, Long> sources);

    /**
     * 将知识更新到model
     *
     * @param update 知识
     * @param <T>
     * @return 更新成功/失败
     */
    public abstract <T> boolean applyUpdate(Update<T> update);

    /**
     * 本地知识更新/对端知识更新
     *
     * @param update 知识
     * @param <T>
     * @return 成功:true;失败:false
     */
    <T> boolean update(Update<T> update) {
        long timestamp = update.timestamp;
        String sourceId = update.sourceId;

        long latestTimestamp = sources.computeIfAbsent(sourceId, (key) -> 0L);

        if (latestTimestamp >= timestamp) {
            emit(LEGACY_TIMESTAMP_UPDATE, id + "update is older,ignore update:" + update);
            return false;
        }

        // 更新本地知识
        boolean success = this.applyUpdate(update);
        if (success) {
            this.sources.put(sourceId, timestamp);
        }

        // 将知识广播到对端
        emit(BROADCAST_UPDATE, update);

        return success;
    }

    /**
     * 创建SbStream
     *
     * @return 和对端通信的Duplex
     */
    public Duplex createSbStream() {
        SbStream sbStream = new SbStream(this);
        return sbStream.getDuplex();
    }

    /**
     * 获取本端掌握的最新时钟
     *
     * @return
     */
    public Map<String, Long> getSources() {
        return sources;
    }
}
