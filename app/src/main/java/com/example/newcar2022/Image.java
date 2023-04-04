package com.example.newcar2022;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class Image extends Fragment implements View.OnClickListener {
    private Button qr_codeButton;//二维码
    private Button image_lcrButton; //图形识别
    private Button car_plateButton;//车牌识别
    private Button voice_button;//语音识别
    private Button car_photo_button;//小车拍照
    private Button car_photo_button_basic;//小车拍照
    private Button image_debug_Button;//小车拍照
    private Button rfid_sq;       //RFID扇区
    private Button rfid_read;    //RFID读
    private Button rfid_write;   //RFID写
    private Button image_et_del;//识别结果文本框清零
    private Button sb_result;//显示识别结果按钮
    private Button voice_sbButton;//  语音死别 的按键
    private Button mleft_dt_btn,mright_dt_btn,mtft_qrcode_btn,mimage_result_btn;
    private EditText result;//结果显示 文本框
    private int image_num = 1;//图片序号

    private Vibrator vibrator;//震动
    //popup控件声明
    private ImageView mimage_show_view;
    private Button mSet_image_left_btn, mSet_image_right_btn, mSet_image_center_btn, mSave_image_btn, mImage_num_add_btn;
    private EditText mImage_num_text;
    public static int count = 1;//  图片保存名称累加变量
    private AlertDialog.Builder builder;// 对话框
    private MainActivity mainactivity;//  声明一个mainactivity的对象
    private ControlCommand controlCommand;//
    private Message message = new Message();   //创建Message对象
    private PopupWindow mPopup_image_window;
    private static final String ARG_SECTION_NUMBER = "section_number";





    /**
     * 获取sd卡的路径
     *
     * @return 路径的字符串
     */
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取外存目录
        }
        return sdDir.toString();
    }


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Image newInstance(int sectionNumber) {
        Image fragment = new Image();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.image_layout, container, false);
        Context context = this.getContext();            //一般来说,获取 context 上下文即可,最好别用全局的.
        vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);//获取震动服务
        //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        qr_codeButton = (Button) rootView.findViewById(R.id.qr_code);//二维码
        image_lcrButton = (Button) rootView.findViewById(R.id.image_lcr);//图形识别
        car_plateButton = (Button) rootView.findViewById(R.id.car_plate);//车牌识别
        result = (EditText) rootView.findViewById(R.id.xsresult);//结果显示 文本框 文本输入
        car_photo_button = (Button) rootView.findViewById(R.id.car_photo);//  小车拍照
        car_photo_button_basic = (Button) rootView.findViewById(R.id.car_photo_basic);//  小车拍照
        voice_button = (Button) rootView.findViewById(R.id.voice);//  语音播报
        image_et_del = (Button) rootView.findViewById(R.id.image_del_txt);//结果显示文本框 清零
        sb_result = (Button) rootView.findViewById(R.id.sb_result);//  识别结果button
        rfid_sq = (Button) rootView.findViewById(R.id.rfid_sq);  //RFID扇区
        rfid_read = (Button) rootView.findViewById(R.id.rfid_read);  //RFID读
        rfid_write = (Button) rootView.findViewById(R.id.rfid_write); //RFID写
        voice_sbButton = rootView.findViewById(R.id.voice_sb);//语音识别
        image_debug_Button=rootView.findViewById(R.id.image_debug);//   预留的图形测试



        mleft_dt_btn=rootView.findViewById(R.id.left_dt_debug_btn);
        mright_dt_btn=rootView.findViewById(R.id.right_dt_debug_btn);
        mtft_qrcode_btn=rootView.findViewById(R.id.tft_qrcode_debug_btn);
        mimage_result_btn=rootView.findViewById(R.id.image_result_btn);


        mainactivity = new MainActivity();//  对象
        controlCommand = new ControlCommand();//对象
        qr_codeButton.setOnClickListener(this);//二维码
        image_lcrButton.setOnClickListener(this);//图形识别
        car_plateButton.setOnClickListener(this);//车牌识别
        voice_button.setOnClickListener(this);//语音播报
        sb_result.setOnClickListener(this);//  识别结果按钮
        rfid_sq.setOnClickListener(this);
        rfid_read.setOnClickListener(this);
        rfid_write.setOnClickListener(this);
        car_photo_button.setOnClickListener(this);//小车拍照
        car_photo_button_basic.setOnClickListener(this);
        image_et_del.setOnClickListener(this);// 结果显示 文本框 清零
        voice_sbButton.setOnClickListener(this);//语音识别测试按键
        image_debug_Button.setOnClickListener(this);


        mleft_dt_btn.setOnClickListener(this);
        mright_dt_btn.setOnClickListener(this);
        mtft_qrcode_btn.setOnClickListener(this);
        mimage_result_btn.setOnClickListener(this);

        popupWindowIntionclistener();
        intiPopuoWindow();
        return rootView;
    }

    ///   浮窗控件声明
    void popupWindowIntionclistener() {

        // View view = LayoutInflater.from()   Coinflate(R.layout.popup_activity, null);
        final View image_popupView = getActivity().getLayoutInflater().inflate(R.layout.image_popup_activity, null);
        // PopupWindow实例化
        mPopup_image_window = new PopupWindow(image_popupView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);

//
        mimage_show_view = image_popupView.findViewById(R.id.image_show_view);// 输入超声波距离的文本框
        mSet_image_left_btn = image_popupView.findViewById(R.id.set_image_left_btn);//设置图片属性左
        mSet_image_right_btn = image_popupView.findViewById(R.id.set_image_right_btn);//设置图片属性右

        mSet_image_center_btn = image_popupView.findViewById(R.id.set_iage_center_btn);//设置图片属性 中
        mSave_image_btn = image_popupView.findViewById(R.id.save_image_btn);
        mImage_num_add_btn = image_popupView.findViewById(R.id.image_num_add);
        mImage_num_text = image_popupView.findViewById(R.id.image_num_text);// 图片序号的num

//        mparameter_set_lightbtn.setOnClickListener(parameter_listener);
        mSet_image_left_btn.setOnClickListener(image_popup_window_listener);
        mSet_image_right_btn.setOnClickListener(image_popup_window_listener);
        mSet_image_center_btn.setOnClickListener(image_popup_window_listener);
        mSave_image_btn.setOnClickListener(image_popup_window_listener);
        mImage_num_add_btn.setOnClickListener(image_popup_window_listener);
    }

    // @SuppressLint("WrongConstant")
    void intiPopuoWindow()//  初始化 popupwindow
    {
        // 设置PopupWindow是否可触摸(设置为不可触摸，那弹出框内的任何控件都不能进行任何点击等等类似操作)

        mPopup_image_window.setTouchable(true);
        // 设置非PopupWindow区域是否可触摸
        // 1.若设置PopupWindow获得焦点和非PopupWindow区域可触摸，但实际上非PopupWindow区域的控件并不能响应点击事件等等
        // 2.若设置PopupWindow不可获得焦点，则不管非PopupWindow区域被设置能否触摸，实际上非PopupWindow区域的控件都能响应点击事件等等
        // 3.若设置PopupWindow不可获得焦点，非PopupWindow区域被设置能触摸,当点击非PopupWindow区域时能隐藏PopupWindow，而点击返回键并不能隐藏窗口，
        //  此时通过按钮只能控制窗口的弹出，并不能控制消失，消失只能通过点击其他非PopupWindow区域
        mPopup_image_window.setOutsideTouchable(true);

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框(但目前并没有发现此问题)
        // mPopupWindow.setBackgroundDrawable( new BitmapDrawable( getResources(), (Bitmap) null));
        // 设置PopupWindow显示和隐藏时的动画
        //  mPopupWindow.setAnimationStyle(R.style.anim_popup_window);
        // 设置PopupWindow是否可获得焦点
        // 1.如果设置为可获得焦点，不管非PopupWindow区域被设置能否触摸，也会在点击屏幕非PopupWindow区域和点击返回键时，使PopupWindow隐藏
        // 2.相反，如果设置为不可获得焦点，在点击屏幕非PopupWindow区域或点击返回键时，都不能使PopupWindow隐藏
        mPopup_image_window.setFocusable(true);

//        mPopupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
//
//        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //   mPopupWindow.showAtLocation(getView(), Gravity.BOTTOM,0,0);

    }

    @Override
    //控件监听
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.qr_code:// 识别 二维码
                vibrator.vibrate(100);
                handler.sendEmptyMessage(new Message().what = 0);//  handler 发送
                break;
            case R.id.image_lcr://图形识别
                vibrator.vibrate(100);
                ImageControl(getView());
                break;

            case R.id.car_plate:  //车牌识别 vibrator.vibrate(100);

                CarPlateControl(getView());
                break;
            case R.id.car_photo:// 小车保存截取的矩形屏幕照片
                vibrator.vibrate(100);
                mPopup_image_window.showAtLocation(getView(), Gravity.CENTER, 0, 0);//  控制 popupwindow 显示 的  位置
                mimage_show_view.setImageBitmap(getCutRectangleBitmap());
                break;

            case R.id.car_photo_basic://  小车 拍摄原图
                vibrator.vibrate(100);
                mainactivity.myImageView.setDrawingCacheEnabled(true);//  开启获得    activity imageview 的图片
                saveBitmapFile(MainActivity.bitmap);//保存
                new Fill().savePhoth(File_Name.FIGURE_PHOTH,MainActivity.bitmap);  //保存图片
                mainactivity.myImageView.setDrawingCacheEnabled(false);//关闭
                Snackbar.make(getView(), "拍照成功", Snackbar.LENGTH_SHORT).show();//  测试
                break;

            case R.id.image_del_txt://清空显示结果文本框
                vibrator.vibrate(100);
                result.setText("");
                break;
            case R.id.voice://语音播报
                vibrator.vibrate(100);
                Voice_dialog(getView());
                break;
            case R.id.voice_sb:
                vibrator.vibrate(100);

                if(controlCommand.z_c_flag==0)
                {
                    controlCommand.comDataTest(1, controlCommand.voice_sb, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                    Snackbar.make(getView(), "主车语音识别测试", Snackbar.LENGTH_SHORT).show();//  测试
                }else
                {
                    controlCommand.C_ComData(controlCommand.CC_ORDER,controlCommand.voice_sb,0,0,0,false);
                    Snackbar.make(getView(), "从车语音识别测试", Snackbar.LENGTH_SHORT).show();//  测试
                }

                break;
            case R.id.sb_result:
                vibrator.vibrate(100);
                result_dialog(getView());
                break;

            case R.id.rfid_sq:  //RFID扇区
                vibrator.vibrate(100);
                handler.sendEmptyMessage(new Message().what = 5);//  handler 发送
                break;

            case R.id.rfid_read:  //RFID读卡
                vibrator.vibrate(100);
                handler.sendEmptyMessage(new Message().what = 3);//  handler 发送
                break;

            case R.id.rfid_write: //RFID写卡
                vibrator.vibrate(100);
                handler.sendEmptyMessage(new Message().what = 4);//  handler 发送
                break;

            case R.id.image_debug: //白卡密钥设置
                vibrator.vibrate(100);
                Snackbar.make(getView(), "白卡密钥设置", Snackbar.LENGTH_SHORT).show();//  测试
                try {
                    String str = result.getText().toString().toUpperCase();
                    Log.e( "白卡密钥: ",str );
                    int rfidmy_num1 = Integer.valueOf(str.substring(0,2),16);
                    int rfidmy_num2 = Integer.valueOf(str.substring(2,4),16);
                    int rfidmy_num3 = Integer.valueOf(str.substring(4,6),16);
                    int rfidmy_num4 = Integer.valueOf(str.substring(6,8),16);
                    int rfidmy_num5 = Integer.valueOf(str.substring(8,10),16);
                    int rfidmy_num6 = Integer.valueOf(str.substring(10,12),16);
                    controlCommand.comDataTest(1, controlCommand.trends_data, controlCommand.rfid_my, 1, rfidmy_num1, rfidmy_num2, rfidmy_num3, rfidmy_num4,rfidmy_num5, rfidmy_num6, 0, 0, 0);


                } catch (Exception e) {
                    // TODO: handle exception
                    Log.e( "白卡密钥: ","错误" );
                }
            case R.id.left_dt_debug_btn:
                vibrator.vibrate(100);
                if(controlCommand.z_c_flag==0)
                {
                    controlCommand.comDataTest(1, controlCommand.left, controlCommand.dt, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                    Snackbar.make(getView(), "左转掉头测试", Snackbar.LENGTH_SHORT).show();//  测试

                }
                else
                {

                    Snackbar.make(getView(), "从车左转掉头测试", Snackbar.LENGTH_SHORT).show();//  测试
                    controlCommand.C_ComData(controlCommand.CC_ORDER, controlCommand.left, controlCommand.dt,0, 0, false);

                }

                break;
            case R.id.right_dt_debug_btn:
                vibrator.vibrate(100);
                if(controlCommand.z_c_flag==0)
                {
                    controlCommand.comDataTest(1, controlCommand.right, controlCommand.dt, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

                    Snackbar.make(getView(), "右转掉头测试", Snackbar.LENGTH_SHORT).show();//  测试
                }
                else
                {

                    controlCommand.C_ComData(controlCommand.CC_ORDER, controlCommand.right, controlCommand.dt,0, 0, false);

                    Snackbar.make(getView(), "从车右转掉头测试", Snackbar.LENGTH_SHORT).show();//  测试
                }

                break;

            case R.id.tft_qrcode_debug_btn:
                vibrator.vibrate(100);
                controlCommand.comDataTest(1, controlCommand.cc_ewm, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                Snackbar.make(getView(), "tft二维码识别", Snackbar.LENGTH_SHORT).show();//  测试

                break;
            case R.id.image_result_btn:
                vibrator.vibrate(100);


                break;
        }
    }


    /*
     */
    int image_save_flag = 0;

    Button.OnClickListener image_popup_window_listener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.set_image_left_btn:

                    mimage_show_view.setImageBitmap(getCutRectangleBitmap());
                    image_save_flag = 1;
                    Snackbar.make(getView(), "左", Snackbar.LENGTH_SHORT).show();
                    vibrator.vibrate(100);
                    break;
                case R.id.set_image_right_btn:
                    mimage_show_view.setImageBitmap(getCutRectangleBitmap());
                    image_save_flag = 2;
                    Snackbar.make(getView(), "右", Snackbar.LENGTH_SHORT).show();
                    vibrator.vibrate(100);
                    break;
                case R.id.set_iage_center_btn:
                    mimage_show_view.setImageBitmap(getCutRectangleBitmap());
                    image_save_flag = 3;
                    Snackbar.make(getView(), "中", Snackbar.LENGTH_SHORT).show();
                    vibrator.vibrate(100);
                    break;
                case R.id.image_num_add:
                    int n = Integer.parseInt(mImage_num_text.getText().toString());
                    n++;
                    mImage_num_text.setText(n + "");
                    vibrator.vibrate(100);
                    break;
                case R.id.save_image_btn:
                    switch (image_save_flag) {
                        case 1:

                            mimage_show_view.setDrawingCacheEnabled(true);
                            saveBitmapFile(Bitmap.createBitmap(mimage_show_view.getDrawingCache()), Integer.parseInt(mImage_num_text.getText().toString()), "left");
                            mimage_show_view.setDrawingCacheEnabled(false);
                            break;
                        case 2:
                            mimage_show_view.setDrawingCacheEnabled(true);
                            saveBitmapFile(Bitmap.createBitmap(mimage_show_view.getDrawingCache()), Integer.parseInt(mImage_num_text.getText().toString()), "right");
                            mimage_show_view.setDrawingCacheEnabled(false);
                            break;
                        case 3:
                            mimage_show_view.setDrawingCacheEnabled(true);
                            saveBitmapFile(Bitmap.createBitmap(mimage_show_view.getDrawingCache()), Integer.parseInt(mImage_num_text.getText().toString()), "center");
                            mimage_show_view.setDrawingCacheEnabled(false);
                            break;
                    }
                    vibrator.vibrate(100);
                    Snackbar.make(getView(), "保存成功", Snackbar.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void result_dialog(View view) {
        builder = new AlertDialog.Builder(getContext());//使用  getContext
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("识别结果");

        /**
         * 设置内容区域为单选列表项
         */
        final String[] items = {"二维码", "LCD图形", "TFT图形", "LCD车牌", "TFT车牌"};
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Toast.makeText(getContext(), "You clicked "+items[i], Toast.LENGTH_SHORT).show();
                switch (i) {
                    case 0://二维码
                        vibrator.vibrate(100);
                        dialogInterface.cancel();//点击后关闭对话框
                        break;
                    case 1:
                        vibrator.vibrate(100);
                        dialogInterface.cancel();//点击后关闭对话框
                        break;
                    case 2:
                        vibrator.vibrate(100);
                        dialogInterface.cancel();//点击后关闭对话框
                        break;
                    case 3:
                        vibrator.vibrate(100);
                        dialogInterface.cancel();//点击后关闭对话框
                        break;
                    case 4:
                        vibrator.vibrate(100);
                        dialogInterface.cancel();//点击后关闭对话框
                        break;
                }
            }
        });
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void Voice_dialog(View view) {
        builder = new AlertDialog.Builder(getContext());//使用  getContext
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("语音播报模式");

        /**
         * 设置内容区域为单选列表项
         */
        final String[] items = {"二维码", "LCD图形", "TFT图形", "LCD车牌", "TFT车牌", "小车播报文本框内容", "标志物播报文本框内容"};
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Toast.makeText(getContext(), "You clicked "+items[i], Toast.LENGTH_SHORT).show();
                switch (i) {
                    case 0://   二维码

                        dialogInterface.cancel();//点击后关闭对话框
                        vibrator.vibrate(100);
                        break;
                    case 1://  lcd图形

                        dialogInterface.cancel();//点击后关闭对话框
                        vibrator.vibrate(100);
                        break;
                    case 2://  tft图形

                        dialogInterface.cancel();//点击后关闭对话框
                        vibrator.vibrate(100);
                        break;
                    case 3://  lcd 车牌

                        dialogInterface.cancel();//点击后关闭对话框
                        vibrator.vibrate(100);
                        break;
                    case 4://  tft车牌

                        dialogInterface.cancel();//点击后关闭对话框
                        vibrator.vibrate(100);
                        break;

                    case 5:// 小车播报文本框内容

                        controlCommand.Voice(result.getText().toString(), 3, false);
                        dialogInterface.cancel();//点击后关闭对话框
                        vibrator.vibrate(100);
                        break;
                    case 6:// 标志物播报文本框内容
                        controlCommand.Voice(result.getText().toString(), 2, false);
                        dialogInterface.cancel();//点击后关闭对话框
                        vibrator.vibrate(100);
                        break;
                }
            }
        });
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void ImageControl(View view) {

        builder = new AlertDialog.Builder(getContext());//使用  getContext
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("图形识别类型选择");

        /**
         * 设置内容区域为单选列表项
         */
        final String[] items = {"识别TFT图形", "识别LCD图形"};
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Toast.makeText(getContext(), "You clicked "+items[i], Toast.LENGTH_SHORT).show();
                switch (i) {
                    case 0:
                        vibrator.vibrate(100);
                        Toast.makeText(getContext(), "识别TFT图形", Toast.LENGTH_SHORT).show();
                        dialogInterface.cancel();
                        Log.e("图形识别", "开始识别");
//                            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.left_button);
//                            Mat mat = new Mat();
//                            Utils.bitmapToMat(bitmap,mat);
                        handler.sendEmptyMessage(new Message().what = 2);
                        // My_OpenCV(mat,mat);
                        Log.e("图形识别", "识别结果已生成");
                        break;
                    case 1:
                        Toast.makeText(getContext(), "识别LCD图形", Toast.LENGTH_SHORT).show();
                        dialogInterface.cancel();
                        handler.sendEmptyMessage(new Message().what = 2);
                        break;
                }
            }
        });
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void CarPlateControl(View view) {
        builder = new AlertDialog.Builder(getContext());//使用  getContext
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("车牌识别类型选择");

        /**
         * 设置内容区域为单选列表项
         */
        final String[] items = {"识别TFT车牌", "识别LCD车牌"};
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Toast.makeText(getContext(), "You clicked "+items[i], Toast.LENGTH_SHORT).show();
                switch (i) {
                    case 0:
                        vibrator.vibrate(100);
                        Toast.makeText(getContext(), "识别TFT车牌", Toast.LENGTH_SHORT).show();
                        dialogInterface.cancel();

                        handler.sendEmptyMessage(new Message().what = 1);
                        break;
                    case 1:
                        vibrator.vibrate(100);
                        Toast.makeText(getContext(), "识别LCD车牌", Toast.LENGTH_SHORT).show();
                        dialogInterface.cancel();
                        handler.sendEmptyMessage(new Message().what = 1);
                        break;
                }
            }
        });
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private Handler handler = new Handler(new Handler.Callback() {
        ControlCommand controlCommand = new ControlCommand();
        String str = "";

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:    //二维码识别\
                    vibrator.vibrate(100);
                    result.setText("正在识别中~~");

                    Thread threads=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            QR_Code qr_code=new QR_Code();
                            str = qr_code.QR_ccode(1);
                        }
                    });
                    threads.start();



                    result.setText(str);
                    break;
                case 1:    //车牌识别
                    vibrator.vibrate(100);
                    //                   Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.imppp);
//                    /** 语种为英语 */  ocr
//                    String LANGUAGE = "eng";
//                    String str = doOcr(bitmap,LANGUAGE);
                    result.setText("正在识别中~~");
                    Thread thread=new Thread(new Runnable() {


                        @Override
                        public void run() {
                            ControlCommand controlCommand122=new ControlCommand();
                            controlCommand122.Discern(19,0);
                        }
                    });
                    thread.start();

               //     str = new LPR_Handle().SimpleRecogss(MainActivity.bitmap);
                    result.setText(str);


                    break;
                case 2:   //图形识别
                    vibrator.vibrate(100);
               //     result.setText("正在识别中~~");

                    for (int i=0;i<=100;i++) {
                        controlCommand.comDataTest(1,controlCommand.qh_AB,controlCommand.bzw_A,controlCommand.tft_fy,controlCommand.d_f+1,0,0,0,0,0,0,0,0);
                        controlCommand.yanchi(6000);
              //          Bitmap bitmapsss = getCutRectangleBitmap();
                        Fill.savePhoths(i+File_Name.XC, MainActivity.bitmap);  //保存图片
                    }



                    result.setText(str);
                    break;
                case 3:  //RFID读卡 //交通标志
                    vibrator.vibrate(100);
                    //     controlCommand.comDataTest(1, 0, 0, 0, 0, controlCommand.rfid_read, 1, 0, 0, 0, 0, 0, 0);

//                    str=new Figure().colorins();
//                    //     str=new Figure().Filsdsds();
                    for (int i=0;i<=100;i++) {
                        controlCommand.comDataTest(1,controlCommand.qh_AB,controlCommand.bzw_A,controlCommand.tft_fy,controlCommand.d_f+1,0,0,0,0,0,0,0,0);
                        controlCommand.yanchi(6000);
                        pingmujiequ pingmujiequ=new pingmujiequ();
                        Mat dsmat=pingmujiequ.init_carpingmujiequ(MainActivity.bitmap);

                        Bitmap resultbitmap = Bitmap.createBitmap(dsmat.width(), dsmat.height(),Bitmap.Config.ARGB_8888);

                        Utils.matToBitmap(dsmat, resultbitmap);
                        Fill.savePhoths(i+File_Name.aC,  resultbitmap);  //保存图片
                    }

                    result.setText(str);
                    break;

                case 4: //RFID写卡
                    vibrator.vibrate(100);
//                    str = result.getText() + "";
//                    str.trim();
//                    char[] str_char = str.toCharArray();
//                    try {
//                        controlCommand.comData(1, controlCommand.rfid_write, 0x01, (int) str_char[0], (int) str_char[1], (int) str_char[2], (int) str_char[3], (int) str_char[4], (int) str_char[5], (int) str_char[6], (int) str_char[7], (int) str_char[8], (int) str_char[9], true);
//                        controlCommand.comData(1, controlCommand.rfid_write, 0x02, (int) str_char[10], (int) str_char[11], (int) str_char[12], (int) str_char[13], (int) str_char[14], (int) str_char[15], 0, 0, 0, 0, true);
//                    } catch (Exception e) {
//                        result.setText("");
//                        result.setText("数据填写错误");
//                    }
                    for (int i=0;i<=100;i++) {
                        controlCommand.comDataTest(1,controlCommand.qh_AB,controlCommand.bzw_A,controlCommand.tft_fy,controlCommand.d_f+1,0,0,0,0,0,0,0,0);
                        controlCommand.yanchi(6000);

                        Fill.savePhoths(i+File_Name.bC,  MainActivity.bitmap);  //保存图片
                    }
                    result.setText(str);
                    break;
                case 5:  //RFID扇区
                    vibrator.vibrate(100);
                    try {
                        str = result.getText() + "";
                        String[] st_buf = str.split("\\D");
                        int[] st_int = new int[st_buf.length];
                        for (int index = 0; index < st_buf.length; index++) {
                            st_int[index] = Integer.valueOf(st_buf[index]);
                        }
                        Log.e("RFID读卡", st_int[0] + "");
                        controlCommand.comDataTest(1, controlCommand.rfid_sq, 1,st_int[0], st_int[1], st_int[2],  0, 0, 0, 0, 0, 0, 0);
                    } catch (Exception e) {
                        result.setText("");
                        result.setText("数据填写错误");
                    }
                    break;

            }
            return false;
        }
    });
    //  private  native void My_OpenCV(Mat mat, Mat mat1);


    public Bitmap saveImage() {
/**********************保存TFT屏幕*****************************/
        Bitmap yuantu = MainActivity.bitmap;
        Mat maxRect = new Mat();
        Utils.bitmapToMat(yuantu, maxRect);
        maxRect = new Figure().cutRectangle1(maxRect,true);  //截取显示屏
        Bitmap newBitMap = Bitmap.createBitmap(maxRect.width(), maxRect.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(maxRect, newBitMap);
        saveBitmapFile(newBitMap);//保存


//        saveBitmapFile(MainActivity.bitmap);//保存
        count++;
        return newBitMap;
    }

    Mat mat = new Mat();

    public Bitmap getCutRectangleBitmap() {
/**********************保存TFT屏幕*****************************/
        Bitmap yuantu = MainActivity.bitmap;
        Mat maxRect = new Mat();
        Utils.bitmapToMat(yuantu, maxRect);
        maxRect = new Figure().cutRectangle1(maxRect,true);  //截取显示屏
        Bitmap newBitMap = Bitmap.createBitmap(maxRect.width(), maxRect.height(), Bitmap.Config.ARGB_8888);
        //mat.convertTo(maxRect,CV_8UC1);
        Utils.matToBitmap(maxRect, newBitMap);
        return newBitMap;
    }

    public static synchronized String createtFileName() {
        java.util.Date dt = new java.util.Date(System.currentTimeMillis());
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String fileName = fmt.format(dt);
        fileName = fileName;//extension, you can change it.
        return fileName;
    }


    public static void saveBitmapFile(Bitmap bitmap) {
        File temp = new File(getSDPath() + "/CarImage/");//要保存文件先创建文件夹
        if (!temp.exists()) {
            temp.mkdir();
        }
        ////重复保存
        File file = new File(getSDPath() + "/CarImage/" + "/" + createtFileName() + ".jpg");//将要保存图片的路径和图片名称// 生成文件名 加时间加   1随机字符串
        //    File file =  new File("/sdcard/1delete/1.png");/////延时较长
        try {
            BufferedOutputStream mbos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, mbos);
            mbos.flush();
            mbos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveBitmapFile(Bitmap bitmap, int num, String str) {
        File temp = new File(getSDPath() + "/CarImage/");//要保存文件先创建文件夹
        if (!temp.exists()) {
            temp.mkdir();
        }
        ////重复保存
        File file = new File(getSDPath() + "/CarImage/" + "/" + num + str + ".jpg");//将要保存图片的路径和图片名称// 生成文件名 加时间加   1随机字符串
        //    File file =  new File("/sdcard/1delete/1.png");/////延时较长
        try {
            BufferedOutputStream mbos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, mbos);
            mbos.flush();
            mbos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
