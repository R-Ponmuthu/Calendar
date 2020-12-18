package com.it.calendar.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.it.calendar.R;
import com.it.calendar.beans.VirathaDay;
import com.it.calendar.realm.RealmController;
import com.it.calendar.util.DateTimeHelper;
import com.it.calendar.util.EnumTamilMonth;
import com.it.calendar.util.EnumVirathaDay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class VirathamAdapter extends RecyclerView.Adapter<VirathamAdapter.ItemViewHolder> {

    List<String> tamMonthList;
    private Context context;
    private Realm realm;
    private HashMap<String, HashMap<String, List<VirathaDay>>> hashMap;

    public VirathamAdapter(Context context, List<String> tamMonthList, HashMap<String, HashMap<String, List<VirathaDay>>> hashMap) {
        this.context = context;
        this.hashMap = hashMap;
        this.tamMonthList = tamMonthList;

        realm = RealmController.with(context).getRealm();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new ItemViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

        HashMap<String, List<VirathaDay>> values = hashMap.get(tamMonthList.get(position));
        Set<String> keys = values.keySet();
        //List<VirathaDay> virathaDays = values.get(tamMonthList.get(position) + CalendarFragment.viratham[position]);

        holder.bind_viradham(tamMonthList.get(position), keys, values);
    }

    @Override
    public int getItemCount() {
        return tamMonthList.size();
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

        void bind_viradham(String tamMonth, Set<String> keys, HashMap<String, List<VirathaDay>> values) {

            title.setText(EnumTamilMonth.getTamilMonth(tamMonth).getText());
            List<String> keyList = new ArrayList<>();
            keyList.addAll(keys);

            container.setLayoutManager(new GridLayoutManager(context, 3));
            container.setAdapter(new SubItemAdapter(context, keyList, values));
        }
    }

    public class SubItemAdapter extends RecyclerView.Adapter<SubItemAdapter.ItemViewHolder> {

        private List<String> keys;
        private HashMap<String, List<VirathaDay>> values;
        private Context context;

        SubItemAdapter(Context context, List<String> keys, HashMap<String, List<VirathaDay>> values) {
            this.context = context;
            this.values = values;
            this.keys = keys;

            Realm.init(context);
            realm = Realm.getDefaultInstance();
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.viratham_sub_item, parent, false);
            return new ItemViewHolder(itemView);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {

            List<VirathaDay> virathaDays = values.get(keys.get(position));

            holder.bind_item(virathaDays);
        }

        @Override
        public int getItemCount() {
            return keys.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.day)
            TextView txtDay;
            @BindView(R.id.viratham)
            TextView txtViratham;
            @BindView(R.id.containerLay)
            LinearLayout conatinerLay;

            ItemViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            @SuppressLint("SetTextI18n")
            void bind_item(List<VirathaDay> virathaDays) {

                if (virathaDays.size() > 0) {
                    txtViratham.setText(EnumVirathaDay.getVirathaDay(virathaDays.get(0).getViratham()).getText());

                    StringBuilder stringBuilder = new StringBuilder();
                    for (VirathaDay virathaDay : virathaDays) {
                        stringBuilder.append(DateTimeHelper.getDateFromMillis(virathaDay.getDate()).split("-")[0].trim()).append(",");
                    }
                    txtDay.setText(stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1));
                }
            }
        }
    }
}
