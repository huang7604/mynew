package com.mynew.http.json;

import android.content.Context;
import android.util.Log;

import com.mynew.model.ImageDetailModel;
import com.mynew.model.NewDetailModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by user on 2017/4/20.
 */
public class ReadNewDetailJson extends BaseJson {
    private static final String TAG="ReadNewDetailJson";
    private static ReadNewDetailJson readNewDetailListJson;
    public ReadNewDetailJson(Context context){
        super(context);
    }

    //返回实例
    public static ReadNewDetailJson getInstance(Context context){
        if(readNewDetailListJson==null){
            readNewDetailListJson=new ReadNewDetailJson(context);
        }
        return readNewDetailListJson;
    }

    public NewDetailModel readNewDetailJson(String value, String result)throws Exception{
        NewDetailModel newDetailModel=null;
        JSONObject jsonObject=new JSONObject(result);
        JSONObject json=jsonObject.getJSONObject(value);
        newDetailModel=readNewDetail(json);
        return newDetailModel;
    }

    //获取图文列表
    public NewDetailModel readNewDetail(JSONObject json){
        NewDetailModel newDetailModel=new NewDetailModel();
        String docid="";
        String title="";
        String source="";
        String ptime="";
        String body="";
        String url_mp4 = "";
        String cover = "";
        ArrayList<ImageDetailModel> imgList=null;
        try {
            docid=getString("docid",json);
            title=getString("title",json);
            source=getString("source",json);
            ptime=getString("ptime",json);
            body=getString("body",json);
            if(json.has("video")){
                url_mp4=getString("url_mp4",json);
                cover=getString("cover",json);
            }
            imgList=readImgList(json.getJSONArray("img"));
        }catch (Exception e){
            Log.e(TAG,"readNewDetail:"+e.getMessage());
            e.printStackTrace();
        }
        newDetailModel.setDocid(docid);
        newDetailModel.setTitle(title);
        newDetailModel.setSource(source);
        newDetailModel.setBody(body);
        newDetailModel.setPtime(ptime);
        newDetailModel.setUrl_mp4(url_mp4);
        newDetailModel.setCover(cover);
        newDetailModel.setImgList(imgList);
        return newDetailModel;
    }

    //获取图片
    public ArrayList<ImageDetailModel> readImgList(JSONArray jsonArray) {
        ArrayList<ImageDetailModel> list=new ArrayList<ImageDetailModel>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ImageDetailModel imageDetailModel=new ImageDetailModel();
                imageDetailModel.setRef(getString("ref",jsonObject));
                imageDetailModel.setPixel(getString("pixel",jsonObject));
                imageDetailModel.setSrc(getString("src",jsonObject));
                imageDetailModel.setAlt(getString("alt",jsonObject));
                list.add(imageDetailModel);
            }
        }catch (JSONException e){
            Log.e(TAG,"readImgListJSONException:"+e.getMessage());
            e.printStackTrace();
        }catch (Exception e){
            Log.e(TAG,"readImgListException:"+e.getMessage());
            e.printStackTrace();
        }
        return list;
    }



}
