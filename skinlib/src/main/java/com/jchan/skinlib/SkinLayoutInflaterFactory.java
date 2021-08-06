package com.jchan.skinlib;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jchan.skinlib.util.SkinAttribute;
import com.jchan.skinlib.util.SkinThemeUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
 * @create time: 2021/8/5
 * @author: JChan
 * @description: 接管系统view的生产过程
 */
public class SkinLayoutInflaterFactory implements LayoutInflater.Factory2, Observer {
    /**
     * 控件名字前缀
     */
    private static final String[] mClassPrefixList = {
            "android.widget.",
            "android.webkit.",
            "android.app.",
            "android.view."
    };

    /**
     * 记录view对应的构造函数所需要的参数类型
     */
    private static final Class<?>[] mConstructorSignature = new Class[]{
            Context.class, AttributeSet.class};
    /**
     * 记录构造函数
     */
    private static final HashMap<String, Constructor<? extends View>> mConstructorMap =
            new HashMap<String, Constructor<? extends View>>();


    /**
     * 选择新皮肤后需要替换view与之对应的属性
     * 页面属性管理器
     */
    private SkinAttribute skinAttribute;
    /**
     * 用于获取窗口的状态
     */
    private Activity activity;

    public SkinLayoutInflaterFactory( Activity activity) {
        this.activity = activity;
        this.skinAttribute = new SkinAttribute();

    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        View view = createSDKView(name, context, attrs);
        if (view == null) {
            view = createView(name, context, attrs);
        }

        if (view != null) {
            //查找需要替换的皮肤属性并存起来
            skinAttribute.findAttributes(view, attrs);
        }
        return view;
    }

    private View createSDKView(String name, Context context, AttributeSet attrs) {
        //如果带点的就是自定义view
        if (-1 != name.indexOf('.')) {
            return null;
        }
        //拼上前缀尝试去反射
        for (int i = 0; i < mClassPrefixList.length; i++) {
            Log.e("factory",  mClassPrefixList[i] + name );
            View view = createView(mClassPrefixList[i] + name, context, attrs);
            if(view != null){
                return view;
            }
        }
        return null;
    }

    private View createView(String name, Context context, AttributeSet attrs) {
        Log.e("createView",name);

        Constructor<? extends View> constructor = finConstructor(context, name);
        try {
            return constructor.newInstance(context, attrs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查找view的构造方法
     *
     * @param context
     * @param name
     * @return
     */
    private Constructor<? extends View> finConstructor(Context context, String name) {
        Constructor<? extends View> constructor = mConstructorMap.get(name);
        if (constructor == null) {
            try {
                Class<? extends View> clazz = context.getClassLoader().loadClass(name).asSubclass(View.class);
                constructor = clazz.getConstructor(mConstructorSignature);
                mConstructorMap.put(name, constructor);
            } catch (ClassNotFoundException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return constructor;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return null;
    }

    /**
     * 如果被观察者发送通知，就会执行
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        //状态栏，和所有view都执行换肤
        SkinThemeUtils.updateStatusBarColor(activity);
        skinAttribute.updateAllSkin();
    }
}
