package com.trans.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
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

import java.io.File;
import java.net.URISyntaxException;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by home on 12/07/18.
 */

public class EditProfileActivity extends BaseActivity implements View.OnClickListener {
    Context context;
    ImageView imgv_back, imgv_add;
    TextView txt_title, txt_save;
    ImageLoader imageLoader;

    CircleImageView cimgv_profile;
    String final_path;
    EditText edt_email, edt_name;
    File file = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
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

    }

    private void setView() {
        imgv_back = findViewById(R.id.imgv_back);
        imgv_add = findViewById(R.id.imgv_add);
        txt_title = findViewById(R.id.txt_title);
        edt_email = findViewById(R.id.edt_email);
        edt_name = findViewById(R.id.edt_name);
        txt_save = findViewById(R.id.txt_save);
        cimgv_profile = findViewById(R.id.cimgv_profile);

    }

    private void setData() {
        txt_title.setText("Edit Profile");
        edt_name.setText(AppPrefrece.getInstance().getName());
        imageLoader.displayImage(AppPrefrece.getInstance().getProfilePic(), cimgv_profile, Utility.getProfileImageOptions());
        edt_email.setText(AppPrefrece.getInstance().getEmail());
        edt_email.setEnabled(false);
        ImageLoader.getInstance().displayImage(AppPrefrece.getInstance().getProfilePic(), cimgv_profile, Utility.getProfileImageOptions());
    }

    private void setLitionar() {
        imgv_back.setOnClickListener(this);
        imgv_add.setOnClickListener(this);
        txt_save.setOnClickListener(this);
    }

    private void setColor() {
        imgv_back.setColorFilter(context.getResources().getColor(R.color.white_image));
    }

    @Override
    public void onClick(View view) {
        if (view == imgv_back) {
            onBackPressed();
        } else if (view == imgv_add) {
            fromCameraGallery();
        } else if (view == txt_save) {
            update();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Utility.RESULT_TAKE) {
                final_path = "";
                File f = new File(String.valueOf((Utility.camera)));
                file = f;
                ImageLoader.getInstance().displayImage("file://" + f, cimgv_profile, Utility.getProfileImageOptions());
                if (f.exists()) {
                    final_path = Utility.camera + "";

                }
            } else if (requestCode == Utility.RESULT_GALLARY) {
                final_path = "";
                Uri uri = data.getData();
                String path = null;
                File f = null;
                try {
                    path = Utility.getRealPathFromURI(context, uri);
                    f = new File(path);
                    file = f;
                    ImageLoader.getInstance().displayImage("file://" + f, cimgv_profile, Utility.getProfileImageOptions());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                if (path != null) {
                    final_path = path;

                }
            }
        }
    }

    private void fromCameraGallery() {

        final AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(context);

        alertDialog2.setTitle("Select from..");
        alertDialog2.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Utility.openCamera(context);
                        dialog.cancel();
                    }
                });
        alertDialog2.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Utility.openGallry(context);
                        dialog.cancel();
                    }
                });

        alertDialog2.show();


    }

    public void update() {
        JSONObject object = new JSONObject();
        try {
            object.put("name", edt_name.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiServer.getInstance().updateSimple(new APIResponse() {
            @Override
            public void onSuccess(JSONObject object) {
                if (ApiServer.getInstance().isSuccess(context, object)) {
                    try {
                        Utility.showToast(context, object.getString("message"));
                        AppPrefrece.getInstance().setProfilePic("file://" +file);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    onBackPressed();
//                    details();
                }
            }

            @Override
            public void onFailure(String error) {

            }
        }, context, true, object);
    }

    public void details() {
        ApiServer.getInstance().details(new APIResponse() {
            @Override
            public void onSuccess(JSONObject object) {
                if (ApiServer.getInstance().isSuccess(context, object)) {
                    try {
                        AppPrefrece.getInstance().setName(object.getJSONObject("data").getString("name"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(String error) {

            }
        }, context, false);
    }
}
