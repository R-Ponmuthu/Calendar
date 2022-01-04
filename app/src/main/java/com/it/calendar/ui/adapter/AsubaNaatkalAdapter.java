package com.it.calendar.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.vipulasri.timelineview.TimelineView;
import com.it.calendar.CalendarApp;
import com.it.calendar.R;
import com.it.calendar.beans.MainTable;
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

public class AsubaNaatkalAdapter extends RecyclerView.Adapter<AsubaNaatkalAdapter.ItemViewHolder> {

    List<String> months;
    Map<String, List<?>> mainTbl;
    Map<String, List<VirathaDay>> viratham = new HashMap<>();
    List<VirathaDay> virathaDayList = new ArrayList<>();
    private Context context;


    public AsubaNaatkalAdapter(Context context, String queryFlag, List<String> months, Map<String, List<?>> mainTbl) {
        this.context = context;
        this.months = months;
        this.mainTbl = mainTbl;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new ItemViewHolder(itemView, viewType);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

        holder.bind_muhurtham(months.get(position));
    }

    @Override
    public int getItemCount() {
        return mainTbl.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.container)
        RecyclerView container;
        @BindView(R.id.timeline)
        TimelineView timelineView;

        ItemViewHolder(View view, int viewType) {
            super(view);
            ButterKnife.bind(this, view);

            timelineView.initLine(viewType);
        }

        @SuppressLint("SetTextI18n")
        void bind_muhurtham(String s) {

            //container.removeAllViews();
            title.setText(EnumMonth.getMonthStr(s).getText());

            viratham = new HashMap<>();
            virathaDayList = new ArrayList<>();

            for (int i = 0; i < mainTbl.get(EnumMonth.getMonthStr(s).getDay()).size(); i++) {

                VirathaDay virathaDay = (VirathaDay) mainTbl.get(s).get(i);
                virathaDayList.add(virathaDay);
            }

            viratham.put(s, virathaDayList);
            container.setLayoutManager(new LinearLayoutManager(context));
            container.setAdapter(new SubItemAdapter(context, s, viratham));
        }
    }

    public class SubItemAdapter extends RecyclerView.Adapter<SubItemAdapter.ItemViewHolder> {

        Map<String, List<VirathaDay>> viratham;
        String month;
        private Context context;

        SubItemAdapter(Context context, String month, Map<String, List<VirathaDay>> viratham) {
            this.context = context;
            this.viratham = viratham;
            this.month = month;
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.asuba_naal_item, parent, false);
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

//                MainTable mainTable = realm.where(MainTable.class)
//                        .equalTo(Constants.date, virathaDay.getDate())
//                        .findFirst();

                Date dt = null;
                String dateStr = null;
                try {
                    dt = DateTimeHelper.simpleDateFormat1.parse(virathaDay.getDate());
                    dateStr = DateTimeHelper.simpleDateFormat.format(dt);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                TableHelper<MainTable> mainTableTableHelper = CalendarApp.getTable(context, MainTable.class);
                MainTable mainTable = mainTableTableHelper.getItem(mainTableTableHelper.getReadableDatabase(), "date=?",
                        new String[]{String.valueOf(dateStr)}, null);

                date.setText(dateStr + "  " + EnumWeekDay.getWeekDay(mainTable.getWeekday()).getText() + "  " + mainTable.getTam_day() + "  "
                        + EnumTamilMonth.getTamilMonth(mainTable.getTam_month()).getText() + "  " + virathaDay.getTime());

                conatinerLay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, "" + virathaDay.getDate(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
