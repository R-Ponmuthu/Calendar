package com.it.calendar.ui;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.it.calendar.R;
import com.it.calendar.model.Notification;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {


    private Context context;
    private List<Notification> notificationList;

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.month)
        TextView month;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.message)
        TextView message;
        @BindView(R.id.itemParent)
        CardView itemParent;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(Notification notification) {

            title.setText(notification.getTitle());
            message.setText(notification.getMessage());
            String dt = notification.getDate().substring(0, 10);
            String[] dateSplit = dt.split("/");
            for (String str : dateSplit)
                Log.e("Date", str);
            date.setText(dateSplit[2]);
            month.setText(dateSplit[1] + "/" + dateSplit[0]);

            itemParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    NotificationAdapter(Context context, List<Notification> notificationList) {
        this.notificationList = notificationList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Notification notification = notificationList.get(position);

        holder.bind(notification);
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }
}
