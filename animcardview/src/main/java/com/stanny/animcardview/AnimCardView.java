package com.stanny.animcardview;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

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

        setWillNotDraw(false);

    }

    public void buildView(AnimCardBuilder animCardBuilder) {
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
    }

    /**
     * 手动选中指定item
     *
     * @param position
     */
    public void selectItem(int position) {
        if (position < childViews.size() && position > 0) {
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
        public void onItemSelect(final int position) {
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
                actionListener.onItemSelect(position);
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
