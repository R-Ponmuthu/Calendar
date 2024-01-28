package com.it.calendar.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.it.calendar.CalendarApp;
import com.it.calendar.R;
import com.it.calendar.beans.Panchangam;
import com.it.calendar.util.EnumPanchangam;
import com.it.core.db.TableHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PanchangamFragment extends Fragment {

    private static final String ARG_PARAM = "param";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam, mParam1;
    private int mParam2;
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
    @BindView(R.id.horizontalView)
    LinearLayout horizontalSV;

    public PanchangamFragment() {
        // Required empty public constructor
    }

    public static PanchangamFragment newInstance(String param, String param1, int param2) {
        PanchangamFragment fragment = new PanchangamFragment();
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

        View view = inflater.inflate(R.layout.fragment_panchangam, container, false);
        ButterKnife.bind(this, view);

        TableHelper<Panchangam> panchangamTableHelper = CalendarApp.getTable(getActivity(), Panchangam.class);

        for (int i = 1; i <= 7; i++) {

            List<Panchangam> panchangamList = new ArrayList<>();
            try {
                panchangamList = panchangamTableHelper.rawQuery(panchangamTableHelper.getReadableDatabase(),
                        "select * from Panchangam where weekday=" + i + " and year='2024'", null);
            } catch (Exception e) {
                e.printStackTrace();
            }

            View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.panchangam_item_view, null, false);
            ((TextView) view1.findViewById(R.id.txt1)).setText(EnumPanchangam.getPanchangamStr(panchangamList.get(0).getParikaram()).getText());
            ((TextView) view1.findViewById(R.id.txt2)).setText(EnumPanchangam.getPanchangamStr(panchangamList.get(1).getParikaram()).getText());
            ((TextView) view1.findViewById(R.id.txt3)).setText(EnumPanchangam.getPanchangamStr(panchangamList.get(2).getParikaram()).getText());
            ((TextView) view1.findViewById(R.id.txt4)).setText(EnumPanchangam.getPanchangamStr(panchangamList.get(3).getParikaram()).getText());
            ((TextView) view1.findViewById(R.id.txt5)).setText(EnumPanchangam.getPanchangamStr(panchangamList.get(4).getParikaram()).getText());
            ((TextView) view1.findViewById(R.id.txt6)).setText(EnumPanchangam.getPanchangamStr(panchangamList.get(5).getParikaram()).getText());
            ((TextView) view1.findViewById(R.id.txt7)).setText(EnumPanchangam.getPanchangamStr(panchangamList.get(6).getParikaram()).getText());
            ((TextView) view1.findViewById(R.id.txt8)).setText(EnumPanchangam.getPanchangamStr(panchangamList.get(7).getParikaram()).getText());
            ((TextView) view1.findViewById(R.id.txt9)).setText(EnumPanchangam.getPanchangamStr(panchangamList.get(8).getParikaram()).getText());
            ((TextView) view1.findViewById(R.id.txt10)).setText(EnumPanchangam.getPanchangamStr(panchangamList.get(9).getParikaram()).getText());
            ((TextView) view1.findViewById(R.id.txt11)).setText(EnumPanchangam.getPanchangamStr(panchangamList.get(10).getParikaram()).getText());
            ((TextView) view1.findViewById(R.id.txt12)).setText(EnumPanchangam.getPanchangamStr(panchangamList.get(11).getParikaram()).getText());
            ((TextView) view1.findViewById(R.id.txt13)).setText(EnumPanchangam.getPanchangamStr(panchangamList.get(12).getParikaram()).getText());
            ((TextView) view1.findViewById(R.id.txt14)).setText(EnumPanchangam.getPanchangamStr(panchangamList.get(13).getParikaram()).getText());
            ((TextView) view1.findViewById(R.id.txt15)).setText(EnumPanchangam.getPanchangamStr(panchangamList.get(14).getParikaram()).getText());
            ((TextView) view1.findViewById(R.id.txt16)).setText(EnumPanchangam.getPanchangamStr(panchangamList.get(15).getParikaram()).getText());
            horizontalSV.addView(view1);
        }



//        txt1.setText(EnumPanchangam.getPanchangamStr(panchangamList.get(0).getParikaram()).getText());
//        txt1.setTextColor(getResources().getColor(EnumPanchangam.getPanchangamStr(panchangamList.get(0).getParikaram()).getColor()));
//        txt2.setText(EnumPanchangam.getPanchangamStr(panchangamList.get(1).getParikaram()).getText());
//        txt2.setTextColor(getResources().getColor(EnumPanchangam.getPanchangamStr(panchangamList.get(1).getParikaram()).getColor()));
//        txt3.setText(EnumPanchangam.getPanchangamStr(panchangamList.get(2).getParikaram()).getText());
//        txt3.setTextColor(getResources().getColor(EnumPanchangam.getPanchangamStr(panchangamList.get(2).getParikaram()).getColor()));
//        txt4.setText(EnumPanchangam.getPanchangamStr(panchangamList.get(3).getParikaram()).getText());
//        txt4.setTextColor(getResources().getColor(EnumPanchangam.getPanchangamStr(panchangamList.get(3).getParikaram()).getColor()));
//        txt5.setText(EnumPanchangam.getPanchangamStr(panchangamList.get(4).getParikaram()).getText());
//        txt5.setTextColor(getResources().getColor(EnumPanchangam.getPanchangamStr(panchangamList.get(4).getParikaram()).getColor()));
//        txt6.setText(EnumPanchangam.getPanchangamStr(panchangamList.get(5).getParikaram()).getText());
//        txt6.setTextColor(getResources().getColor(EnumPanchangam.getPanchangamStr(panchangamList.get(5).getParikaram()).getColor()));
//        txt7.setText(EnumPanchangam.getPanchangamStr(panchangamList.get(6).getParikaram()).getText());
//        txt7.setTextColor(getResources().getColor(EnumPanchangam.getPanchangamStr(panchangamList.get(6).getParikaram()).getColor()));
//        txt8.setText(EnumPanchangam.getPanchangamStr(panchangamList.get(7).getParikaram()).getText());
//        txt8.setTextColor(getResources().getColor(EnumPanchangam.getPanchangamStr(panchangamList.get(7).getParikaram()).getColor()));
//        txt9.setText(EnumPanchangam.getPanchangamStr(panchangamList.get(8).getParikaram()).getText());
//        txt9.setTextColor(getResources().getColor(EnumPanchangam.getPanchangamStr(panchangamList.get(8).getParikaram()).getColor()));
//        txt10.setText(EnumPanchangam.getPanchangamStr(panchangamList.get(9).getParikaram()).getText());
//        txt10.setTextColor(getResources().getColor(EnumPanchangam.getPanchangamStr(panchangamList.get(9).getParikaram()).getColor()));
//        txt11.setText(EnumPanchangam.getPanchangamStr(panchangamList.get(10).getParikaram()).getText());
//        txt11.setTextColor(getResources().getColor(EnumPanchangam.getPanchangamStr(panchangamList.get(10).getParikaram()).getColor()));
//        txt12.setText(EnumPanchangam.getPanchangamStr(panchangamList.get(11).getParikaram()).getText());
//        txt12.setTextColor(getResources().getColor(EnumPanchangam.getPanchangamStr(panchangamList.get(11).getParikaram()).getColor()));
//        txt13.setText(EnumPanchangam.getPanchangamStr(panchangamList.get(12).getParikaram()).getText());
//        txt13.setTextColor(getResources().getColor(EnumPanchangam.getPanchangamStr(panchangamList.get(12).getParikaram()).getColor()));
//        txt14.setText(EnumPanchangam.getPanchangamStr(panchangamList.get(13).getParikaram()).getText());
//        txt14.setTextColor(getResources().getColor(EnumPanchangam.getPanchangamStr(panchangamList.get(13).getParikaram()).getColor()));
//        txt15.setText(EnumPanchangam.getPanchangamStr(panchangamList.get(14).getParikaram()).getText());
//        txt15.setTextColor(getResources().getColor(EnumPanchangam.getPanchangamStr(panchangamList.get(14).getParikaram()).getColor()));
//        txt16.setText(EnumPanchangam.getPanchangamStr(panchangamList.get(15).getParikaram()).getText());
//        txt16.setTextColor(getResources().getColor(EnumPanchangam.getPanchangamStr(panchangamList.get(15).getParikaram()).getColor()));

        return view;
    }
}