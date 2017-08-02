package com.mynew.view.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by user on 2017/5/24.
 */
public class OtherGridView extends GridView {

    public OtherGridView(Context context){
        super(context);
    }

    public OtherGridView(Context context, AttributeSet attrs){
        super(context,attrs);
    }

    public OtherGridView(Context context,AttributeSet attrs,int defStyle){
        super(context,attrs,defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        int expandSpec=MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >>2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec,expandSpec);
    }
}
