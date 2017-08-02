package com.mynew.view.htmltextview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by user on 2017/4/30.
 */
public class UrlImageGetter implements Html.ImageGetter {

    private Context context;
    private View view;
    public UrlImageGetter(Context context, View view){
        this.context=context;
        this.view=view;
    }

    @Override
    public Drawable getDrawable(String source){
        //解析source
        String[] src=source.split("\\?");
        String pixel=src[1].split("=")[1];
        String[] num=pixel.split("\\*");
        int pixel_width=Integer.valueOf(num[0]);   //图片宽度
        int pixel_height=Integer.valueOf(num[1]);  //图片高度
        float precent=(float)pixel_height/(float)pixel_width;
        float height=view.getWidth()*precent;
        UrlDrawable urlDrawable=new UrlDrawable();
        ImageGetterAsyncTask imageGetterAsyncTask=new ImageGetterAsyncTask(urlDrawable,height);
        imageGetterAsyncTask.execute(src[0]);   //执行线程
        urlDrawable.setBounds(0, 0,view.getWidth(),(int)height );  //图片占位
        return urlDrawable;

    }

    public class ImageGetterAsyncTask extends AsyncTask<String,Void,Drawable>{
        UrlDrawable urlDrawable=null;
        float height;
        public ImageGetterAsyncTask(UrlDrawable urlDrawable,float height){
            this.urlDrawable=urlDrawable;
            this.height=height;
        }


        @Override
        public Drawable doInBackground(String... param){
            return fetchDrawable(param[0]);
        }

        @Override
        public void onPostExecute(Drawable drawable){
            urlDrawable.setBounds(0,0,view.getWidth(),(int)height);
            urlDrawable.drawable=drawable;

            UrlImageGetter.this.view.invalidate();  //跟新图片
        }

        public Drawable fetchDrawable(String source){
            Drawable drawable=null;
            try {
                //URL url=new URL(source);
                InputStream in=fetch(source);
                //获取网络图片
                drawable=Drawable.createFromStream(in,"");
                drawable.setBounds(0, 0,view.getWidth(), (int)height);
            }catch (IOException e){
                return null;
            }
            return drawable;
        }

        //获取网络连接
        private InputStream fetch(String source) throws IOException,MalformedURLException{
            URL url=new URL(source);
            HttpURLConnection connection=(HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream in=null;
            if(connection.getResponseCode()==200){
                in=connection.getInputStream();
            }
            return in;
        }


    }

    //绘制图片
    public class UrlDrawable extends BitmapDrawable{
        protected Drawable drawable;
        @Override
        public void draw(Canvas canvas){
            if(drawable!=null){
                drawable.draw(canvas);
            }
        }
    }
}
