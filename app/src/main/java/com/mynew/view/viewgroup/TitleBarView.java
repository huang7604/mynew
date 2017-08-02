package com.mynew.view.viewgroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mynew.R;

/**
 * Created by user on 2017/4/20.
 */
public class TitleBarView extends RelativeLayout {

    private Button leftButton;
    private Button rightButton;
    private TextView title;
    public TitleBarView(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        LayoutInflater.from(context).inflate(R.layout.title,this,true);
        leftButton=(Button) findViewById(R.id.title_bar_left);
        title=(TextView)findViewById(R.id.title_bar_title);
        rightButton=(Button)findViewById(R.id.title_bar_right);
        initAttribute(context,attributeSet);
    }

    private void initAttribute(Context context,AttributeSet attrs){
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.TitleBarView);
        if(typedArray!=null){
            //设置标题背景
            int title_background_color=typedArray.getResourceId(R.styleable.TitleBarView_title_background_color,Color.RED);
            setBackgroundColor(title_background_color);
            //设置左按钮
            boolean left_button_visible=typedArray.getBoolean(R.styleable.TitleBarView_left_button_visible,true);
            if(left_button_visible){
                leftButton.setVisibility(VISIBLE);
                String button_text=typedArray.getString(R.styleable.TitleBarView_left_button_text);
                if(!TextUtils.isEmpty(button_text)){
                    int button_text_color=typedArray.getColor(R.styleable.TitleBarView_left_button_text_color, Color.WHITE);
                    leftButton.setText(button_text);
                    leftButton.setTextColor(button_text_color);
                }else {
                    int button_left_drawable=typedArray.getResourceId(R.styleable.TitleBarView_left_button_text_drawable,R.drawable.title_bar_left_default);
                    leftButton.setBackgroundResource(button_left_drawable);
                }
            }else {
                leftButton.setVisibility(INVISIBLE);
            }
            //设置右按钮
            boolean right_button_visible=typedArray.getBoolean(R.styleable.TitleBarView_right_button_visible,true);
            if(right_button_visible){
                rightButton.setVisibility(VISIBLE);
                String button_text=typedArray.getString(R.styleable.TitleBarView_right_button_text);
                if(!TextUtils.isEmpty(button_text)){
                    int button_text_color=typedArray.getColor(R.styleable.TitleBarView_right_button_text_color,Color.WHITE);
                    rightButton.setText(button_text);
                    rightButton.setTextColor(button_text_color);
                }else {
                    int button_right_drawable=typedArray.getResourceId(R.styleable.TitleBarView_right_button_text_drawable,R.drawable.title_bar_right_default);
                    rightButton.setBackgroundResource(button_right_drawable);
                }
            }else {
                rightButton.setVisibility(INVISIBLE);
            }
            String title_text=typedArray.getString(R.styleable.TitleBarView_title_text);
            if(!TextUtils.isEmpty(title_text)){
                int title_text_color=typedArray.getColor(R.styleable.TitleBarView_title_text_color,Color.WHITE);
                title.setText(title_text);
                title.setTextColor(title_text_color);
            }else{
                int title_text_drawable=typedArray.getResourceId(R.styleable.TitleBarView_title_text_drawable,-1);
                if(title_text_drawable!=-1) {
                    title.setBackgroundResource(title_text_drawable);
                }
            }
            typedArray.recycle();
        }
    }

    public void setOnClickListener(OnClickListener onClickListener){
        if(onClickListener!=null){
            leftButton.setOnClickListener(onClickListener);
            rightButton.setOnClickListener(onClickListener);
        }
    }

    public Button getLeftButton(){
        return leftButton;
    }

    public Button getRightButton(){
        return rightButton;
    }

    public TextView getTitle(){
        return title;
    }

}
