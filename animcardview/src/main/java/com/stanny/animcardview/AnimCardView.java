package com.stanny.animcardview;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Xiangb on 2018/12/21.
 * 功能：
 */
public class AnimCardView extends FrameLayout {

    private Context context;

    private boolean viewLoad = false;
    private List<AnimCardItemView> childViews = new ArrayList<>();//view集合
    private float deformSize;//item的变形长度
    private float viewMargin;//整个view的margin值
    private float itemIndex;//item间隔
    private boolean isAnimAction = false;//是否正在运行动画
    private AnimActionListener actionListener;
    private Timer timer;//自动切换

    private boolean autoSelectable = true;//是否自动切换
    private AnimCardBuilder animCardBuilder;

    public AnimCardView(Context context) {
        this(context, null);
    }

    public AnimCardView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimCardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;

        animCardBuilder = new AnimCardBuilder();

        deformSize = animCardBuilder.getDeformSize();
        viewMargin = animCardBuilder.getViewMargin();
        itemIndex = animCardBuilder.getItemIndex();

        timer = new Timer();

        setWillNotDraw(false);

    }

    /**
     * 构建view
     *
     * @param animCardBuilder
     */
    public void buildView(final AnimCardBuilder animCardBuilder) {
        this.animCardBuilder = animCardBuilder;
        deformSize = animCardBuilder.getDeformSize();
        viewMargin = animCardBuilder.getViewMargin();
        itemIndex = animCardBuilder.getItemIndex();

        viewLoad = false;

        //如果没有设置过默认值，设置第一个为默认选中
        if (animCardBuilder.getDefaultSelectItem() == -1) {
            animCardBuilder.setDefaultSelectItem(0);
        }
        invalidate();
        runAutoSelect();
    }

    /**
     * 开始自动切换
     */
    private void runAutoSelect() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                autoSelectHandler.sendEmptyMessage(0);
            }
        }, animCardBuilder.getAutoSelectPeriod() + animCardBuilder.getAnimDuring(), animCardBuilder.getAutoSelectPeriod() + animCardBuilder.getAnimDuring());
    }

    /**
     * 设置自动切换，默认为true
     *
     * @param autoSelectable
     */
    public void setAutoSelectable(boolean autoSelectable) {
        this.autoSelectable = autoSelectable;
    }

    private Handler autoSelectHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (autoSelectable) {
                selectItem(animCardBuilder.getNowSelectPosition() == (animCardBuilder.getAnimResouce().size() - 1) ? 0 : (animCardBuilder.getNowSelectPosition() + 1));
            }
        }
    };

    /**
     * 手动选中指定item
     *
     * @param position
     */
    public void selectItem(int position) {
        if (position < childViews.size() && position >= 0) {
            childViews.get(position).selectItem();
        }
    }

    public void setAnimActionListener(AnimActionListener actionListener) {
        this.actionListener = actionListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (animCardBuilder.getAnimResouce().size() == 0 || viewLoad) {
            return;
        }
        removeAllViews();
        float itemWidth = (getWidth() - viewMargin * 2 - deformSize + itemIndex) / (animCardBuilder.getAnimResouce().size() + 1.5f) - itemIndex;
        float maxHeight = getHeight() - viewMargin * 4;
        for (int i = 0; i < animCardBuilder.getAnimResouce().size(); i++) {
            AnimCardItemView itemView = new AnimCardItemView(context, i, animCardBuilder, itemWidth, maxHeight);
            itemView.setActionListener(innerActionListener);
            addView(itemView);
            childViews.add(itemView);
        }
        bringChildToFront(getChildAt(animCardBuilder.getDefaultSelectItem()));
        viewLoad = true;
    }

    private AnimActionListener innerActionListener = new AnimActionListener() {
        @Override
        public void onItemSelect(final int position, boolean isAutoSelet) {
            isAnimAction = true;
            childViews.get(animCardBuilder.getLastSelectPosition()).deSelectItem();//关闭之前的选中
            for (AnimCardItemView childView : childViews) {
                if (childView != childViews.get(position)) {
                    childView.refreshStatus();
                }
            }
            bringChildToFront(childViews.get(position));
            animCardBuilder.setNowSelectPosition(position);
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    animCardBuilder.setLastSelectPosition(position);
                    isAnimAction = false;
                    //该段代码是为了防止息屏或者其他方式引起的界面重置刷新后导致的图片位置更改的问题
                    //我不想改了。。。所以。。。
                    animCardBuilder.setDefaultSelectItem(position);
                }
            }, animCardBuilder.getAnimDuring() + 100);
            if (actionListener != null) {
                actionListener.onItemSelect(position, isAutoSelet);
            }
            //如果不是自动选中，且此时开启了自动切换，那么当手动选中之后，暂停几秒后再执行自动选中
            if (!isAutoSelet && autoSelectable) {
                timer.cancel();
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        runAutoSelect();
                    }
                }, animCardBuilder.getAutoSelectPeriod());
            }
        }

        @Override
        public void onItemDeselect(int position) {
            if (actionListener != null) {
                actionListener.onItemDeselect(position);
            }
        }

        @Override
        public void onItemClick(int position) {
            if (actionListener != null) {
                actionListener.onItemClick(position);
            }
        }
    };

}
