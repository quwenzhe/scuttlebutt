package com.quwenzhe.scuttlebutt.model;

/**
 * @Description 事件类型
 * @Author quwenzhe
 * @Date 2020/7/23 5:51 PM
 */
public enum EventType {
    /**
     * 已发送数据同步
     */
    SYNC_SENT,
    /**
     * 已接收数据同步
     */
    SYNC_RECEIVE,
    /**
     * 知识更新
     */
    UPDATE;
}
