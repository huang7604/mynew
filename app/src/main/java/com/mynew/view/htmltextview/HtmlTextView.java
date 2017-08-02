package com.mynew.view.htmltextview;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by user on 2017/4/30.
 */
public class HtmlTextView extends TextView {

    public HtmlTextView(Context context){
        super(context);
    }







































































    public HtmlTextView(Context context, AttributeSet attrs){
        super(context,attrs);
    }

    public HtmlTextView(Context context,AttributeSet attrs,int defStyle){
        super(context,attrs,defStyle);
    }

    public void setHtmlFromString(String source,boolean useLocalImage){
        Html.ImageGetter imageGetter=null;
        if(useLocalImage){
            imageGetter=new LocalImageGetter(getContext());
        }else {
            imageGetter=new UrlImageGetter(getContext(),this);
        }
        setMovementMethod(LinkMovementMethod.getInstance());  //滚动
        setText(Html.fromHtml(source,imageGetter,new HtmlTagHandle())); //设置html文本

    }
}
