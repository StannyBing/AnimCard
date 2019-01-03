package com.stanny.animcardview;

/**
 * Created by Xiangb on 2018/12/21.
 * 功能：
 */
public interface AnimActionListener {

    void onItemSelect(int position, boolean isAutoSelect);

    void onItemDeselect(int position);

    void onItemClick(int position);

}
