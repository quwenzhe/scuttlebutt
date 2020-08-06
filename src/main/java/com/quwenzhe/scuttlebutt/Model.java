package com.quwenzhe.scuttlebutt;

import com.quwenzhe.scuttlebutt.model.ModelValueItem;
import com.quwenzhe.scuttlebutt.model.Update;
import com.quwenzhe.scuttlebutt.utils.Utils;

import java.util.*;

import static com.quwenzhe.scuttlebutt.model.EventType.LEGACY_TIMESTAMP_UPDATE;
import static com.quwenzhe.scuttlebutt.model.EventType.MODEL_UPDATE;

/**
 * @Description 知识内容存储
 * @Author quwenzhe
 * @Date 2020/7/22 7:37 PM
 */
public class Model extends Scuttlebutt {

    public Model(String id) {
        this.id = id;
    }

    /**
     * 本端拥有的最新知识
     * key:业务数据的key value:最新知识
     */
    private Map<String, Update> stores = new HashMap<>();

    @Override
    public List<Update> history(Map<String, Long> sources) {
        List<Update> history = new ArrayList<>();

        stores.forEach((key, update) -> {
            if (Utils.filter(update, sources)) {
                history.add(update);
            }
        });

        // update根据时间、数据源排序，确保知识有旧到新送达
        Collections.sort(history);

        return history;
    }

    @Override
    public <T> boolean applyUpdate(Update<T> update) {
        ModelValueItem modelValueItem = (ModelValueItem) update.data;

        if (stores.computeIfAbsent(modelValueItem.getKey(), (k) -> new Update()).timestamp > update.timestamp) {
            emit(LEGACY_TIMESTAMP_UPDATE, "update is older,update:" + update);
            return false;
        }

        stores.put(modelValueItem.getKey(), update);
        emit(MODEL_UPDATE, update);

        return true;
    }

    /**
     * 更新知识
     *
     * @param update 知识
     */
    public void set(Update update) {
        this.update(new Update<>(update.data, System.currentTimeMillis(), this.id, this.id));
    }

    /**
     * 获取知识
     *
     * @param key 知识的key
     * @return 知识
     */
    public Update get(String key) {
        return stores.get(key);
    }
}
