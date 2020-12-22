package com.it.calendar.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.vipulasri.timelineview.TimelineView;
import com.it.calendar.R;
import com.it.calendar.beans.MainTable;
import com.it.calendar.beans.MuhurthamTable;
import com.it.calendar.beans.VirathaDay;
import com.it.calendar.util.Constants;
import com.it.calendar.util.DateTimeHelper;
import com.it.calendar.util.EnumMonth;
import com.it.calendar.util.EnumWeekDay;

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
    Map<String, List<VirathaDay>> viratham = new HashMap<>();
    List<VirathaDay> virathaDayList = new ArrayList<>();
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
        return new ItemViewHolder(itemView,viewType);
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
        @BindView(R.id.timeline)
        TimelineView timelineView;

        ItemViewHolder(View view, int viewType) {
            super(view);
            ButterKnife.bind(this, view);

            timelineView.initLine(viewType);
        }

        @SuppressLint("SetTextI18n")
        void bind_muhurtham(String s) {

            //container.removeAllViews();
            title.setText(EnumMonth.getMonthStr(s).getText());

            viratham = new HashMap<>();
            virathaDayList = new ArrayList<>();

            for (int i = 0; i < mainTbl.get(s).size(); i++) {

                VirathaDay virathaDay = (VirathaDay) mainTbl.get(s).get(i);
                virathaDayList.add(virathaDay);
            }

            viratham.put(s, virathaDayList);
            container.setLayoutManager(new GridLayoutManager(activity, 6));
            container.setAdapter(new SubItemAdapter(activity, s, viratham));
        }
    }

    public class SubItemAdapter extends RecyclerView.Adapter<SubItemAdapter.ItemViewHolder> {

        Map<String, List<VirathaDay>> viratham;
        String month;
        private Activity activity;
        private Realm realm;

        SubItemAdapter(Activity activity, String month, Map<String, List<VirathaDay>> viratham) {
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
            void bind_item(VirathaDay virathaDay) {

                date.setText(DateTimeHelper.getDateFromMillis(virathaDay.getDate()).split("-")[0]);

                MuhurthamTable moogurthamTable = realm.where(MuhurthamTable.class)
                        .equalTo(Constants.date, virathaDay.getDate())
                        .findFirst();

                if (moogurthamTable != null)
                    if (moogurthamTable.getValrpirai() == 1)
                        conatinerLay.setBackgroundColor(activity.getResources().getColor(R.color.yellow_light));


                MainTable mainTable = realm.where(MainTable.class)
                        .equalTo(Constants.date, virathaDay.getDate())
                        .findFirst();
                day.setText("" + EnumWeekDay.getWeekDay(mainTable.getWeekday()).getText().substring(0, 2));

                conatinerLay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //MuhurthamBottomSheet.newInstance(virathaDay.getDate()).show(((DrawerActivity) activity).getSupportFragmentManager(), "Muhurtham");
                    }
                });
            }
        }
    }
}
