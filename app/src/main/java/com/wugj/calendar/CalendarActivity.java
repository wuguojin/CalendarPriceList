package com.wugj.calendar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huicent.library.utils.ThreadPoolProxy;
import com.huicent.library.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 价格日期页面
 *
 * @author wuguojin
 * @date 2018/6/8
 */
public class CalendarActivity extends AppCompatActivity implements CalendarListener {

    public static void start(Context context) {
        context.startActivity(new Intent(context, CalendarActivity.class));
    }

    private static Context mContext;
    LinearLayout weekLayout;
    RecyclerView recyclerView;
    RecyclerAdapter groupAdatper;
    private List<CalendarBean> calendars = new ArrayList<>();
    private int[] startToEndMonth = {0, 8};
    private static String[] weeks = {"日", "一", "二", "三", "四", "五", "六"};
    private HashMap<String, String> priceMap = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.calendar_activity_layout);
        initData();
        initView();
        initPrice();
    }

    private void initData() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        CalendarBean calendarBean = null;
        int groupIndex = 0;
        for (int i = startToEndMonth[0]; i <= startToEndMonth[1]; i++) {
            calendarBean = new CalendarBean();
            Calendar calendarClone = (Calendar) calendar.clone();
            calendarClone.add(Calendar.MONTH, i);
            calendarClone.set(Calendar.DATE, 1);
            calendarBean.setYear(calendarClone.get(Calendar.YEAR));
            calendarBean.setMonth(calendarClone.get(Calendar.MONTH) + 1);
            calendarBean.setShownTitle(calendarBean.getYear() + "-" + calendarBean.getMonth());

            // 1-星期天 7-星期六
            int dayOfWeek = calendarClone.get(Calendar.DAY_OF_WEEK);
            //上月的最后几天展示在本月
            int emptyCount = dayOfWeek - 1;
            calendarClone.roll(Calendar.DATE, -1);
            // 当月的最大天数
            int maxDays = calendarClone.get(Calendar.DATE);

            List<DateBean> daysList = new ArrayList<>();
            int maxRows = 5;
            if (emptyCount + maxDays > 35) {
                //当月有效日期+1号前空白日期个数>35,则当月需要6行
                maxRows = 6;
            }
            for (int j = 0; j < (maxRows * 7); j++) {
                DateBean dayItem = new DateBean();
                // 用于控制定位
                dayItem.setGroupIndex(groupIndex);
                dayItem.setChildIndex(j);

                Calendar calendarDayClone = (Calendar) calendarClone.clone();
                if (j < emptyCount) {
                    dayItem.setCanSelect(false);
                    //上月最后几天
                    calendarDayClone.add(Calendar.DATE, (j - emptyCount));
                    dayItem.setShownDay("");
                    dayItem.setNongliDay("");
                    daysList.add(dayItem);

                    continue;
                }
                if (j >= emptyCount + maxDays) {
                    dayItem.setCanSelect(false);
                    //下月头几天
                    calendarDayClone.add(Calendar.MONTH, 1);
                    calendarDayClone.set(Calendar.DATE, j - (emptyCount + maxDays) + 1);
                    dayItem.setShownDay("");
                    dayItem.setNongliDay("");
                    daysList.add(dayItem);

                    continue;
                }
                calendarDayClone.set(Calendar.DATE, j - emptyCount + 1);
                //显示的日期
                dayItem.setShownDay(String.valueOf(calendarDayClone.get(Calendar.DATE)));
                //显示的农历
                dayItem.setNongliDay(new LunarDayUtil(calendarDayClone).toStringSimpleDay());
                //显示的特殊日子
                dayItem.setSpecialDayTag(SpecialDayUtil.getInstance().getHolidayName(calendarDayClone));

                dayItem.setYear(calendarDayClone.get(Calendar.YEAR));
                dayItem.setMonth(calendarDayClone.get(Calendar.MONTH) + 1);
                dayItem.setDay(calendarDayClone.get(Calendar.DATE));
                dayItem.setDayOfWeek(calendarDayClone.get(Calendar.DAY_OF_WEEK));
                dayItem.setGovHoliday(SpecialDayUtil.getInstance().isGovHoliday(calendarDayClone));
                dayItem.setGovHolidayWork(SpecialDayUtil.getInstance().isGovHolidayWork(calendarDayClone));

                if (calendarDayClone.before(calendar)) {
                    //今天之前
                    dayItem.setCanSelect(false);
                } else if (calendarDayClone.equals(calendar)) {
                    dayItem.setShownDay("今天");
                    dayItem.setCanSelect(true);
                } else {
                    //今天之后
                    dayItem.setCanSelect(true);
                }
                dayItem.setSaverCalendar(calendarDayClone);

                daysList.add(dayItem);
            }
            calendarBean.setDateBeans(daysList);
            calendars.add(calendarBean);
            groupIndex++;
        }
    }

    private void initView() {
        weekLayout = (LinearLayout) findViewById(R.id.week_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_calendar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        groupAdatper = new RecyclerAdapter(calendars, this);
        recyclerView.setAdapter(groupAdatper);

        TextView weekHint;
        weekLayout.removeAllViews();
        for (int i = 0; i < weeks.length; i++) {
            weekHint = new TextView(mContext);
            weekHint.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            weekHint.setGravity(Gravity.CENTER);
            weekHint.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            weekHint.setText(weeks[i]);
            if (i == 0 || i == weeks.length - 1) {
                //周末-红色
                weekHint.setTextColor(Color.parseColor("#D83939"));
            } else {
                //工作日
                weekHint.setTextColor(Color.parseColor("#111111"));
            }
            weekLayout.addView(weekHint);
        }
    }

    private void initPrice() {
        priceMap.put("2018-06-11", "360");
        priceMap.put("2018-06-12", "370");
        priceMap.put("2018-06-13", "310");
        priceMap.put("2018-06-14", "380");
        priceMap.put("2018-06-15", "250");
        priceMap.put("2018-06-16", "330");
        priceMap.put("2018-06-17", "460");
        priceMap.put("2018-06-18", "460");
        priceMap.put("2018-06-19", "470");
        priceMap.put("2018-06-20", "440");
        priceMap.put("2018-06-21", "410");
        priceMap.put("2018-06-22", "550");
        priceMap.put("2018-06-23", "250");
        priceMap.put("2018-06-24", "520");
        priceMap.put("2018-06-25", "530");
        priceMap.put("2018-06-26", "470");
        priceMap.put("2018-06-27", "490");

        new ThreadPoolProxy().executeTaskDelay(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        groupAdatper.updateForPrice(priceMap);
                    }
                });
            }
        }, 1000);
    }

    @Override
    public void onDaySelect(DateBean bean) {
        ToastUtils.showShort(bean.getFomartTag());
        for (CalendarBean monthBean : calendars) {
            for (DateBean dateBean : monthBean.getDateBeans()) {
                if (dateBean.getFomartTag().equals(bean.getFomartTag())) {
                    dateBean.setCheck(true);
                } else {
                    dateBean.setCheck(false);
                }
                if (dateBean.getSaverCalendar() != null) {
                    if (dateBean.getSaverCalendar().before(bean.getSaverCalendar())) {
                        dateBean.setCanSelect(false);
                    } else {
                        dateBean.setCanSelect(true);
                    }
                } else {
                    dateBean.setCanSelect(false);
                }

            }
        }
        groupAdatper.notifyDataSetChanged();
        ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(
                bean.getGroupIndex(), 0);
    }


    public static class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final static int TYPE_CONTENT = 1;

        private List<CalendarBean> results = new ArrayList<>();
        private HashMap<String, String> priceMap = new HashMap<>();
        private CalendarListener calendarListener;

        public RecyclerAdapter(List<CalendarBean> results, CalendarListener calendarListener) {
            this.results = results;
            this.calendarListener = calendarListener;
        }

        public void updateForPrice(HashMap<String, String> priceMap) {
            this.priceMap = priceMap;
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            return TYPE_CONTENT;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.calendar_group_item, parent, false);
            return new CalendarViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            initItemView(holder, position, results.get(position));
        }

        @Override
        public int getItemCount() {
            return results != null ? (results.size()) : 0;
        }

        private void initItemView(RecyclerView.ViewHolder holder, int position, CalendarBean bean) {
            CalendarViewHolder calendarViewHolder = (CalendarViewHolder) holder;
            calendarViewHolder.mTitle.setText(bean.getShownTitle());
            calendarViewHolder.subAdapter.showResult(bean.getDateBeans(), priceMap);
        }

        class CalendarViewHolder extends RecyclerView.ViewHolder {

            TextView mTitle;
            RecyclerView recyclerView;
            SubRecyclerAdapter subAdapter;
            List<DateBean> subResults = new ArrayList<>();

            public CalendarViewHolder(View itemView) {
                super(itemView);
                mTitle = (TextView) itemView.findViewById(R.id.month_title);
                recyclerView = (RecyclerView) itemView.findViewById(R.id.month_recycler);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 7);
                recyclerView.setLayoutManager(gridLayoutManager);
                subAdapter = new SubRecyclerAdapter(subResults, calendarListener);
                recyclerView.setAdapter(subAdapter);
            }
        }
    }


    public static class SubRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<DateBean> results = new ArrayList<>();
        private HashMap<String, String> priceMap = new HashMap<>();
        private CalendarListener calendarListener;

        public SubRecyclerAdapter(List<DateBean> results, CalendarListener calendarListener) {
            this.results = results;
            this.calendarListener = calendarListener;
        }

        public void showResult(List<DateBean> results, HashMap<String, String> priceMap) {
            this.results = results;
            this.priceMap = priceMap;
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.calendar_child_item, parent, false);
            return new SubCalendarViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            initItemView(holder, position, results.get(position));
        }

        @Override
        public int getItemCount() {
            return results != null ? (results.size()) : 0;
        }

        private void initItemView(RecyclerView.ViewHolder holder, int position, final DateBean bean) {
            final SubCalendarViewHolder subCalendarViewHolder = (SubCalendarViewHolder) holder;
            subCalendarViewHolder.mDay.setText(bean.getShownDay());
            if (priceMap != null && !TextUtils.isEmpty(priceMap.get(bean.getFomartTag()))) {
                subCalendarViewHolder.mSubDay.setText("￥" + priceMap.get(bean.getFomartTag()));
            } else if (!TextUtils.isEmpty(bean.getSpecialDayTag())) {
                subCalendarViewHolder.mSubDay.setText(bean.getSpecialDayTag());
            } else {
                subCalendarViewHolder.mSubDay.setText(bean.getNongliDay());
            }
            // 法定节假日默认隐藏
            subCalendarViewHolder.mGovHolidayHint.setVisibility(View.INVISIBLE);
            if (!bean.isCanSelect()) {
                // 不可选
                subCalendarViewHolder.mDay.setTextColor(Color.parseColor("#D5D5D5"));
            } else if (bean.isGovHoliday()) {
                // 节假日
                subCalendarViewHolder.mGovHolidayHint.setVisibility(View.VISIBLE);
                subCalendarViewHolder.mGovHolidayHint.setText("休");
                subCalendarViewHolder.mGovHolidayHint.setTextColor(Color.parseColor("#D83939"));
                subCalendarViewHolder.mDay.setTextColor(Color.parseColor("#D83939"));
            } else if (bean.isGovHolidayWork()) {
                // 调休工作日
                subCalendarViewHolder.mGovHolidayHint.setVisibility(View.VISIBLE);
                subCalendarViewHolder.mGovHolidayHint.setText("班");
                subCalendarViewHolder.mGovHolidayHint.setTextColor(Color.parseColor("#111111"));
                subCalendarViewHolder.mDay.setTextColor(Color.parseColor("#111111"));
            } else if (bean.getDayOfWeek() == 1 || bean.getDayOfWeek() == 7) {
                //正常周末
                subCalendarViewHolder.mDay.setTextColor(Color.parseColor("#D83939"));
            } else {
                //其他工作日
                subCalendarViewHolder.mDay.setTextColor(Color.parseColor("#111111"));
            }
            if (bean.isCheck()) {
                //选中 blue
                subCalendarViewHolder.itemView.setBackgroundResource(R.drawable.calendar_check_bg);
            } else {
                subCalendarViewHolder.itemView.setBackgroundColor(Color.parseColor("#00000000"));
            }
            subCalendarViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bean.isCanSelect()) {
                        calendarListener.onDaySelect(bean);
                    }
                }
            });
        }

        static class SubCalendarViewHolder extends RecyclerView.ViewHolder {

            TextView mDay;
            TextView mSubDay;
            TextView mGovHolidayHint;

            public SubCalendarViewHolder(View itemView) {
                super(itemView);
                mDay = (TextView) itemView.findViewById(R.id.day);
                mSubDay = (TextView) itemView.findViewById(R.id.subday);
                mGovHolidayHint = (TextView) itemView.findViewById(R.id.gov_holiday_hint);
            }
        }
    }
}
