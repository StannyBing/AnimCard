package com.stanny.animcardview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by Xiangb on 2018/12/21.
 * 功能：
 */
public class AnimCardItemView extends View implements View.OnTouchListener {
    private AnimCardBean cardBean;//当前数据
    private int cardItemSize;//数据总长度
    private int position;//当前position
    private float itemWidth;//item宽度
    private boolean isSelect = false;//是否被选中

    private Paint bgPaint;//背景画笔
    private Paint linePaint;//线条画笔
    private Paint resPaint;//资源画笔
    private Path bgPath;//背景路径

    private float deformSize;//item的变形长度
    private float viewMargin;//整个view的margin值
    private float animHeight;//动画变化高度
    private float animWidth;//动画变化宽度
    private float itemHeight;//item高度
    private float maxHeight;//最大高度
    private float itemIndex;//item间隔
    private float bitmapAnimRange;//图片动画幅度
    private float animOpenProgress = 0.0f;//开始动画进度
    private float animCloseProgress = 0.0f;//关闭动画进度
    private float animMoveProgress = 0.0f;//跳选的移动动画
    private int animDuring;//动画周期
    private int bgColor;//背景颜色

    private boolean isAutoSelect = true;//是否自动选择

    private Bitmap bitmap;
    private float bitmapX;
    private float bitmapY;

    private Context context;

    private AnimCardBuilder animCardBuilder;

    private AnimActionListener actionListener;

    private int[] defaultColorBg = new int[]{R.color.defaultColor1, R.color.defaultColor2, R.color.defaultColor3, R.color.defaultColor4, R.color.defaultColor5};

    public AnimCardItemView(Context context, int position, AnimCardBuilder animCardBuilder, float itemWidth, float maxHeight) {
        super(context);
        this.cardBean = animCardBuilder.getAnimResouce().get(position);
        this.cardItemSize = animCardBuilder.getAnimResouce().size();
        this.position = position;
        this.itemWidth = itemWidth;
        this.maxHeight = maxHeight;
        animWidth = itemWidth * 2f;
        this.animCardBuilder = animCardBuilder;
        this.context = context;
        this.animCardBuilder = animCardBuilder;
        if (cardBean.getBgColor() == -1) {
            bgColor = defaultColorBg[(int) (Math.random() * 5)];
        } else {
            bgColor = cardBean.getBgColor();
        }
        init();
    }

    private void init() {
        bgPaint = new Paint();
        linePaint = new Paint();
        resPaint = new Paint();
        bgPath = new Path();

        deformSize = animCardBuilder.getDeformSize();
        viewMargin = animCardBuilder.getViewMargin();
        itemHeight = animCardBuilder.getItemHeight() > maxHeight ? maxHeight : animCardBuilder.getItemHeight();
        itemIndex = animCardBuilder.getItemIndex();
        animDuring = animCardBuilder.getAnimDuring();
        animHeight = animCardBuilder.getAnimHeight();
        if (animCardBuilder.getBitmapAnimRange() < 1) {
            bitmapAnimRange = 1.0f;
        } else if (animCardBuilder.getBitmapAnimRange() > 1.6) {
            bitmapAnimRange = 2.0f;
        } else {
            bitmapAnimRange = animCardBuilder.getBitmapAnimRange();
        }
        if (cardBean.getAnimItemBg() != -1) {
            bitmap = BitmapFactory.decodeResource(getResources(), cardBean.getAnimItemBg());
            bitmap = resizeBitmap(bitmap, (int) itemHeight, (int) itemHeight);
        }

        if (animCardBuilder.getDefaultSelectItem() == position) {
            isSelect = true;
            animOpenProgress = 1.0f;
            animMoveProgress = 1.0f;
        }

        setOnTouchListener(this);
    }

    public float getAnimOpenProgress() {
        return animOpenProgress;
    }

    public void setAnimOpenProgress(float animOpenProgress) {
        this.animOpenProgress = animOpenProgress;
        invalidate();
    }

    public float getAnimCloseProgress() {
        return animCloseProgress;
    }

    public void setAnimCloseProgress(float animCloseProgress) {
        this.animCloseProgress = animCloseProgress;
        invalidate();
    }

    public float getAnimMoveProgress() {
        return animMoveProgress;
    }

    public void setAnimMoveProgress(float animMoveProgress) {
        this.animMoveProgress = animMoveProgress;
        invalidate();
    }

    public void setActionListener(AnimActionListener actionListener) {
        this.actionListener = actionListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //初始化背景绘笔
        bgPaint.setColor(ContextCompat.getColor(context, bgColor));
        bgPaint.setAntiAlias(true);
        bgPaint.setStrokeJoin(Paint.Join.ROUND);
        bgPaint.setStyle(Paint.Style.FILL);

        //初始化线条绘笔
        linePaint.setColor(ContextCompat.getColor(context, animCardBuilder.getStrokeColor()));
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setStrokeJoin(Paint.Join.ROUND);
        linePaint.setStrokeWidth(animCardBuilder.getStrokeWidth());
//        linePaint.setShadowLayer(4, 3, 5, 0xFFA9A9A9);

        //
//        setLayerType(LAYER_TYPE_SOFTWARE, linePaint);

        bgPath.reset();

        PointF[] viewPointArray = getViewPointArray();
        PointF[] animPointArray = getAnimPointArray();

        initPointPath(viewPointArray, animPointArray);

        //绘制边线
        if (isSelect) {
            canvas.save();
            canvas.drawPath(bgPath, linePaint);
            canvas.restore();
        }
        //绘制图形
        canvas.save();
        canvas.drawPath(bgPath, bgPaint);
        canvas.restore();
        //绘制图案
        canvas.save();
        drawBitmap(canvas, viewPointArray);
        canvas.restore();
    }

    /**
     * 绘制图案
     *
     * @param canvas
     * @param viewPointArray
     */
    private void drawBitmap(Canvas canvas, PointF[] viewPointArray) {
        if (bitmap == null) {
            return;
        }
        float animSize = itemWidth * 1.5f / 2f;//空隙长度的一半
        if (isSelect) {
            if (animOpenProgress < 0.5) {
                canvas.clipPath(bgPath);//裁剪绘制区域
            }
            float bitmapX;
            if (position < animCardBuilder.getLastSelectPosition() ||
                    (position == animCardBuilder.getLastSelectPosition() && position == animCardBuilder.getNowSelectPosition()) &&
                            (position != animCardBuilder.getDefaultSelectItem() || position == 0)) {
                bitmapX = viewPointArray[1].x - deformSize / 2 - itemWidth / 2 - itemHeight / 2 + animSize * animOpenProgress;
            } else {
                bitmapX = viewPointArray[1].x - deformSize / 2 - itemHeight / 2 - itemWidth / 2 - animSize * animOpenProgress;
            }
            float bitmapY = viewPointArray[1].y;
            canvas.scale(1 + (bitmapAnimRange - 1.0f) * animOpenProgress, 1 + (bitmapAnimRange - 1.0f) * animOpenProgress,
                    bitmapX + bitmap.getWidth() / 2,
                    bitmapY + bitmap.getHeight() / 2);
            canvas.drawBitmap(bitmap, bitmapX, bitmapY, resPaint);
        } else {
            if (animCloseProgress < 0.5) {
                canvas.clipPath(bgPath);//裁剪绘制区域
            }
            float bitmapX;
            if (position < animCardBuilder.getNowSelectPosition()) {
                bitmapX = viewPointArray[1].x - deformSize / 2 - itemHeight / 2 - itemWidth / 2 + animSize * animCloseProgress;
            } else if (position > animCardBuilder.getNowSelectPosition()) {
                bitmapX = viewPointArray[1].x - deformSize / 2 - itemHeight / 2 - itemWidth / 2 - animSize * animCloseProgress;
            } else {
                bitmapX = viewPointArray[1].x - deformSize / 2 - itemHeight / 2 - itemWidth / 2;
            }
            float bitmapY = viewPointArray[0].y;
            canvas.scale(1 + (bitmapAnimRange - 1.0f) * animCloseProgress, 1 + (bitmapAnimRange - 1.0f) * animCloseProgress,
                    bitmapX + bitmap.getWidth() / 2,
                    bitmapY + bitmap.getHeight() / 2);
            canvas.drawBitmap(bitmap, bitmapX, bitmapY, resPaint);
        }
    }

    /**
     * 初始化点绘制路径
     *
     * @param viewPointArray
     * @param animPointArray
     */
    private void initPointPath(PointF[] viewPointArray, PointF[] animPointArray) {
        if (position == 0) {
            bgPath.moveTo(viewPointArray[0].x + animPointArray[0].x, viewPointArray[0].y + animPointArray[0].y + animHeight);
            bgPath.lineTo(viewPointArray[0].x + animPointArray[0].x + animHeight / 2, viewPointArray[0].y + animPointArray[0].y + animHeight);
            bgPath.lineTo(viewPointArray[0].x + animPointArray[0].x + animHeight / 2, viewPointArray[0].y + animPointArray[0].y + animHeight / 2);
            bgPath.lineTo(viewPointArray[0].x + animPointArray[0].x + animHeight, viewPointArray[0].y + animPointArray[0].y + animHeight / 2);
            bgPath.lineTo(viewPointArray[0].x + animPointArray[0].x + animHeight, viewPointArray[0].y + animPointArray[0].y);
        } else {
            bgPath.moveTo(viewPointArray[0].x + animPointArray[0].x, viewPointArray[0].y + animPointArray[0].y);
        }
        bgPath.lineTo(viewPointArray[1].x + animPointArray[1].x, viewPointArray[1].y + animPointArray[1].y);
        if (position == cardItemSize - 1) {
            bgPath.lineTo(viewPointArray[2].x + animPointArray[2].x, viewPointArray[2].y + animPointArray[2].y - animHeight);
            bgPath.lineTo(viewPointArray[2].x + animPointArray[2].x - animHeight / 2, viewPointArray[2].y + animPointArray[2].y - animHeight);
            bgPath.lineTo(viewPointArray[2].x + animPointArray[2].x - animHeight / 2, viewPointArray[2].y + animPointArray[2].y - animHeight / 2);
            bgPath.lineTo(viewPointArray[2].x + animPointArray[2].x - animHeight, viewPointArray[2].y + animPointArray[2].y - animHeight / 2);
            bgPath.lineTo(viewPointArray[2].x + animPointArray[2].x - animHeight, viewPointArray[2].y + animPointArray[2].y);
        } else {
            bgPath.lineTo(viewPointArray[2].x + animPointArray[2].x, viewPointArray[2].y + animPointArray[2].y);
        }
        bgPath.lineTo(viewPointArray[3].x + animPointArray[3].x, viewPointArray[3].y + animPointArray[3].y);
        bgPath.close();
    }

    /**
     * 压缩bitmap
     *
     * @param bitmap
     * @param w
     * @param h
     * @return
     */
    public static Bitmap resizeBitmap(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float scaleWidth = ((float) w) / width;
        float scaleHeight = ((float) h) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        return Bitmap.createBitmap(bitmap, 0, 0, width,
                height, matrix, true);
    }

    /**
     * 获取view的绘制路径点集
     *
     * @return
     */
    public PointF[] getViewPointArray() {
        float placeSize = 0f;//空白区域;
        if (animCardBuilder.getNowSelectPosition() > animCardBuilder.getLastSelectPosition()) {//顺序展开，向右
            if (position >= animCardBuilder.getNowSelectPosition()) {
                placeSize = itemWidth * 1.5f;
            }
            if (position < animCardBuilder.getNowSelectPosition() && position > animCardBuilder.getLastSelectPosition()) {
                placeSize = itemWidth * 1.5f * (1 - animMoveProgress);
            }
        } else if (animCardBuilder.getNowSelectPosition() < animCardBuilder.getLastSelectPosition()) {//倒序展开，向左
            if (position > animCardBuilder.getNowSelectPosition()) {
                placeSize = itemWidth * 1.5f;
            }
            if (position > animCardBuilder.getNowSelectPosition() && position < animCardBuilder.getLastSelectPosition()) {
                placeSize = itemWidth * 1.5f * animMoveProgress;
            }
        } else {
            if (position >= animCardBuilder.getNowSelectPosition()) {
                placeSize = itemWidth * 1.5f;
            }
        }

        PointF[] pathPointArray = new PointF[4];
        if (position == 0) {
            pathPointArray[0] = new PointF(viewMargin, viewMargin);
            pathPointArray[1] = new PointF(viewMargin + deformSize + itemWidth, viewMargin);
            pathPointArray[2] = new PointF(viewMargin + itemWidth, viewMargin + itemHeight);
            pathPointArray[3] = new PointF(viewMargin, viewMargin + itemHeight);
        } else if (position == cardItemSize - 1) {
            pathPointArray[0] = new PointF(viewMargin + deformSize + itemWidth * position + itemIndex * position + placeSize, viewMargin);
            pathPointArray[1] = new PointF(viewMargin + deformSize + itemWidth * (position + 1) + itemIndex * position + placeSize, viewMargin);
            pathPointArray[2] = new PointF(viewMargin + deformSize + itemWidth * (position + 1) + itemIndex * position + placeSize, viewMargin + itemHeight);
            pathPointArray[3] = new PointF(viewMargin + itemWidth * position + itemIndex * position + placeSize, viewMargin + itemHeight);
        } else {
            pathPointArray[0] = new PointF(viewMargin + deformSize + itemWidth * position + itemIndex * position + placeSize, viewMargin);
            pathPointArray[1] = new PointF(viewMargin + deformSize + itemWidth * (position + 1) + itemIndex * position + placeSize, viewMargin);
            pathPointArray[2] = new PointF(viewMargin + itemWidth * (position + 1) + itemIndex * position + placeSize, viewMargin + itemHeight);
            pathPointArray[3] = new PointF(viewMargin + itemWidth * position + itemIndex * position + placeSize, viewMargin + itemHeight);
        }
        return pathPointArray;
    }

    /**
     * 获取view的动画变化绘制路径点集
     *
     * @return
     */
    public PointF[] getAnimPointArray() {
        PointF[] pathPointArray = new PointF[4];
        if (isSelect) {
            if (position == 0) {
                pathPointArray[0] = new PointF(-animOpenProgress * (animHeight + (deformSize * animHeight) / itemHeight), -animOpenProgress * animHeight);
                pathPointArray[1] = new PointF(+animOpenProgress * (animWidth + (deformSize * animHeight) / itemHeight), -animOpenProgress * animHeight);
                pathPointArray[2] = new PointF(+animOpenProgress * (animWidth - (deformSize * animHeight) / itemHeight), +animOpenProgress * animHeight);
                pathPointArray[3] = new PointF(-animOpenProgress * (animHeight - (deformSize * animHeight) / itemHeight), +animOpenProgress * animHeight);
            } else if (position == cardItemSize - 1) {
                if (animCardBuilder.getLastSelectPosition() > position) {
                    pathPointArray[0] = new PointF(-animOpenProgress * (animHeight + (deformSize * animHeight) / itemHeight), -animOpenProgress * animHeight);
                    pathPointArray[1] = new PointF(+animOpenProgress * (animWidth - (deformSize * animHeight) / itemHeight), -animOpenProgress * animHeight);
                    pathPointArray[2] = new PointF(+animOpenProgress * (animWidth + (deformSize * animHeight) / itemHeight), +animOpenProgress * animHeight);
                    pathPointArray[3] = new PointF(-animOpenProgress * (animHeight - (deformSize * animHeight) / itemHeight), +animOpenProgress * animHeight);
                } else {
                    pathPointArray[0] = new PointF(-animOpenProgress * (animWidth - (deformSize * animHeight) / itemHeight), -animOpenProgress * animHeight);
                    pathPointArray[1] = new PointF(+animOpenProgress * (animHeight + (deformSize * animHeight) / itemHeight), -animOpenProgress * animHeight);
                    pathPointArray[2] = new PointF(+animOpenProgress * (animHeight - (deformSize * animHeight) / itemHeight), +animOpenProgress * animHeight);
                    pathPointArray[3] = new PointF(-animOpenProgress * (animWidth + (deformSize * animHeight) / itemHeight), +animOpenProgress * animHeight);
                }
            } else {
                if (animCardBuilder.getLastSelectPosition() > position) {
                    pathPointArray[0] = new PointF(-animOpenProgress * (animHeight + (deformSize * animHeight) / itemHeight + animHeight * 2), -animOpenProgress * animHeight);
                    pathPointArray[1] = new PointF(+animOpenProgress * (animWidth - (deformSize * animHeight) / itemHeight - animHeight * 2), -animOpenProgress * animHeight);
                    pathPointArray[2] = new PointF(+animOpenProgress * (animWidth + (deformSize * animHeight) / itemHeight - animHeight * 2), +animOpenProgress * animHeight);
                    pathPointArray[3] = new PointF(-animOpenProgress * (animHeight - (deformSize * animHeight) / itemHeight + animHeight * 2), +animOpenProgress * animHeight);
                } else {
                    pathPointArray[0] = new PointF(-animOpenProgress * (animWidth - (deformSize * animHeight) / itemHeight), -animOpenProgress * animHeight);
                    pathPointArray[1] = new PointF(+animOpenProgress * (animHeight + (deformSize * animHeight) / itemHeight), -animOpenProgress * animHeight);
                    pathPointArray[2] = new PointF(+animOpenProgress * (animHeight - (deformSize * animHeight) / itemHeight), +animOpenProgress * animHeight);
                    pathPointArray[3] = new PointF(-animOpenProgress * (animWidth + (deformSize * animHeight) / itemHeight), +animOpenProgress * animHeight);
                }
            }
        } else {
            if (position == 0) {
                pathPointArray[0] = new PointF(-animCloseProgress * (animHeight + (deformSize * animHeight) / itemHeight), -animCloseProgress * animHeight);
                pathPointArray[1] = new PointF(+animCloseProgress * (animWidth + (deformSize * animHeight) / itemHeight), -animCloseProgress * animHeight);
                pathPointArray[2] = new PointF(+animCloseProgress * (animWidth - (deformSize * animHeight) / itemHeight), +animCloseProgress * animHeight);
                pathPointArray[3] = new PointF(-animCloseProgress * (animHeight - (deformSize * animHeight) / itemHeight), +animCloseProgress * animHeight);
            } else if (position == cardItemSize - 1) {
                if (position <= animCardBuilder.getNowSelectPosition()) {
                    pathPointArray[0] = new PointF(-animCloseProgress * (animHeight + (deformSize * animHeight) / itemHeight + animHeight * 2), -animCloseProgress * animHeight);
                    pathPointArray[1] = new PointF(+animCloseProgress * (animWidth - (deformSize * animHeight) / itemHeight - animHeight * 2), -animCloseProgress * animHeight);
                    pathPointArray[2] = new PointF(+animCloseProgress * (animWidth + (deformSize * animHeight) / itemHeight - animHeight * 2), +animCloseProgress * animHeight);
                    pathPointArray[3] = new PointF(-animCloseProgress * (animHeight - (deformSize * animHeight) / itemHeight + animHeight * 2), +animCloseProgress * animHeight);
                } else {
                    pathPointArray[0] = new PointF(-animCloseProgress * (animWidth - (deformSize * animHeight) / itemHeight), -animCloseProgress * animHeight);
                    pathPointArray[1] = new PointF(+animCloseProgress * (animHeight + (deformSize * animHeight) / itemHeight), -animCloseProgress * animHeight);
                    pathPointArray[2] = new PointF(+animCloseProgress * (animHeight - (deformSize * animHeight) / itemHeight), +animCloseProgress * animHeight);
                    pathPointArray[3] = new PointF(-animCloseProgress * (animWidth + (deformSize * animHeight) / itemHeight), +animCloseProgress * animHeight);
                }
            } else {
                if (position <= animCardBuilder.getNowSelectPosition()) {//从右边缩小
                    pathPointArray[0] = new PointF(-animCloseProgress * (animHeight + (deformSize * animHeight) / itemHeight + animHeight * 2), -animCloseProgress * animHeight);
                    pathPointArray[1] = new PointF(+animCloseProgress * (animWidth - (deformSize * animHeight) / itemHeight - animHeight * 2), -animCloseProgress * animHeight);
                    pathPointArray[2] = new PointF(+animCloseProgress * (animWidth + (deformSize * animHeight) / itemHeight - animHeight * 2), +animCloseProgress * animHeight);
                    pathPointArray[3] = new PointF(-animCloseProgress * (animHeight - (deformSize * animHeight) / itemHeight + animHeight * 2), +animCloseProgress * animHeight);
                } else {//从左边缩小
                    pathPointArray[0] = new PointF(-animCloseProgress * (animWidth - (deformSize * animHeight) / itemHeight), -animCloseProgress * animHeight);
                    pathPointArray[1] = new PointF(+animCloseProgress * (animHeight + (deformSize * animHeight) / itemHeight), -animCloseProgress * animHeight);
                    pathPointArray[2] = new PointF(+animCloseProgress * (animHeight - (deformSize * animHeight) / itemHeight), +animCloseProgress * animHeight);
                    pathPointArray[3] = new PointF(-animCloseProgress * (animWidth + (deformSize * animHeight) / itemHeight), +animCloseProgress * animHeight);
                }
            }
        }

        return pathPointArray;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (pointInPath(bgPath, event.getX(), event.getY())) {
            //animCardBuilder.getLastSelectPosition() == animCardBuilder.getNowSelectPosition()表示动画已完成
            if (!isSelect && animCardBuilder.getLastSelectPosition() == animCardBuilder.getNowSelectPosition()) {
                isAutoSelect = false;
                selectItem(animDuring);
            } else if (position == animCardBuilder.getLastSelectPosition() && animCardBuilder.getLastSelectPosition() == animCardBuilder.getNowSelectPosition()) {
                //已被选中，执行点击事件
                actionListener.onItemClick(position);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 执行选中
     */
    private void selectItem(int animDuring) {
        //未被选中，执行选中动画
        actionListener.onItemSelect(position, isAutoSelect);
        isSelect = true;
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "animOpenProgress", 0.0f, 1.0f);
        animator.setDuration(animDuring);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }

    /**
     * 执行选中
     */
    public void selectItem() {
        isAutoSelect = true;
        selectItem(animDuring);
    }

    /**
     * 取消选中
     */
    public void deSelectItem() {
        //取消选中，执行取消动画
        actionListener.onItemDeselect(position);
        isSelect = false;
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "animCloseProgress", 1.0f, 0.0f);
        animator.setDuration(animDuring);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }

    /**
     * 跳选的移动动画
     */
    public void moveItem() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "animMoveProgress", 0.0f, 1.0f);
        animator.setDuration(animDuring);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }

    /**
     * 判断当前点击的是否在为该view
     *
     * @param path
     * @param x
     * @param y
     * @return
     */
    private boolean pointInPath(Path path, float x, float y) {
        RectF bounds = new RectF();
        path.computeBounds(bounds, true);
        Region region = new Region();
        region.setPath(path, new Region((int) bounds.left, (int) bounds.top, (int) bounds.right, (int) bounds.bottom));
        return region.contains((int) x, (int) y);
    }

    public void refreshStatus() {
        isSelect = false;
        invalidate();
        moveItem();
    }
}
