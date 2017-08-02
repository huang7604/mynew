package com.mynew.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.mynew.App;
import com.mynew.R;
import com.mynew.Sql.MyOpenHelper;
import com.mynew.Sql.dao.TabDao;
import com.mynew.adapter.FragmentAdapter;
import com.mynew.model.TabModel;
import com.mynew.view.viewgroup.TitleBarView;

import java.util.ArrayList;
import java.util.concurrent.Executor;


public class MainActivity extends FragmentActivity {

    private ViewPager viewPager=null;
    private ArrayList<Fragment> fragments;
    private FragmentAdapter adapter;
    private LinearLayout linearLayout;
    private ArrayList<TextView> textViews;
    private ArrayList<View> views;
    private HorizontalScrollView scrollView;
    private SQLiteDatabase db;
    private ArrayList<TabModel> list;
    private Button addTabButton;
    private DrawerLayout drawerLayout;
    private TitleBarView titleBarView;
    private int index=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db= App.getApp().getSQLiteOpenHelper(this).getReadableDatabase();
        list= TabDao.selectTabAll(db);
        init();
    }

    private void init(){
        viewPager=(ViewPager)findViewById(R.id.viewPage);
        linearLayout=(LinearLayout)findViewById(R.id.scrollView_layout);
        scrollView=(HorizontalScrollView)findViewById(R.id.scrollView);
        addTabButton=(Button)findViewById(R.id.add_tab_button) ;
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        titleBarView=(TitleBarView)findViewById(R.id.title_bar_view);
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        final int width=dm.widthPixels;
        textViews=new ArrayList<>();
        fragments=new ArrayList<>();
        views=new ArrayList<>();
        addTextView(list);
        addFragment(list);
        adapter=new FragmentAdapter(getSupportFragmentManager(),fragments,list);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //Toast.makeText(MainActivity.this,position+"",Toast.LENGTH_LONG).show();
                TextView textView=textViews.get(position);
                textView.setBackgroundColor(getColor(R.color.textBackGroundColor));
                textView.setTextColor(Color.WHITE);

                TextView textView1=textViews.get(index);
                textView1.setBackgroundColor(getColor(R.color.ScrollBackGround));
                textView1.setTextColor(Color.BLACK);
                //Toast.makeText(MainActivity.this,width+"",Toast.LENGTH_SHORT).show();
                int offset=width/textViews.size();
                if(position>4&&position>index) {
                    scrollView.scrollTo(position * offset, 0);
                }else if(position<=4&&position<index){
                    scrollView.scrollTo(0, 0);
                }
                index=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        addTabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTabActivity.startActivity(MainActivity.this,list);
                overridePendingTransition(R.anim.base_slide_right_in,R.anim.base_slide_remain);
            }
        });

        titleBarView.getLeftButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!drawerLayout.isDrawerOpen(Gravity.LEFT)){
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });


    }

    private void addTextView(ArrayList<TabModel> list){
        if(list!=null&&list.size()!=0){
            for (int i=0;i<list.size();i++){
                TabModel tabModel=list.get(i);
                if (index==i){
                    TextView topTextView=createTextView(tabModel.getName(),i);
                    topTextView.setBackgroundColor(getColor(R.color.textBackGroundColor));
                    topTextView.setTextColor(Color.WHITE);
                    textViews.add(topTextView);
                }else {
                    textViews.add(createTextView(tabModel.getName(),i));
                }
            }
        }
    }

    private void addFragment(ArrayList<TabModel> list){
        try {
            if (list != null && list.size() != 0) {
                int i=0;
                for (TabModel tabModel : list) {
                    Class classes = Class.forName("com.mynew.fragment."+tabModel.getId());
                    fragments.add(i,(Fragment)classes.newInstance());
                    i++;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private TextView createTextView(String name,int tags){
        View view=LayoutInflater.from(this).inflate(R.layout.scroll_text_tiew, null);
        final TextView textView=(TextView)view.findViewById(R.id.textView) ;
        textView.setText(name);
        textView.setTag(tags);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem((int)textView.getTag());
            }
        });
        linearLayout.addView(view,tags);
        views.add(tags,view);
        return textView;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent date){
        ArrayList<TabModel> arrayList=TabDao.selectTabAll(db);
        for (View view:views){
            linearLayout.removeView(view);
        }
        views.clear();
        textViews.clear();
        fragments.clear();
        list.clear();
        for (TabModel tabModel:arrayList){
            list.add(tabModel);
        }
        index=0;
        addFragment(arrayList);
        addTextView(arrayList);
        adapter.notifyDataSetChanged();
        viewPager.setCurrentItem(0);

    }





}
