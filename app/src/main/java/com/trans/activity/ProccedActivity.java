package com.trans.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.trans.R;
import com.trans.api.APIResponse;
import com.trans.api.ApiServer;
import com.trans.global.Utility;
import com.trans.model.JourneyItem;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by home on 12/07/18.
 */

public class ProccedActivity extends BaseActivity implements View.OnClickListener {
    Context context;
    ImageView imgv_back, imgv_info;
    ImageLoader imageLoader;

    String departDate, returnDate, from, to;
    Bundle bundle;
    TextView txt_from_to, txt_depart, txt_selected_seat, txt_booking_for,
            txt_duration_one, txt_from_one, txt_from_time_one, txt_to_one, txt_to_time_one, txt_bus_fare_one,
            txt_duration_return, txt_from_return, txt_from_time_return, txt_to_return, txt_to_time_return, txt_bus_fare_return, txt_return_date, txt_return_day;
    TextView txt_disabled, txt_disabled_rate, txt_tot_passenger, txt_tot_passenger_rate, txt_proceed;
    LinearLayout ll_retun, ll_special_need;
    public static JSONObject price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procced);
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
        imgv_info = findViewById(R.id.imgv_info);
        imgv_info.setVisibility(View.VISIBLE);
        txt_from_to = findViewById(R.id.txt_from_to);
        txt_depart = findViewById(R.id.txt_depart);
        txt_selected_seat = findViewById(R.id.txt_selected_seat);
        txt_booking_for = findViewById(R.id.txt_booking_for);

        txt_duration_one = findViewById(R.id.txt_duration_one);
        txt_from_one = findViewById(R.id.txt_from_one);
        txt_from_time_one = findViewById(R.id.txt_from_time_one);
        txt_to_one = findViewById(R.id.txt_to_one);
        txt_to_time_one = findViewById(R.id.txt_to_time_one);
        txt_bus_fare_one = findViewById(R.id.txt_bus_fare_one);
        txt_proceed = findViewById(R.id.txt_proceed);

        ll_retun = findViewById(R.id.ll_retun);
        ll_special_need = findViewById(R.id.ll_special_need);

        txt_duration_return = findViewById(R.id.txt_duration_return);
        txt_from_return = findViewById(R.id.txt_from_return);
        txt_from_time_return = findViewById(R.id.txt_from_time_return);
        txt_to_return = findViewById(R.id.txt_to_return);
        txt_to_time_return = findViewById(R.id.txt_to_time_return);
        txt_bus_fare_return = findViewById(R.id.txt_bus_fare_return);
        txt_return_day = findViewById(R.id.txt_return_day);
        txt_return_date = findViewById(R.id.txt_return_date);

        txt_disabled = findViewById(R.id.txt_disabled);
        txt_disabled_rate = findViewById(R.id.txt_disabled_rate);
        txt_tot_passenger = findViewById(R.id.txt_tot_passenger);
        txt_tot_passenger_rate = findViewById(R.id.txt_tot_passenger_rate);

    }

    private void setData() {
        bookingPrice();
        JourneyItem dept = BusListActivity.journeyItemDeparture;

        txt_from_to.setText(from + " - " + to);
        txt_depart.setText("Depart " + Utility.getDepartProceedFormat(departDate));
        txt_duration_one.setText(dept.getTimeDifference().split(":")[0] + "hrs "
                + Utility.checkMinute(dept.getTimeDifference().split(":")[1]) + "min");
        txt_from_one.setText(dept.getFromStopCity());
        txt_to_one.setText(dept.getToStopCity());
        txt_from_time_one.setText(dept.getFromStopArrivalTime().getHour() + ":" + Utility.checkMinute(dept.getFromStopArrivalTime().getMinute()+""));
        txt_to_time_one.setText(dept.getToStopDepartureTime().getHour() + ":" + Utility.checkMinute(dept.getToStopDepartureTime().getMinute()+""));
        //txt_bus_fare_one.setText(dept.getFare() + "$");

        if (!MainActivity.isTwoWay) {
            ll_retun.setVisibility(View.GONE);
            ll_special_need.setVisibility(View.VISIBLE);
            if (MainActivity.specialNeed) {
                txt_disabled.setText("1 Disabled Passenger");
                //txt_disabled_rate.setText("$" + (8 * dept.getFare()));

                txt_tot_passenger.setText(MainActivity.seat + " x passengers");
                //txt_tot_passenger_rate.setText("$" + (MainActivity.seat * dept.getFare()));

                setSeatText((MainActivity.seat + 1));
                //txt_booking_for.setText("$" + (MainActivity.seat + 8) * dept.getFare());
            } else {
                txt_disabled.setText("0 Disabled Passenger");
                txt_disabled_rate.setText("$0");

                txt_tot_passenger.setText(MainActivity.seat + " x passengers");
                //txt_tot_passenger_rate.setText("$" + (MainActivity.seat * dept.getFare()));

                setSeatText(MainActivity.seat);


                //txt_booking_for.setText("$" + (MainActivity.seat * dept.getFare()));
            }

        } else {
            JourneyItem retn = BusListActivity.journeyItemReturn;
            ll_retun.setVisibility(View.VISIBLE);
            ll_special_need.setVisibility(View.GONE);
            txt_duration_return.setText(retn.getTimeDifference().split(":")[0] + "hrs "
                    + Utility.checkMinute(retn.getTimeDifference().split(":")[1]) + "min");
            txt_from_return.setText(retn.getFromStopCity());
            txt_to_return.setText(retn.getToStopCity());
            txt_from_time_return.setText(retn.getFromStopArrivalTime().getHour() + ":" + Utility.checkMinute(retn.getFromStopArrivalTime().getMinute()+""));
            txt_to_time_return.setText(retn.getToStopDepartureTime().getHour() + ":" + Utility.checkMinute(retn.getToStopDepartureTime().getMinute()+""));
//            txt_bus_fare_return.setText(retn.getFare() + "$");
            txt_return_date.setText(Utility.getDepartReturnDateFormat(returnDate));
            txt_return_day.setText(Utility.getDepartReturnDay(returnDate));

            if (MainActivity.specialNeed) {
                setSeatText((MainActivity.seat + 1));
                //txt_booking_for.setText("$" + ((MainActivity.seat + 8) * (dept.getFare() + retn.getFare())));
            } else {
                setSeatText(MainActivity.seat);
                //txt_booking_for.setText("$" + (MainActivity.seat * (dept.getFare() + retn.getFare())));
            }
        }

    }


    private void setSeatText(int set){
        if(set<10){
            txt_selected_seat.setText("0"+set);
        }else{
            txt_selected_seat.setText(set + "");
        }
    }

    private void setLitionar() {
        imgv_back.setOnClickListener(this);
        imgv_info.setOnClickListener(this);
        txt_proceed.setOnClickListener(this);
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
        } else if (view == txt_proceed) {
            Intent i = new Intent(context, BookNowActivity.class);
            i.putExtras(bundle);
            context.startActivity(i);
            ((Activity) context).overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TicketActivity.is_backed)
            onBackPressed();
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
        TextView txt_read_more = dialog.findViewById(R.id.txt_read_more);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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

    private void bookingPrice(){
        JSONObject object = new JSONObject();
        try {


            if (!MainActivity.isTwoWay) {
                object.put("routeId",BusListActivity.journeyItemDeparture.getRouteId());
                object.put("isRoundTrip",false);
            }else{
                object.put("departRouteId",BusListActivity.journeyItemDeparture.getRouteId());
                object.put("returnRouteId",BusListActivity.journeyItemReturn.getRouteId());
                object.put("isRoundTrip", true);
            }
            object.put("seats", MainActivity.seat);
            object.put("specialNeedSeat", MainActivity.specialNeed);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiServer.getInstance().bookingPrice(new APIResponse() {
            @Override
            public void onSuccess(JSONObject object) {
                if(ApiServer.getInstance().isSuccess(context,object)){
                    try {

                        price=object.getJSONObject("data");

                        if (!MainActivity.isTwoWay) {
                            txt_bus_fare_one.setText( "$"+price.getJSONObject("priceDetails").getString("ticketPrice"));
                        }else{
                            txt_bus_fare_one.setText("$"+price.getJSONObject("priceDetails").getString("departTicketPrice"));
                            txt_bus_fare_return.setText("$"+price.getJSONObject("priceDetails").getString("returnTicketPrice"));
                        }

                        txt_booking_for.setText("$" + price.getJSONObject("priceDetails").getString("totalCost"));
                        txt_tot_passenger_rate.setText("$" + price.getJSONObject("priceDetails").getString("totalPriceForNormalSeats"));
                        if(MainActivity.specialNeed){
                            txt_disabled_rate.setText("$" +price.getJSONObject("priceDetails").getString("disabledTicketPrice"));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(String error) {

            }
        },context,true,object);
    }
}
