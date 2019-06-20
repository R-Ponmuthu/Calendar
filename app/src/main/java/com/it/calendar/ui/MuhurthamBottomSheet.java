package com.it.calendar.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.it.calendar.R;
import com.it.calendar.model.moogurtham_table;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class MuhurthamBottomSheet extends BottomSheetDialogFragment {

    @BindView(R.id.thithi)
    TextView thithi;
    @BindView(R.id.star)
    TextView star;
    @BindView(R.id.yokam)
    TextView yokam;
    @BindView(R.id.neram)
    TextView neram;
    @BindView(R.id.laknam)
    TextView laknam;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.close)
    ImageView close;
    private String date;
    private Realm realm;

    public MuhurthamBottomSheet() {

    }

    public static MuhurthamBottomSheet newInstance(String date) {

        MuhurthamBottomSheet muhurthamBottomSheet = new MuhurthamBottomSheet();
        Bundle bundle = new Bundle();
        bundle.putString("Date", date);
        muhurthamBottomSheet.setArguments(bundle);

        return muhurthamBottomSheet;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            date = getArguments().getString("Date");

        Realm.init(getActivity());
        realm = Realm.getDefaultInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.muhurtham_bottom_sheet, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        close.setOnClickListener(view1 -> dismiss());
        title.setText("" + date);

        moogurtham_table moogurthamTable = realm.where(moogurtham_table.class)
                .equalTo("date", date)
                .findFirst();

        thithi.setText("திதி: " + moogurthamTable.getThethi());
        star.setText("நட்சத்திரம்: " + moogurthamTable.getStar());
        yokam.setText("யோகம்:" + moogurthamTable.getYokam());
        neram.setText("நேரம்:" + moogurthamTable.getTime());
        laknam.setText("லக்னம் : " + moogurthamTable.getLakknam());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
