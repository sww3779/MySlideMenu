package com.sww.mySlidingMenu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by sww on 2015/10/15.
 */
public class SlideMenu extends ViewGroup {

    private int downX;
    private final int SCREEN_MENU=0;
    private final int SCREEN_MAIN=1;

    private int currentScreen=SCREEN_MAIN;
    private Scroller scroller;

    public SlideMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }



    public SlideMenu(Context context) {
        super(context);
        init();
    }
    private void init() {
        scroller=new Scroller(getContext());
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //测量菜单的宽和高。宽：240dip,高：填充屏幕
        View menuView = getChildAt(0);//获取菜单对象
        menuView.measure(menuView.getLayoutParams().width, heightMeasureSpec);
        //测量主界面的宽和高，宽和高都是填充屏幕
        View mainView = getChildAt(1);
        mainView.measure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * int l 左边=0
     * int t 上边=0
     * int r 右边=屏幕的宽度
     * int b 下边=屏幕的高度
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //主菜单的位置在屏幕左上角
        View mainView = getChildAt(1);
        mainView.layout(l, t, r, b);

        //菜单在屏幕的左侧
        View menuView = getChildAt(0);
        //左上右下
        menuView.layout(-menuView.getMeasuredWidth(), t, 0, b);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int scrollX;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX= (int) event.getX();
                int deltaX=downX-moveX;//计算偏移量
                 scrollX=getScrollX()+deltaX;
                if(scrollX<-getChildAt(0).getMeasuredWidth()){
                //当前超出了左边界，应该设置为在菜单的左边界位置上
                    scrollTo(-getChildAt(0).getMeasuredWidth(),0);
                }else if(scrollX>0){
                    scrollTo(0,0);
                }else{
                    scrollBy(deltaX,0);
                }
                downX=moveX;//重新赋值以便下次事件处理

                break;
            case MotionEvent.ACTION_UP:
                //获得菜单宽度的一半
                int center=-getChildAt(0).getMeasuredWidth()/2;
                scrollX=getScrollX();//当前屏幕左上角的值
                if(scrollX>center){
                currentScreen=SCREEN_MAIN;
                }else{
                    currentScreen=SCREEN_MENU;
                }
                switchScreen();
                break;
            default:
                break;

        }


        return true;
    }


    @Override
    public void computeScroll() {
        // 当scroller数据模拟完毕时, 不应该继续进行递归
        // 反之, 如果正在模拟数据才进行递归的操作

        if(scroller.computeScrollOffset()) {// 当前还是正在模拟数据中
            // 把当前scroller正在模拟的数据取出来, 使用scrollTo方法切换屏幕
            int currX = scroller.getCurrX();
            scrollTo(currX, 0);
            invalidate(); // 在触发当前方法, 相当于递归.
        }
    }

    //根据currentScreen变量来切换屏幕显示
    private void switchScreen() {
        int startX=getScrollX();//开始的位置
        int dx=0;
        if(currentScreen==SCREEN_MENU){
            dx=0-startX;
        }else if(currentScreen==SCREEN_MAIN){
            dx=-getChildAt(0).getMeasuredWidth()-startX;
        }
        int duration=Math.abs(dx)*10;
        if(duration>1000){
            duration=1000;
        }
        scroller.startScroll(startX,0,dx,0,duration);
        //刷新当前控件，会引起onDraw方法的调用
        invalidate();

    }

    public boolean isShowMenu(){
        return currentScreen==SCREEN_MENU;
    }
    public void hideMenu(){
        currentScreen=SCREEN_MENU;
        switchScreen();
    }
    public void showMenu(){
        currentScreen=SCREEN_MENU;
        switchScreen();
    }
}
