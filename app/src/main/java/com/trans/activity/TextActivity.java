package com.trans.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.trans.R;
import com.trans.global.Utility;


/**
 * Created by home on 12/07/18.
 */

public class TextActivity extends BaseActivity implements View.OnClickListener {
    Context context;
    ImageView imgv_back;
    TextView txt_title, txt_desc;
    ImageLoader imageLoader;

    String title, desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        initialize();
        setView();
        setData();
        setLitionar();
        setColor();

    }

    private void initialize() {
        context = this;
        Utility.setStatusColor(this);
        Utility.crashLytics(context);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));

        title = getIntent().getStringExtra("title");
        desc = getIntent().getStringExtra("desc");
    }

    private void setView() {
        imgv_back = findViewById(R.id.imgv_back);
        txt_title = findViewById(R.id.txt_title);
        txt_desc = findViewById(R.id.txt_desc);

    }

    private void setData() {
        txt_title.setText(title);
        txt_desc.setText(desc);
    }

    private void setLitionar() {
        imgv_back.setOnClickListener(this);
    }

    private void setColor() {
        imgv_back.setColorFilter(context.getResources().getColor(R.color.red_image));
    }

    @Override
    public void onClick(View view) {
        if (view == imgv_back) {
            onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utility.gotoBack(context);
    }
}
