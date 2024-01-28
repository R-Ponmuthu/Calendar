package com.it.calendar.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.it.calendar.CalendarApp;
import com.it.calendar.R;
import com.it.calendar.beans.Kalangal;
import com.it.calendar.beans.Krakakalam;
import com.it.calendar.beans.MainTable;
import com.it.calendar.beans.Panchangam;
import com.it.calendar.beans.Vasthu;
import com.it.calendar.beans.VirathaDay;
import com.it.calendar.ui.adapter.FestivalAdapter;
import com.it.calendar.util.EnumMonth;
import com.it.core.db.TableHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FestivalsFragment extends Fragment {

    private static final String ARG_PARAM = "param";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam, mParam1;
    private int mParam2;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ll_noData)
    LinearLayout llNoData;
    @BindView(R.id.txtMonth)
    TextView txtMonth;

    public FestivalsFragment() {
        // Required empty public constructor
    }

    public static FestivalsFragment newInstance(String param, String param1, int param2) {
        FestivalsFragment fragment = new FestivalsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param);
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = getArguments().getString(ARG_PARAM);
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_festivals, container, false);
        ButterKnife.bind(this, view);

        TableHelper<MainTable> mainTableTableHelper = CalendarApp.getTable(getActivity(), MainTable.class);

        if (mParam.equals("hindu_fes") || mParam.equals("muslim_fes") || mParam.equals("chirs_fes") || mParam.equals("gov_holiday")) {

            List<MainTable> mainTables = new ArrayList<>();
            try {
                mainTables = mainTableTableHelper.rawQuery(mainTableTableHelper.getReadableDatabase(),
                        "SELECT * FROM MainTable where date like '%" + mParam1 + "-2024%' and " + mParam + "!='-'", null);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (mainTables.size() == 0) {
                llNoData.setVisibility(View.VISIBLE);
                txtMonth.setText(EnumMonth.getMonthStr(String.valueOf(mParam2).substring(1)).getText() + " மாதத்தில் பண்டிகைகள் இல்லை");
            } else
                llNoData.setVisibility(View.GONE);

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(new FestivalAdapter(getActivity(), mParam, mainTables));
        }

        return view;
    }
}