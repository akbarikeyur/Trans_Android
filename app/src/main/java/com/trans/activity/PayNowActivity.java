package com.trans.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.trans.R;
import com.trans.global.Utility;

import org.json.JSONException;

import java.util.concurrent.TimeUnit;


/**
 * Created by home on 12/07/18.
 */

public class PayNowActivity extends BaseActivity implements View.OnClickListener {
    Context context;
    ImageView imgv_back;
    ImageLoader imageLoader;

    TextView txt_from_to, txt_depart, txt_num_seat, txt_pay_now, txt_total_fare, txt_return_detail, txt_depart_detail, txt_timer;
    TextView txt_disabled, txt_disabled_rate, txt_tot_passenger, txt_tot_passenger_rate, txt_departure_booking,txt_service_rate;
    String departDate, returnDate, from, to;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_now);
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
        txt_pay_now = findViewById(R.id.txt_pay_now);
        txt_total_fare = findViewById(R.id.txt_total_fare);

        txt_disabled = findViewById(R.id.txt_disabled);
        txt_disabled_rate = findViewById(R.id.txt_disabled_rate);
        txt_tot_passenger = findViewById(R.id.txt_tot_passenger);
        txt_tot_passenger_rate = findViewById(R.id.txt_tot_passenger_rate);
        txt_depart_detail = findViewById(R.id.txt_depart_detail);
        txt_return_detail = findViewById(R.id.txt_return_detail);
        txt_departure_booking = findViewById(R.id.txt_departure_booking);
        txt_timer = findViewById(R.id.txt_timer);
        txt_service_rate = findViewById(R.id.txt_service_rate);
    }

    private void setData() {

        Utility.errDialog(bundle.getString("msg"), context);
        new CountDownTimer(60 * 10 * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                txt_timer.setText(String.format("%02d", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
                        + " : "
                        + String.format("%02d", TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60));
            }

            public void onFinish() {
                onBackPressed();
            }

        }.start();

        if (MainActivity.specialNeed)
            setSeatText((MainActivity.seat + 1));
        else
            setSeatText(MainActivity.seat);

        txt_from_to.setText(from + " - " + to);

        try {
            txt_total_fare.setText("$" + ProccedActivity.price.getJSONObject("priceDetails").getString("totalCost"));
            txt_service_rate.setText("$" + ProccedActivity.price.getJSONObject("priceDetails").getString("serviceCharges"));
            if (!MainActivity.isTwoWay) {
                txt_depart_detail.setVisibility(View.GONE);
                txt_return_detail.setVisibility(View.GONE);
                txt_departure_booking.setVisibility(View.VISIBLE);
                txt_depart.setText("Depart " + Utility.getDepartProceedFormat(departDate));

                txt_tot_passenger_rate.setText("$" + ProccedActivity.price.getJSONObject("priceDetails").getString("totalPriceForNormalSeats"));

                if (MainActivity.specialNeed) {
                    txt_disabled.setText("1 Disabled Pessenger");
                    //txt_disabled_rate.setText("$" + (8 * BusListActivity.journeyItemDeparture.getFare()));
                    txt_disabled_rate.setText("$" + ProccedActivity.price.getJSONObject("priceDetails").getString("disabledTicketPrice"));

                    txt_tot_passenger.setText(MainActivity.seat + " x pessengers");
                    //txt_tot_passenger_rate.setText("$" + (MainActivity.seat * BusListActivity.journeyItemDeparture.getFare()));

                    //txt_total_fare.setText("$" + ((MainActivity.seat + 8) * BusListActivity.journeyItemDeparture.getFare()));
                } else {
                    txt_disabled.setText("0 Disabled Pessenger");
                    txt_disabled_rate.setText("$0");

                    txt_tot_passenger.setText(MainActivity.seat + " x pessengers");
                    //txt_tot_passenger_rate.setText("$" + (MainActivity.seat * BusListActivity.journeyItemDeparture.getFare()));

                    //txt_total_fare.setText("$" + (MainActivity.seat * BusListActivity.journeyItemDeparture.getFare()));
                }

            } else {
                txt_depart_detail.setVisibility(View.VISIBLE);
                txt_return_detail.setVisibility(View.VISIBLE);
                txt_departure_booking.setVisibility(View.GONE);
                txt_depart.setText("Depart " + Utility.getDepartProceedFormat(departDate) + " - " + "Return " + Utility.getDepartProceedFormat(returnDate));


                txt_disabled.setText("Departure Booking");
                txt_tot_passenger.setText("Return Booking");
                if (MainActivity.specialNeed) {
                    //depart
                    //txt_depart_detail.setText((MainActivity.seat + 8) + " x $" + BusListActivity.journeyItemDeparture.getFare());
                    //txt_disabled_rate.setText("$" + ((MainActivity.seat + 8) * BusListActivity.journeyItemDeparture.getFare()));
                    txt_depart_detail.setText((MainActivity.seat + 1) + " x $" + ProccedActivity.price.getJSONObject("priceDetails").getString("departTicketPrice"));
                    txt_disabled_rate.setText("$" + ProccedActivity.price.getJSONObject("departPrice").getString("totalPriceForAllSeats"));

                    //return
                    //txt_return_detail.setText((MainActivity.seat + 8) + " x $" + BusListActivity.journeyItemReturn.getFare());
                    //txt_tot_passenger_rate.setText("$" + ((MainActivity.seat + 8) * BusListActivity.journeyItemReturn.getFare()));
                    txt_return_detail.setText((MainActivity.seat + 1) + " x $" + ProccedActivity.price.getJSONObject("priceDetails").getString("returnTicketPrice"));
                    txt_tot_passenger_rate.setText("$" + ProccedActivity.price.getJSONObject("returnPrice").getString("totalPriceForAllSeats"));


                    //txt_total_fare.setText("$" + ((MainActivity.seat + 8) * (BusListActivity.journeyItemDeparture.getFare() + BusListActivity.journeyItemReturn.getFare())));
                } else {
                    //depart
                    //txt_depart_detail.setText(MainActivity.seat + " x $" + BusListActivity.journeyItemDeparture.getFare());
                    //txt_disabled_rate.setText("$" + (MainActivity.seat * BusListActivity.journeyItemDeparture.getFare()));
                    txt_depart_detail.setText(MainActivity.seat + " x $" + ProccedActivity.price.getJSONObject("priceDetails").getString("departTicketPrice"));
                    txt_disabled_rate.setText("$" + ProccedActivity.price.getJSONObject("departPrice").getString("totalPriceForAllSeats"));

                    //return
                    //txt_return_detail.setText(MainActivity.seat + " x $" + BusListActivity.journeyItemReturn.getFare());
                    //txt_tot_passenger_rate.setText("$" + (MainActivity.seat * BusListActivity.journeyItemReturn.getFare()));
                    txt_return_detail.setText(MainActivity.seat + " x $" + ProccedActivity.price.getJSONObject("priceDetails").getString("returnTicketPrice"));
                    txt_tot_passenger_rate.setText("$" +  ProccedActivity.price.getJSONObject("returnPrice").getString("totalPriceForAllSeats"));


                    //txt_total_fare.setText("$" + (MainActivity.seat * (BusListActivity.journeyItemDeparture.getFare()) + BusListActivity.journeyItemReturn.getFare()));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void setSeatText(int set){
        if(set<10){
            txt_num_seat.setText("0"+set);
        }else{
            txt_num_seat.setText(set + "");
        }
    }

    private void setLitionar() {
        imgv_back.setOnClickListener(this);
        txt_pay_now.setOnClickListener(this);
    }

    private void setColor() {
        imgv_back.setColorFilter(context.getResources().getColor(R.color.white_image));
    }

    @Override
    public void onClick(View view) {
        if (view == imgv_back) {
            onBackPressed();
        } else if (view == txt_pay_now) {
            Intent i = new Intent(context, PaymentActivity.class);
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

}
