package com.myhome.server.Service.MyModel;

import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class MyModel {

    private Map<String,Object> myModel = new HashMap<>();

    public void setModel(String key, Object value){
        this.myModel.put(key,value);
    }

    public Object getModel(String key){
        return this.myModel.get(key);
    }

    public Map<String, Object> Models(){
        return this.myModel;
    }
}
