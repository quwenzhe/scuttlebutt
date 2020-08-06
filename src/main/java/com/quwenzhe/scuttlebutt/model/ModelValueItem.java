package com.quwenzhe.scuttlebutt.model;

/**
 * @Description 知识内容
 * @Author quwenzhe
 * @Date 2020/7/25 7:51 PM
 */
public class ModelValueItem {

    private String key;

    private String value;

    @Override
    public String toString() {
        return "ModelValueItem{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
