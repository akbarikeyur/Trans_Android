package com.trans.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.jacksonandroidnetworking.JacksonParserFactory;
import com.trans.R;
import com.trans.global.Utility;
import com.trans.sharedPrefrence.AppPrefrece;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Properties;


public class ApiServer {
    public static ApiServer apiServer;
    public static int serverSuccessCode = 100;
    Context context;
    Properties properties;
    String subUrl = "";
    String mainUrl = "";
    //Dialog dialog;
    ProgressDialog pd;

    public ApiServer(Context context) {
        this.context = context;
        properties = new LoadAssetProperties().loadRESTApiFile(context.getResources(), "rest.properties", context);
        mainUrl = properties.getProperty("MainUrl");
        AndroidNetworking.setParserFactory(new JacksonParserFactory());
        apiServer = this;
    }

    public static ApiServer getInstance() {
        return apiServer;
    }

    public boolean isSuccess(Context context, JSONObject jsonObject) {
        try {
            printResponse(jsonObject.toString());
            if (jsonObject.getInt("code") != ApiServer.serverSuccessCode) {
                Utility.showToast(context, jsonObject.getString("message"));
                return false;
            } else {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void showLoader(Context context, boolean isDialog) {
        /*dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setDimAmount(0);
        dialog.setContentView(R.layout.custom_alert_loader);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.transparent);
        dialog.show();*/
        if (isDialog)
            pd = Utility.showProgressDialog(context);
    }

    public void removeLoader(boolean isDialog) {
//        dialog.dismiss();
        if (isDialog)
            Utility.dismissProgressDialog(pd);
    }

    public void printError(ANError error) {
        Utility.showLog("getErrorBody => " + error.getErrorBody()
                + "\ngetErrorCode => " + error.getErrorCode()
                + "\ngetErrorDetail => " + error.getErrorDetail()
                + "\ngetLocalizedMessage => " + error.getLocalizedMessage()
                + "\ngetMessage=> " + error.getMessage());
    }

    public void printResponse(String response) {
        Utility.showLog("Response => " + response);
    }

    public void printUrlAndParams(String params) {
        Utility.showLog("url => " + mainUrl + subUrl);
        Utility.showLog("params => " + params);
    }

    public void sendVerification(final APIResponse listener, Context context, final boolean isDialog, JSONObject jsonObject) {
        if (Utility.isNetworkAvailable(context)) {
            showLoader(context, isDialog);
            subUrl = properties.getProperty("sendVerification");
            printUrlAndParams(jsonObject.toString());
            AndroidNetworking.post(mainUrl + subUrl)
                    .addJSONObjectBody(jsonObject)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject object) {
                            removeLoader(isDialog);
                            listener.onSuccess(object);
                        }

                        @Override
                        public void onError(ANError error) {
                            printError(error);
                            removeLoader(isDialog);
                            listener.onFailure(error.getErrorBody());
                        }
                    });
        } else {
            Utility.errDialog(context.getResources().getString(R.string.network), context);
        }
    }

    public void verify(final APIResponse listener, Context context, final boolean isDialog, JSONObject jsonObject) {
        if (Utility.isNetworkAvailable(context)) {
            showLoader(context, isDialog);
            subUrl = properties.getProperty("verify");
            printUrlAndParams(jsonObject.toString());
            AndroidNetworking.post(mainUrl + subUrl)
                    .addJSONObjectBody(jsonObject)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject object) {
                            removeLoader(isDialog);
                            listener.onSuccess(object);
                        }

                        @Override
                        public void onError(ANError error) {
                            printError(error);
                            removeLoader(isDialog);
                            listener.onFailure(error.getErrorBody());
                        }
                    });
        } else {
            Utility.errDialog(context.getResources().getString(R.string.network), context);
        }
    }

    public void updateSimple(final APIResponse listener, Context context, final boolean isDialog, JSONObject jsonObject) {
        if (Utility.isNetworkAvailable(context)) {
            showLoader(context, isDialog);
            subUrl = properties.getProperty("update");
            printUrlAndParams(jsonObject.toString());
            AndroidNetworking.post(mainUrl + subUrl)
                    .addHeaders("Authorization", AppPrefrece.getInstance().getAccessToken())
                    .addJSONObjectBody(jsonObject)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject object) {
                            removeLoader(isDialog);
                            listener.onSuccess(object);
                        }

                        @Override
                        public void onError(ANError error) {
                            printError(error);
                            removeLoader(isDialog);
                            listener.onFailure(error.getErrorBody());
                        }
                    });
        } else {
            Utility.errDialog(context.getResources().getString(R.string.network), context);
        }
    }

    public void update(final APIResponse listener, Context context, final boolean isDialog, JSONObject jsonObject, File file) {
        if (Utility.isNetworkAvailable(context)) {
            showLoader(context, isDialog);
            subUrl = properties.getProperty("update");
            printUrlAndParams(jsonObject.toString());
            if (file == null) {
                Log.e("Tag -> ", "null");
                AndroidNetworking.upload(mainUrl + subUrl)
                        .addHeaders("Authorization", AppPrefrece.getInstance().getAccessToken())
                        .addMultipartParameter("data", jsonObject.toString())
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject object) {
                                removeLoader(isDialog);
                                listener.onSuccess(object);
                            }

                            @Override
                            public void onError(ANError error) {
                                printError(error);
                                removeLoader(isDialog);
                                listener.onFailure(error.getErrorBody());
                            }
                        });
            } else {
                Log.e("Tag -> ", "not null");
                AndroidNetworking.upload(mainUrl + subUrl)
                        .addHeaders("Authorization", AppPrefrece.getInstance().getAccessToken())
                        .addMultipartFile("images", file)
                        .addMultipartParameter("data", jsonObject.toString())
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject object) {
                                removeLoader(isDialog);
                                listener.onSuccess(object);
                            }

                            @Override
                            public void onError(ANError error) {
                                printError(error);
                                removeLoader(isDialog);
                                listener.onFailure(error.getErrorBody());
                            }
                        });
            }
        } else {
            Utility.errDialog(context.getResources().getString(R.string.network), context);
        }
    }

    public void searchFrom(final APIResponse listener, Context context, final boolean isDialog) {
        if (Utility.isNetworkAvailable(context)) {
            showLoader(context, isDialog);
            subUrl = properties.getProperty("searchFrom");
            AndroidNetworking.post(mainUrl + subUrl)
                    .addHeaders("Authorization", AppPrefrece.getInstance().getAccessToken())
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject object) {
                            removeLoader(isDialog);
                            listener.onSuccess(object);
                        }

                        @Override
                        public void onError(ANError error) {
                            printError(error);
                            removeLoader(isDialog);
                            listener.onFailure(error.getErrorBody());
                        }
                    });
        } else {
            Utility.errDialog(context.getResources().getString(R.string.network), context);
        }
    }

    public void searchTo(final APIResponse listener, Context context, final boolean isDialog, JSONObject jsonObject) {
        if (Utility.isNetworkAvailable(context)) {
            showLoader(context, isDialog);
            subUrl = properties.getProperty("searchTo");
            printUrlAndParams(jsonObject.toString());
            AndroidNetworking.post(mainUrl + subUrl)
                    .addHeaders("Authorization", AppPrefrece.getInstance().getAccessToken())
                    .addJSONObjectBody(jsonObject)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject object) {
                            removeLoader(isDialog);
                            listener.onSuccess(object);
                        }

                        @Override
                        public void onError(ANError error) {
                            printError(error);
                            removeLoader(isDialog);
                            listener.onFailure(error.getErrorBody());
                        }
                    });
        } else {
            Utility.errDialog(context.getResources().getString(R.string.network), context);
        }
    }

    public void journeyDetails(final APIResponse listener, Context context, final boolean isDialog, JSONObject jsonObject) {
        if (Utility.isNetworkAvailable(context)) {
            showLoader(context, isDialog);
            subUrl = properties.getProperty("journeyDetails");
            printUrlAndParams(jsonObject.toString());
            AndroidNetworking.post(mainUrl + subUrl)
                    .addJSONObjectBody(jsonObject)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject object) {
                            removeLoader(isDialog);
                            listener.onSuccess(object);
                        }

                        @Override
                        public void onError(ANError error) {
                            printError(error);
                            removeLoader(isDialog);
                            listener.onFailure(error.getErrorBody());
                        }
                    });
        } else {
            Utility.errDialog(context.getResources().getString(R.string.network), context);
        }
    }

    public void book(final APIResponse listener, Context context, final boolean isDialog, JSONObject jsonObject) {
        if (Utility.isNetworkAvailable(context)) {
            showLoader(context, isDialog);
            subUrl = properties.getProperty("book");
            printUrlAndParams(jsonObject.toString());
            AndroidNetworking.post(mainUrl + subUrl)
                    .addHeaders("Authorization", AppPrefrece.getInstance().getAccessToken())
                    .addJSONObjectBody(jsonObject)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject object) {
                            removeLoader(isDialog);
                            listener.onSuccess(object);
                        }

                        @Override
                        public void onError(ANError error) {
                            printError(error);
                            removeLoader(isDialog);
                            listener.onFailure(error.getErrorBody());
                        }
                    });
        } else {
            Utility.errDialog(context.getResources().getString(R.string.network), context);
        }
    }

    public void transaction(final APIResponse listener, Context context, final boolean isDialog, JSONObject jsonObject) {
        if (Utility.isNetworkAvailable(context)) {
            showLoader(context, isDialog);
            subUrl = properties.getProperty("transaction");
            printUrlAndParams(jsonObject.toString());
            AndroidNetworking.post(mainUrl + subUrl)
                    .addHeaders("Authorization", AppPrefrece.getInstance().getAccessToken())
                    .addJSONObjectBody(jsonObject)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject object) {
                            removeLoader(isDialog);
                            listener.onSuccess(object);
                        }

                        @Override
                        public void onError(ANError error) {
                            printError(error);
                            removeLoader(isDialog);
                            listener.onFailure(error.getErrorBody());
                        }
                    });
        } else {
            Utility.errDialog(context.getResources().getString(R.string.network), context);
        }
    }

    public void addToWallet(final APIResponse listener, Context context, final boolean isDialog, JSONObject jsonObject) {
        if (Utility.isNetworkAvailable(context)) {
            showLoader(context, isDialog);
            subUrl = properties.getProperty("addToWallet");
            printUrlAndParams(jsonObject.toString());
            AndroidNetworking.post(mainUrl + subUrl)
                    .addHeaders("Authorization", AppPrefrece.getInstance().getAccessToken())
                    .addJSONObjectBody(jsonObject)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject object) {
                            removeLoader(isDialog);
                            listener.onSuccess(object);
                        }

                        @Override
                        public void onError(ANError error) {
                            printError(error);
                            removeLoader(isDialog);
                            listener.onFailure(error.getErrorBody());
                        }
                    });
        } else {
            Utility.errDialog(context.getResources().getString(R.string.network), context);
        }
    }

    public void bookingPrice(final APIResponse listener, Context context, final boolean isDialog, JSONObject jsonObject) {
        if (Utility.isNetworkAvailable(context)) {
            showLoader(context, isDialog);
            subUrl = properties.getProperty("bookingPrice");
            printUrlAndParams(jsonObject.toString());
            AndroidNetworking.post(mainUrl + subUrl)
                    .addJSONObjectBody(jsonObject)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject object) {
                            removeLoader(isDialog);
                            listener.onSuccess(object);
                        }

                        @Override
                        public void onError(ANError error) {
                            printError(error);
                            removeLoader(isDialog);
                            listener.onFailure(error.getErrorBody());
                        }
                    });
        } else {
            Utility.errDialog(context.getResources().getString(R.string.network), context);
        }
    }

    public void contactUs(final APIResponse listener, Context context, final boolean isDialog, JSONObject jsonObject) {
        if (Utility.isNetworkAvailable(context)) {
            showLoader(context, isDialog);
            subUrl = properties.getProperty("contactUs");
            printUrlAndParams(jsonObject.toString());
            AndroidNetworking.post(mainUrl + subUrl)
                    .addJSONObjectBody(jsonObject)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject object) {
                            removeLoader(isDialog);
                            listener.onSuccess(object);
                        }

                        @Override
                        public void onError(ANError error) {
                            printError(error);
                            removeLoader(isDialog);
                            listener.onFailure(error.getErrorBody());
                        }
                    });
        } else {
            Utility.errDialog(context.getResources().getString(R.string.network), context);
        }
    }

    public void details(final APIResponse listener, Context context, final boolean isDialog) {
        if (Utility.isNetworkAvailable(context)) {
            showLoader(context, isDialog);
            subUrl = properties.getProperty("details");
            AndroidNetworking.post(mainUrl + subUrl)
                    .addHeaders("Authorization", AppPrefrece.getInstance().getAccessToken())
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject object) {
                            removeLoader(isDialog);
                            listener.onSuccess(object);
                        }

                        @Override
                        public void onError(ANError error) {
                            printError(error);
                            removeLoader(isDialog);
                            listener.onFailure(error.getErrorBody());
                        }
                    });
        } else {
            Utility.errDialog(context.getResources().getString(R.string.network), context);
        }
    }

    public void viewBookings(final APIResponse listener, Context context, final boolean isDialog, JSONObject jsonObject) {
        if (Utility.isNetworkAvailable(context)) {
            showLoader(context, isDialog);
            printUrlAndParams(jsonObject.toString());
            Utility.showLog( AppPrefrece.getInstance().getAccessToken());
            subUrl = properties.getProperty("viewBookings");
            AndroidNetworking.post(mainUrl + subUrl)
                    .addHeaders("Authorization", AppPrefrece.getInstance().getAccessToken())
                    .addJSONObjectBody(jsonObject)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject object) {
                            removeLoader(isDialog);
                            listener.onSuccess(object);
                        }

                        @Override
                        public void onError(ANError error) {
                            printError(error);
                            removeLoader(isDialog);
                            listener.onFailure(error.getErrorBody());
                        }
                    });
        } else {
            Utility.errDialog(context.getResources().getString(R.string.network), context);
        }
    }

    public void cancelBooking(final APIResponse listener, Context context, final boolean isDialog, JSONObject jsonObject) {
        if (Utility.isNetworkAvailable(context)) {
            showLoader(context, isDialog);
            printUrlAndParams(jsonObject.toString());
            Utility.showLog( AppPrefrece.getInstance().getAccessToken());
            subUrl = properties.getProperty("cancelBooking");
            AndroidNetworking.post(mainUrl + subUrl)
                    .addHeaders("Authorization", AppPrefrece.getInstance().getAccessToken())
                    .addJSONObjectBody(jsonObject)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject object) {
                            removeLoader(isDialog);
                            listener.onSuccess(object);
                        }

                        @Override
                        public void onError(ANError error) {
                            printError(error);
                            removeLoader(isDialog);
                            listener.onFailure(error.getErrorBody());
                        }
                    });
        } else {
            Utility.errDialog(context.getResources().getString(R.string.network), context);
        }
    }
}
