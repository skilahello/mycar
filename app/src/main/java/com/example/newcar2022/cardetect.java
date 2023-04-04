package com.example.newcar2022;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.widget.ImageView;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import android.content.Context;
import java.io.IOException;

public class cardetect {
    private static final int SELECT_IMAGE = 1;
    private Mat srcmat,dsmat,hsvmat,carmat;
    private Bitmap resultbitmap;
    private YoloV5Ncnn yolov5ncnn = new YoloV5Ncnn();

    private Bitmap yourSelectedImage = null;

    private Bitmap res,bitmapss;


    private Bitmap bitmap = null;
    private String os;


    public String init_cardete(Bitmap bitmapss){

        new Fill().savePhoth(File_Name.Car_NUMBER, bitmapss);  //保存车型原图片


        bitmap=bitmapss;
        yourSelectedImage=bitmap;
        YoloV5Ncnn.Obj[] objects = yolov5ncnn.Detect(yourSelectedImage, false);

        String str=showObjects(objects,bitmap);
        return str;



    }

    private String showObjects(YoloV5Ncnn.Obj[] objects,Bitmap bitmaps)
    {

        if (objects == null)
        {
            bitmap=bitmaps;

        }

        // draw objects on bitmap
        Bitmap rgba = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        final int[] colors = new int[] {
                Color.rgb( 54,  67, 244),
                Color.rgb( 99,  30, 233),
                Color.rgb(176,  39, 156),
                Color.rgb(183,  58, 103),
                Color.rgb(181,  81,  63),
                Color.rgb(243, 150,  33),
                Color.rgb(244, 169,   3),
                Color.rgb(212, 188,   0),
                Color.rgb(136, 150,   0),
                Color.rgb( 80, 175,  76),
                Color.rgb( 74, 195, 139),
                Color.rgb( 57, 220, 205),
                Color.rgb( 59, 235, 255),
                Color.rgb(  7, 193, 255),
                Color.rgb(  0, 152, 255),
                Color.rgb( 34,  87, 255),
                Color.rgb( 72,  85, 121),
                Color.rgb(158, 158, 158),
                Color.rgb(139, 125,  96)
        };

        Canvas canvas = new Canvas(rgba);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);

        Paint textbgpaint = new Paint();
        textbgpaint.setColor(Color.WHITE);
        textbgpaint.setStyle(Paint.Style.FILL);

        Paint textpaint = new Paint();
        textpaint.setColor(Color.BLACK);
        textpaint.setTextSize(26);
        textpaint.setTextAlign(Paint.Align.LEFT);

        for (int i = 0; i < objects.length; i++)
        {

            String ops=objects[i].label;
            System.out.println(ops);

//            ops.equals("car") || ops.equals("bicycle") || ops.equals("truck")  ||


            if (ops.equals("motorcycle") || ops.equals("car") || ops.equals("bicycle") || ops.equals("truck") ) {

                paint.setColor(colors[i % 19]);

                canvas.drawRect(objects[i].x, objects[i].y, objects[i].x + objects[i].w, objects[i].y + objects[i].h, paint);
            //    System.out.println(objects[i].x+"a"+objects[i].y+"a"+objects[i].x + objects[i].w+"a"+objects[i].y + objects[i].h);

                int w=(int)(Math.round(objects[i].w));
                int x = (int)(Math.round(objects[i].x));
                int y = (int)(Math.round(objects[i].y));
                int h=(int)(Math.round(objects[i].h));

                srcmat=new Mat();
                carmat=new Mat();
                Utils.bitmapToMat(bitmap,carmat);

                Imgproc.cvtColor(carmat,carmat,Imgproc.COLOR_BGR2RGB);







                srcmat= carmat;



                Rect rect=new Rect(x,y,w,h);
                res = cutRectangleres(srcmat,rect);

                new Fill().savePhoth(File_Name.CarRESULT_NUMBER, res);  //保存车型图片

                carppocr carppocr=new carppocr();
                os=carppocr.init_carppocr(res,1);
//                if (os == null  || os.equals("")){
//
//                }
                //lpr(res);







                // draw filled text inside image
                {
                    String text = objects[i].label + " = " + String.format("%.1f", objects[i].prob * 100) + "%";
                    System.out.println(objects[i].label);
                    String opss = objects[i].label;
                    new Fill().saveFile(File_Name.CarRESULT_NU, opss);  //保存车型结果



//                    float text_width = textpaint.measureText(text);
//                    float text_height = -textpaint.ascent() + textpaint.descent();
//
//                    float x = objects[i].x;
//                    float y = objects[i].y - text_height;
//                    if (y < 0)
//                        y = 0;
//                    if (x + text_width > rgba.getWidth())
//                        x = rgba.getWidth() - text_width;
//
//                Rect rect=new Rect(x,y,w,h);
//                Bitmap res=cutRectangleres(srcmat,rect);
//                    canvas.drawRect(x, y, x + text_width, y + text_height, textbgpaint);
//
//                    canvas.drawText(text, x, y - textpaint.ascent(), textpaint);


                }
            }else {
                os=null;
            }

        }
        return os;



    }




    public Bitmap cutRectangleres(Mat srcmat, Rect rect){
        dsmat = new Mat(srcmat,rect);
        //    dsmat=srcmat;
        resultbitmap = Bitmap.createBitmap(dsmat.width(), dsmat.height(),Bitmap.Config.ARGB_8888);
        Imgproc.cvtColor(dsmat, dsmat,Imgproc.COLOR_BGR2RGB);
        Utils.matToBitmap(dsmat, resultbitmap);
        return resultbitmap;
    }

}
