package com.wugj.calendar;

import java.io.Serializable;
import java.util.List;

/**
 * 类描述
 *
 * @author wuguojin
 * @date 2018/6/8
 */
public class CalendarBean implements Serializable {
    private int year;
    private int month;
    private String shownTitle;
    private List<DateBean> dateBeans;

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

    public String getShownTitle() {
        return shownTitle;
    }

    public void setShownTitle(String shownTitle) {
        this.shownTitle = shownTitle;
    }

    public List<DateBean> getDateBeans() {
        return dateBeans;
    }

    public void setDateBeans(List<DateBean> dateBeans) {
        this.dateBeans = dateBeans;
    }
}
