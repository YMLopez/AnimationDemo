package com.lopez.animationdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    CardView cv_coin_menu;
    CardView cv_menu_scan;
    CardView cv_menu_receive;
    CardView cv_menu_update;
    private boolean isOpen;
    TextView tv_scan;
    TextView tv_receive;
    TextView tv_update;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cv_coin_menu = findViewById(R.id.cv_coin_menu);
        cv_menu_scan = findViewById(R.id.cv_menu_scan);
        cv_menu_receive = findViewById(R.id.cv_menu_receive);
        cv_menu_update = findViewById(R.id.cv_menu_update);
        tv_scan = findViewById(R.id.tv_scan);
        tv_receive = findViewById(R.id.tv_receive);
        tv_update = findViewById(R.id.tv_update);

        cv_coin_menu.setOnClickListener(this);
        cv_menu_scan.setOnClickListener(this);
        cv_menu_receive.setOnClickListener(this);
        cv_menu_update.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        //开始你的表演
        toggleTheShow();

        switch (view.getId()) {
            case R.id.cv_menu_scan:
                Toast.makeText(this, "扫一扫！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cv_menu_receive:
                Toast.makeText(this, "接收！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cv_menu_update:
                Toast.makeText(this, "更新！", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    /**
     * dip转px
     */
    public int dipToPx(float dip) {
        return (int) (dip * getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * px转dip
     */
    public int pxToDip(float pxValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    private void toggleTheShow() {
        //如果开了就关，关了就开,
        isOpen = !isOpen;

        // 1. 菜单按钮的起终点
        final float rotate;
        final float original;

        if (isOpen) {
            //开，顺时针旋转45°
            rotate = 45;
            original = 0f;
        } else {
            //关，逆时针旋转45°回去
            rotate = 0f;
            original = 45f;
        }

        // 第二个参数"rotation"表明要执行旋转
        // 0f -> 360f，从旋转360度，也可以是负值，负值即为逆时针旋转，正值是顺时针旋转。
        ObjectAnimator rotation = ObjectAnimator.ofFloat(cv_coin_menu, "rotation", original, rotate);
        // 动画的持续时间，执行多久？
        rotation.setDuration(250);
        // 正式开始启动执行动画
        rotation.start();

        beginButtonShow(cv_menu_update, tv_update);
        beginButtonShow(cv_menu_receive, tv_receive);
        beginButtonShow(cv_menu_scan, tv_scan);
    }

    private void beginButtonShow(final View view, final TextView tv) {
        //点击时文本都要隐藏，只有动画完成了才会显示！
        hideText(tv);

        float absY = 240;
        // 2. 扫一扫按钮的起终点
        float translationY_scan_start = 0;
        float translationY_scan_end = 0;

        if (isOpen) {
            switch (view.getId()) {
                case R.id.cv_menu_scan:
                    absY = 240;
                    translationY_scan_start = 0;
                    translationY_scan_end = -dipToPx(240);
                    break;
                case R.id.cv_menu_receive:
                    absY = 160;
                    translationY_scan_start = 0;
                    translationY_scan_end = -dipToPx(160);
                    break;
                case R.id.cv_menu_update:
                    absY = 80;
                    translationY_scan_start = 0;
                    translationY_scan_end = -dipToPx(80);
                    break;
            }
        } else {
            switch (view.getId()) {
                case R.id.cv_menu_scan:
                    translationY_scan_start = -dipToPx(240);
                    translationY_scan_end = 0;
                    break;
                case R.id.cv_menu_receive:
                    translationY_scan_start = -dipToPx(160);
                    translationY_scan_end = 0;
                    break;
                case R.id.cv_menu_update:
                    translationY_scan_start = -dipToPx(80);
                    translationY_scan_end = 0;
                    break;
            }
        }

        ObjectAnimator translationY_scan = ObjectAnimator.ofFloat(view, "translationY", translationY_scan_start, translationY_scan_end);
        //translationY_scan.setDuration(220);
        translationY_scan.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (isOpen) {
                    view.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (isOpen) return;
                //回程动画结束了就直接关闭按钮的显示
                view.setVisibility(View.GONE);
                hideText(tv);
            }
        });
        //translationY_scan.start();

        //回弹开始的地方
        float translationY_scan_spring_original = -dipToPx(absY);
        //往上回弹一次
        float translationY_scan_spring_top = -dipToPx(absY + 18);
        //往下回弹一次
        float translationY_scan_spring_bottom = -dipToPx(absY - 6);

        switch (view.getId()) {
            case R.id.cv_menu_scan:
                translationY_scan_spring_original = -dipToPx(absY);
                translationY_scan_spring_top = -dipToPx(absY + 18);
                translationY_scan_spring_bottom = -dipToPx(absY - 8);
                break;
            case R.id.cv_menu_receive:
                translationY_scan_spring_original = -dipToPx(absY);
                translationY_scan_spring_top = -dipToPx(absY + 8);
                translationY_scan_spring_bottom = -dipToPx(absY - 4);
                break;
            case R.id.cv_menu_update:
                translationY_scan_spring_original = -dipToPx(absY);
                translationY_scan_spring_top = -dipToPx(absY + 4);
                translationY_scan_spring_bottom = -dipToPx(absY - 2);
                break;
        }

        ObjectAnimator translationY_scan2 = ObjectAnimator.ofFloat(view, "translationY", translationY_scan_spring_original, translationY_scan_spring_top, translationY_scan_spring_original, translationY_scan_spring_bottom, translationY_scan_spring_original);
        translationY_scan2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isOpen) return;    //只有开启才会显示
                if (tv.getVisibility() == View.INVISIBLE) {
                    //抖完了就显示
                    tv.setVisibility(View.VISIBLE);
                }
            }
        });

        //开始组合动画的表演
        AnimatorSet translationY_scan_set = new AnimatorSet();
        //translationY_scan_set.play(translationY_scan);
        if (isOpen) {
            //出去按钮需要重置点击状态
            setButtonClickable(true);

            //出去
            translationY_scan_set.playSequentially(translationY_scan, translationY_scan2);
        } else {
            //回来按钮就不能点击了
            setButtonClickable(false);

            //回来
            translationY_scan_set.playSequentially(translationY_scan);
        }

        long duration = 250;
        switch (view.getId()) {
            case R.id.cv_menu_scan:
                duration = 250;
                break;
            case R.id.cv_menu_receive:
                duration = 220;
                break;
            case R.id.cv_menu_update:
                duration = 180;
                break;
        }
        translationY_scan_set.setDuration(duration);
        //在开始时改变较慢，然后开始加速
        //translationY_scan_set.setInterpolator(new AccelerateDecelerateInterpolator());
        //在开始地方速度较快，然后开始减速
        translationY_scan_set.setInterpolator(new DecelerateInterpolator());
        translationY_scan_set.start();
    }

    private void hideText(TextView tv) {
        //因为快速点击时存在bug，故必须在此处强制隐藏
        if (tv.getVisibility() == View.VISIBLE) {
            //回程动画一开始就关闭文字的显示
            tv.setVisibility(View.INVISIBLE);
        }
    }

    private void setButtonClickable(boolean isClickable) {
        cv_menu_scan.setEnabled(isClickable);
        cv_menu_receive.setEnabled(isClickable);
        cv_menu_update.setEnabled(isClickable);
    }

    @Override
    public void onBackPressed() {
        if (isOpen) {
            //开了就关掉
            toggleTheShow();
            //然后赋值状态
            isOpen = false;
        } else {
            super.onBackPressed();
        }
    }

}
