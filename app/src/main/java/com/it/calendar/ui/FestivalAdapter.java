package com.it.calendar.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.it.calendar.R;
import com.it.calendar.model.MainTable;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class FestivalAdapter extends RecyclerView.Adapter<FestivalAdapter.ItemViewHolder> {

    List<String> months;
    Map<String, List<?>> mainTbl;
    private Context context;
    private Realm realm;
    private String queryFlag;

    public FestivalAdapter(Context context, String queryFlag, List<String> months, Map<String, List<?>> mainTbl) {
        this.context = context;
        this.months = months;
        this.mainTbl = mainTbl;
        this.queryFlag = queryFlag;

        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_item_layout, parent, false);
        return new ItemViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

        holder.bind_fes(months.get(position));
    }

    @Override
    public int getItemCount() {
        return mainTbl.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.container)
        LinearLayout container;

        ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind_fes(String s) {

            container.removeAllViews();
            title.setText(s);

            for (int i = 0; i < mainTbl.get(s).size(); i++) {

                MainTable mainTable = (MainTable) mainTbl.get(s).get(i);
                View view = LayoutInflater.from(context).inflate(R.layout.festival_sub_item, null, false);

                SubItemViewHolder subItemViewHolder = new SubItemViewHolder(view);
                subItemViewHolder.bind(i, mainTable);

                container.addView(view);
            }
        }
    }

    class SubItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.day)
        TextView day;
        @BindView(R.id.function)
        TextView function;
        @BindView(R.id.itemContainer)
        LinearLayout itemContainer;

        SubItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @SuppressLint("SetTextI18n")
        void bind(int i, MainTable mainTable) {

            if (i == 0 || (i % 2 == 0)) {
                itemContainer.setBackgroundColor(context.getResources().getColor(R.color.grey_ec));
            } else {
                itemContainer.setBackgroundColor(Color.WHITE);
            }

            date.setText("" + mainTable.getDay());
            day.setText("" + mainTable.getWeekday());
            if (queryFlag.equals("hindu_fes")) {
                function.setText("" + mainTable.getHindu_fes());
            } else if (queryFlag.equals("muslim_fes")) {
                function.setText("" + mainTable.getMuslim_fes());
            } else if (queryFlag.equals("gov_holiday")) {
                function.setText("" + mainTable.getGov_holiday());
            } else {
                function.setText("" + mainTable.getChirs_fes());
            }
        }
    }
}
