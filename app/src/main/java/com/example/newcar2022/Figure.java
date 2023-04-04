package com.example.newcar2022;

import static com.example.newcar2022.MainActivity.bitmap;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Figure {
    private static ImageView image_filter_view;

    /**************导入本地类库*****************/


    public static String iol;
    private static String ssooso;

    private List<MatOfPoint>contours=new ArrayList<>();
    private int contoursCounts;

    private  int rect;  //矩形
    private  int tri;   //三角形
    private  int circle; //圆
    private  int wjx;   //五角星

    private List<MatOfPoint> rectContour     = new ArrayList<MatOfPoint>();  //矩形轮廓
    private List<MatOfPoint> triContour     = new ArrayList<MatOfPoint>();  //三角轮廓
    private List<MatOfPoint> circleContour   = new ArrayList<MatOfPoint>();  //圆轮廓
    private List<MatOfPoint> fiveAngleContour  = new ArrayList<MatOfPoint>();  //五角星轮廓
    public Mat drawFigure = new Mat();
    private static final  String TAG = "<-Figure->";
    private Mat dsmat;
    private Mat hsvmat;
    private Bitmap resultbitmap;

    //形状识别
    public   int[]  figureDiscriminate(Mat matMould, MatOfPoint contour, int cont)
    {
        Rect lkContour;
        int   wide; //宽
        int   tall; //高
        int[]   form = new int[5];
        int   areaNum = 0; //区域的个数
        double bufPoint  = 255;
        double bufPoint1 = 255;
        int angleContNum = 0;
        int rowWhiteContStart = 0;
        int rowWhiteContEnd   = 0;
        int rowBlackContStart = 0;
        int rowBlackContEnd   = 0;

        int colWhiteContStart = 0;
        int colWhiteContEnd   = 0;
        int colBlackContStart = 0;
        int colBlackContEnd   = 0;
        lkContour = Imgproc.boundingRect(contour);  //轮廓转为矩形轮廓
        Rect rectArea = new Rect(new Point(lkContour.br().x - 8,lkContour.br().y - 8),
                new Point(lkContour.tl().x + 8,lkContour.tl().y + 8));  //定义截取区域的大小
//        if(rectArea.size().width >= matMould.size().width )
//        {
//            if(rectArea.size().height >= matMould.size().height)
//            {
//                Log.e("单个轮廓截取","错误" + "rectArea.size:" + rectArea.size() +
//                        "   " + "matMould.size:" + matMould.size() );
//            }
//        }else
        {
            Mat areaRect = new Mat(matMould, rectArea);
            Bitmap grayBmp = Bitmap.createBitmap(areaRect.width(), areaRect.height(), Bitmap.Config.RGB_565);
            Utils.matToBitmap(areaRect, grayBmp);
            new Fill().savePhoto(grayBmp,"最小矩形"+ cont + ".png");
            wide = areaRect.cols() - 1;
            tall = areaRect.rows() - 1;
            if ((areaRect.get(0, 0)[0]) == 255)
                areaNum++;
            if ((areaRect.get(0, wide)[0]) == 255)
                areaNum++;
            if ((areaRect.get(tall, 0)[0]) == 255)
                areaNum++;
            if ((areaRect.get(tall, wide)[0]) == 255)
                areaNum++;
            if ((areaNum > 0) && (areaNum < 4))
            {
                tri++;
                form[0]= 1;
                triContour.add(contour);  //添加轮廓
            }
            else if (areaNum == 0)
            {
                rect++;
                form[0] = 0;
                rectContour.add(contour);
            }
            else
            {
                for (int row = 0; row < areaRect.rows(); row++)
                {
                    if (areaRect.get(row, 0)[0] != bufPoint)     //判断第1列
                    {
                        bufPoint = areaRect.get(row, 0)[0];
                        angleContNum++;
                    }
                    if (bufPoint == 0)   //记录像素点个数
                        rowBlackContStart++;  //黑色
                    else
                        rowWhiteContStart++;  //白色
                    if (areaRect.get(row, wide)[0] != bufPoint1)  //判断最后一列
                    {
                        bufPoint1 = areaRect.get(row, wide)[0];
                        angleContNum++;
                    }
                    if (bufPoint1 == 0)   //记录像素点个数
                        rowBlackContEnd++;   //黑色
                    else
                        rowWhiteContEnd++;   //白色
                }
                bufPoint = 255;
                bufPoint1 = 255;
                for (int col = 0; col < areaRect.cols(); col++)
                {
                    if (areaRect.get(0, col)[0] != bufPoint)
                    {
                        bufPoint = areaRect.get(0, col)[0];
                        angleContNum++;
                    }
                    if (bufPoint == 0)   //记录像素点个数
                        colBlackContStart++;  //黑色
                    else
                        colWhiteContStart++;  //白色
                    if (areaRect.get(tall, col)[0] != bufPoint1)
                    {
                        bufPoint1 = areaRect.get(tall, col)[0];
                        angleContNum++;
                    }
                    if (bufPoint1 == 0)   //记录像素点个数
                        colBlackContEnd++;  //黑色
                    else
                        colWhiteContEnd++;  //白色
                }
                if (angleContNum / 2 == 5)
                {
                    wjx++;   //五角星
                    form[0] = 3;
                    fiveAngleContour.add(contour);
                }
                else if ((rowBlackContEnd > rowWhiteContEnd) && (rowBlackContStart > rowWhiteContStart)
                        || (colBlackContEnd > colWhiteContEnd) && (colBlackContStart > colWhiteContStart))
                {
                    circle++;  //圆
                    form[0] = 2;
                    circleContour.add(contour);
                } else
                {
                    rect++;
                    form[0] = 0;
                    rectContour.add(contour);
                }
                Log.e("单个图形像素", rowBlackContEnd + "  " + rowBlackContStart + "  " + colBlackContEnd + "  " + colBlackContStart + "  " + tall + "  " + wide);
            }
        }
        form[1] = rect;
        form[2] = tri;
        form[3] = circle;
        form[4] = wjx;
        Log.e("单个颜色",form[1] + " " + form[2] + " " + form[3] + " "+ form[4]);
        return  form;
    }

    //注意srcmat为mat对象
    //手动截取
    //自行提供Rect
    public Bitmap cutRectangleres(Mat srcmat,Rect rect){
        dsmat = new Mat(srcmat,rect);
        resultbitmap = Bitmap.createBitmap(dsmat.width(), dsmat.height(),Bitmap.Config.ARGB_8888);
        Imgproc.cvtColor(dsmat, dsmat,Imgproc.COLOR_BGR2RGB);
        Utils.matToBitmap(dsmat, resultbitmap);
        return resultbitmap;
    }



    ///2022华顶峰颜色提取识别

    public Mat cvtdoublecolors(Bitmap map,boolean swit){
        Mat hssvmat = new Mat();
        Mat srcmat=new Mat();
        Mat mask_r=new Mat();
        Mat mask_r2=new Mat();
        Mat mask=new Mat();
        Mat ops=new Mat();
        Bitmap resss=null;


        Utils.bitmapToMat(map,srcmat);
        dsmat=srcmat;



        if (swit){
            Imgproc.cvtColor(dsmat,hssvmat,Imgproc.COLOR_RGB2HSV);

            Core.inRange(hssvmat,new Scalar(0,45,45),new Scalar(10,255,255),mask_r);//转hsv 红  //阈值表
            Core.inRange(hssvmat,new Scalar(156,45,45),new Scalar(180,255,255),mask_r2);
            Core.add(mask_r,mask_r2,mask);  //可提取两种

//            Mat kernel=Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(1,1));//运算核

//            Imgproc.morphologyEx(mask,hssvmat,Imgproc.MORPH_OPEN,kernel);//开运算
//            Imgproc.morphologyEx(hssvmat,hssvmat,Imgproc.MORPH_CLOSE,kernel);//闭运算
//            Imgproc.dilate(mask,mask,kernel);

            Core.bitwise_and(srcmat,srcmat,ops,mask);  //图像区域提取
            resss = Bitmap.createBitmap(dsmat.width(), dsmat.height(),Bitmap.Config.ARGB_8888);
            Imgproc.cvtColor(dsmat, dsmat,Imgproc.COLOR_BGR2RGB);
            Utils.matToBitmap(ops,resss);
            Fill.savePhoth(File_Name.QR_PHOTH +  ".png", resss);  //保存二维码图片

        }else {
//            Imgproc.cvtColor(dsmat,hsvmat,Imgproc.COLOR_RGB2HSV);
//            Core.inRange(hsvmat,new Scalar(160,90,90),new Scalar(179,255,255),mask);//转hsv 红
//            Mat kernel=Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(3,3));//运算核
//            Imgproc.morphologyEx(mask,hsvmat,Imgproc.MORPH_OPEN,kernel);//开运算
//            Imgproc.morphologyEx(hsvmat,hsvmat,Imgproc.MORPH_CLOSE,kernel);//闭运算
//            Imgproc.dilate(hsvmat,hsvmat,kernel);
//            Core.bitwise_and(dsmat,dsmat,ops,hsvmat);//图像区域提取
//            //Utils.matToBitmap(ops,resultbitmap);
        }
        return ops;
    }



    //截取矩形
    public static Mat cutRectangle1(Mat master, boolean flage)
    {
        Mat mat2 = new Mat();
        Mat mat1 = new Mat();
        List<MatOfPoint> contour = new ArrayList<MatOfPoint>();
        master.copyTo(mat2);
        Imgproc.cvtColor(mat2,mat2, Imgproc.COLOR_BGR2GRAY);  //转换颜色通道
        if(flage){
            Imgproc.threshold(mat2,mat2,150,255, Imgproc.THRESH_BINARY); //二值化
        }else{
            Imgproc.threshold(mat2,mat2,50,255, Imgproc.THRESH_BINARY_INV); //二值化
        }

        //轮廓提取
        Imgproc.findContours(mat2,contour, mat1, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);
        double area_max = 0;
        Log.e("Figure:矩形轮廓的个数", contour.size() + "");
        for(int j=0; j<contour.size(); j++)
        {
            double area = Imgproc.contourArea(contour.get(j));
            if(area > area_max)
                area_max = area;   //找出最大轮廓
        }
        for(int j=0; j<contour.size(); j++)    //移除小的面积，只剩最大
        {
            if(!(Imgproc.contourArea(contour.get(j)) == area_max))
            {
                contour.remove(j);
                j--;
            }
        }
        Rect contourRect;   //轮廓矩形
        contourRect = Imgproc.boundingRect(contour.get(0));  //轮廓转为矩形
        Rect rectArea = new Rect( new Point(contourRect.br().x,contourRect.br().y),
                new Point(contourRect.tl().x,contourRect.tl().y));
        // Rect  rectArea = new Rect(contourRect.br(),contourRect.tl());
        //Utils.bitmapToMat(master,mat2);  //转换为矩阵

        if((rectArea.br().x  > mat2.width()) || (rectArea.br().y > mat2.height()) ||
                ((rectArea.tl().x > mat2.width()) && (rectArea.tl().y > mat2.height())))
        {           // 截出的矩形坐标范围大于原图范围
            Log.e("单个轮廓截取","错误" + "rectArea.size:" + rectArea.size() +
                    "   " + "matMould.size:" + mat2.size());
            mat1 = master;
        }else if(((rectArea.br().x  > 0) && (rectArea.br().y > 0)) && ((rectArea.tl().x > 0) && (rectArea.tl().y > 0)))
        {           // 截出的矩形坐标不在原图的边缘
            Log.e("《——————截取矩形的大小——————》",rectArea.tl()+ "  " + rectArea.br() + "  "+ mat2.size());
            mat1 = new Mat(master,rectArea);     //截取出整个显示屏
        }else if(flage){
            mat1 = cutRectangle1(master,false);
            Log.e("Figure","取反");
        }else {
            mat1 = master;
        }
        return  mat1;
    }



    //截取矩形
    public Mat cutRectangle(Bitmap master)
    {
        Mat mat2 = new Mat();
        Mat mat1 = new Mat();
        List<MatOfPoint> contour = new ArrayList<MatOfPoint>();

        Utils.bitmapToMat(master,mat2);  //转换为矩阵
        Mat rectMax = mat2;
        Imgproc.cvtColor(mat2,mat1, Imgproc.COLOR_BGR2GRAY);  //转换颜色通道
        Imgproc.threshold(mat1,mat2,150,255, Imgproc.THRESH_BINARY); //二值化
        //轮廓提取
        Imgproc.findContours(mat2,contour, mat1, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);
        double area_max = 0;
        for(int j=0; j<contour.size(); j++)
        {
            double area = Imgproc.contourArea(contour.get(j));
            if(area > area_max)
                area_max = area;   //找出最大轮廓
        }
        for(int j=0; j<contour.size(); j++)    //移除小的面积，只剩最大
        {
            if(!(Imgproc.contourArea(contour.get(j)) == area_max))
            {
                contour.remove(j);
                j--;
            }
        }
        Rect contourRect;   //轮廓矩形
        contourRect = Imgproc.boundingRect(contour.get(0));  //轮廓转为矩形
        Rect rectArea = new Rect( new Point(contourRect.br().x + 18,contourRect.br().y + 18),
                new Point(contourRect.tl().x - 18,contourRect.tl().y - 18));
        //Rect  rr = new Rect(lk.br(),lk.tl());
        Utils.bitmapToMat(master,mat2);  //转换为矩阵
        if((rectArea.br().x  > mat2.width()) || (rectArea.br().y > mat2.height()) ||
                ((rectArea.tl().x > mat2.width()) && (rectArea.tl().y > mat2.height())))
        {
            Log.e("单个轮廓截取","错误" + "rectArea.size:" + rectArea.size() +
                    "   " + "matMould.size:" + mat2.size());
        }else if(((rectArea.br().x  > 0) && (rectArea.br().y > 0)) && ((rectArea.tl().x > 0) && (rectArea.tl().y > 0)))
        {
            Log.e("《——————截取矩形的大小——————》",rectArea.tl()+ "  " + rectArea.br() + "  "+ mat2.size());
            mat1 = new Mat(mat2,rectArea);     //截取出整个显示屏
            Bitmap grayBmp = Bitmap.createBitmap(mat1.width(), mat1.height(), Bitmap.Config.RGB_565);
            Utils.matToBitmap(mat1, grayBmp);
            new Fill().savePhoto(grayBmp, "截取显示屏"+".png");
            // image1.setImageBitmap(grayBmp);
            rectMax = mat1;
        }
        return  rectMax;
    }


    public   void  oneColorDiscriminate(Mat maxRect, int valhsv)
    {
        Mat mat1 = new Mat(maxRect.height(),maxRect.width(), CvType.CV_8UC3);
        Mat mat2 = new Mat(maxRect.height(),maxRect.width(), CvType.CV_8UC3);

        String formColor = "";
        MatOfPoint temp_contour;   //坐标的形式

        MatOfPoint2f approxCurve = new MatOfPoint2f();
        List<MatOfPoint> lunkuo = new ArrayList<MatOfPoint>();
        tri = 0;
        circle = 0;
        rect = 0;
        wjx = 0;

        Imgproc.cvtColor(maxRect,mat1, Imgproc.COLOR_BGR2HSV);  //转换颜色通道

        //比对范围内的值
        Core.inRange(mat1, new Scalar(HsvValue.HSV_VALUE_LOW[valhsv]),
                new Scalar(HsvValue.HSV_VALUE_HIGH[valhsv]),mat2);
        //开操作 (去除一些噪点)
        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(6, 6));
        Imgproc.morphologyEx(mat2,mat1, Imgproc.MORPH_OPEN, element);
        //闭操作 (连接一些连通域)
        Mat closeelement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
        Imgproc.morphologyEx(mat1,mat2, Imgproc.MORPH_CLOSE, closeelement);

        Imgproc.threshold(mat2,mat1, 150, 255, Imgproc.THRESH_BINARY_INV); //二值化
        Imgproc.medianBlur(mat1,mat1,9);

        Mat copyMat = new Mat(mat1.width(),mat1.height(), CvType.CV_8UC3);
        mat1.copyTo(copyMat);    //拷贝二值图
        Imgproc.cvtColor(copyMat,copyMat, Imgproc.COLOR_GRAY2BGR);
        Scalar scalar = new Scalar(0,0,0);
        switch(valhsv)
        {
            case 0: formColor = "red"; scalar = new Scalar(255,0,0);    break;
            case 1: formColor = "green"; scalar = new Scalar(0,255,0);    break;
            case 2: formColor = "blue"; scalar = new Scalar(0,0,255);     break;
            case 3: formColor = "yellow"; scalar = new Scalar(255,242,0); break;
            case 4: formColor = "purple";scalar = new Scalar(245,10,239); break;
            case 5: formColor = "wathet";scalar = new Scalar(77,220,223); break;
            case 6: formColor = "black"; scalar = new Scalar(0,0,0);      break;
        }
        // Imgproc.drawContours(, lunkuo, i, scala, 5, 8, hd, 0, new Point(0, 0));
        Imgproc.putText(copyMat,formColor,new Point(copyMat.width()/2-80,copyMat.height()-20),
                1,3,scalar);
        Bitmap grayBmp = Bitmap.createBitmap(copyMat.width(), copyMat.height(), Bitmap.Config.RGB_565);
        Utils.matToBitmap(copyMat, grayBmp); //轮廓绘制
        new Fill().savePhoto(grayBmp,"不同颜色二值图"+ valhsv + ".png");

        int  pointNum = 0;
        for (int row=30; row<mat1.cols(); row++)   //判断背景是否为黄色
        {
            if(mat1.get(mat1.rows()-30,row)[0] == 0)
                pointNum++;
            else
                pointNum--;
        }
        if(!(pointNum > 60))
        {
            //轮廓提取
            Imgproc.findContours(mat1, lunkuo, mat2, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
            for (int ind = 0; ind < lunkuo.size(); ind++)  //移除无用轮廓
            {
                temp_contour = lunkuo.get(ind);
                Log.e("轮廓面积：", Imgproc.contourArea(temp_contour) + "");
                if (Imgproc.contourArea(temp_contour) < 1000 || Imgproc.contourArea(temp_contour) > 50000) {
                    lunkuo.remove(ind);   //移除面积小的
                    ind--;
                }
            }

            String formText = "";
            for (int i = 0; i < lunkuo.size(); i++)   //识别图形
            {
                switch (figureDiscriminate(copyMat, lunkuo.get(i), i)[0])
                {
                    case 0:  formText = "rect";  break;
                    case 1:  formText = "tri";   break;
                    case 2:  formText = "cri";     break;
                    case 3:  formText = "five";  break;
                }
                switch(valhsv)
                {
                    case 0: formColor = "red";   break;
                    case 1: formColor = "green";  break;
                    case 2: formColor = "blue";   break;
                    case 3: formColor = "yellow"; break;
                    case 4: formColor = "purple"; break;
                    case 5: formColor = "wathet"; break;
                    case 6: formColor = "black";  break;
                }

                Rect rec;
                rec = Imgproc.boundingRect(lunkuo.get(i));  //轮廓转为矩形轮廓
                Imgproc.putText(drawFigure,formText,new Point(rec.x+25,rec.y+35),
                        3,0.5,new Scalar(255,255,255));
                Imgproc.putText(drawFigure,formColor,new Point(rec.x+20,rec.y+55),
                        3,0.5,new Scalar(255,255,255));
                Bitmap gray = Bitmap.createBitmap(drawFigure.width(), drawFigure.height(), Bitmap.Config.RGB_565);
                Utils.matToBitmap(drawFigure, gray); //轮廓绘制
                new Fill().savePhoto(gray,valhsv + "单个阈值" + i + ".png");
            }
        }
    }
    String[]  STR = {"红色","绿色","蓝色","黄色","品色","青色","黑色","白色"};


    public static double[][]  HSV_VALUE_LOW = {
            {160,90,90},    //红色
            {45,90,90},      //绿色
            {82,90,90},    //蓝色
            {7,90,90 },  //黄色,
            {137,90,90}, //品色
            {78,90,90},   //青色
            {0,0,0},   //黑色
            {0,0,180}, //白色
    };

    public static double[][]  HSV_VALUE_HIGH = {
            {179,255,255},   //红色
            {82,255,255},    //绿色
            {137,255,255},   //蓝色
            {45,255,255 },   //黄色,
            {167,255,255},   //品色
            {97,255,255},    //青色
            {180,255,90},    //黑色
            {180,30,255},    //白色
    };

    public String figuteHandle(Bitmap bitmap){
        MatOfPoint2f contour2f;
        MatOfPoint2f approxCurve;
        double epsilon;
        int tri,rect,circle,orus,zheng;
        String esprust="";


        hsvmat = new Mat();
        Mat mask_r = new Mat();
        Mat mask_r2 = new Mat();
        Mat mask = new Mat();

        String[]  STR = {"红色","绿色","蓝色","黄色","品色","青色","黑色","白色"};

   //     Utils.bitmapToMat(bitmap,dsmat);


        pingmujiequ pingmujiequs=new pingmujiequ();
        dsmat =pingmujiequs.init_carpingmujiequ(bitmap);
        ///////
        resultbitmap = Bitmap.createBitmap(dsmat.width(), dsmat.height(),Bitmap.Config.ARGB_8888);
        Imgproc.cvtColor(dsmat, dsmat,Imgproc.COLOR_BGR2RGB);
        Utils.matToBitmap(dsmat, resultbitmap);
        new Fill().savePhoth(File_Name.FIGURE_PHOTH,resultbitmap);  //保存图片

        Imgproc.cvtColor(dsmat, hsvmat, Imgproc.COLOR_RGB2HSV);

        //              Core.inRange(hsvmat,new Scalar(38,90,90),new Scalar(75,255,255),mask);//转hsv 红
        for(int index=0; index<8; index++) {

            orus=tri=rect=circle=zheng=0;

            Core.inRange(hsvmat, new Scalar(HSV_VALUE_LOW[index]), new Scalar(HSV_VALUE_HIGH[index]),mask_r);

            Core.inRange(hsvmat, new Scalar(HSV_VALUE_LOW[index]), new Scalar(HSV_VALUE_HIGH[index]), mask_r2);

            Core.add(mask_r, mask_r2, mask);


//                Imgproc.medianBlur(mask,hsvmat,7); //中值滤波
//                Imgproc.blur(hsvmat,hsvmat,new Size(3,3),new Point(-1,-1));//均值滤波器

            Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));//运算核

            Imgproc.morphologyEx(mask, hsvmat, Imgproc.MORPH_OPEN, kernel);//开运算
            Imgproc.morphologyEx(hsvmat, hsvmat, Imgproc.MORPH_CLOSE, kernel);//闭运算
            Imgproc.dilate(hsvmat, hsvmat, kernel);
//                这个函数的第一个参数表示内核的形状，有三种形状可以选择。
//                矩形：MORPH_RECT;
//                交叉形：MORPH_CROSS;
//                椭圆形：MORPH_ELLIPSE;

            Mat ops = new Mat();
            Core.bitwise_and(dsmat, dsmat, ops, hsvmat);  //图像区域提取
            Utils.matToBitmap(ops,resultbitmap);

            cartxsb cartxsbs=new cartxsb();
            esprust=cartxsbs.init_cartxsb(resultbitmap,STR[index]);
            esprust+=esprust+"\n";



            //    Mat newMat=new Mat();
            //     Imgproc.cvtColor(ops,newMat, Imgproc.COLOR_BGR2GRAY); //灰度化

//                detectssss(ops,newMat);
//            Utils.matToBitmap(ops, resultbitmap);
//
            //       iv1.setImageBitmap(resultbitmap);
//            Mat outmat = new Mat();

//            Imgproc.findContours(hsvmat, contours, outmat, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);//找轮廓信息保存到一个集合中
//            contoursCounts = contours.size();
//            // System.out.println("轮廓的数量为：" + contoursCounts);
//            Imgproc.drawContours(dsmat, contours, -1, new Scalar(0, 0, 255), 4);//画轮廓
//            for (int i = 0; i < contoursCounts; i++) {
//                if (Imgproc.contourArea(contours.get(i)) > 10) {
//                    contour2f = new MatOfPoint2f(contours.get(i).toArray());  //第几个轮廓转换为数组
//                    epsilon = 0.04 * Imgproc.arcLength(contour2f, true);  //多边形拟合
//                    approxCurve = new MatOfPoint2f(); //拟合完后的数量
//
//                    Imgproc.approxPolyDP(contour2f, approxCurve, epsilon, true);
//                    Rect bb = Imgproc.boundingRect(approxCurve);
//
//
//                    if (approxCurve.rows() == 3) {
//                        tri++;
//                    }
//                    if (approxCurve.rows() == 4) {
//
//                        if (bb.width - bb.height <= 30) {
//                            zheng++;
//                        } else {
//                            rect++;
//                        }
//                    }
//                    if (approxCurve.rows() == 10) {
//                        orus++;
//                    }
//                    if (approxCurve.rows() > 13) {
//                        circle++;
//                    }
//                }
//            }
            //tex1.setText("轮廓:" + contoursCounts + "\n圆形:" + circle + "\n三角:" + tri + "\n矩形:" + rect + "\n五角形:" + orus + "\n正方形" + zheng);
            //System.out.println(STR[index]+"" + "圆形:" + circle +""+ "三角:" + tri + ""+"矩形:" + rect + ""+"五角形:" + orus + ""+ "正方形" + zheng);
//            String opsss=STR[index]+":"+"\t" + "圆形" + circle +"\t"+ "三角形" + tri + "\t"+"矩形" + rect + "\t"+"五角星" + orus + "\t"+ "菱形" + zheng+"\n";
//            esprust+=opsss;



        }

        new Fill().saveFile(File_Name.CarRES_txsb, esprust);  //保存图形识别标志结果

//        System.out.println(esprust);
//        new Fill().saveFile(File_Name.FIGURE_FRUIT,esprust);  //保存识别结果
        return esprust;
    }


    public String figureHandle(Bitmap print)
    {
        String fruitNum = "";
        String colorFruitNum = "";
        String color = "";

        int triangle = 0;
        int circular = 0;
        int rectangle = 0;
        int fiveangle = 0;


        Mat maxRect = new Mat();
        Utils.bitmapToMat(print,maxRect);
        maxRect = new Figure().cutRectangle1(maxRect,true);  //截取显示屏
        print = print.createScaledBitmap(print,960,540,true);  //放大图片

        drawFigure = maxRect;

        for (int i=0; i<7; i++)
        {
            oneColorDiscriminate(maxRect,i);  //单个颜色图形识别
            switch(i)
            {
                case 0: color = "红色"; break;
                case 1: color = "绿色"; break;
                case 2: color = "蓝色"; break;
                case 3: color = "黄色"; break;
                case 4: color = "紫色"; break;
                case 5: color = "浅蓝"; break;
                case 6: color = "黑色"; break;
            }
            colorFruitNum += color + " " + "矩形:" + rect + " " + "三角形:" +  tri +" " + "圆形:" + circle + " " + "五角星:" + wjx + "\n";
            rectangle += rect;
            triangle  += tri;
            circular  += circle;
            fiveangle += wjx;
        }
        fruitNum = "矩形:" + rectangle + " " + "三角形:" +  triangle +" " + "圆形:" + circular + " " + "五角星:" + fiveangle;
        Log.e("图形个数：", "矩形:" + rectangle + " " + "三角形:" +  triangle +" " + "圆形:" + circular + " " + "五角星:" + fiveangle );
        try {
            new Fill().saveToSDCard("单个识别结果.txt",colorFruitNum);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("单个识别结果", colorFruitNum + "");

//            Mat fruit = cutRectangle(print);
//            Imgproc.putText(fruit,"rect",new Point(rectContour.get(0).width(),rectContour.get(0).height()),3,0.2,new Scalar(255,255,255));
//            Imgproc.fillPoly(fruit,rectContour,new Scalar(255,255,255));
//            Bitmap grayBmp = Bitmap.createBitmap(fruit.width(), fruit.height(), Bitmap.Config.RGB_565);
//            Utils.matToBitmap(fruit, grayBmp); //轮廓绘制
//            new Fill().savePhoto(grayBmp,"所有矩形.png");
//
//            fruit = cutRectangle(print);
//            Imgproc.fillPoly(fruit,triContour,new Scalar(255,255,255));
//            grayBmp = Bitmap.createBitmap(fruit.width(), fruit.height(), Bitmap.Config.RGB_565);
//            Utils.matToBitmap(fruit, grayBmp); //轮廓绘制
//            new Fill().savePhoto(grayBmp,"所有三角形.png");
//
//            fruit = cutRectangle(print);
//            Imgproc.fillPoly(fruit,circleContour,new Scalar(255,255,255));
//            grayBmp = Bitmap.createBitmap(fruit.width(), fruit.height(), Bitmap.Config.RGB_565);
//            Utils.matToBitmap(fruit, grayBmp); //轮廓绘制
//            new Fill().savePhoto(grayBmp,"所有圆形.png");
//
//            fruit =cutRectangle(print);
//            Imgproc.fillPoly(fruit,fiveAngleContour,new Scalar(255,255,255));
//            grayBmp = Bitmap.createBitmap(fruit.width(), fruit.height(), Bitmap.Config.RGB_565);
//            Utils.matToBitmap(fruit, grayBmp); //轮廓绘制
//            new Fill().savePhoto(grayBmp,"所有五角星.png");
//
        Bitmap grayBmp = Bitmap.createBitmap(drawFigure.width(), drawFigure.height(), Bitmap.Config.RGB_565);
        Utils.matToBitmap(drawFigure,grayBmp); //轮廓绘制
        // new MainActivity().image2.setImageBitmap(grayBmp);
        new Fill().savePhoto(grayBmp,"识别绘制图.png");
        return fruitNum;
    }




    public static String filehad(){
        String dis_fruit = "";

        HsvValue.Set_Hsv_Value(File_Name.FIGURE_HSV_VALUE);   //设置HSV的值


        return dis_fruit;
    }



    //2021/4/13/华顶峰
    public void colorAndShape()  {// XXX opencv图形识别

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            OpenCvUtils cvut = new OpenCvUtils();
            ArrayList<ColorShapeInfo> resultList = new ArrayList<>();
            int erzhi = 130;



            @Override
            public void run() {//说明：主要的调用在这里哈，线程那块可以忽略不计
                Log.d("pzj", "in to cv !!");
                Bitmap bitmap_buf = bitmap;
                new Fill().savePhoth(File_Name.FIGURE_PHOTH,bitmap_buf);  //保存图片
                Bitmap print = Bitmap.createScaledBitmap(bitmap_buf,960
                        ,540,true);  //放大图片
                Mat maxRect = new Mat();
                Utils.bitmapToMat(print,maxRect);
                maxRect = new Figure().cutRectangle1(maxRect,true);  //截取显示屏

                //    new FileService().savePhoto(bitmap, "CV/doit.png");
                //    Mat image = cvut.getImageAsFile("CV/doit.png");
                //    Mat image = cvut.bitmapToMat(print);

                Mat m1 = cvut.cloneMat(maxRect);
                Mat declarMat = cvut.cloneMat(maxRect);
                cvut.toReverseColorMat(maxRect);// 反色
                cvut.toGaussUnClearMat(declarMat, 9, 9, 9);// 模糊图像，用于推测颜色
                Mat m2 = cvut.cloneMat(declarMat);
                Mat srcWrite = cvut.cloneMat(declarMat);
                cvut.toDilate(maxRect, 1, 1, 1);// 膨胀
                cvut.toGrayMat(maxRect);// 灰度化
                Mat binary = cvut.cloneMat(maxRect);
                cvut.toBinaryMat(binary, erzhi, 255);// 二值化
                cvut.coverBackGroundToBlack(binary);// 背景变黑
                cvut.saveMatAsPngFileAndTimestamp(binary, "binary");// SAVE
                List<MatOfPoint> contoursList = cvut.findContoursList(binary);// 找到轮廓
                for (int i = 0; i < contoursList.size()
                        && contoursList.size() <= 9; i++) {// 处理单个轮廓
                    Mat mat = cvut.makeBGRMat(0, 0, 0);// 黑色背景
                    cvut.drawContoursToMat(mat, contoursList, i, 0, 255, 0, 1);// 白色描边
                    Point[] points = cvut.findp(mat);// 找到描边点
                    Point[] checkedPoints = cvut.checkPoint(points);// 清除同一条直线上的点

                    //ColorShapeInfo csi = ;
                    //String shape = cvut.findShape(checkedPoints);// 确定形状
                    String color = cvut.findColor(declarMat, checkedPoints);// 确定颜色
                    System.out.println("111111"+color);

                    resultList.add(new ColorShapeInfo(cvut.findColor(declarMat, checkedPoints), cvut.findShape(checkedPoints)));
                    for (int j = 0; j < checkedPoints.length; j++) {
                        cvut.drawCircleByPoint(mat, checkedPoints[j]);

                    }
                    cvut.saveMatAsPngFile(mat, "Shape" + i);


                    Bitmap grayBmp = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.RGB_565);
                    Utils.matToBitmap(mat, grayBmp);

                    new Fill().savePhoto(grayBmp, "单个颜色提取"+COLOR_NAME[i]+".png");


                }
                if (resultList.size() != 0) {
                    Log.d("CvResoult", Arrays.toString(resultList.toArray()));
                    cvut.saveMatAsPngFileAndTimestamp(m2, "declar");// SAVE
                    cvut.saveMatAsPngFileAndTimestamp(m1, "image");// SAVE


                    Bitmap grayBmps = Bitmap.createBitmap(m1.width(), m1.height(), Bitmap.Config.RGB_565);
                    Utils.matToBitmap(m1, grayBmps);
                    new Fill().savePhoth(File_Name.FIGURE_PHOTH,grayBmps);  //保存图片


                    timer.cancel();
                    this.cancel();
                } else {
                    resultList.clear();
                    if (erzhi <= 180) {
                        erzhi += 5;
                    } else {
                        erzhi = 150;
                    }
                }
            }
        }, 0, 200);
    }
    //2021/6.7华顶峰

    //  静态图形
    public static String  Feelsdsds(){
        iol = "123";

//        MainActivity mainActivity=new MainActivity();
        //       mainActivity.initPermission();

 //       MainActivity.innner();

        ControlCommand controlCommand=new ControlCommand();
        controlCommand.yanchi(30000);

        try {
            iol=new Fill().carRead(File_Name.FIGURE_FRUIT);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return iol;
    }

    //2021/5.27华顶峰

    public static String colorins(){
        OpenCvUtils cvut = new OpenCvUtils();
        ArrayList<ColorShapeInfo> resultList = new ArrayList<>();
        Bitmap bitmap_buf = bitmap;
        new Fill().savePhoth(File_Name.FIGURE_PHOTH,bitmap_buf);  //保存图片
        Bitmap print = Bitmap.createScaledBitmap(bitmap_buf,960
                ,540,true);  //放大图片
        Mat maxRect = new Mat();

        Utils.bitmapToMat(print,maxRect);
        //       maxRect = new Figure().cutRectangle1(maxRect,true);  //截取显示屏
        Mat declarMat = cvut.cloneMat(maxRect);

        Mat mat = cvut.makeBGRMat(0, 0, 0);// 黑色背景
        //       cvut.drawContoursToMat(mat, contoursList, i, 0, 255, 0, 1);// 白色描边
        Point[] points = cvut.findp(mat);// 找到描边点
        Point[] checkedPoints = cvut.checkPoint(points);// 清除同一条直线上的点


        String color = cvut.findColor(declarMat, checkedPoints);// 确定颜色
        System.out.println(color);

        return color;

    }

    public static String Filsdsds(){
        ssooso = "";
        //       MainActivity mainActivity=new MainActivity();
        //       mainActivity.initPermission();

//        MainActivity.ioust();

        ControlCommand controlCommand=new ControlCommand();
        controlCommand.yanchi(7000);


        try {
            ssooso=new Fill().carRead(File_Name.JT_BZ);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ssooso;
    }

    //2021/5.8/华顶峰
    public static String Filter_Handle(){
        iol = "123";

        //      MainActivity mainActivity=new MainActivity();
        //      mainActivity.initPermission();

 //       MainActivity.initstr();

        ControlCommand controlCommand=new ControlCommand();
        controlCommand.yanchi(40000);

        try {
            iol=new Fill().carRead(File_Name.FIGURE_FRUIT);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return iol;
    }





//    /********************图形识别调用C++*********************/
    final  static String[] COLOR_NAME = {"红色：","绿色：","蓝色：","黄色："
            ,"紫色：","浅蓝：","黑色：","白色："};
//    public static String Filster_Handle() {
//        List<MatOfPoint> lunkuo = new ArrayList<MatOfPoint>();
//        Mat  mat_buf = new Mat();
//        Mat mat_buf1 = new Mat();
//        Mat  mat_wq = new Mat();
//        OpenCvUtils cvut = new OpenCvUtils();
//        String dis_fruit = "";
//        String dis_buf = "";
//        HsvValue.Set_Hsv_Value(File_Name.FIGURE_HSV_VALUE);   //设置HSV的值
//        try{
//            Bitmap bitmap_buf = bitmap;
//
//
//
//            Bitmap print = Bitmap.createScaledBitmap(bitmap_buf,960
//                    ,540,true);  //放大图片
//            Mat maxRect = new Mat();
//            Utils.bitmapToMat(print,maxRect);
//            maxRect = new Figure().cutRectangle1(maxRect,true);  //截取显示屏
//
//            Mat m1 = cvut.cloneMat(maxRect);
//            Bitmap grayBmps = Bitmap.createBitmap(m1.width(), m1.height(), Bitmap.Config.RGB_565);
//            Utils.matToBitmap(m1, grayBmps);
//            new Fill().savePhoth(File_Name.FIGURE_PHOTH,grayBmps);  //保存图片
//
//            Imgproc.blur(maxRect,maxRect,new Size(3,3));
//            Imgproc.cvtColor(maxRect,mat_buf, Imgproc.COLOR_BGR2HSV);  //转换颜色通道
//            for(int index=0; index<8; index++){
//                //比对范围内的值
//                Core.inRange(mat_buf, new Scalar(HsvValue.HSV_VALUE_LOW[index]),
//                        new Scalar(HsvValue.HSV_VALUE_HIGH[index]),mat_buf1);
//                Mat matBuf = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(5,5));
//                Imgproc.morphologyEx(mat_buf1, mat_buf1,Imgproc.MORPH_OPEN, matBuf);	//进行形态学操作 开运算
//
//                int  pointNum = 0;
//                for (int col=35; col<mat_buf1.cols(); col++)   //判断背景是否有黑色像素
//                {
//                    if(mat_buf1.get(mat_buf1.rows()-35,col)[0] == 255)
//                        pointNum++;
//                    else
//                        pointNum--;
//                }
//                if(!(pointNum > 60)) {
//                    if(index == 6)    //黑色需要处理
//                    {
//                        int  index_flag = 0;
////                    pointNum = 0;
//                        index_flag =(int)( mat_buf1.rows()*0.07);
//                        for(int row=0; row<index_flag; row++)
//                        {
//                            for(int col=0; col<mat_buf1.cols(); col++ )
//                            {
//                                Log.e(TAG,mat_buf1.get(row,col)[0] + " ");
//                                if(mat_buf1.get(row,col)[0] == 255)
//                                {
//                                    mat_buf1.put(row,col,0);
//                                }
//                            }
//                        }
//
//                        index_flag =(int)( mat_buf1.cols()*0.04);
//                        for(int col=0; col<index_flag; col++ )     //x轴
//                        {
//                            for(int row=0; row<mat_buf1.rows(); row++)
//                            {
//                                if(mat_buf1.get(row,col)[0] != 0)
//                                {
//                                    mat_buf1.put(row,col,0);
//                                }
//                            }
//                        }
//                        index_flag =(int)( mat_buf1.cols()*0.96);
//                        for(int col=mat_buf1.cols()-1; col>index_flag; col-- )
//                        {
//                            for(int row=0; row<mat_buf1.rows(); row++)
//                            {
//                                if(mat_buf1.get(row,col)[0] != 0)
//                                {
//                                    mat_buf1.put(row,col,0);
//                                }
//                            }
//                        }
//                        index_flag =(int)( mat_buf1.rows() * 0.93);
//                        for(int row=mat_buf1.rows()-1; row>index_flag; row--)
//                        {
//                            for(int col=0; col<mat_buf1.cols(); col++ )
//                            {
//                                if(mat_buf1.get(row,col)[0] != 0)
//                                {
//                                    mat_buf1.put(row,col,0);
//                                }
//                            }
//                        }
//                    }
//                    double white_ratio = Color_ratio(mat_buf1);             //获得当前颜色占总像素的比例
//                    if(white_ratio >= 0.35) continue;                       //剔除背景色
////                        int judge =0; int judge_num =0;
//                    //                       for (int i=0;i<10;i++)                                                      //判断四个角 向内数10 个像素颜色
//                    //                  { if(mat_buf1.get(10,i)[0] == 255)  judge_num++ ; }
//                    //                    if (judge_num >= 5) judge++ ;  judge_num = 0;
//                    //                    for (int i=0;i<10;i++)
//                    //                     { if(mat_buf1.get(10,mat_buf1.cols() - i-1)[0] == 255)  judge_num++ ; }
//                    //                    if (judge_num >= 5) judge++ ;  judge_num = 0;
//                    //                     for (int i=0;i<10;i++)
//                    //                      { if(mat_buf1.get(mat_buf1.rows()-11,i)[0] == 255)  judge_num++ ; }
//                    //                      if (judge_num >= 5) judge++ ;  judge_num = 0;
//                    //                       for (int i=0;i<10;i++)
//                    //                       { if(mat_buf1.get(mat_buf1.rows()-11,mat_buf1.cols() - i-1)[0] == 255)  judge_num++ ; }
//                    //                       if (judge_num >= 5) judge++ ;
//                    //                       if (judge >= 1)                                                             //如果有两个角判断到白色 就跳出当前颜色的图形判断
//                    //                          continue;
//                    Mat  buf_buf = new Mat();
//                    mat_buf1.copyTo(buf_buf);
//                    String str_buf = Form_Dis(buf_buf.nativeObj);  //识别当前颜色所有图形
//                    if(!str_buf.equals(""))
//                    {
//                        dis_buf += COLOR_NAME[index];
//                        dis_buf += str_buf;
//                        dis_fruit += dis_buf + "\n";
//                        dis_buf = "";
//                    }
//                    // getMat(mat_wq.nativeObj);
//                    Bitmap grayBmp = Bitmap.createBitmap(mat_buf1.width(), mat_buf1.height(), Bitmap.Config.RGB_565);
//                    Utils.matToBitmap(mat_buf1, grayBmp);
//                    new Fill().savePhoto(grayBmp, "单个颜色提取"+COLOR_NAME[index]+".png");
//                }else {
//                    continue;
//                }
//            }
//        }catch(Exception e){
//            Log.e(TAG,"图形识别错误");
//        }
//
//        //     Mat mat = new Mat();
//        //      Utils.bitmapToMat(MainActivity.bitmap,mat);
//        //     dis_fruit = Form_Dis(mat.nativeObj);
//        //     Mat return_mat = new Mat();
//        //      Get_Mat(return_mat.nativeObj);
//        //    Bitmap grayBmp = Bitmap.createBitmap(return_mat.width(), return_mat.height(), Bitmap.Config.RGB_565);
//        //     Utils.matToBitmap(return_mat, grayBmp);
//        //     image_filter_view.setImageBitmap(grayBmp);
//        //       new Fill().savePhoto(grayBmp, "单个.png");
//        new Fill().saveFile(File_Name.FIGURE_FRUIT,dis_fruit);  //保存识别结果
//        return dis_fruit;
//    }








    /***
     * 颜色占图像比例（一半）
     * @param mat_1  输入图像
     * @return
     */
    public static double Color_ratio(Mat mat_1){
        int white_num = 0;
        int all_num = 0;
        double white_ratio = 0;
        int rowNumber = mat_1.rows();//行数
        int colNumber = mat_1.cols();//每一行元素个数 = 列数 x 通道数
        for (int i = 0; i < rowNumber; i++)//行循环
        {
            for (int j = 0; j < colNumber/3; j++)//列循环
            {
                if( (mat_1.get(i,j)[0] == 255) ) {
                    white_num++;
                }
                all_num++;
            }
        }
        white_ratio = (white_num*1.0) / all_num;
//        all_num = 0; white_num = 0;
        return  white_ratio;
    }








//    public static int Traffic_place()
//    {
//        int renum = 0;
//        Mat input_A = new Mat();
//        Mat input_B = new Mat();
//        try{
//            Utils.bitmapToMat( Fill.readPhoto("图形识别/图片/","交通灯定位1.png"),input_A);
//            Utils.bitmapToMat( Fill.readPhoto("图形识别/图片/","交通灯定位2.png"),input_B);
//            renum = Traffic_Place(input_A.nativeObj,input_B.nativeObj);
//        }catch (Exception e){
//
//            Log.e("交通灯: ","交通灯定位报错" );
//        }
//
//        return renum;
//    }
//
//    public static int Traffic_dim()
//    {
//        int renum = 0;
//        String  Traffic_nameFile =  "图形识别/图片/";
//        Mat input_A = new Mat();
//        Mat input_B = new Mat();
//        Mat input_C = new Mat();
//
//        try{
//            Utils.bitmapToMat( Fill.readPhoto("图形识别/图片/","交通灯定位1.png"),input_A);
//            Utils.bitmapToMat( Fill.readPhoto("图形识别/图片/","交通灯定位2.png"),input_B);
//            Utils.bitmapToMat( Fill.readPhoto("图形识别/图片/","交通灯识别.png"),input_C);
//
//            renum = Traffic_Dim(input_A.nativeObj,input_B.nativeObj,input_C.nativeObj);
//            Bitmap bit = Bitmap.createBitmap(input_A.width(), input_A.height(), Bitmap.Config.RGB_565);
//            Utils.matToBitmap(input_A,bit);
//            Fill.savePhoto(Traffic_nameFile, "lalala.png", bit);
//            Image.saveBitmapFile(bit);
//        }catch(Exception e){
//            renum = 1;
//            Log.e(TAG,"交通灯识别错误");
//        }
//
//        return renum;
//    }


//    public static int Figure_dim(int num){
//
//        Bitmap left,rigth,front,bitmap;  //左，右，正前 ,现在
//        int result_num = 0;
//        double result_probability = 65535;
//        double cache = 0;
//        try{
//            //获取当前图片
//            bitmap = MainActivity.bitmap;
//            Mat maxRect = new Mat();
//            Utils.bitmapToMat(bitmap,maxRect);
//            maxRect = new Figure().cutRectangle1(maxRect,true);  //截取显示屏
//            Bitmap newBitMap = Bitmap.createBitmap(maxRect.width(),maxRect.height(),Bitmap.Config.ARGB_8888);
//            Utils.matToBitmap(maxRect,newBitMap);
//
//            for (int i = 1; i <= num; i++) {
//                left = Fill.readPhoto("图形识别/图片/",i+"left.jpg");
//                rigth = Fill.readPhoto("图形识别/图片/",i+"right.jpg");
//                front = Fill.readPhoto("图形识别/图片/",i+"center.jpg");
//                cache = Image_Contrast(left,newBitMap);
//                if(cache < result_probability) {
//                    result_probability = cache;
//                    result_num = i;
//                }
//                Log.e(TAG,cache+"左");
//                cache = Image_Contrast(rigth,newBitMap);
//                if(cache < result_probability){
//                    result_probability = cache;
//                    result_num = i;
//                }
//                Log.e(TAG,cache+"右");
//                cache = Image_Contrast(front,newBitMap);
//                if(cache < result_probability){
//                    result_probability = cache;
//                    result_num = i;
//                }
//                Log.e(TAG,cache+"正");
//            }
//
//        }catch (Exception e){
//            Log.e(TAG,cache+"错误");
//        }
//
//        return result_num;
//    }


//
//    public static int Figure_CPorTX(){
//        Bitmap bitmap;
//        int result_num = 0;
//        try{
//            //获取当前图片
//            bitmap = MainActivity.bitmap;
////            Mat maxRect = new Mat();
////            Utils.bitmapToMat(bitmap,maxRect);
////            maxRect = new Figure().cutRectangle1(maxRect,true);  //截取显示屏
////            Utils.matToBitmap(maxRect,bitmap2);
//            result_num = CPorTX(bitmap);
//
//        }catch (Exception e){
//            Log.e(TAG,"CPorTX:错误");
//        }
//
//        return result_num;          //车牌1  图形0
//    }


//    public static int Figure_dim(int num){
//
//        Bitmap left,rigth,front,bitmap;  //左，右，正前 ,现在
//        int result_num = 0;
//        double result_probability = 65535;
//        double cache = 0;
//        try{
//            //获取当前图片
//            bitmap = MainActivity.bitmap;
//            Mat maxRect = new Mat();
//            Utils.bitmapToMat(bitmap,maxRect);
//            maxRect = new Figure().cutRectangle1(maxRect);  //截取显示屏
//            Bitmap newBitMap = Bitmap.createBitmap(maxRect.width(),maxRect.height(),Bitmap.Config.ARGB_8888);
//            Utils.matToBitmap(maxRect,newBitMap);
//
//            for (int i = 1; i <= num; i++) {
////                left = Fill.readPhoto("图形识别/图片/",num+"左.jpg");
////                rigth = Fill.readPhoto("图形识别/图片/",num+"右.jpg");
//                front = Fill.readPhoto("图形识别/图片/",i+"正.jpg");
////                cache = Image_Contrast(left,bitmap);
////                if(cache > result_probability) {
////                    result_probability = cache;
////                    result_num = i;
////                }
////                cache = Image_Contrast(rigth,bitmap);
////                if(cache > result_probability){
////                    result_probability = cache;
////                    result_num = i;
////                }
//                cache = Image_Contrast(front,newBitMap);
//                Log.e(TAG,cache+"");
//                if(cache < result_probability){
//                    result_probability = cache;
//                    result_num = i;
//                }
//            }
//
//        }catch (Exception e){
//            Log.e(TAG,cache+"错误");
//        }
//
//
//
//        return result_num;
//    }

}
