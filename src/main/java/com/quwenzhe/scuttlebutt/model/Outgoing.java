package com.quwenzhe.scuttlebutt.model;

import java.util.Map;

/**
 * @Description 初次会晤时发送的数据
 * @Author quwenzhe
 * @Date 2020/8/5 6:04 PM
 */
public class Outgoing {

    private String sourceId;

    private Map<String, Long> sources;

    public Outgoing(String sourceId, Map<String, Long> sources) {
        this.sourceId = sourceId;
        this.sources = sources;
    }

    public String getSourceId() {
        return sourceId;
    }

    public Map<String, Long> getSources() {
        return sources;
    }
}
