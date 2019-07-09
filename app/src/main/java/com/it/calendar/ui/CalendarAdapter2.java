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

public class CalendarAdapter2 extends RecyclerView.Adapter<CalendarAdapter2.MyViewHolder> {

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

    CalendarAdapter2(Context context, int year, List<String> dataList, Integer[] iconList) {
        this.year = year;
        this.context = context;
        this.dataList = dataList;
        this.iconList = iconList;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if (position == 5)
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
                    context.startActivity(new Intent(context, MoreCalendarMenu.class)
                            .putExtra("Year", year));
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

    private void startDrawerActivity(String flag, String title) {
        context.startActivity(new Intent(context, DrawerActivity.class)
                .putExtra("QueryFlag", flag)
                .putExtra("curYear", year)
                .putExtra("title", title));
    }
}
