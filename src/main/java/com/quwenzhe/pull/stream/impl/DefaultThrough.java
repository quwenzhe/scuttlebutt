package com.quwenzhe.pull.stream.impl;

import com.quwenzhe.pull.stream.Source;
import com.quwenzhe.pull.stream.Through;

/**
 * @Description 默认数据加工
 * @Author quwenzhe
 * @Date 2020/8/6 2:55 PM
 */
public class DefaultThrough<T> implements Through<T> {

    @Override
    public Source<T> transform(Source<T> source) {
        return source;
    }
}
