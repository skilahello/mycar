package com.example.newcar2022;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class testActiv extends AppCompatActivity {
    private Button but1,but2,but3,but4;
    private ImageView iv1;
    private TextView tex1;
    private Mat srcmat,dsmat,hsvmat;
    private Bitmap resultbitmap;
    private List<MatOfPoint> contours=new ArrayList<>();
    private int contoursCounts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testact);
        initloadOpencv();
        initandro();




    }

    private void initandro(){
        but1=findViewById(R.id.button_1);
        but2=findViewById(R.id.button_2);
        but3=findViewById(R.id.button_3);
        but4=findViewById(R.id.button_4);
        iv1=findViewById(R.id.image_1);
        tex1=findViewById(R.id.text_1);
        srcmat=new Mat();

        try {
            srcmat= Utils.loadResource(this, R.drawable.img);

        } catch (IOException e) {
            e.printStackTrace();
        }

        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //手动
                Rect rect=new Rect(142,81,306,170);
                dsmat=new Mat(srcmat,rect);
                resultbitmap=Bitmap.createBitmap(dsmat.width(),dsmat.height(),Bitmap.Config.ARGB_8888);
                Imgproc.cvtColor(dsmat,dsmat,Imgproc.COLOR_BGR2RGB);
                Utils.matToBitmap(dsmat,resultbitmap);
                iv1.setImageBitmap(resultbitmap);
            }
        });

        but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hsvmat=new Mat();
                Imgproc.cvtColor(dsmat,hsvmat,Imgproc.COLOR_RGB2HSV);
                Core.inRange(hsvmat,new Scalar(38,90,90),new Scalar(75,255,255),hsvmat);//转hsv 红
                Mat kernel=Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(3,3));//运算核
                Imgproc.morphologyEx(hsvmat,hsvmat,Imgproc.MORPH_OPEN,kernel);//开运算
                Imgproc.morphologyEx(hsvmat,hsvmat,Imgproc.MORPH_CLOSE,kernel);//闭运算

                Utils.matToBitmap(hsvmat,resultbitmap);
                iv1.setImageBitmap(resultbitmap);

            }
        });
        but3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mat outmat=new Mat();
                Imgproc.findContours(hsvmat,contours,outmat,Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE);//找轮廓信息保存到一个集合中
                contoursCounts=contours.size();
                System.out.println("轮廓的数量为："+contoursCounts);
                Imgproc.drawContours(dsmat,contours,-1,new Scalar(0,0,255),4);//画轮廓
                Utils.matToBitmap(dsmat,resultbitmap);
                iv1.setImageBitmap(resultbitmap);
            }
        });
        but4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MatOfPoint2f contour2f;
                MatOfPoint2f approxCurve;
                double epsilon;
                int tri,rect,circle;

                tri=rect=circle=0;

                for (int i=0;i<contoursCounts;i++){
                    if(Imgproc.contourArea(contours.get(i))>10) {
                        contour2f = new MatOfPoint2f(contours.get(i).toArray());  //第几个轮廓转换为数组
                        epsilon = 0.04 * Imgproc.arcLength(contour2f, true);  //多边形拟合
                        approxCurve = new MatOfPoint2f(); //拟合完后的数量
                        Imgproc.approxPolyDP(contour2f, approxCurve, epsilon, true);
                        if (approxCurve.rows() == 3)
                            tri++;
                        if (approxCurve.rows() == 4)
                            rect++;
                        if (approxCurve.rows() > 4)
                            circle++;
                    }
                }
                tex1.setText("轮廓:"+contoursCounts+"\n圆形:"+circle+"\n三角:"+tri+"\n矩形:"+rect);
                System.out.println("轮廓:"+contoursCounts+"\n圆形:"+circle+"\n三角:"+tri+"\n矩形:"+rect);
            }
        });
    }

    private void initloadOpencv(){
        boolean success= OpenCVLoader.initDebug();
        if (success){
            Toast.makeText(this.getApplicationContext(),"loading",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this.getApplicationContext(),"sorry",Toast.LENGTH_LONG).show();
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        srcmat.release();
        dsmat.release();
        hsvmat.release();
    }


}
