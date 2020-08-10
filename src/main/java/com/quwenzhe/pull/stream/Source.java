package com.quwenzhe.pull.stream;

import com.quwenzhe.pull.stream.funnction.SourceCallback;
import com.quwenzhe.pull.stream.model.EndOrError;

/**
 * @Description 数据源
 * @Author quwenzhe
 * @Date 2020/8/5 5:46 PM
 */
public interface Source<T> extends Stream {

    /**
     * 向source推送新数据
     *
     * @param data 数据
     */
    void push(T data);

    /**
     * 供sink读取数据
     *
     * @param end      结束/异常
     * @param callback 执行完后的回调方法
     */
    void read(EndOrError end, SourceCallback<T> callback);
}
