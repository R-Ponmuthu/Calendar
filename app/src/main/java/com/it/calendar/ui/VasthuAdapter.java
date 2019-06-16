package com.it.calendar.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.it.calendar.R;
import com.it.calendar.model.MainTable;
import com.it.calendar.model.Vasthu;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class VasthuAdapter extends RecyclerView.Adapter<VasthuAdapter.ItemViewHolder> {

    List<Vasthu> vasthus;
    private Context context;
    private Realm realm;

    public VasthuAdapter(Context context, RealmResults<Vasthu> vasthus) {

        this.context = context;
        this.vasthus = vasthus;

        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vasthu_sub_item, parent, false);
        return new ItemViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

        Vasthu vasthu = vasthus.get(position);
        holder.bind_kalangal(position, vasthu);
    }

    @Override
    public int getItemCount() {
        return vasthus.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.containerLay)
        LinearLayout containerLay;

        ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @SuppressLint("SetTextI18n")
        void bind_kalangal(int position, Vasthu vasthu) {

            MainTable mainTable = realm.where(MainTable.class)
                    .equalTo("date", vasthu.getDay())
                    .findFirst();

            date.setText(mainTable.getDay() + " " + mainTable.getMonth() + "," + mainTable.getWeekday() + " - " + mainTable.getTam_day() + " " + mainTable.getTam_month());
            time.setText("காலை " + vasthu.getTime());

            if (position == 0 || position % 2 == 0) {
                containerLay.setBackgroundColor(context.getResources().getColor(R.color.grey_ec));
            }
        }
    }
}
