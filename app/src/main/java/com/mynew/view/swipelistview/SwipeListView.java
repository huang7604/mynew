package com.mynew.view.swipelistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mynew.R;
import com.mynew.view.TwoBallRotationProgressBar;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by user on 2017/5/8.
 * @author huangjunjie
 * @version 1.0
 * @desc 自定义Listview　下拉刷新,上拉加载更多
*/
public class SwipeListView extends ListView implements AbsListView.OnScrollListener{

    private final static int NONE=1;
    private final static int PULL=2;
    private final static int REFRESH=3;
    private final static int REFRESHING=4;

    //下拉状态
    private int status=1;
    private View headView;
    private View footView;
    //滑动最小距离
    private int mTouchSlop;
    //按下的坐标
    private float downY;
    private float downX;
    //滑动距离
    private float detail;

    private TextView textRefresh;
    private TextView textTime;
    private LinearLayout linearLayout;
    private TwoBallRotationProgressBar twoBallRotationProgressBar;
    //箭头
    private ImageView arrow;
    //箭头特效
    private RotateAnimation animation;
    private RotateAnimation reverseAnimation;
    //是否滑动
    private boolean isSliding=false;
    //第一个显示的标号
    private int firstVisibleItem;
    //显示Item数
    private int visibleItemCount;
    private int totalItemCount;
    //滚动状态
    private int scrollState;
    //headerView的高度
    private int height;
    //加载和刷新的监听
    private LoadingListener loadingListener;
    private RefreshListener refreshListener;
    //是否加载
    private boolean isLoading=true;
    private Context context;
    private int tag=0;
    public SwipeListView(Context context){
        super(context);
        init(context);
    }

    public SwipeListView(Context context, AttributeSet attrs){
        super(context,attrs);
        init(context);
    }

    public SwipeListView(Context context,AttributeSet attrs,int defStyle){
        super(context,attrs,defStyle);
        init(context);
    }

    //初始化
    private void init(Context context){
        this.context=context;
        //头部
        headView= LayoutInflater.from(context).inflate(R.layout.listview_header,null);
        //尾部
        footView=LayoutInflater.from(context).inflate(R.layout.listview_bottom,null);
        footView.setVisibility(INVISIBLE);
        //标签
        textRefresh=(TextView) headView.findViewById(R.id.listView_header_textRefresh);
        textTime=(TextView)headView.findViewById(R.id.listView_header_textTime);
        linearLayout=(LinearLayout)headView.findViewById(R.id.layout);
        twoBallRotationProgressBar=(TwoBallRotationProgressBar)headView.findViewById(R.id.listView_header_progressBar);
        //箭头
        arrow=(ImageView) headView.findViewById(R.id.arrow);
        //添加头部和底部
        addHeaderView(headView);
        addFooterView(footView);
        //测量headView的高度
        measure(headView);
        height=headView.getMeasuredHeight();
        setHeadViewPadding(-height);
        //最短滑动距离
        mTouchSlop= ViewConfiguration.get(context).getScaledTouchSlop();
        //箭头特效
        animation = new RotateAnimation(0, -180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(100);
        animation.setFillAfter(true);

        reverseAnimation = new RotateAnimation(-180, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        reverseAnimation.setInterpolator(new LinearInterpolator());
        reverseAnimation.setDuration(100);
        reverseAnimation.setFillAfter(true);
        setOnScrollListener(this);
    }

    //测量Header
    private void measure(View childView){
        ViewGroup.LayoutParams lp=childView.getLayoutParams();
        if(lp==null){
            lp= new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        int childWidthSpec=ViewGroup.getChildMeasureSpec(0,0,lp.width);
        int lpHeight=lp.height;
        int childHeightSpec;
        if(lpHeight>0){
            childHeightSpec=MeasureSpec.makeMeasureSpec(lpHeight,MeasureSpec.EXACTLY);
        }else {
            childHeightSpec=MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED);
        }
        childView.measure(childWidthSpec,childHeightSpec);
    }

    //设置padding
    private void setHeadViewPadding(int heightPadding){
        headView.setPadding(headView.getPaddingLeft(),headView.getPaddingTop(),headView.getPaddingRight(),heightPadding);
        headView.invalidate();
    }

   @Override
    public boolean onTouchEvent(MotionEvent event){

       //根据手势判断
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:{
                downY=event.getY();
                downX=event.getX();
                if (status==REFRESHING){
                    //setHeadViewPadding(-150);
                }
                status=PULL;
                refresh();
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                float moveX=event.getX();
                float moveY=event.getY();
                detail=downY-moveY;
                if(Math.abs((downX-moveX))<mTouchSlop&&Math.abs(detail)>mTouchSlop){
                    if(firstVisibleItem==0&&detail<0) {
                        if (scrollState==SCROLL_STATE_TOUCH_SCROLL) {
                            isSliding = false;
                        }else if(SCROLL_STATE_IDLE==scrollState){
                            isSliding=true;
                        }
                    }
                  }
                if(isSliding){
                    setHeadViewPadding(-height-(int)(detail/3));
                    if(height>=Math.abs((int)(detail/3))){
                        status=PULL;
                        refresh();
                    }else {
                        status=REFRESH;
                        refresh();
                    }
                    MotionEvent cancelEvent=MotionEvent.obtain(event);
                    cancelEvent.setAction(MotionEvent.ACTION_CANCEL);
                    onTouchEvent(cancelEvent);
                    return true;
                 }

               break;
            }
            case MotionEvent.ACTION_UP:{
                if(status==PULL){
                    status=NONE;
                    refresh();
                }else if(status==REFRESH){
                    status=REFRESHING;
                    refresh();
                }
                isSliding=false;
            }
        }

       return super.onTouchEvent(event);
    }

    //根据状态刷新
    private void refresh() {
        switch (status) {
            case NONE: {
                twoBallRotationProgressBar.stop();
                twoBallRotationProgressBar.setVisibility(GONE);
                linearLayout.setVisibility(VISIBLE);
                //measure(headView);
                //height=headView.getMeasuredHeight();
                setHeadViewPadding(-height);
                break;
            }
            case PULL: {
                if (arrow.getAnimation() != reverseAnimation) {
                    twoBallRotationProgressBar.stop();
                    twoBallRotationProgressBar.setVisibility(GONE);
                    linearLayout.setVisibility(VISIBLE);
                    textRefresh.setText("下拉刷新");
                    arrow.setAnimation(reverseAnimation);
                }

                break;
            }
            case REFRESH: {
                arrow.setAnimation(animation);
                textRefresh.setText("松开刷新");
                //测量headerView
                measure(headView);
                height = headView.getMeasuredHeight();

            break;
        }
        case REFRESHING: {
            linearLayout.setVisibility(GONE);
            setHeadViewPadding(0);
            twoBallRotationProgressBar.setVisibility(VISIBLE);
            twoBallRotationProgressBar.start();
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            textTime.setText("上次更新时间:" + dateFormat.format(new Date()));
            textTime.setVisibility(VISIBLE);
            //测量headerView
            measure(headView);
            height = headView.getMeasuredHeight();
            refreshListener.refreshing();
            break;
        }

        }
    }


    @Override
    public void onScrollStateChanged(AbsListView listView,int scrollState){
        this.scrollState=scrollState;
        if((firstVisibleItem+visibleItemCount)==totalItemCount&&scrollState==SCROLL_STATE_IDLE){
            footView.setVisibility(VISIBLE);
            if(loadingListener!=null&&isLoading) {
                loadingListener.loading();
                isLoading=false;
            }
        }
    }

    @Override
    public void onScroll(AbsListView listView,int firstVisibleItem,int visibleItemCount,int totalItemCount){
        this.firstVisibleItem=firstVisibleItem;
        this.visibleItemCount=visibleItemCount;
        this.totalItemCount=totalItemCount;

    }

    //设置加载监听
    public void setLoadingListener(LoadingListener loadingListener){
        this.loadingListener=loadingListener;
    }
    //设置刷新监听
    public void setRefreshListener(RefreshListener refreshListener){
        this.refreshListener=refreshListener;
    }

    public interface LoadingListener{
         void loading();
    }

    public interface RefreshListener{
        void refreshing();
    }

    //回调函数,加载完毕，进度条隐藏
    public void loadingCallBack(){
        footView.setVisibility(INVISIBLE);
        isLoading=true;
    }
    //刷新完毕，状态改变
    public void RefreshCallBack(){
        status=NONE;
        refresh();
    }

}
