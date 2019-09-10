package com.trans.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.trans.R;
import com.trans.adapter.MenuAdapter;
import com.trans.adapter.SearchPagerAdapter;
import com.trans.api.APIResponse;
import com.trans.api.ApiServer;
import com.trans.global.GlobalAppConfiguration;
import com.trans.global.Utility;
import com.trans.model.FromToData;
import com.trans.model.MenuData;
import com.trans.sharedPrefrence.AppPrefrece;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    Context context;
    ImageLoader imageLoader;

    ImageView imgv_menu;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView txt_usr_name, txt_usr_email;
    ListView lstview;
    CircleImageView imgv_user;
    MenuAdapter adapter;

    ViewPager vpgr;
    TabLayout vpgr_tab;
    SearchPagerAdapter searchPagerAdapter;


    TextView txt_from_city, txt_from_country, txt_to_city, txt_to_country, txt_special_need, txt_search_bus,txt_wallet;
    TextView txt_depart_date, txt_depart_day, txt_return_date, txt_return_day, txt_round_trip, txt_seat, txt_disabality;
    ImageView imgv_up, imgv_down, imgv_cancel,imgv_wallet;
    public static int seat = 01;
    String departDate, returnDate;
    ArrayList<String> strings = new ArrayList<>();

    boolean doubleBackToExitPressedOnce = false;

    LinearLayout ll_from, ll_to;
    public static FromToData from, to;
    public static int fromPosition = -1, toPosition = -1;

    public static boolean specialNeed = false;
    public static boolean isTwoWay = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        setView();
        setCardView();
        setData();
        setColor();
        setCardData();
        setLitionar();
        setCardLitionar();
        setCardColorFilter();

    }

    private void initialize() {
        context = this;
        Utility.setStatusColorRed(this);
        Utility.crashLytics(context);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));

        strings.add(context.getResources().getString(R.string.one_way));
        strings.add(context.getResources().getString(R.string.round_trip));
        searchPagerAdapter = new SearchPagerAdapter(context, getSupportFragmentManager(), strings);

    }

    private void setCardView() {
        vpgr = findViewById(R.id.vpgr);
        vpgr_tab = findViewById(R.id.vpgr_tab);
        vpgr_tab.setTabMode(TabLayout.MODE_FIXED);
        vpgr_tab.setTabGravity(TabLayout.GRAVITY_FILL);

        txt_from_city = findViewById(R.id.txt_from_city);
        txt_from_country = findViewById(R.id.txt_from_country);
        txt_to_city = findViewById(R.id.txt_to_city);
        txt_to_country = findViewById(R.id.txt_to_country);
        txt_depart_date = findViewById(R.id.txt_depart_date);
        txt_depart_day = findViewById(R.id.txt_depart_day);
        txt_return_date = findViewById(R.id.txt_return_date);
        txt_return_day = findViewById(R.id.txt_return_day);
        txt_round_trip = findViewById(R.id.txt_round_trip);

        txt_seat = findViewById(R.id.txt_seat);
        imgv_up = findViewById(R.id.imgv_up);
        imgv_down = findViewById(R.id.imgv_down);
        imgv_wallet = findViewById(R.id.imgv_wallet);

        txt_disabality = findViewById(R.id.txt_disabality);
        txt_special_need = findViewById(R.id.txt_special_need);
        txt_search_bus = findViewById(R.id.txt_search_bus);

        ll_from = findViewById(R.id.ll_from);
        ll_to = findViewById(R.id.ll_to);
        imgv_cancel = findViewById(R.id.imgv_cancel);
    }

    private void setCardData() {
        imgv_cancel.setVisibility(View.GONE);
        specialNeed = false;
        seat = 01;
        departDate = Utility.getCurrentDate();
        vpgr.setAdapter(searchPagerAdapter);
        vpgr_tab.setupWithViewPager(vpgr);

        txt_to_city.setText("City");
        txt_to_country.setText("State");
        txt_from_city.setText("City");
        txt_from_country.setText("State");

        setSeatText(seat);

        txt_disabality.setText("1 of them have disability");

        txt_round_trip.setVisibility(View.VISIBLE);
        txt_return_date.setVisibility(View.GONE);
        txt_return_day.setVisibility(View.GONE);
        txt_disabality.setVisibility(View.GONE);

        txt_depart_date.setText(Utility.getDepartReturnDateFormat(departDate));
//        txt_return_date.setText(Utility.getDepartReturnDateFormat(returnDate));
        txt_depart_day.setText(Utility.getDepartReturnDay(departDate));
//        txt_return_day.setText(Utility.getDepartReturnDay(returnDate));
    }

    private void setCardLitionar() {
        imgv_up.setOnClickListener(this);
        imgv_down.setOnClickListener(this);
        txt_depart_date.setOnClickListener(this);
        txt_return_date.setOnClickListener(this);
        txt_round_trip.setOnClickListener(this);
        txt_special_need.setOnClickListener(this);
        txt_search_bus.setOnClickListener(this);
        ll_from.setOnClickListener(this);
        ll_to.setOnClickListener(this);
        imgv_cancel.setOnClickListener(this);

        vpgr.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == 0) {
                    txt_round_trip.setVisibility(View.VISIBLE);
                    txt_return_date.setVisibility(View.GONE);
                    txt_return_day.setVisibility(View.GONE);
                } else {
                    txt_round_trip.setVisibility(View.GONE);
                    returnDate = departDate;

                    txt_return_date.setVisibility(View.VISIBLE);
                    txt_return_day.setVisibility(View.VISIBLE);
                    txt_return_date.setText(Utility.getDepartReturnDateFormat(returnDate));
                    txt_return_day.setText(Utility.getDepartReturnDay(returnDate));
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void setCardColorFilter() {
        imgv_cancel.setColorFilter(context.getResources().getColor(R.color.red_image));
    }

    private void incrementSeat() {
        if (seat != 55) {
            seat++;
            setSeatText(seat);
        }
    }

    private void decrementSeat() {
        if (seat != 1) {
            seat--;
            setSeatText(seat);

        }
    }

    private void openDatePickerDepart() {
        String[] temp = departDate.split("-");

        int d, m, y;

        y = Integer.parseInt(temp[0]);
        m = Integer.parseInt(temp[1]) - 1;
        d = Integer.parseInt(temp[2]);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                departDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                txt_depart_date.setText(Utility.getDepartReturnDateFormat(departDate));
                txt_depart_day.setText(Utility.getDepartReturnDay(departDate));
            }
        }, y, m, d);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void openDatePickerReturn() {
        if (returnDate == null)
            returnDate = departDate;
        String[] temp = returnDate.split("-");

        int d, m, y;

        y = Integer.parseInt(temp[0]);
        m = Integer.parseInt(temp[1]) - 1;
        d = Integer.parseInt(temp[2]);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                returnDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                txt_return_date.setVisibility(View.VISIBLE);
                txt_return_day.setVisibility(View.VISIBLE);
                txt_return_date.setText(Utility.getDepartReturnDateFormat(returnDate));
                txt_return_day.setText(Utility.getDepartReturnDay(returnDate));
            }
        }, y, m, d);


        datePickerDialog.getDatePicker().setMinDate(Utility.milliseconds(departDate));
        datePickerDialog.show();
    }

    private void showDialogSpecialNeeds() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_confirmation);
        ImageView imgv_close = dialog.findViewById(R.id.imgv_close);
        TextView txt_message = dialog.findViewById(R.id.txt_message);
        TextView txt_no = dialog.findViewById(R.id.txt_no);
        TextView txt_yes = dialog.findViewById(R.id.txt_yes);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        txt_message.setText(context.getResources().getString(R.string.do_you_need_seat_for));

        imgv_close.setColorFilter(context.getResources().getColor(R.color.red_background));

        imgv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        txt_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        txt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_disabality.setVisibility(View.VISIBLE);
                imgv_cancel.setVisibility(View.VISIBLE);
                specialNeed = true;
                setSeatText(seat);
//                showDialogSpecialNeedsConfirmation();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void setSeatText(int set){
        if(set<10){
            txt_seat.setText("0"+set);
        }else{
            txt_seat.setText(set + "");
        }
    }

    private void showDialogSpecialNeedsConfirmation() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_confirmation);
        ImageView imgv_close = dialog.findViewById(R.id.imgv_close);
        View view = dialog.findViewById(R.id.view);
        TextView txt_message = dialog.findViewById(R.id.txt_message);
        TextView txt_no = dialog.findViewById(R.id.txt_no);
        TextView txt_yes = dialog.findViewById(R.id.txt_yes);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        txt_message.setText(context.getResources().getString(R.string.for_wheel_chair));
        txt_no.setText(context.getResources().getString(R.string.ok));


        imgv_close.setColorFilter(context.getResources().getColor(R.color.red_background));

        imgv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        txt_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        view.setVisibility(View.GONE);

        txt_yes.setVisibility(View.GONE);
        dialog.show();
    }

    private void setView() {
        imgv_menu = findViewById(R.id.imgv_menu);

        imgv_menu = findViewById(R.id.imgv_menu);
        navigationView = findViewById(R.id.nvViewList);
        drawerLayout = findViewById(R.id.drawer_lauout_list);
        txt_usr_name = findViewById(R.id.txt_usr_name);
        txt_usr_email = findViewById(R.id.txt_usr_mobile);
        lstview = findViewById(R.id.lstview);
        txt_wallet = findViewById(R.id.txt_wallet);
        imgv_user = findViewById(R.id.imgv_user);

    }

    private void setData() {
        imageLoader.displayImage(AppPrefrece.getInstance().getProfilePic(), imgv_user, Utility.getProfileImageOptions());
    }

    private void setLitionar() {

        imgv_menu.setOnClickListener(this);
        txt_usr_name.setOnClickListener(this);
        txt_usr_email.setOnClickListener(this);

        lstview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setDrawer();
                if (GlobalAppConfiguration.menuDataList.get(i).getTitle().equalsIgnoreCase(context.getResources().getString(R.string.menu_bookin_history))) {
                    if (AppPrefrece.getInstance().getLogin()){
                        Utility.gotoNext(context, BookingHistoryActivity.class);
                    }
                } else if (GlobalAppConfiguration.menuDataList.get(i).getTitle().equalsIgnoreCase(context.getResources().getString(R.string.menu_about_us))) {
                    Intent intent = new Intent(context, TextActivity.class);
                    intent.putExtra("title", context.getResources().getString(R.string.about_us));
                    intent.putExtra("desc", context.getResources().getString(R.string.about_desc));
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                } else if (GlobalAppConfiguration.menuDataList.get(i).getTitle().equalsIgnoreCase(context.getResources().getString(R.string.menu_terms_condition))) {
                    Intent intent = new Intent(context, TextActivity.class);
                    intent.putExtra("title", context.getResources().getString(R.string.terms_conditions));
                    intent.putExtra("desc", context.getResources().getString(R.string.terms_desc));
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                } else if (GlobalAppConfiguration.menuDataList.get(i).getTitle().equalsIgnoreCase(context.getResources().getString(R.string.menu_contact_us))) {
                    Utility.gotoNext(context, ContactUsActivity.class);
                } else if (GlobalAppConfiguration.menuDataList.get(i).getTitle().equalsIgnoreCase(context.getResources().getString(R.string.menu_logout))) {
                    logoutConfirm();
                }
            }
        });
    }

    public void setDrawer() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            drawerLayout.openDrawer(Gravity.LEFT);
        }
    }

    private void setColor() {
        imgv_menu.setColorFilter(context.getResources().getColor(R.color.white_image));
        imgv_wallet.setColorFilter(context.getResources().getColor(R.color.white_image));
    }

    @Override
    public void onClick(View view) {
        if (view == imgv_menu) {
            setDrawer();
        } else if (view == imgv_up) {
            incrementSeat();
        } else if (view == imgv_down) {
            decrementSeat();
        } else if (view == txt_depart_date) {
            openDatePickerDepart();
        } else if (view == txt_return_date) {
            openDatePickerReturn();
        } else if (view == txt_round_trip) {
            vpgr.setCurrentItem(1);
            txt_round_trip.setVisibility(View.GONE);
            openDatePickerReturn();
        } else if (view == txt_special_need) {
            showDialogSpecialNeeds();
        } else if (view == txt_search_bus) {
            TicketActivity.is_backed = false;
            checkValidation();
        } else if (view == txt_usr_name || view == txt_usr_email) {
            setDrawer();
            if (!txt_usr_name.getText().toString().equalsIgnoreCase("Guest User")) {
                Utility.gotoNext(context, EditProfileActivity.class);
            }
        } else if (view == ll_from) {
            Intent i = new Intent(context, FromToSelectionActivity.class);
            from = null;
            i.putExtra("isFrom", true);
            context.startActivity(i);
            ((Activity) context).overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
        } else if (view == ll_to) {
            if (txt_from_city.getText().toString().equalsIgnoreCase("City")) {
                Utility.showToast(context, "Please select departure city");
            } else {
                Intent i = new Intent(context, FromToSelectionActivity.class);
                to = null;
                i.putExtra("isFrom", false);
                context.startActivity(i);
                ((Activity) context).overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }

        } else if (view == imgv_cancel) {
            imgv_cancel.setVisibility(View.GONE);
            txt_disabality.setVisibility(View.GONE);
            specialNeed = false;
        }
    }

    private void checkValidation() {

        String error = "";

        if (from == null) {
            error = "Please select from city";
        } else if (to == null) {
            error = "Please select to city";
        }else if (seat == 0 && !specialNeed) {
            error = "Please add seat/special needs";
        }
        if (error.equalsIgnoreCase("")) {
            Intent i = new Intent(context, BusListActivity.class);

            if (vpgr.getCurrentItem() == 0) {
                isTwoWay = false;
            } else {
                isTwoWay = true;
            }

            Bundle bundle = new Bundle();
            bundle.putInt("1or2", vpgr.getCurrentItem() + 1);
            bundle.putString("from", txt_from_city.getText().toString());
            bundle.putString("to", txt_to_city.getText().toString());
            bundle.putString("departDate", departDate);
            bundle.putString("returnDate", returnDate);
            i.putExtras(bundle);
            context.startActivity(i);
            ((Activity) context).overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
        } else {
            Utility.showToast(context, error);
        }
    }

    private void logoutConfirm() {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_are_you_sure);
        TextView txt_msg = dialog.findViewById(R.id.txt_msg);
        TextView txt_no = dialog.findViewById(R.id.txt_no);
        TextView txt_yes = dialog.findViewById(R.id.txt_yes);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        txt_msg.setText(context.getResources().getString(R.string.are_you_sure_logout));

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
                AppPrefrece.getInstance().remove();
                Intent i = new Intent(context, LoginActivity.class);
                i.putExtra("fromBookNow", false);
                startActivity(i);
                ((Activity) context).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                ((Activity) context).finish();
            }
        });

        dialog.show();

    }

    @Override
    protected void onResume() {
        super.onResume();

        imageLoader.displayImage(AppPrefrece.getInstance().getProfilePic(), imgv_user, Utility.getProfileImageOptions());
        if (TicketActivity.is_backed) {
            TicketActivity.is_backed = false;
            from = null;
            to = null;
            fromPosition = -1;
            toPosition = -1;
            setCardData();

        }

        if (from != null && fromPosition != -1)
            setFromData();
        if (to != null && toPosition != -1)
            setToData();

        if (AppPrefrece.getInstance().getLogin()) {
            txt_usr_email.setVisibility(View.VISIBLE);
            details();
        } else {
            txt_usr_name.setText("Guest User");
            txt_usr_email.setVisibility(View.GONE);
        }

        if (GlobalAppConfiguration.menuDataList.get(GlobalAppConfiguration.menuDataList.size() - 1).getTitle().equalsIgnoreCase(context.getResources().getString(R.string.menu_logout))) {
            if (!AppPrefrece.getInstance().getLogin()) {
                GlobalAppConfiguration.menuDataList.remove(GlobalAppConfiguration.menuDataList.size() - 1);
            }
        } else {
            if (AppPrefrece.getInstance().getLogin()) {
                GlobalAppConfiguration.menuDataList.add(new MenuData(context.getResources().getString(R.string.menu_logout), R.drawable.ic_logout, 0));
            }
        }
        adapter = new MenuAdapter(context);
        lstview.setAdapter(adapter);
        Utility.setCommonListViewHeightBasedOnChildren(lstview);
    }

    private void setFromData() {
        txt_from_city.setText(from.getData().get(fromPosition).getCity());
        txt_from_country.setText(from.getData().get(fromPosition).getState());
    }

    private void setToData() {
        txt_to_city.setText(to.getData().get(toPosition).getCity());
        txt_to_country.setText(to.getData().get(toPosition).getState());
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    public void details() {
        ApiServer.getInstance().details(new APIResponse() {
            @Override
            public void onSuccess(JSONObject object) {
                if (ApiServer.getInstance().isSuccess(context, object)) {
                    try {
                        AppPrefrece.getInstance().setProfile(object.getJSONObject("data"));
                        txt_wallet.setText("$ "+object.getJSONObject("data").getString("appCredits"));
                        AppPrefrece.getInstance().setName(object.getJSONObject("data").getString("name"));
                        Log.e("Tag -> ","$ "+object.getJSONObject("data").getString("appCredits"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Utility.showLog(e.getMessage());
                    }
                    txt_usr_name.setText(AppPrefrece.getInstance().getName());
                    txt_usr_email.setText(AppPrefrece.getInstance().getEmail());
                }
            }

            @Override
            public void onFailure(String error) {

            }
        }, context, false);
    }


}

