package com.mynew.view.viewgroup;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * Created by user on 2017/4/21.
 */
public class SlidingFinishLayout extends RelativeLayout{

    private static final String TAG="SlidingFinishLayout";
    //当前RelativeLayout的父View
    private ViewGroup parentView;
    //处理滑动逻辑的view
    private View touchView;
    //当前View的宽度
    private int viewWidth;
    //触摸按下时X的坐标
    private int dowX;
    //触摸按下时y的坐标
    private int dowY;
    //触摸移动的距离
    private int deltaX;
    //滑动处理控件
    private Scroller mScroller;
    //触摸的最小距离
    private int mTouchSlop;
    //是否结束活动
    private boolean isFinish=false;
    private boolean isSliding=false;
    //事件监听
    private SlidingFinishLayout.SlidingFinishListener slidingFinishListener;

    public SlidingFinishLayout(Context context){
        super(context);
    }

    public SlidingFinishLayout(Context context, AttributeSet attrs){
        this(context,attrs,0);
    }

    public SlidingFinishLayout(Context context,AttributeSet attrs,int defType){
        super(context,attrs,defType);
        mScroller=new Scroller(context);
        mTouchSlop= ViewConfiguration.get(context).getScaledTouchSlop();
        Log.d(TAG,mTouchSlop+"");
    }

    @Override
    public void onLayout(boolean change,int l,int t,int r,int d){
        super.onLayout(change,l,t,r,d);
        if(change){
            viewWidth=this.getWidth();
            parentView=(ViewGroup) this.getParent();
        }

    }

    //滚动处界面
    public void scrollRight(){
        final int delta=viewWidth+parentView.getScrollX();
        //设置滚动参数，调用computeScroll
        mScroller.startScroll(parentView.getScrollX(),0,(-delta+1),0,Math.abs(delta));
        //刷新界面
        postInvalidate();
    }

    //滚动到初始位置
    public void scrollOrigin(){
        final int delta=parentView.getScrollX();
        //设置滚动参数，调用computeScroll
        mScroller.startScroll(parentView.getScrollX(),0,-delta,0,Math.abs(delta));
        postInvalidate();
    }

    //拦截触摸事件
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                dowX = (int) ev.getRawX();
                dowY = (int) ev.getRawY();
                if(isSliding){
                    return true;
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int currentX = (int) ev.getRawX();
                int currentY = (int) ev.getRawY();
                deltaX = dowX - currentX;
                int deltaY = dowY - currentY;
                //X轴移动距离大于最小移动距离与y轴移动距离小于最小距离时，当前viewGround在OnTouchEven消耗此事件否则传递改子控件
                if (Math.abs(deltaX) > mTouchSlop && Math.abs(deltaY) < mTouchSlop) {
                    if (currentX - dowX >= 0) {

                        isSliding=true;
                        // 则当手指滑动，如果子控件是ListView,则取消item的点击事件，不然我们滑动也伴随着item点击事件的发生
                        for(int i=0;i<getChildCount();i++) {
                            if (getChildAt(i) instanceof ListView) {
                                MotionEvent cancelEvent = MotionEvent.obtain(ev);
                                cancelEvent.setAction(MotionEvent.ACTION_CANCEL | (ev.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
                                getChildAt(i).onTouchEvent(cancelEvent);
                            }
                        }
                        return true;
                    }
                }

                if (isSliding&&currentX - dowX >= 0){
                    parentView.scrollTo(deltaX, 0);
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                if(isSliding) {
                    if ((-deltaX) > viewWidth / 2) {
                        scrollRight();
                        isFinish = true;
                        return true;

                    } else {
                        isFinish = false;
                        scrollOrigin();
                        isSliding = false;
                        return true;
                    }
                }
                break;
            }
        }

        if(isSliding){
            return true;
        }else {
            return super.dispatchTouchEvent(ev);
        }

    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        // 调用startScroll的时候scroller.computeScrollOffset()返回true，
        if (mScroller.computeScrollOffset()) {
            parentView.scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
            if (mScroller.isFinished()) {
                if (slidingFinishListener!= null &&isFinish) {
                    slidingFinishListener.onSlidingFinish();
                }
            }
        }
    }

    public interface SlidingFinishListener{
        public void onSlidingFinish();
    }

    public void setSlidingFinishListener(SlidingFinishListener slidingFinishListener){
        this.slidingFinishListener=slidingFinishListener;
    }



}
