package com.quwenzhe.scuttlebutt.model;

/**
 * @Description 知识
 * @Author quwenzhe
 * @Date 2020/7/22 5:50 PM
 */
public class Update<T> implements Comparable<Update> {

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
    public T data;

    public Update() {
    }

    public Update(T data, Long timestamp, String sourceId, String fromId) {
        this.data = data;
        this.timestamp = timestamp;
        this.sourceId = sourceId;
        this.fromId = fromId;
    }

    @Override
    public int compareTo(Update otherUpdate) {
        int result = Long.compare(this.timestamp, otherUpdate.timestamp);
        if (result == 0) {
            result = this.sourceId.compareTo(otherUpdate.sourceId);
        }
        return result;
    }

    @Override
    public String toString() {
        return "Update{" +
                "sourceId='" + sourceId + '\'' +
                ", fromId='" + fromId + '\'' +
                ", timestamp=" + timestamp +
                ", data=" + data +
                '}';
    }
}
