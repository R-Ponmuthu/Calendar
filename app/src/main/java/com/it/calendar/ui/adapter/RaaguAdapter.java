package com.it.calendar.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
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

public class RaaguAdapter extends RecyclerView.Adapter<RaaguAdapter.ItemViewHolder> {

    List<Kalangal> kalangals;
    private Context context;

    public RaaguAdapter(Context context, List<Kalangal> kalangals) {

        this.context = context;
        this.kalangals = kalangals;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.raagu_sub_item, parent, false);
        return new ItemViewHolder(itemView, viewType);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

        Kalangal kalangal = kalangals.get(position);

        holder.raagu.setText("இராகு: " + kalangal.getRagu());
        holder.kulikai.setText("குளிகை: " + kalangal.getKuligai());
        holder.emakandam.setText("எமகண்டம்: " + kalangal.getEmakandam());
        holder.vaarasulai.setText("வாரசூலை: " + EnumKalangal.getKalangalStr(kalangal.getSoolam()).getText());
        holder.parikaram.setText("பரிகாரம்: " + EnumParikaram.getParikaramStr(kalangal.getParikaram()).getText());
    }

    @Override
    public int getItemCount() {
        return kalangals.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

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

        ItemViewHolder(View view, int viewType) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }
}
