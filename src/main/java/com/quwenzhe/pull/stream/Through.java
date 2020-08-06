package com.quwenzhe.pull.stream;

/**
 * @Description 数据加工类
 * @Author quwenzhe
 * @Date 2020/8/5 5:48 PM
 */
public interface Through<T> extends Stream {

    /**
     * 加工数据并输出
     *
     * @param source 原始数据源
     * @return 加工后的数据源
     */
    Source<T> transform(Source<T> source);
}
