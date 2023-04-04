package com.example.newcar2022;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import org.opencv.android.CameraActivity;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.wechat_qrcode.WeChatQRCode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

/**
 * Created by dell on 2018/1/13.
 */
public class QR_Code extends Application {
    /**************导入本地类库*****************/

  //  public static native int Correct_Partition_QR(long matA);   //矫正分割二维码


    /**
     * 二维码识别方法
     *
     * @param qrName
     *            第几个二维码
     */
    private static Timer timer;
    static int flag = 0;
    static Bitmap ewm_bp;
    static Bitmap ewm_bp1;
    static String qr_str = "";


    protected WeChatQRCode weChatQRCode = null;
    private Mat dstRgb = null;
    private Mat dstGray = null;
    private Mat m = null;
    private Size size = null;
    private List<Mat> points = new ArrayList<>();
    private Scalar scalar = new Scalar(255, 255, 0, 0);
    private Point center = new Point();

    private String ooooo;

    public void Delay(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public final String QR_ccode(final int cont) {


        String result=null;
        flag = 0;
        Mat newMat = new Mat();
        weChatQRCode = WechatQr.init(this);

        while(result==null) {

//        Bitmap bitmapss= BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/drawable/a11.png"));
            Bitmap buff = MainActivity.bitmap;
            Fill.savePhoth(File_Name.QR_PHOTH + cont + ".png", buff);  //保存二维码图片


//            Utils.bitmapToMat(buff, newMat);
//            Imgproc.cvtColor(newMat, newMat,Imgproc.COLOR_BGR2RGB);

            Utils.bitmapToMat(buff, newMat);
            Figure figure = new Figure();
            Mat ops = new Mat();
            ops = figure.cvtdoublecolors(buff, true);  //指定颜色二维码
            Imgproc.cvtColor(ops, newMat, Imgproc.COLOR_BGR2GRAY); //灰度化

            Bitmap bitmap_buf = Bitmap.createBitmap(ops.width(), ops.height(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(ops, bitmap_buf);



            result = detectssss(ops, newMat);
            System.out.println(result);

            Log.e("debug", "falg=" + flag);
            if (result!=null) {    //判断是否为空
                Log.e("二维码", "5");
                Fill.saveFile(File_Name.QR_CODE + cont + ".txt",result);
                Log.e("二维码", "识别成功！");
                break;
            }
            flag++;

            if(flag >= 8)
            {
               // timer.cancel();
                Log.e("二维码", "识别失败");
                flag = 0;
                result = "识别失败";
            }
        }
        return  result;

    }

    public final String Qr_11code(final int cont){                  //不经过颜色提取的
        qr_str = "";
        flag = 0;
        Mat newMat = new Mat();
        Mat srcMat =new Mat();

        while(qr_str.equals("") || qr_str==null) {

            weChatQRCode = WechatQr.init(this);

//        Bitmap bitmapss= BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/drawable/a11.png"));
            Bitmap buff = MainActivity.bitmap;
            Fill.savePhoth(File_Name.QR_PHOTH + cont + ".png", buff);  //保存二维码图片

            Utils.bitmapToMat(buff, srcMat);
//        Utils.bitmapToMat(bitmapss, newMat);
//        Figure figure = new Figure();
//        Mat ops = new Mat();
//        ops = figure.cvtdoublecolors(newMat, true);  //指定颜色二维码
            Imgproc.cvtColor(srcMat, newMat, Imgproc.COLOR_BGR2GRAY); //灰度化

//        Bitmap bitmap_buf = Bitmap.createBitmap(ops.width(), ops.height(), Bitmap.Config.RGB_565);
//        Utils.matToBitmap(ops, bitmap_buf);


            qr_str = detectssss(srcMat, newMat);
            System.out.println("sssssss" + qr_str);



            if (!qr_str.equals("")|| qr_str!=null) {    //判断是否为空
                Log.e("二维码", "5");
                Fill.saveFile(File_Name.QR_CODE + cont + ".txt",qr_str);
                Log.e("二维码", "识别成功！");
                break;
            }
            flag++;
            Log.e("debug", "falg=" + flag);
            if(flag >= 5)
            {
                timer.cancel();
                Log.e("二维码", "识别失败");
                flag = 0;
                qr_str = "识别失败";
            }
        }
        return  qr_str;
    }



    public String detectssss(Mat ops,Mat gray) {
        points.clear();
        if (null != dstGray) {
            dstGray.release();
        }
        if (null != dstRgb) {
            dstRgb.release();
        }
        // 原彩图
        Mat rgba = ops;
        // 原灰度图
        Mat grayMat = gray;

        center.x = rgba.cols() >> 1;
        center.y = rgba.rows() >> 1;

        if (null == dstRgb) {
            m = Imgproc.getRotationMatrix2D(center, 270, 1);
            // 如果只处理彩图，就只需要创建和处理dstRgb，如果只需要处理灰度图，就只需要创建和处理dstGray
            // 接受旋转后的彩色图
            dstRgb = new Mat(rgba.cols(), rgba.rows(), rgba.type());
            // 接受旋转后的灰度图
            dstGray = new Mat(rgba.cols(), rgba.rows(), rgba.type());
            size = new Size(rgba.cols(), rgba.rows());
        }

        // 如果只处理彩图，就只需要创建和处理dstRgb，如果只需要处理灰度图，就只需要创建和处理dstGray
        // 旋转原彩图
        Imgproc.warpAffine(rgba, dstRgb, m, size);
        // 旋转灰度图
        Imgproc.warpAffine(grayMat, dstGray, m, size);

        // 灰度图帧率更高
        List<String> results = weChatQRCode.detectAndDecode(dstRgb, points);
        // 原彩帧率低一些
        if (null != results && results.size() > 0) {
         //   Log.e(TAG, "识别的结果数量：" + results.size());
            for (int i = 0, isize = results.size(); i < isize; i++) {
                Rect rect = Imgproc.boundingRect(points.get(i));
                Imgproc.rectangle(dstRgb, rect, scalar, 5);
                ooooo = results.get(i);

            }

        }
        System.out.println(ooooo);


        // 返回原彩图旋转后的图
        return ooooo;
    }



    public static final String Qrcode(final int cont) {
        qr_str = "";
        flag = 0;
        Mat newMat = new Mat();

        while(qr_str.equals("")){
            Utils.bitmapToMat(Fill.getBitmap(),newMat);
            Figure figure=new Figure();

         //   newMat=figure.cvtdoublecolors(newMat,true);  //指定颜色二维码

            Imgproc.cvtColor(newMat,newMat, Imgproc.COLOR_BGR2GRAY); //灰度化
            Bitmap bitmap_buf = Bitmap.createBitmap(newMat.width(),newMat.height(),Bitmap.Config.RGB_565);
            Utils.matToBitmap(newMat,bitmap_buf);
            Fill.savePhoth(File_Name.QR_PHOTH + cont + ".png",bitmap_buf);  //保存二维码图片
            ewm_bp = bitmap_buf;

            Matrix matrix = new Matrix();
            matrix.postScale(1.5f, 1.5f);
            ewm_bp1 = Bitmap.createBitmap(ewm_bp, 0, 0, ewm_bp.getWidth(),ewm_bp.getHeight(),matrix,true);
            timer = new Timer();

            Result result = null;
            try {
                Tow_Code tow_code = new Tow_Code(ewm_bp1);
                BinaryBitmap binaryBitmap = new BinaryBitmap(
                        new HybridBinarizer(tow_code));
                Log.e("二维码", "1");
                Map<DecodeHintType, String> hint = new HashMap<DecodeHintType, String>();
                hint.put(DecodeHintType.CHARACTER_SET, "utf-8");
                Log.e("二维码", "2");
                QRCodeReader reader = new QRCodeReader();
                Log.e("二维码", "3");
                result = reader.decode(binaryBitmap, hint);
                Log.e("二维码", "4");
                qr_str = result.toString();
                Log.e("二维码", qr_str);
            } catch (NotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ChecksumException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (FormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (!qr_str.equals("")) {    //判断是否为空
                Log.e("二维码", "5");
                Fill.saveFile(File_Name.QR_CODE + cont + ".txt",qr_str);
                Log.e("二维码", "识别成功！");
                break;
            }
                flag++;
                Log.e("debug", "falg=" + flag);
                if(flag >= 10)
                {
                    timer.cancel();
                    Log.e("二维码", "识别失败");
                    flag = 0;
                    qr_str = "识别失败";
                }
        }
       return  qr_str;
    }


    public static final String TFT_Qrcode(final int cont) {
        qr_str = "";
        flag = 0;
        Mat newMat = new Mat();


        while(qr_str.equals("")){
            Utils.bitmapToMat(Fill.getBitmap(),newMat);
            newMat = new Figure().cutRectangle1(newMat,true);  //截取显示屏
            Imgproc.cvtColor(newMat,newMat, Imgproc.COLOR_BGR2GRAY); //灰度化
            Bitmap bitmap_buf = Bitmap.createBitmap(newMat.width(),newMat.height(),Bitmap.Config.RGB_565);
            Utils.matToBitmap(newMat,bitmap_buf);
            Fill.savePhoth("TFT"+ File_Name.QR_PHOTH + cont + ".png",bitmap_buf);  //保存二维码图片
            ewm_bp = bitmap_buf;
            Matrix matrix = new Matrix();
            matrix.postScale(1.5f, 1.5f);
            ewm_bp1 = Bitmap.createBitmap(ewm_bp, 0, 0, ewm_bp.getWidth(),ewm_bp.getHeight(),matrix,true);
            timer = new Timer();

            Result result = null;
            try {
                Tow_Code tow_code = new Tow_Code(ewm_bp1);
                BinaryBitmap binaryBitmap = new BinaryBitmap(
                        new HybridBinarizer(tow_code));
                Log.e("二维码", "1");
                Map<DecodeHintType, String> hint = new HashMap<DecodeHintType, String>();
                hint.put(DecodeHintType.CHARACTER_SET, "utf-8");
                Log.e("二维码", "2");
                QRCodeReader reader = new QRCodeReader();
                Log.e("二维码", "3");
                result = reader.decode(binaryBitmap, hint);
                Log.e("二维码", "4");
                qr_str = result.toString();
                Log.e("二维码", qr_str);
            } catch (NotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ChecksumException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (FormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (!qr_str.equals("")) {    //判断是否为空
                Log.e("二维码", "5");
                Fill.saveFile(File_Name.QR_CODE + cont + ".txt",qr_str);
                Log.e("二维码", "识别成功！");
                break;
            }
            flag++;
            Log.e("debug", "falg=" + flag);
            if(flag >= 10)
            {
                timer.cancel();
                Log.e("二维码", "识别失败");
                flag = 0;
                qr_str = "识别失败";
            }
        }
        return  qr_str;
    }


//    public static void Correct_QR(){       //矫正分割二维码
//        Bitmap bitmap = Fill.getBitmap();
//        String nameFile_text =  "图形识别/二维码/";
//        try {
//            if(!Fill.isFileExist(nameFile_text)){
//                Fill.createDir(nameFile_text);
//            }
//            Log.e("Correct_QR: ","789789" );
//            Mat input_A = new Mat();
//            Utils.bitmapToMat(bitmap,input_A);
//            Correct_Partition_QR(input_A.nativeObj);
//        }catch (Exception e){
//
//        }
//
//    }


}

