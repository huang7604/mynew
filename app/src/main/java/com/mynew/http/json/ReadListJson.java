package com.mynew.http.json;

import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;

import com.mynew.R;
import com.mynew.model.ImageModel;
import com.mynew.model.NewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017/4/19.
 */
public class ReadListJson extends BaseJson {

    private static final String TAG="ReadListJson";
    private static ReadListJson readListJson=null;
    public ReadListJson(Context context){
        super(context);
        Log.d(TAG,"getInstance");
    }

    //返回实例
    public static ReadListJson getInstance(Context context){
        if(readListJson==null){
            readListJson=new ReadListJson(context);
        }
        return readListJson;
    }

    //读取新闻数据
    public ArrayList<NewModel> getReadListNewModels(String result,String value){
        NewModel newModel=null;
        ArrayList<NewModel> newModels=new ArrayList<>();
        if(result!=null){
            try {
                JSONObject jsonObject=new JSONObject(result);          //获取jsonObject对象
                JSONArray jsonArray=jsonObject.getJSONArray(value);    //获取json数组
                if(jsonArray==null){
                    return null;
                }
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject json=jsonArray.getJSONObject(i);
                    if(json.has("skipType")&&json.getString("skipType").equals("special")){
                        continue;
                    }

                    if(json.has("TAG")&&json.has("TAGS")){
                        continue;
                    }

                    if(json.has("imgextra")){     //判断是否存在多张图片
                        newModel=new NewModel();
                        newModel.setTitle(getString("title",json));
                        newModel.setLtitle(getString("ltitle",json));
                        newModel.setDocid(getString("docid",json));
                        newModel.setDigest(getString("digest",json));
                        newModel.setImgsrc(getString("imgsrc",json));
                        newModel.setSource(getString("source",json));
                        newModel.setPtime(getString("ptime",json));
                        ArrayList<String> list=readImageList(json.getJSONArray("imgextra"));
                        ImageModel imageModel=new ImageModel();
                        imageModel.setImageList(list);
                        newModel.setImageModel(imageModel);
                        newModels.add(newModel);
                    }else {
                        newModel = readNewModel(json);
                        newModels.add(newModel);
                    }

                }
            }catch (JSONException e){
                Log.d(TAG,"getReadListNewModelJSONException:"+e.getMessage());
                e.printStackTrace();
            }catch (Exception e) {
                Log.d(TAG,"getReadListNewModelJException:"+e.getMessage());
                e.printStackTrace();
            }
            return newModels;
        }

        return newModels;
    }

    //读取图片
    public ArrayList<String> readImageList(JSONArray jsonArray) {
        ArrayList<String> list=new ArrayList<String>();
        try {
            for(int i=0;i<jsonArray.length();i++){
                list.add(getString("imgsrc",jsonArray.getJSONObject(i)));
            }
        }catch (JSONException e){
            Log.d(TAG,"readImageListJSONException:"+e.getMessage());
            e.printStackTrace();
        }catch (Exception e){
            Log.d(TAG,"readImageListException:"+e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
    //读取newModel
   public NewModel readNewModel(JSONObject json){
       NewModel newModel=new NewModel();
       try {
           newModel.setLtitle(getString("title", json));
           newModel.setLtitle(getString("ltitle", json));
           newModel.setDocid(getString("docid", json));
           newModel.setDigest(getString("digest", json));
           newModel.setImgsrc(getString("imgsrc", json));
           newModel.setSource(getString("source", json));
           newModel.setPtime(getString("ptime", json));
       }catch (JSONException e){
           Log.d(TAG,"readNewModelJSONException:"+e.getMessage());
           e.printStackTrace();
       }catch (Exception e){
           Log.d(TAG,"readNewModelException:"+e.getMessage());
           e.printStackTrace();
       }
       Log.d(TAG,"readNewModel");
       return newModel;
   }

}
