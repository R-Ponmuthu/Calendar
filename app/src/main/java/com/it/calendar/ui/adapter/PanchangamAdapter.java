package com.it.calendar.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.github.vipulasri.timelineview.TimelineView;
import com.it.calendar.CalendarApp;
import com.it.calendar.R;
import com.it.calendar.beans.Krakakalam;
import com.it.calendar.beans.Panchangam;
import com.it.calendar.util.EnumNeram;
import com.it.calendar.util.EnumPanchangam;
import com.it.calendar.util.EnumParikaram;
import com.it.calendar.util.EnumWeekDay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.it.core.db.TableHelper;

public class PanchangamAdapter extends RecyclerView.Adapter<PanchangamAdapter.ItemViewHolder> {

    private List<Panchangam> weekdays;
    private Context context;
    private List<Panchangam> PpanchangamList = new ArrayList<>();
    private List<Panchangam> IpanchangamList = new ArrayList<>();
    private int curYear;

    private String[] times = new String[]{"6.00 - 7.30",
            "7.30 - 9.00",
            "9.00 - 10.30",
            "10.30 - 12.00",
            "12.00 - 1.30",
            "1.30 - 3.00",
            "3.00 - 4.30",
            "4.30 - 6.00"};

    public PanchangamAdapter(Context context, int curYear, List<Panchangam> weekdays) {
        this.context = context;
        this.weekdays = weekdays;
        this.curYear = curYear;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_item_layout, parent, false);
        return new ItemViewHolder(itemView, viewType);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

        holder.bind_panchangam(weekdays.get(position));
    }

    @Override
    public int getItemCount() {
        return weekdays.size();
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

        void bind_panchangam(Panchangam panchangam) {

            PpanchangamList = new ArrayList<>();
            IpanchangamList = new ArrayList<>();

            container.removeAllViews();
            title.setText(EnumWeekDay.getWeekDay(panchangam.getWeekday()).getText());

            for (String time : times) {

                TableHelper<Panchangam> panchangamTableHelper = CalendarApp.getTable(context, Panchangam.class);

                String Pselection = "time = '" + time + "'" + " and neram=1 and weekday=" +
                        EnumWeekDay.getWeekDay(panchangam.getWeekday()).getDay() + " and year=2022";

                String Iselection = "time ='" + time + "'" + " and neram=2  and weekday=" +
                        EnumWeekDay.getWeekDay(panchangam.getWeekday()).getDay() + " and year=2022";

                Panchangam Ppanchangam = panchangamTableHelper.getItem(panchangamTableHelper.getReadableDatabase(), Pselection, null, null);

                Panchangam Ipanchangam = panchangamTableHelper.getItem(panchangamTableHelper.getReadableDatabase(), Iselection, null, null);

                PpanchangamList.add(Ppanchangam);
                IpanchangamList.add(Ipanchangam);
            }

            View view = LayoutInflater.from(context).inflate(R.layout.panchangam_sub_item, null, false);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(params);
            SubItemViewHolder subItemViewHolder = new SubItemViewHolder(view);
            subItemViewHolder.bind(PpanchangamList, IpanchangamList);

            container.addView(view);
        }
    }

    class SubItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt1)
        TextView txt1;
        @BindView(R.id.txt2)
        TextView txt2;
        @BindView(R.id.txt3)
        TextView txt3;
        @BindView(R.id.txt4)
        TextView txt4;
        @BindView(R.id.txt5)
        TextView txt5;
        @BindView(R.id.txt6)
        TextView txt6;
        @BindView(R.id.txt7)
        TextView txt7;
        @BindView(R.id.txt8)
        TextView txt8;
        @BindView(R.id.txt9)
        TextView txt9;
        @BindView(R.id.txt10)
        TextView txt10;
        @BindView(R.id.txt11)
        TextView txt11;
        @BindView(R.id.txt12)
        TextView txt12;
        @BindView(R.id.txt13)
        TextView txt13;
        @BindView(R.id.txt14)
        TextView txt14;
        @BindView(R.id.txt15)
        TextView txt15;
        @BindView(R.id.txt16)
        TextView txt16;

        SubItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @SuppressLint("SetTextI18n")
        void bind(List<Panchangam> ppanchangam, List<Panchangam> ipanchangam) {

            txt1.setText("6.00-7.30: " + EnumPanchangam.getPanchangamStr(ppanchangam.get(0).getParikaram()).getText());
            txt1.setTextColor(context.getResources().getColor(EnumPanchangam.getPanchangamStr(ppanchangam.get(0).getParikaram()).getColor()));
            txt2.setText("7.30-9.00: " + EnumPanchangam.getPanchangamStr(ppanchangam.get(1).getParikaram()).getText());
            txt2.setTextColor(context.getResources().getColor(EnumPanchangam.getPanchangamStr(ppanchangam.get(1).getParikaram()).getColor()));
            txt3.setText("9.00-10.30: " + EnumPanchangam.getPanchangamStr(ppanchangam.get(2).getParikaram()).getText());
            txt3.setTextColor(context.getResources().getColor(EnumPanchangam.getPanchangamStr(ppanchangam.get(2).getParikaram()).getColor()));
            txt4.setText("10.30-12.00: " + EnumPanchangam.getPanchangamStr(ppanchangam.get(3).getParikaram()).getText());
            txt4.setTextColor(context.getResources().getColor(EnumPanchangam.getPanchangamStr(ppanchangam.get(3).getParikaram()).getColor()));
            txt5.setText("12.00-1.30: " + EnumPanchangam.getPanchangamStr(ppanchangam.get(4).getParikaram()).getText());
            txt5.setTextColor(context.getResources().getColor(EnumPanchangam.getPanchangamStr(ppanchangam.get(4).getParikaram()).getColor()));
            txt6.setText("1.30-3.00: " + EnumPanchangam.getPanchangamStr(ppanchangam.get(5).getParikaram()).getText());
            txt6.setTextColor(context.getResources().getColor(EnumPanchangam.getPanchangamStr(ppanchangam.get(5).getParikaram()).getColor()));
            txt7.setText("3.00-4.30: " + EnumPanchangam.getPanchangamStr(ppanchangam.get(6).getParikaram()).getText());
            txt7.setTextColor(context.getResources().getColor(EnumPanchangam.getPanchangamStr(ppanchangam.get(6).getParikaram()).getColor()));
            txt8.setText("4.30-6.00: " + EnumPanchangam.getPanchangamStr(ppanchangam.get(7).getParikaram()).getText());
            txt8.setTextColor(context.getResources().getColor(EnumPanchangam.getPanchangamStr(ppanchangam.get(7).getParikaram()).getColor()));
            txt9.setText("6.00-7.30: " + EnumPanchangam.getPanchangamStr(ipanchangam.get(0).getParikaram()).getText());
            txt9.setTextColor(context.getResources().getColor(EnumPanchangam.getPanchangamStr(ipanchangam.get(0).getParikaram()).getColor()));
            txt10.setText("7.30-9.00: " + EnumPanchangam.getPanchangamStr(ipanchangam.get(1).getParikaram()).getText());
            txt10.setTextColor(context.getResources().getColor(EnumPanchangam.getPanchangamStr(ipanchangam.get(1).getParikaram()).getColor()));
            txt11.setText("9.00-10.30: " + EnumPanchangam.getPanchangamStr(ipanchangam.get(2).getParikaram()).getText());
            txt11.setTextColor(context.getResources().getColor(EnumPanchangam.getPanchangamStr(ipanchangam.get(2).getParikaram()).getColor()));
            txt12.setText("10.30-12.00: " + EnumPanchangam.getPanchangamStr(ipanchangam.get(3).getParikaram()).getText());
            txt12.setTextColor(context.getResources().getColor(EnumPanchangam.getPanchangamStr(ipanchangam.get(3).getParikaram()).getColor()));
            txt13.setText("12.00-1.30:" + EnumPanchangam.getPanchangamStr(ipanchangam.get(4).getParikaram()).getText());
            txt13.setTextColor(context.getResources().getColor(EnumPanchangam.getPanchangamStr(ipanchangam.get(4).getParikaram()).getColor()));
            txt14.setText("1.30-3.00: " + EnumPanchangam.getPanchangamStr(ipanchangam.get(5).getParikaram()).getText());
            txt14.setTextColor(context.getResources().getColor(EnumPanchangam.getPanchangamStr(ipanchangam.get(5).getParikaram()).getColor()));
            txt15.setText("3.00-4.30: " + EnumPanchangam.getPanchangamStr(ipanchangam.get(6).getParikaram()).getText());
            txt15.setTextColor(context.getResources().getColor(EnumPanchangam.getPanchangamStr(ipanchangam.get(6).getParikaram()).getColor()));
            txt16.setText("4.30-6.00: " + EnumPanchangam.getPanchangamStr(ipanchangam.get(7).getParikaram()).getText());
            txt16.setTextColor(context.getResources().getColor(EnumPanchangam.getPanchangamStr(ipanchangam.get(7).getParikaram()).getColor()));
        }
    }
}
