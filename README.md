# AnimCard<br>
动画展示卡片<br>
<div align=center><img width="324" height="576" src="https://github.com/StannyBing/AnimCard/blob/master/demo.gif"/><br>
使用方式：<br>
 ###引用依赖<br>
 ```
    compile 'com.github.StannyBing:AnimCard:1.0.1'
 ```
 <br>
 ###添加控件<br>
 ```
 <com.stanny.animcardview.AnimCardView
         android:id="@+id/anim_card_view"
         android:layout_marginTop="50dp"
         android:layout_width="match_parent"
         android:layout_height="match_parent"/>
 ```
 <br>
 ###控件初始化<br>
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
 ###所有的参数设置都是通过AnimCardBuider进行初始化<br>
 AnimCardBuider包含了以下参数设置：<br>
 ```
 setDeformSize//设置item的倾斜长度
 setViewMargin//设置view的margin
 setItemHeight//设置item的高度
 setItemIndex//设置item之间的间隔
 setAnimDuring//设置动画的时长
 setAutoSelectPeriod//设置自动切换周期
 setStrokeColor//设置边框颜色
 setStrokeWidth//设置边框宽度
 setBitmapAnimRange//设置图片的动画幅度，1f~1.6f
 setAnimResouce//设置资源，最低为5条，不够将由代码补足五条
 setDefaultSelectItem//设置默认选中的item
 ```
 <br>
 ###AnimCardView包含了以下方法<br>
 ```
 buildView//构建view
 setAutoSelectable//设置自动切换
 selectItem//选中item
 setAnimActionListener//设置监听
 ```

 注意事项：<br>
 传入资源时，需要注意，传入的图片资源需要为背景透明的png图片，图片内容与图片大小的比例不能差距太大，图片资源尽量为正方形<br>