package com.mynew.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mynew.R;
import com.mynew.model.TabModel;

import java.util.ArrayList;

/**
 * Created by user on 2017/5/24.
 */
public class OtherGridAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<TabModel> list;
    private boolean isVisible=true;
    private int remove_position=-1;
    public OtherGridAdapter(Context context, ArrayList<TabModel> list){
        this.context=context;
        this.list=list;
    }

    @Override
    public int getCount(){
        if (list!=null){
            return list.size();
        }else {
            return 0;
        }
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
        View view= LayoutInflater.from(context).inflate(R.layout.grid_view_text,null);
        TextView textView=(TextView)view.findViewById(R.id.grid_text);
        textView.setText(list.get(position).getName());
        if(!isVisible&&position==list.size()-1){
            textView.setText("");
        }

        if(remove_position!=-1&&remove_position==position){
            textView.setText("");
        }
        return view;
    }

    public boolean isVisible(){
        return isVisible;
    }
    public void setVisible(boolean isVisible){
        this.isVisible=isVisible;
    }

    public void addItem(TabModel tabModel){
        list.add(tabModel);
        notifyDataSetChanged();
    }

    public void setRemove(int remove_position){
        this.remove_position=remove_position;
        notifyDataSetChanged();
    }

    public void Remove(){
        if(remove_position!=-1) {
            list.remove(remove_position);
            remove_position=-1;
            notifyDataSetChanged();
        }
    }

}
