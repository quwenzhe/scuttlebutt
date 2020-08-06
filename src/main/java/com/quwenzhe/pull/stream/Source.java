package com.quwenzhe.pull.stream;

import com.quwenzhe.pull.stream.model.ReadResult;

/**
 * @Description 数据源
 * @Author quwenzhe
 * @Date 2020/8/5 5:46 PM
 */
public interface Source<T> extends Stream {

    /**
     * 向source推送新数据
     *
     * @param data 数据
     * @return 成功:true;失败:false
     */
    boolean push(T data);

    /**
     * 从source读取数据
     *
     * @param end  是否结束
     * @param sink 数据提取方
     * @return 读取结果
     */
    ReadResult<T> get(boolean end, Sink<T> sink);
}
