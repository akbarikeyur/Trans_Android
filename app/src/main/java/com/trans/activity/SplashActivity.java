package com.trans.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.trans.R;
import com.trans.global.Utility;
import com.trans.sharedPrefrence.AppPrefrece;


public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    Context context;
    ImageView imgv_logo, imgv_depart, imgv_arrive, imgv_bus;
    FrameLayout fl_bus, fl_arrive_depart;
    LinearLayout ll_arrive_depart;
    ImageLoader imageLoader;
    int width = 0, w190, w40, w2, w15;
    int height = 0, h40;
    TextView txt_arrive, txt_depart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initialize();
        setView();
        setPading();
        setData();
    }

    private void initialize() {
        context = this;
        Utility.setStatusColor(this);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;

        w190 = width * 180 / 320;
        w40 = width * 40 / 320;
        w2 = width * 2 / 320;
        w15 = width * 15 / 320;
        h40 = height * 40 / 480;
    }

    private void setView() {
        imgv_logo = findViewById(R.id.imgv_logo);
        imgv_depart = findViewById(R.id.imgv_depart);
        imgv_arrive = findViewById(R.id.imgv_arrive);
        imgv_bus = findViewById(R.id.imgv_bus);
        fl_bus = findViewById(R.id.fl_bus);
        fl_arrive_depart = findViewById(R.id.fl_arrive_depart);
        txt_arrive = findViewById(R.id.txt_arrive);
        txt_depart = findViewById(R.id.txt_depart);
        ll_arrive_depart = findViewById(R.id.ll_arrive_depart);
    }

    private void setPading() {

        fl_arrive_depart.getLayoutParams().height = w40 * 2;

        imgv_depart.getLayoutParams().width = w40;
        imgv_depart.getLayoutParams().height = w40;
        imgv_depart.setPadding(w2, w2, w2, w2);

        imgv_arrive.getLayoutParams().width = w40;
        imgv_arrive.getLayoutParams().height = w40;

        imgv_arrive.setPadding(w2, w2, w2, w2);

        imgv_bus.getLayoutParams().height = w40;

        fl_bus.setPadding(w15, 0, w15, 0);
        ll_arrive_depart.setPadding(w15, 0, w15, 0);
    }

    private void setData() {
        imgv_depart.setColorFilter(context.getResources().getColor(R.color.black_image));
        imgv_arrive.setColorFilter(context.getResources().getColor(R.color.red_image));


        imgv_bus.animate()
                .translationX(w190)
                .setDuration(2000)
                .setInterpolator(new LinearInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                });

        imgv_depart.animate()
                .translationY(w40)
                .setDuration(2000)
                .setInterpolator(new LinearInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                });


        imgv_arrive.animate()
                .alpha(0)
                .setDuration(2000)
                .setInterpolator(new LinearInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                });

        txt_depart.animate()
                .alpha(0)
                .setDuration(2000)
                .setInterpolator(new LinearInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                });

        txt_arrive.animate()
                .alpha(1)
                .setDuration(2000)
                .setInterpolator(new LinearInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        if (AppPrefrece.getInstance().getLogin()) {
                            Utility.gotoNext(context, MainActivity.class);
                        } else {
                            Intent i = new Intent(context, LoginActivity.class);
                            i.putExtra("fromBookNow", false);
                            context.startActivity(i);
                            ((Activity) context).overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                        }
                        finish();
                    }
                });


    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
