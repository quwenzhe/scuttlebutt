package com.quwenzhe.scuttlebutt;

import com.quwenzhe.pull.stream.Duplex;
import com.quwenzhe.pull.stream.Pull;
import com.quwenzhe.scuttlebutt.model.ModelValueItem;
import com.quwenzhe.scuttlebutt.model.Update;
import org.junit.Assert;
import org.junit.Test;

/**
 * @Description 单元测试
 * @Author quwenzhe
 * @Date 2020/7/22 6:05 PM
 */
public class ScuttlebuttTest {

    @Test
    public void putAndGet() {
        Model modelOne = new Model("one");
        Model modelTwo = new Model("two");

        Duplex duplexOne = modelOne.createSbStream();
        Duplex duplexTwo = modelTwo.createSbStream();

        // duplexOne的source --> duplexTwo的sink
        Pull.pull(duplexOne, duplexTwo);
        // duplexTwo的source --> duplexOne的sink
        Pull.pull(duplexTwo, duplexOne);

        String modelName = "modelOne";
        String keyName = "keyOne";
        String valueName = "valueOne";
        Update update = buildUpdate(modelName, keyName, valueName);
        modelOne.set(update);

        Update resultOne = modelOne.get(keyName);
        Update resultTwo = modelTwo.get(keyName);
        Assert.assertTrue(resultOne.equals(resultTwo));
    }

    private Update buildUpdate(String modelName, String modelKey, String modelValue) {
        ModelValueItem modelValueItem = new ModelValueItem();
        modelValueItem.setKey(modelKey);
        modelValueItem.setValue(modelValue);

        Update update = new Update();
        update.sourceId = modelName;
        update.fromId = modelName;
        update.timestamp = System.currentTimeMillis();
        update.data = modelValueItem;

        return update;
    }

}
