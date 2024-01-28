package com.it.calendar.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    private List<Panchangam> panchangamList;
    private int curYear;

    private String[] times = new String[]{"6.00 - 7.30",
            "7.30 - 9.00",
            "9.00 - 10.30",
            "10.30 - 12.00",
            "12.00 - 1.30",
            "1.30 - 3.00",
            "3.00 - 4.30",
            "4.30 - 6.00"};

    public PanchangamAdapter(Context context, List<Panchangam> panchangams) {
        this.context = context;
        this.panchangamList = panchangams;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.panchangam_sub_item, parent, false);
        return new ItemViewHolder(itemView, viewType);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

//        holder.txt1.setText("6.00-7.30: " + EnumPanchangam.getPanchangamStr(panchangamList.get(0).getParikaram()).getText());
//        holder.txt1.setTextColor(context.getResources().getColor(EnumPanchangam.getPanchangamStr(panchangamList.get(0).getParikaram()).getColor()));
//        holder.txt2.setText("7.30-9.00: " + EnumPanchangam.getPanchangamStr(panchangamList.get(1).getParikaram()).getText());
//        holder.txt2.setTextColor(context.getResources().getColor(EnumPanchangam.getPanchangamStr(panchangamList.get(1).getParikaram()).getColor()));
//        holder.txt3.setText("9.00-10.30: " + EnumPanchangam.getPanchangamStr(panchangamList.get(2).getParikaram()).getText());
//        holder.txt3.setTextColor(context.getResources().getColor(EnumPanchangam.getPanchangamStr(panchangamList.get(2).getParikaram()).getColor()));
//        holder.txt4.setText("10.30-12.00: " + EnumPanchangam.getPanchangamStr(panchangamList.get(3).getParikaram()).getText());
//        holder.txt4.setTextColor(context.getResources().getColor(EnumPanchangam.getPanchangamStr(panchangamList.get(3).getParikaram()).getColor()));
//        holder.txt5.setText("12.00-1.30: " + EnumPanchangam.getPanchangamStr(panchangamList.get(4).getParikaram()).getText());
//        holder.txt5.setTextColor(context.getResources().getColor(EnumPanchangam.getPanchangamStr(panchangamList.get(4).getParikaram()).getColor()));
//        holder.txt6.setText("1.30-3.00: " + EnumPanchangam.getPanchangamStr(panchangamList.get(5).getParikaram()).getText());
//        holder.txt6.setTextColor(context.getResources().getColor(EnumPanchangam.getPanchangamStr(panchangamList.get(5).getParikaram()).getColor()));
//        holder.txt7.setText("3.00-4.30: " + EnumPanchangam.getPanchangamStr(panchangamList.get(6).getParikaram()).getText());
//        holder.txt7.setTextColor(context.getResources().getColor(EnumPanchangam.getPanchangamStr(panchangamList.get(6).getParikaram()).getColor()));
//        holder.txt8.setText("4.30-6.00: " + EnumPanchangam.getPanchangamStr(panchangamList.get(7).getParikaram()).getText());
//        holder.txt8.setTextColor(context.getResources().getColor(EnumPanchangam.getPanchangamStr(panchangamList.get(7).getParikaram()).getColor()));
//        holder.txt9.setText("6.00-7.30: " + EnumPanchangam.getPanchangamStr(panchangamList.get(8).getParikaram()).getText());
//        holder.txt9.setTextColor(context.getResources().getColor(EnumPanchangam.getPanchangamStr(panchangamList.get(8).getParikaram()).getColor()));
//        holder.txt10.setText("7.30-9.00: " + EnumPanchangam.getPanchangamStr(panchangamList.get(9).getParikaram()).getText());
//        holder.txt10.setTextColor(context.getResources().getColor(EnumPanchangam.getPanchangamStr(panchangamList.get(9).getParikaram()).getColor()));
//        holder.txt11.setText("9.00-10.30: " + EnumPanchangam.getPanchangamStr(panchangamList.get(10).getParikaram()).getText());
//        holder.txt11.setTextColor(context.getResources().getColor(EnumPanchangam.getPanchangamStr(panchangamList.get(10).getParikaram()).getColor()));
//        holder.txt12.setText("10.30-12.00: " + EnumPanchangam.getPanchangamStr(panchangamList.get(11).getParikaram()).getText());
//        holder.txt12.setTextColor(context.getResources().getColor(EnumPanchangam.getPanchangamStr(panchangamList.get(11).getParikaram()).getColor()));
//        holder.txt13.setText("12.00-1.30:" + EnumPanchangam.getPanchangamStr(panchangamList.get(12).getParikaram()).getText());
//        holder.txt13.setTextColor(context.getResources().getColor(EnumPanchangam.getPanchangamStr(panchangamList.get(12).getParikaram()).getColor()));
//        holder.txt14.setText("1.30-3.00: " + EnumPanchangam.getPanchangamStr(panchangamList.get(13).getParikaram()).getText());
//        holder.txt14.setTextColor(context.getResources().getColor(EnumPanchangam.getPanchangamStr(panchangamList.get(13).getParikaram()).getColor()));
//        holder.txt15.setText("3.00-4.30: " + EnumPanchangam.getPanchangamStr(panchangamList.get(14).getParikaram()).getText());
//        holder.txt15.setTextColor(context.getResources().getColor(EnumPanchangam.getPanchangamStr(panchangamList.get(14).getParikaram()).getColor()));
//        holder.txt16.setText("4.30-6.00: " + EnumPanchangam.getPanchangamStr(panchangamList.get(15).getParikaram()).getText());
//        holder.txt16.setTextColor(context.getResources().getColor(EnumPanchangam.getPanchangamStr(panchangamList.get(15).getParikaram()).getColor()));
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {



        ItemViewHolder(View view, int viewType) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
