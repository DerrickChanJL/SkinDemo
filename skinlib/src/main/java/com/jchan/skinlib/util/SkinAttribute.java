package com.jchan.skinlib.util;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.view.ViewCompat;

import com.jchan.skinlib.support.SkinViewSupport;
import com.jchan.skinlib.util.SkinResources;
import com.jchan.skinlib.util.SkinThemeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

/**
 * @create time: 2021/8/5
 * @author: JChan
 * @description: 保存所有需要换肤的view的属性集合，切提供更换属性功能
 */
public class SkinAttribute {
    /**
     * 需要换肤的属性
     */
    private static final List<String> mAttributes = new ArrayList<>();
    static {
        mAttributes.add("background");
        mAttributes.add("src");
        mAttributes.add("textColor");
        mAttributes.add("drawableLeft");
        mAttributes.add("drawableTop");
        mAttributes.add("drawableRight");
        mAttributes.add("drawableBottom");
    }

    /**
     * view集合
     */
    private List<SkinView> mSkinViews = new ArrayList<>();

    /**
     * 对所有view中的所有属性进行皮肤修改
     */
    public void updateAllSkin(){
        for (SkinView skinView:mSkinViews){
            skinView.updateSkin();
        }
    }

    /**
     * 查找一个View的所有需要换肤的属性
     */
    public void findAttributes(View view, AttributeSet attrs){
        List<SkinPair> skinPairs = new ArrayList<>();
        for (int i = 0 ; i < attrs.getAttributeCount(); i++){
            //获得属性名 如background
            String attributeName = attrs.getAttributeName(i);
            if(mAttributes.contains(attributeName)){
                //R.color.xx
                String attributeValue = attrs.getAttributeValue(i);
                if(attributeValue.startsWith("#")){
                    continue;
                }
                int resId;
                //以?开头的表示使用属性
                if(attributeValue.startsWith("?")){
                    int attrId = Integer.parseInt(attributeValue.substring(1));
                    resId = SkinThemeUtils.getResId(view.getContext(),new int[]{attrId})[0];
                    Log.e("Skin","attrId "+attrId+" attributeValue "+attributeValue);
                }else {
                    //正常以@开头
                    Log.e("attributeValue"," attributeValue "+attributeValue);
                    resId = Integer.parseInt(attributeValue.substring(1));
                }
                SkinPair skinPair = new SkinPair(attributeName,resId);
                skinPairs.add(skinPair);
            }
        }
        if(!skinPairs.isEmpty() || view instanceof SkinViewSupport){
            SkinView skinView = new SkinView(view,skinPairs);
            //如果选择过皮肤，调用一次updateSkin加载皮肤资源
            skinView.updateSkin();
            mSkinViews.add(skinView);
        }
    }


    static class SkinView{

        View view;
        //这个view对应的皮肤属性集合
        List<SkinPair> skinPairs;

        public SkinView(View view, List<SkinPair> skinPairs) {
            this.view = view;
            this.skinPairs = skinPairs;
        }

        /**
         * 对一个view中的所有需要换肤的属性修改
         */
        public void updateSkin(){
            updateSupportSkin();
            for (SkinPair skinPair:skinPairs){
                Drawable left = null,top = null,right = null,bottom = null;
                Log.e("attribute",skinPair.attributeName);
                switch (skinPair.attributeName){
                    case "background":
                        Object background = SkinResources.getInstance().getBackground(skinPair.resId);
                        //background 有可能是@drawable 或者 @color
                        if(background instanceof Integer){
                            view.setBackgroundColor((Integer) background);
                        }else {
                            ViewCompat.setBackground(view, (Drawable) background);
                        }
                        break;
                    case "src":
                        background = SkinResources.getInstance().getBackground(skinPair.resId);
                        if(background instanceof Integer){
                            ((ImageView)view).setImageDrawable(new ColorDrawable((Integer) background));
                        }else {
                            ((ImageView)view).setImageDrawable((Drawable) background);
                        }
                        view.setBackground(SkinResources.getInstance().getDrawable(skinPair.resId));
                        break;
                    case "textColor":
                        ((TextView)view).setTextColor(SkinResources.getInstance().getColorStateList(skinPair.resId));
                        break;
                    case "drawableLeft":
                        left = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;
                    case "drawableTop":
                        top = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;
                    case "drawableRight":
                        right = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;
                    case "drawableBottom":
                        bottom = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + skinPair.attributeName);
                }
                if(null != left || null != right || null != top || null != bottom){
                    ((TextView)view).setCompoundDrawablesWithIntrinsicBounds(left,top,right,bottom);
                }
            }
        }

        /**
         * 支持自定义控件换肤
         */
        private void updateSupportSkin() {
            if(view instanceof SkinViewSupport){
                ((SkinViewSupport)view).applySkin();
            }
        }
    }

    static class SkinPair{
        /**
         * 属性名
         */
        String attributeName;
        /**
         * 对应的资源id
         */
        int resId;

        public SkinPair(String attributeName, int resId) {
            this.attributeName = attributeName;
            this.resId = resId;
        }
    }
}
