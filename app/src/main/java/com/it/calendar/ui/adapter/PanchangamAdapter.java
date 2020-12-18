package com.it.calendar.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.it.calendar.R;
import com.it.calendar.beans.Panchangam;
import com.it.calendar.util.EnumNeram;
import com.it.calendar.util.EnumWeekDay;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class PanchangamAdapter extends RecyclerView.Adapter<PanchangamAdapter.ItemViewHolder> {

    private List<Panchangam> weekdays;
    private Context context;
    private Realm realm;
    private List<Panchangam> PpanchangamList = new ArrayList<>();
    private List<Panchangam> IpanchangamList = new ArrayList<>();
    private int curYear;

    private String[] times = new String[]{"6 - 7.30",
            "7.30 - 9.00",
            "9 - 10.30",
            "10.30 - 12",
            "12 - 1.30",
            "1.30 - 3",
            "3 - 4.30",
            "4.30 - 6"};

    public PanchangamAdapter(Context context, int curYear, RealmResults<Panchangam> weekdays) {
        this.context = context;
        this.weekdays = weekdays;
        this.curYear = curYear;

        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_item_layout, parent, false);
        return new ItemViewHolder(itemView);
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

        ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind_panchangam(Panchangam panchangam) {

            PpanchangamList = new ArrayList<>();
            IpanchangamList = new ArrayList<>();

            container.removeAllViews();
            title.setText(EnumWeekDay.getWeekDay(panchangam.getWeekday()).getText());

            for (String time : times) {

                Panchangam Ppanchangam = realm.where(Panchangam.class)
                        .in("time", new String[]{time})
                        .and()
                        .equalTo("neram", EnumNeram.getNeram("பகல்").getNeram())
                        .and()
                        .equalTo("weekday", EnumWeekDay.getWeekDay(panchangam.getWeekday()).getDay())
                        .and()
                        .equalTo("year", curYear)
                        .findFirst();

                Panchangam Ipanchangam = realm.where(Panchangam.class)
                        .in("time", new String[]{time})
                        .and()
                        .equalTo("neram", EnumNeram.getNeram("இரவு").getNeram())
                        .and()
                        .equalTo("weekday", EnumWeekDay.getWeekDay(panchangam.getWeekday()).getDay())
                        .and()
                        .equalTo("year", curYear)
                        .findFirst();

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

            txt1.setText("6.00-7.30: " + ppanchangam.get(0).getParikaram());
            txt2.setText("7.30-9.00: " + ppanchangam.get(1).getParikaram());
            txt3.setText("9.00-10.30: " + ppanchangam.get(2).getParikaram());
            txt4.setText("10.30-12.00: " + ppanchangam.get(3).getParikaram());
            txt5.setText("12.00-1.30: " + ppanchangam.get(4).getParikaram());
            txt6.setText("1.30-3.00: " + ppanchangam.get(5).getParikaram());
            txt7.setText("3.00-4.30: " + ppanchangam.get(6).getParikaram());
            txt8.setText("4.30-6.00: " + ppanchangam.get(7).getParikaram());
            txt9.setText("6.00-7.30: " + ipanchangam.get(0).getParikaram());
            txt10.setText("7.30-9.00: " + ipanchangam.get(1).getParikaram());
            txt11.setText("9.00-10.30: " + ipanchangam.get(2).getParikaram());
            txt12.setText("10.30-12.00: " + ipanchangam.get(3).getParikaram());
            txt13.setText("12.00-1.30:" + ipanchangam.get(4).getParikaram());
            txt14.setText("1.30-3.00: " + ipanchangam.get(5).getParikaram());
            txt15.setText("3.00-4.30: " + ipanchangam.get(6).getParikaram());
            txt16.setText("4.30-6.00: " + ipanchangam.get(7).getParikaram());
        }
    }
}
