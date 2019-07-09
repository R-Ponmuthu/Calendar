package com.it.calendar.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.it.calendar.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CalendarAdapter3 extends RecyclerView.Adapter<CalendarAdapter3.MyViewHolder> {

    private int year;
    private List<String> dataList;
    private Integer[] iconList;
    private Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemImage)
        ImageView itemImage;
        @BindView(R.id.itemName)
        TextView itemName;
        @BindView(R.id.parentItem)
        CardView parentItem;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    CalendarAdapter3(Context context, int year, List<String> dataList, Integer[] iconList) {
        this.year = year;
        this.context = context;
        this.dataList = dataList;
        this.iconList = iconList;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if (position == 11 || position == 12 || position == 13)
            holder.itemName.setMaxLines(1);

        holder.itemName.setText(dataList.get(position));
        holder.itemImage.setImageResource(iconList[position]);

        holder.parentItem.setOnClickListener(v -> {

            switch (position) {

                case 0:
                    startDrawerActivity("gov_holiday", dataList.get(0));
                    break;
                case 1:
                    startDrawerActivity("hindu_fes", dataList.get(1));
                    break;
                case 2:
                    startDrawerActivity("muslim_fes", dataList.get(2));
                    break;
                case 3:
                    startDrawerActivity("chirs_fes", dataList.get(3));
                    break;
                case 4:
                    startDrawerActivity("muhurtha_days", dataList.get(4));
                    break;
                case 5:
                    startDrawerActivity(dataList.get(5), dataList.get(5));
                    break;
                case 6:
                    startDrawerActivity("viradha_days", dataList.get(6));
                    break;
                case 7:
                    startDrawerActivity("vasthu_days", dataList.get(7));
                    break;
                case 8:
                    startDrawerActivity("raagu", dataList.get(8));
                    break;
                case 9:
                    startDrawerActivity("gowri_panchanagam", dataList.get(9));
                    break;
                case 10:
                    startDrawerActivity("suba_horai", dataList.get(10));
                    break;
                case 11:
                    startDrawerActivity(dataList.get(12), dataList.get(11));
                    break;
                case 12:
                    startDrawerActivity(dataList.get(13), dataList.get(12));
                    break;
                case 13:
                    startDrawerActivity(dataList.get(14), dataList.get(13));
                    break;
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_item, parent, false);
        return new MyViewHolder(v);
    }

    public void startDrawerActivity(String flag, String title) {
        context.startActivity(new Intent(context, DrawerActivity.class)
                .putExtra("QueryFlag", flag)
                .putExtra("curYear", year)
                .putExtra("title", title));
    }
}
