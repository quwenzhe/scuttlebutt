package com.quwenzhe.scuttlebutt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @Description 事件订阅、通知
 * @Author quwenzhe
 * @Date 2020/7/23 5:38 PM
 */
public abstract class EventEmit {

    /**
     * key:事件 value:事件发生触发的回调
     */
    private Map<Object, List<Consumer>> eventMap = new ConcurrentHashMap<>();

    /**
     * 订阅事件
     *
     * @param event    事件
     * @param consumer 回调
     */
    public EventEmit subscribe(Object event, Consumer consumer) {
        List<Consumer> consumers = eventMap.getOrDefault(event, new ArrayList<>());
        if (!consumers.contains(consumer)) {
            consumers.add(consumer);
        }
        eventMap.put(event, consumers);
        return this;
    }

    /**
     * 发送事件
     *
     * @param event 事件
     * @param data  内容
     */
    public void emit(Object event, Object data) {
        List<Consumer> consumers = eventMap.getOrDefault(event, new ArrayList<>());
        consumers.forEach(consumer -> {
            consumer.accept(data);
        });
    }
}
