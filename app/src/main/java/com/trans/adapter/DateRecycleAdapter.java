package com.trans.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trans.R;
import com.trans.intrfc.ItemClick;
import com.trans.model.DateData;

import java.util.ArrayList;

public class DateRecycleAdapter extends RecyclerView.Adapter<DateRecycleAdapter.ViewHolder> {

    private Context context;
    private ArrayList<DateData> list = new ArrayList<>();
    ItemClick itemClick;

    public DateRecycleAdapter(Context context, ItemClick itemClick) {
        this.context = context;
        this.itemClick = itemClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recycle_date, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        setData(holder, position);
        setColor(holder, position);
    }

    private void setData(ViewHolder holder, final int position) {
        holder.txt_date.setText(list.get(position).getDate());

        holder.txt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<list.size();i++){
                    list.get(i).setSelected(false);
                }
                list.get(position).setSelected(true);
                notifyDataSetChanged();
                itemClick.itemClick(position, "");
            }
        });
    }

    private void setColor(ViewHolder holder, int position) {
        if (list.get(position).isCurrent()) {
            holder.txt_date.setTextColor(context.getResources().getColor(R.color.red_image));
            holder.view.setVisibility(View.VISIBLE);
            holder.view.setBackgroundColor(context.getResources().getColor(R.color.red_image));
        } else if (list.get(position).isSelected()) {
            holder.txt_date.setTextColor(context.getResources().getColor(R.color.green_font));
            holder.view.setVisibility(View.VISIBLE);
            holder.view.setBackgroundColor(context.getResources().getColor(R.color.green_line));
        } else {
            holder.txt_date.setTextColor(context.getResources().getColor(R.color.grey_verification));
            holder.view.setVisibility(View.GONE);
        }

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_date;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_date = itemView.findViewById(R.id.txt_date);
            view = itemView.findViewById(R.id.view);
        }
    }

    public void addAllData(ArrayList<DateData> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}