package com.yzc35326.runningassistant.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.yzc35326.runningassistant.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabView extends FrameLayout {
    private ImageView iv_icon, iv_icon_select;
    private TextView tv_title;
    private static final int COLOR_DEFAULT = Color.parseColor("#ff000000");
    private static final int COLOR_SELECT = Color.parseColor("#ff45C01A");


    public TabView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.tab_view, this);
        initView();
    }

    private void initView() {
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        iv_icon_select = (ImageView) findViewById(R.id.iv_icon_select);
        tv_title = (TextView) findViewById(R.id.tv_title);
        setProgress(0);
    }

    //设置icon.text
    //方式一：抽取自定义属性，通过xml设置
    //方式二：直接对外开放方法设置
    public void setIconAndText( int icon, int iconSelect, String text){
        iv_icon.setImageResource(icon);
        iv_icon_select.setImageResource(iconSelect);
        tv_title.setText(text);
    }

    public void setProgress(float progress) {
        this.iv_icon.setAlpha(1 - progress);
        this.iv_icon_select.setAlpha(progress);
        //this.tv_title
        this.tv_title.setTextColor(evaluate(progress, COLOR_DEFAULT, COLOR_SELECT));
    }

    public int evaluate(float fraction, int startValue, int endValue) {
        int startInt = (Integer) startValue;
        int startA = ((startInt >> 24) & 0xff);
        int startR = ((startInt >> 16) & 0xff);///255.0f;
        int startG = ((startInt >> 8) & 0xff);///255.0f;
        int startB = (startInt & 0xff);//255.0f;

        int endInt = (Integer) endValue;
        int endA = ((endInt >> 24) & 0xff);//255.0f;
        int endR = ((endInt >> 16) & 0xff);//255.0f;
        int endG = ((endInt >> 8) & 0xff);//255.0f;
        int endB = (endInt & 0xff);//255.0f;

        return (int) ((startA + (int) (fraction * (endA - startA))) << 24) |
                (int) ((startR + (int) (fraction * (endR - startR))) << 16) |
                (int) ((startG + (int) (fraction * (endG - startG))) << 8) |
                (int) ((startB + (int) (fraction * (endB - startB))));
//        //convert from sRGB to linear
//        startR=(float)Math.pow(startR,2.2);
//        startG=(float)Math.pow(startG,2.2);
//        startB=(float)Math.pow(startB,2.2);
//
//        endR=(float)Math.pow(endR,2.2);
//        endG=(float)Math.pow(endG,2.2);
//        endB=(float)Math.pow(endB,2.2);
//
//        //compute the interpolated color in linear space
//        float a=startA + fraction *(endA-startA);
//        float r=startR+fraction*(endR-startR);
//        float g=startG+fraction*(endG-startG);
//        float b = startB+fraction*(endB-startB);
//
//        //convert back to sRGB in the[0...255] range
//        a=a*255.0f;
//        r=(float)Math.pow(r,1.0/2.2)*255.0f;
//        g=(float)Math.pow(g,1.0/2.2)*255.0f;
//        b=(float)Math.pow(b,1.0/2.2)*255.0f;
//
//        return Math.round(a)<<24|Math.round(r)<<16|Math.round(g)<<8
    }
}
