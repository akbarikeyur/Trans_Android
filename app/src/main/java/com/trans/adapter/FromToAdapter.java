package com.trans.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.trans.R;
import com.trans.intrfc.ItemClick;
import com.trans.model.FromToItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 12-08-2017.
 */
public class FromToAdapter extends BaseAdapter implements Filterable {
    ImageLoader imageLoader;
    Context context;
    List<FromToItem> list = new ArrayList<>();
    List<FromToItem> listTemp = new ArrayList<>();
    ItemClick itemClick;
    ValueFilter valueFilter = new ValueFilter();

    public FromToAdapter(Context context, ItemClick itemClick) {
        this.context = context;
        initialize();
        this.itemClick = itemClick;
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
            convertView = inflater.inflate(R.layout.row_from_to, null);
            setView(convertView, holder);
            convertView.setTag(holder);
        } else
            holder = (Holder) convertView.getTag();
        setData(holder, position);
        return convertView;
    }

    private void setView(View view, Holder holder) {
        holder.txt_city = view.findViewById(R.id.txt_city);
        holder.txt_state = view.findViewById(R.id.txt_state);
        holder.ll_main = view.findViewById(R.id.ll_main);
        holder.view = view.findViewById(R.id.view);
    }

    @SuppressLint("NewApi")
    private void setData(Holder holder, final int position) {
        holder.txt_city.setText(list.get(position).getCity());
        holder.txt_state.setText(list.get(position).getState());
        holder.ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.itemClick(position, list.get(position).getId());
            }
        });

        if (position == list.size() - 1) {
            holder.view.setVisibility(View.GONE);
        } else {
            holder.view.setVisibility(View.VISIBLE);
        }
    }

    private void initialize() {
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
    }

    private static class Holder {
        TextView txt_city, txt_state;
        LinearLayout ll_main;
        View view;
    }

    public void addAll(List<FromToItem> fromToItems) {
        list.clear();
        list.addAll(fromToItems);
        listTemp.clear();
        listTemp.addAll(fromToItems);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<FromToItem> filterList = new ArrayList<FromToItem>();
                for (int i = 0; i < listTemp.size(); i++) {
                    if (listTemp.get(i).getCity().toUpperCase().contains(constraint.toString().toUpperCase())) {
                        filterList.add(listTemp.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = listTemp.size();
                results.values = listTemp;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            list = (ArrayList<FromToItem>) results.values;
            notifyDataSetChanged();
        }
    }
}
