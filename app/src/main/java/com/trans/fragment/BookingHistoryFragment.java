package com.trans.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.trans.R;
import com.trans.activity.TicketActivity;
import com.trans.adapter.HistoryAdapter;
import com.trans.api.APIResponse;
import com.trans.api.ApiServer;
import com.trans.global.Utility;
import com.trans.model.HistoryData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by home on 14/07/18.
 */


@SuppressLint("ValidFragment")
public class BookingHistoryFragment extends Fragment implements View.OnClickListener {
    View view;
    Context context;
    ImageLoader imageLoader;
    int i;
    ListView lstv_hstry;
    TextView txt_no_ticket;
    ArrayList<HistoryData> historyData = new ArrayList<>();
    JSONArray historyJson = new JSONArray();
    HistoryAdapter historyAdapter;
    boolean hasMore = false, isApiCalling = false;
    int list_type = 0, position = 0;
    SwipeRefreshLayout swipe_refersh;

    @SuppressLint("ValidFragment")
    public BookingHistoryFragment(int i) {
        this.i = i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.fragment_booking_history, null, false);
        this.setHasOptionsMenu(true);
        initialize();
        setView(view);
        setData();
        setLitionar();
        setColor();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return view;
    }

    private void initialize() {
        context = getContext();
        Utility.crashLytics(context);

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        historyAdapter = new HistoryAdapter(context, historyData);
    }

    private void setView(View view) {
        txt_no_ticket = view.findViewById(R.id.txt_no_ticket);
        lstv_hstry = view.findViewById(R.id.lstv_hstry);
        swipe_refersh = view.findViewById(R.id.swipe_refersh);
        swipe_refersh.setColorSchemeColors(context.getResources().getColor(R.color.red_line));
        lstv_hstry.setAdapter(historyAdapter);
    }

    public void setData() {
        if (Utility.isNetworkAvailable(context)) {
            if (i == 0) {
                list_type = 2;
                getUpComingList(true);
            } else if (i == 1) {
                list_type = 1;
                getPastList();
            } else {
                list_type = 3;
                getCancelledList();
            }
        } else {
            Utility.errDialog(context.getResources().getString(R.string.network), context);
        }
    }

    private void getUpComingList(boolean is_dialog) {
        historyData.clear();
        historyJson = new JSONArray();
        viewBooking(is_dialog);

    }

    private void getPastList() {
        historyData.clear();
        historyJson = new JSONArray();
        viewBooking(false);

    }

    private void getCancelledList() {
        historyData.clear();
        historyJson = new JSONArray();
        viewBooking(false);
    }

    private void setLitionar() {
        lstv_hstry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                if (i == 0) {
                    position=pos;
                    Intent i = new Intent(context, TicketActivity.class);
                    Bundle bundle = new Bundle();
                    try {
                        bundle.putString("ticket", String.valueOf(historyJson.get(pos)));
                        bundle.putBoolean("fromHistory", true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    i.putExtras(bundle);
                    context.startActivity(i);
                    ((Activity) context).overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }
            }
        });

        lstv_hstry.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                final int lastItem = firstVisibleItem + visibleItemCount;

                if (lastItem == totalItemCount - 1) {
                    if (hasMore && !isApiCalling)
                        viewBooking(false);
                }
            }
        });

        swipe_refersh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utility.isNetworkAvailable(context)) {
                    if (i == 0) {
                        list_type = 2;
                        getUpComingList(false);
                    } else if (i == 1) {
                        list_type = 1;
                        getPastList();
                    } else {
                        list_type = 3;
                        getCancelledList();
                    }
                } else {
                    Utility.errDialog(context.getResources().getString(R.string.network), context);
                    swipe_refersh.setRefreshing(false);
                }
            }
        });
    }

    private void setColor() {

    }

    private void viewBooking(boolean isDialog) {
        isApiCalling = true;
        JSONObject object = new JSONObject();
        try {
            object.put("listType", list_type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiServer.getInstance().viewBookings(new APIResponse() {
            @Override
            public void onSuccess(JSONObject object) {
                if (ApiServer.getInstance().isSuccess(context, object)) {
                    try {
                        JSONArray jsonArray = object.getJSONArray("data");
                        hasMore = object.getBoolean("hasMore");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject temp = jsonArray.getJSONObject(i);
                            historyJson.put(temp);
                            String ticketNumber = temp.getString("ticketNumber");
                            String fare = "$" + temp.getJSONObject("price").getJSONObject("priceDetails").getString("totalCost");
                            int numOfSeat = temp.getInt("seats");
                            String from = "", to = "", departDate = "", departTime = "";

                            if (temp.getBoolean("specialNeedSeat")) {
                                numOfSeat = numOfSeat + 1;
                            }

                            if (!temp.getBoolean("isTwoWay")) {
                                from = temp.getJSONObject("fromStopDetails").getString("city");
                                to = temp.getJSONObject("toStopDetails").getString("city");
                                departTime = temp.getJSONObject("fromStopDetails").getJSONObject("departureTime").getString("hour") + ":"
                                        + temp.getJSONObject("fromStopDetails").getJSONObject("departureTime").getString("minute");
                                departDate = Utility.getDepartReturnDateFormatFromIOS(temp.getString("date"));
                                historyData.add(new HistoryData(ticketNumber, fare, from, to, numOfSeat + "", departDate, departTime, false, "", ""));
                            } else {

                                from = temp.getJSONObject("departBooking").getJSONObject("fromStopDetails").getString("city");
                                to = temp.getJSONObject("departBooking").getJSONObject("toStopDetails").getString("city");
                                departTime = temp.getJSONObject("departBooking").getJSONObject("fromStopDetails").getJSONObject("departureTime").getString("hour") + ":"
                                        + temp.getJSONObject("departBooking").getJSONObject("fromStopDetails").getJSONObject("departureTime").getString("minute");
                                departDate = Utility.getDepartReturnDateFormatFromIOS(temp.getJSONObject("departBooking").getString("date"));

                                String returnTime = temp.getJSONObject("returnBooking").getJSONObject("fromStopDetails").getJSONObject("departureTime").getString("hour") + ":"
                                        + temp.getJSONObject("returnBooking").getJSONObject("fromStopDetails").getJSONObject("departureTime").getString("minute");
                                String returnDate = Utility.getDepartReturnDateFormatFromIOS(temp.getJSONObject("returnBooking").getString("date"));

                                historyData.add(new HistoryData(ticketNumber, fare, from, to, numOfSeat + "", departDate, departTime, true, returnDate, returnTime));
                            }


                        }
                        if (historyData.size() > 0) {
                            txt_no_ticket.setVisibility(View.GONE);
                        } else {
                            txt_no_ticket.setVisibility(View.VISIBLE);
                        }
                        historyAdapter.notifyDataSetChanged();
                        isApiCalling = false;
                        swipe_refersh.setRefreshing(false);
                    } catch (JSONException e) {
                        Utility.showLog(e.getMessage());
                        e.printStackTrace();
                        isApiCalling = false;
                        swipe_refersh.setRefreshing(false);
                    }

                }
            }

            @Override
            public void onFailure(String error) {
                isApiCalling = false;
                swipe_refersh.setRefreshing(false);
            }
        }, context, isDialog, object);
    }

    @Override
    public void onResume() {
        if (TicketActivity.cancel) {
            if (i == 0) {

                Utility.showLog("Upcoming Resume");
                historyData.remove(position);
                historyAdapter.notifyDataSetChanged();
                if (historyData.size() > 0) {
                    txt_no_ticket.setVisibility(View.GONE);
                } else {
                    txt_no_ticket.setVisibility(View.VISIBLE);
                }
                TicketActivity.cancel=false;
            }
        }
        super.onResume();
    }

    @Override
    public void onClick(View view) {

    }

}