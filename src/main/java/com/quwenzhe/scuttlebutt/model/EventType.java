package com.quwenzhe.scuttlebutt.model;

/**
 * @Description 事件类型
 * @Author quwenzhe
 * @Date 2020/7/23 5:51 PM
 */
public enum EventType {

    /**
     * 向对端发送outgoing
     */
    SENT_OUTGOING,
    /**
     * 从对端接收到outgoing
     */
    RECEIVE_OUTGOING,
    /**
     * 向对端发送命令
     */
    SENT_CMD,
    /**
     * 从对端接收命令
     */
    RECEIVE_CMD,
    /**
     * 向对端发送新知识
     */
    SENT_UPDATE,
    /**
     * 从对端接收新知识
     */
    RECEIVE_UPDATE,
    /**
     * 向对端发送历史知识
     */
    SENT_HISTORY,
    /**
     * 从对端接收历史知识
     */
    RECEIVE_HISTORY,
    /**
     * 广播知识
     */
    BROADCAST_UPDATE,
    /**
     * model更新
     */
    MODEL_UPDATE,
    /**
     * 从对端接收到非法事件
     */
    RECEIVE_ILLEGAL_EVENT,
    /**
     * 收到延迟的知识更新
     */
    LEGACY_TIMESTAMP_UPDATE,
    /**
     * A发送到B的知识，B又发送到A，A再次接收到时不再处理
     */
    UPDATE_FROM_PEER_WONT_SEND_BACK,
    /**
     * 发送知识到对端
     */
    SENT_UPDATE_TO_PEER;
}
