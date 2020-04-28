package com.ewheelers.eWheelersBuyers.ModelClass;

public class SuggestionModel {
    String keyName;
    String value;

    public SuggestionModel(String value) {
        this.value = value;
    }

    public SuggestionModel() {

    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }
}
