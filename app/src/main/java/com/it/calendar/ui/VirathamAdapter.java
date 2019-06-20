package com.it.calendar.ui;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.it.calendar.R;
import com.it.calendar.model.MainTable;
import com.it.calendar.model.VirathaDay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class VirathamAdapter extends RecyclerView.Adapter<VirathamAdapter.ItemViewHolder> {

    List<MainTable> mainTableList;
    private Context context;
    private Realm realm;
    private HashMap<String, String> hashMap = new HashMap<>();
    private List<String> flag = new ArrayList<>();
    private String[] viratham = new String[]{"அமாவாசை", "பௌர்ணமி", "கிருத்திகை", "சஷ்டி", "சங்கடஹர சதுர்த்தி", "திருவோணம்", "சிவராத்திரி", "ஏகாதசி", "பிரதோஷம்", "சதுர்த்தி"};

    public VirathamAdapter(Context context, RealmResults<MainTable> mainTableList) {
        this.context = context;
        this.mainTableList = mainTableList;

        Realm.init(context);
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

        holder.bind_viradham(mainTableList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mainTableList.size();
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

        void bind_viradham(MainTable mainTable, int position) {

            title.setText(mainTable.getMonth());
            hashMap = new HashMap<>();
            flag = new ArrayList<>();

            String str = "/" + (position + 1) + "/" + 2019;

            for (String viradham : viratham) {

                RealmResults<VirathaDay> virathaDays = realm.where(VirathaDay.class)
                        .contains("date", str)
                        .and()
                        .equalTo("viratham", viradham)
                        .findAll();

                StringBuilder stringBuilder = new StringBuilder();
                if (virathaDays.size() == 1)
                    stringBuilder.append(virathaDays.get(0).getDate().split("/")[0]);
                else
                    for (VirathaDay virathaDay : virathaDays) {
                        stringBuilder.append(virathaDay.getDate().split("/")[0]).append(",");
                    }

                flag.add(str + "-" + viradham);
                hashMap.put(str + "-" + viradham, stringBuilder.toString());
            }

            container.setLayoutManager(new GridLayoutManager(context, 5));
            container.setAdapter(new SubItemAdapter(context, flag, hashMap));
        }
    }

    public class SubItemAdapter extends RecyclerView.Adapter<SubItemAdapter.ItemViewHolder> {

        Map<String, String> viratham;
        List<String> flag;
        private Context context;

        SubItemAdapter(Context context, List<String> flag, HashMap<String, String> viratham) {
            this.context = context;
            this.viratham = viratham;
            this.flag = flag;

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

            holder.bind_item(flag.get(position), viratham.get(flag.get(position)));
        }

        @Override
        public int getItemCount() {
            return viratham.size();
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
            void bind_item(String viratham, String days) {

                txtViratham.setText(viratham.split("-")[1].substring(0, 3));
                txtDay.setText(days);

                conatinerLay.setOnClickListener(view -> Toast.makeText(context, "" + viratham + "--" + days, Toast.LENGTH_SHORT).show());
            }
        }
    }
}
