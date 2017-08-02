package com.mynew.view.htmltextview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;

/**
 * Created by user on 2017/4/30.
 */
public class LocalImageGetter implements Html.ImageGetter {

    public LocalImageGetter(Context context){

    }

    @Override
    public Drawable getDrawable(String source){
        Drawable drawable=null;
        //获取本地图片
        drawable=Drawable.createFromPath(source);
        drawable.setBounds(0,0,0+drawable.getIntrinsicWidth(),0+drawable.getIntrinsicHeight());
        return drawable;
    }
}
