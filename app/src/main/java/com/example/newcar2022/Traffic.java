package com.example.newcar2022;

import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class Traffic {
    /**************导入本地类库*****************/

    private static String TAG = "<-Traffic->";
    /************交通灯识别阈值*************/
    public static double[][]  HSV_VALUE_LOW = {
            {110,0,0},    //红色  //156 ，45，45 180，255，255
            {30,45,90},      //绿色
         //  { 77,121,101 },  //黄色,
    };

    public static double[][]  HSV_VALUE_HIGH = {
            {180,255,255},   //红色
            {82,255,255},    //绿色
       //     { 102,255,255 }, //黄色,
    };

    /***************交通灯识别*****************/

    public static int Traffic_Out(){
        Mat maxRect = new Mat();
        Mat mat = new Mat();
        List<MatOfPoint> contour = new ArrayList<MatOfPoint>();
        String[]  STR = {"红色","绿色","黄色"};                           ///////21cm处
        int color_num = 2;
        try{
            HsvValue.Set_Hsv_Value(File_Name.TRAFFIC_HSV_VALUE);   //设置HSV的值
            Bitmap bitmap_buf = MainActivity.bitmap;
            Fill.savePhoth(File_Name.JTD_PHOTH, bitmap_buf);  //保存图片
            Utils.bitmapToMat(bitmap_buf, maxRect);
            Imgproc.medianBlur(maxRect,maxRect,39);  //中值滤波
            Imgproc.threshold(maxRect,maxRect,225,255, Imgproc.THRESH_BINARY); //二值化
            Mat matBuf = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(3,3));
            Imgproc.dilate(maxRect,maxRect,matBuf);    //膨胀
            Imgproc.cvtColor(maxRect,maxRect, Imgproc.COLOR_BGR2HSV);  //转换颜色 通道（HSV）

            Mat contour_buf = new Mat();
            Mat erode_buf = new Mat();
            for(int index=0; index<2; index++){
                Core.inRange(maxRect, new Scalar(HSV_VALUE_LOW[index]),
                        new Scalar(HSV_VALUE_HIGH[index]), erode_buf);   //提取对应颜色图片
                matBuf = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(13,13));
                Imgproc.erode(erode_buf,erode_buf,matBuf);   //腐蚀去除噪点
                Imgproc.findContours(erode_buf,contour, contour_buf, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE); //寻找轮廓

                Log.e("轮廓个数",contour.size()+" "+ STR[index]);

                if(contour.size() == 1 ){
                    double area = Imgproc.contourArea(contour.get(0));   //获取轮廓面积
                    Log.e("交通灯识别开始",area + " "  + STR[index] + "\n");
                    //if(area > 100){
                    color_num = index;  //记录对应颜色值
                    contour.clear();  //清除轮廓
                    break;
                    //}
                }
                else {
                    Log.e("黄灯识别开始","开始");
                    color_num = 2;
                }
            }
        }catch(Exception e){
            Log.e(TAG,"交通灯识别错误" + "\n");
        }

        Log.e("交通灯识别", STR[color_num]);
        new Fill().saveFile(File_Name.JTD_FRUIT,STR[color_num]);  //保存识别结果
        return color_num+1;
    }




}
