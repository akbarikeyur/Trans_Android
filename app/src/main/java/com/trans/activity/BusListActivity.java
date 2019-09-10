package com.trans.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.trans.R;
import com.trans.adapter.BusAdapter;
import com.trans.adapter.DateRecycleAdapter;
import com.trans.adapter.SearchPagerAdapter;
import com.trans.api.APIResponse;
import com.trans.api.ApiServer;
import com.trans.global.Utility;
import com.trans.intrfc.ItemClick;
import com.trans.model.DateData;
import com.trans.model.JourneyData;
import com.trans.model.JourneyItem;
import com.trans.widget.AutofitTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by home on 12/07/18.
 */

public class BusListActivity extends BaseActivity implements View.OnClickListener, ItemClick {
    Context context;
    ImageView imgv_back;
    ImageLoader imageLoader;


    ViewPager vpgr;
    TabLayout vpgr_tab;
    SearchPagerAdapter searchPagerAdapter;
    TextView txt_from, txt_to, txt_departure, txt_no_bus;

    AutofitTextView txt_month;
    ArrayList<String> strings = new ArrayList<>();
    public static String departDate, returnDate;


    RecyclerView rcycl_date;
    DateRecycleAdapter dateRecycleAdapter;

    ListView lstv_bus;
    BusAdapter busAdapter;


    Bundle bundle;
    int departDatePosition = 0, returnDatePosition = 0;

    ArrayList<DateData> dateDataDeparture = new ArrayList<>();
    ArrayList<DateData> dateDataReturn = new ArrayList<>();

    public static JourneyItem journeyItemDeparture;
    public static JourneyItem journeyItemReturn;

    boolean isDeparture = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_list);
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

        if (!MainActivity.isTwoWay) {
            strings.add(context.getResources().getString(R.string.departure));
        } else {
            strings.add(context.getResources().getString(R.string.departure));
            strings.add(context.getResources().getString(R.string.returnn));
        }
        departDate = bundle.getString("departDate");
        returnDate = bundle.getString("returnDate");
        searchPagerAdapter = new SearchPagerAdapter(context, getSupportFragmentManager(), strings);
        busAdapter = new BusAdapter(context);
        dateRecycleAdapter = new DateRecycleAdapter(context, this);
    }

    private void setView() {
        imgv_back = findViewById(R.id.imgv_back);
        txt_from = findViewById(R.id.txt_from);
        txt_to = findViewById(R.id.txt_to);
        txt_month = findViewById(R.id.txt_month);
        txt_departure = findViewById(R.id.txt_departure);
        txt_no_bus = findViewById(R.id.txt_no_bus);


        vpgr = findViewById(R.id.vpgr);
        vpgr_tab = findViewById(R.id.vpgr_tab);
        vpgr_tab.setTabMode(TabLayout.MODE_FIXED);
        vpgr_tab.setTabGravity(TabLayout.GRAVITY_FILL);

        rcycl_date = findViewById(R.id.rcycl_date);
        rcycl_date.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        rcycl_date.setAdapter(dateRecycleAdapter);


        lstv_bus = findViewById(R.id.lstv_bus);
        lstv_bus.setAdapter(busAdapter);

    }

    private void setData() {

        txt_from.setText(bundle.getString("from"));
        txt_to.setText(bundle.getString("to"));
        vpgr.setAdapter(searchPagerAdapter);
        vpgr_tab.setupWithViewPager(vpgr);

        dateDataDeparture.add(new DateData(departDate, true, true));
        for(int i=0;i<3;i++){
            dateDataDeparture.add(new DateData(Utility.getTomorrowDate(dateDataDeparture.get(i).getDateOrigional()), false));
        }

        /*dateDataDeparture.add(new DateData(Utility.getTomorrowDate(dateDataDeparture.get(1).getDateOrigional()), false));
        dateDataDeparture.add(new DateData(Utility.getTomorrowDate(dateDataDeparture.get(2).getDateOrigional()), false));*/
        dateRecycleAdapter.addAllData(dateDataDeparture);

        journeyDetails(dateDataDeparture.get(0).getDateMilliSecond(), 0);

        if (!MainActivity.isTwoWay) {
            txt_departure.setVisibility(View.VISIBLE);
            vpgr_tab.setVisibility(View.GONE);
        } else {
            txt_departure.setVisibility(View.GONE);
            vpgr_tab.setVisibility(View.VISIBLE);
            setDateDataReturn(returnDate);
        }

    }

    public void setDateDataReturn(String startingDate) {
        returnDatePosition = 0;
        dateDataReturn.clear();
        dateDataReturn.add(new DateData(startingDate, true, true));
        for(int i=0;i<3;i++){
            dateDataReturn.add(new DateData(Utility.getTomorrowDate(dateDataReturn.get(i).getDateOrigional()), false));
        }

        /*dateDataReturn.add(new DateData(Utility.getTomorrowDate(dateDataReturn.get(1).getDateOrigional()), false));
        dateDataReturn.add(new DateData(Utility.getTomorrowDate(dateDataReturn.get(2).getDateOrigional()), false));*/
    }

    private void setLitionar() {
        imgv_back.setOnClickListener(this);
        lstv_bus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!MainActivity.isTwoWay) {

                    if (dateDataDeparture.get(departDatePosition).getJourneyData().getJournyItems().get(position).getAvailableSeats() < MainActivity.seat) {
                        Utility.showToast(context, "Avialable seats are not enough");
                    } else if (MainActivity.specialNeed && !dateDataDeparture.get(departDatePosition).getJourneyData().getJournyItems().get(position).isSpecialNeedSeatAvailable()) {
                        Utility.showToast(context, "Special needs are not avialable");
                    } else {
                        removeDepartJourneySelection();
                        dateDataDeparture.get(departDatePosition).getJourneyData().getJournyItems().get(position).setSelected(true);
                        moveNext(position);
                    }


                } else {
                    if (vpgr.getCurrentItem() == 0) {
                        if (dateDataDeparture.get(departDatePosition).getJourneyData().getJournyItems().get(position).getAvailableSeats() < MainActivity.seat) {
                            Utility.showToast(context, "Avialable seats are not enough");
                        } else if (MainActivity.specialNeed && !dateDataDeparture.get(departDatePosition).getJourneyData().getJournyItems().get(position).isSpecialNeedSeatAvailable()) {
                            Utility.showToast(context, "Special needs are not avialable");
                        } else {
                            isDeparture = true;
                            removeDepartJourneySelection();
                            dateDataDeparture.get(departDatePosition).getJourneyData().getJournyItems().get(position).setSelected(true);
                            vpgr.setCurrentItem(1);
                        }

                    } else {

                        if (dateDataReturn.get(returnDatePosition).getJourneyData().getJournyItems().get(position).getAvailableSeats() < MainActivity.seat) {
                            Utility.showToast(context, "Avialable seats are not enough");
                        } else if (MainActivity.specialNeed && !dateDataReturn.get(returnDatePosition).getJourneyData().getJournyItems().get(position).isSpecialNeedSeatAvailable()) {
                            Utility.showToast(context, "Special needs are not avialable");
                        } else {
                            removeReturnJourneySelection();
                            dateDataReturn.get(returnDatePosition).getJourneyData().getJournyItems().get(position).setSelected(true);
                            moveNext(position);
                        }

                    }
                }
            }
        });


        vpgr.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == 0) {
                    dateRecycleAdapter.addAllData(dateDataDeparture);
                    setDepartureDateData(departDatePosition);
                    rcycl_date.scrollToPosition(departDatePosition);
                } else {
                    if (isDeparture) {
                        dateRecycleAdapter.addAllData(dateDataReturn);
                        if (dateDataReturn.get(0).getJourneyData() == null) {
                            journeyDetails(dateDataReturn.get(0).getDateMilliSecond(), 0);
                        } else {
                            setReturnDateData(returnDatePosition);
                            rcycl_date.scrollToPosition(returnDatePosition);
                        }
                    } else {
                        vpgr.setCurrentItem(0);
                        Utility.showToast(context, "Please select departure bus first");
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void moveNext(int position) {
        Intent i = new Intent(context, ProccedActivity.class);

        for (int k = 0; k < dateDataDeparture.size(); k++) {
            if (dateDataDeparture.get(k).getJourneyData() != null)
                for (int j = 0; j < dateDataDeparture.get(k).getJourneyData().getJournyItems().size(); j++) {
                    if (dateDataDeparture.get(k).getJourneyData().getJournyItems().get(j).isSelected()) {
                        journeyItemDeparture = dateDataDeparture.get(k).getJourneyData().getJournyItems().get(j);
                        break;
                    }
                }
        }


        if (MainActivity.isTwoWay) {
            for (int k = 0; k < dateDataReturn.size(); k++) {
                if (dateDataReturn.get(k).getJourneyData() != null)
                    for (int j = 0; j < dateDataReturn.get(k).getJourneyData().getJournyItems().size(); j++) {
                        if (dateDataReturn.get(k).getJourneyData().getJournyItems().get(j).isSelected()) {
                            journeyItemReturn = dateDataReturn.get(k).getJourneyData().getJournyItems().get(position);
                            break;
                        }
                    }
            }
        }
        i.putExtras(bundle);
        context.startActivity(i);
        ((Activity) context).overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }

    private void setColor() {
        imgv_back.setColorFilter(context.getResources().getColor(R.color.white_image));
    }

    @Override
    public void onClick(View view) {
        if (view == imgv_back) {
            onBackPressed();
        }
    }

    @Override
    protected void onResume() {

        if(!MainActivity.isTwoWay){
            rcycl_date.scrollToPosition(departDatePosition);
        }
        super.onResume();
        journeyItemDeparture = null;
        journeyItemReturn = null;
        if (TicketActivity.is_backed) {
            onBackPressed();
        }
        busAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utility.gotoBack(context);
    }

    public void journeyDetails(long date, final int position) {
        JSONObject object = new JSONObject();
        try {
            if (!MainActivity.isTwoWay || vpgr.getCurrentItem() == 0) {
                object.put("from", MainActivity.from.getData().get(MainActivity.fromPosition).getId());
                object.put("to", MainActivity.to.getData().get(MainActivity.toPosition).getId());
            } else {
                object.put("to", MainActivity.from.getData().get(MainActivity.fromPosition).getId());
                object.put("from", MainActivity.to.getData().get(MainActivity.toPosition).getId());
            }
            object.put("specialNeedSeat", MainActivity.specialNeed);
            object.put("seats", MainActivity.seat);
            object.put("date", date);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiServer.getInstance().journeyDetails(new APIResponse() {
            @Override
            public void onSuccess(JSONObject object) {
                if (ApiServer.getInstance().isSuccess(context, object)) {
                    if (!MainActivity.isTwoWay || vpgr.getCurrentItem() == 0) {
                        dateDataDeparture.get(position).setJourneyData(new Gson().fromJson(object.toString(), JourneyData.class));
                        setDepartureDateData(position);
                    } else {
                        dateDataReturn.get(position).setJourneyData(new Gson().fromJson(object.toString(), JourneyData.class));
                        setReturnDateData(position);
                    }

                }
            }

            @Override
            public void onFailure(String error) {

            }
        }, context, true, object);
    }

    @Override
    public void itemClick(int position, String id) {
        if (!MainActivity.isTwoWay || vpgr.getCurrentItem() == 0) {
            departDatePosition = position;
            setDepartureDateData(position);
            departDate = dateDataDeparture.get(position).getDateOrigional();

            if (MainActivity.isTwoWay) {
                try {
                    Date date1 = Utility.inputFormate1.parse(dateDataDeparture.get(dateDataDeparture.size() - 1).getDateOrigional());
                    Date date2 = Utility.inputFormate1.parse(dateDataReturn.get(0).getDateOrigional());
                    if (date1.after(date2)) {
                        setDateDataReturn(Utility.getTomorrowDate(departDate));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else {
            returnDatePosition = position;
            returnDate = dateDataReturn.get(position).getDateOrigional();
            setReturnDateData(position);
        }
    }

    public void setDepartureDateData(int position) {
        if (dateDataDeparture.get(position).getJourneyData() == null) {
            journeyDetails(dateDataDeparture.get(position).getDateMilliSecond(), position);
        } else {
            busAdapter.addAllData(dateDataDeparture.get(position).getJourneyData().getJournyItems());
        }
        txt_month.setText(Utility.getFullMonthName(dateDataDeparture.get(position).getDateOrigional()));
    }

    public void setReturnDateData(int position) {
        if (dateDataReturn.get(position).getJourneyData() == null) {
            journeyDetails(dateDataReturn.get(position).getDateMilliSecond(), position);
        } else {
            busAdapter.addAllData(dateDataReturn.get(position).getJourneyData().getJournyItems());
        }
        txt_month.setText(Utility.getFullMonthName(dateDataReturn.get(position).getDateOrigional()));
    }

    public void removeReturnJourneySelection() {
        for (int i = 0; i < dateDataReturn.size(); i++) {
            if (dateDataReturn.get(i).getJourneyData() != null)
                for (int j = 0; j < dateDataReturn.get(i).getJourneyData().getJournyItems().size(); j++) {
                    dateDataReturn.get(i).getJourneyData().getJournyItems().get(j).setSelected(false);
                }
        }
    }

    public void removeDepartJourneySelection() {
        for (int i = 0; i < dateDataDeparture.size(); i++) {
            if (dateDataDeparture.get(i).getJourneyData() != null)
                for (int j = 0; j < dateDataDeparture.get(i).getJourneyData().getJournyItems().size(); j++) {
                    dateDataDeparture.get(i).getJourneyData().getJournyItems().get(j).setSelected(false);
                }
        }
    }
}
