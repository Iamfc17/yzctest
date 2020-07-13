package com.yzc35326.runningassistant.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.yzc35326.runningassistant.R;
import com.yzc35326.runningassistant.fragment.ListFragmentMe;
import com.yzc35326.runningassistant.fragment.ListFragmentNav;
import com.yzc35326.runningassistant.fragment.ListFragmentSchool;
import com.yzc35326.runningassistant.fragment.TabFragment;
import com.yzc35326.runningassistant.util.LogUtil;
import com.yzc35326.runningassistant.view.TabView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private List<String> titles = new ArrayList<>(Arrays.asList("校园跑", "导航定位", "发现", "我"));
    private TabView tabView1, tabView2, tabView3, tabView4;
    private List<TabView> tabList = new ArrayList<>();
    private SparseArray<Fragment> fragmentSparseArray = new SparseArray<>();
    private static final String BUNDLE_KEY_POS = "bundle_key_pos";
    private int currentTabPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_viewpager_tab);
        if (savedInstanceState != null) {
            currentTabPos = savedInstanceState.getInt(BUNDLE_KEY_POS, 0);
        }
        LogUtil.d("currentTabPos = ", "" + currentTabPos);
        initView();
        initViewPagerAdapter();
        initEvents();

    }

    /**
     * 添加点击事件
     */
    private void initEvents() {
        for (int i = 0; i < this.tabList.size(); i++) {
            TabView tabView = this.tabList.get(i);
            final int finalI = i;
            tabView.setOnClickListener((v) -> {
                this.viewPager.setCurrentItem(finalI, true);
                setCurrentTab(finalI);
            });
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(BUNDLE_KEY_POS, this.viewPager.getCurrentItem());
        super.onSaveInstanceState(outState);
        LogUtil.d("onSaveInstanceState", "执行了");

    }

    private void initViewPagerAdapter() {
        viewPager.setOffscreenPageLimit(titles.size());
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                if (position == 1) {
                    ListFragmentNav listFragmentNav = ListFragmentNav.getInstance();
                    return listFragmentNav;
                }

                if (position == 0) {
                    ListFragmentSchool listFragmentSchool = ListFragmentSchool.getInstance();
                    return listFragmentSchool;
                }
                if(position==3){
                    ListFragmentMe listFragmentMe = ListFragmentMe.getInstance();
                    return listFragmentMe;
                }
                TabFragment tabFragment = TabFragment.newInstance(titles.get(position));
                return tabFragment;
            }

            @Override
            public int getCount() {
                return titles.size();
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                if (position == 1) {
                    ListFragmentNav listFragmentNav = (ListFragmentNav) super.instantiateItem(container, position);
                    fragmentSparseArray.put(position, listFragmentNav);
                    return listFragmentNav;
                }
                if(position==0){
                    ListFragmentSchool listFragmentSchool =(ListFragmentSchool) super.instantiateItem(container, position);
                    fragmentSparseArray.put(position, listFragmentSchool);
                    return listFragmentSchool;
                }
                if(position==3){
                    ListFragmentMe listFragmentMe = (ListFragmentMe)super.instantiateItem(container, position);
                    fragmentSparseArray.put(position,listFragmentMe);
                    return listFragmentMe;
                }
                TabFragment tabFragment = (TabFragment) super.instantiateItem(container, position);
                fragmentSparseArray.put(position, tabFragment);
                return tabFragment;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                fragmentSparseArray.remove(position);
                super.destroyItem(container, position, object);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //左->右 0->1, left pos, right pos + 1, positionOffset 0~1
                //left progress:1->0(1-positionOffset), right progress:0->1(positionOffset)
                //右->左 1->0, left pos,right pos+1,positionOffset 1~0
                //left progress:0->1(1-positionOffset), right progress:1->0(positionOffset)
                LogUtil.d("onPageScrolled", "pos=" + position + ",positionOffset = " + positionOffset);
                //left tab
                //left tab
                if (positionOffset > 0) {
                    TabView left = tabList.get(position);
                    TabView right = tabList.get(position + 1);
                    left.setProgress(1 - positionOffset);
                    right.setProgress(positionOffset);
                }

            }

            @Override
            public void onPageSelected(int position) {

                LogUtil.d("onPageSelected", "pos = " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initView() {
        /**
         * 初始化控件
         */
        tabView1 = (TabView) findViewById(R.id.tab_view_1);
        tabView2 = (TabView) findViewById(R.id.tab_view_2);
        tabView3 = (TabView) findViewById(R.id.tab_view_3);
        tabView4 = (TabView) findViewById(R.id.tab_view_4);
        tabView1.setIconAndText(R.drawable.aio, R.drawable.ain, "校园跑");
        tabView2.setIconAndText(R.drawable.oq, R.drawable.arc, "导航定位");
        tabView3.setIconAndText(R.drawable.aiq, R.drawable.aip, "发现");
        tabView4.setIconAndText(R.drawable.ais, R.drawable.air, "我");
        tabList.add(tabView1);
        tabList.add(tabView2);
        tabList.add(tabView3);
        tabList.add(tabView4);
        viewPager = (ViewPager) findViewById(R.id.vp);
        this.setCurrentTab(this.currentTabPos);
    }

    private void setCurrentTab(int pos) {
        for (int i = 0; i < this.tabList.size(); i++) {
            TabView tabView = this.tabList.get(i);
            if (i == pos) {
                tabView.setProgress(1);
            } else {
                tabView.setProgress(0);
            }
        }
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        MainActivityPermissionsDispatcher.getMultiWithPermissionCheck(this);
//    }
//
//    //获取多个权限
//    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO})
//    public void getMulti() {
//        Toast.makeText(this, "getMulti", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this,requestCode,grantResults);
//    }
}