package com.quwenzhe.pull.stream;

/**
 * @Description 从对端读取数据
 * @Author quwenzhe
 * @Date 2020/8/5 5:47 PM
 */
public interface Sink<T> extends Stream {

    /**
     * 从source读取数据
     *
     * @param source 数据源
     */
    void read(Source<T> source);

}
