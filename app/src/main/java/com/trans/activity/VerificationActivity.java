package com.trans.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class VerificationActivity extends AppCompatActivity implements View.OnClickListener {
    Context context;
    ImageLoader imageLoader;

    EditText edt_1, edt_2, edt_3, edt_4;
    TextView txt_done, txt_not_get_code;
    boolean fromBookNow;
    String email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
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
        email = getIntent().getStringExtra("email");
    }

    private void setView() {
        edt_1 = findViewById(R.id.edt_1);
        edt_2 = findViewById(R.id.edt_2);
        edt_3 = findViewById(R.id.edt_3);
        edt_4 = findViewById(R.id.edt_4);
        txt_done = findViewById(R.id.txt_done);
        txt_not_get_code = findViewById(R.id.txt_not_get_code);
    }

    private void setData() {

    }

    private void setLitionar() {
        edt_1.addTextChangedListener(new GenericTextWatcher(edt_1));
        edt_2.addTextChangedListener(new GenericTextWatcher(edt_2));
        edt_3.addTextChangedListener(new GenericTextWatcher(edt_3));
        edt_4.addTextChangedListener(new GenericTextWatcher(edt_4));
        txt_done.setOnClickListener(this);
        txt_not_get_code.setOnClickListener(this);
    }

    private void setColor() {
    }

    @Override
    public void onClick(View view) {
        if (view == txt_done) {
            checkValidation();
        } else if (view == txt_not_get_code) {
            sendVerification();
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

    public void checkValidation() {
        String error = "";
        if (edt_1.getText().toString().trim().equals("") ||
                edt_2.getText().toString().trim().equals("") ||
                edt_3.getText().toString().trim().equals("") ||
                edt_4.getText().toString().trim().equals("")) {
            error = context.getResources().getString(R.string.pls_enter_veri_code);
        }
        if (error.equals(""))
            verify();
        else
            Utility.showToast(context, error);
    }

    private void verify() {
        JSONObject object = new JSONObject();
        try {
            object.put("email", email);
            object.put("verificationCode", Integer.parseInt(edt_1.getText().toString() + edt_2.getText().toString() + edt_3.getText().toString() + edt_4.getText().toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiServer.getInstance().verify(new APIResponse() {
            @Override
            public void onSuccess(JSONObject object) {
                try {
                    if (ApiServer.getInstance().isSuccess(context, object)) {
                        AppPrefrece.getInstance().setLogin(true);
                        AppPrefrece.getInstance().setEmail(object.getJSONObject("data").getJSONObject("user").getString("email"));
                        AppPrefrece.getInstance().setUserId(object.getJSONObject("data").getJSONObject("user").getString("_id"));
                        AppPrefrece.getInstance().setProfile(object.getJSONObject("data").getJSONObject("user"));
                        AppPrefrece.getInstance().setAccessToken(object.getJSONObject("data").getString("accessToken"));
                        if (fromBookNow) {
                            onBackPressed();
                        } else {
                            Intent i = new Intent(context, MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                            finish();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {

            }
        }, context, true, object);
    }

    public class GenericTextWatcher implements TextWatcher {
        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            switch (view.getId()) {

                case R.id.edt_1:
                    if (text.length() == 1) {
                        edt_2.requestFocus();
                    }
                    break;
                case R.id.edt_2:
                    if (text.length() == 1)
                        edt_3.requestFocus();
                    else if (text.length() == 0)
                        edt_1.requestFocus();
                    break;
                case R.id.edt_3:
                    if (text.length() == 1)
                        edt_4.requestFocus();
                    else if (text.length() == 0)
                        edt_2.requestFocus();
                    break;
                case R.id.edt_4:
                    if (text.length() == 0)
                        edt_3.requestFocus();
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }
    }

    public void sendVerification() {
        edt_4.setText("");
        edt_3.setText("");
        edt_2.setText("");
        edt_1.setText("");
        JSONObject object = new JSONObject();
        try {
            object.put("email", email);
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
                }
            }

            @Override
            public void onFailure(String error) {

            }
        }, context, true, object);
    }
}
