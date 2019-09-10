package com.trans.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.trans.R;
import com.trans.adapter.FromToAdapter;
import com.trans.api.APIResponse;
import com.trans.api.ApiServer;
import com.trans.global.Utility;
import com.trans.intrfc.ItemClick;
import com.trans.model.FromToData;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by home on 12/07/18.
 */

public class FromToSelectionActivity extends BaseActivity implements View.OnClickListener, ItemClick {
    Context context;
    ImageView imgv_back;
    TextView txt_title;
    ImageLoader imageLoader;
    boolean isFrom;
    ListView lstv_from_to;
    EditText edt_search;
    TextView txt_no_city;
    FromToAdapter fromToAdapter;
    boolean hasMore = false, isApiCalling = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_from_to_selection);
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
        isFrom = getIntent().getBooleanExtra("isFrom", true);
        if (isFrom)
            fromToAdapter = new FromToAdapter(context, this);
        else
            fromToAdapter = new FromToAdapter(context, this);
    }

    private void setView() {
        imgv_back = findViewById(R.id.imgv_back);
        edt_search = findViewById(R.id.edt_search);
        txt_title = findViewById(R.id.txt_title);
        txt_no_city = findViewById(R.id.txt_no_city);
        lstv_from_to = findViewById(R.id.lstv_from_to);
        lstv_from_to.setAdapter(fromToAdapter);
    }

    private void setData() {
        if (isFrom) {
            txt_title.setText("From City");
            //MainActivity.fromPosition=0;
            searchFrom(true);
        } else {
            txt_title.setText("To City");
            //MainActivity.toPosition=0;
            searchTo(true);
        }
    }

    private void setLitionar() {
        imgv_back.setOnClickListener(this);

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fromToAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lstv_from_to.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                final int lastItem = firstVisibleItem + visibleItemCount;

                if (lastItem == totalItemCount - 1) {
                    if (hasMore && !isApiCalling) {
                        if (isFrom) {
                            searchFrom(false);
                        } else {
                            searchTo(false);
                        }
                    }
                }
            }
        });
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
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utility.gotoBack(context);
    }

    @Override
    public void itemClick(int position, String id) {
        if (isFrom) {
            if (MainActivity.to != null && MainActivity.to.getData().get(MainActivity.toPosition).getId().equals(id)) {
                Utility.showToast(context, "Do not select same city as an arrival city");
            } else {
                for (int i = 0; i < MainActivity.from.getData().size(); i++) {
                    if (MainActivity.from.getData().get(i).getId().equals(id)) {
                        MainActivity.fromPosition = i;
                        onBackPressed();
                        break;
                    }
                }
            }
        } else {
            if (MainActivity.from.getData().get(MainActivity.fromPosition).getId().equals(id)) {
                Utility.showToast(context, "Do not select same city as a departure city");
            } else {
                for (int i = 0; i < MainActivity.to.getData().size(); i++) {
                    if (MainActivity.to.getData().get(i).getId().equals(id)) {
                        MainActivity.toPosition = i;
                        onBackPressed();
                        break;
                    }
                }
            }
        }

    }

    public void searchTo(boolean isDialog) {
        isApiCalling = true;
        JSONObject object = new JSONObject();
        try {
            object.put("fromNameId", MainActivity.from.getData().get(MainActivity.fromPosition).getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiServer.getInstance().searchTo(new APIResponse() {
            @Override
            public void onSuccess(JSONObject object) {
                if (ApiServer.getInstance().isSuccess(context, object)) {
                    if (MainActivity.to == null) {
                        MainActivity.to = new Gson().fromJson(object.toString(), FromToData.class);
                    } else {
                        FromToData fromToItem = new Gson().fromJson(object.toString(), FromToData.class);
                        MainActivity.to.getData().addAll(fromToItem.getData());
                    }
                    fromToAdapter.addAll(MainActivity.to.getData());
                    if (MainActivity.to.getData().isEmpty()) {
                        txt_no_city.setVisibility(View.VISIBLE);
                    } else {
                        txt_no_city.setVisibility(View.GONE);
                    }
                }
                isApiCalling = false;
                try {
                    hasMore=object.getBoolean("hasMore");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                isApiCalling = false;
            }
        }, context, isDialog, object);
    }

    public void searchFrom(boolean isDialog) {
        isApiCalling = true;
        ApiServer.getInstance().searchFrom(new APIResponse() {
            @Override
            public void onSuccess(JSONObject object) {
                if (ApiServer.getInstance().isSuccess(context, object)) {
                    if (MainActivity.from == null) {
                        MainActivity.from = new Gson().fromJson(object.toString(), FromToData.class);
                    } else {
                        FromToData fromToItem = new Gson().fromJson(object.toString(), FromToData.class);
                        MainActivity.from.getData().addAll(fromToItem.getData());
                    }
                    fromToAdapter.addAll(MainActivity.from.getData());
                    if (MainActivity.from.getData().isEmpty()) {
                        txt_no_city.setVisibility(View.VISIBLE);
                    } else {
                        txt_no_city.setVisibility(View.GONE);
                    }

                }
                isApiCalling = false;
                try {
                    hasMore=object.getBoolean("hasMore");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(String error) {
                isApiCalling = false;
            }
        }, context, isDialog);
    }
}
