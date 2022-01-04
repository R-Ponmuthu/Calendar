package com.it.calendar.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.it.calendar.CalendarApp;
import com.it.calendar.R;
import com.it.calendar.beans.Kalangal;
import com.it.calendar.beans.Krakakalam;
import com.it.calendar.beans.MainTable;
import com.it.calendar.beans.Panchangam;
import com.it.calendar.beans.Vasthu;
import com.it.calendar.beans.VirathaDay;
import com.it.calendar.ui.adapter.AsubaNaatkalAdapter;
import com.it.calendar.ui.adapter.FestivalAdapter;
import com.it.calendar.ui.adapter.MuhurthamAdapter;
import com.it.calendar.ui.adapter.PanchangamAdapter;
import com.it.calendar.ui.adapter.RaaguAdapter;
import com.it.calendar.ui.adapter.SubaHoraiAdapter;
import com.it.calendar.ui.adapter.VasthuAdapter;
import com.it.calendar.ui.adapter.VirathamAdapter;
import com.it.calendar.util.Constants;
import com.it.calendar.util.DateTimeHelper;
import com.it.calendar.util.EnumMonth;
import com.it.calendar.util.EnumVirathaDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.it.core.db.TableHelper;


public class CalendarFragment extends Fragment {

    //    public static String[] viratham = new String[]{"அமாவாசை", "பௌர்ணமி", "கிருத்திகை", "சஷ்டி", "சங்கடஹர சதுர்த்தி", "திருவோணம்", "சிவராத்திரி", "ஏகாதசி", "பிரதோஷம்", "சதுர்த்தி"};
    public static String[] viratham = new String[]{"1", "3", "4", "6", "7", "17", "11", "13", "14", "15"};
    public static Integer[] years = new Integer[]{2020, 2021, 2022};
    @BindView(R.id.muhurtham)
    TextView muhurtham;
    @BindView(R.id.panchangam)
    TextView panchangham;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.year)
    AppCompatAutoCompleteTextView year;
    @BindView(R.id.tagContainerLayout)
    ChipGroup tagContainerLayout;
    @BindView(R.id.chip1)
    Chip chip1;
    @BindView(R.id.chip2)
    Chip chip2;
    @BindView(R.id.chip3)
    Chip chip3;
    private String queryFlag;
    private int curYear;
    private final HashMap<String, List<?>> listMap = new HashMap<>();
    private final List<String> monthsList = new ArrayList<>();


    public CalendarFragment() {
        // Required empty public constructor
    }


    public static CalendarFragment newInstance(String queryFlag, int curYear) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString("queryFlag", queryFlag);
        args.putInt("curYear", curYear);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            queryFlag = getArguments().getString("queryFlag");
            curYear = getArguments().getInt("curYear");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        notifyAdapter(curYear);

        if (curYear == 2021)
            chip2.setChecked(true);
        else
            chip3.setChecked(true);

        chip1.setOnClickListener(view1 -> {
            chip1.setChecked(true);
            chip2.setChecked(false);
            chip3.setChecked(false);
            notifyAdapter(Integer.parseInt(chip1.getText().toString()));
        });

        chip2.setOnClickListener(view12 -> {
            chip2.setChecked(true);
            chip1.setChecked(false);
            chip3.setChecked(false);
            notifyAdapter(Integer.parseInt(chip2.getText().toString()));
        });

        chip3.setOnClickListener(view12 -> {
            chip3.setChecked(true);
            chip1.setChecked(false);
            chip2.setChecked(false);
            notifyAdapter(Integer.parseInt(chip3.getText().toString()));
        });

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, years);
        year.setAdapter(adapter);
        year.setOnClickListener(v -> year.showDropDown());
        year.setHint("" + curYear);

        year.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                notifyAdapter(years[position]);
            }
        });
    }

    private void notifyAdapter(int curYear) {

        TableHelper<MainTable> mainTableTableHelper = CalendarApp.getTable(getActivity(), MainTable.class);
        TableHelper<VirathaDay> virathaDayTableHelper = CalendarApp.getTable(getActivity(), VirathaDay.class);
        TableHelper<Kalangal> kalangalTableHelper = CalendarApp.getTable(getActivity(), Kalangal.class);
        TableHelper<Vasthu> vasthuTableHelper = CalendarApp.getTable(getActivity(), Vasthu.class);
        TableHelper<Panchangam> panchangamTableHelper = CalendarApp.getTable(getActivity(), Panchangam.class);
        TableHelper<Krakakalam> krakakalamTableHelper = CalendarApp.getTable(getActivity(), Krakakalam.class);

        if (queryFlag.equals("hindu_fes") || queryFlag.equals("muslim_fes") || queryFlag.equals("chirs_fes") || queryFlag.equals("gov_holiday")) {

            List<MainTable> mainTables = new ArrayList<>();
            try {
                mainTables = mainTableTableHelper.getDistinctList(mainTableTableHelper.getReadableDatabase(),
                        new String[]{"month"}, "" + queryFlag + "!=? and " + queryFlag + "!=? and year=?",
                        new String[]{"-", "", String.valueOf(curYear)}, null, null, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }

//            RealmResults<MainTable> mainTables = realm.where(MainTable.class)
//                    .distinct("month")
//                    .notEqualTo(queryFlag, "-")
//                    .and()
//                    .isNotEmpty(queryFlag)
//                    .and()
//                    .equalTo("year", curYear)
//                    .findAll();

            for (MainTable mainTable : mainTables) {

                List<MainTable> data = new ArrayList<>();
                try {
                    data = mainTableTableHelper.getList("" + queryFlag + "!=? and month =? and year=?",
                            new String[]{"-", String.valueOf(mainTable.getMonth()), String.valueOf(curYear)}, "day ASC", null);
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                RealmResults<MainTable> data = realm.where(MainTable.class)
//                        .notEqualTo(queryFlag, "-")
//                        .and()
//                        .equalTo("month", mainTable.getMonth())
//                        .and()
//                        .equalTo("year", curYear)
//                        .sort("day", Sort.ASCENDING)
//                        .findAll();

                monthsList.add(mainTable.getMonth());
                listMap.put(mainTable.getMonth(), data);
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(new FestivalAdapter(getActivity(), queryFlag, monthsList, listMap));

        } else if (queryFlag.equals("muhurtha_days")) {

            muhurtham.setVisibility(View.VISIBLE);

//            RealmResults<MainTable> mainTable = realm.where(MainTable.class)
//                    .distinct("month")
//                    .and()
//                    .equalTo("year", curYear)
//                    .findAll();

            List<MainTable> mainTable = new ArrayList<>();
            try {
                mainTable = mainTableTableHelper.getDistinctList(mainTableTableHelper.getReadableDatabase(),
                        new String[]{"month"}, "year=?",
                        new String[]{String.valueOf(curYear)}, null, null, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (int i = 0; i < 12; i++) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.MONTH, i);
                calendar.set(Calendar.YEAR, curYear);

                String fromDate = DateTimeHelper.simpleDateFormat1.format(DateTimeHelper.getCalendarViewFromDate(calendar));
                String toDate = DateTimeHelper.simpleDateFormat1.format(DateTimeHelper.getToDate(calendar));

//                RealmResults<VirathaDay> virathaDays = realm.where(VirathaDay.class)
//                        .greaterThanOrEqualTo(Constants.date, fromDate)
//                        .and()
//                        .lessThanOrEqualTo(Constants.date, toDate)
//                        .and()
//                        .equalTo("viratham", "21")
//                        .sort(Constants.date, Sort.ASCENDING)
//                        .findAll();

                List<VirathaDay> virathaDays = new ArrayList<>();
                try {
                    virathaDays = virathaDayTableHelper.getList("date>=? and date<=? and viratham=?",
                            new String[]{fromDate, toDate, "21"}, "date ASC", null);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (!monthsList.contains(mainTable.get(i).getMonth()))
                    monthsList.add(mainTable.get(i).getMonth());
                listMap.put(mainTable.get(i).getMonth(), virathaDays);
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(new MuhurthamAdapter(getActivity(), monthsList, listMap));
        } else if (queryFlag.equals("raagu")) {

            List<Kalangal> kalangals = new ArrayList<>();
            try {
                kalangals = kalangalTableHelper.getList("year=?",
                        new String[]{String.valueOf(curYear)}, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }

//            RealmResults<Kalangal> kalangals = realm.where(Kalangal.class)
//                    .equalTo("year", curYear)
//                    .findAll();

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(new RaaguAdapter(getActivity(), kalangals));
        } else if (queryFlag.equals("vasthu_days")) {

            List<Vasthu> vasthus = new ArrayList<>();
            try {
                vasthus = vasthuTableHelper.getList("year=?",
                        new String[]{String.valueOf(curYear)}, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }

//            RealmResults<Vasthu> vasthus = realm.where(Vasthu.class)
//                    .equalTo("year", curYear)
//                    .findAll();

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(new VasthuAdapter(getActivity(), vasthus));
        } else if (queryFlag.equals("gowri_panchanagam")) {

            panchangham.setVisibility(View.VISIBLE);

            List<Panchangam> panchangams = new ArrayList<>();
            try {
                panchangams = panchangamTableHelper.getDistinctList(panchangamTableHelper.getReadableDatabase(),
                        new String[]{"weekday"}, "year=?",
                        new String[]{String.valueOf(curYear)}, null, null, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }

//            RealmResults<Panchangam> panchangams = realm.where(Panchangam.class)
//                    .distinct("weekday")
//                    .equalTo("year", curYear)
//                    .findAll();

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(new PanchangamAdapter(getActivity(), curYear, panchangams));
        } else if (queryFlag.equals("viradha_days")) {

            List<MainTable> mainTable = new ArrayList<>();
            try {
                mainTable = mainTableTableHelper.getDistinctList(mainTableTableHelper.getReadableDatabase(),
                        new String[]{"month"}, "year=?",
                        new String[]{String.valueOf(curYear)}, null, null, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }

//            RealmResults<MainTable> mainTable = realm.where(MainTable.class)
//                    .distinct("month")
//                    .and()
//                    .equalTo("year", curYear)
//                    .findAll();

            HashMap<String, HashMap<String, List<VirathaDay>>> hashMap = new HashMap<>();
            List<String> tamMonthList = new ArrayList<>();

            for (int i = 0; i < 12; i++) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.MONTH, i);
                calendar.set(Calendar.YEAR, curYear);

                String fromDate = DateTimeHelper.simpleDateFormat1.format(DateTimeHelper.getCalendarViewFromDate(calendar));
                String toDate = DateTimeHelper.simpleDateFormat1.format(DateTimeHelper.getToDate(calendar));

                HashMap<String, List<VirathaDay>> subHashMap = new HashMap<>();

                for (String str : viratham) {

                    List<VirathaDay> virathaDays = new ArrayList<>();
                    try {
                        virathaDays = virathaDayTableHelper.getList("date >= ? and date <= ? and viratham=?",
                                new String[]{fromDate, toDate, str}, null, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

//                    RealmResults<VirathaDay> virathaDays = realm.where(VirathaDay.class)
//                            .greaterThanOrEqualTo(Constants.date, fromDate)
//                            .and()
//                            .lessThanOrEqualTo(Constants.date, toDate)
//                            .and()
//                            .equalTo("viratham", str)
//                            .findAll();

                    Log.e("Data",mainTable.get(i).getMonth() + "-" + str+"---"+virathaDays);
                    subHashMap.put(mainTable.get(i).getMonth() + "-" + str, virathaDays);
                }

                hashMap.put(mainTable.get(i).getMonth(), subHashMap);
                tamMonthList.add(mainTable.get(i).getMonth());
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(new VirathamAdapter(getActivity(), tamMonthList, hashMap));

        } else if (queryFlag.equals("suba_horai")) {

            List<Krakakalam> krakakalams = new ArrayList<>();
            try {
                krakakalams = krakakalamTableHelper.getDistinctList(krakakalamTableHelper.getReadableDatabase(),
                        new String[]{"weekday"}, "year=?",
                        new String[]{String.valueOf(curYear)}, null, null, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }

//            RealmResults<Krakakalam> krakakalams = realm.where(Krakakalam.class)
//                    .distinct("weekday")
//                    .equalTo("year", curYear)
//                    .findAll();

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(new SubaHoraiAdapter(getActivity(), curYear, krakakalams));
        } else if (queryFlag.equals("அஷ்டமி") || queryFlag.equals("நவமி") || queryFlag.equals("கரிநாள்")) {

            List<MainTable> mainTable = new ArrayList<>();
            try {
                mainTable = mainTableTableHelper.getDistinctList(mainTableTableHelper.getReadableDatabase(),
                        new String[]{"month"}, "year=?",
                        new String[]{String.valueOf(curYear)}, null, null, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }

//            RealmResults<MainTable> mainTable = realm.where(MainTable.class)
//                    .distinct("month")
//                    .and()
//                    .equalTo("year", curYear)
//                    .findAll();

            for (int i = 0; i < 12; i++) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.MONTH, i);
                calendar.set(Calendar.YEAR, curYear);

                String fromDate = DateTimeHelper.simpleDateFormat1.format(DateTimeHelper.getCalendarViewFromDate(calendar));
                String toDate = DateTimeHelper.simpleDateFormat1.format(DateTimeHelper.getToDate(calendar));

                List<VirathaDay> virathaDays = new ArrayList<>();
                try {
                    virathaDays = virathaDayTableHelper.getList("date>=? and date<=? and viratham=?",
                            new String[]{fromDate, toDate, String.valueOf(EnumVirathaDay.virathaDay(queryFlag).getDay())},
                            "date ASC", null);
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                RealmResults<VirathaDay> virathaDays = realm.where(VirathaDay.class)
//                        .greaterThanOrEqualTo(Constants.date, fromDate)
//                        .and()
//                        .lessThanOrEqualTo(Constants.date, toDate)
//                        .and()
//                        .equalTo("viratham", EnumVirathaDay.virathaDay(queryFlag).getDay())
//                        .sort(Constants.date, Sort.ASCENDING)
//                        .findAll();

                if (!monthsList.contains(mainTable.get(i).getMonth()))
                    monthsList.add(mainTable.get(i).getMonth());
                listMap.put(mainTable.get(i).getMonth(), virathaDays);
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(new AsubaNaatkalAdapter(getActivity(), queryFlag, monthsList, listMap));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
