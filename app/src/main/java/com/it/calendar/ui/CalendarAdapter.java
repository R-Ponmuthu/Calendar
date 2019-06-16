package com.it.calendar.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.it.calendar.R;
import com.it.calendar.model.GowriNeram;
import com.it.calendar.model.Kalangal;
import com.it.calendar.model.MainTable;
import com.it.calendar.model.Virathaday;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class CalendarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private MainTable mainTable;
    private Realm realm;

    CalendarAdapter(Context context, MainTable mainTbl) {
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
        calendarViewHolder.monthYear.setText(mainTable.getMonth() + "  " + mainTable.getYear());

        calendarViewHolder.tamilDate.setText("" + mainTable.getDay());
        calendarViewHolder.tamilMonth.setText(mainTable.getTam_month());
        calendarViewHolder.tamilYear.setText(mainTable.getTam_year() + " வருடம்");

        calendarViewHolder.quote.setText(mainTable.getQuote());

        Kalangal kalangal = realm.where(Kalangal.class)
                .equalTo("year", mainTable.getYear())
                .and()
                .equalTo("weekday", mainTable.getWeekday())
                .findFirst();

        GowriNeram gowriNeram = realm.where(GowriNeram.class)
                .equalTo("date", mainTable.getDate())
                .findFirst();

        RealmResults<Virathaday> virathaDays = realm.where(Virathaday.class)
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
            for (Virathaday virathaDay : virathaDays)
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

        /*switch (getItemViewType(position)) {
>>>>>>> Stashed changes
            case 0:
                GroupViewHolder groupViewHolder = (GroupViewHolder) holder;
                if (mainTable != null) {

                    Kalangal Kalangal = realm.where(Kalangal.class)
                            .equalTo("year", mainTable.getYear())
                            .and()
                            .equalTo("weekday", mainTable.getWeekday())
                            .findFirst();

                    GowriNeram gowriNeram = realm.where(GowriNeram.class)
                            .equalTo("date", mainTable.getDate())
                            .findFirst();

                    RealmResults<Virathaday> virathaDays = realm.where(Virathaday.class)
                            .equalTo("date", mainTable.getDate())
                            .findAll();

                    if (mainTable.getDay_type().equals("மேல் நோக்கு நாள்")) {
                        groupViewHolder.daySymbol.setImageResource(R.drawable.ic_up_arrow);
                    } else if (mainTable.getDay_type().equals("கீழ் நோக்கு நாள்")) {
                        groupViewHolder.daySymbol.setImageResource(R.drawable.ic_down_arrow);
                    } else {
                        groupViewHolder.daySymbol.setImageResource(R.drawable.ic_double_arrow);
                    }

                    StringBuilder stringBuilder = new StringBuilder();
                    if (!mainTable.getGov_holiday().equals("-"))
                        stringBuilder.append(mainTable.getGov_holiday()).append(",");
                    if (!mainTable.getHindu_fes().equals("-"))
                        stringBuilder.append(mainTable.getHindu_fes()).append(',');
                    if (!mainTable.getChirs_fes().equals("-"))
                        stringBuilder.append(mainTable.getChirs_fes()).append(",");
                    if (!mainTable.getMuslim_fes().equals("-"))
                        stringBuilder.append(mainTable.getMuslim_fes()).append(",");
                    if (virathaDays.size() > 0)
<<<<<<< Updated upstream
                        for (VirathaDay virathaDay : virathaDays)
                            stringBuilder.append(virathaDay.getViratham()).append(",");
=======
                        for (Virathaday virathaDay : virathaDays)
                            stringBuilder.append(virathaDay.getViratham()).append("\n\n");

                    if (stringBuilder.length() > 0)
                        groupViewHolder.impDays.setText("" + stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString());
                    else
                        groupViewHolder.impCard.setVisibility(View.GONE);
>>>>>>> Stashed changes

                    if (stringBuilder != null)
                        groupViewHolder.impDays.setText("" + stringBuilder.toString());
                    groupViewHolder.title.setText(mainTable.getTam_day() + "-" + mainTable.getTam_month() + "-" + mainTable.getTam_year());
                    groupViewHolder.nallaNeramM.setText("கா. " + mainTable.getNallanerem_m());
                    groupViewHolder.nallaNeramE.setText("மா. " + mainTable.getNallanerem_e());
                    if (gowriNeram != null) {
                        groupViewHolder.gowriNeramM.setText("கா. " + gowriNeram.getGowri_m());
                        groupViewHolder.gowriNeramE.setText("மா. " + gowriNeram.getGowri_e());
                        groupViewHolder.suriyaUdhayam.setText("சூரி.உ   " + gowriNeram.getSooriya_r());
                    }
                    if (Kalangal != null) {
                        groupViewHolder.raagu.setText("இரா  " + Kalangal.getRagu());
                        groupViewHolder.kulikai.setText("குளி  " + Kalangal.getKuligai());
                        groupViewHolder.emakandam.setText("எம  " + Kalangal.getEmakandam());
                    }
                    groupViewHolder.thithi.setText("" + mainTable.getThiti());
                    groupViewHolder.nadchadhthiram.setText("" + mainTable.getStar());
                    groupViewHolder.yokam.setText("" + mainTable.getYokam());
                    groupViewHolder.chandhiram.setText("" + mainTable.getChanthran());
                }
                break;
            case 1:
                if (mainTable != null) {
                    RasiViewHolder rasiViewHolder = (RasiViewHolder) holder;
                    rasiViewHolder.title.setText("ராசி பலன்");
                    rasiViewHolder.rasi1.setText("மேஷம் - " + mainTable.getMesam());
                    rasiViewHolder.rasi2.setText("ரிஷபம் - " + mainTable.getRisibam());
                    rasiViewHolder.rasi3.setText("மிதுனம் - " + mainTable.getMithunam());
                    rasiViewHolder.rasi4.setText("கடகம் - " + mainTable.getKadakam());
                    rasiViewHolder.rasi5.setText("சிம்மம் - " + mainTable.getSimmam());
                    rasiViewHolder.rasi6.setText("கன்னி - " + mainTable.getKanni());
                    rasiViewHolder.rasi7.setText("துலாம் - " + mainTable.getThulam());
                    rasiViewHolder.rasi8.setText("விருச்சிகம் - " + mainTable.getViruchakam());
                    rasiViewHolder.rasi9.setText("தனுசு - " + mainTable.getDhanusu());
                    rasiViewHolder.rasi10.setText("மகரம் - " + mainTable.getMakaram());
                    rasiViewHolder.rasi11.setText("கும்பம் - " + mainTable.getKumbam());
                    rasiViewHolder.rasi12.setText("மீனம் - " + mainTable.getMeenam());
                }
                break;
            case 2:
                if (mainTable != null) {
                    ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                    itemViewHolder.title.setText("நாள் மேற்கோள்");
                    itemViewHolder.content.setText("\n" + mainTable.getQuote() + "\n");
                }
                break;
            case 3:
                if (mainTable != null) {
                    ItemViewHolder itemViewHolder1 = (ItemViewHolder) holder;
                    itemViewHolder1.title.setText("முக்கியமான நாட்கள்");
                    String[] impDays = mainTable.getImportantday().split(",");
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < impDays.length; i++) {
                        stringBuilder.append("\n" + "*" + impDays[i] + "\n");
                    }
                    itemViewHolder1.content.setText("" + stringBuilder.toString());
                }
                break;
            case 4:
                if (mainTable != null) {
                    ItemViewHolder itemViewHolder2 = (ItemViewHolder) holder;
                    itemViewHolder2.title.setText("அரசு விடுமுறை");
                    itemViewHolder2.content.setText(mainTable.getGov_holiday());
                }
                break;
            case 5:
                if (mainTable != null) {
                    ItemViewHolder itemViewHolder3 = (ItemViewHolder) holder;
                    itemViewHolder3.title.setText("திருவிழாக்கள்");
                    itemViewHolder3.content.setText(mainTable.getHindu_fes() + "\n" +
                            mainTable.getChirs_fes() + "\n" +
                            mainTable.getMuslim_fes());
                }
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {

        switch (position) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            default:
                return 1;
        }
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    class GroupViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.nallaNeramM)
        TextView nallaNeramM;
        @BindView(R.id.nallaNeramE)
        TextView nallaNeramE;
        @BindView(R.id.gowriNeramM)
        TextView gowriNeramM;
        @BindView(R.id.gowriNeramE)
        TextView gowriNeramE;
        @BindView(R.id.raagu)
        TextView raagu;
        @BindView(R.id.kulikai)
        TextView kulikai;
        @BindView(R.id.emakandam)
        TextView emakandam;
        @BindView(R.id.suriya_udhayam)
        TextView suriyaUdhayam;
        @BindView(R.id.thithi)
        TextView thithi;
        @BindView(R.id.nadchadhthiram)
        TextView nadchadhthiram;
        @BindView(R.id.yokam)
        TextView yokam;
        @BindView(R.id.impDays)
        TextView impDays;
        @BindView(R.id.chandhiram)
        TextView chandhiram;
        @BindView(R.id.day_symbol)
        ImageView daySymbol;

        GroupViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView title, content;

        ItemViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            content = view.findViewById(R.id.content);
        }
    }

    class RasiViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;
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

        RasiViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
<<<<<<< Updated upstream
=======
    }*/

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
