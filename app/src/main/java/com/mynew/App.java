package com.mynew;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.mynew.Sql.MyOpenHelper;
import com.mynew.util.Cache;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by user on 2017/4/21.
 */
public class App extends Application {

    private static App app;
    //线程池
    private Executor executor;
    //缓存目录
    private File cacheFile;
    private Cache cache;
    @Override
    public void onCreate(){
        super.onCreate();
        app=this;
        executor= Executors.newFixedThreadPool(5);

    }

    public static App getApp(){

        return app;
    }

    //返回线程池
    public Executor getExecutor() {
        return executor;
    }

    public MyOpenHelper getSQLiteOpenHelper(Context context){
        MyOpenHelper myOpenHelper=new MyOpenHelper(context,"mynew",null,1);
        return myOpenHelper;
    }

    public Cache getCache(Context context){
        if(cache==null) {
            cache = new Cache(createFile(context));
        }

        return cache;
    }

    private File createFile(Context context){
       // File file=new File(Environment.getExternalStorageDirectory()+"/Android/data",context.getPackageName());
        File file=new File(context.getExternalCacheDir().getPath());
        if (!file.exists()){
            if(file.mkdir()){
                Toast.makeText(context,"成功",Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(context,"失败",Toast.LENGTH_SHORT).show();
            }
        }
        return file;
    }
}
