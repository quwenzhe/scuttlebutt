package com.quwenzhe.pull.stream.funnction;

import com.quwenzhe.pull.stream.model.EndOrError;

/**
 * @Description 供source回调sink使用，sink定义实现，source调用
 * @Author quwenzhe
 * @Date 2020/8/10 3:59 PM
 */
public interface SourceCallback<T> {

    /**
     * source回调sink
     *
     * @param endOrError 结束/错误
     * @param data       回调时传入数据
     */
    void invoke(EndOrError endOrError, T data);
}
