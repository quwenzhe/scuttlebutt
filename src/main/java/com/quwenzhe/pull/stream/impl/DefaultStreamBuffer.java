package com.quwenzhe.pull.stream.impl;

import com.quwenzhe.pull.stream.StreamBuffer;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @Description 数据流缓存
 * @Author quwenzhe
 * @Date 2020/8/5 6:17 PM
 */
public class DefaultStreamBuffer<T> implements StreamBuffer<T> {

    private Queue<T> queue;

    public DefaultStreamBuffer(int capacity) {
        this.queue = new ArrayBlockingQueue<>(capacity);
    }

    @Override
    public boolean offer(T data) {
        return queue.offer(data);
    }

    @Override
    public T poll() {
        return queue.poll();
    }
}
