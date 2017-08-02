package com.mynew.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.mynew.model.TabModel;

import java.util.ArrayList;

/**
 * Created by user on 2017/5/17.
 */
public class FragmentAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments;
    private FragmentManager manager;
    private ArrayList<TabModel> list;
    public FragmentAdapter(FragmentManager manager, ArrayList<Fragment> fragments, ArrayList<TabModel> list){
        super(manager);
        this.fragments=fragments;
        this.manager=manager;
        this.list=list;
    }


    @Override
    public int getCount(){
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position){
        return fragments.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //如果注释这行，那么不管怎么切换，page都不会被销毁
        //super.destroyItem(container, position, object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public long getItemId(int position) {
        TabModel tabModel=list.get(position);
        long id=tabModel.getId().hashCode();
        return  id;
    }



}
