package com.jchan.skindemo;

import android.app.Application;

import com.jchan.skinlib.manager.SkinManager;

/**
 * @create time: 2021/8/6
 * @author: JChan
 * @description:
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.init(this);
    }
}
