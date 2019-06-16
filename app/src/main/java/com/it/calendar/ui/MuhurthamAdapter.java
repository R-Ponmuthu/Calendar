package com.it.calendar.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.it.calendar.R;
import com.it.calendar.model.MainTable;
import com.it.calendar.model.MoogurthamTable;
import com.it.calendar.model.Virathaday;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class MuhurthamAdapter extends RecyclerView.Adapter<MuhurthamAdapter.ItemViewHolder> {

    List<String> months;
    Map<String, List<?>> mainTbl;
    Map<String, List<Virathaday>> viratham = new HashMap<>();
    List<Virathaday> virathaDayList = new ArrayList<>();
    private Activity activity;
    private Realm realm;

    public MuhurthamAdapter(Activity activity, List<String> months, Map<String, List<?>> mainTbl) {
        this.activity = activity;
        this.months = months;
        this.mainTbl = mainTbl;

        Realm.init(activity);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new ItemViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

        holder.bind_muhurtham(months.get(position));
    }

    @Override
    public int getItemCount() {
        return mainTbl.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.container)
        RecyclerView container;

        ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @SuppressLint("SetTextI18n")
        void bind_muhurtham(String s) {

            //container.removeAllViews();
            title.setText(s);

            viratham = new HashMap<>();
            virathaDayList = new ArrayList<>();

            for (int i = 0; i < mainTbl.get(s).size(); i++) {

                Virathaday virathaDay = (Virathaday) mainTbl.get(s).get(i);
                virathaDayList.add(virathaDay);
            }

            viratham.put(s, virathaDayList);
            container.setLayoutManager(new GridLayoutManager(activity, 6));
            container.setAdapter(new SubItemAdapter(activity, s, viratham));
        }
    }

    public class SubItemAdapter extends RecyclerView.Adapter<SubItemAdapter.ItemViewHolder> {

        Map<String, List<Virathaday>> viratham;
        String month;
        private Activity activity;
        private Realm realm;

        SubItemAdapter(Activity activity, String month, Map<String, List<Virathaday>> viratham) {
            this.activity = activity;
            this.viratham = viratham;
            this.month = month;

            Realm.init(activity);
            realm = Realm.getDefaultInstance();
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.muhurtham_sub_item, parent, false);
            return new ItemViewHolder(itemView);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {

            holder.bind_item(viratham.get(month).get(position));
        }

        @Override
        public int getItemCount() {
            return viratham.get(month).size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.day)
            TextView day;
            @BindView(R.id.date)
            TextView date;
            @BindView(R.id.containerLay)
            LinearLayout conatinerLay;

            ItemViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            @SuppressLint("SetTextI18n")
            void bind_item(Virathaday virathaDay) {

                date.setText(virathaDay.getDate().split("/")[0]);

                MoogurthamTable moogurthamTable = realm.where(MoogurthamTable.class)
                        .equalTo("date", virathaDay.getDate())
                        .findFirst();

                if (moogurthamTable.getValrpirai() == 1)
                    conatinerLay.setBackgroundColor(activity.getResources().getColor(R.color.yellow_light));


                MainTable mainTable = realm.where(MainTable.class)
                        .equalTo("date", virathaDay.getDate())
                        .findFirst();
                day.setText("" + mainTable.getWeekday().substring(0, 2));

                conatinerLay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        MuhurthamBottomSheet.newInstance(virathaDay.getDate()).show(((DrawerActivity) activity).getSupportFragmentManager(), "Muhurtham");
                    }
                });
            }
        }
    }
}
