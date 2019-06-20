package com.it.calendar.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.it.calendar.R;
import com.it.calendar.model.MainTable;
import com.it.calendar.model.VirathaDay;
import com.it.calendar.model.gowri_neram;
import com.it.calendar.model.kalangal;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class CalendarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private MainTable mainTable;
    private Realm realm;

    public CalendarAdapter(Context context, MainTable mainTbl) {
        this.context = context;
        this.mainTable = mainTbl;

        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_calendar_item, parent, false);
        return new CalendarViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        CalendarViewHolder calendarViewHolder = (CalendarViewHolder) holder;

        calendarViewHolder.date.setText("" + mainTable.getDay());
        calendarViewHolder.day.setText("" + mainTable.getWeekday());
        calendarViewHolder.monthYear.setText(mainTable.getMonth());

        calendarViewHolder.tamilDate.setText("" + mainTable.getTam_day());
        calendarViewHolder.tamilMonth.setText(mainTable.getTam_month());
        calendarViewHolder.tamilYear.setText(mainTable.getTam_year() + " வருடம்");

        calendarViewHolder.monthYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                context.startActivity(new Intent(context, CurrentMonthActivity.class)
//                        .putExtra("Month", mainTable.getMonth())
//                        .putExtra("Year", mainTable.getYear()));
            }
        });

        calendarViewHolder.quote.setText(mainTable.getQuote());

        kalangal kalangal = realm.where(kalangal.class)
                .equalTo("year", mainTable.getYear())
                .and()
                .equalTo("weekday", mainTable.getWeekday())
                .findFirst();

        gowri_neram gowriNeram = realm.where(gowri_neram.class)
                .equalTo("date", mainTable.getDate())
                .findFirst();

        RealmResults<VirathaDay> virathaDays = realm.where(VirathaDay.class)
                .equalTo("date", mainTable.getDate())
                .findAll();

        calendarViewHolder.nallaNeramK.setText(mainTable.getNallanerem_m());
        calendarViewHolder.nallaNeramM.setText(mainTable.getNallanerem_e());

        calendarViewHolder.gowriNeramK.setText(gowriNeram.getGowri_m());
        calendarViewHolder.gowriNeramM.setText(gowriNeram.getGowri_e());

        calendarViewHolder.raagu1.setText(kalangal.getRagu());
        calendarViewHolder.kulikai1.setText(kalangal.getKuligai());
        calendarViewHolder.ema1.setText(kalangal.getEmakandam());

        calendarViewHolder.soolam.setText("சூலம்: " + kalangal.getSoolam());
        calendarViewHolder.parikaram.setText("பரிகாரம்: " + kalangal.getParikaram());

        calendarViewHolder.sUdayam.setText("" + gowriNeram.getSooriya_r());
        calendarViewHolder.chandhiram.setText(mainTable.getChanthran());
        calendarViewHolder.yokam.setText(mainTable.getYokam());

        calendarViewHolder.thithi.setText(mainTable.getThiti());
        calendarViewHolder.natchatiram.setText(mainTable.getStar());


        StringBuilder stringBuilder = new StringBuilder();
        if (!mainTable.getGov_holiday().equals("-"))
            stringBuilder.append(mainTable.getGov_holiday()).append("\n");
        if (!mainTable.getHindu_fes().equals("-"))
            stringBuilder.append(mainTable.getHindu_fes()).append("\n");
        if (!mainTable.getChirs_fes().equals("-"))
            stringBuilder.append(mainTable.getChirs_fes()).append("\n");
        if (!mainTable.getMuslim_fes().equals("-"))
            stringBuilder.append(mainTable.getMuslim_fes()).append("\n");
        if (virathaDays.size() > 0)
            for (VirathaDay virathaDay : virathaDays)
                stringBuilder.append(virathaDay.getViratham()).append("\n");

        if (stringBuilder.length() > 0)
            calendarViewHolder.festivals.setText("" + stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString());
        else
            calendarViewHolder.festivals.setVisibility(View.GONE);

        String[] impDays = mainTable.getImportantday().split(",");
        StringBuilder stringBuilder1 = new StringBuilder();
        for (String impDay : impDays) {
            stringBuilder1.append(impDay + "\n");
        }
        calendarViewHolder.impDays.setText("" + stringBuilder1.toString());

        calendarViewHolder.rasi1.setText("மேஷம் - " + mainTable.getMesam());
        calendarViewHolder.rasi2.setText("ரிஷபம் - " + mainTable.getRisibam());
        calendarViewHolder.rasi3.setText("மிதுனம் - " + mainTable.getMithunam());
        calendarViewHolder.rasi4.setText("கடகம் - " + mainTable.getKadakam());
        calendarViewHolder.rasi5.setText("சிம்மம் - " + mainTable.getSimmam());
        calendarViewHolder.rasi6.setText("கன்னி - " + mainTable.getKanni());
        calendarViewHolder.rasi7.setText("துலாம் - " + mainTable.getThulam());
        calendarViewHolder.rasi8.setText("விருச்சிகம் - " + mainTable.getViruchakam());
        calendarViewHolder.rasi9.setText("தனுசு - " + mainTable.getDhanusu());
        calendarViewHolder.rasi10.setText("மகரம் - " + mainTable.getMakaram());
        calendarViewHolder.rasi11.setText("கும்பம் - " + mainTable.getKumbam());
        calendarViewHolder.rasi12.setText("மீனம் - " + mainTable.getMeenam());

        if (mainTable.getDay_type().equals("மேல் நோக்கு நாள்")) {
            calendarViewHolder.daySymbol.setImageResource(R.drawable.ic_up_arrow);
        } else if (mainTable.getDay_type().equals("கீழ் நோக்கு நாள்")) {
            calendarViewHolder.daySymbol.setImageResource(R.drawable.ic_down_arrow);
        } else {
            calendarViewHolder.daySymbol.setImageResource(R.drawable.ic_double_arrow);
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class CalendarViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.monthYear)
        TextView monthYear;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.day)
        TextView day;
        @BindView(R.id.tamilYear)
        TextView tamilYear;
        @BindView(R.id.tamilMonth)
        TextView tamilMonth;
        @BindView(R.id.tamilDate)
        TextView tamilDate;
        @BindView(R.id.quote)
        TextView quote;
        @BindView(R.id.festivals)
        TextView festivals;
        @BindView(R.id.rasi1)
        TextView rasi1;
        @BindView(R.id.rasi2)
        TextView rasi2;
        @BindView(R.id.rasi3)
        TextView rasi3;
        @BindView(R.id.rasi4)
        TextView rasi4;
        @BindView(R.id.rasi5)
        TextView rasi5;
        @BindView(R.id.rasi6)
        TextView rasi6;
        @BindView(R.id.rasi7)
        TextView rasi7;
        @BindView(R.id.rasi8)
        TextView rasi8;
        @BindView(R.id.rasi9)
        TextView rasi9;
        @BindView(R.id.rasi10)
        TextView rasi10;
        @BindView(R.id.rasi11)
        TextView rasi11;
        @BindView(R.id.rasi12)
        TextView rasi12;
        @BindView(R.id.day_symbol)
        ImageView daySymbol;
        @BindView(R.id.impDays)
        TextView impDays;

        @BindView(R.id.nallaNeram_K)
        TextView nallaNeramK;
        @BindView(R.id.nallaNeram_M)
        TextView nallaNeramM;
        @BindView(R.id.gowriNeram_K)
        TextView gowriNeramK;
        @BindView(R.id.gowriNeram_M)
        TextView gowriNeramM;
        @BindView(R.id.raagu1)
        TextView raagu1;
        @BindView(R.id.kulikai1)
        TextView kulikai1;
        @BindView(R.id.ema1)
        TextView ema1;
        @BindView(R.id.soolam)
        TextView soolam;
        @BindView(R.id.parikaram)
        TextView parikaram;
        @BindView(R.id.chandhiram)
        TextView chandhiram;
        @BindView(R.id.s_udayam)
        TextView sUdayam;
        @BindView(R.id.yokam)
        TextView yokam;
        @BindView(R.id.thithi)
        TextView thithi;
        @BindView(R.id.natchatiram)
        TextView natchatiram;

        CalendarViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
