package com.it.calendar.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.it.calendar.CalendarApp;
import com.it.calendar.R;
import com.it.calendar.beans.Kalangal;
import com.it.calendar.beans.VirathaDay;
import com.it.calendar.ui.adapter.MuhurthamAdapter;
import com.it.calendar.ui.adapter.RaaguAdapter;
import com.it.core.db.TableHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RaaguFragment extends Fragment {

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
    @BindView(R.id.valarpiraiM)
    TextView valarpiraiM;

    public RaaguFragment() {
        // Required empty public constructor
    }

    public static RaaguFragment newInstance(String param, String param1, int param2) {
        RaaguFragment fragment = new RaaguFragment();
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

        TableHelper<Kalangal> kalangalTableHelper = CalendarApp.getTable(getActivity(), Kalangal.class);


        List<Kalangal> kalangals = new ArrayList<>();
        try {
            kalangals = kalangalTableHelper.rawQuery(kalangalTableHelper.getReadableDatabase(), "select * from Kalangal where weekday=" + mParam1 + " and year='2024'", null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new RaaguAdapter(getActivity(), kalangals));

        return view;
    }
}