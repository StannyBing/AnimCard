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
