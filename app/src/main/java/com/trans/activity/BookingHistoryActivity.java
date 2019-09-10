package com.trans.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.trans.R;
import com.trans.adapter.HistoryPagerAdapter;
import com.trans.global.Utility;

import java.util.ArrayList;


/**
 * Created by home on 12/07/18.
 */

public class BookingHistoryActivity extends BaseActivity implements View.OnClickListener {
    Context context;
    ImageView imgv_back, imgv_info;
    TextView txt_title;
    ImageLoader imageLoader;


    ViewPager vpgr;
    TabLayout vpgr_tab;
    HistoryPagerAdapter historyPagerAdapter;
    ArrayList<String> strings = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);
        initialize();
        setView();
        setData();
        setLitionar();
        setColor();

    }

    private void initialize() {
        context = this;
        Utility.setStatusColorRed(this);
        Utility.crashLytics(context);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));

        strings.add(context.getResources().getString(R.string.upcoming));
        strings.add(context.getResources().getString(R.string.past));
        strings.add(context.getResources().getString(R.string.cancelled));
        historyPagerAdapter = new HistoryPagerAdapter(context, getSupportFragmentManager(), strings);
    }

    private void setView() {
        imgv_back = findViewById(R.id.imgv_back);
        imgv_info = findViewById(R.id.imgv_info);
        imgv_info.setVisibility(View.VISIBLE);
        txt_title = findViewById(R.id.txt_title);

        vpgr = findViewById(R.id.vpgr);
        vpgr_tab = findViewById(R.id.vpgr_tab);
        vpgr_tab.setTabMode(TabLayout.MODE_FIXED);
        vpgr_tab.setTabGravity(TabLayout.GRAVITY_FILL);

    }

    private void setData() {
        txt_title.setText("Booking History");
        vpgr.setAdapter(historyPagerAdapter);
        vpgr_tab.setupWithViewPager(vpgr);
    }

    private void setLitionar() {
        imgv_back.setOnClickListener(this);
        imgv_info.setOnClickListener(this);
    }

    private void setColor() {
        imgv_back.setColorFilter(context.getResources().getColor(R.color.white_image));
        imgv_info.setColorFilter(context.getResources().getColor(R.color.white_image));
    }

    @Override
    public void onClick(View view) {
        if (view == imgv_back) {
            onBackPressed();
        } else if (view == imgv_info) {
            showDialogPolicy();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (vpgr.getCurrentItem() == 0) {
            if (TicketActivity.cancel) {
                if (historyPagerAdapter.cancelledHistoryFragment != null)
                    historyPagerAdapter.cancelledHistoryFragment.setData();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utility.gotoBack(context);
    }

    private void showDialogPolicy() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_cancellation);
        ImageView imgv_close = dialog.findViewById(R.id.imgv_close);
        final TextView txt_message = dialog.findViewById(R.id.txt_message);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView txt_read_more = dialog.findViewById(R.id.txt_read_more);
        txt_message.setText(context.getResources().getString(R.string.cancellation_policy));
        txt_message.setMaxLines(5);

        imgv_close.setColorFilter(context.getResources().getColor(R.color.red_background));

        imgv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        txt_read_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_message.setMaxLines(20);
                v.setVisibility(View.GONE);
            }
        });
        dialog.show();
    }

}
