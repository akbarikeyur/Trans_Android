package com.trans.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbb20.CountryCodePicker;
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

public class BookNowActivity extends BaseActivity implements View.OnClickListener {
    Context context;
    ImageView imgv_back;
    ImageLoader imageLoader;

    TextView txt_from_to, txt_depart, txt_num_seat, txt_book_now;
    String departDate, returnDate, from, to;
    Bundle bundle;
    EditText edt_name, edt_email, edt_num;
    public static String name, email, num;
    CountryCodePicker ccp;

    public static JSONObject ticket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_now);
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


        bundle = getIntent().getExtras();
        departDate = BusListActivity.departDate;
        returnDate = BusListActivity.returnDate;
        from = bundle.getString("from");
        to = bundle.getString("to");


    }

    private void setView() {
        imgv_back = findViewById(R.id.imgv_back);
        txt_from_to = findViewById(R.id.txt_from_to);
        txt_depart = findViewById(R.id.txt_depart);
        txt_num_seat = findViewById(R.id.txt_num_seat);
        txt_book_now = findViewById(R.id.txt_book_now);
        edt_name = findViewById(R.id.edt_name);
        edt_email = findViewById(R.id.edt_email);
        edt_num = findViewById(R.id.edt_num);
        ccp = findViewById(R.id.ccp);
    }

    private void setData() {
        txt_from_to.setText(from + " - " + to);
        if (!MainActivity.isTwoWay) {
            txt_depart.setText("Depart " + Utility.getDepartProceedFormat(departDate));
        } else {
            txt_depart.setText("Depart " + Utility.getDepartProceedFormat(departDate) + " - " + "Return " + Utility.getDepartProceedFormat(returnDate));
        }

        if (MainActivity.specialNeed)
            txt_num_seat.setText((MainActivity.seat + 1) + "");
        else
            txt_num_seat.setText("" + MainActivity.seat);

        if (AppPrefrece.getInstance().getLogin()) {
            edt_name.setText(AppPrefrece.getInstance().getName());
            edt_email.setText(AppPrefrece.getInstance().getEmail());
        }
    }

    private void setLitionar() {
        imgv_back.setOnClickListener(this);
        txt_book_now.setOnClickListener(this);
    }

    private void setColor() {
        imgv_back.setColorFilter(context.getResources().getColor(R.color.white_image));
    }

    @Override
    public void onClick(View view) {
        if (view == imgv_back) {
            onBackPressed();
        } else if (view == txt_book_now) {
            checkValidation();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AppPrefrece.getInstance().getLogin()) {
            edt_name.setText(AppPrefrece.getInstance().getName());
            edt_email.setText(AppPrefrece.getInstance().getEmail());
        }
        if (TicketActivity.is_backed)
            onBackPressed();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utility.gotoBack(context);
    }

    public void checkValidation() {
        String error = "";
        if (edt_name.getText().toString().trim().equals("")) {
            error = context.getResources().getString(R.string.enter_name);
        } else if (TextUtils.isEmpty(edt_email.getText().toString().trim())) {
            error = context.getResources().getString(R.string.enter_email);
        } else if (!Patterns.EMAIL_ADDRESS.matcher(edt_email.getText().toString().trim()).matches()) {
            error = context.getResources().getString(R.string.enter_valid_email);
        }/* else if (edt_num.getText().toString().trim().equals("")) {
            error = context.getResources().getString(R.string.enter_phone);
        } else if (!Utility.isValidMobile(edt_num.getText().toString().trim())) {
            error = context.getResources().getString(R.string.enter_valid_phone);
        }*/

        if (error.equals("")) {
            if (AppPrefrece.getInstance().getLogin()) {
                /*Intent i = new Intent(context, PayNowActivity.class);
                name = edt_name.getText().toString().trim();
                email = edt_email.getText().toString().trim();
                num = ccp.getSelectedCountryCodeWithPlus() + edt_num.getText().toString().trim();
                bundle.putString("msg","Booking Saved Successfully, You have 10 minutes to complete your payment");
                i.putExtras(bundle);
                context.startActivity(i);
                ((Activity) context).overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);*/
                book();
            } else {
                Intent i = new Intent(context, LoginActivity.class);
                i.putExtra("fromBookNow", true);
                context.startActivity(i);
                ((Activity) context).overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        } else {
            Utility.showToast(context, error);
        }
    }


    public void book() {
        JSONObject object = new JSONObject();
        Log.e("Tag -> ", BusListActivity.departDate);
        try {
//            object.put("from", MainActivity.from.getData().get(MainActivity.fromPosition).getId());
//            object.put("to", MainActivity.to.getData().get(MainActivity.toPosition).getId());
            object.put("from", BusListActivity.journeyItemDeparture.getFromStopId());
            object.put("to", BusListActivity.journeyItemDeparture.getToStopId());
            object.put("date", Utility.milliseconds(BusListActivity.departDate));
            object.put("specialNeedSeat", MainActivity.specialNeed);
            object.put("seats", MainActivity.seat);
            object.put("routeRef", BusListActivity.journeyItemDeparture.getRouteId());

            object.put("isTwoWay", MainActivity.isTwoWay);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiServer.getInstance().book(new APIResponse() {
            @Override
            public void onSuccess(JSONObject object) {
                if (ApiServer.getInstance().isSuccess(context, object)) {
                    try {
                        bundle.putString("msg", object.getString("message"));
                        ticket = object.getJSONObject("data");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (MainActivity.isTwoWay) {
                        try {
                            bookReturn(object.getJSONObject("data").getString("bookingId"));
                        } catch (JSONException e) {
                            try {
                                bookReturn(object.getJSONObject("data").getString("booking"));
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                            Utility.showLog(e.getMessage());
                            e.printStackTrace();
                        }
                    } else {
                        Intent i = new Intent(context, PayNowActivity.class);
                        name = edt_name.getText().toString().trim();
                        email = edt_email.getText().toString().trim();
                        num = ccp.getSelectedCountryCodeWithPlus() + edt_num.getText().toString().trim();
                        i.putExtras(bundle);
                        context.startActivity(i);
                        ((Activity) context).overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                    }
                }
            }

            @Override
            public void onFailure(String error) {

            }
        }, context, true, object);
    }

    public void bookReturn(String bookingId) {
        JSONObject object = new JSONObject();
        try {
            object.put("from", BusListActivity.journeyItemReturn.getFromStopId());
            object.put("to", BusListActivity.journeyItemReturn.getToStopId());
            object.put("date", Utility.milliseconds(BusListActivity.returnDate));
            object.put("specialNeedSeat", MainActivity.specialNeed);
            object.put("seats", MainActivity.seat);
            object.put("routeRef", BusListActivity.journeyItemReturn.getRouteId());
            object.put("isTwoWay", MainActivity.isTwoWay);
            object.put("returnOf", bookingId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiServer.getInstance().book(new APIResponse() {
            @Override
            public void onSuccess(JSONObject object) {
                try {
                    bundle.putString("msg", object.getString("message"));
                    ticket = object.getJSONObject("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent i = new Intent(context, PayNowActivity.class);
                name = edt_name.getText().toString().trim();
                email = edt_email.getText().toString().trim();
                num = ccp.getSelectedCountryCodeWithPlus() + edt_num.getText().toString().trim();
                i.putExtras(bundle);
                context.startActivity(i);
                ((Activity) context).overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }

            @Override
            public void onFailure(String error) {

            }
        }, context, true, object);
    }
}
