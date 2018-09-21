package com.lopez.animationdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
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


    //忽略警告，去掉黄色
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    // 正式开始启动执行动画
                    rotation.start();
                    break;
            }
        }
    };
    private ObjectAnimator rotation;


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
            case R.id.cv_coin_menu:
                //开始你的表演
                beginTheShow();
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


    private void beginTheShow() {
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
        rotation = ObjectAnimator.ofFloat(cv_coin_menu, "rotation", original, rotate);
        // 动画的持续时间，执行多久？
        rotation.setDuration(250);
        // 正式开始启动执行动画
        rotation.start();

        //暂时不用子线程，感觉有问题
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                // 第二个参数"rotation"表明要执行旋转
                // 0f -> 360f，从旋转360度，也可以是负值，负值即为逆时针旋转，正值是顺时针旋转。
                rotation = ObjectAnimator.ofFloat(cv_coin_menu, "rotation", original, rotate);
                // 动画的持续时间，执行多久？
                rotation.setDuration(250);
                // 正式开始启动执行动画
                rotation.start();

                //选择菜单按钮
                handler.sendEmptyMessage(0);
            }
        }).start();*/

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

        float translationY_scan_start2 = -dipToPx(absY);
        float translationY_scan_end2 = -dipToPx(absY + 20);
        float translationY_scan_start3 = translationY_scan_end2;
        float translationY_scan_end3 = translationY_scan_start2;

        switch (view.getId()) {
            case R.id.cv_menu_scan:
                translationY_scan_start2 = -dipToPx(absY);
                translationY_scan_end2 = -dipToPx(absY + 20);
                translationY_scan_start3 = translationY_scan_end2;
                translationY_scan_end3 = translationY_scan_start2;
                break;
            case R.id.cv_menu_receive:
                translationY_scan_start2 = -dipToPx(absY);
                translationY_scan_end2 = -dipToPx(absY + 12);
                translationY_scan_start3 = translationY_scan_end2;
                translationY_scan_end3 = translationY_scan_start2;
                break;
            case R.id.cv_menu_update:
                translationY_scan_start2 = -dipToPx(absY);
                translationY_scan_end2 = -dipToPx(absY + 6);
                translationY_scan_start3 = translationY_scan_end2;
                translationY_scan_end3 = translationY_scan_start2;
                break;
        }

        ObjectAnimator translationY_scan2 = ObjectAnimator.ofFloat(view, "translationY", translationY_scan_start2, translationY_scan_end2);
        //translationY_scan2.setDuration(30);
        ObjectAnimator translationY_scan3 = ObjectAnimator.ofFloat(view, "translationY", translationY_scan_start3, translationY_scan_end3);
        translationY_scan3.addListener(new AnimatorListenerAdapter() {
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
            translationY_scan_set.playSequentially(translationY_scan, translationY_scan2, translationY_scan3);
        } else {
            //回来按钮就不能点击了
            setButtonClickable(false);

            //回来
            translationY_scan_set.playSequentially(translationY_scan);
        }

        long duration = 120;
        switch (view.getId()) {
            case R.id.cv_menu_scan:
                duration = 120;
                break;
            case R.id.cv_menu_receive:
                duration = 110;
                break;
            case R.id.cv_menu_update:
                duration = 100;
                break;
        }
        translationY_scan_set.setDuration(duration);
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


}
