package com.example.newcar2022;

import android.util.Log;

/**
 * Created by dell on 2017/9/26.
 */
public class HsvValue {

    /*****红色HSV阈值*****/
    public static final  int  redLowH  = 90;
    public static final  int  redHighH = 150;
    public static final  int  redLowS = 43;
    public static final  int  redHighS = 255;
    public static final  int  redLowV = 46;
    public static final  int  redHighV = 255;

    /*****绿色HSV阈值*****/
    public static final  int  greenLowH  = 50;
    public static final  int  greenHighH = 65;
    public static final  int  greenLowS = 43;
    public static final  int  greenHighS = 255;
    public static final  int  greenLowV = 46;
    public static final  int  greenHighV = 255;

    /*****黄色HSV阈值*****/
    public static final  int  yellowLowH  = 80;
    public static final  int  yellowHighH = 90;
    public static final  int  yellowLowS = 43;
    public static final  int  yellowHighS = 255;
    public static final  int  yellowLowV = 46;
    public static final  int  yellowHighV = 255;

    /*****蓝色HSV阈值*****/
    public static final  int  blueLowH  = 50;
    public static final  int  bluelowHighH = 65;
    public static final  int  blueLowS = 43;
    public static final  int  buleHighS = 255;
    public static final  int  buleLowV = 46;
    public static final  int  bullHighV = 255;


    /*****紫色HSV阈值*****/
    public static final  int  purpleLowH  = 50;
    public static final  int  purpleHighH = 65;
    public static final  int  purpleLowS = 43;
    public static final  int  purpleHighS = 255;
    public static final  int  purpleLowV = 46;
    public static final  int  purpleHighV = 255;


    /******黑色HSV阈值*****/
    public static final  int  blackLowH  = 50;
    public static final  int  blackHighH = 65;
    public static final  int  blackLowS = 43;
    public static final  int  blackHighS = 255;
    public static final  int  blackLowV = 46;
    public static final  int  blackHighV = 255;

    /******粉色HSV阈值*****/
    public static final  int  pinkLowH  = 50;
    public static final  int  pinkHighH = 65;
    public static final  int  pinkLowS = 43;
    public static final  int  pinkHighS = 255;
    public static final  int  pinkLowV = 46;
    public static final  int  pinkHighV = 255;

    /******浅蓝色HSV阈值*****/
    public static final  int  wathetLowH  = 50;
    public static final  int  wathetHighH = 65;
    public static final  int  wathetLowS  = 43;
    public static final  int  wathetighS = 255;
    public static final  int  wathetLowV  = 46;
    public static final  int  wathetHighV = 255;


    public static final int  HSV_RID = 0;    //红色
    public static final int  HSV_GREEN = 1;  //绿色
    public static final int  HSV_BLUE = 2;   //蓝色
    public static final int  HSV_YELLOW = 3; //黄色
    public static final int  HSV_PINK = 4;   //紫色
    public static final int  HSV_WATHET= 5;  //浅蓝
    public static final int  HSV_BLACK = 6;  //黑色

    public static   double[][]  HSV_VALUE_LOW = {
            {113,144,90},  //红色
            {48,120,117},  //绿色
            {0,196,110},  //蓝色
            {71,44,85},  //黄色,
            {165,95,62},  //紫色
            {22,158,97},  //浅蓝
            {0,0,0},      //黑色
            {0,0,142}     //白色
    };

    public static   double[][]  HSV_VALUE_HIGH = {
            {137,255,255},  //红色
            {73,255,255},  //绿色
            {20,255,255},  //蓝色
            {116,255,255},  //黄色,
            {194, 255,255},  //紫色
            {46,255,255},  //浅蓝
            {180,255,80},   //黑色
            {40,127,255}   //白色  {180,255,255}
    };


    /**
     * 设置SHV的值
     */
    public static void Set_Hsv_Value(String file_name) {
        String str = " ";
        try {
            str = new Fill().readFile(file_name);
            str = str.replace("高","");
            str = str.replace("低","");
            str = str.replace("[","");
            str = str.replace("]","");
            str = str.replace(".0","");
            str = str.replace(",","");

            str = str.trim();
            String[]  str_arr = str.split("\n");
            String[] str_buf;
            Log.e("<--------------------------------------->",str_arr.length + " ");
            for(int i=0; i<str_arr.length; i++) {
                Log.e("<------------------------------------->", i + "  " + str_arr[i] + "\n");
                str_buf = str_arr[i].split(" ");
                if (i <= 6) {
                    HSV_VALUE_HIGH[i][0] = (double) Integer.valueOf(str_buf[0]);
                    HSV_VALUE_HIGH[i][1] = (double) Integer.valueOf(str_buf[1]);
                    HSV_VALUE_HIGH[i][2] = (double) Integer.valueOf(str_buf[2]);
                } else if (i != 7) {
                    HSV_VALUE_LOW[i - 8][0] = (double) Integer.valueOf(str_buf[0]);
                    HSV_VALUE_LOW[i - 8][1] = (double) Integer.valueOf(str_buf[1]);
                    HSV_VALUE_LOW[i - 8][2] = (double) Integer.valueOf(str_buf[2]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
