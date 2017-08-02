package com.mynew.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mynew.App;
import com.mynew.R;
import com.mynew.http.HttpUrlConnectUtil;
import com.mynew.http.NetWorkHelper;
import com.mynew.http.Url;
import com.mynew.http.json.ReadNewDetailJson;
import com.mynew.model.NewDetailModel;
import com.mynew.model.NewModel;
import com.mynew.util.Cache;
import com.mynew.util.NewDetailReplace;
import com.mynew.view.htmltextview.HtmlTextView;
import com.mynew.view.viewgroup.SlidingFinishLayout;
import com.mynew.view.viewgroup.TitleBarView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by user on 2017/4/21.
 */
public class DetailNewActivity extends BaseActivity {

    //活动销毁面板
    private SlidingFinishLayout slidingFinishLayout;
    //标题栏
    private TitleBarView titleBarView;
    //内容
    private HtmlTextView htmlTextView;
    //内容标题
    private TextView content_title;
    //内容来源
    private TextView content_source;
    //内容时间
    private TextView content_time;

    private Cache cache;

    private final String TAG="DetailNewActivity";
    private Handler handler=new Handler(){
       @Override
        public void handleMessage(Message message){
           switch (message.what){
               case 1:{
                   dismissDialogProgress();
                   NewDetailModel newDetailModel=(NewDetailModel)message.obj;
                   String content=new NewDetailReplace().replace(newDetailModel.getBody(),newDetailModel.getImgList());
                   content_title.setText(newDetailModel.getTitle());
                   content_source.setText("来源："+newDetailModel.getSource());
                   content_time.setText(newDetailModel.getPtime());
                   //htmlTextView.setText(content);
                   htmlTextView.setHtmlFromString(content,false);
                   break;
               }
           }
       }
    };
    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_detailnew);
        titleBarView=(TitleBarView)findViewById(R.id.title_bar);
        htmlTextView=(HtmlTextView)findViewById(R.id.html_text_view);
        content_title=(TextView)findViewById(R.id.content_title);
        content_source=(TextView)findViewById(R.id.content_source);
        content_time=(TextView)findViewById(R.id.content_time);
        NewModel newModel=(NewModel) getIntent().getSerializableExtra("newModel");
        String url= getUrl(newModel.getDocid());
        cache=getApp().getCache(this);

        //标题栏左侧按钮监听
        titleBarView.getLeftButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack();
            }
        });
        //启动线程访问服务器
        if(NetWorkHelper.isNetWorkAvailable(this)) {
            showDialogProgress();
        }else {
            showDialogProgress();
            showToast("网络不可用！");
        }
        getApp().getExecutor().execute(RunnableReadWeb(url, newModel.getDocid()));
        //初始化滑动面板
        initSlidingFinishLayout();

    }

    //访问网络
    private Runnable RunnableReadWeb(final String url, final String docId){
        Runnable task=new Runnable() {
            @Override
            public void run() {
                String result=cache.getResult(docId);
                if(result==null) {
                    result = HttpUrlConnectUtil.getFormWebByHttpUrlConnect(url, null);
                    cache.putResult(docId,result);
                }
                try {
                    NewDetailModel newDetailModel= ReadNewDetailJson.getInstance(DetailNewActivity.this).readNewDetailJson(docId,result);
                    Message message=new Message();
                    message.what=1;
                    message.obj=newDetailModel;
                    handler.sendMessage(message);
                }catch (IOException e){
                    Log.d(TAG,"RunnableReadWeb:IOException:"+e.getMessage());
                    e.printStackTrace();
                }catch (Exception e){
                    Log.d(TAG,"RunnableReadWeb:Exception:"+e.getMessage());
                    e.printStackTrace();
                }

            }
        };
        return task;
    }

    //初始化滑动删除面板
    private void initSlidingFinishLayout(){
        slidingFinishLayout=(SlidingFinishLayout)findViewById(R.id.detail_sliding_finish);
        //滑动监听
        slidingFinishLayout.setSlidingFinishListener(new SlidingFinishLayout.SlidingFinishListener() {
            @Override
            public void onSlidingFinish() {
                DetailNewActivity.this.finish();
            }
        });
    }

    //启动活动
    public static void startActivity(Context context, NewModel newModel){
        Intent intent=new Intent(context,DetailNewActivity.class);
        intent.putExtra("newModel",newModel);
        context.startActivity(intent);

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        onBack();
    }

    private void onBack(){
        finish();
        overridePendingTransition(0,R.anim.base_slide_right_out);
    }






}
