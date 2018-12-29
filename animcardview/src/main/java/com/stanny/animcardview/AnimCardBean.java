package com.stanny.animcardview;

import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;

/**
 * Created by Xiangb on 2018/12/21.
 * 功能：
 */
public class AnimCardBean {
    private int bgColor = -1;
    private int animItemBg = -1;

    public AnimCardBean() {
    }

    public AnimCardBean(@ColorRes int bgColor, @DrawableRes int animItemBg) {
        this.bgColor = bgColor;
        this.animItemBg = animItemBg;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(@ColorInt int bgColor) {
        this.bgColor = bgColor;
    }

    public int getAnimItemBg() {
        return animItemBg;
    }

    public void setAnimItemBg(@IdRes int animItemBg) {
        this.animItemBg = animItemBg;
    }
}
