package com.it.calendar.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.vipulasri.timelineview.TimelineView;
import com.it.calendar.CalendarApp;
import com.it.calendar.R;
import com.it.calendar.beans.MainTable;
import com.it.calendar.beans.MuhurthamTable;
import com.it.calendar.beans.VirathaDay;
import com.it.calendar.util.Constants;
import com.it.calendar.util.DateTimeHelper;
import com.it.calendar.util.EnumMonth;
import com.it.calendar.util.EnumTamilMonth;
import com.it.calendar.util.EnumWeekDay;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.it.core.db.TableHelper;

public class MuhurthamAdapter extends RecyclerView.Adapter<MuhurthamAdapter.ItemViewHolder> {

    List<String> months;
    List<VirathaDay> virathaDayList;
    private Activity activity;

    public MuhurthamAdapter(Activity activity, List<VirathaDay> virathaDayList) {
        this.activity = activity;
        this.virathaDayList = virathaDayList;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.muhurtham_item, parent, false);
        return new ItemViewHolder(itemView, viewType);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

        VirathaDay virathaDay = virathaDayList.get(position);

        TableHelper<MuhurthamTable> muhurthamTableHelper = CalendarApp.getTable(activity, MuhurthamTable.class);
        MuhurthamTable moogurthamTable = muhurthamTableHelper.getItem(muhurthamTableHelper.getReadableDatabase(), "date=?",
                new String[]{virathaDay.getDate()}, null);

        TableHelper<MainTable> mainTableTableHelper = CalendarApp.getTable(activity, MainTable.class);
        MainTable mainTable = mainTableTableHelper.getItem(mainTableTableHelper.getReadableDatabase(), "date = ?",
                new String[]{virathaDay.getDate()}, null);

        if (moogurthamTable != null)
            if (moogurthamTable.getValrpirai() == 1)
                holder.date.setText(mainTable.getDay() + "*, " + EnumWeekDay.getWeekDay(mainTable.getWeekday()).getText());
            else
                holder.date.setText(mainTable.getDay() + ", " + EnumWeekDay.getWeekDay(mainTable.getWeekday()).getText());
        holder.tamDate.setText(EnumTamilMonth.getTamilMonth(mainTable.getTam_month()).getText() + ", " + mainTable.getTam_day());

        if (position == 0 || position % 2 == 0) {
            holder.parentCV.setBackgroundColor(activity.getResources().getColor(R.color.grey_ec));
        }
    }

    @Override
    public int getItemCount() {
        return virathaDayList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.tamDate)
        TextView tamDate;
        @BindView(R.id.parentCV)
        RelativeLayout parentCV;

        ItemViewHolder(View view, int viewType) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class SubItemAdapter extends RecyclerView.Adapter<SubItemAdapter.ItemViewHolder> {

        Map<String, List<VirathaDay>> viratham;
        String month;
        private Activity activity;

        SubItemAdapter(Activity activity, String month, Map<String, List<VirathaDay>> viratham) {
            this.activity = activity;
            this.viratham = viratham;
            this.month = month;
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.muhurtham_sub_item, parent, false);
            return new ItemViewHolder(itemView);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {

            holder.bind_item(viratham.get(month).get(position));
        }

        @Override
        public int getItemCount() {
            return viratham.get(month).size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.day)
            TextView day;
            @BindView(R.id.date)
            TextView date;
            @BindView(R.id.containerLay)
            LinearLayout conatinerLay;

            ItemViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            @SuppressLint("SetTextI18n")
            void bind_item(VirathaDay virathaDay) {

                date.setText(virathaDay.getDate().split("-")[0]);

//                Date dt;
                String dateStr = virathaDay.getDate();
//                try {
//                    dt = DateTimeHelper.simpleDateFormat1.parse(virathaDay.getDate());
//                    dateStr = DateTimeHelper.simpleDateFormat.format(dt);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }

                TableHelper<MuhurthamTable> muhurthamTableHelper = CalendarApp.getTable(activity, MuhurthamTable.class);
                MuhurthamTable moogurthamTable = muhurthamTableHelper.getItem(muhurthamTableHelper.getReadableDatabase(), "date=?",
                        new String[]{dateStr}, null);

                if (moogurthamTable != null)
                    if (moogurthamTable.getValrpirai() == 1)
                        conatinerLay.setBackgroundColor(activity.getResources().getColor(R.color.yellow_light));

                TableHelper<MainTable> mainTableTableHelper = CalendarApp.getTable(activity, MainTable.class);
                MainTable mainTable = mainTableTableHelper.getItem(mainTableTableHelper.getReadableDatabase(), "date = ?",
                        new String[]{dateStr}, null);

                day.setText("" + EnumWeekDay.getWeekDay(mainTable.getWeekday()).getText().substring(0, 2));

                conatinerLay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //MuhurthamBottomSheet.newInstance(virathaDay.getDate()).show(((DrawerActivity) activity).getSupportFragmentManager(), "Muhurtham");
                    }
                });
            }
        }
    }
}
