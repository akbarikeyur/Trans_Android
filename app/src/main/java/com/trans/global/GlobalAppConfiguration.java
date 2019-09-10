package com.trans.global;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.trans.R;
import com.trans.api.ApiServer;
import com.trans.model.MenuData;
import com.trans.sharedPrefrence.AppPrefrece;

import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;

/**
 * Created by Assure on 15-03-2018.
 */

public class GlobalAppConfiguration extends Application {
    public static final String ENVIRONMENT= PayPalConfiguration.ENVIRONMENT_SANDBOX;
    public static final String PAYPAL_KEY="AW_dm2k-qWUxKf0hPkSTIkKODH64twcmc6ZDBbXdl8PZ_bbGPlJuij9M3DuUo_kvQCiOsQwwm01mNVOk";
    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";
    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
    public static final String SHARED_PREF = "ah_firebase";
    public final static int REQUEST_STORAGE = 101;
    public final static int RESULT_TAKE = 102;
    public final static int RESULT_GALLARY = 103;
    public static final int REQUEST_PERMISSION_CAM_STORAGE = 1005;
    public static ArrayList<MenuData> menuDataList = new ArrayList<>();
    public static String auth = "simplerestapi", client = "frontend-client";
    private static GlobalAppConfiguration appInstance;
    Context context;

    public static synchronized GlobalAppConfiguration getInstance() {
        return appInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        context = this;
        appInstance = this;

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        ImageLoader.getInstance().init(config);

        menuDataList.add(new MenuData(context.getResources().getString(R.string.menu_bookin_history), R.drawable.ic_white_calendar, 0));
        menuDataList.add(new MenuData(context.getResources().getString(R.string.menu_about_us), R.drawable.ic_about_us, 0));
        menuDataList.add(new MenuData(context.getResources().getString(R.string.menu_terms_condition), R.drawable.ic_terms_conditions, 0));
        menuDataList.add(new MenuData(context.getResources().getString(R.string.menu_contact_us), R.drawable.ic_contactus, 0));
        menuDataList.add(new MenuData(context.getResources().getString(R.string.menu_logout), R.drawable.ic_logout, 0));

        new ApiServer(context);
        new AppPrefrece(context);

    }


}
