package com.wugj.calendar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * 首页
 *
 * @author wuguojin
 * @date 2018/7/3
 */
public class MainActivity extends AppCompatActivity {

    protected TextView barTitle;
    protected Toolbar toolbar;
    private Button singleCalendar;
    private Button roundCalendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);
        initActionBar();
        assignViews();
    }

    public void initActionBar() {
        toolbar = (Toolbar) findViewById(R.id.base_toolbar);
        barTitle = (TextView) findViewById(R.id.bar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        barTitle.setText("主页");
    }

    private void assignViews() {
        singleCalendar = (Button) findViewById(R.id.single_calendar);
        roundCalendar = (Button) findViewById(R.id.round_calendar);
        singleCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarActivity.startForResult(MainActivity.this,
                        CalendarActivity.MODE.SINGLE.toNumber(), null, 0x1024);
            }
        });
        roundCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarActivity.startForResult(MainActivity.this,
                        CalendarActivity.MODE.ROUND.toNumber(), null, 0x1024);
            }
        });
    }


}
