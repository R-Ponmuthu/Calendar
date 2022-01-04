package com.it.calendar.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.it.calendar.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewNotificationActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.content)
    TextView content;

    private int id;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notification);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("அறிவிப்புகள்");

        if (getIntent().getExtras() != null) {
            id = getIntent().getExtras().getInt("Id");
            type = getIntent().getExtras().getString("Type");
        }

//        Notification notification = realm.where(Notification.class)
//                .equalTo("id", id)
//                .findFirst();
//
//        if (notification != null)
//            if (type.equals("BT")) {
//
//                imageView.setVisibility(View.GONE);
//                title.setText(notification.getTitle());
//                content.setText(notification.getBigMessage());
//            } else if (type.equals("BP")) {
//
//                title.setText(notification.getTitle());
//                content.setText(notification.getBigMessage());
//                imageView.setImageBitmap(getBitmapfromUrl(notification.getImageUrl()));
//
//            } else {
//                imageView.setVisibility(View.GONE);
//                title.setText(notification.getTitle());
//                content.setText(notification.getBigMessage());
//            }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }
}
