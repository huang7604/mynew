package com.mynew.Sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mynew.Sql.dao.ItemDao;
import com.mynew.Sql.dao.TabDao;
import com.mynew.model.TabModel;

import java.util.ArrayList;

/**
 * Created by user on 2017/5/22.
 */
public class MyOpenHelper extends SQLiteOpenHelper {

    private final String sql="create table tab(id text primary key,"+
            "name text)";
    private final String sqlItem="create table item(id text primary key,"+
            "name text)";
    //private final String sqlOne="create table tab_name(id text primary key,name text)";

    public MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(sql);
        db.execSQL(sqlItem);
        TabDao.insertTab(db,getData());
        ItemDao.insertItem(db,getItem());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }

    private ArrayList<TabModel> getData(){
        ArrayList<TabModel> list=new ArrayList<>();
        list.add(new TabModel("TopFragment","头条"));
        list.add(new TabModel("KeJiFragment","科技"));
        list.add(new TabModel("CaiJingFragment","财经"));
        list.add(new TabModel("TiYuFragment","体育"));
        list.add(new TabModel("XiaoHuaFragment","笑话"));
        list.add(new TabModel("YuLeFragment","娱乐"));
        return list;
    }

    private ArrayList<TabModel> getItem(){
        ArrayList<TabModel> list=new ArrayList<>();
        list.add(new TabModel("BaoXueFragment","暴雪"));
        list.add(new TabModel("BoKeFragment","博客"));
        list.add(new TabModel("CaiJingFragment","财经"));
        list.add(new TabModel("CaiPiaoFragment","彩票"));
        list.add(new TabModel("CBAFragment","CBA"));
        list.add(new TabModel("DianTaiFragment","电台"));

        list.add(new TabModel("DianYingFragment","电影"));
        list.add(new TabModel("FootFragment","足球"));
        list.add(new TabModel("JiaJuFragment","家居"));
        list.add(new TabModel("JiaoYuFragment","教育"));
        list.add(new TabModel("JingXuanFragment","精选"));
        list.add(new TabModel("JunShiFragment","军事"));

        list.add(new TabModel("LunTanFragment","论坛"));
        list.add(new TabModel("LvYouFragment","旅游"));
        list.add(new TabModel("MsgFragment","消息"));
        list.add(new TabModel("NBAFragment","NBA"));
        list.add(new TabModel("QiCheFragment","汽车"));

        list.add(new TabModel("QingGanFragment","情感"));
        list.add(new TabModel("QinZiFragment","亲子"));
        list.add(new TabModel("SheHuiFragment","社会"));
        list.add(new TabModel("ShiShangFragment","时尚"));
        list.add(new TabModel("ShouJiFragment","手机"));
        list.add(new TabModel("ShuMaFragment","数码"));

        list.add(new TabModel("TiYuFragment","体育"));
        list.add(new TabModel("TopFragment","头条"));
        list.add(new TabModel("XiaoHuaFragment","笑话"));
        list.add(new TabModel("YiDongFragment","移动"));
        list.add(new TabModel("YouXiFragment","游戏"));
        list.add(new TabModel("YuLeFragment","娱乐"));
        return list;
    }
}
