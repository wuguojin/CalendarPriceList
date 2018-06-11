package com.wugj.calendar;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * 节假日手动维护
 *
 * @author wuguojin
 * @date 2018/6/11
 */
public class SpecialDayUtil {

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    static SimpleDateFormat sdf2 = new SimpleDateFormat("MM-dd");
    private static SpecialDayUtil mSpecailDay;
    List<String> govHoldays = new ArrayList<String>();
    List<String> workInHoliday = new ArrayList<String>();
    HashMap<String, String> lunarHodilays = new HashMap<>();
    HashMap<String, String> solarHodilays = new HashMap<>();

    public static SpecialDayUtil getInstance() {
        if (mSpecailDay == null) {
            mSpecailDay = new SpecialDayUtil();
        }
        return mSpecailDay;
    }

    public SpecialDayUtil() {
        // 添加法定节假日
        addGovHoliday();
        // 添加节假日前后调休工作日
        addGovHolidayWork();
        // 添加农历节日
        addLunarHolidays();
        // 添加公历节日
        addSolarHolidays();

    }

    /**
     * 法定节假日
     * 需手动维护
     */
    private void addGovHoliday() {
        // 元旦
        govHoldays.add("2017-12-30");
        govHoldays.add("2017-12-31");
        govHoldays.add("2018-01-01");
        // 春节
        govHoldays.add("2018-02-15");
        govHoldays.add("2018-02-16");
        govHoldays.add("2018-02-17");
        govHoldays.add("2018-02-18");
        govHoldays.add("2018-02-19");
        govHoldays.add("2018-02-20");
        govHoldays.add("2018-02-21");
        // 清明
        govHoldays.add("2018-04-05");
        govHoldays.add("2018-04-06");
        govHoldays.add("2018-04-07");
        // 五一
        govHoldays.add("2018-04-29");
        govHoldays.add("2018-04-30");
        govHoldays.add("2018-05-01");
        // 端午
        govHoldays.add("2018-06-16");
        govHoldays.add("2018-06-17");
        govHoldays.add("2018-06-18");
        // 端午
        govHoldays.add("2018-09-22");
        govHoldays.add("2018-09-23");
        govHoldays.add("2018-09-24");
        // 国庆
        govHoldays.add("2018-10-01");
        govHoldays.add("2018-10-02");
        govHoldays.add("2018-10-03");
        govHoldays.add("2018-10-04");
        govHoldays.add("2018-10-05");
        govHoldays.add("2018-10-06");
        govHoldays.add("2018-10-07");
    }

    /**
     * 法定节假日调休
     * 周末为工作日的情况
     * 需手动维护
     */
    private void addGovHolidayWork() {
        //春节
        workInHoliday.add("2018-02-11");
        workInHoliday.add("2018-02-24");
        //清明
        workInHoliday.add("2018-04-08");
        //五一
        workInHoliday.add("2018-04-28");
        //十一
        workInHoliday.add("2018-09-29");
        workInHoliday.add("2018-09-30");
    }

    /**
     * 添加公历节日
     */
    private void addLunarHolidays() {
        lunarHodilays.put("12-08", "腊八");
        lunarHodilays.put("12-30", "除夕");
        lunarHodilays.put("01-01", "春节");
        lunarHodilays.put("01-15", "元宵节");
        lunarHodilays.put("05-05", "端午节");
        lunarHodilays.put("07-07", "七夕");
        lunarHodilays.put("07-15", "中元节");
        lunarHodilays.put("08-15", "中秋节");
        lunarHodilays.put("09-09", "重阳节");
    }

    /**
     * 添加公历节日
     */
    private void addSolarHolidays() {
        solarHodilays.put("01-01", "元旦");
        solarHodilays.put("02-14", "情人节");
        solarHodilays.put("03-08", "妇女节");
        solarHodilays.put("03-12", "植树节");
        solarHodilays.put("04-01", "愚人节");
        solarHodilays.put("04-05", "清明节");
        solarHodilays.put("05-01", "劳动节");
        solarHodilays.put("05-04", "青年节");
        solarHodilays.put("06-01", "儿童节");
        solarHodilays.put("07-01", "建党节");
        solarHodilays.put("08-01", "建军节");
        solarHodilays.put("09-10", "教师节");
        solarHodilays.put("10-01", "国庆节");
        solarHodilays.put("11-11", "双11");
        solarHodilays.put("12-25", "圣诞节");
    }

    /**
     * 是否为法定节假日
     *
     * @param calendar
     */
    public boolean isGovHoliday(Calendar calendar) {
        String dayStr = sdf.format(calendar.getTime());
        return govHoldays.contains(dayStr);
    }

    /**
     * 是否法定节假日前后调休工作日
     *
     * @param calendar
     * @return
     */
    public boolean isGovHolidayWork(Calendar calendar) {
        String dayStr = sdf.format(calendar.getTime());
        return workInHoliday.contains(dayStr);
    }

    /**
     * 获取节假日名称
     *
     * @param calendar
     * @return
     */
    public String getHolidayName(Calendar calendar) {
        String solarDate = sdf2.format(calendar.getTime());
        String holidayName = solarHodilays.get(solarDate);
        if (TextUtils.isEmpty(holidayName)) {
            String lunarDate = new LunarDayUtil(calendar).toLunarFormatDay();
            holidayName = lunarHodilays.get(lunarDate);
        }
        return holidayName;
    }
}
