package com.quwenzhe.scuttlebutt;

import com.quwenzhe.scuttlebutt.model.StreamOptions;
import com.quwenzhe.scuttlebutt.model.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description 定义节点数据计算、存储标准
 * @Author quwenzhe
 * @Date 2020/7/22 5:34 PM
 */
public abstract class Scuttlebutt extends EventEmit {

    /**
     * 本节点Id
     */
    protected String peerId;

    /**
     * 一个scuttlebutt管理多个duplex
     * 每个duplex负责和对端的一个连接
     */
    protected List<Duplex> duplexes = new ArrayList<>();

    /**
     * 本节点+对端所有数据源的知识最新时钟
     * key:sourceId value:节点最新时钟
     */
    protected Map<String, Long> sources = new ConcurrentHashMap<>();

    /**
     * 创建数据流
     *
     * @param streamOptions 流选项
     * @return 通道
     */
    protected abstract Duplex createStream(StreamOptions streamOptions);

    /**
     * 本节点知识更新、对端知识更新并同步到本节点
     *
     * @param update 知识
     */
    protected abstract void applyUpdate(Update update);

    /**
     * 针对每个数据源计算知识差
     *
     * @param sources 全部对端知识源
     * @return 每个端和本地的知识差
     */
    protected abstract List<Update> history(Map<String, Long> sources);
}
