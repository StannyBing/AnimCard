package com.stanny.animcardview;

import android.support.annotation.ColorRes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xiangb on 2018/12/24.
 * 功能：
 */
public class AnimCardBuilder {
    private float deformSize = 50;//item的变形长度
    private float viewMargin = 80;//整个view的margin值
    private float animHeight = 20;//动画变化高度
    private float itemHeight = 500;//item高度
    private float itemIndex = 5;//item间隔
    private float strokeWidth = 12;//线条宽度
    private float bitmapAnimRange = 1.3f;//图片的动画幅度
    private int strokeColor = R.color.gold;//边框颜色
    private int animDuring = 300;//动画周期
    private int autoSelectPeriod = 2000;//自动切换的动画周期
    private int nowSelectPosition = 0;//当前选中的item
    private int lastSelectPosition = 0;//上一个选中的item
    private int defaultSelectItem = -1;//默认选中item
    private List<AnimCardBean> animResouce = new ArrayList<>();//资源

    public float getDeformSize() {
        return deformSize;
    }

    /**
     * 设置item所在平行四边形的偏离宽度
     *
     * @param deformSize
     * @return
     */
    public AnimCardBuilder setDeformSize(float deformSize) {
        this.deformSize = deformSize;
        return this;
    }

    public float getViewMargin() {
        return viewMargin;
    }

    /**
     * 设置与父view与周围的margin值，该值与普通的setMargin不同
     *
     * @param viewMargin
     * @return
     */
    public AnimCardBuilder setViewMargin(float viewMargin) {
        this.viewMargin = viewMargin;
        return this;
    }

    public float getAnimHeight() {
        return animHeight;
    }

    public float getItemHeight() {
        return itemHeight;
    }

    /**
     * 设置item高度
     *
     * @param itemHeight
     * @return
     */
    public AnimCardBuilder setItemHeight(float itemHeight) {
        this.itemHeight = itemHeight;
        return this;
    }

    public float getItemIndex() {
        return itemIndex;
    }

    /**
     * 设置item间距
     *
     * @param itemIndex
     * @return
     */
    public AnimCardBuilder setItemIndex(float itemIndex) {
        this.itemIndex = itemIndex;
        return this;
    }

    public int getAnimDuring() {
        return animDuring;
    }

    /**
     * 设置动画周期
     *
     * @param animDuring
     * @return
     */
    public AnimCardBuilder setAnimDuring(int animDuring) {
        this.animDuring = animDuring;
        return this;
    }

    public int getAutoSelectPeriod() {
        return autoSelectPeriod;
    }

    /**
     * 自动切换的动画周期
     * @param autoSelectPeriod
     */
    public AnimCardBuilder setAutoSelectPeriod(int autoSelectPeriod) {
        this.autoSelectPeriod = autoSelectPeriod;
        return this;
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    /**
     * 设置边框颜色
     *
     * @param strokeColor
     */
    public AnimCardBuilder setStrokeColor(@ColorRes int strokeColor) {
        this.strokeColor = strokeColor;
        return this;
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    /**
     * 设置边框宽度
     *
     * @param strokeWidth
     */
    public AnimCardBuilder setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        return this;
    }

    public int getLastSelectPosition() {
        return lastSelectPosition;
    }

    /**
     * 请勿使用
     *
     * @param lastSelectPosition
     */
    public AnimCardBuilder setLastSelectPosition(int lastSelectPosition) {
        this.lastSelectPosition = lastSelectPosition;
        return this;
    }

    public int getNowSelectPosition() {
        return nowSelectPosition;
    }

    /**
     * 请勿使用
     *
     * @param nowSelectPosition
     */
    public AnimCardBuilder setNowSelectPosition(int nowSelectPosition) {
        this.nowSelectPosition = nowSelectPosition;
        return this;
    }

    public float getBitmapAnimRange() {
        return bitmapAnimRange;
    }

    /**
     * 设置图片动画幅度
     *
     * @param bitmapAnimRange 1f~1.6f
     */
    public AnimCardBuilder setBitmapAnimRange(float bitmapAnimRange) {
        this.bitmapAnimRange = bitmapAnimRange;
        return this;
    }

    /**
     * 设置资源
     * 长度随意，但低于5时，会默认添加颜色值
     *
     * @param animResouce
     */
    public AnimCardBuilder setAnimResouce(List<AnimCardBean> animResouce) {
        if (animResouce.size() < 5) {
            for (int i = 0; i < 5 - animResouce.size(); i++) {
                animResouce.add(new AnimCardBean());
            }
        }
        this.animResouce.clear();
        this.animResouce.addAll(animResouce);
        return this;
    }

    public List<AnimCardBean> getAnimResouce() {
        return animResouce;
    }

    public int getDefaultSelectItem() {
        return defaultSelectItem;
    }

    /**
     * 设置默认选中item
     *
     * @param defaultSelectItem
     */
    public AnimCardBuilder setDefaultSelectItem(int defaultSelectItem) {
        this.defaultSelectItem = defaultSelectItem;
        this.nowSelectPosition = defaultSelectItem;
        this.lastSelectPosition = defaultSelectItem;
        return this;
    }
}
