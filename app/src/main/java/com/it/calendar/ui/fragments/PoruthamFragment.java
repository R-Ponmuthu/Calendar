package com.it.calendar.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import com.it.calendar.R;
import com.it.calendar.beans.ThirumanaPorutham;
import com.it.calendar.realm.RealmController;
import com.it.calendar.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;


public class PoruthamFragment extends Fragment {

    @BindView(R.id.female)
    AppCompatAutoCompleteTextView female;
    @BindView(R.id.male)
    AppCompatAutoCompleteTextView male;
    @BindView(R.id.expandableListView)
    ExpandableListView expandableListView;
    @BindView(R.id.mark)
    TextView mark;
    @BindView(R.id.porundhumNatchathiram)
    TextView porundhumNatchathiram;
    @BindView(R.id.porutham)
    TextView poruthamTxt;
    private String queryFlag;
    private int curYear;
    private String[] array;
    private String[] rasiNatchathiram = {"மேஷம் - அசுவினி", "மேஷம் - பரணி", "மேஷம் - கிருத்திகை 1-ஆம் பாதம்",
            "ரிஷபம் - கிருத்திகை 2,3,4ஆம் பாதம்", "ரிஷபம் - ரோகிணி", "ரிஷபம் - மிருகசிரீஷம் 1,2-ஆம் பாதம்,",
            "மிதுனம் - மிருகசிரீஷம் 3,4ஆம் பாதம்", "மிதுனம் - திருவாதிரை", "மிதுனம் - புனர்பூசம் 1,2,3-ஆம் பாதம்",
            "கடகம் - புனர்பூசம் 4-ஆம் பாதம்", "கடகம் - பூசம்", "கடகம் - ஆயில்யம் ",
            "சிம்மம் - மகம்", "சிம்மம் - பூரம்", "சிம்மம் - உத்திரம் 1-ஆம் பாதம்,",
            "கன்னி - உத்திரம் 2,3,4ஆம் பாதம்", "கன்னி - அஸ்தம்", "கன்னி - சித்திரை 1,2-ஆம் பாதம்",
            "துலாம் - சித்திரை 3,4ஆம் பாதம்", "துலாம் - சுவாதி", "துலாம் - விசாகம் 3-ஆம் பாதம்",
            "விருச்சிகம் - விசாகம் 4-ஆம் பாதம்", "விருச்சிகம் - அனுஷம்", "விருச்சிகம் - கேட்டை",
            "தனுசு - முலம்", "தனுசு - பூராடம்", "தனுசு - உத்திராடம் 1-ஆம் பாதம்",
            "மகரம் - உத்திராடம் 2,3,4ஆம் பாதம்", "மகரம் - திருவோணம்", "மகரம் - அவிட்டம் 1,2-ஆம் பாதம்",
            "கும்பம் - அவிட்டம் 3,4ஆம் பாதம்", "கும்பம் - சதயம்", "கும்பம் - பூரட்டாதி 1,2,3-ஆம் பாதம்",
            "மீனம் - பூரட்டாதி 4-ஆம் பாதம்", "மீனம் - உத்திரட்டாதி", "மீனம் - ரேவதி"};
    private int malePosition = 0, femalePosition = 0;
    private Realm realm;

    public PoruthamFragment() {
        // Required empty public constructor
    }


    public static PoruthamFragment newInstance(String queryFlag, int curYear) {
        PoruthamFragment fragment = new PoruthamFragment();
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
            curYear = getArguments().getInt("queryFlag");
            queryFlag = getArguments().getString("curYear");
        }

        realm = RealmController.with(getActivity()).getRealm();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_porutham, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, rasiNatchathiram);
        male.setAdapter(adapter);
        female.setAdapter(adapter);

        male.setOnClickListener(v -> male.showDropDown());
        female.setOnClickListener(v -> female.showDropDown());

        male.setOnItemClickListener((parent, view1, position, id) -> {

            malePosition = position + 1;

            if (femalePosition != 0) {
                ThirumanaPorutham porutham = realm.where(ThirumanaPorutham.class)
                        .equalTo("nid", Double.parseDouble(femalePosition + "." + malePosition))
                        .findFirst();

                if (porutham != null) {
                    mark.setText("பொருத்தம்: " + porutham.getValue() + "/12");
                    array = (new String(porutham.getTitle().toString().toCharArray())).split("(?<=.)");
                    StringBuilder stringBuilder = new StringBuilder();
                    int size = 12 - array.length;
                    for (int i = 0; i < size; i++) {
                        stringBuilder.append(0);
                    }
                    String str = stringBuilder.toString() + porutham.getTitle().toString();
                    array = (new String(str.toCharArray())).split("(?<=.)");
                    if (porutham.getMark() == 0)
                        poruthamTxt.setText("பொருத்தமில்லை");
                    else if (porutham.getMark() == 1)
                        poruthamTxt.setText("மத்திமம்");
                    else if (porutham.getMark() == 2)
                        poruthamTxt.setText("உத்தமம்");
                    else if (porutham.getMark() == 3)
                        poruthamTxt.setText("உன்னதம் ");
                    else if (porutham.getMark() == 4)
                        poruthamTxt.setText("அதி உன்னதம்");

                    expandableListView.setAdapter(new PoruthamExpandableAdapter(getActivity(), array, new Utils().poruthamList(), new Utils().childData()));
                }
            }
        });

        female.setOnItemClickListener((parent, view12, position, id) -> {

            femalePosition = position + 1;

            if (malePosition != 0) {

                ThirumanaPorutham porutham = realm.where(ThirumanaPorutham.class)
                        .equalTo("nid", Double.parseDouble(femalePosition + "." + malePosition))
                        .findFirst();

                if (porutham != null) {
                    mark.setText("பொருத்தம்: " + porutham.getValue() + "/12");
                    array = (new String(porutham.getTitle().toString().toCharArray())).split("(?<=.)");
                    StringBuilder stringBuilder = new StringBuilder();
                    int size = 12 - array.length;
                    for (int i = 0; i < size; i++) {
                        stringBuilder.append(0);
                    }
                    String str = stringBuilder.toString() + porutham.getTitle().toString();
                    array = (new String(str.toCharArray())).split("(?<=.)");
                    if (porutham.getMark() == 0)
                        poruthamTxt.setText("பொருத்தமில்லை");
                    else if (porutham.getMark() == 1)
                        poruthamTxt.setText("மத்திமம்");
                    else if (porutham.getMark() == 2)
                        poruthamTxt.setText("உத்தமம்");
                    else if (porutham.getMark() == 3)
                        poruthamTxt.setText("உன்னதம் ");
                    else if (porutham.getMark() == 4)
                        poruthamTxt.setText("அதி உன்னதம்");
                    expandableListView.setAdapter(new PoruthamExpandableAdapter(getActivity(), array, new Utils().poruthamList(), new Utils().childData()));
                }
            }
        });

        porundhumNatchathiram.setOnClickListener(v -> {

            Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Light_NoTitleBar_Fullscreen);
            dialog.setContentView(R.layout.porundhum_natchathiram);

            WebView webView = dialog.findViewById(R.id.webView);
            webView.loadUrl("file:///android_asset/porutham.html");

            AppCompatImageView close = dialog.findViewById(R.id.close);
            close.setOnClickListener(v1 -> dialog.dismiss());

            dialog.show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
