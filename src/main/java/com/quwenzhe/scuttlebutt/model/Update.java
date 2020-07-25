package com.quwenzhe.scuttlebutt.model;

/**
 * @Description 知识
 * @Author quwenzhe
 * @Date 2020/7/22 5:50 PM
 */
public class Update {

    /**
     * 数据产生源Id
     */
    public String sourceId;

    /**
     * 数据发送源Id
     */
    public String fromId;

    /**
     * 知识的最新数据
     */
    public Long timestamp = 0L;

    /**
     * 知识
     */
    public Object data;
}