package com.quwenzhe.pull.stream.impl;

import com.quwenzhe.pull.stream.Sink;
import com.quwenzhe.pull.stream.Source;
import com.quwenzhe.pull.stream.StreamBuffer;
import com.quwenzhe.pull.stream.model.ReadResult;
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
     * 记录和本source建立连接的sink
     */
    private Sink<T> sink;

    public DefaultSource(StreamBuffer<T> buffer) {
        this.buffer = buffer;
    }

    @Override
    public boolean push(T data) {
        boolean success = buffer.offer(data);
        if (!success) {
            // todo:溢出来如何通知上游
            log.warn("buffer is exceed");
        }

        doNotify();
        return success;
    }

    @Override
    public ReadResult<T> get(boolean end, Sink<T> sink) {
        this.sink = sink;

        // 如果已经结束，直接返回完成状态
        if (end) {
            return ReadResult.Completed;
        }

        // 无数据返回等待状态
        // 有数据直接返回数据
        T data = buffer.poll();
        if (data == null) {
            return ReadResult.Waiting;
        } else {
            return new ReadResult<>(data);
        }
    }

    /**
     * source有了数据后通知sink拉取
     */
    private void doNotify() {
        if (sink != null) {
            sink.notifyAvailable();
        }
    }
}
