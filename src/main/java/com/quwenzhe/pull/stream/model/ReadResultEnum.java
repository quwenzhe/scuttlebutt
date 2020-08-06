package com.quwenzhe.pull.stream.model;

/**
 * @Description 数据读取枚举
 * @Author quwenzhe
 * @Date 2020/8/5 6:59 PM
 */
public enum ReadResultEnum {

    // 读取到一条数据
    Available,

    // Source暂无数据
    Wait,

    // Source抛出异常
    Closed
}
