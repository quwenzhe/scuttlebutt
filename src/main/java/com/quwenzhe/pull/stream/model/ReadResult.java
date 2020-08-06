package com.quwenzhe.pull.stream.model;

/**
 * @Description 数据读取结果
 * @Author quwenzhe
 * @Date 2020/8/5 6:58 PM
 */
public class ReadResult<T> {

    public static final ReadResult Completed = new ReadResult<>(ReadResultEnum.Closed);

    public static final ReadResult Waiting = new ReadResult<>(ReadResultEnum.Wait);

    public ReadResultEnum status;

    public T data;

    public Throwable throwable;

    public ReadResult(T data){
        this(ReadResultEnum.Available, data);
    }

    private ReadResult(ReadResultEnum status){
        this(status, null, null);
    }

    public ReadResult(ReadResultEnum status, Throwable throwable){
        this(status, null, throwable);
    }

    public ReadResult(ReadResultEnum status, T data) {
        this(status, data, null);
    }

    private ReadResult(ReadResultEnum status, T data, Throwable e){
        this.status = status;
        this.data = data;
        this.throwable = e;
    }
}
