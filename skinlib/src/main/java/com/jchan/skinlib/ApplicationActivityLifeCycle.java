package com.jchan.skinlib;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;
import androidx.core.view.LayoutInflaterCompat;

import com.jchan.skinlib.manager.SkinManager;
import com.jchan.skinlib.util.SkinThemeUtils;

import java.lang.reflect.Field;
import java.util.Observable;

/**
 * @create time: 2021/8/5
 * @author: JChan
 * @description:
 */
public class ApplicationActivityLifeCycle implements Application.ActivityLifecycleCallbacks {

    private Observable mObservable;
    private ArrayMap<Activity,SkinLayoutInflaterFactory> mLayoutInflaterFactories = new ArrayMap<>();

    public ApplicationActivityLifeCycle(Observable mObservable) {
        this.mObservable = mObservable;
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        /**
         * 更新状态栏
         */
        Log.e("skin","onActivityCreated "+activity.getClass().getSimpleName());
        SkinThemeUtils.updateStatusBarColor(activity);

        //获取布局管理器
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        /**
         * android Q 以后不能用
         */
//        try {
//            //反射Factory
//            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
//            field.setAccessible(true);
//            field.setBoolean(layoutInflater,false);
//        } catch (Exception e) {
//            Log.e("skin","exception "+e.toString());
//            e.printStackTrace();
//        }
        //使用自己定义的皮肤工厂
        SkinLayoutInflaterFactory skinLayoutInflaterFactory = new SkinLayoutInflaterFactory(activity);
        forceSetFactory2(layoutInflater,skinLayoutInflaterFactory);

//        LayoutInflaterCompat.setFactory2(layoutInflater,skinLayoutInflaterFactory);
        mLayoutInflaterFactories.put(activity,skinLayoutInflaterFactory);

        mObservable.addObserver(skinLayoutInflaterFactory);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        SkinLayoutInflaterFactory observer = mLayoutInflaterFactories.remove(activity);
        SkinManager.getInstance().deleteObserver(observer);
    }

    private static void forceSetFactory2(LayoutInflater inflater,SkinLayoutInflaterFactory skinLayoutInflaterFactory) {
        Class<LayoutInflaterCompat> compatClass = LayoutInflaterCompat.class;
        Class<LayoutInflater> inflaterClass = LayoutInflater.class;
        try {
            Field sCheckedField = compatClass.getDeclaredField("sCheckedField");
            sCheckedField.setAccessible(true);
            sCheckedField.setBoolean(inflater, false);
            Field mFactory = inflaterClass.getDeclaredField("mFactory");
            mFactory.setAccessible(true);
            Field mFactory2 = inflaterClass.getDeclaredField("mFactory2");
            mFactory2.setAccessible(true);
//            if (inflater.getFactory2() != null) {
//                factory.setInterceptFactory2(inflater.getFactory2());
//            } else if (inflater.getFactory() != null) {
//                factory.setInterceptFactory(inflater.getFactory());
//            }
            mFactory2.set(inflater, skinLayoutInflaterFactory);
            mFactory.set(inflater, skinLayoutInflaterFactory);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

}
