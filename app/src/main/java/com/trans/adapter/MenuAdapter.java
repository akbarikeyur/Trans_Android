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
import com.trans.global.GlobalAppConfiguration;
import com.trans.model.MenuData;

import java.util.ArrayList;

/**
 * Created by acer on 12-08-2017.
 */
public class MenuAdapter extends BaseAdapter {
    ImageLoader imageLoader;
    Context context;
    ArrayList<MenuData> list = new ArrayList<>();

    public MenuAdapter(Context context) {
        this.context = context;
        initialize();
        list.clear();
        list.addAll(GlobalAppConfiguration.menuDataList);
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
            convertView = inflater.inflate(R.layout.row_menu, null);
            setView(convertView, holder);
            convertView.setTag(holder);
        } else
            holder = (Holder) convertView.getTag();
        setData(holder, position);
        return convertView;
    }

    private void setView(View view, Holder holder) {
        holder.txt_title = view.findViewById(R.id.txt_title);
        holder.imgv_icon = view.findViewById(R.id.imgv_icon);
        holder.ll_main = view.findViewById(R.id.ll_main);
    }

    @SuppressLint("NewApi")
    private void setData(Holder holder, int position) {
        holder.txt_title.setText(list.get(position).getTitle());
        holder.imgv_icon.setImageResource(list.get(position).getImage());
        // holder.imgv_icon.setColorFilter(context.getResources().getColor(R.color.black_font));
    }


    private void initialize() {
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
    }

    private static class Holder {
        TextView txt_title;
        LinearLayout ll_main;
        ImageView imgv_icon;
    }
}
