package com.trans.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.trans.R;
import com.trans.global.Utility;
import com.trans.model.HistoryData;

import java.util.ArrayList;

/**
 * Created by acer on 12-08-2017.
 */
public class HistoryAdapter extends BaseAdapter {
    ImageLoader imageLoader;
    Context context;
    ArrayList<HistoryData> list;

    public HistoryAdapter(Context context, ArrayList<HistoryData> list) {
        this.context = context;
        initialize();
        this.list = list;
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
            convertView = inflater.inflate(R.layout.row_history, null);
            setView(convertView, holder);
            convertView.setTag(holder);
        } else
            holder = (Holder) convertView.getTag();
        setData(holder, position);
        return convertView;
    }

    private void setView(View view, Holder holder) {
        holder.txt_depart_time = view.findViewById(R.id.txt_depart_time);
        holder.txt_num_seat = view.findViewById(R.id.txt_num_seat);
        holder.txt_depart_date = view.findViewById(R.id.txt_depart_date);
        holder.txt_from = view.findViewById(R.id.txt_from);
        holder.txt_to = view.findViewById(R.id.txt_to);
        holder.txt_tkt_number = view.findViewById(R.id.txt_tkt_number);
        holder.txt_tkt_fare = view.findViewById(R.id.txt_tkt_fare);
        holder.txt_return_date = view.findViewById(R.id.txt_return_date);
        holder.txt_return_time = view.findViewById(R.id.txt_return_time);
        holder.txt_num_seat_return = view.findViewById(R.id.txt_num_seat_return);
        holder.ll_return = view.findViewById(R.id.ll_return);
    }

    @SuppressLint("NewApi")
    private void setData(Holder holder, int position) {

        int seats=Integer.parseInt(list.get(position).getNumOfSeat());
        String seat="01";
        if(seats<10){
            seat="0"+seats;
        }else{
            seat=seats+"";
        }

        holder.txt_num_seat.setText(seat);
        holder.txt_depart_date.setText(list.get(position).getDepartDate());

        String duration = list.get(position).getDepartTime().split(":")[0] + ":" + Utility.checkMinute(list.get(position).getDepartTime().split(":")[1]);

        holder.txt_depart_time.setText(duration);
        holder.txt_from.setText(list.get(position).getFrom());
        holder.txt_to.setText(list.get(position).getTo());
        holder.txt_tkt_fare.setText(list.get(position).getFare());
        holder.txt_tkt_number.setText("Ticket Number : " + list.get(position).getTktNumber());

        if(list.get(position).isTwoWay()){
            holder.ll_return.setVisibility(View.VISIBLE);
            String durationReturn = list.get(position).getReturnTime().split(":")[0] + ":" + Utility.checkMinute(list.get(position).getReturnTime().split(":")[1]);
            holder.txt_return_time.setText(durationReturn);
            holder.txt_return_date.setText(list.get(position).getReturnDate());
            holder.txt_num_seat_return.setText(seat);
        }else{
            holder.ll_return.setVisibility(View.GONE);
        }

    }


    private void initialize() {
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
    }

    private static class Holder {
        TextView txt_num_seat, txt_depart_date, txt_depart_time, txt_from, txt_to, txt_tkt_number, txt_tkt_fare,txt_return_date, txt_return_time, txt_num_seat_return;
        LinearLayout ll_return;
    }
}
