package com.soft.eva.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class HandleJsonArray {
    public List<String> jsonToArray(String string){
        List<String> resList = new ArrayList<>();
        try{
            JSONArray jsonArray = JSON.parseArray(string);
            for(int i = 0; i < jsonArray.size(); i++){
                resList.add(jsonArray.get(i).toString());
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return resList;
    }
}
