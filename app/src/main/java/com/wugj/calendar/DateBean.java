package com.wugj.calendar;

import java.io.Serializable;
import java.util.Calendar;

/**
 * 类描述
 *
 * @author wuguojin
 * @date 2018/6/8
 */
public class DateBean implements Serializable {
    // 组index
    private int groupIndex;
    // 当前index
    private int childIndex;
    private int year;
    private int month;
    private int day;
    private int dayOfWeek;
    // 普通公历
    private String shownDay;
    //普通农历
    private String nongliDay;
    //特殊日子，如节假日
    private String specialDayTag;
    //选中情况
    private boolean isCheck;
    // 可否选择
    private boolean canSelect;
    // 法定节假日
    private boolean isGovHoliday;
    // 节假日调休工作
    private boolean isGovHolidayWork;
    // 记录的日历
    private Calendar saverCalendar;

    public int getGroupIndex() {
        return groupIndex;
    }

    public void setGroupIndex(int groupIndex) {
        this.groupIndex = groupIndex;
    }

    public int getChildIndex() {
        return childIndex;
    }

    public void setChildIndex(int childIndex) {
        this.childIndex = childIndex;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getShownDay() {
        return shownDay;
    }

    public void setShownDay(String shownDay) {
        this.shownDay = shownDay;
    }

    public String getNongliDay() {
        return nongliDay;
    }

    public void setNongliDay(String nongliDay) {
        this.nongliDay = nongliDay;
    }

    public String getSpecialDayTag() {
        return specialDayTag;
    }

    public void setSpecialDayTag(String specialDayTag) {
        this.specialDayTag = specialDayTag;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public boolean isCanSelect() {
        return canSelect;
    }

    public void setCanSelect(boolean canSelect) {
        this.canSelect = canSelect;
    }


    public boolean isGovHoliday() {
        return isGovHoliday;
    }

    public void setGovHoliday(boolean govHoliday) {
        isGovHoliday = govHoliday;
    }

    public boolean isGovHolidayWork() {
        return isGovHolidayWork;
    }

    public void setGovHolidayWork(boolean govHolidayWork) {
        isGovHolidayWork = govHolidayWork;
    }

    public Calendar getSaverCalendar() {
        return saverCalendar;
    }

    public void setSaverCalendar(Calendar saverCalendar) {
        this.saverCalendar = saverCalendar;
    }

    /**
     * yyyy-MM-dd标准格式
     */
    public String getFomartTag() {
        return String.valueOf(year) + "-" + ((month < 10) ? ("0" + month) : String.valueOf(month)) + "-" +
                ((day < 10) ? ("0" + day) : String.valueOf(day));
    }
}
