package com.quwenzhe.scuttlebutt;

import com.quwenzhe.pull.stream.Duplex;
import com.quwenzhe.pull.stream.impl.DefaultDuplex;
import com.quwenzhe.scuttlebutt.model.Outgoing;
import com.quwenzhe.scuttlebutt.model.Update;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.quwenzhe.scuttlebutt.model.EventType.*;

/**
 * @Description scuttlebutt的通信类，向下承接pull-stream
 * @Author quwenzhe
 * @Date 2020/8/5 5:44 PM
 */
@Slf4j
public class SbStream {

    /**
     * 记录SbStream拥有的Scuttlebutt
     */
    private Scuttlebutt sb;

    /**
     * 记录SbStream拥有的Duplex
     */
    private Duplex duplex;

    /**
     * 记录对端scuttlebutt的Id
     */
    private String peerId;

    /**
     * 记录所有端知识的最新时钟
     */
    private Map<String, Long> peerSources = new HashMap<>();

    public SbStream(Scuttlebutt scuttlebutt) {
        this.sb = scuttlebutt;

        // 创建属于本SbStream的Duplex
        this.duplex = new DefaultDuplex<>(this::onData);

        // 向对端发送Outgoing
        Outgoing outgoing = new Outgoing(sb.id, sb.getSources());
        sb.emit(SENT_OUTGOING, outgoing);

        duplex.push(outgoing);
    }

    /**
     * 在sink从source读取到数据时触发
     *
     * @param update 知识
     */
    private void onData(Object update, Runnable readNext) {
        // 1.sink读取到outgoing,计算知识差并发送给对端;
        // 2.sink读取到command,发送命令事件
        // 3.sink读取到Update,应用到本地
        if (update instanceof Outgoing) {
            sb.emit(RECEIVE_OUTGOING, update);
            shakeHands((Outgoing) update);
        } else if (update instanceof String) {
            sb.emit(RECEIVE_CMD, update);
            processCommand((String) update);
        } else if (update instanceof Update) {
            sb.emit(RECEIVE_UPDATE, update);
            processUpdate((Update) update);
        }

        // 触发sink继续执行loop
        readNext.run();
    }

    public void shakeHands(Outgoing outgoing) {
        // 获取对端的Id和对端掌握的最新知识时钟
        peerId = outgoing.getSourceId();
        Map<String, Long> peerSources = outgoing.getSources();

        // 计算本节点和对端的知识差
        List<Update> history = sb.history(peerSources);

        // 逐条向对端发送知识差
        history.forEach(update -> duplex.push(update));
        sb.emit(SENT_HISTORY, sb.id + " send history to peer:" + peerId + ",history:" + history);

        sb.subscribe(BROADCAST_UPDATE, onUpdate);
    }

    private Consumer<Update> onUpdate = this::onUpdate;

    /**
     * 收到本节点的广播事件后触发
     *
     * @param update 知识
     */
    private void onUpdate(Update update) {
        // 新传播的知识比已存储的知识旧，直接忽略
        if (peerSources.computeIfAbsent(update.sourceId, k -> 0L) >= update.timestamp) {
            sb.emit(LEGACY_TIMESTAMP_UPDATE, "update is older,update:" + update);
            return;
        }

        // 从对端接收到的数据不再重复发送到同一对端
        if (update.fromId.equals(peerId)) {
            sb.emit(UPDATE_FROM_PEER_WONT_SEND_BACK, "update from peerId" + sb.id + "won't sent update");
            return;
        }

        update.fromId = sb.id;

        duplex.push(update);
        sb.emit(SENT_UPDATE_TO_PEER, sb.id + " push update:" + update + " to peer");

        peerSources.put(update.sourceId, update.timestamp);
    }


    public void processCommand(String command) {
        log.info("receive command:{}", command);
    }

    public void processUpdate(Update update) {
        sb.update(update);
    }

    public Duplex getDuplex() {
        return duplex;
    }
}
