package com.mynew.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.mynew.App;
import com.mynew.R;
import com.mynew.http.Url;
import com.mynew.util.DialogUtil;

/**
 * Created by user on 2017/4/20.
 */
public class BaseActivity extends Activity {

    private WindowManager windowManager;
    private Dialog dialogProgress;
    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        windowManager=(WindowManager)getSystemService(Context.WINDOW_SERVICE);
        dialogProgress= DialogUtil.createDialogProgress(this);
        initStatusBar();
    }

    //返回新闻内容URL
    public String getUrl(String newId){
        return Url.NewDetail+newId+Url.endDetailUrl;
    }

    //返回App
    public App getApp(){
        return App.getApp();
    }

    //生成Toast
    public void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }

    //获取屏幕宽度
    public int getScreenWidth(){
        DisplayMetrics displayMetrics=new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    //显示进度条
    public void showDialogProgress(){
        dialogProgress.show();
    }

    //关闭进度条
    public void dismissDialogProgress(){
        dialogProgress.dismiss();
    }

    //标题栏和导航栏
    protected void initStatusBar(){
        Window window=getWindow();
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT) {
            window.setStatusBarColor(Color.BLACK);
            window.setNavigationBarColor(Color.BLACK);
        }
    }


}
