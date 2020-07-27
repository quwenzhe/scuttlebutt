package com.quwenzhe.scuttlebutt;

import com.quwenzhe.scuttlebutt.model.StreamOptions;
import com.quwenzhe.scuttlebutt.model.Update;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

import static com.quwenzhe.scuttlebutt.Constants.SYNC;
import static com.quwenzhe.scuttlebutt.model.EventType.SYNC_RECEIVE;
import static com.quwenzhe.scuttlebutt.model.EventType.SYNC_SENT;

/**
 * @Description 连接
 * @Author quwenzhe
 * @Date 2020/7/22 5:26 PM
 */
@Slf4j
public class Duplex extends EventEmit {

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
    public void link(Duplex peerDuplex) {
        // 记录和本节点建立连接的duplex
        this.peerDuplex = peerDuplex;

        // 订阅数据发送、接收事件
        this.subscribe(SYNC_SENT, o -> log.info("peerId:{} sent sync to peerId:{}", this.scuttlebutt.peerId, peerDuplex.scuttlebutt.peerId));
        this.subscribe(SYNC_RECEIVE, o -> log.info("peerId:{} receive sync from peerId:{}", this.scuttlebutt.peerId, peerDuplex.scuttlebutt.peerId));
    }

    /**
     * 收到对端的连接请求后，发起握手，并发送知识差
     *
     * @param sources 对端掌握的全部知识数据源及最新时钟
     */
    public void shakeHand(Map<String, Long> sources) {
        List<Update> updates = scuttlebutt.history(sources);
        updates.forEach(this::put);

        // 本地及对端通知SYNC事件
        emit(SYNC_SENT, null);
        this.put(SYNC);
    }

    /**
     * 通知和当前Duplex建立连接的Duplex更新知识
     *
     * @param object 知识
     */
    public void put(Object object) {
        this.peerDuplex.localUpdate(object);
    }

    public Map<String, Long> getSources() {
        return this.scuttlebutt.sources;
    }

    /**
     * 接收对端的知识，更新本地知识
     *
     * @param object 知识
     */
    private void localUpdate(Object object) {
        if (object instanceof String) {
            if (SYNC.equals(object)) {
                emit(SYNC_RECEIVE, null);
            }
        } else if (object instanceof Update) {
            Update update = (Update) object;

            // 如果自己发送的数据又传播回来，直接忽略
            if (update.fromId.equals(scuttlebutt.peerId)) {
                log.info("I receive my own message,fromId:{},update:{}", update.fromId, update);
                return;
            }

            this.scuttlebutt.applyUpdate(update);
        }
    }
}
