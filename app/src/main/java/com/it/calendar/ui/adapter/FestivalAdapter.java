package com.it.calendar.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.github.vipulasri.timelineview.TimelineView;
import com.it.calendar.R;
import com.it.calendar.beans.MainTable;
import com.it.calendar.util.EnumMonth;
import com.it.calendar.util.EnumTamilMonth;
import com.it.calendar.util.EnumWeekDay;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FestivalAdapter extends RecyclerView.Adapter<FestivalAdapter.ItemViewHolder> {

    List<MainTable> mainTableList;
    private Context context;
    private String queryFlag;

    public FestivalAdapter(Context context, String queryFlag, List<MainTable> mainTableList) {
        this.context = context;
        this.mainTableList = mainTableList;
        this.queryFlag = queryFlag;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_item_layout, parent, false);
        return new ItemViewHolder(itemView, viewType);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

        MainTable mainTable = mainTableList.get(position);

        if (queryFlag.equals("hindu_fes")) {
            holder.data.setText(mainTable.getHindu_fes());
        } else if (queryFlag.equals("muslim_fes")) {
            holder.data.setText(mainTable.getMuslim_fes());
        } else if (queryFlag.equals("chirs_fes")) {
            holder.data.setText(mainTable.getChirs_fes());
        } else {
            holder.data.setText(mainTable.getGov_holiday());
        }
        holder.date.setText(mainTable.getDay().toString() + ", " + EnumWeekDay.getWeekDay(mainTable.getWeekday()).getText());
        holder.tamDate.setText(EnumTamilMonth.getTamilMonth(mainTable.getTam_month()).getText() + ", " + mainTable.getTam_day());

        if (position == 0 || position % 2 == 0) {
            holder.parentCV.setBackgroundColor(context.getResources().getColor(R.color.grey_ec));
        }
    }

    @Override
    public int getItemCount() {
        return mainTableList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.tamDate)
        TextView tamDate;
        @BindView(R.id.data)
        TextView data;
        @BindView(R.id.parentCV)
        RelativeLayout parentCV;

        ItemViewHolder(View view, int viewType) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
