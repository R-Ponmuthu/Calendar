package com.it.calendar.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.github.vipulasri.timelineview.TimelineView;
import com.it.calendar.R;
import com.it.calendar.beans.Kalangal;
import com.it.calendar.util.EnumKalangal;
import com.it.calendar.util.EnumParikaram;
import com.it.calendar.util.EnumWeekDay;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class RaaguAdapter extends RecyclerView.Adapter<RaaguAdapter.ItemViewHolder> {

    List<Kalangal> kalangals;
    private Context context;
    private Realm realm;

    public RaaguAdapter(Context context, RealmResults<Kalangal> kalangals) {

        this.context = context;
        this.kalangals = kalangals;

        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_item_layout, parent, false);
        return new ItemViewHolder(itemView, viewType);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

        Kalangal kalangal = kalangals.get(position);
        holder.bind_kalangal(kalangal);
    }

    @Override
    public int getItemCount() {
        return kalangals.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.container)
        LinearLayout container;
        @BindView(R.id.timeline)
        TimelineView timelineView;

        ItemViewHolder(View view, int viewType) {
            super(view);
            ButterKnife.bind(this, view);

            timelineView.initLine(viewType);
        }

        @SuppressLint("SetTextI18n")
        void bind_kalangal(Kalangal kalangal) {

            container.removeAllViews();

            title.setText(EnumWeekDay.getWeekDay(kalangal.getWeekday()).getText());

            View view = LayoutInflater.from(context).inflate(R.layout.raagu_sub_item, null, false);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(params);
            SubItemViewHolder subItemViewHolder = new SubItemViewHolder(view);
            subItemViewHolder.bind(kalangal);

            container.addView(view);
        }
    }

    class SubItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.raagu)
        TextView raagu;
        @BindView(R.id.kulikai)
        TextView kulikai;
        @BindView(R.id.emakandam)
        TextView emakandam;
        @BindView(R.id.vaarasulai)
        TextView vaarasulai;
        @BindView(R.id.parikaram)
        TextView parikaram;
        @BindView(R.id.containerLay)
        LinearLayout containerLay;

        SubItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @SuppressLint("SetTextI18n")
        void bind(Kalangal kalangal) {

            raagu.setText("இராகு: " + kalangal.getRagu());
            kulikai.setText("குளிகை: " + kalangal.getKuligai());
            emakandam.setText("எமகண்டம்: " + kalangal.getEmakandam());
            vaarasulai.setText("வாரசூலை: " + EnumKalangal.getKalangalStr(kalangal.getSoolam()).getText());
            parikaram.setText("பரிகாரம்: " + EnumParikaram.getParikaramStr(kalangal.getParikaram()).getText());
        }
    }
}
