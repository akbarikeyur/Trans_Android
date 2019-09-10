package com.trans.activity;

import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.trans.R;
import com.trans.api.APIResponse;
import com.trans.api.ApiServer;
import com.trans.global.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * Created by home on 12/07/18.
 */

public class TicketActivity extends BaseActivity implements View.OnClickListener {
    public static boolean is_backed = false;
    Context context;
    ImageView imgv_back, imgv_information, imgv_logo, imgv_bottom;
    ImageLoader imageLoader;
    TextView txt_title, txt_tkt_number, txt_usr_name, txt_usr_email, txt_cmpny_name, txt_bottom;
    TextView txt_fare;
    Bundle bundle;
    Bitmap bitmap;


    boolean fromHistory = false;
    LinearLayout ll_dwnl_tkt, ll_return;

    TextView txt_depart_date, txt_depart_day, txt_from_depart, txt_from_time_depart, txt_duration_depart, txt_to_depart, txt_to_time_depart;
    TextView txt_return_date, txt_return_day, txt_from_time_return, txt_duration_return, txt_to_time_return, txt_num_seat;
    LinearLayout ll_ticket;

    JSONObject ticket;
    JSONObject priceDetails, departPrice, returnPrice;
    String timeDiffrence, returnTimeDiffrence, departTimeDiffrence;
    int seats = 0;
    JSONObject fromArrival, toDeparture, departFromArrival, departTodeparture, returnFromArrival, returnTodeparture;
    public static boolean cancel = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        initialize();
        setView();
        setData();
        setLitionar();
        setColor();

    }

    private void initialize() {
        is_backed = false;
        context = this;
        Utility.setStatusColorRed(this);
        Utility.crashLytics(context);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));


        bundle = getIntent().getExtras();

        fromHistory = bundle.getBoolean("fromHistory");

    }

    private void setView() {
        imgv_back = findViewById(R.id.imgv_back);
        imgv_bottom = findViewById(R.id.imgv_bottom);
        imgv_information = findViewById(R.id.imgv_information);
        txt_title = findViewById(R.id.txt_title);
        txt_tkt_number = findViewById(R.id.txt_tkt_number);

        ll_dwnl_tkt = findViewById(R.id.ll_dwnl_tkt);
        ll_return = findViewById(R.id.ll_return);

        txt_usr_name = findViewById(R.id.txt_usr_name);
        txt_usr_email = findViewById(R.id.txt_usr_email);

        txt_depart_day = findViewById(R.id.txt_depart_day);
        txt_depart_date = findViewById(R.id.txt_depart_date);
        txt_duration_depart = findViewById(R.id.txt_duration_depart);
        txt_from_depart = findViewById(R.id.txt_from_depart);
        txt_from_time_depart = findViewById(R.id.txt_from_time_depart);
        txt_to_depart = findViewById(R.id.txt_to_depart);
        txt_to_time_depart = findViewById(R.id.txt_to_time_depart);

        txt_return_day = findViewById(R.id.txt_return_day);
        txt_return_date = findViewById(R.id.txt_return_date);
        txt_duration_return = findViewById(R.id.txt_duration_return);
        txt_from_time_return = findViewById(R.id.txt_from_time_return);
        txt_to_time_return = findViewById(R.id.txt_to_time_return);

        txt_fare = findViewById(R.id.txt_fare);
        imgv_logo = findViewById(R.id.imgv_logo);
        txt_cmpny_name = findViewById(R.id.txt_cmpny_name);
        txt_bottom = findViewById(R.id.txt_bottom);
        txt_num_seat = findViewById(R.id.txt_num_seat);
        ll_ticket = findViewById(R.id.ll_ticket);
    }

    private void setData() {

        if (fromHistory) {
            txt_bottom.setText("Cancel Booking");
            imgv_bottom.setImageResource(R.drawable.cancel);
            imgv_bottom.setColorFilter(context.getResources().getColor(R.color.red_image));
        } else {
            txt_bottom.setText("Download Ticket");
            imgv_bottom.setImageResource(R.drawable.download);
        }

        try {
            ticket = new JSONObject(bundle.getString("ticket"));
            txt_usr_name.setText(ticket.getString("name"));
        } catch (JSONException e) {
            txt_usr_name.setText("");
            e.printStackTrace();
        }
        try {
            ticket = new JSONObject(bundle.getString("ticket"));
            seats = ticket.getInt("seats");
            if (ticket.getBoolean("specialNeedSeat")) {
                seats = seats + 1;
            }

            priceDetails = ticket.getJSONObject("price").getJSONObject("priceDetails");
            if (!ticket.getBoolean("isTwoWay")) {

                timeDiffrence = ticket.getString("timeDifference");
                fromArrival = ticket.getJSONObject("fromStopDetails").getJSONObject("arrivalTime");
                toDeparture = ticket.getJSONObject("toStopDetails").getJSONObject("departureTime");


            } else {
                departPrice = ticket.getJSONObject("price").getJSONObject("departPrice");
                returnPrice = ticket.getJSONObject("price").getJSONObject("returnPrice");
                departTimeDiffrence = ticket.getJSONObject("departBooking").getString("timeDifference");
                returnTimeDiffrence = ticket.getJSONObject("returnBooking").getString("timeDifference");
                departFromArrival = ticket.getJSONObject("departBooking").getJSONObject("fromStopDetails").getJSONObject("arrivalTime");
                departTodeparture = ticket.getJSONObject("departBooking").getJSONObject("toStopDetails").getJSONObject("departureTime");
                returnFromArrival = ticket.getJSONObject("returnBooking").getJSONObject("fromStopDetails").getJSONObject("arrivalTime");
                returnTodeparture = ticket.getJSONObject("returnBooking").getJSONObject("toStopDetails").getJSONObject("departureTime");
            }

            txt_tkt_number.setText(ticket.getString("ticketNumber"));

            txt_usr_email.setText(ticket.getString("email"));
            txt_fare.setText(priceDetails.getString("totalCost"));
//            txt_num_seat.setText(seats + "");

            if(seats<10){
                txt_num_seat.setText("0"+seats);
            }else{
                txt_num_seat.setText(seats + "");
            }

            //dataSet
            if (!ticket.getBoolean("isTwoWay")) {

                //depart Data
                ll_return.setVisibility(View.GONE);

                txt_depart_date.setText(Utility.getDepartReturnDateFormatFromIOS(ticket.getString("date")));
                txt_depart_day.setText(Utility.getDepartReturnDayFromISO(ticket.getString("date")));

                String duration = timeDiffrence.split(":")[0] + "hrs " + Utility.checkMinute(timeDiffrence.split(":")[1]) + "min";
                txt_duration_depart.setText(duration);

                txt_from_depart.setText(ticket.getJSONObject("fromStopDetails").getString("city"));
                txt_to_depart.setText(ticket.getJSONObject("toStopDetails").getString("city"));

                txt_from_time_depart.setText(fromArrival.getString("hour") + ":" + Utility.checkMinute(fromArrival.getString("minute")));
                txt_to_time_depart.setText(toDeparture.getString("hour") + ":" + Utility.checkMinute(toDeparture.getString("minute")));

            } else {

                //depart data
                String duration = departTimeDiffrence.split(":")[0] + "hrs " + Utility.checkMinute(departTimeDiffrence.split(":")[1]) + "min";
                txt_duration_depart.setText(duration);

                txt_depart_date.setText(Utility.getDepartReturnDateFormatFromIOS(ticket.getJSONObject("departBooking").getString("date")));
                txt_depart_day.setText(Utility.getDepartReturnDayFromISO(ticket.getJSONObject("departBooking").getString("date")));

                txt_from_depart.setText(ticket.getJSONObject("departBooking").getJSONObject("fromStopDetails").getString("city"));
                txt_to_depart.setText(ticket.getJSONObject("departBooking").getJSONObject("toStopDetails").getString("city"));

                txt_from_time_depart.setText(departFromArrival.getString("hour") + ":" + Utility.checkMinute(departFromArrival.getString("minute")));
                txt_to_time_depart.setText(departTodeparture.getString("hour") + ":" +  Utility.checkMinute(departTodeparture.getString("minute")));

                //return data
                ll_return.setVisibility(View.VISIBLE);

                String durationReturn = returnTimeDiffrence.split(":")[0] + "hrs " + Utility.checkMinute(returnTimeDiffrence.split(":")[1]) + "min";
                txt_duration_return.setText(durationReturn);

                txt_return_date.setText(Utility.getDepartReturnDateFormatFromIOS(ticket.getJSONObject("returnBooking").getString("date")));
                txt_return_day.setText(Utility.getDepartReturnDayFromISO(ticket.getJSONObject("returnBooking").getString("date")));

                txt_from_time_return.setText(returnFromArrival.getString("hour") + ":" + Utility.checkMinute(returnFromArrival.getString("minute")));
                txt_to_time_return.setText(returnTodeparture.getString("hour") + ":" + Utility.checkMinute(returnTodeparture.getString("minute")));
            }


        } catch (JSONException e) {
            Log.e("Tag -> ", e.getMessage());
            e.printStackTrace();
        }

        txt_title.setText(context.getResources().getString(R.string.journy_details));

        txt_cmpny_name.setText("Mercedes-Benz");
        imageLoader.displayImage("https://images-na.ssl-images-amazon.com/images/I/61VaoHj7IbL._SX425_.jpg", imgv_logo, Utility.getImageOptions());

    }

    private void setLitionar() {
        imgv_back.setOnClickListener(this);
        imgv_information.setOnClickListener(this);
        ll_dwnl_tkt.setOnClickListener(this);
    }

    private void setColor() {
        imgv_back.setColorFilter(context.getResources().getColor(R.color.white_image));
        imgv_information.setColorFilter(context.getResources().getColor(R.color.red_image));
    }

    @Override
    public void onClick(View view) {
        if (view == imgv_back) {
            onBackPressed();


        } else if (view == ll_dwnl_tkt) {
            if (fromHistory) {
                showAreYouSure();
            } else {
                if (Utility.checkPermissionForStorage(context))
                    downLoadTicket();
                else
                    Utility.requestPermissionForStorage(context);
            }
        } else if (view == imgv_information) {
            showDialogTicketInfo();
        }
    }

    private void downLoadTicket() {
        ll_ticket.setDrawingCacheEnabled(true);
        ll_ticket.buildDrawingCache(true);
        bitmap = Bitmap.createBitmap(ll_ticket.getDrawingCache());
        ll_ticket.setDrawingCacheEnabled(false);
        try {
            String path = Environment.getExternalStorageDirectory().toString() + "/C Trans";

            File f = new File(path);
            if (!f.isDirectory())
                f.mkdir();

            OutputStream fOut = null;
            Integer counter = 0;
            File file = new File(path, ticket.getString("ticketNumber") + ".jpg"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
            fOut.flush(); // Not really required
            fOut.close(); // do not forget to close the stream
            MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
            Toast.makeText(context, "Successfully download", Toast.LENGTH_SHORT).show();
            fireNotification(file);

        } catch (FileNotFoundException e) {
            Utility.showLog("FileNotFoundException -> " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Utility.showLog("IOException -> " + e.getMessage());
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void fireNotification(File file) {
        Uri photoURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".com.trans.provider", file);


        Intent myIntent = new Intent(Intent.ACTION_VIEW, photoURI);
        myIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        myIntent.setDataAndTypeAndNormalize(photoURI, "image/*");
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, myIntent, 0);

        Intent sendIntent = new Intent();
        myIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_STREAM, photoURI);
        sendIntent.setType("image/*");
        PendingIntent share = PendingIntent.getActivity(this, 0, sendIntent, 0);

        Notification notification = new NotificationCompat.Builder(context, "0")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Ticket Download.")
                .addAction(R.drawable.logo, "Share", share)
                .setLargeIcon(bitmap)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmap)
                        .bigLargeIcon(null))
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, notification);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (!fromHistory) {
            is_backed = true;
        }
        super.onBackPressed();
        Utility.gotoBack(context);
    }

    private void showDialogTicketInfo() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_ticket_info);
        ImageView imgv_close = dialog.findViewById(R.id.imgv_close);

        TextView txt_num_seat_dia = dialog.findViewById(R.id.txt_num_seat_dia);

        TextView txt_depart_detail = dialog.findViewById(R.id.txt_depart_detail);
        TextView txt_depart_rate = dialog.findViewById(R.id.txt_depart_rate);

        LinearLayout ll_retun = dialog.findViewById(R.id.ll_retun);
        TextView txt_return_detail = dialog.findViewById(R.id.txt_return_detail);
        TextView txt_return_rate = dialog.findViewById(R.id.txt_return_rate);

        TextView txt_service_rate = dialog.findViewById(R.id.txt_service_rate);

        TextView txt_total_fare = dialog.findViewById(R.id.txt_total_fare);

        try {
//            txt_num_seat_dia.setText(seats + "");
            if(seats<10){
                txt_num_seat_dia.setText("0"+seats);
            }else{
                txt_num_seat_dia.setText(seats + "");
            }


            if (!ticket.getBoolean("isTwoWay")) {
                ll_retun.setVisibility(View.GONE);

                txt_depart_detail.setText(seats + " x " + priceDetails.getString("ticketPrice"));
                txt_depart_rate.setText("$" + priceDetails.getString("totalPriceForAllSeats"));
            } else {
                ll_retun.setVisibility(View.VISIBLE);

                txt_depart_detail.setText(seats + " x " + departPrice.getString("ticketPrice"));
                txt_depart_rate.setText("$" + departPrice.getString("totalPriceForAllSeats"));
                txt_return_detail.setText(seats + " x " + returnPrice.getString("ticketPrice"));
                txt_return_rate.setText("$" + returnPrice.getString("totalPriceForAllSeats"));
            }

            txt_service_rate.setText("$" + priceDetails.getString("serviceCharges"));
            txt_total_fare.setText("$" + priceDetails.getString("totalCost"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        imgv_close.setColorFilter(context.getResources().getColor(R.color.red_background));

        imgv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showAreYouSure() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_are_you_sure);
        TextView txt_no = dialog.findViewById(R.id.txt_no);
        TextView txt_yes = dialog.findViewById(R.id.txt_yes);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        txt_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        txt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                cancelBooking();
            }
        });

        dialog.show();
    }

    private void cancelBooking() {
        JSONObject object = new JSONObject();
        try {
            object.put("ticketNumber", txt_tkt_number.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiServer.getInstance().cancelBooking(new APIResponse() {
            @Override
            public void onSuccess(JSONObject object) {
                if (ApiServer.getInstance().isSuccess(context, object)) {
                    cancel = true;
                    onBackPressed();
                }
            }

            @Override
            public void onFailure(String error) {

            }
        }, context, true, object);
    }
}
