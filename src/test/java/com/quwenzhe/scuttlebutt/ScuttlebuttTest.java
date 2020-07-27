package com.quwenzhe.scuttlebutt;

import com.quwenzhe.scuttlebutt.model.ModelValueItem;
import com.quwenzhe.scuttlebutt.model.StreamOptions;
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
        String modelNameVehicle = "vehicle";
        String keyVehicle = "key_" + modelNameVehicle;
        String valueVehicle = "value_" + modelNameVehicle;
        Model modelVehicle = new Model(modelNameVehicle);
        Duplex duplexVehicle = buildDuplex(modelVehicle);

        String modelNameCloud = "cloud";
        Model modelCloud = new Model(modelNameCloud);
        Duplex duplexCloud = buildDuplex(modelCloud);

        duplexVehicle.link(duplexCloud, duplexCloud::shakeHand);
        duplexCloud.link(duplexVehicle, duplexVehicle::shakeHand);

        Update setUpdate = buildUpdate(modelNameVehicle, keyVehicle, valueVehicle);
        modelVehicle.set(setUpdate);

        Update vehicleUpdate = modelVehicle.get(keyVehicle);
        Update cloudUpdate = modelCloud.get(keyVehicle);
        Assert.assertTrue(vehicleUpdate.equals(setUpdate));
        Assert.assertTrue(cloudUpdate.equals(setUpdate));
    }

    private Duplex buildDuplex(Model model) {
        StreamOptions streamOptions = new StreamOptions();
        streamOptions.readable = true;
        streamOptions.writable = true;
        return model.createStream(streamOptions);
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
