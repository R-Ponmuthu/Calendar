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
import com.it.calendar.beans.Krakakalam;
import com.it.calendar.util.EnumKrakakalam;
import com.it.calendar.util.EnumNeram;
import com.it.calendar.util.EnumWeekDay;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class SubaHoraiAdapter extends RecyclerView.Adapter<SubaHoraiAdapter.ItemViewHolder> {

    private List<Krakakalam> weekdays;
    private Context context;
    private Realm realm;
    private List<Krakakalam> PKrakakalamList = new ArrayList<>();
    private List<Krakakalam> IKrakakalamList = new ArrayList<>();
    private int curYear;

    private String[] times = new String[]{"6.00 - 7.00",
            "7.00 - 8.00",
            "8.00 - 9.00",
            "9.00 - 10.00",
            "10.00 - 11.00",
            "11.00 - 12.00",
            "12.00 - 1.00",
            "1.00 - 2.00",
            "2.00 - 3.00",
            "3.00 - 4.00",
            "4.00 - 5.00",
            "5.00 - 6.00"};

    public SubaHoraiAdapter(Context context, int curYear, RealmResults<Krakakalam> weekdays) {
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

        holder.bind_krakakalam(weekdays.get(position));
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

        void bind_krakakalam(Krakakalam krakakalam) {

            PKrakakalamList = new ArrayList<>();
            IKrakakalamList = new ArrayList<>();

            container.removeAllViews();
            title.setText(EnumWeekDay.getWeekDay(krakakalam.getWeekday()).getText());

            for (String time : times) {

                Krakakalam Pkrakakalam = realm.where(Krakakalam.class)
                        .in("time", new String[]{time})
                        .and()
                        .equalTo("neram", EnumNeram.getNeram("பகல்").getNeram())
                        .and()
                        .equalTo("weekday", EnumWeekDay.getWeekDay(krakakalam.getWeekday()).getDay())
                        .and()
                        .equalTo("year", curYear)
                        .findFirst();

                Krakakalam Ikrakakalam = realm.where(Krakakalam.class)
                        .in("time", new String[]{time})
                        .and()
                        .equalTo("neram", EnumNeram.getNeram("இரவு").getNeram())
                        .and()
                        .equalTo("weekday", EnumWeekDay.getWeekDay(krakakalam.getWeekday()).getDay())
                        .and()
                        .equalTo("year", curYear)
                        .findFirst();

                PKrakakalamList.add(Pkrakakalam);
                IKrakakalamList.add(Ikrakakalam);
            }

            View view = LayoutInflater.from(context).inflate(R.layout.suba_horai_sub_item, null, false);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(params);
            SubItemViewHolder subItemViewHolder = new SubItemViewHolder(view);
            subItemViewHolder.bind(PKrakakalamList, IKrakakalamList);

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
        @BindView(R.id.txt17)
        TextView txt17;
        @BindView(R.id.txt18)
        TextView txt18;
        @BindView(R.id.txt19)
        TextView txt19;
        @BindView(R.id.txt20)
        TextView txt20;
        @BindView(R.id.txt21)
        TextView txt21;
        @BindView(R.id.txt22)
        TextView txt22;
        @BindView(R.id.txt23)
        TextView txt23;
        @BindView(R.id.txt24)
        TextView txt24;


        SubItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @SuppressLint("SetTextI18n")
        void bind(List<Krakakalam> pkrakakalam, List<Krakakalam> ikrakakalam) {

            txt1.setText("6.00 - 7.00: " + EnumKrakakalam.getKrakakalamStr(pkrakakalam.get(0).getYokam()).getText());
            txt2.setText("7.00 - 8.00: " + EnumKrakakalam.getKrakakalamStr(pkrakakalam.get(1).getYokam()).getText());
            txt3.setText("8.00 - 9.00: " + EnumKrakakalam.getKrakakalamStr(pkrakakalam.get(2).getYokam()).getText());
            txt4.setText("9.00 - 10.00: " + EnumKrakakalam.getKrakakalamStr(pkrakakalam.get(3).getYokam()).getText());
            txt5.setText("10.00 - 11.00: " + EnumKrakakalam.getKrakakalamStr(pkrakakalam.get(4).getYokam()).getText());
            txt6.setText("11.00 - 12.00: " + EnumKrakakalam.getKrakakalamStr(pkrakakalam.get(5).getYokam()).getText());
            txt7.setText("12.00 - 1.00: " + EnumKrakakalam.getKrakakalamStr(pkrakakalam.get(6).getYokam()).getText());
            txt8.setText("1.00 - 2.00: " + EnumKrakakalam.getKrakakalamStr(pkrakakalam.get(7).getYokam()).getText());
            txt9.setText("2.00 - 300: " + EnumKrakakalam.getKrakakalamStr(pkrakakalam.get(8).getYokam()).getText());
            txt10.setText("3.00 - 4.00: " + EnumKrakakalam.getKrakakalamStr(pkrakakalam.get(9).getYokam()).getText());
            txt11.setText("4.00 - 5.00: " + EnumKrakakalam.getKrakakalamStr(pkrakakalam.get(10).getYokam()).getText());
            txt12.setText("5.00 - 6.00: " + EnumKrakakalam.getKrakakalamStr(pkrakakalam.get(11).getYokam()).getText());
            txt13.setText("6.00 - 7.00: " + EnumKrakakalam.getKrakakalamStr(ikrakakalam.get(0).getYokam()).getText());
            txt14.setText("7.00 - 8.00: " + EnumKrakakalam.getKrakakalamStr(ikrakakalam.get(1).getYokam()).getText());
            txt15.setText("8.00 - 9.00: " + EnumKrakakalam.getKrakakalamStr(ikrakakalam.get(2).getYokam()).getText());
            txt16.setText("9.00 - 10.00: " + EnumKrakakalam.getKrakakalamStr(ikrakakalam.get(3).getYokam()).getText());
            txt17.setText("10.00 - 11.00: " + EnumKrakakalam.getKrakakalamStr(ikrakakalam.get(4).getYokam()).getText());
            txt18.setText("11.00 - 12.00: " + EnumKrakakalam.getKrakakalamStr(ikrakakalam.get(5).getYokam()).getText());
            txt19.setText("12.00 - 1.00: " + EnumKrakakalam.getKrakakalamStr(ikrakakalam.get(6).getYokam()).getText());
            txt20.setText("1.00 - 2.00: " + EnumKrakakalam.getKrakakalamStr(ikrakakalam.get(7).getYokam()).getText());
            txt21.setText("2.00 - 3.00: " + EnumKrakakalam.getKrakakalamStr(ikrakakalam.get(8).getYokam()).getText());
            txt22.setText("3.00 - 4.00: " + EnumKrakakalam.getKrakakalamStr(ikrakakalam.get(9).getYokam()).getText());
            txt23.setText("4.00 - 5.00: " + EnumKrakakalam.getKrakakalamStr(ikrakakalam.get(10).getYokam()).getText());
            txt24.setText("5.00 - 6.00: " + EnumKrakakalam.getKrakakalamStr(ikrakakalam.get(11).getYokam()).getText());
        }
    }
}
