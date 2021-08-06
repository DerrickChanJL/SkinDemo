package com.jchan.skinlib.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.jchan.skinlib.support.SkinPreference;

/**
 * @create time: 2021/8/4
 * @author: JChan
 * @description: 换肤资源
 */
public class SkinResources {

    /**
     * 单例 start================================================================================
     */
    private volatile static SkinResources instance;
    public static void init(Context context){
        if(instance == null){
            synchronized (SkinResources.class){
                if(instance == null){
                    instance = new SkinResources(context);
                }
            }
        }
    }
    private SkinResources(Context context){
        mAppResources = context.getResources();
    }
    public static SkinResources getInstance(){
        return instance;
    }
    /**
     * 单例 end================================================================================
     */

    /**
     * 宿主app的resources
     */
    private Resources mAppResources;

    /**
     * 皮肤包的resources
     */
    private Resources mSkinResources;

    /**
     * 皮肤包 包名
     */
    private String mSkinPkgName;

    /**
     * 是否使用默认皮肤包
     */
    private boolean isDefaultSkin = true;

    /**
     * 复位
     */
    public void reset(){
        mSkinResources = null;
        mSkinPkgName = "";
        isDefaultSkin = true;
    }

    /**
     * 应用皮肤包
     */
    public void applySkin(Resources resources,String pkgName){
        mSkinResources = resources;
        mSkinPkgName = pkgName;
        //是否使用默认皮肤
        isDefaultSkin = TextUtils.isEmpty(pkgName) || resources == null;
    }

    /**
     * 1.根据宿主app的resId(R.color.xxx)来获取自己的名字
     * 2.根据步骤1获取到的名字在皮肤包中获取对应的id
     * @param resId
     * @return
     */
    public int getIdentifier(int resId){
        if(isDefaultSkin){
            return resId;
        }
        String resName = mAppResources.getResourceEntryName(resId);
        String resType = mAppResources.getResourceTypeName(resId);
        int skinId = mSkinResources.getIdentifier(resName,resType,mSkinPkgName);
        return skinId;
    }

    /**
     * 根据宿主app的id获取皮肤包app的颜色
     * @param resId
     * @return
     */
    public int getColor(int resId){
        if(isDefaultSkin){
            return mAppResources.getColor(resId);
        }

        int skinId = getIdentifier(resId);
        if(skinId == 0){
            return mAppResources.getColor(resId);
        }
        return mSkinResources.getColor(skinId);
    }

    /**
     * 根据宿主app的id获取皮肤包app的ColorStateList
     * @param resId
     * @return
     */
    public ColorStateList getColorStateList(int resId){
        if(isDefaultSkin){
            return mAppResources.getColorStateList(resId);
        }

        int skinId = getIdentifier(resId);
        if(skinId == 0){
            return mAppResources.getColorStateList(resId);
        }
        return mSkinResources.getColorStateList(skinId);
    }

    /**
     * 根据宿主app的id获取皮肤包app的drawable
     * @param resId
     * @return
     */
    public Drawable getDrawable(int resId){
        if(isDefaultSkin){
            return mAppResources.getDrawable(resId);
        }

        int skinId = getIdentifier(resId);
        if(skinId == 0){
            return mAppResources.getDrawable(resId);
        }
        return mSkinResources.getDrawable(skinId);
    }

    /**
     * 根据宿主app的id获取皮肤包app的背景  可能是color 或者 drawable
     * @param resId
     * @return
     */
    public Object getBackground(int resId){
        String resourceTypeName = mAppResources.getResourceTypeName(resId);

        if("color".equals(resourceTypeName)){
            return getColor(resId);
        }else {
            return  getDrawable(resId);
        }
    }



}
