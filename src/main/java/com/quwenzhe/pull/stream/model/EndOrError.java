package com.quwenzhe.pull.stream.model;

/**
 * @Description 结束/错误
 * @Author quwenzhe
 * @Date 2020/8/10 1:44 PM
 */
public class EndOrError {

    /**
     * 正常结束：true，非正常结束：false
     */
    private boolean end = false;

    /**
     * 异常退出信息
     */
    private Throwable throwable;

    public static EndOrError End = new EndOrError(true);

    public EndOrError(boolean end) {
        this.end = end;
    }

    public EndOrError(Throwable throwable) {
        this.end = end;
        this.throwable = throwable;
    }

    public boolean isEnd() {
        return end;
    }

    public Throwable getError() {
        return throwable;
    }

    @Override
    public String toString() {
        return "EndOrError{" +
                "end=" + end +
                ", throwable=" + throwable +
                '}';
    }
}
