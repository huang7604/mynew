package com.mynew.http.json;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by user on 2017/4/19.
 */
public abstract class BaseJson {
    private Context context;
    public BaseJson(Context context){
        this.context=context;
    }

    protected Context getContext(){
        return context;
    }

    //获取字符串
    public static String getString(String key, JSONObject jsonObject) throws Exception{
        String res="";
        if(jsonObject.has(key)){
            if(key==null){
                return "";
            }
            res=jsonObject.getString(key);
        }
        return res;
    }
        //获取整数
    public static int getInt(String key,JSONObject jsonObject)throws Exception{
        int res=-1;
        if(jsonObject.has(key)){
            res=jsonObject.getInt(key);
        }
        return res;
    }

    //获取浮点数
    public static double getDouble(String key,JSONObject jsonObject)throws Exception{
        double res=01;
        if(jsonObject.has(key)){
            res=jsonObject.getDouble(key);
        }
        return res;
    }

}
