package com.mynew.activity;

import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mynew.App;
import com.mynew.R;
import com.mynew.Sql.MyOpenHelper;
import com.mynew.Sql.dao.ItemDao;
import com.mynew.Sql.dao.TabDao;
import com.mynew.adapter.DragGridAdapter;
import com.mynew.adapter.OtherGridAdapter;
import com.mynew.model.NewModel;
import com.mynew.model.TabModel;
import com.mynew.view.viewgroup.DragGridView;
import com.mynew.view.viewgroup.OtherGridView;
import com.mynew.view.viewgroup.SlidingFinishLayout;
import com.mynew.view.viewgroup.TitleBarView;

import java.util.ArrayList;

/**
 * Created by user on 2017/5/22.
 */
public class AddTabActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private RelativeLayout relativeLayout;
    private TitleBarView titleBarView;
    private DragGridView dragGrid=null;
    private GridView otherGrid=null;
    private DragGridAdapter dragGridAdapter=null;
    private OtherGridAdapter otherGridAdapter=null;
    private ArrayList<TabModel> dragList=null;
    private ArrayList<TabModel> otherList=null;
    private SQLiteDatabase db=null;
    private boolean isMove=true;
    @Override
    protected void onCreate(Bundle onInstanceState){
        super.onCreate(onInstanceState);
        setContentView(R.layout.activity_addtab);
        relativeLayout=(RelativeLayout) findViewById(R.id.add_tab_slidingLayout);
        titleBarView=(TitleBarView)findViewById(R.id.add_tab_titleBarView);
        dragGrid=(DragGridView) findViewById(R.id.dragGridView);
        otherGrid=(GridView)findViewById(R.id.otherGridView);
        init();

    }

    private void init(){
        db= App.getApp().getSQLiteOpenHelper(this).getReadableDatabase();
        dragList=(ArrayList<TabModel>) getIntent().getSerializableExtra("list");
        otherList= ItemDao.selectItemAll(db);
        for(TabModel tabModel:dragList){
            otherList.remove(tabModel);
        }
        dragGridAdapter=new DragGridAdapter(this,dragList);
        otherGridAdapter=new OtherGridAdapter(this,otherList);
        dragGrid.setOnItemClickListener(this);
        otherGrid.setOnItemClickListener(this);
        titleBarView.getLeftButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack();
            }
        });
        dragGrid.setAdapter(dragGridAdapter);
        dragGrid.setSelector(new ColorDrawable(Color.TRANSPARENT));
        otherGrid.setAdapter(otherGridAdapter);
        otherGrid.setSelector(new ColorDrawable(Color.TRANSPARENT));

    }

    @Override
    public void onItemClick(AdapterView<?> parent,View view, final int position,long id) {
        if(!isMove){
            return;
        }

        switch (parent.getId()) {
            case R.id.dragGridView: {
                if (position != 0&&position != 1) {
                    final ImageView moveImageView = getView(view);
                    if (moveImageView != null) {
                        TextView textView = (TextView) view.findViewById(R.id.grid_text);
                        final int[] startLocation = new int[2];
                        textView.getLocationInWindow(startLocation);
                        final TabModel tabModel = (TabModel) parent.getAdapter().getItem(position);
                        otherGridAdapter.setVisible(false);
                        otherGridAdapter.addItem(tabModel);
                         new Handler().postDelayed(new Runnable() {
                             @Override
                             public void run() {
                                 int[] endLocation=new int[2];
                                 otherGrid.getChildAt(otherGrid.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                 dragGridAdapter.setRemove(position);
                                 MoveAnim(moveImageView,startLocation,endLocation,tabModel,dragGrid);
                             }
                         }, 50L);
                    }

                }
                break;
            }
            case R.id.otherGridView:{
                final ImageView moveImageView=getView(view);
                final int[] startLocation=new int[2];
                view.getLocationInWindow(startLocation);
                final TabModel tabModel=(TabModel)parent.getAdapter().getItem(position);
                dragGridAdapter.setVisible(false);
                dragGridAdapter.addItem(tabModel);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int[] endLocation=new int[2];
                        dragGrid.getChildAt(dragGrid.getLastVisiblePosition()).getLocationInWindow(endLocation);
                        otherGridAdapter.setRemove(position);
                        MoveAnim(moveImageView,startLocation,endLocation,tabModel,otherGrid);
                    }
                }, 50L);
                break;
            }
        }
    }


    /**
     * 获取点击的Item的对应View，
     * @param view
     * @return
     */
    private ImageView getView(View view){
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        Bitmap cache=Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        ImageView imageView=new ImageView(this);
        imageView.setImageBitmap(cache);
        return imageView;
    }

    /**
     * 创建移动的ITEM对应的ViewGroup布局容器
     */
    private ViewGroup getMoveViewGround(){
        ViewGroup viewGroup=(ViewGroup)getWindow().getDecorView();
        LinearLayout linearLayout=new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        viewGroup.addView(linearLayout);
        return linearLayout;
    }

    /**
     * 获取移动的VIEW，放入对应ViewGroup布局容器
     * @param viewGroup
     * @param view
     * @param initLocation
     * @return
     */
    private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
        int x = initLocation[0];
        int y = initLocation[1];
        viewGroup.addView(view);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin=x;
        layoutParams.topMargin=y;
        view.setLayoutParams(layoutParams);
        return view;
    }

    /**
     * 点击ITEM移动动画
     * @param moveView
     * @param startLocation
     * @param endLocation
     * @param tabModel
     * @param clickGridView
     */
    private void MoveAnim(final View moveView, int[] startLocation, int[] endLocation, final TabModel tabModel, final GridView clickGridView) {
        int[] initLocation=new int[2];
        moveView.getLocationInWindow(initLocation);
        final ViewGroup moveViewGroup=getMoveViewGround();
        final View mMoveView=getMoveView(moveViewGroup,moveView,initLocation);
        //创建移动动画
        TranslateAnimation moveAnimation=new TranslateAnimation(startLocation[0],endLocation[0],startLocation[1],endLocation[1]);
        moveAnimation.setDuration(300);//动画时间
        //动画配置
        AnimationSet animationSet=new AnimationSet(true);
        animationSet.setFillAfter(false);
        animationSet.addAnimation(moveAnimation);
        mMoveView.startAnimation(animationSet);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                 isMove=false;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isMove=true;
                moveViewGroup.removeView(mMoveView);
                if(clickGridView instanceof DragGridView){
                    otherGridAdapter.setVisible(true);
                    otherGridAdapter.notifyDataSetChanged();
                    dragGridAdapter.Remove();
                }else if(clickGridView instanceof OtherGridView){
                    dragGridAdapter.setVisible(true);
                    dragGridAdapter.notifyDataSetChanged();
                    otherGridAdapter.Remove();
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();
        onBack();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event){
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        return super.onTouchEvent(event);
    }

    private void onBack(){
        TabDao.deleteTabAll(db);
        TabDao.insertTab(db,dragList);
        Intent intent=new Intent();
        setResult(RESULT_OK,intent);
        finish();
        overridePendingTransition(0,R.anim.base_slide_right_out);
    }

    public static void startActivity(Activity activity, ArrayList<TabModel> list){
        Intent intent=new Intent(activity,AddTabActivity.class);
        intent.putExtra("list",list);
        activity.startActivityForResult(intent,1);
    }

}
