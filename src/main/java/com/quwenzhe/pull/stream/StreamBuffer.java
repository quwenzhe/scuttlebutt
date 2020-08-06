package com.quwenzhe.pull.stream;

/**
 * @Description 数据缓冲区
 * @Author quwenzhe
 * @Date 2020/8/6 8:54 AM
 */
public interface StreamBuffer<T> {

    /**
     * 向数据缓冲区提供数据
     *
     * @param data 提供的数据
     * @return 成功:true;失败:false
     */
    boolean offer(T data);

    /**
     * 从数据缓冲区提取数据
     *
     * @return 提取到的数据
     */
    T poll();
}
