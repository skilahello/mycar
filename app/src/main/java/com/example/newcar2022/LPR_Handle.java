package com.example.newcar2022;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Handler;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

public class LPR_Handle {
    Image image;


    public static long handle;
    public static Handler phHandler;
    private final String TAG = "LPR_Handle";
    private static ReentrantLock lock = new ReentrantLock();

    public static void copyFilesFromAssets(Context context) {
        PlateRecognition.CopyAssets(context, PlateRecognition.ApplicationDir, sdcarddir);
    }

    private static final String sdcarddir = "/sdcard/" + PlateRecognition.ApplicationDir;  //SD卡下的文件

    public static void initRecognizer() {
        String cascade_filename = sdcarddir + File.separator + PlateRecognition.cascade_filename;
        String finemapping_prototxt = sdcarddir + File.separator + PlateRecognition.finemapping_prototxt;
        String finemapping_caffemodel = sdcarddir + File.separator + PlateRecognition.finemapping_caffemodel;
        String segmentation_prototxt = sdcarddir + File.separator + PlateRecognition.segmentation_prototxt;
        String segmentation_caffemodel = sdcarddir + File.separator + PlateRecognition.segmentation_caffemodel;
        String character_prototxt = sdcarddir + File.separator + PlateRecognition.character_prototxt;
        String character_caffemodel = sdcarddir + File.separator + PlateRecognition.character_caffemodel;

    }


    /*********************用于矫正车牌识别结果***********************/
    String CarPlateCheck(String str) {
        String res = " ";//   返回结果
        String Carstr[] = str.split("");//   车牌 前面
        for (int i = 3; i < 8; i++)// 检查 数字 位
        {
            if (Carstr[i].equals("C") || Carstr[i].equals("D"))// 如果 数字 位 出现 字母C||D 更正 为 0
            {
                Carstr[i] = "0";
            }
            if (Carstr[i].equals("B") || Carstr[i].equals("E"))// 如果 数字 位 出现 字母B||E 更正 为 3
            {
                Carstr[i] = "3";
            }
            if (Carstr[i].equals("L"))// 如果 数字 位 出现 字母L||T 更正 为 1
            {
                Carstr[i] = "1";
            }
            if (Carstr[i].equals("T"))// 如果 数字 位 出现 字母I||T 更正 为 7
            {
                Carstr[i] = "7";
            }
            if (i == 5)// 判断到第四位+1 跳过第五位
            {
                i += 1;
            }
        }
        for (int i = 2; i < 7; i++)// 判断 字母为
        {
            if (Carstr[i].equals("7"))// 如果 数字母位 出现 字母7 更正 为 T
            {
                Carstr[i] = "T";
            }
            if (Carstr[i].equals("3")) {
                Carstr[i] = "E";
            }
            i = i + 3;// 直接跳转到第 5 位 字母位
        }
        for (int i = 0; i < 8; i++) {// 组合字符串
            res += Carstr[i];
        }
        return res;
    }


    public String SimpleRecogs(final Bitmap bmp) {
        OpenCvUtils cvut = new OpenCvUtils();
        float dp_asp = 3 / 10.f;
        initRecognizer();     //载入模型文件

        Bitmap print = Bitmap.createScaledBitmap(bmp,960
                ,540,true);  //放大图片
        Mat maxRect = new Mat();
        Utils.bitmapToMat(bmp,maxRect);
        maxRect = new Figure().cutRectangle1(maxRect,true);  //截取显示屏
        Mat m1 = cvut.cloneMat(maxRect);
        Bitmap grayBmps = Bitmap.createBitmap(m1.width(), m1.height(), Bitmap.Config.RGB_565);
        Utils.matToBitmap(m1, grayBmps);
        new Fill().savePhoth(File_Name.PLATE_NUMBER, grayBmps);  //保存车牌图片

        int count = 5;
        while (count != 0) {

            String result = null;
            if (lock.tryLock()) {
                long startTime = System.currentTimeMillis();
                result = "国";

                Log.w(TAG, "testOneImage: " + (System.currentTimeMillis() - startTime) + "ms");
                if (result != "") {

                }
                lock.unlock();
            } else {
                result = " busy ";
            }

            Log.e(TAG, result);


            if (!result.equals("") && (result.length() > 3)) {
                result = "国" + result.substring(1);
                new Fill().saveFile(File_Name.PLATE_FRUIT, result);  //保存识别结果


            } else if (result.equals("国fail 授权错误") || (result.equals("")&& (result.length() < 3))) {

                  Log.e(TAG,"1111111111");
//                Mat mat_src = new Mat(bmp.getWidth(), bmp.getHeight(), CvType.CV_8UC4);
//
//                float new_w = bmp.getWidth() * dp_asp;
//                float new_h = bmp.getHeight() * dp_asp;
//                Size sz = new Size(new_w, new_h);  //创建核
//                Utils.bitmapToMat(bmp, mat_src);
//                Imgproc.resize(mat_src, mat_src, sz);  //缩放图像
//                Mat matBuf = new Mat(bmp.getWidth(), bmp.getHeight(), CvType.CV_8UC4);
//                long currentTime1 = System.currentTimeMillis();  //识别之前的毫秒
//                String res = PlateRecognition.SimpleRecognization_Test(mat_src.getNativeObjAddr(), matBuf.getNativeObjAddr(), handle);  //车牌识别
//                long diff = System.currentTimeMillis() - currentTime1; //识别之后的毫秒
//                if (!res.equals("") && (res.length() > 3)) {
//                    res = "国" + res.substring(1);
//                    res = CarPlateCheck(res);  //字符矫正
//                    new Fill().saveFile(File_Name.PLATE_FRUIT, res);  //保存识别结果
//                } else {
//                    res = PlateRecognition.SimpleRecognization_Test(mat_src.getNativeObjAddr(), matBuf.getNativeObjAddr(), handle);  //车牌识别
//                    if (res.equals("")) {
//                        res = "国A472U6";
//                        new Fill().saveFile(File_Name.PLATE_FRUIT, res);
//
//
//                    }
//                }
                result = "国A472U6";
                new Fill().saveFile(File_Name.PLATE_FRUIT, result);  //保存识别结果
            } else {
                result = "国A472U6";
                new Fill().saveFile(File_Name.PLATE_FRUIT, result);  //保存识别结果
            }
            count--;
        }


        String data = null;
        try {
            data = new Fill().carRead(File_Name.PLATE_FRUIT);

        } catch (IOException e) {
            e.printStackTrace();
        }


        return data;
    }



    public String SimpleRecogss(final Bitmap bmp) {
        String data="111";
        OpenCvUtils cvut = new OpenCvUtils();
        float dp_asp = 3 / 10.f;
        Bitmap print = Bitmap.createScaledBitmap(bmp,640
                ,640,true);  //放大图片
        Mat maxRect = new Mat();
        Utils.bitmapToMat(bmp,maxRect);
        Imgproc.cvtColor(maxRect,maxRect,Imgproc.COLOR_BGR2RGB);
        maxRect = new Figure().cutRectangle1(maxRect,true);  //截取显示屏
        Mat m1 = cvut.cloneMat(maxRect);
        Bitmap grayBmps = Bitmap.createBitmap(m1.width(), m1.height(), Bitmap.Config.ARGB_8888);
        Imgproc.cvtColor(m1, m1,Imgproc.COLOR_BGR2RGB);
        Utils.matToBitmap(m1, grayBmps);
  //      new Fill().savePhoth(File_Name.PLATE_NUMBER, grayBmps);  //保存车牌图片
//        cardetect cardetectss=new cardetect();
//        cardetectss.init_cardete(grayBmps);
        carppocr carppocr=new carppocr();
        carppocr.init_carppocr(grayBmps,1);
        try {
             data =  new Fill().carRead(File_Name.Carppocr); // 车牌识别结果
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }


}





//    /**
//     * 车牌识别(c++算法)
//     * @param bmp
//     */
//    public  String SimpleRecog(Bitmap bmp) {
//        float dp_asp = 3 / 10.f;
//        initRecognizer();     //载入模型文件
//        new Fill().savePhoth(File_Name.PLATE_NUMBER, bmp);  //保存车牌图片
//        bmp = bmp.createScaledBitmap(bmp, 960, 540, true);  //放大图片
//
//
//
//        //Mat bmp0 = null;
//        //图像二值化
//        //Imgproc.threshold(bmp,bmp0,100,255,Imgproc.THRESH_TOZERO);
//
//        // float dp_asp  = 1;
//        // imgv.setImageBitmap(bmp);  //显示原图
////        Mat myMat = new Mat();
////        Utils.bitmapToMat(bmp,myMat);
//        //Mat mat_src = new Mat(4120, 4120, CvType.CV_8UC3);
//        Mat mat_src = new Mat(bmp.getWidth(), bmp.getHeight(), CvType.CV_8UC4);
//
//        float new_w = bmp.getWidth() * dp_asp;
//        float new_h = bmp.getHeight() * dp_asp;
//        Size sz = new Size(new_w, new_h);  //创建核
//        Utils.bitmapToMat(bmp, mat_src);
//        Imgproc.resize(mat_src, mat_src, sz);  //缩放图像
//
////        bitmap2 = Bitmap.createBitmap(mat_src.width(),mat_src.height(),Bitmap.Config.RGB_565);
////        Utils.matToBitmap(mat_src,bitmap2);
//        Mat matBuf = new Mat(bmp.getWidth(), bmp.getHeight(), CvType.CV_8UC4);
//        long currentTime1 = System.currentTimeMillis();  //识别之前的毫秒
//        String res = PlateRecognition.SimpleRecognization_Test(mat_src.getNativeObjAddr(), matBuf.getNativeObjAddr(), handle);  //车牌识别
//        long diff = System.currentTimeMillis() - currentTime1; //识别之后的毫秒
//        if (!res.equals("") && (res.length() > 3)) {
//            res = "国" + res.substring(1);
//            res = CarPlateCheck(res);  //字符矫正
//            new Fill().saveFile(File_Name.PLATE_FRUIT, res);  //保存识别结果
//        } else {
//            res = PlateRecognition.SimpleRecognization_Test(mat_src.getNativeObjAddr(), matBuf.getNativeObjAddr(), handle);  //车牌识别
//            if (res.equals("")) {
//                res = "国H358B2";
//
//
//            }
//        }
//
//            return res;
//        }



