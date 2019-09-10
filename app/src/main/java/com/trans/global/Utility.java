package com.trans.global;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.trans.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

import io.fabric.sdk.android.Fabric;


/**
 * Created by acer on 08-05-2017.
 */
public class Utility {

    public final static int REQUEST_STORAGE = 99;
    public final static int RESULT_TAKE = 100;
    public final static int RESULT_GALLARY = 101;
    public static File camera;
    public static Typeface normalFont, boldFont, boldItalicFont, lightFont,
            lightItalicFont, semiBoldFont, semiBoldItalicFont, boldExtraFont, boldExtraItalicFont, italicFont;
    public static Calendar cal;
    public static Date date;
    public static DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat inputFormate1 = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat isoFormate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    public static SimpleDateFormat inputFormate2 = new SimpleDateFormat("dd-MM-yyyy");
    public static SimpleDateFormat inputFormateExpense = new SimpleDateFormat("EEEE dd MMM yyyy");
    public static SimpleDateFormat inputFormateDepartReturn = new SimpleDateFormat("dd MMM yyyy");
    public static SimpleDateFormat inputFormateProcced = new SimpleDateFormat("dd MMM");
    public static SimpleDateFormat inputShorMonthName = new SimpleDateFormat("MMM");
    public static SimpleDateFormat inputFullMonthName = new SimpleDateFormat("MMMM");
    public static SimpleDateFormat inputTimeFormateExpense = new SimpleDateFormat("H:m");
    public static SimpleDateFormat outputTimeFormateExpense = new SimpleDateFormat("HH:mm:ss");
    public static SimpleDateFormat outputTimeFormateExpense1 = new SimpleDateFormat("HH:mm");
    public static DateFormat calenderFormat = new SimpleDateFormat("yyyy-MM-d HH:mm:ss");
    public static SimpleDateFormat outputFormate1 = new SimpleDateFormat("dd MMM yyyy");
    public static byte[] data;
    static int w20, w50, w40, w10, w15, w8, w5, w70, w120, w250, w290, h3;
    static int h20, h2, h10, h15, h8, h7, h5, h290;
    static int PICK_IMAGE_REQUEST;

    public static void calculate(int width, int height) {
        w8 = width * 8 / 320;
        w10 = width * 10 / 320;
        w5 = width * 5 / 320;
        w15 = width * 15 / 320;
        w50 = width * 50 / 320;
        w40 = width * 40 / 320;
        w20 = width * 20 / 320;
        w120 = width * 120 / 320;
        w70 = width * 70 / 320;
        w250 = width * 250 / 320;
        w290 = width * 290 / 320;

        h10 = height * 10 / 480;
        h2 = height * 2 / 480;
        h8 = height * 8 / 480;
        h3 = height * 3 / 480;
        h5 = height * 5 / 480;
        h7 = height * 7 / 480;
        h15 = height * 15 / 480;
        h20 = height * 20 / 480;
        h290 = height * 290 / 480;
    }


    public static String checkMinute(String minute) {
        if (Integer.parseInt(minute) < 10) {
            minute = "0" + minute;
        }
        return minute;
    }

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static void showFileChooser(Context context) {
        PICK_IMAGE_REQUEST = 2;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            ((Activity) context).startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"), PICK_IMAGE_REQUEST);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
        }
    }

    public static void shareApp(Context context, String s) {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "TradeKiya");
            String sAux = s + "\n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=com.trans \n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            context.startActivity(Intent.createChooser(i, "Share with.."));
        } catch (Exception e) {
            //e.toString();
        }
    }

    public static void rateApp(Context context) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.trans"));
            context.startActivity(i);
        } catch (Exception e) {
            //e.toString();
        }
    }


    public static void errDialog(String error, Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_contact_us);
        TextView txt_ok = dialog.findViewById(R.id.txt_ok);
        TextView txt_title = dialog.findViewById(R.id.txt_title);
        txt_title.setText(context.getResources().getString(R.string.alert));
        TextView txt_desc = dialog.findViewById(R.id.txt_desc);
        txt_desc.setText(error);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        txt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        /*final AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog2.setTitle(context.getResources().getString(R.string.alert));

        // Setting Dialog Message
        alertDialog2.setMessage(error);

        alertDialog2.setPositiveButton(context.getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();
                    }
                });

        alertDialog2.show();*/
    }

    public static void checkOrCreatePath(String directoryName) {
        File directory = new File(directoryName);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                Log.e("Tag", "Successfully created the parent dir:" + directory.getName());
            } else {
                Log.e("Tag", "Failed to create the parent dir:" + directory.getName());
            }
        }
    }

    public static void errDialogTryAgain(String error, final Context context) {
        final AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(context);
        alertDialog2.setTitle(context.getResources().getString(R.string.alert));
        alertDialog2.setMessage(error);
        alertDialog2.setCancelable(false);
        alertDialog2.setPositiveButton(context.getResources().getString(R.string.try_again),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog

                        dialog.cancel();
                    }
                });

        alertDialog2.show();
    }

    public static ProgressDialog showProgressDialog(Context context) {
        ProgressDialog progressDoalog = ProgressDialog.show(context, null, null);
        progressDoalog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDoalog.setCancelable(false);
        ProgressBar progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyle);
        progressBar.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(R.color.progress_bar), PorterDuff.Mode.SRC_IN);
        progressDoalog.setContentView(progressBar);
        return progressDoalog;
    }

    public static void dismissProgressDialog(ProgressDialog progressDoalog) {
        progressDoalog.dismiss();
    }


    public static String getFormattedDate(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateNew = format.parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateNew);
        //2nd of march 2015
        int day = cal.get(Calendar.DATE);

        switch (day % 10) {
            case 1:
                return new SimpleDateFormat("d'st' MMMM yyyy").format(dateNew);
            case 2:
                return new SimpleDateFormat("d'nd' MMMM yyyy").format(dateNew);
            case 3:
                return new SimpleDateFormat("d'rd' MMMM yyyy").format(dateNew);
            default:
                return new SimpleDateFormat("d'th' MMMM yyyy").format(dateNew);
        }
    }


    public static void crashLytics(Context context) {
        if (Build.VERSION.SDK_INT > 19) {
            Fabric.with(context, new Crashlytics());
        }
    }


    public static boolean checkAndRequestPermissions(Context context) {
        int CAMERA = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);

        int READ_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        int WRITE_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        int CALL_PHONE = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CALL_PHONE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (CAMERA != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (READ_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (WRITE_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (CALL_PHONE != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) context,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    GlobalAppConfiguration.REQUEST_PERMISSION_CAM_STORAGE);
            return false;
        }
        return true;
    }

    public static void permissionForLocation(final Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }
    }


    public static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static double getLattitude(Context context) {
        //permissionForLocation(context);
        GPSTracker gps = new GPSTracker(context);
        double latitude = 0.0;
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
        } else {
            gps.showSettingsAlert();
        }
        return latitude;
    }

    public static String getCurrentLocation(Context context) {
        GPSTracker gps = new GPSTracker(context);

        String lat_long = "";

        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            lat_long = latitude + "," + longitude;
        } else {
            gps.showSettingsAlert();
        }
        return lat_long;
    }

    public static double getLongitude(Context context) {
        // permissionForLocation(context);
        GPSTracker gps = new GPSTracker(context);
        double longitude = 0.0;
        if (gps.canGetLocation()) {
            longitude = gps.getLongitude();
        } else {
            //gps.showSettingsAlert();
        }
        return longitude;
    }

    public static File compressImage(String path, Context context, String name) {
        Bitmap bmp = BitmapFactory.decodeFile(path);
        if (bmp.getHeight() > 150 && bmp.getWidth() > 150) {
            File filesDir = context.getFilesDir();
            File imageFile = new File(filesDir, name);
            OutputStream os;
            try {
                os = new FileOutputStream(imageFile);
                bmp.compress(Bitmap.CompressFormat.JPEG, 50, os);
                os.flush();
                os.close();
            } catch (Exception e) {

            }

            return imageFile;
        } else
            return new File(path);
    }


    public static void stopProgressBar(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    public static DisplayImageOptions getProfileImageOptions() {
        return new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.no_profile)
                .showImageOnLoading(R.drawable.no_profile)
                .showImageOnFail(R.drawable.no_profile)
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    public static String getCompleteAddressString(Context context, double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(",");
                }
                strAdd = strReturnedAddress.toString();
                //Log.e("Tag My Current address", strReturnedAddress.toString());
            } else {
                //Log.e("Tag My Current address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //Log.e("Tag My Current address", "Canont get Address!");
        }
        return strAdd;
    }


    public static void gotoNext(Context context, Class aClass) {
        Intent i = new Intent(context, aClass);
        context.startActivity(i);
        ((Activity) context).overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }

    public static void gotoBack(Context context) {
        ((Activity) context).finish();
        ((Activity) context).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }


    public static boolean checkPermissionForStorage(Context context) {
        int result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        return result1 == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermissionForStorage(Context context) {
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE);
    }

    public static boolean checkIfAlreadyCameraPermission(Context context) {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        int result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestForCameraPermission(Context context) {
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE);
    }

    public static void openCamera(Context context) {
        if (!checkIfAlreadyCameraPermission(context)) {
            requestForCameraPermission(context);
        } else {
            Utility.imgfrmcptr(context);
        }
    }

    public static void openGallry(Context context) {
        if (!checkIfAlreadyCameraPermission(context)) {
            requestForCameraPermission(context);
        } else {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            ((Activity) context).startActivityForResult(photoPickerIntent, RESULT_GALLARY);
        }
    }

    public static void imgfrmcptr(Context context) {
        camera = new File(Environment.getExternalStorageDirectory(), "IMG_" + gen() + "_" + gen() + "_temp.jpg");
        Log.e("Tag", "URI UTILITY : CAM OPEN");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri photoURI;
        if (Build.VERSION.SDK_INT >= 24) {
            photoURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".com.trans.provider", camera);
        } else {
            photoURI = Uri.fromFile(camera);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Log.e("Tag", "URI UTILITY : " + photoURI);
        ((Activity) context).startActivityForResult(intent, RESULT_TAKE);
    }

    public static DisplayImageOptions getImageOptions() {
        return new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.no_image)
                .showImageOnLoading(R.drawable.no_image)
                .showImageOnFail(R.drawable.no_image)
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }


    public static void setGridViewHeightBasedOnChildren(GridView gridView,
                                                        int columncount) {
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        int size = gridView.getAdapter().getCount();
        int dynamicHeight = getTotalHeightofListView(gridView);
        if (size % columncount == 0) {
            params.height = (dynamicHeight / columncount) + 25;
        } else {
            params.height = (dynamicHeight / columncount)
                    + (dynamicHeight / size);
        }
        gridView.setLayoutParams(params);
        gridView.requestLayout();
    }

    public static int getTotalHeightofListView(AdapterView argAdapterView) {
        Adapter mAdapter = argAdapterView.getAdapter();
        if (mAdapter == null) {
            // pre-condition
            return 0;
        }
        int totalHeight = 0;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View mView = mAdapter.getView(i, null, argAdapterView);
            mView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            totalHeight += mView.getMeasuredHeight();
        }
        return totalHeight;
    }

    public static DisplayImageOptions getProfile() {
        return new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.no_profile)
                .showImageOnLoading(R.drawable.no_profile)
                .showImageOnFail(R.drawable.no_profile)
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    public static void setStatusColor(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            //window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));

        }
    }

    public static void setStatusColorRed(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            Drawable background = ((Context) activity).getResources().getDrawable(R.drawable.ic_topbar);
            //window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            //window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }

    public static void expand(final View v, int txt_height) {
        try {
            v.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            final int targetHeight = txt_height;
            v.getLayoutParams().height = 1;
            v.setVisibility(View.VISIBLE);
            Animation a = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime,
                                                   Transformation t) {
                    int temp = (interpolatedTime == 1 ? LinearLayout.LayoutParams.WRAP_CONTENT : (int) (targetHeight * interpolatedTime));
                    if (temp > 0) {
                        v.getLayoutParams().height = temp;
                    }
                    v.requestLayout();
                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };

            a.setDuration(300);
            v.startAnimation(a);
        } catch (Exception e) {
            //  Log.d("tag->exception",e.toString());
        }
    }

    public static void collapse(final View v) {
        try {
            final int initialHeight = v.getMeasuredHeight();
            Animation a = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime,
                                                   Transformation t) {
                    if (interpolatedTime == 1) {
                        v.setVisibility(View.GONE);
                    } else {
                        v.getLayoutParams().height = initialHeight
                                - (int) (initialHeight * interpolatedTime);
                        v.requestLayout();
                    }
                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };
            a.setDuration(300);
            v.startAnimation(a);
        } catch (Exception e) {
        }
    }

    public static boolean isValidMobile(String phone) {
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() > 5 && phone.length() <= 13;
        }
        return false;
    }

    public static String getRealPathFromURI(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (uri != null) {
            if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    if (new File(Environment.getExternalStorageDirectory() + "/" + split[1]).exists()) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    } else if (new File("storage/" + split[0] + "/" + split[1]).exists()) {
                        return "storage/" + split[0] + "/" + split[1];
                    } else {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                } else if (isDownloadsDocument(uri)) {
                    final String id = DocumentsContract.getDocumentId(uri);
                    uri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                } else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    if ("image".equals(type)) {
                        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    selection = "_id=?";
                    selectionArgs = new String[]{
                            split[1]
                    };
                }
            }

            if ("content".equalsIgnoreCase(uri.getScheme())) {
                String[] projection = {
                        MediaStore.Images.Media.DATA
                };
                Cursor cursor = null;
                try {
                    cursor = context.getContentResolver()
                            .query(uri, projection, selection, selectionArgs, null);
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    if (cursor.moveToFirst()) {
                        return cursor.getString(column_index);
                    }
                } catch (Exception e) {
                }
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    public static int gen() {
        Random r = new Random();
        return ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
    }

    @SuppressLint("NewApi")
    public static String getState(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static void hideSoftKeyboard(View view, Context context) {

        try {
            InputMethodManager inputManager = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);

            if (view != null) {
                inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static String getCurrentDateTime() {
        return new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());

    }

    public static String getCurrentDateTimeServerFormate() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

    }

    public static String getCurrentDateTimeForSync() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());

    }

    //  2018-12-01
    public static String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
    }

    //22-12-2018
    public static String getCurrentDate1() {
        return new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
    }

    public static String getCurrentTime() {
        return new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    }


    public static String setDateShortFormate(String dateTime) {
        try {
            Date date = originalFormat.parse(dateTime);
            dateTime = inputFormate1.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    public static void openFile(Activity context, String url, Boolean isWeb) {
        Uri uri = null;
        File file = new File(url);

        if (isWeb)
            uri = Uri.fromFile(file);
        else
            uri = Uri.parse(url);

        Intent intent = new Intent(Intent.ACTION_VIEW);

        if (url.toLowerCase().contains(".doc") || url.toLowerCase().contains(".docx")) {
            // Word document
            intent.setDataAndType(uri, "application/msword");
        } else if (url.toLowerCase().contains(".pdf")) {
            // PDF file
            intent.setDataAndType(uri, "application/pdf");
        } else if (url.toLowerCase().contains(".ppt") || url.toLowerCase().contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if (url.toLowerCase().contains(".xls") || url.toLowerCase().contains(".xlsx")) {
            // Excel file
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if (url.toLowerCase().contains(".zip") || url.toLowerCase().contains(".rar")) {
            // WAV audio file
            intent.setDataAndType(uri, "application/x-wav");
        } else if (url.toLowerCase().contains(".rtf")) {
            // RTF file
            intent.setDataAndType(uri, "application/rtf");
        } else if (url.toLowerCase().contains(".wav") || url.toLowerCase().contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav");
        } else if (url.toLowerCase().contains(".gif")) {
            // GIF file
            intent.setDataAndType(uri, "image/gif");
        } else if (url.toLowerCase().toLowerCase().contains(".jpg") || url.toLowerCase().contains(".jpeg") || url.toLowerCase().contains(".png")) {
            //JPG file
            intent.setDataAndType(uri, "image/*");
        } else if (url.toLowerCase().contains(".txt")) {
            // Text file
            intent.setDataAndType(uri, "text/plain");
        } else if (url.toLowerCase().contains(".3gp") || url.toLowerCase().contains(".mpg") || url.toLowerCase().contains(".mpeg") || url.toLowerCase().contains(".mpe") || url.toLowerCase().contains(".mp4") || url.toLowerCase().contains(".avi")) {
            // Video files
            intent.setDataAndType(uri, "video/*");
        } else {
            //intent.setDataAndType(uri, "**");
            intent.setDataAndType(uri, "application/*");
        }


        try {
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e("Tag -> ", e.getMessage());
            WebView mWebView = new WebView(context);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + uri);
            context.setContentView(mWebView);
        }

    }

    public static String getFileNameFromURL(String url) {
        if (url == null) {
            return "";
        }
        try {
            URL resource = new URL(url);
            String host = resource.getHost();
            if (host.length() > 0 && url.endsWith(host)) {
                // handle ...example.com
                return "";
            }
        } catch (MalformedURLException e) {
            return "";
        }

        int startIndex = url.lastIndexOf('/') + 1;
        int length = url.length();

        // find end index for ?
        int lastQMPos = url.lastIndexOf('?');
        if (lastQMPos == -1) {
            lastQMPos = length;
        }

        // find end index for #
        int lastHashPos = url.lastIndexOf('#');
        if (lastHashPos == -1) {
            lastHashPos = length;
        }

        // calculate the end index
        int endIndex = Math.min(lastQMPos, lastHashPos);
        return url.substring(startIndex, endIndex);
    }

    public static String getDepartReturnDateFormat(String dateTime) {
        try {
            Date date = inputFormate1.parse(dateTime);
            dateTime = inputFormateDepartReturn.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    public static String getDepartReturnDateFormatFromIOS(String dateTime) {
        try {
            Date date = isoFormate.parse(dateTime);
            dateTime = inputFormateDepartReturn.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    public static String getDepartProceedFormat(String dateTime) {
        try {
            Date date = inputFormate1.parse(dateTime);
            dateTime = inputFormateProcced.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    public static String getShortMonthName(String dateTime) {
        try {
            Date date = inputFormate1.parse(dateTime);
            dateTime = inputShorMonthName.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    public static String getFullMonthName(String dateTime) {
        try {
            Date date = inputFormate1.parse(dateTime);
            dateTime = inputFullMonthName.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    public static String getDepartReturnDay(String dateTime) {
        try {
            Date date = inputFormate1.parse(dateTime);
            dateTime = new SimpleDateFormat("EEEE").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    public static String getDepartReturnDayFromISO(String dateTime) {
        try {
            Date date = isoFormate.parse(dateTime);
            dateTime = new SimpleDateFormat("EEEE").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    public static String getTomorrowDate(String toDayDate) {
        try {
            Date dateToDay = Utility.inputFormate1.parse(toDayDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateToDay);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            Date tomorrow = calendar.getTime();
            return new SimpleDateFormat("yyyy-MM-dd").format(tomorrow);
        } catch (ParseException e) {
            e.printStackTrace();
            return toDayDate;
        }
    }


    public static String setDateFromCalDialog(String dateTime) {
        try {
            DateFormat fromFormat = new SimpleDateFormat("yyyy-M-d");
            fromFormat.setLenient(false);
            Date date = fromFormat.parse(dateTime);
            dateTime = inputFormate1.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    public static String setDateFromCalDialogExpense(String dateTime) {
        try {
            DateFormat fromFormat = new SimpleDateFormat("yyyy-M-d");
            fromFormat.setLenient(false);
            Date date = fromFormat.parse(dateTime);
            dateTime = inputFormateExpense.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }


    public static String setTimeFromDialogExpense(String dateTime) {
        try {
            inputTimeFormateExpense.setLenient(false);
            Date date = inputTimeFormateExpense.parse(dateTime);
            dateTime = outputTimeFormateExpense.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    public static String setTimeFromDialogExpense1(String dateTime) {
        try {
            inputTimeFormateExpense.setLenient(false);
            Date date = inputTimeFormateExpense.parse(dateTime);
            dateTime = outputTimeFormateExpense1.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }


    public static String datesuffix(int n) {
        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    public static void setCommonListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        if (adapter == null)
            return;

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();

        }
        totalHeight += (listView.getPaddingTop() + listView.getPaddingBottom());
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (adapter.getCount() - 1)); // 476
        listView.setLayoutParams(params);
        listView.requestLayout();
        listView.setFocusable(false);

        if (adapter.getCount() > 0) {
            listView.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.GONE);
            return;
        }

    }


    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }
    }


    public static String getExpenseDateTime(String dateTime) {
        try {
            DateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            fromFormat.setLenient(false);
            DateFormat toFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm");
            toFormat.setLenient(false);
            Date date = fromFormat.parse(dateTime);
            dateTime = toFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    public static int getStatusBarHeight(final Context context) {
        final Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
            return resources.getDimensionPixelSize(resourceId);
        else
            return (int) Math.ceil((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? 24 : 25) * resources.getDisplayMetrics().density);
    }


    public static Typeface getNormalFont(Context context) {
        if (normalFont == null) {
            normalFont = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        }
        return normalFont;
    }

    public static Typeface getBoldFont(Context context) {
        if (boldFont == null) {
            boldFont = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");
        }
        return boldFont;
    }

    public static Typeface getBoldItalicFont(Context context) {
        if (boldItalicFont == null) {
            boldItalicFont = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-BoldItalic.ttf");
        }
        return boldItalicFont;
    }

    public static Typeface getLightFont(Context context) {
        if (lightFont == null) {
            lightFont = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Light.ttf");
        }
        return lightFont;
    }

    public static Typeface getLightItalicFont(Context context) {
        if (lightItalicFont == null) {
            lightItalicFont = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-LightItalic.ttf");
        }
        return lightItalicFont;
    }

    public static Typeface getSemiBoldFont(Context context) {
        if (semiBoldFont == null) {
            semiBoldFont = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Semibold.ttf");
        }
        return semiBoldFont;
    }

    public static Typeface getSemiBoldItalicFont(Context context) {
        if (semiBoldItalicFont == null) {
            semiBoldItalicFont = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-SemiboldItalic.ttf");
        }
        return semiBoldItalicFont;
    }

    public static Typeface getBoldExtraFont(Context context) {
        if (boldExtraFont == null) {
            boldExtraFont = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-ExtraBold.ttf");
        }
        return boldExtraFont;
    }

    public static Typeface getBoldExtraItalicFont(Context context) {
        if (boldExtraItalicFont == null) {
            boldExtraItalicFont = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-ExtraBoldItalic.ttf");
        }
        return boldExtraItalicFont;
    }

    public static Typeface getItalicFont(Context context) {
        if (italicFont == null) {
            italicFont = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Italic.ttf");
        }
        return italicFont;
    }

    //  set max length of edit text
    public static void setEditTextMaxLength(EditText editText, int length) {
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(length);
        editText.setFilters(filterArray);
    }

    @SuppressLint("NewApi")
    public static void setNewGridViewHeightBasedOnChildren(GridView gridView, int columns) {
        ListAdapter adapter = gridView.getAdapter();
        if (adapter == null) {
            return;
        }
        if (adapter.getCount() > 0) {
            int totalHeight = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(gridView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            totalHeight += gridView.getPaddingTop() + 1;
            // here done adapter.getCount() - 1 for managing space in top user grid
            for (int i = 0; i <= Math.ceil((adapter.getCount() - 1) / columns); i++) {
                View listItem = adapter.getView(i, null, gridView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight() + (gridView.getVerticalSpacing());
            }
            ViewGroup.LayoutParams params = gridView.getLayoutParams();
            params.height = (int) (totalHeight + (columns * (Math.ceil((adapter.getCount() - 1) / columns)) - 1)); // 476
            gridView.setLayoutParams(params);
            gridView.requestLayout();
            gridView.setFocusable(false);
        }
        if (adapter.getCount() > 0) {
            gridView.setVisibility(View.VISIBLE);
        } else {
            gridView.setVisibility(View.GONE);
            return;
        }
    }

    public static int getTotalHeightofListView(ListView listView) {

        ListAdapter mAdapter = listView.getAdapter();

        int totalHeight = 0;

        for (int i = 0; i < mAdapter.getCount(); i++) {
            View mView = mAdapter.getView(i, null, listView);

            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),

                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            totalHeight += mView.getMeasuredHeight();

        }
        return totalHeight;
    }

    public static int getViewHeight(View view) {
        WindowManager wm =
                (WindowManager) view.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        int deviceWidth;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            display.getSize(size);
            deviceWidth = size.x;
        } else {
            deviceWidth = display.getWidth();
        }

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthMeasureSpec, heightMeasureSpec);
        return view.getMeasuredHeight(); //        view.getMeasuredWidth();
    }

    public static int getViewWidth(View view) {
        WindowManager wm =
                (WindowManager) view.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        int deviceWidth;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            display.getSize(size);
            deviceWidth = size.x;
        } else {
            deviceWidth = display.getWidth();
        }

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthMeasureSpec, heightMeasureSpec);
        return view.getMeasuredWidth(); //        view.getMeasuredHeight();
    }

    public static void showLog(Object s) {
        Log.e("Tag -> ", String.valueOf(s));
    }

    public static void showToast(Context context, String s) {
        Toast.makeText(context, String.valueOf(s), Toast.LENGTH_SHORT).show();
    }

    public static long milliseconds(String date) {
        //String date_ = date;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date mDate = sdf.parse(date);
            long timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);
            return timeInMilliseconds;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return 0;
    }
}
