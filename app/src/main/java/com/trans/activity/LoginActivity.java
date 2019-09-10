package com.trans.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.trans.R;
import com.trans.api.APIResponse;
import com.trans.api.ApiServer;
import com.trans.global.Utility;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by home on 12/07/18.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Context context;
    ImageView imgv_logo, imgv_background;
    ImageLoader imageLoader;

    TextView txt_login, txt_withot_login;
    EditText edt_email;
    boolean fromBookNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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

        fromBookNow = getIntent().getBooleanExtra("fromBookNow", false);
    }

    private void setView() {
        imgv_logo = findViewById(R.id.imgv_logo);
        imgv_background = findViewById(R.id.imgv_background);
        edt_email = findViewById(R.id.edt_email);
        txt_login = findViewById(R.id.txt_login);
        txt_withot_login = findViewById(R.id.txt_withot_login);
        if (fromBookNow) {
            txt_withot_login.setVisibility(View.GONE);
        } else {
            txt_withot_login.setVisibility(View.VISIBLE);
        }
    }

    private void setData() {
        imageLoader.displayImage("drawable://" + R.drawable.login_bg, imgv_background, new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.login_bg)
                .showImageOnLoading(R.drawable.login_bg)
                .showImageOnFail(R.drawable.login_bg)
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build());
    }

    private void setLitionar() {
        txt_login.setOnClickListener(this);
        txt_withot_login.setOnClickListener(this);
    }

    private void setColor() {

    }

    @Override
    public void onClick(View view) {
        if (view == txt_login) {
            checkValidation();
        } else if (view == txt_withot_login) {
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            finish();
        }

    }

    public void checkValidation() {
        String error = "";
        if (TextUtils.isEmpty(edt_email.getText().toString().trim())) {
            error = context.getResources().getString(R.string.enter_email);
        } else if (!Patterns.EMAIL_ADDRESS.matcher(edt_email.getText().toString().trim()).matches()) {
            error = context.getResources().getString(R.string.enter_valid_email);
        }
        if (error.equals("")) {
            sendVerification();
        } else {
            Utility.showToast(context, error);
        }
    }

    public void sendVerification() {
        JSONObject object = new JSONObject();
        try {
            object.put("email", edt_email.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiServer.getInstance().sendVerification(new APIResponse() {
            @Override
            public void onSuccess(JSONObject object) {
                if (ApiServer.getInstance().isSuccess(context, object)) {
                    try {
                        Utility.showToast(context, object.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent i = new Intent(context, VerificationActivity.class);
                    i.putExtra("fromBookNow", fromBookNow);
                    i.putExtra("email", edt_email.getText().toString().trim());
                    context.startActivity(i);
                    ((Activity) context).overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                    if (fromBookNow) {
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(String error) {

            }
        }, context, true, object);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (fromBookNow) {
            Utility.gotoBack(context);
        }
    }
}
