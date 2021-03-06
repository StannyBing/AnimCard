AnimCard<br>
![示例](https://github.com/StannyBing/AnimCard/blob/master/demo.gif) <br>
使用方式：<br>
# 引用依赖<br>
```
compile 'com.github.StannyBing:AnimCard:1.0.1'
```

# 添加控件<br>
 添加下列代码到xml中<br>
```
 <com.stanny.animcardview.AnimCardView
         android:id="@+id/anim_card_view"
         android:layout_marginTop="50dp"
         android:layout_width="match_parent"
         android:layout_height="match_parent"/>
```
 <br>
 
 # 控件初始化<br>
 ```
 animCardView = findViewById(R.id.anim_card_view);
         AnimCardBuilder animCardBuilder = new AnimCardBuilder();
         animCardBeans.add(new AnimCardBean(R.color.defaultColor1, R.drawable.op1));
         animCardBeans.add(new AnimCardBean(R.color.defaultColor2, R.drawable.op2));
         animCardBeans.add(new AnimCardBean(R.color.defaultColor3, R.drawable.op3));
         animCardBeans.add(new AnimCardBean(R.color.defaultColor4, R.drawable.op4));
         animCardBeans.add(new AnimCardBean(R.color.defaultColor5, R.drawable.op5));
         animCardBuilder.setAnimResouce(animCardBeans)
                 .setAutoSelectPeriod(1500);
         animCardView.buildView(animCardBuilder);
         animCardView.setAutoSelectable(true);
         animCardView.setAnimActionListener(new AnimActionListener() {
             @Override
             public void onItemSelect(int position,  boolean isAutoSelect) {

             }

             @Override
             public void onItemDeselect(int position) {

             }

             @Override
             public void onItemClick(int position) {
                 Toast.makeText(MainActivity.this, "当前选中:" + position, Toast.LENGTH_SHORT).show();
             }
         });

```

 <br>
# 所有的参数设置都是通过AnimCardBuider进行初始化<br>
 AnimCardBuider包含了以下参数设置：<br>
 setDeformSize//设置item的倾斜长度<br>
 setViewMargin//设置view的margin<br>
 setItemHeight//设置item的高度<br>
 setItemIndex//设置item之间的间隔<br>
 setAnimDuring//设置动画的时长<br>
 setAutoSelectPeriod//设置自动切换周期<br>
 setStrokeColor//设置边框颜色<br>
 setStrokeWidth//设置边框宽度<br>
 setBitmapAnimRange//设置图片的动画幅度，1f~1.6f<br>
 setAnimResouce//设置资源，最低为5条，不够将由代码补足五条<br>
 setDefaultSelectItem//设置默认选中的item<br>

 <br>
 ###AnimCardView包含了以下方法<br>
 buildView//构建view<br>
 setAutoSelectable//设置自动切换<br>
 selectItem//选中item<br>
 setAnimActionListener//设置监听<br>
 
# 注意事项：<br>
 传入资源时，需要注意，传入的图片资源需要为背景透明的png图片，图片内容与图片大小的比例不能差距太大，图片资源尽量为正方形<br>
