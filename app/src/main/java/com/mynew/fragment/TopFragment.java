package com.mynew.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.mynew.App;
import com.mynew.R;
import com.mynew.activity.DetailNewActivity;
import com.mynew.adapter.ListViewAdapter;
import com.mynew.http.HttpUrlConnectUtil;
import com.mynew.http.NetWorkHelper;
import com.mynew.http.Url;
import com.mynew.http.json.ReadListJson;
import com.mynew.model.NewModel;
import com.mynew.util.Cache;
import com.mynew.view.swipelistview.SwipeListView;

import java.util.ArrayList;

/**
 * Created by user on 2017/5/17.
 */
public class TopFragment extends BaseFragment{

    private SwipeListView listView;
    private ArrayList<NewModel> list=null;
    private int page=0;
    private ListViewAdapter adapter;
    private View view;
    private boolean isRefresh=false;
    private Cache cache;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message message){
            switch (message.what) {
                case 1: {
                    ArrayList<NewModel> arrayLists=(ArrayList<NewModel>)message.obj;
                    if(arrayLists!=null&&!isRefresh){
                        for(NewModel newModel:arrayLists){
                            list.add(newModel);
                        }
                    }else {
                        for(NewModel newModel:arrayLists){
                            if(!list.contains(newModel)) {
                                list.add(newModel);
                            }
                        }
                    }

                    if(isRefresh){
                        adapter.notifyDataSetChanged();
                        listView.RefreshCallBack();
                        isRefresh=false;
                        return;
                    }
                    if(page==0){
                        intiListView(list);
                        if(list.size()>0) {
                            listView.setVisibility(View.VISIBLE);
                        }
                    }else {
                        listView.loadingCallBack();
                        adapter.notifyDataSetChanged();
                    }

                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view=inflater.inflate(R.layout.fragment_new_list,container,false);
        init();
        return view;
    }



    @Override
    public void onStart(){
        super.onStart();
        executor.execute(task(0));
    }

    @Override
    public void onStop(){
        super.onStop();
        page=0;
    }


    public void init(){
        listView=(SwipeListView)view.findViewById(R.id.list_view);
        listView.setVisibility(View.INVISIBLE);
        list=new ArrayList<NewModel>();
        cache=App.getApp().getCache(getContext());
    }

    //线程
    private Runnable task(final int index){
        Runnable task=new Runnable() {
            @Override
            public void run() {
                String result=null;
                if(!NetWorkHelper.isNetWorkAvailable(getContext())){
                    result=cache.getResult("TopFragment"+index);
                }else {
                    result= HttpUrlConnectUtil.getFormWebByHttpUrlConnect(getTopUrl(index),null);
                    cache.putResult("TopFragment"+index,result);
                }
                ArrayList<NewModel> arrayList= ReadListJson.getInstance(getContext()).getReadListNewModels(result, Url.TopId);
                Message message=new Message();
                message.what=1;
                message.obj=arrayList;
                //传递数据到主线程
                handler.sendMessage(message);
            }
        };
        return task;
    }

    private void intiListView(final ArrayList<NewModel> list) {
        adapter = new ListViewAdapter(getContext(), R.layout.test_listview, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewModel newModel = list.get(position - 1);
                DetailNewActivity.startActivity(getContext(), newModel);
                getActivity().overridePendingTransition(R.anim.base_slide_right_in,R.anim.base_slide_remain);
            }
        });
        listView.setLoadingListener(new SwipeListView.LoadingListener() {
            @Override
            public void loading() {
                page = page + 20;
                executor.execute(task(page));
            }
        });

        listView.setRefreshListener(new SwipeListView.RefreshListener() {
            @Override
            public void refreshing() {
                executor.execute(task(0));
                isRefresh=true;
            }
        });
    }


}
