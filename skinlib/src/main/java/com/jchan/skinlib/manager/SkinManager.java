package com.jchan.skinlib.manager;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;

import com.jchan.skinlib.ApplicationActivityLifeCycle;
import com.jchan.skinlib.support.SkinPreference;
import com.jchan.skinlib.util.SkinResources;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Observable;

/**
 * @create time: 2021/8/5
 * @author: JChan
 * @description:
 */
public class SkinManager extends Observable {
    /**
     * Activity生命周期
     */
    private ApplicationActivityLifeCycle applicationActivityLifeCycle;
    private Application application;

    /**
     * 单例 start================================================================================
     */
    private volatile static SkinManager instance;
    public static void init(Application application){
        if(instance == null){
            synchronized (SkinManager.class){
                if(instance == null){
                    instance = new SkinManager(application);
                }
            }
        }
    }
    private SkinManager(Application application){
        this.application = application;
        //初始化
        SkinPreference.init(application);
        SkinResources.init(application);
        //注册Activity生命周期
        applicationActivityLifeCycle = new ApplicationActivityLifeCycle(this);
        application.registerActivityLifecycleCallbacks(applicationActivityLifeCycle);
        //加载上次保存的皮肤
        loadSkin(SkinPreference.getInstance().getSkin());
    }

    public static SkinManager getInstance(){
        return instance;
    }
    /**
     * 单例 end================  ================================================================
     */

    public void loadSkin(String skinPath) {
        if(TextUtils.isEmpty(skinPath)){
            //还原默认皮肤
            SkinPreference.getInstance().reset();
            SkinResources.getInstance().reset();
        }else {
            //宿主app的resource
            Resources appResources = application.getResources();
            try {
                //反射创建AssetManager 与 Resource
                AssetManager assetManager = AssetManager.class.newInstance();
                Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
                addAssetPath.invoke(assetManager,skinPath);

                //根据当前设备显示器信息创建Resources
                Resources skinResources = new Resources(assetManager,appResources.getDisplayMetrics(),appResources.getConfiguration());

                //获取皮肤包包名
                PackageManager packageManager = application.getPackageManager();
                PackageInfo info = packageManager.getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES);
                String packageName = info.packageName;
                //存储皮肤包resources
                SkinResources.getInstance().applySkin(skinResources,packageName);
                //记录当前皮肤包
                SkinPreference.getInstance().setSkin(skinPath);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        //通过观察者模式 通知所有观察者
        setChanged();
        notifyObservers(null);

    }

}
