package com.stanny.animcard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.stanny.animcardview.AnimActionListener;
import com.stanny.animcardview.AnimCardBean;
import com.stanny.animcardview.AnimCardBuilder;
import com.stanny.animcardview.AnimCardView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AnimCardView animCardView;
    private List<AnimCardBean> animCardBeans = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    }

}
