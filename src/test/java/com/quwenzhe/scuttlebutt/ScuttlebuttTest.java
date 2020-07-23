package com.quwenzhe.scuttlebutt;

import org.junit.Assert;
import org.junit.Test;

/**
 * @Description
 * @Author quwenzhe
 * @Date 2020/7/22 6:05 PM
 */
public class ScuttlebuttTest {

    @Test
    public void putAndGet() {
        // 构建Model、Duplex One
        String modelNameOne = "one";
        Model modelOne = new Model(modelNameOne);
        Duplex duplexOne = buildDuplex(modelOne);

        // 构建Model、Duplex Two
        String modelNameTwo = "two";
        Model modelTwo = new Model(modelNameTwo);
        Duplex duplexTwo = buildDuplex(modelTwo);

        //发起连接的一端，负责把自己和对方的知识差算出来，并发送到对端
        duplexOne.link(duplexTwo, modelOne::shakeHand);
        duplexTwo.link(duplexOne, modelTwo::shakeHand);

        // Model One更新，验证Model One、Two是否都更新
        String modelOneKey = "key_" + modelNameOne;
        String modelOneValue = "value_" + modelNameOne;
        Update updateOne = buildUpdate(modelNameOne, modelOneKey, modelOneValue);
        modelOne.set(updateOne);
        Assert.assertTrue(modelOne.get(modelOneKey).equals(updateOne));
        Assert.assertTrue(modelTwo.get(modelOneKey).equals(updateOne));

        // Model Two更新，验证Model One、Two是否都更新
        String modelTwoKey = "key_" + modelNameTwo;
        String modelTwoValue = "value_" + modelNameTwo;
        Update updateTwo = buildUpdate(modelNameTwo, modelTwoKey, modelTwoValue);
        modelTwo.set(updateTwo);
        Assert.assertTrue(modelOne.get(modelTwoKey).equals(updateTwo));
        Assert.assertTrue(modelTwo.get(modelTwoKey).equals(updateTwo));
    }

    private Duplex buildDuplex(Model modelOne) {
        StreamOptions streamOptionsTwo = new StreamOptions();
        streamOptionsTwo.readable = true;
        streamOptionsTwo.writable = true;
        return modelOne.createStream(streamOptionsTwo);
    }

    private Update buildUpdate(String modelName, String modelKey, String modelValue) {
        ModelValueItem modelValueItem = new ModelValueItem();
        modelValueItem.key = modelKey;
        modelValueItem.value = modelValue;

        Update update = new Update();
        update.sourceId = modelName;
        update.fromId = modelName;
        update.timestamp = System.currentTimeMillis();
        update.data = modelValueItem;

        return update;
    }
}
