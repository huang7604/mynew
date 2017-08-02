package com.mynew.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.mynew.R;

/**
 * Created by user on 2017/5/2.
 */
public class TwoBallRotationProgressBar extends View {
    //默认最大半径
    private static final int DEFAULT_MAX_RADIUS=15;
    //默认最小半径
    private static final int DEFAULT_MIN_RADIUS=5;
    //默认两个小球运行轨迹直径距离
    private final static int DEFAULT_DISTANCE = 20;
    //默认动画执行时间
    private final static int DEFAULT_ANIMATOR_DURATION = 2000;
    //默认第一个小球颜色
    private final static int DEFAULT_ONE_BALL_COLOR = Color.parseColor("#40df73");
    //默认第二个小球颜色
    private final static int DEFAULT_TWO_BALL_COLOR = Color.parseColor("#ffdf3e");
    //画笔
    private Paint paint;
    private float min_radius;
    private float max_radius;
    private float duration;
    private float distance;
    private float centerX;
    private float centerY;
    private Ball oneBall;
    private Ball twoBall;
    private int one_ball_color;
    private int two_ball_color;
    //动画集合
    private AnimatorSet animatorSet;

    public TwoBallRotationProgressBar(Context context){
        super(context);
    }

    public TwoBallRotationProgressBar(Context context, AttributeSet attrs){
        this(context,attrs,0);
    }

    public TwoBallRotationProgressBar(Context context,AttributeSet attrs,int defStyle){
        super(context,attrs,defStyle);
        //获取属性
        init(context,attrs);
    }

    @Override
    public void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        centerX=getWidth()/2;
        centerY=getHeight()/2;
        //设置X轴坐标
        //oneBall.setCenterX(centerX);
       // twoBall.setCenterX(centerX);
    }

    @Override
    public void onSizeChanged(int w,int h,int oldW,int oldH){
        super.onSizeChanged(w,h,oldW,oldH);
        centerX=w/2;
        centerY=h/2;
        //设置X轴坐标
        oneBall.setCenterX(centerX);
        twoBall.setCenterX(centerX);
    }

    @Override
    public void onDraw(Canvas canvas){
        //画两个小球，半径小的先画
        if(oneBall.getRadius()<twoBall.getRadius()){
            paint.setColor(oneBall.getColor());
            canvas.drawCircle(oneBall.getCenterX(),centerY,oneBall.getRadius(),paint);

            paint.setColor(twoBall.getColor());
            canvas.drawCircle(twoBall.getCenterX()+distance,centerY,twoBall.getRadius(),paint);
        }else {
            paint.setColor(twoBall.getColor());
            canvas.drawCircle(twoBall.getCenterX(),centerY,twoBall.getRadius(),paint);

            paint.setColor(oneBall.getColor());
            canvas.drawCircle(oneBall.getCenterX()+distance,centerY,oneBall.getRadius(),paint);
        }

    }

     //初始化
    private void init(Context context,AttributeSet attrs){
        //获取属性
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.TwoBallRotationProgressBar);
        min_radius=typedArray.getFloat(R.styleable.TwoBallRotationProgressBar_min_radius,DEFAULT_MIN_RADIUS);
        max_radius=typedArray.getFloat(R.styleable.TwoBallRotationProgressBar_max_radius,DEFAULT_MAX_RADIUS);
        one_ball_color=typedArray.getColor(R.styleable.TwoBallRotationProgressBar_one_ball_color,DEFAULT_ONE_BALL_COLOR);
        two_ball_color=typedArray.getColor(R.styleable.TwoBallRotationProgressBar_two_ball_color,DEFAULT_TWO_BALL_COLOR);
        duration=typedArray.getFloat(R.styleable.TwoBallRotationProgressBar_duration,DEFAULT_ANIMATOR_DURATION);
        distance=typedArray.getFloat(R.styleable.TwoBallRotationProgressBar_distance,DEFAULT_DISTANCE);
        //初始化画笔
        paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        //初始化圆球
        oneBall=new Ball();
        twoBall=new Ball();
        oneBall.setColor(one_ball_color);
        twoBall.setColor(two_ball_color);
        //小球
        oneBall.setRadius(min_radius);
        //大球
        twoBall.setRadius(max_radius);
        //动画配置
        configAnimator();
    }

    //初始化动画
    private void configAnimator(){
        //中间半径大小
        final float centerRadius=(max_radius+min_radius)/2;
        //第一个小球缩放动画，通过改变小球的半径
        //半径变化规律：中间大小->最大->中间大小->最小->中间大小
        ObjectAnimator oneRadiusAnimator=ObjectAnimator.ofFloat(oneBall,"radius",centerRadius,max_radius,centerRadius,min_radius,centerRadius);
        //无限循环
        oneRadiusAnimator.setRepeatCount(ValueAnimator.INFINITE);

        //第一个小球的位移动画
        ValueAnimator oneCenterXAnimator=ValueAnimator.ofFloat(-1,0,1,0,-1);
        oneCenterXAnimator.setRepeatCount(ValueAnimator.INFINITE);
        oneCenterXAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value=(float)animation.getAnimatedValue();
                float x=centerX+(distance)*value;
                oneBall.setCenterX(x);
                //不断的刷新VIEW
                invalidate();
            }
        });

        //第二个小球缩放动画
        //半径变化规律：中间大小->最小->中间大小->最大>中间大小
        ObjectAnimator twoRadiusAnimator=ObjectAnimator.ofFloat(twoBall,"radius",centerRadius,min_radius,centerRadius,max_radius,centerRadius);
        //无限循环
        twoRadiusAnimator.setRepeatCount(ValueAnimator.INFINITE);
        //第二个小球位移动画
        ValueAnimator twoCenterXAnimator=ValueAnimator.ofFloat(1,0,-1,0,1);
        twoCenterXAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value=(float)animation.getAnimatedValue();
                float x=centerX+distance*value;
                twoBall.setCenterX(x);
                //不断的刷新VIEW
                invalidate();
            }
        });


        //属性动画集合
        animatorSet=new AnimatorSet();
        //属性动画执行
        animatorSet.playTogether(oneRadiusAnimator,oneCenterXAnimator,twoRadiusAnimator,twoCenterXAnimator);
        //属性动画执行时间
        animatorSet.setDuration((int)duration);
        //时间插值器，这里表示动画开始最快，结尾最慢
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.start();

    }

    public void stop(){
        animatorSet.end();
    }
    public void start(){
        animatorSet.start();
    }

    public class Ball{

        private float radius;
        private float centerX;
        private int color;
        public Ball(){

        }

        public void setRadius(float radius) {
            this.radius = radius;
        }

        public float getRadius() {
            return radius;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public int getColor() {
            return color;
        }

        public void setCenterX(float centerX) {
            this.centerX = centerX;
        }

        public float getCenterX() {
            return centerX;
        }
    }

}
