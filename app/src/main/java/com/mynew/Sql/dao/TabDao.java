package com.mynew.Sql.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mynew.model.TabModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 2017/5/22.
 */
public class TabDao {
    public static ArrayList<TabModel> selectTabAll(SQLiteDatabase db){
        ArrayList<TabModel> list=new ArrayList<>();
        Cursor cursor=db.rawQuery("select * from tab",null);
        if(cursor.moveToFirst()){
            do{
                TabModel tabModel=new TabModel();
                tabModel.setId(cursor.getString(cursor.getColumnIndex("id")));
                tabModel.setName(cursor.getString(cursor.getColumnIndex("name")));
                list.add(tabModel);
            }while(cursor.moveToNext());
        }
        return list;
    }

    public static void insertTab(SQLiteDatabase db,ArrayList<TabModel> list){
        if(list!=null&&list.size()!=0){
            for (TabModel tabModel:list){
                ContentValues values=new ContentValues();
                values.put("id",tabModel.getId());
                values.put("name",tabModel.getName());
                db.insert("tab",null,values);
            }
        }
    }

    public static void deleteTabAll(SQLiteDatabase db){
        db.execSQL("delete from tab");
    }
}
