package com.quwenzhe.pull.stream.looper;

/**
 * @Description 循环器
 * @Author quwenzhe
 * @Date 2020/8/12 3:47 PM
 */
public class Looper {

    private boolean active = false;
    private boolean called = false;

    public Runnable loop(Runnable function) {
        return () -> {
            called = true;
            if (!active) {
                active = true;
                while (called) {
                    called = false;
                    function.run();
                }
                active = false;
            }
        };
    }
}
