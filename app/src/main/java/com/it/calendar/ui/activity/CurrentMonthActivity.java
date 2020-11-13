package com.it.calendar.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.it.calendar.R;
import com.it.calendar.beans.MainTable;
import com.it.calendar.beans.MuhurthamTable;
import com.it.calendar.beans.VirathaDay;
import com.it.calendar.realm.RealmController;
import com.it.calendar.ui.adapter.CurrentMonthAdapter;
import com.it.calendar.util.Constants;
import com.it.calendar.util.DateTimeHelper;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class CurrentMonthActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private String title = "";
    private Realm realm;

    private HashMap<String, List<?>> listHashMap = new HashMap<>();
    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_month);
        ButterKnife.bind(this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getIntent().getExtras() != null) {
            title = getIntent().getExtras().getString("Title");
        }
        getSupportActionBar().setTitle(title);

        realm = RealmController.with(this).getRealm();

//        DateFormat dateFormat = new SimpleDateFormat("yyyy");
//        Date date = new Date();

        String[] strs = title.split("-");

        RealmResults<MainTable> mainTables = realm.where(MainTable.class)
                .equalTo("month", strs[0].trim())
                .and()
                .equalTo("year", Integer.parseInt(strs[1].trim()))
                .sort("date", Sort.ASCENDING)
                .findAll();

//        int month = Integer.parseInt(DateTimeHelper.getDateFromMillis(mainTables.get(0).getDate()).split("-")[1]);

        Long fromDate = DateTimeHelper.getMillisFromDate(DateTimeHelper.simpleDateFormat.format(DateTimeHelper.getCalendarViewFromDate(calendar)));
        Long toDate = DateTimeHelper.getMillisFromDate(DateTimeHelper.simpleDateFormat.format(DateTimeHelper.getToDate(calendar)));

        RealmResults<MuhurthamTable> moogurthamTables = realm.where(MuhurthamTable.class)
                .greaterThanOrEqualTo(Constants.date, fromDate)
                .and()
                .lessThanOrEqualTo(Constants.date, toDate)
                .findAll();

        RealmResults<VirathaDay> virathaDays = realm.where(VirathaDay.class)
                .greaterThanOrEqualTo(Constants.date, fromDate)
                .and()
                .lessThanOrEqualTo(Constants.date, toDate)
                .and()
                .notEqualTo("viratham", "சுபமுகூர்த்தம்")
                .sort(Constants.date, Sort.ASCENDING)
                .findAll();

        List<MainTable> govtLeave = mainTables.where().equalTo("leave_flag", 1).findAll();
        List<MainTable> hinduFes = mainTables.where().notEqualTo("hindu_fes", "-").findAll();
        List<MainTable> chirsFes = mainTables.where().notEqualTo("chirs_fes", "-").findAll();
        List<MainTable> muslimFes = mainTables.where().notEqualTo("muslim_fes", "-").findAll();

        listHashMap.put("அரசினர் விடுமுறை நாட்கள்", govtLeave);
        listHashMap.put("சுபமுகூர்த்த தினங்கள்", moogurthamTables);
        listHashMap.put("முக்கிய விரத தினங்கள்", virathaDays);
        listHashMap.put("இந்துக்கள் பண்டிகைகள்", hinduFes);
        listHashMap.put("கிறிஸ்துவ பண்டிகைகள்", chirsFes);
        listHashMap.put("முஸ்லீம் பண்டிகைகள்", muslimFes);

        recyclerView.setLayoutManager(new LinearLayoutManager(CurrentMonthActivity.this));
        recyclerView.setAdapter(new CurrentMonthAdapter(this, listHashMap));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
