package com.quwenzhe.pull.stream.impl;

import com.quwenzhe.pull.stream.Source;
import com.quwenzhe.pull.stream.StreamBuffer;
import com.quwenzhe.pull.stream.funnction.SourceCallback;
import com.quwenzhe.pull.stream.model.EndOrError;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 默认数据源实现
 * @Author quwenzhe
 * @Date 2020/8/5 5:52 PM
 */
@Slf4j
public class DefaultSource<T> implements Source<T> {

    private StreamBuffer<T> buffer;

    /**
     * 记录source执行完成触发的回调
     */
    SourceCallback<T> callback;

    public DefaultSource(StreamBuffer<T> buffer) {
        this.buffer = buffer;
    }

    @Override
    public void push(T data) {
        // 如果callback不为空，立刻执行回调函数
        // 如果callback为空，说明sink未读取过source，先放到缓存
        if (callback != null) {
            callback.invoke(null, data);
        } else {
            buffer.offer(data);
        }
    }

    @Override
    public void read(EndOrError end, SourceCallback<T> callback) {
        // 如果sink已结束，直接通知sink
        if (end != null) {
            callback.invoke(end, null);
            return;
        }

        this.callback = callback;

        T data = buffer.poll();
        if (data == null) {
            callback.invoke(new EndOrError(true), data);
        } else {
            callback.invoke(null, data);
        }
    }
}
