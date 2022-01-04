package com.it.calendar.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.it.calendar.beans.Vasthu;
import com.it.calendar.util.Constants;
import com.it.calendar.util.DateTimeHelper;
import com.it.calendar.util.EnumMonth;
import com.it.calendar.util.EnumTamilMonth;
import com.it.calendar.util.EnumWeekDay;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.it.core.db.TableHelper;

public class VasthuAdapter extends RecyclerView.Adapter<VasthuAdapter.ItemViewHolder> {

    List<Vasthu> vasthus;
    private Context context;

    public VasthuAdapter(Context context, List<Vasthu> vasthus) {

        this.context = context;
        this.vasthus = vasthus;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vasthu_sub_item, parent, false);
        return new ItemViewHolder(itemView, viewType);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

        Vasthu vasthu = vasthus.get(position);
        holder.bind_kalangal(position, vasthu);
    }

    @Override
    public int getItemCount() {
        return vasthus.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.containerLay)
        LinearLayout containerLay;
        @BindView(R.id.timeline)
        TimelineView timelineView;

        ItemViewHolder(View view, int viewType) {
            super(view);
            ButterKnife.bind(this, view);

            timelineView.initLine(viewType);
        }

        @SuppressLint("SetTextI18n")
        void bind_kalangal(int position, Vasthu vasthu) {

            TableHelper<MainTable> mainTableTableHelper = CalendarApp.getTable(context, MainTable.class);
            MainTable mainTable = mainTableTableHelper.getItem(mainTableTableHelper.getReadableDatabase(), "date=?",
                    new String[]{String.valueOf(vasthu.getDay())}, null);

//            MainTable mainTable = realm.where(MainTable.class)
//                    .equalTo(Constants.date, DateTimeHelper.getMillisFromDate(vasthu.getDay()))
//                    .findFirst();

            date.setText(mainTable.getDay() + " " + EnumMonth.getMonthStr(mainTable.getMonth()).getText() + "," + EnumWeekDay.getWeekDay(mainTable.getWeekday()).getText() + " - " +
                    mainTable.getTam_day() + " " + EnumTamilMonth.getTamilMonth(mainTable.getTam_month()).getText());
            time.setText("காலை " + vasthu.getTime());

            if (position == 0 || position % 2 == 0) {
                containerLay.setBackgroundColor(context.getResources().getColor(R.color.grey_ec));
            }
        }
    }
}
