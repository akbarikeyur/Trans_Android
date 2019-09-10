package com.trans.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.trans.R;
import com.trans.global.Utility;
import com.trans.model.JourneyItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 12-08-2017.
 */
public class BusAdapter extends BaseAdapter {
    ImageLoader imageLoader;
    Context context;

    List<JourneyItem> list = new ArrayList<>();


    public BusAdapter(Context context) {
        this.context = context;
        initialize();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            holder = new Holder();
            convertView = inflater.inflate(R.layout.row_bus, null);
            setView(convertView, holder);
            convertView.setTag(holder);
        } else
            holder = (Holder) convertView.getTag();
        setData(holder, position);
        return convertView;
    }

    private void setView(View view, Holder holder) {
        holder.txt_cmpny_name = view.findViewById(R.id.txt_cmpny_name);
        holder.imgv_logo = view.findViewById(R.id.imgv_logo);
        holder.txt_desc = view.findViewById(R.id.txt_desc);
        holder.txt_fare = view.findViewById(R.id.txt_fare);
        holder.txt_available_seats = view.findViewById(R.id.txt_available_seats);
        holder.txt_duration = view.findViewById(R.id.txt_duration);
        holder.txt_from = view.findViewById(R.id.txt_from);
        holder.txt_from_time = view.findViewById(R.id.txt_from_time);
        holder.txt_to = view.findViewById(R.id.txt_to);
        holder.txt_to_time = view.findViewById(R.id.txt_to_time);
        holder.ll_main = view.findViewById(R.id.ll_main);


    }

    @SuppressLint("NewApi")
    private void setData(Holder holder, int position) {
        imageLoader.displayImage("https://images-na.ssl-images-amazon.com/images/I/61VaoHj7IbL._SX425_.jpg", holder.imgv_logo, Utility.getImageOptions());
        holder.txt_cmpny_name.setText(list.get(position).getVehicleName());
        holder.txt_available_seats.setText(list.get(position).getAvailableSeats() + "");
        holder.txt_desc.setText(list.get(position).getVehicleDetails());
        String duration = list.get(position).getTimeDifference().split(":")[0] + "hrs " + Utility.checkMinute(list.get(position).getTimeDifference().split(":")[1]) + "min";
        holder.txt_duration.setText(duration);
        holder.txt_fare.setText("$" + list.get(position).getFare());
        holder.txt_from.setText(list.get(position).getFromStopCity());
        holder.txt_from_time.setText(list.get(position).getFromStopArrivalTime().getHour() + ":" + Utility.checkMinute(list.get(position).getFromStopArrivalTime().getMinute() + ""));
        holder.txt_to.setText(list.get(position).getToStopCity());
        holder.txt_to_time.setText(list.get(position).getToStopDepartureTime().getHour() + ":" + Utility.checkMinute(list.get(position).getToStopDepartureTime().getMinute() + ""));

        if (list.get(position).isSelected()) {
            holder.ll_main.setAlpha((float) 0.5);
        } else {
            holder.ll_main.setAlpha(1);
        }

    }


    private void initialize() {
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
    }

    private static class Holder {
        TextView txt_cmpny_name, txt_desc, txt_fare, txt_available_seats, txt_duration, txt_from, txt_from_time, txt_to, txt_to_time;
        ImageView imgv_logo;
        LinearLayout ll_main;
    }

    public void addAllData(List<JourneyItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
