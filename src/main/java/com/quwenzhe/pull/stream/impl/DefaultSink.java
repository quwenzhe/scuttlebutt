package com.quwenzhe.pull.stream.impl;

import com.quwenzhe.pull.stream.Sink;
import com.quwenzhe.pull.stream.Source;
import com.quwenzhe.pull.stream.funnction.SourceCallback;

import javax.xml.ws.Holder;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @Description 默认读取数据源
 * @Author quwenzhe
 * @Date 2020/8/5 5:52 PM
 */
public class DefaultSink<T> implements Sink<T> {

    private Source<T> source;

    /**
     * 定义在sink从source提取数据，返回可继续读取时触发的回调
     */
    private Function<T, Boolean> onNext;

    /**
     * 定义在sink从source提取数据，返回关闭状态时触发的回调
     */
    private Consumer<Throwable> onClosed;

    /**
     * 定义source执行完成，触发的回调函数
     */
    Holder<SourceCallback> holder = new Holder<>();

    {
        holder.value = ((endOrError, data) -> {
            // 如果结束/异常，直接返回
            if (endOrError != null) {
                return;
            }

            // sink接收到source传入的数据，调用回调函数处理数据
            onNext.apply((T) data);

            // 再次触发从source读取数据，并定义source执行完后的回调函数
            source.read(null, holder.value);
        });
    }

    public DefaultSink(Function<T, Boolean> onNext, Consumer<Throwable> onClosed) {
        this.onNext = onNext;
        this.onClosed = onClosed;
    }

    @Override
    public void read(Source<T> source) {
        // sink读取数据时记录和sink建立连接的source
        this.source = source;

        // 从source读取数据，并传入source执行完的回调函数
        source.read(null, holder.value);
    }
}
