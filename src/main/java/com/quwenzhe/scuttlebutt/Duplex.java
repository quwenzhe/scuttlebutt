package com.quwenzhe.scuttlebutt;

import com.quwenzhe.scuttlebutt.model.StreamOptions;
import com.quwenzhe.scuttlebutt.model.Update;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.function.Consumer;

/**
 * @Description 连接
 * @Author quwenzhe
 * @Date 2020/7/22 5:26 PM
 */
@Slf4j
public class Duplex {

    /**
     * duplex归属的scuttlebutt
     */
    private Scuttlebutt scuttlebutt;

    /**
     * 流选项
     */
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
     * 与对端的duplex建立连接，成功后触发回调
     *
     * @param peerDuplex 对端的duplex
     */
    public void link(Duplex peerDuplex, Consumer<Map<String, Long>> callback) {
        // 记录和本节点建立连接的duplex
        this.peerDuplex = peerDuplex;
        callback.accept(this.scuttlebutt.sources);
    }

    /**
     * 收到对端的连接请求后，发起握手，并发送知识差
     *
     * @param sources 对端掌握的全部知识数据源及最新时钟
     */
    public void shakeHand(Map<String, Long> sources) {
        Map<String, Update> deltaUpdate = scuttlebutt.history(sources);
        deltaUpdate.forEach((s, update) -> this.put(update));
    }

    /**
     * 通知和当前Duplex建立连接的Duplex更新知识
     *
     * @param update 知识
     */
    public void put(Update update) {
        this.peerDuplex.update(update);
    }

    /**
     * 接收对端的知识，更新本地知识
     *
     * @param update 知识
     */
    public void update(Update update) {
        // 如果自己发送的数据又传播回来，直接忽略
        if (update.sourceId.equals(scuttlebutt.sourceId)) {
            log.info("I receive my own message,sourceId:{},update:{}", update.sourceId, update);
            return;
        }

        this.scuttlebutt.applyUpdate(update);
    }
}
