package com.quwenzhe.scuttlebutt;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description 定义计算方法、存储协议
 * @Author quwenzhe
 * @Date 2020/7/22 5:34 PM
 */
public abstract class Scuttlebutt {

    /**
     * 本节点Id
     */
    protected String sourceId;

    /**
     * 本节点最新时钟
     */
    protected Long timestamp = 0L;

    /**
     * model拥有的duplex
     */
    protected Duplex duplex;

    /**
     * key:sourceId  value:sourceId知识最新时钟
     */
    protected Map<String, Long> sources = new ConcurrentHashMap<>();

    /**
     * 获取本节点Id
     *
     * @return 节点Id
     */
    public String getSourceId() {
        return sourceId;
    }

    /**
     * 获取本节点最大时钟
     *
     * @return 本节点最大时钟
     */
    public Long getTimestamp() {
        return timestamp;
    }

    /**
     * 创建流
     *
     * @param streamOptions 流选项
     * @return 通道
     */
    public abstract Duplex createStream(StreamOptions streamOptions);

    /**
     * 建立建立后，节点之间进行握手（非常像国家领导人会晤）
     *
     * @param object 初次会晤时，表明自己的身份和最新的知识时钟即可
     */
    public abstract void shakeHand(Object object);

    /**
     * 应用更新
     *
     * @param update 新知识
     */
    public abstract void applyUpdate(Update update);

    /**
     * 计算对端数据和本节点数据的知识差
     *
     * @param source 对端信息
     * @return 知识差
     */
    public abstract List<Update> history(Source source);

}
