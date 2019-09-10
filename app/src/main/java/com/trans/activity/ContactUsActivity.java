package com.trans.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.trans.R;
import com.trans.api.APIResponse;
import com.trans.api.ApiServer;
import com.trans.global.Utility;
import com.trans.sharedPrefrence.AppPrefrece;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by home on 12/07/18.
 */

public class ContactUsActivity extends AppCompatActivity implements View.OnClickListener {
    Context context;

    ImageView imgv_back;
    ImageLoader imageLoader;
    TextView txt_submit;
    EditText edt_name, edt_email, edt_phone, edt_comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        initialize();
        setView();
        setData();
        setLitionar();
        setColor();

    }

    private void initialize() {
        context = this;
        Utility.crashLytics(context);
        Utility.setStatusColor(this);

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
    }

    private void setView() {
        imgv_back = findViewById(R.id.imgv_back);
        txt_submit = findViewById(R.id.txt_submit);
        edt_name = findViewById(R.id.edt_name);
        edt_email = findViewById(R.id.edt_email);
        edt_phone = findViewById(R.id.edt_phone);
        edt_comment = findViewById(R.id.edt_comment);


    }

    private void setData() {
            if(AppPrefrece.getInstance().getLogin()){
                edt_name.setText(AppPrefrece.getInstance().getName());
                edt_email.setText(AppPrefrece.getInstance().getEmail());
            }
    }

    private void setLitionar() {
        imgv_back.setOnClickListener(this);
        txt_submit.setOnClickListener(this);
    }

    private void setColor() {

        imgv_back.setColorFilter(context.getResources().getColor(R.color.red_image));

    }

    @Override
    public void onClick(View view) {
        if (view == imgv_back) {
            onBackPressed();
        } else if (view == txt_submit) {
            checkValidation();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_contact_us);
        TextView txt_ok = dialog.findViewById(R.id.txt_ok);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        txt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onBackPressed();
            }
        });

        dialog.show();
    }

    private void checkValidation() {
        String error = "";
        if (edt_name.getText().toString().trim().equals("")) {
            error = context.getResources().getString(R.string.enter_name);
        } else if (TextUtils.isEmpty(edt_email.getText().toString().trim())) {
            error = context.getResources().getString(R.string.enter_email);
        } else if (!Patterns.EMAIL_ADDRESS.matcher(edt_email.getText().toString().trim()).matches()) {
            error = context.getResources().getString(R.string.enter_valid_email);
        } else if (edt_phone.getText().toString().trim().equals("")) {
            error = context.getResources().getString(R.string.enter_phone);
        } else if (!Utility.isValidMobile(edt_phone.getText().toString().trim())) {
            error = context.getResources().getString(R.string.enter_valid_phone);
        } else if (edt_comment.getText().toString().trim().equals("")) {
            error = context.getResources().getString(R.string.enter_phone);
        }

        if (error.equals("")) {
            contactUs();
        } else {
            Utility.showToast(context, error);
        }
    }

    private void contactUs() {
        JSONObject object = new JSONObject();
        try {
            object.put("name", edt_name.getText().toString().trim());
            object.put("email", edt_email.getText().toString().trim());
            object.put("description", edt_comment.getText().toString().trim());
            object.put("phoneNumber", edt_phone.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiServer.getInstance().contactUs(new APIResponse() {
            @Override
            public void onSuccess(JSONObject object) {
                if (ApiServer.getInstance().isSuccess(context, object))
                    showDialog();
            }

            @Override
            public void onFailure(String error) {

            }
        }, context, true, object);
    }
}
