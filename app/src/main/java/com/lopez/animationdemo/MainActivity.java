package com.lopez.animationdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
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
        float translationY_start = 0;
        float translationY_end = 0;

        if (isOpen) {
            switch (view.getId()) {
                case R.id.cv_menu_scan:
                    absY = 240;
                    translationY_start = 0;
                    translationY_end = -dipToPx(240);
                    break;
                case R.id.cv_menu_receive:
                    absY = 160;
                    translationY_start = 0;
                    translationY_end = -dipToPx(160);
                    break;
                case R.id.cv_menu_update:
                    absY = 80;
                    translationY_start = 0;
                    translationY_end = -dipToPx(80);
                    break;
            }
        } else {
            switch (view.getId()) {
                case R.id.cv_menu_scan:
                    translationY_start = -dipToPx(240);
                    translationY_end = 0;
                    break;
                case R.id.cv_menu_receive:
                    translationY_start = -dipToPx(160);
                    translationY_end = 0;
                    break;
                case R.id.cv_menu_update:
                    translationY_start = -dipToPx(80);
                    translationY_end = 0;
                    break;
            }
        }

        //回弹开始的地方
        float translationY_spring_original = -dipToPx(absY);
        //往上回弹一次
        float translationY_spring_top = -dipToPx(absY + 20);
        //往下回弹一次
        float translationY_spring_bottom = -dipToPx(absY - 12);

        switch (view.getId()) {
            case R.id.cv_menu_scan:
                translationY_spring_original = -dipToPx(absY);
                translationY_spring_top = -dipToPx(absY + 16);
                translationY_spring_bottom = -dipToPx(absY - 12);
                break;
            case R.id.cv_menu_receive:
                translationY_spring_original = -dipToPx(absY);
                translationY_spring_top = -dipToPx(absY + 8);
                translationY_spring_bottom = -dipToPx(absY - 4);
                break;
            case R.id.cv_menu_update:
                translationY_spring_original = -dipToPx(absY);
                translationY_spring_top = -dipToPx(absY + 4);
                translationY_spring_bottom = -dipToPx(absY - 2);
                break;
        }

        // 1. 出发到达最顶端
        ObjectAnimator translationY_animator = ObjectAnimator.ofFloat(view, "translationY", translationY_start, translationY_spring_top);
        translationY_animator.setDuration(200);
        translationY_animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (isOpen) {
                    view.setVisibility(View.VISIBLE);
                }
            }
        });
        //translationY_animator.setInterpolator(new AccelerateDecelerateInterpolator());
        // 2. 从最顶端回到原始位置
        ObjectAnimator translationY2 = ObjectAnimator.ofFloat(view, "translationY", translationY_spring_top, translationY_spring_original);
        translationY2.setDuration(200);
        // 3. 从原始位置往下弹动一下
        ObjectAnimator translationY3 = ObjectAnimator.ofFloat(view, "translationY", translationY_spring_original, translationY_spring_bottom);
        translationY3.setDuration(80);
        // 4. 从下端位置往上复原位置
        ObjectAnimator translationY4 = ObjectAnimator.ofFloat(view, "translationY", translationY_spring_bottom, translationY_spring_original);

        /*if (isOpen) {

        }
        switch (view.getId()) {
            case R.id.cv_menu_scan:
                duration = 200;
                break;
            case R.id.cv_menu_receive:
                duration = 180;
                break;
            case R.id.cv_menu_update:
                duration = 160;
                break;
        }*/

        translationY4.setDuration(160);

        translationY4.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (!isOpen) return;    //只有开启才会显示
                if (tv.getVisibility() == View.INVISIBLE) {
                    //抖完了就显示
                    tv.setVisibility(View.VISIBLE);
                }
            }
        });

        /**
         *  回程动画
         */
        ObjectAnimator translationY_scan2 = ObjectAnimator.ofFloat(view, "translationY", translationY_start, translationY_end);
        translationY_scan2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (isOpen) return;
                //回程动画结束了就直接关闭按钮的显示
                view.setVisibility(View.GONE);
                hideText(tv);
            }
        });

        //开始组合动画的表演
        AnimatorSet translationY_scan_set = new AnimatorSet();
        //translationY_scan_set.play(translationY_animator);
        long duration = 300;

        if (isOpen) {
            //出去 按钮需要重置点击状态
            setButtonClickable(true);
            //translationY_scan_set.play(translationY_animator).before(translationY2).before(translationY3).before(translationY4);
            translationY_scan_set.playSequentially(translationY_animator, translationY2, translationY3, translationY4);

            //在开始和结束的时较慢，中间加速
            translationY_scan_set.setInterpolator(new AccelerateDecelerateInterpolator());

            switch (view.getId()) {
                case R.id.cv_menu_scan:
                    duration = 200;
                    break;
                case R.id.cv_menu_receive:
                    duration = 180;
                    break;
                case R.id.cv_menu_update:
                    duration = 160;
                    break;
            }
        } else {
            //回来 按钮就不能点击了
            setButtonClickable(false);
            translationY_scan_set.play(translationY_scan2);
            //在开始时改变较慢，然后开始加速
            translationY_scan_set.setInterpolator(new AccelerateInterpolator());

            switch (view.getId()) {
                case R.id.cv_menu_scan:
                    duration = 100;
                    break;
                case R.id.cv_menu_receive:
                    duration = 100;
                    break;
                case R.id.cv_menu_update:
                    duration = 100;
                    break;
            }

            translationY_scan_set.setDuration(duration);
        }

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
