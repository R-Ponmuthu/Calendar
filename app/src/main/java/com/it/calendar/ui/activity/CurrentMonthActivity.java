package com.it.calendar.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.it.calendar.CalendarApp;
import com.it.calendar.R;
import com.it.calendar.beans.MainTable;
import com.it.calendar.beans.MuhurthamTable;
import com.it.calendar.beans.VirathaDay;
import com.it.calendar.ui.adapter.CurrentMonthAdapter;
import com.it.calendar.util.Constants;
import com.it.calendar.util.DateTimeHelper;
import com.it.calendar.util.EnumMonth;
import com.it.calendar.util.EnumTamilMonth;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.it.core.db.TableHelper;

public class CurrentMonthActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private String title = "";

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

//        DateFormat dateFormat = new SimpleDateFormat("yyyy");
//        Date date = new Date();

        String[] strs = title.split("-");

        String fromDate = DateTimeHelper.simpleDateFormat.format(DateTimeHelper.getCalendarViewFromDate(calendar));
        String toDate = DateTimeHelper.simpleDateFormat.format(DateTimeHelper.getToDate(calendar));

        TableHelper<MuhurthamTable> muhurthamTableTableHelper = CalendarApp.getTable(this, MuhurthamTable.class);
//        List<MuhurthamTable> moogurthamTables = muhurthamTableTableHelper.getList("date>=? and date<=?", new String[]{fromDate, toDate}, null, null);

        List<MuhurthamTable> moogurthamTables = muhurthamTableTableHelper.rawQuery(muhurthamTableTableHelper.getReadableDatabase(),
                "select * from MuhurthamTable where date like '%" + fromDate.substring(3) + "%'", null);

        TableHelper<VirathaDay> virathaDayTableHelper = CalendarApp.getTable(this, VirathaDay.class);
//        List<VirathaDay> virathaDays = virathaDayTableHelper.getList("date>=? and date<=? and viratham!=?",
//                new String[]{fromDate, toDate, "சுபமுகூர்த்தம்"}, "date ASC", null);

        List<VirathaDay> virathaDays = virathaDayTableHelper.rawQuery(virathaDayTableHelper.getReadableDatabase(),
                "select * from VirathaDay where date like '%" + fromDate.substring(3) + "%' and viratham=1 union " +
                        "select * from VirathaDay where date like '%" + fromDate.substring(3) + "%' and viratham=3 union " +
                        "select * from VirathaDay where date like '%" + fromDate.substring(3) + "%' and viratham=4 union " +
                        "select * from VirathaDay where date like '%" + fromDate.substring(3) + "%' and viratham=17 union " +
                        "select * from VirathaDay where date like '%" + fromDate.substring(3) + "%' and viratham=13 union " +
                        "select * from VirathaDay where date like '%" + fromDate.substring(3) + "%' and viratham=5 union " +
                        "select * from VirathaDay where date like '%" + fromDate.substring(3) + "%' and viratham=6 union " +
                        "select * from VirathaDay where date like '%" + fromDate.substring(3) + "%' and viratham=7 union " +
                        "select * from VirathaDay where date like '%" + fromDate.substring(3) + "%' and viratham=11 union " +
                        "select * from VirathaDay where date like '%" + fromDate.substring(3) + "%' and viratham=14 union " +
                        "select * from VirathaDay where date like '%" + fromDate.substring(3) + "%' and viratham=15 union " +
                        "select * from VirathaDay where date like '%" + fromDate.substring(3) + "%' and viratham=19", null);

        TableHelper<MainTable> mainTableTableHelper = CalendarApp.getTable(this, MainTable.class);
        List<MainTable> govtLeave = mainTableTableHelper.getList("month=? and year=? and leave_flag=?",
                new String[]{String.valueOf(EnumMonth.getMonth(strs[0]).getDay()), String.valueOf(Integer.parseInt(strs[1].trim())), String.valueOf(1)}, "date ASC", null);

        List<MainTable> hinduFes = mainTableTableHelper.getList("month=? and year=? and hindu_fes!=?",
                new String[]{String.valueOf(EnumMonth.getMonth(strs[0]).getDay()), String.valueOf(Integer.parseInt(strs[1].trim())), "-"}, "date ASC", null);

        List<MainTable> chirsFes = mainTableTableHelper.getList("month=? and year=? and chirs_fes!=?",
                new String[]{String.valueOf(EnumMonth.getMonth(strs[0]).getDay()), String.valueOf(Integer.parseInt(strs[1].trim())), "-"}, "date ASC", null);

        List<MainTable> muslimFes = mainTableTableHelper.getList("month=? and year=? and muslim_fes!=?",
                new String[]{String.valueOf(EnumMonth.getMonth(strs[0]).getDay()), String.valueOf(Integer.parseInt(strs[1].trim())), "-"}, "date ASC", null);


//        List<MainTable> govtLeave = mainTables.where().equalTo("leave_flag", 1).findAll();
//        List<MainTable> hinduFes = mainTables.where().notEqualTo("hindu_fes", "-").findAll();
//        List<MainTable> chirsFes = mainTables.where().notEqualTo("chirs_fes", "-").findAll();
//        List<MainTable> muslimFes = mainTables.where().notEqualTo("muslim_fes", "-").findAll();

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
