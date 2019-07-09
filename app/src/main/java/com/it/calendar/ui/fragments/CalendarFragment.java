package com.it.calendar.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.it.calendar.R;
import com.it.calendar.model.MainTable;
import com.it.calendar.model.VirathaDay;
import com.it.calendar.model.kalangal;
import com.it.calendar.model.krakakalam;
import com.it.calendar.model.panchangam;
import com.it.calendar.model.vasthu;
import com.it.calendar.util.SharedPreference;
import com.it.calendar.ui.AsubaNaatkalAdapter;
import com.it.calendar.ui.FestivalAdapter;
import com.it.calendar.ui.MuhurthamAdapter;
import com.it.calendar.ui.PanchangamAdapter;
import com.it.calendar.ui.RaaguAdapter;
import com.it.calendar.ui.SubaHoraiAdapter;
import com.it.calendar.ui.VasthuAdapter;
import com.it.calendar.ui.VirathamAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;


public class CalendarFragment extends Fragment {

    @BindView(R.id.muhurtham)
    TextView muhurtham;
    @BindView(R.id.panchangam)
    TextView panchangham;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
    private String queryFlag;
    @BindView(R.id.year)
    AppCompatAutoCompleteTextView year;
    private int curYear;
    private Realm realm;
    private HashMap<String, List<?>> listMap = new HashMap<>();
    private List<String> monthsList = new ArrayList<>();
    private SharedPreference sharedPreference = new SharedPreference();

    private Integer[] years = {2018, 2019, 2020};

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

        Realm.init(getActivity());
        realm = Realm.getDefaultInstance();
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

        if (queryFlag.equals("hindu_fes") || queryFlag.equals("muslim_fes") || queryFlag.equals("chirs_fes") || queryFlag.equals("gov_holiday")) {

            RealmResults<MainTable> mainTables = realm.where(MainTable.class)
                    .distinct("month")
                    .notEqualTo(queryFlag, "-")
                    .and()
                    .isNotEmpty(queryFlag)
                    .and()
                    .equalTo("year", curYear)
                    .findAll();

            for (MainTable mainTable : mainTables) {

                RealmResults<MainTable> data = realm.where(MainTable.class)
                        .notEqualTo(queryFlag, "-")
                        .and()
                        .equalTo("month", mainTable.getMonth())
                        .and()
                        .equalTo("year", curYear)
                        .sort("day", Sort.ASCENDING)
                        .findAll();

                monthsList.add(mainTable.getMonth());
                listMap.put(mainTable.getMonth(), data);
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(new FestivalAdapter(getActivity(), queryFlag, monthsList, listMap));

        } else if (queryFlag.equals("muhurtha_days")) {

            muhurtham.setVisibility(View.VISIBLE);

            RealmResults<MainTable> mainTable = realm.where(MainTable.class)
                    .distinct("month")
                    .and()
                    .equalTo("year", curYear)
                    .findAll();

            for (int i = 0; i < 12; i++) {

                String str = "/" + (i + 1) + "/" + curYear;

                RealmResults<VirathaDay> virathaDays = realm.where(VirathaDay.class)
                        .contains("date", str)
                        .and()
                        .equalTo("viratham", "சுபமுகூர்த்தம்")
                        .sort("date", Sort.ASCENDING)
                        .findAll();

                if (!monthsList.contains(mainTable.get(i).getMonth()))
                    monthsList.add(mainTable.get(i).getMonth());
                listMap.put(mainTable.get(i).getMonth(), virathaDays);
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(new MuhurthamAdapter(getActivity(), monthsList, listMap));
        } else if (queryFlag.equals("raagu")) {

            RealmResults<kalangal> kalangals = realm.where(kalangal.class)
                    .equalTo("year", curYear)
                    .findAll();

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(new RaaguAdapter(getActivity(), kalangals));
        } else if (queryFlag.equals("vasthu_days")) {

            RealmResults<vasthu> vasthus = realm.where(vasthu.class)
                    .equalTo("year", curYear)
                    .findAll();

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(new VasthuAdapter(getActivity(), vasthus));
        } else if (queryFlag.equals("gowri_panchanagam")) {

            panchangham.setVisibility(View.VISIBLE);

            RealmResults<panchangam> panchangams = realm.where(panchangam.class)
                    .distinct("weekday")
                    .equalTo("year", curYear)
                    .findAll();

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(new PanchangamAdapter(getActivity(), curYear, panchangams));
        } else if (queryFlag.equals("viradha_days")) {

            RealmResults<MainTable> mainTable = realm.where(MainTable.class)
                    .distinct("month")
                    .and()
                    .equalTo("year", curYear)
                    .findAll();

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(new VirathamAdapter(getActivity(), mainTable, curYear));
        } else if (queryFlag.equals("suba_horai")) {

            RealmResults<krakakalam> krakakalams = realm.where(krakakalam.class)
                    .distinct("weekday")
                    .equalTo("year", curYear)
                    .findAll();

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(new SubaHoraiAdapter(getActivity(), curYear, krakakalams));
        } else if (queryFlag.equals("அஷ்டமி") || queryFlag.equals("நவமி") || queryFlag.equals("கரிநாள்")) {

            RealmResults<MainTable> mainTable = realm.where(MainTable.class)
                    .distinct("month")
                    .and()
                    .equalTo("year", curYear)
                    .findAll();

            for (int i = 0; i < 12; i++) {

                String str = "/" + (i + 1) + "/" + curYear;

                RealmResults<VirathaDay> virathaDays = realm.where(VirathaDay.class)
                        .contains("date", str)
                        .and()
                        .equalTo("viratham", queryFlag)
                        .sort("date", Sort.ASCENDING)
                        .findAll();

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
