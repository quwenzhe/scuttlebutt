package com.quwenzhe.pull.stream.impl;

import com.quwenzhe.pull.stream.Duplex;
import com.quwenzhe.pull.stream.Sink;
import com.quwenzhe.pull.stream.Source;
import com.quwenzhe.pull.stream.model.EndOrError;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @Description 默认连接器
 * @Author quwenzhe
 * @Date 2020/7/22 5:26 PM
 */
public class DefaultDuplex<T> implements Duplex<T> {

    private Source<T> source;

    private Sink<T> sink;

    public DefaultDuplex(Function<T, Boolean> onSinkNext) {
        this.source = new DefaultSource<>(new DefaultStreamBuffer<>(1), endOrError -> {
        });
        this.sink = new DefaultSink<>(onSinkNext, endOrError -> {
        });
    }

    @Override
    public Source<T> source() {
        return source;
    }

    @Override
    public Sink<T> sink() {
        return sink;
    }

    @Override
    public void push(T data) {
        source.push(data);
    }

    @Override
    public void close(EndOrError endOrError) {
        sink.close(endOrError);
        source.close(endOrError);
    }
}
