package com.mynew.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.mynew.App;
import com.mynew.R;
import com.mynew.http.NetWorkHelper;
import com.mynew.model.ImageModel;
import com.mynew.model.NewModel;
import com.mynew.util.Cache;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by user on 2017/4/19.
 */
public class ListViewAdapter extends BaseAdapter {

    private Context context;
    private int SourceId;
    ArrayList<NewModel> list;
    Executor executor= Executors.newFixedThreadPool(5);
    private HashMap<String,Drawable> map=null;
    private Cache cache;
    public Handler handler=new Handler(){
        public void handleMessage(Message message){
            switch (message.what){
                case 1:{
                    Help help=(Help) message.obj;
                    help.imageView.setImageDrawable(help.drawable);
                }
            }
        }
    };

    public ListViewAdapter(Context context, int SourceId, ArrayList<NewModel> list){
        this.context=context;
        this.SourceId=SourceId;
        this.list=list;
        map=new HashMap<String,Drawable>();
        cache= App.getApp().getCache(context);
    }

    @Override
    public int getCount(){
        if(list==null){
            return 0;
        }
        return  list.size();
    }

    @Override
    public long getItemId(int position){
        return 0;
    }

    @Override
    public Object getItem(int position){
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup){
        NewModel newModel=list.get(position);
        if(newModel.getImageModel()!=null){
            View view= LayoutInflater.from(context).inflate(R.layout.image_listview,null);
            ImageView[] imageViews=new ImageView[3];
            imageViews[0]=(ImageView)view.findViewById(R.id.image_1);
            imageViews[1]=(ImageView)view.findViewById(R.id.image_2);
            imageViews[2]=(ImageView)view.findViewById(R.id.image_3);
            TextView title=(TextView)view.findViewById(R.id.image_listView_text);
            if(newModel.getLtitle()==null||newModel.getLtitle().equals("")){
                title.setText(newModel.getTitle());
            }else {
                title.setText(newModel.getLtitle());
            }
            ImageModel imageModel=newModel.getImageModel();
            ArrayList<String> arrayList=imageModel.getImageList();
            if (map.get(String.valueOf(newModel.getImgsrc().hashCode())) != null) {
                imageViews[0].setImageDrawable(map.get(String.valueOf(newModel.getImgsrc().hashCode())));
            } else if(cache.getImage(String.valueOf(newModel.getImgsrc().hashCode()))!=null){
                imageViews[0].setImageDrawable(cache.getImage(String.valueOf(newModel.getImgsrc().hashCode())));
            } else {
                executor.execute(RunnableImageTask(newModel.getImgsrc(), imageViews[0]));
            }
            int i=1;
            for (String src:arrayList){
                if (map.get(String.valueOf(src.hashCode())) != null) {
                    imageViews[i].setImageDrawable(map.get(String.valueOf(src.hashCode())));
                } else if(cache.getImage(String.valueOf(src.hashCode()))!=null){
                    imageViews[i].setImageDrawable(cache.getImage(String.valueOf(src.hashCode())));
                } else {
                    executor.execute(RunnableImageTask(src, imageViews[i]));
                }
                i++;
            }

            return view;
        }

        View view= LayoutInflater.from(context).inflate(SourceId,null);
        ImageView image=(ImageView)view.findViewById(R.id.image);
        TextView title=(TextView) view.findViewById(R.id.title);
        TextView digest=(TextView)view.findViewById(R.id.digest);
        String code=String.valueOf(newModel.getImgsrc().hashCode());
        if(map.get(code)!=null) {
            image.setImageDrawable(map.get(code));
        }else if(cache.getImage(code)!=null){
            image.setImageDrawable(cache.getImage(code));
        }else {
            executor.execute(RunnableTask(newModel, image));
        }
        String ti=newModel.getLtitle();
        if(ti==null){
            title.setText(newModel.getTitle());
        }
        title.setText(ti);

        if(newModel.getDigest().length()>35) {
            digest.setText(newModel.getDigest().substring(0,35));
        }else {
            digest.setText(newModel.getDigest());
        }
        return view;
    }



    //加载图片
    private Runnable RunnableTask(final NewModel newModel, final ImageView imageView){
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                //没有网络时
              /*  if(!NetWorkHelper.isNetWorkAvailable(context)){
                    Drawable drawable=cache.getImage(newModel.getDocid());
                    if(drawable!=null){
                        map.put(newModel.getDocid(),drawable);
                    }
                    Message message=new Message();
                    message.what=1;
                    Help help=new Help();
                    help.drawable=drawable;
                    help.imageView=imageView;
                    message.obj=help;
                    handler.sendMessage(message);
                    return;
                }*/

                HttpURLConnection connection=null;
                InputStream in=null;
                try {
                    URL url=new URL(newModel.getImgsrc());
                    connection=(HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    if(connection.getResponseCode()==200){
                        in=connection.getInputStream();
                    }
                    if(in!=null){
                        Drawable drawable=Drawable.createFromStream(in,"src");
                        if(drawable!=null){
                            map.put(String.valueOf(newModel.getImgsrc().hashCode()),drawable);
                            //缓存图片
                            cache.putImage(String.valueOf(newModel.getImgsrc().hashCode()),drawable);
                        }
                        Message message=new Message();
                        message.what=1;
                        Help help=new Help();
                        help.drawable=drawable;
                        help.imageView=imageView;
                        message.obj=help;
                        handler.sendMessage(message);
                        in.close();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    connection.disconnect();
                }

            }
        };
        return runnable;
    }

    //加载横排图片
    private Runnable RunnableImageTask(final String src, final ImageView imageView){
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                //没有网络时
               /* if(!NetWorkHelper.isNetWorkAvailable(context)){
                    Drawable drawable=cache.getImage(String.valueOf(src.hashCode()));
                    if(drawable!=null){
                        map.put(String.valueOf(src.hashCode()),drawable);
                    }
                    Message message=new Message();
                    message.what=1;
                    Help help=new Help();
                    help.drawable=drawable;
                    help.imageView=imageView;
                    message.obj=help;
                    handler.sendMessage(message);
                    return;
                }*/

                HttpURLConnection connection=null;
                InputStream in=null;
                try {
                    URL url=new URL(src);
                    connection=(HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    if(connection.getResponseCode()==200){
                        in=connection.getInputStream();
                    }
                    if(in!=null){
                        Drawable drawable=Drawable.createFromStream(in,"src");
                        if(drawable!=null){
                            map.put(String.valueOf(src.hashCode()),drawable);
                            //缓存图片
                            cache.putImage(String.valueOf(src.hashCode()),drawable);
                        }
                        Message message=new Message();
                        message.what=1;
                        Help help=new Help();
                        help.drawable=drawable;
                        help.imageView=imageView;
                        message.obj=help;
                        handler.sendMessage(message);
                        in.close();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    connection.disconnect();
                }

            }
        };
        return runnable;
    }

    class Help{
        ImageView imageView;
        Drawable drawable;
    }
}
