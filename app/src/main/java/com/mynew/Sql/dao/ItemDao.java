package com.mynew.Sql.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mynew.model.TabModel;

import java.util.ArrayList;

/**
 * Created by user on 2017/5/24.
 */
public class ItemDao {
    public static ArrayList<TabModel> selectItemAll(SQLiteDatabase db){
        ArrayList<TabModel> list=new ArrayList<>();
        Cursor cursor=db.rawQuery("select * from item",null);
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

    public static void insertItem(SQLiteDatabase db,ArrayList<TabModel> list){
        if(list!=null&&list.size()!=0){
            for (TabModel tabModel:list){
                ContentValues values=new ContentValues();
                values.put("id",tabModel.getId());
                values.put("name",tabModel.getName());
                db.insert("item",null,values);
            }
        }
    }
}
