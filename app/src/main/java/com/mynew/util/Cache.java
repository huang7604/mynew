package com.mynew.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by user on 2017/6/3.
 */
public class Cache {

    private File cacheFile;
    public Cache(File cacheFile){
        this.cacheFile=cacheFile;
    }

    //保存文件
    public void putResult(String key,String value){
        File file=new File(cacheFile.getPath(),key);
        if(file.exists()){
            file.delete();
            file=new File(cacheFile.getPath(),key);
        }
        BufferedWriter writer=null;
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            writer=new BufferedWriter(new OutputStreamWriter(outputStream));
            writer.write(value);
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //获取文件
    public String getResult(String key){
        File file=new File(cacheFile.getPath(),key);
        if(!file.exists()){
            return null;
        }
        StringBuffer result=new StringBuffer();
        try {
            FileInputStream inputStream=new FileInputStream(file);
            BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
            String line=reader.readLine();
            while (line!=null){
                result.append(line);
                line=reader.readLine();
            }
            reader.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }

    //保存图片
    public void putImage(String key, Drawable drawable){
        File file=new File(cacheFile.getPath(),key);
        BitmapDrawable bitmapDrawable=(BitmapDrawable)drawable;
        Bitmap bitmap= bitmapDrawable.getBitmap();
        try {
            FileOutputStream outputStream=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //获取图片
    public Drawable getImage(String key){
        File file=new File(cacheFile.getPath(),key);
        if (!file.exists()){
            return null;
        }
        Drawable drawable=Drawable.createFromPath(file.getPath());
        return drawable;
    }
}
