package com.quwenzhe.pull.stream.impl;

import com.quwenzhe.pull.stream.Sink;
import com.quwenzhe.pull.stream.Source;
import com.quwenzhe.pull.stream.funnction.SourceCallback;
import com.quwenzhe.pull.stream.looper.Looper;
import com.quwenzhe.pull.stream.model.EndOrError;

import javax.xml.ws.Holder;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @Description 默认读取数据源
 * @Author quwenzhe
 * @Date 2020/8/5 5:52 PM
 */
public class DefaultSink<T> implements Sink<T> {

    /**
     * 记录与sink建立连接的source
     */
    private Source<T> source;

    /**
     * 定义在sink从source提取数据，返回可继续读取时触发的回调
     */
    private BiConsumer<T, Runnable> onNext;

    /**
     * 定义在sink从source提取数据，返回关闭状态时触发的回调
     */
    private Consumer<EndOrError> onClose;

    /**
     * 为避免sink与source循环调用导致栈溢出，sink定义的回调函数通过异步线程方式调用source
     */
    private Looper looper = new Looper();

    /**
     * 定义source执行完成，触发的回调函数
     */
    Holder<SourceCallback> holder = new Holder<>();

    /**
     * 再次触发从source读取数据，并定义source执行完后的回调函数
     */
    Runnable runnable = looper.loop(() -> {
        source.read(null, holder.value);
    });

    {
        holder.value = ((endOrError, data) -> {
            // 如果结束/异常，直接返回
            if (endOrError != null) {
                return;
            }

            // sink接收到source传入的数据，调用回调函数处理数据
            onNext.accept((T) data, runnable);
        });
    }

    public DefaultSink(BiConsumer<T, Runnable> onNext, Consumer<EndOrError> onClose) {
        this.onNext = onNext;
        this.onClose = onClose;
    }

    @Override
    public void read(Source<T> source) {
        // sink读取数据时记录和sink建立连接的source
        this.source = source;

        // 从source读取数据，并传入source执行完的回调函数
        source.read(null, holder.value);
    }

    @Override
    public void close(EndOrError endOrError) {
        if (onClose != null) {
            onClose.accept(endOrError);
        }
    }
}
