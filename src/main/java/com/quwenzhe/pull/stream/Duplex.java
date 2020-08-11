package com.quwenzhe.pull.stream;

import com.quwenzhe.pull.stream.model.EndOrError;

/**
 * @Description pull-stream的通信类，对上承接scuttlebutt
 * @Author quwenzhe
 * @Date 2020/8/5 5:42 PM
 */
public interface Duplex<T> extends Stream {

    /**
     * 获取duplex拥有的Source
     *
     * @return duplex拥有的Source
     */
    Source<T> source();

    /**
     * 获取duplex拥有的Sink
     *
     * @return duplex拥有的Sink
     */
    Sink<T> sink();

    /**
     * 向duplex推送数据
     *
     * @param data 数据
     */
    void push(T data);

    /**
     * 关闭Duplex管理的连接
     *
     * @param endOrError 结束/失败
     */
    void close(EndOrError endOrError);
}
