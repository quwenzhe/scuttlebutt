package com.quwenzhe.scuttlebutt.utils;

import com.quwenzhe.scuttlebutt.model.Update;

import java.util.Map;

/**
 * @Description 工具类
 * @Author quwenzhe
 * @Date 2020/7/27 3:41 PM
 */
public class Utils {

    /**
     * 过滤知识
     *
     * @return true:符合过滤条件;false:不符合过滤条件
     */
    public static boolean filter(Update update, Map<String, Long> sources) {
        long timestamp = update.timestamp;
        String sourceId = update.sourceId;
        return !sources.containsKey(sourceId) || sources.get(sourceId) < timestamp;
    }
}
