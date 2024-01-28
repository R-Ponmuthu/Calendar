package com.it.calendar.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.it.calendar.CalendarApp;
import com.it.calendar.R;
import com.it.calendar.beans.MainTable;
import com.it.calendar.beans.VirathaDay;
import com.it.calendar.util.EnumWeekDay;
import com.it.core.db.TableHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VirathaFragment extends Fragment {

    @BindView(R.id.day1)
    TextView day1;
    @BindView(R.id.day2)
    TextView day2;
    @BindView(R.id.day3)
    TextView day3;
    @BindView(R.id.day4)
    TextView day4;
    @BindView(R.id.day5)
    TextView day5;
    @BindView(R.id.day6)
    TextView day6;
    @BindView(R.id.day7)
    TextView day7;
    @BindView(R.id.day8)
    TextView day8;
    @BindView(R.id.day9)
    TextView day9;
    @BindView(R.id.day10)
    TextView day10;
    @BindView(R.id.day11)
    TextView day11;
    @BindView(R.id.day12)
    TextView day12;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VirathaFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static VirathaFragment newInstance(String param1) {
        VirathaFragment fragment = new VirathaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public static VirathaFragment newInstance(String param1, String param2) {
        VirathaFragment fragment = new VirathaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_viratha, container, false);
        ButterKnife.bind(this, view);

        TableHelper<VirathaDay> virathaDayTableHelper = CalendarApp.getTable(getActivity(), VirathaDay.class);
        List<VirathaDay> virathaDays1 = virathaDayTableHelper.rawQuery(virathaDayTableHelper.getReadableDatabase(), "select * from VirathaDay where date like '%01-2024%' and viratham='" + mParam1 + "' union" +
                " select * from VirathaDay where date like '%01-2024%' and viratham='" + mParam2 + "'", null);
        StringBuilder stringBuilder1 = new StringBuilder();
        for (VirathaDay virathaDay : virathaDays1) {

            TableHelper<MainTable> mainTh = CalendarApp.getTable(getActivity(), MainTable.class);
            MainTable mainTable = mainTh.getItem(mainTh.getReadableDatabase(), "date = ?", new String[]{virathaDay.getDate()}, null);

            stringBuilder1.append(virathaDay.getDate().split("-")[0].trim()).append("-").append(EnumWeekDay.getWeekDay(mainTable.getWeekday()).getText()).append("\n");
        }
        day1.setText(stringBuilder1.substring(0, stringBuilder1.toString().length() - 1));

        List<VirathaDay> virathaDays2 = virathaDayTableHelper.rawQuery(virathaDayTableHelper.getReadableDatabase(), "select * from VirathaDay where date like '%02-2024%' and viratham='" + mParam1 + "' union" +
                " select * from VirathaDay where date like '%02-2024%' and viratham='" + mParam2 + "'", null);
        StringBuilder stringBuilder2 = new StringBuilder();
        for (VirathaDay virathaDay : virathaDays2) {

            TableHelper<MainTable> mainTh = CalendarApp.getTable(getActivity(), MainTable.class);
            MainTable mainTable = mainTh.getItem(mainTh.getReadableDatabase(), "date = ?", new String[]{virathaDay.getDate()}, null);

            stringBuilder2.append(virathaDay.getDate().split("-")[0].trim()).append("-").append(EnumWeekDay.getWeekDay(mainTable.getWeekday()).getText()).append("\n");
        }
        day2.setText(stringBuilder2.substring(0, stringBuilder2.toString().length() - 1));

        List<VirathaDay> virathaDays3 = virathaDayTableHelper.rawQuery(virathaDayTableHelper.getReadableDatabase(), "select * from VirathaDay where date like '%03-2024%' and viratham='" + mParam1 + "' union" +
                " select * from VirathaDay where date like '%03-2024%' and viratham='" + mParam2 + "'", null);
        StringBuilder stringBuilder3 = new StringBuilder();
        for (VirathaDay virathaDay : virathaDays3) {

            TableHelper<MainTable> mainTh = CalendarApp.getTable(getActivity(), MainTable.class);
            MainTable mainTable = mainTh.getItem(mainTh.getReadableDatabase(), "date = ?", new String[]{virathaDay.getDate()}, null);

            stringBuilder3.append(virathaDay.getDate().split("-")[0].trim()).append("-").append(EnumWeekDay.getWeekDay(mainTable.getWeekday()).getText()).append("\n");
        }
        if (!stringBuilder3.toString().isEmpty())
            day3.setText(stringBuilder3.substring(0, stringBuilder3.toString().length() - 1));

        List<VirathaDay> virathaDays4 = virathaDayTableHelper.rawQuery(virathaDayTableHelper.getReadableDatabase(), "select * from VirathaDay where date like '%04-2024%' and viratham='" + mParam1 + "' union" +
                " select * from VirathaDay where date like '%04-2024%' and viratham='" + mParam2 + "'", null);
        StringBuilder stringBuilder4 = new StringBuilder();
        for (VirathaDay virathaDay : virathaDays4) {

            TableHelper<MainTable> mainTh = CalendarApp.getTable(getActivity(), MainTable.class);
            MainTable mainTable = mainTh.getItem(mainTh.getReadableDatabase(), "date = ?", new String[]{virathaDay.getDate()}, null);

            stringBuilder4.append(virathaDay.getDate().split("-")[0].trim()).append("-").append(EnumWeekDay.getWeekDay(mainTable.getWeekday()).getText()).append("\n");
        }
        day4.setText(stringBuilder4.substring(0, stringBuilder4.toString().length() - 1));

        List<VirathaDay> virathaDays5 = virathaDayTableHelper.rawQuery(virathaDayTableHelper.getReadableDatabase(), "select * from VirathaDay where date like '%05-2024%' and viratham='" + mParam1 + "' union" +
                " select * from VirathaDay where date like '%05-2024%' and viratham='" + mParam2 + "'", null);
        StringBuilder stringBuilder5 = new StringBuilder();
        for (VirathaDay virathaDay : virathaDays5) {

            TableHelper<MainTable> mainTh = CalendarApp.getTable(getActivity(), MainTable.class);
            MainTable mainTable = mainTh.getItem(mainTh.getReadableDatabase(), "date = ?", new String[]{virathaDay.getDate()}, null);

            stringBuilder5.append(virathaDay.getDate().split("-")[0].trim()).append("-").append(EnumWeekDay.getWeekDay(mainTable.getWeekday()).getText()).append("\n");
        }
        day5.setText(stringBuilder5.substring(0, stringBuilder5.toString().length() - 1));

        List<VirathaDay> virathaDays6 = virathaDayTableHelper.rawQuery(virathaDayTableHelper.getReadableDatabase(), "select * from VirathaDay where date like '%06-2024%' and viratham='" + mParam1 + "' union" +
                " select * from VirathaDay where date like '%06-2024%' and viratham='" + mParam2 + "'", null);
        StringBuilder stringBuilder6 = new StringBuilder();
        for (VirathaDay virathaDay : virathaDays6) {

            TableHelper<MainTable> mainTh = CalendarApp.getTable(getActivity(), MainTable.class);
            MainTable mainTable = mainTh.getItem(mainTh.getReadableDatabase(), "date = ?", new String[]{virathaDay.getDate()}, null);

            stringBuilder6.append(virathaDay.getDate().split("-")[0].trim()).append("-").append(EnumWeekDay.getWeekDay(mainTable.getWeekday()).getText()).append("\n");
        }
        day6.setText(stringBuilder6.substring(0, stringBuilder6.toString().length() - 1));

        List<VirathaDay> virathaDays7 = virathaDayTableHelper.rawQuery(virathaDayTableHelper.getReadableDatabase(), "select * from VirathaDay where date like '%07-2024%' and viratham='" + mParam1 + "' union" +
                " select * from VirathaDay where date like '%07-2024%' and viratham='" + mParam2 + "'", null);
        StringBuilder stringBuilder7 = new StringBuilder();
        for (VirathaDay virathaDay : virathaDays7) {

            TableHelper<MainTable> mainTh = CalendarApp.getTable(getActivity(), MainTable.class);
            MainTable mainTable = mainTh.getItem(mainTh.getReadableDatabase(), "date = ?", new String[]{virathaDay.getDate()}, null);

            stringBuilder7.append(virathaDay.getDate().split("-")[0].trim()).append("-").append(EnumWeekDay.getWeekDay(mainTable.getWeekday()).getText()).append("\n");
        }
        day7.setText(stringBuilder7.substring(0, stringBuilder7.toString().length() - 1));

        List<VirathaDay> virathaDays8 = virathaDayTableHelper.rawQuery(virathaDayTableHelper.getReadableDatabase(), "select * from VirathaDay where date like '%08-2024%' and viratham='" + mParam1 + "' union" +
                " select * from VirathaDay where date like '%08-2024%' and viratham='" + mParam2 + "'", null);
        StringBuilder stringBuilder8 = new StringBuilder();
        for (VirathaDay virathaDay : virathaDays8) {

            TableHelper<MainTable> mainTh = CalendarApp.getTable(getActivity(), MainTable.class);
            MainTable mainTable = mainTh.getItem(mainTh.getReadableDatabase(), "date = ?", new String[]{virathaDay.getDate()}, null);

            stringBuilder8.append(virathaDay.getDate().split("-")[0].trim()).append("-").append(EnumWeekDay.getWeekDay(mainTable.getWeekday()).getText()).append("\n");
        }
        if (!stringBuilder8.toString().isEmpty())
            day8.setText(stringBuilder8.substring(0, stringBuilder8.toString().length() - 1));

        List<VirathaDay> virathaDays9 = virathaDayTableHelper.rawQuery(virathaDayTableHelper.getReadableDatabase(), "select * from VirathaDay where date like '%09-2024%' and viratham='" + mParam1 + "' union" +
                " select * from VirathaDay where date like '%09-2024%' and viratham='" + mParam2 + "'", null);
        StringBuilder stringBuilder9 = new StringBuilder();
        for (VirathaDay virathaDay : virathaDays9) {

            TableHelper<MainTable> mainTh = CalendarApp.getTable(getActivity(), MainTable.class);
            MainTable mainTable = mainTh.getItem(mainTh.getReadableDatabase(), "date = ?", new String[]{virathaDay.getDate()}, null);

            stringBuilder9.append(virathaDay.getDate().split("-")[0].trim()).append("-").append(EnumWeekDay.getWeekDay(mainTable.getWeekday()).getText()).append("\n");
        }
        day9.setText(stringBuilder9.substring(0, stringBuilder9.toString().length() - 1));

        List<VirathaDay> virathaDays10 = virathaDayTableHelper.rawQuery(virathaDayTableHelper.getReadableDatabase(), "select * from VirathaDay where date like '%10-2024%' and viratham='" + mParam1 + "' union" +
                " select * from VirathaDay where date like '%10-2024%' and viratham='" + mParam2 + "'", null);
        StringBuilder stringBuilder10 = new StringBuilder();
        for (VirathaDay virathaDay : virathaDays10) {

            TableHelper<MainTable> mainTh = CalendarApp.getTable(getActivity(), MainTable.class);
            MainTable mainTable = mainTh.getItem(mainTh.getReadableDatabase(), "date = ?", new String[]{virathaDay.getDate()}, null);

            stringBuilder10.append(virathaDay.getDate().split("-")[0].trim()).append("-").append(EnumWeekDay.getWeekDay(mainTable.getWeekday()).getText()).append("\n");
        }
        day10.setText(stringBuilder10.substring(0, stringBuilder10.toString().length() - 1));

        List<VirathaDay> virathaDays11 = virathaDayTableHelper.rawQuery(virathaDayTableHelper.getReadableDatabase(), "select * from VirathaDay where date like '%11-2024%' and viratham='" + mParam1 + "' union" +
                " select * from VirathaDay where date like '%11-2024%' and viratham='" + mParam2 + "'", null);
        StringBuilder stringBuilder11 = new StringBuilder();
        for (VirathaDay virathaDay : virathaDays11) {

            TableHelper<MainTable> mainTh = CalendarApp.getTable(getActivity(), MainTable.class);
            MainTable mainTable = mainTh.getItem(mainTh.getReadableDatabase(), "date = ?", new String[]{virathaDay.getDate()}, null);

            stringBuilder11.append(virathaDay.getDate().split("-")[0].trim()).append("-").append(EnumWeekDay.getWeekDay(mainTable.getWeekday()).getText()).append("\n");
        }
        day11.setText(stringBuilder11.substring(0, stringBuilder11.toString().length() - 1));

        List<VirathaDay> virathaDays12 = virathaDayTableHelper.rawQuery(virathaDayTableHelper.getReadableDatabase(), "select * from VirathaDay where date like '%12-2024%' and viratham='" + mParam1 + "' union" +
                " select * from VirathaDay where date like '%12-2024%' and viratham='" + mParam2 + "'", null);
        StringBuilder stringBuilder12 = new StringBuilder();
        for (VirathaDay virathaDay : virathaDays12) {

            TableHelper<MainTable> mainTh = CalendarApp.getTable(getActivity(), MainTable.class);
            MainTable mainTable = mainTh.getItem(mainTh.getReadableDatabase(), "date = ?", new String[]{virathaDay.getDate()}, null);

            stringBuilder12.append(virathaDay.getDate().split("-")[0].trim()).append("-").append(EnumWeekDay.getWeekDay(mainTable.getWeekday()).getText()).append("\n");
        }
        day12.setText(stringBuilder12.substring(0, stringBuilder12.toString().length() - 1));

        return view;
    }
}