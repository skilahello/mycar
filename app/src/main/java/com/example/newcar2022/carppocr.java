package com.example.newcar2022;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

public class carppocr {
    private static final int SELECT_IMAGE = 1;
    private Mat srcmat,dsmat,hsvmat,carmat;
    private Bitmap resultbitmap;
    private YoloV5Ncnn yolov5ncnn = new YoloV5Ncnn();

    private Bitmap yourSelectedImage = null;

    private Bitmap res,bitmapss;


    private Bitmap bitmap = null;
    private String result="国A12345";


    public String init_carppocr(Bitmap bitmapss,int flag){

        new Fill().savePhoth(File_Name.PLATE_NUMBER, bitmapss);  //保存车牌原图片

        bitmap=bitmapss;
        yourSelectedImage=bitmap;
        YoloV5Ncnn.Objs[] objectss = yolov5ncnn.DetectS(yourSelectedImage, false);

        String str=showObjectsa(objectss,bitmap,flag);

        return str;



    }


    private String showObjectsa(YoloV5Ncnn.Objs[] objects, Bitmap bitmapss,int flag) {
        if (objects == null) {


        }

        // draw objects on bitmap
        Bitmap rgba = bitmapss.copy(Bitmap.Config.ARGB_8888, true);

        final int[] colors = new int[]{
                Color.rgb(54, 67, 244),
                Color.rgb(99, 30, 233),
                Color.rgb(176, 39, 156),
                Color.rgb(183, 58, 103),
                Color.rgb(181, 81, 63),
                Color.rgb(243, 150, 33),
                Color.rgb(244, 169, 3),
                Color.rgb(212, 188, 0),
                Color.rgb(136, 150, 0),
                Color.rgb(80, 175, 76),
                Color.rgb(74, 195, 139),
                Color.rgb(57, 220, 205),
                Color.rgb(59, 235, 255),
                Color.rgb(7, 193, 255),
                Color.rgb(0, 152, 255),
                Color.rgb(34, 87, 255),
                Color.rgb(72, 85, 121),
                Color.rgb(158, 158, 158),
                Color.rgb(139, 125, 96)
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
        textpaint.setTextSize(56);
        textpaint.setTextAlign(Paint.Align.LEFT);

        for (int i = 0; i < objects.length; i++) {
            paint.setColor(colors[i % 19]);

            //canvas.drawRect(objects[i].x, objects[i].y, objects[i].x + objects[i].w, objects[i].y + objects[i].h, paint);
            canvas.drawLine(objects[i].x0, objects[i].y0, objects[i].x1, objects[i].y1, paint);
            canvas.drawLine(objects[i].x1, objects[i].y1, objects[i].x2, objects[i].y2, paint);
            canvas.drawLine(objects[i].x2, objects[i].y2, objects[i].x3, objects[i].y3, paint);
            canvas.drawLine(objects[i].x3, objects[i].y3, objects[i].x0, objects[i].y0, paint);
            // draw filled text inside image
            {
                String text = objects[i].label;// + " = " + String.format("%.1f", objects[i].prob * 100) + "%";
                System.out.println(text);


                result = text;
                if (flag == 1) {
                    if (!result.equals("") || (result.length() >= 5)) {
                        result = result.replace(".", "");
                        result = result.replace("|", "");
                        result = result.replace("-", "");
                        result = result.replace("_", "");
                        result = "国" + result.substring(1);
                        result = CarPlateCheck(result);
                        System.out.println(result);
                        new Fill().saveFile(File_Name.PLATE_FRUIT, result);  //保存识别结果

                        float text_width = textpaint.measureText(text);
                        float text_height = -textpaint.ascent() + textpaint.descent();

                        float x = objects[i].x0;
                        float y = objects[i].y0 - text_height;
                        if (y < 0)
                            y = 0;
                        if (x + text_width > rgba.getWidth())
                            x = rgba.getWidth() - text_width;

                        canvas.drawRect(x, y, x + text_width, y + text_height, textbgpaint);

                        //      canvas.drawText(text, x, y - textpaint.ascent(), textpaint);
                    }
                }else{
                    System.out.println(result);
                    new Fill().saveFile(File_Name.PLATE_FRUIT, result);  //保存识别结果
                    float text_width = textpaint.measureText(text);
                    float text_height = -textpaint.ascent() + textpaint.descent();

                    float x = objects[i].x0;
                    float y = objects[i].y0 - text_height;
                    if (y < 0)
                        y = 0;
                    if (x + text_width > rgba.getWidth())
                        x = rgba.getWidth() - text_width;

                    canvas.drawRect(x, y, x + text_width, y + text_height, textbgpaint);
                }
            }
            new Fill().savePhoth(File_Name.Carppocr, rgba);  //保存文字识别图片
        }

        return result;
    }



    /*********************用于矫正车牌识别结果***********************/
    String CarPlateCheck(String str) {
        String res = " ";//   返回结果
        String Carstr[] = str.split("");//   车牌 前面
        for (int i = 3; i < 8; i++)// 检查 数字 位  //h358b2
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








}
