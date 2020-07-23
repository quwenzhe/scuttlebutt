package com.quwenzhe.scuttlebutt;

import java.util.function.Consumer;

/**
 * @Description 节点之间通信相关
 * @Author quwenzhe
 * @Date 2020/7/22 5:26 PM
 */
public class Duplex {

    /**
     * duplex归属于的Scuttlebutt
     */
    private Scuttlebutt scuttlebutt;

    private StreamOptions streamOptions;

    /**
     * 对端的duplex
     */
    private Duplex peerDuplex;

    public Duplex(Scuttlebutt scuttlebutt, StreamOptions streamOptions) {
        this.scuttlebutt = scuttlebutt;
        this.streamOptions = streamOptions;
    }

    /**
     * 和指定的duplex建立连接，连接成功后触发回调
     *
     * @param peerDuplex 来连接的对端duplex
     * @param callback   建立连接后执行的回调
     */
    public void link(Duplex peerDuplex, Consumer callback) {
        // 记录和本节点建立连接的duplex
        this.peerDuplex = peerDuplex;

        Source source = new Source();
        source.sourceId = scuttlebutt.getSourceId();
        source.timestamp = scuttlebutt.getTimestamp();

        callback.accept(source);
    }

    public void put(Update update) {
        this.peerDuplex.scuttlebutt.applyUpdate(update);
    }
}
