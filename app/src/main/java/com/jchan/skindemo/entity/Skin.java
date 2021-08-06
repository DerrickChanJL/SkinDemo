package com.jchan.skindemo.entity;

/**
 * @create time: 2021/8/6
 * @author: JChan
 * @description:
 */
public class Skin {

    private String title;
    private String path;
    private int resId;

    public Skin(){}

    public Skin(String title, String path, int resId) {
        this.title = title;
        this.path = path;
        this.resId = resId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
