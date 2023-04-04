package com.example.newcar2022;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /***
     *  功能： 切换对应页面内容
     * @param position 对应页面的标号
     * @return  要跳转到的页面
     */
    @Override
    public Fragment getItem(int position){
        Fragment fragment = new Fragment();
        switch(position)
        {
            case 0:
                fragment = new BasicControl();
                break;    //基本操作
            case 1:
                fragment = new Infrared_Activity();
                break;   //红外
            case 2:
                fragment = new Zigbee_Activity();
                break;    //zigbee
            case 3:
                fragment = new Image();
                break;    //识别处理
            case 4:
                fragment = new AutoControl();
                break; //全自动
            default:
                break;
        }
        return  fragment;
    }


    /**
     * 控制页面的个数
     * @return
     */
    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return  "基本控制";
            case 1:
                return "红外控制";
            case 2:
                return "Zigbee控制";
            case 3:
                return  "识别控制";
            case 4:
                return  "自动行走";
        }
        return null;
    }
}
