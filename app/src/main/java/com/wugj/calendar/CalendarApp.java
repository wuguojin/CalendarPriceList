package com.wugj.calendar;

import android.app.Application;

import com.huicent.library.utils.Utils;

/**
 * 注意： 核心库初始化
 *
 * @author wuguojin
 * @date 2018/6/11
 */
public class CalendarApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
