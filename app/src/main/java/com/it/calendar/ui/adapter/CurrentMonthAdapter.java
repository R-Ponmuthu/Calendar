package com.it.calendar.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.github.vipulasri.timelineview.TimelineView;
import com.it.calendar.CalendarApp;
import com.it.calendar.R;
import com.it.calendar.beans.MainTable;
import com.it.calendar.beans.MuhurthamTable;
import com.it.calendar.beans.VirathaDay;
import com.it.calendar.util.Constants;
import com.it.calendar.util.DateTimeHelper;
import com.it.calendar.util.EnumVirathaDay;
import com.it.calendar.util.EnumWeekDay;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.it.core.db.TableHelper;

public class CurrentMonthAdapter extends RecyclerView.Adapter<CurrentMonthAdapter.ItemViewHolder> {

    private HashMap<String, List<?>> listHashMap;
    private Context context;

    public CurrentMonthAdapter(Context context, HashMap<String, List<?>> listHashMap) {

        this.context = context;
        this.listHashMap = listHashMap;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_item_layout, parent, false);
        return new ItemViewHolder(itemView, viewType);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

        if (listHashMap.keySet().toArray()[position].toString().equals("சுபமுகூர்த்த தினங்கள்")) {
            List<MuhurthamTable> list = (List<MuhurthamTable>) listHashMap.get(listHashMap.keySet().toArray()[position].toString());
            holder.bind_moogurtham(listHashMap.keySet().toArray()[position].toString(), list);
        } else if (listHashMap.keySet().toArray()[position].toString().equals("முக்கிய விரத தினங்கள்")) {
            List<VirathaDay> list = (List<VirathaDay>) listHashMap.get(listHashMap.keySet().toArray()[position].toString());
            holder.bind_viratham(listHashMap.keySet().toArray()[position].toString(), list);
        } else {
            List<MainTable> list = (List<MainTable>) listHashMap.get(listHashMap.keySet().toArray()[position].toString());
            holder.bind(listHashMap.keySet().toArray()[position].toString(), list);
        }
    }

    @Override
    public int getItemCount() {
        return listHashMap.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.container)
        LinearLayout container;
        @BindView(R.id.timeline)
        TimelineView timelineView;

        ItemViewHolder(View view, int viewType) {
            super(view);
            ButterKnife.bind(this, view);

            timelineView.initLine(viewType);
        }

        @SuppressLint("SetTextI18n")
        void bind(String titleStr, List<MainTable> list) {

            container.removeAllViews();

            title.setText(titleStr);

            for (int i = 0; i < list.size(); i++) {

                View view = LayoutInflater.from(context).inflate(R.layout.festival_sub_item, null, false);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(params);
                SubItemViewHolder subItemViewHolder = new SubItemViewHolder(view);
                subItemViewHolder.bind(i, titleStr, list.get(i));

                container.addView(view);
            }
        }

        @SuppressLint("SetTextI18n")
        void bind_moogurtham(String titleStr, List<MuhurthamTable> list) {

            container.removeAllViews();

            title.setText(titleStr);

            for (int i = 0; i < list.size(); i++) {

                View view = LayoutInflater.from(context).inflate(R.layout.festival_sub_item, null, false);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(params);
                SubItemViewHolder subItemViewHolder = new SubItemViewHolder(view);
                subItemViewHolder.bind(i, list.get(i));

                container.addView(view);
            }
        }

        void bind_viratham(String titleStr, List<VirathaDay> list) {

            container.removeAllViews();

            title.setText(titleStr);

            for (int i = 0; i < list.size(); i++) {

                View view = LayoutInflater.from(context).inflate(R.layout.festival_sub_item, null, false);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(params);
                SubItemViewHolder subItemViewHolder = new SubItemViewHolder(view);
                subItemViewHolder.bind(i, list.get(i));

                container.addView(view);
            }
        }
    }

    class SubItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.day)
        TextView day;
        @BindView(R.id.function)
        TextView function;
        @BindView(R.id.itemContainer)
        LinearLayout itemContainer;

        SubItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @SuppressLint("SetTextI18n")
        void bind(int i, String titleStr, MainTable mainTable) {

            if (i == 0 || (i % 2 == 0)) {
                itemContainer.setBackgroundColor(context.getResources().getColor(R.color.grey_ec));
            } else {
                itemContainer.setBackgroundColor(Color.WHITE);
            }

            switch (titleStr) {
                case "அரசினர் விடுமுறை நாட்கள்":
                    date.setText("" + mainTable.getDay());
                    day.setText("" + EnumWeekDay.getWeekDay(mainTable.getWeekday()).getText());
                    function.setText("" + mainTable.getGov_holiday());
                    break;
                case "இந்துக்கள் பண்டிகைகள்":
                    date.setText("" + mainTable.getDay());
                    day.setText("" + EnumWeekDay.getWeekDay(mainTable.getWeekday()).getText());
                    function.setText("" + mainTable.getHindu_fes());
                    break;
                case "கிறிஸ்துவ பண்டிகைகள்":
                    date.setText("" + mainTable.getDay());
                    day.setText("" + EnumWeekDay.getWeekDay(mainTable.getWeekday()).getText());
                    function.setText("" + mainTable.getChirs_fes());
                    break;
                default:
                    date.setText("" + mainTable.getDay());
                    day.setText("" + EnumWeekDay.getWeekDay(mainTable.getWeekday()).getText());
                    function.setText("" + mainTable.getMuslim_fes());
                    break;
            }
        }

        @SuppressLint("SetTextI18n")
        void bind(int i, MuhurthamTable moogurthamTable) {

            if (i == 0 || (i % 2 == 0)) {
                itemContainer.setBackgroundColor(context.getResources().getColor(R.color.grey_ec));
            } else {
                itemContainer.setBackgroundColor(Color.WHITE);
            }

//            MainTable mainTable = realm.where(MainTable.class)
//                    .equalTo(Constants.date, moogurthamTable.getDate())
//                    .findFirst();

            TableHelper<MainTable> mainTableTableHelper = CalendarApp.getTable(context, MainTable.class);
            MainTable mainTable = mainTableTableHelper.getItem(mainTableTableHelper.getReadableDatabase(), "date=?",
                    new String[]{String.valueOf(moogurthamTable.getDate())}, null);

            if (moogurthamTable.getValrpirai() == 1)
                date.setText(moogurthamTable.getDate().split("-")[0].trim() + "*");
            else
                date.setText("" + moogurthamTable.getDate().split("-")[0].trim());
            if (mainTable != null)
                day.setText("" + EnumWeekDay.getWeekDay(mainTable.getWeekday()).getText());
            function.setVisibility(View.GONE);
        }

        @SuppressLint("SetTextI18n")
        void bind(int i, VirathaDay virathaDay) {

            if (i == 0 || (i % 2 == 0)) {
                itemContainer.setBackgroundColor(context.getResources().getColor(R.color.grey_ec));
            } else {
                itemContainer.setBackgroundColor(Color.WHITE);
            }

//            MainTable mainTable = realm.where(MainTable.class)
//                    .equalTo(Constants.date, virathaDay.getDate())
//                    .findFirst();

            TableHelper<MainTable> mainTableTableHelper = CalendarApp.getTable(context, MainTable.class);
            MainTable mainTable = mainTableTableHelper.getItem(mainTableTableHelper.getReadableDatabase(), "date=?",
                    new String[]{String.valueOf(virathaDay.getDate())}, null);

            date.setText("" + virathaDay.getDate().split("-")[0].trim());
            if (mainTable != null)
                day.setText("" + EnumWeekDay.getWeekDay(mainTable.getWeekday()).getText());
            if (!virathaDay.getTime().equals("-"))
                function.setText(EnumVirathaDay.getVirathaDay(virathaDay.getViratham()).getText() + "  (" + virathaDay.getTime() + ")");
            else
                function.setText(EnumVirathaDay.getVirathaDay(virathaDay.getViratham()).getText());
        }
    }
}
