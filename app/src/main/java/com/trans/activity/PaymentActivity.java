package com.trans.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.trans.R;
import com.trans.api.APIResponse;
import com.trans.api.ApiServer;
import com.trans.global.GlobalAppConfiguration;
import com.trans.global.Utility;
import com.trans.sharedPrefrence.AppPrefrece;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by home on 12/07/18.
 */

public class PaymentActivity extends BaseActivity implements View.OnClickListener {
    Context context;
    ImageView imgv_back, imgv_paypal, imgv_wallet;
    LinearLayout ll_wallet, ll_paypal;
    ImageLoader imageLoader;
    private static PayPalConfiguration config;
    PayPalPayment thingToBuy;
    TextView txt_title, txt_wallet_balance, txt_pay;
    TextView edt_payment;
    String wallet = "";
    Bundle bundle;
    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final String CONFIG_ENVIRONMENT = GlobalAppConfiguration.ENVIRONMENT;
    String totalFare = "";
    boolean isMoneyForWallet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
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
        configPaypal();
        bundle = getIntent().getExtras();

        try {
            totalFare = ProccedActivity.price.getJSONObject("priceDetails").getString("totalCost");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*if (!MainActivity.isTwoWay) {
            if (MainActivity.specialNeed) {
                totalFare = "" + ((MainActivity.seat + 8) * BusListActivity.journeyItemDeparture.getFare());
            } else {
                totalFare = "" + (MainActivity.seat * BusListActivity.journeyItemDeparture.getFare());
            }
        } else {
            if (MainActivity.specialNeed) {
                totalFare = "" + ((MainActivity.seat + 8) * (BusListActivity.journeyItemDeparture.getFare() + BusListActivity.journeyItemReturn.getFare()));
            } else {
                totalFare = "" + (MainActivity.seat * (BusListActivity.journeyItemDeparture.getFare()) + BusListActivity.journeyItemReturn.getFare());
            }
        }*/
    }

    private void setView() {
        imgv_back = findViewById(R.id.imgv_back);
        imgv_wallet = findViewById(R.id.imgv_wallet);
        imgv_paypal = findViewById(R.id.imgv_paypal);
        txt_title = findViewById(R.id.txt_title);
        ll_wallet = findViewById(R.id.ll_wallet);
        ll_paypal = findViewById(R.id.ll_paypal);
        txt_wallet_balance = findViewById(R.id.txt_wallet_balance);
        edt_payment = findViewById(R.id.edt_payment);
        txt_pay = findViewById(R.id.txt_pay);
    }

    private void setData() {
        isMoneyForWallet = false;
        details();
        txt_title.setText(context.getResources().getString(R.string.payment_option));
        showPaymentSelection();
        try {
            edt_payment.setText(ProccedActivity.price.getJSONObject("priceDetails").getString("totalCost"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setLitionar() {
        imgv_back.setOnClickListener(this);
        txt_pay.setOnClickListener(this);
        imgv_paypal.setOnClickListener(this);
    }

    private void setColor() {
        imgv_back.setColorFilter(context.getResources().getColor(R.color.white_image));
        imgv_wallet.setColorFilter(context.getResources().getColor(R.color.grey_verification));
    }

    @Override
    public void onClick(View view) {
        if (view == imgv_back) {
            onBackPressed();
        } else if (view == imgv_paypal) {
            if (Utility.isNetworkAvailable(context)) {
                isMoneyForWallet = false;
                MakePayment(totalFare);
            } else {
                Utility.showToast(context, context.getResources().getString(R.string.network));
            }
            //transaction("123456");
        } else if (view == txt_pay) {
            checkValidation();
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

    public void configPaypal() {
        config = new PayPalConfiguration()
                .environment(CONFIG_ENVIRONMENT)
                .clientId(GlobalAppConfiguration.PAYPAL_KEY)
                .merchantName(getString(R.string.app_name))
                .merchantPrivacyPolicyUri(
                        Uri.parse(getString(R.string.merchant_privacy)))
                .merchantUserAgreementUri(
                        Uri.parse(getString(R.string.merchant_user_agreement)));
    }

    public void MakePayment(String price) {

        if (price != null && !price.equals("")) {
            Intent intent = new Intent(this, PayPalService.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
            startService(intent);
            thingToBuy = new PayPalPayment(new java.math.BigDecimal(String.valueOf(price)),
                    getString(R.string.paypal_payment_currency), "Payment", PayPalPayment.PAYMENT_INTENT_SALE);
            Intent payment = new Intent(this,
                    com.paypal.android.sdk.payments.PaymentActivity.class);

            payment.putExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_PAYMENT, thingToBuy);
            payment.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

            startActivityForResult(payment, REQUEST_CODE_PAYMENT);


        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                //  String.valueOf(finalfare)
                PaymentConfirmation confirm = data
                        .getParcelableExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    Utility.showLog(confirm.toJSONObject());
                    Utility.showLog(confirm.getPayment().toJSONObject());

                    if (isMoneyForWallet) {
                        addToWallet(confirm.toJSONObject().toString());
                    } else {
                        transaction(confirm.toJSONObject().toString());
                    }


                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Utility.showToast(context, context.getResources().getString(R.string.payment_hbeen_cancelled));
            } else if (resultCode == com.paypal.android.sdk.payments.PaymentActivity.RESULT_EXTRAS_INVALID) {
                Utility.showToast(context, context.getResources().getString(R.string.error_occurred));
                //  Log.d("payment", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        } else if (requestCode == REQUEST_CODE_FUTURE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth = data
                        .getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.i("FuturePaymentExample", auth.toJSONObject()
                                .toString(4));

                        String authorization_code = auth.getAuthorizationCode();
                        Log.d("FuturePaymentExample", authorization_code);

                        /*sendAuthorizationToServer(auth);
                        Toast.makeText(getActivity(),
                                "Future Payment code received from PayPal",
                                Toast.LENGTH_LONG).show();*/
                        Log.e("paypal", "future Payment code received from PayPal  :" + authorization_code);

                    } catch (JSONException e) {
                        Utility.showToast(context, context.getResources().getString(R.string.payment_hbeen_cancelled));
                        Log.e("FuturePaymentExample",
                                "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Utility.showToast(context, context.getResources().getString(R.string.payment_hbeen_cancelled));
                Log.d("FuturePaymentExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Utility.showToast(context, context.getResources().getString(R.string.error_occurred));
                Log.d("FuturePaymentExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        }

    }

    public void transaction(String transactionId) {
        JSONObject object = new JSONObject();
        try {
            object.put("bookingId", BookNowActivity.ticket.getString("bookingId"));
            if (transactionId.equals("")) {
                object.put("payViaCredits", true);
            } else {
                object.put("transaction", new JSONObject(transactionId));
            }
        } catch (JSONException e) {

            try {
                object.put("bookingId", BookNowActivity.ticket.getString("booking"));
                if (transactionId.equals("")) {
                    object.put("payViaCredits", true);
                } else {
                    object.put("transaction", new JSONObject(transactionId));
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        ApiServer.getInstance().transaction(new APIResponse() {
            @Override
            public void onSuccess(JSONObject object) {
                if (ApiServer.getInstance().isSuccess(context, object)) {
                    //Toast.makeText(context,"Your ticket will be send to your registerd Email ID.",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context, TicketActivity.class);
                    try {
                        bundle.putString("ticket", String.valueOf(object.getJSONObject("data")));
                        bundle.putBoolean("fromHistory", false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    i.putExtras(bundle);
                    context.startActivity(i);
                    ((Activity) context).overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }
            }

            @Override
            public void onFailure(String error) {

            }
        }, context, true, object);
    }

    public void addToWallet(String transactionId) {
        JSONObject object = new JSONObject();
        try {
            object.put("transaction", new JSONObject(transactionId));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiServer.getInstance().addToWallet(new APIResponse() {
            @Override
            public void onSuccess(JSONObject object) {
                if (ApiServer.getInstance().isSuccess(context, object)) {
                    try {
                        Utility.showToast(context, object.getString("message"));
                        details();
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

    private void showPaymentSelection() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_payment_method_select);
        ImageView imgv_close = dialog.findViewById(R.id.imgv_close);

        final TextView txt_ok = dialog.findViewById(R.id.txt_ok);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        imgv_close.setColorFilter(context.getResources().getColor(R.color.red_background));
        imgv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        final RadioButton radio_wallet = dialog.findViewById(R.id.radio_wallet);
        final RadioButton radio_paypal = dialog.findViewById(R.id.radio_paypal);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            radio_paypal.getButtonDrawable().setColorFilter(getResources().getColor(R.color.red_background), PorterDuff.Mode.SRC_IN);
            radio_wallet.getButtonDrawable().setColorFilter(getResources().getColor(R.color.red_background), PorterDuff.Mode.SRC_IN);
        }

        txt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (radio_paypal.isChecked()) {
                    ll_paypal.setVisibility(View.VISIBLE);
                    ll_wallet.setVisibility(View.GONE);
                } else if (radio_wallet.isChecked()) {
                    ll_paypal.setVisibility(View.GONE);
                    ll_wallet.setVisibility(View.VISIBLE);
                }
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    public void details() {
        ApiServer.getInstance().details(new APIResponse() {
            @Override
            public void onSuccess(JSONObject object) {
                if (ApiServer.getInstance().isSuccess(context, object)) {
                    try {
                        AppPrefrece.getInstance().setProfile(object.getJSONObject("data"));
                        txt_wallet_balance.setText("$" + object.getJSONObject("data").getString("appCredits"));
                        wallet = object.getJSONObject("data").getString("appCredits");
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

    public void checkValidation() {
        if (Double.parseDouble(wallet) < Double.parseDouble(edt_payment.getText().toString())) {
            double total = Double.parseDouble(edt_payment.getText().toString()) - Double.parseDouble(wallet);
            isMoneyForWallet = true;
            Toast.makeText(context, "Insufficient balance in your wallet.Add $"+total+" from PayPal to your wallet.", Toast.LENGTH_LONG).show();
            MakePayment(String.valueOf(total));
            /*showPaymentSelection();*/
        } else {
            transaction("");
        }
    }
}
