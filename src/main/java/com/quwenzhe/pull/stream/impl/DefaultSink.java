package com.quwenzhe.pull.stream.impl;

import com.quwenzhe.pull.stream.Sink;
import com.quwenzhe.pull.stream.Source;
import com.quwenzhe.pull.stream.model.ReadResult;

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
     * 定义在sink从source提取数据，返回等待状态时触发的回调
     */
    private Runnable onWait;

    /**
     * 定义在sink从source提取数据，返回关闭状态时触发的回调
     */
    private Consumer<Throwable> onClosed;

    public DefaultSink(Function<T, Boolean> onNext, Runnable onWait, Consumer<Throwable> onClosed) {
        this.onNext = onNext;
        this.onWait = onWait;
        this.onClosed = onClosed;
    }

    @Override
    public void read(Source<T> source) {
        this.source = source;

        boolean stop = false;
        boolean end = false;
        while (!stop) {
            ReadResult<T> readResult = source.get(end, this);
            switch (readResult.status) {
                case Available:
                    stop = onNext.apply(readResult.data);
                    break;
                case Wait:
                    stop = true;
                    onWait.run();
                    break;
                case Closed:
                    onClosed.accept(readResult.throwable);
                    stop = true;
                    break;
            }
        }
    }

    @Override
    public void notifyAvailable() {
        read(source);
    }

}
