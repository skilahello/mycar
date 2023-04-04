package com.example.newcar2022;

import static android.widget.Toast.makeText;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Looper;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

public class BasicControl extends Fragment implements View.OnClickListener {
    private static final String ARG_SECTION_NUMBER = "section_number";
    public static int mmpvalue = 980, mmpvaluego, mmpvalueback, mmpvalueleft, mmpvalueright, mmpvaluebzleft, mmpvaluedtleft, mmpvaluebzright, mmpvaluedtright;//  静态的 全局变量 码盘值
    private ImageButton up_button, down_button, left_button, right_button, stop_button;//行进控制按钮
    private ToggleButton z_c_control, buzz_switch, control_flag;//开关按钮//  基本控制切换
    private Button auto_button, Light_button, camera_button, z_w_value, software_res;//全自动，转向灯，摄像头预设位，码盘前进
    private ControlCommand controlCommand;//声明controlcommand类的的对象
    private AlertDialog.Builder builder;  //声明对话框对象
    private EditText et_mp;//自动行走路线编辑框，码盘，速度
    private Vibrator vibrator;//震动
    private MainActivity mainactivity;//   声明Mainactivity的对象
    private PopupWindow mPopupWindow, mParameterPopupWindow;
    private SeekBar mmp;
    private Switch mparameter_z_c_switch;
    //码盘调整浮窗控件省声明
    private EditText text;//   popupwindow  显示 码盘 的文本框

    private Button mmpsavebtn, mmpsendbtn, mmpupbtn, mmpdownbtn, mmpleftbtn, mmpbzleftbtn, mmpdtleftbtn, mmprightbtn, mmpbzrightbtn, mmpdtrightbtn, mmpvalupbtn, mmpvaldownbtn;// 更新码盘 的按键


    private TextView clemptext;
    public static byte[] parameter_array = new byte[12];

    /**
     * Returns a new instance of this fragment for the given section
     * 返回   fragment
     * number.w
     */
    public static BasicControl newInstance(int sectionNumber) {
        BasicControl fragment = new BasicControl();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.basic_control, container, false);
        mainactivity = new MainActivity();//实例化对象   mianactivity
        controlCommand = new ControlCommand();//实例化    controlcommand对象
        MainActivity activity = (MainActivity) this.getActivity();    //这里获取的 activity 要强转成你的 Fragment 宿主的Activity,
        Context context = this.getContext();            //一般来说,获取 context 上下文即可,最好别用全局的.
        vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);//获取震动服务

        //   et_z_w_val = (EditText) rootView.findViewById(R.id.z_w_val);
        software_res = (Button) rootView.findViewById(R.id.software_res);
        et_mp = (EditText) rootView.findViewById(R.id.et_mp);//码盘
        //et_speed = (EditText) rootView.findViewById(R.id.et_speed);//速度
        stop_button = (ImageButton) rootView.findViewById(R.id.stop);//停止
        up_button = (ImageButton) rootView.findViewById(R.id.up);//前进
        down_button = (ImageButton) rootView.findViewById(R.id.down);//后退
        left_button = (ImageButton) rootView.findViewById(R.id.left);//左转
        right_button = (ImageButton) rootView.findViewById(R.id.right);//右转
        z_c_control = (ToggleButton) rootView.findViewById(R.id.z_c_control);//主从控制切换
        buzz_switch = (ToggleButton) rootView.findViewById(R.id.buzz);//蜂鸣器控制
        control_flag = (ToggleButton) rootView.findViewById(R.id.control_flag_button);
        auto_button = (Button) rootView.findViewById(R.id.auto);//全自动开始按钮
        z_w_value = (Button) rootView.findViewById(R.id.set_z_w_val);
        Light_button = (Button) rootView.findViewById(R.id.light);//转向灯
        camera_button = (Button) rootView.findViewById(R.id.camera);//摄像头预设位


        stop_button.setOnClickListener(this);//绑定见监听器
        up_button.setOnClickListener(this);
        down_button.setOnClickListener(this);
        left_button.setOnClickListener(this);
        right_button.setOnClickListener(this);
        auto_button.setOnClickListener(this);
        Light_button.setOnClickListener(this);
        camera_button.setOnClickListener(this);
        z_w_value.setOnClickListener(this);
        software_res.setOnClickListener(this);

        Toggle_button();//开关按钮初始化
        popupWindowIntionclistener();
        intiPopuoWindow();

        return rootView;
    }

    ///   浮窗控件声明
    void popupWindowIntionclistener() {

        // View view = LayoutInflater.from()   Coinflate(R.layout.popup_activity, null);
        final View popupView = getActivity().getLayoutInflater().inflate(R.layout.popup_activity, null);
        // PopupWindow实例化
        mPopupWindow = new PopupWindow(popupView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);


        mmpsendbtn = popupView.findViewById(R.id.mpsend);//  码盘更新到小车底层flash存储 暂时未使用
        mmp = popupView.findViewById(R.id.mp);//  码盘   拖动条
        text = popupView.findViewById(R.id.tv_popup_text);//  码盘 显示 文本
        mmpsavebtn = popupView.findViewById(R.id.mpsave);//  码盘保存为  文本
        mmpupbtn = popupView.findViewById(R.id.mpupbtn);//  前进码盘测试
        mmpdownbtn = popupView.findViewById(R.id.mpdownbtn);//  后退码盘测试
        mmpleftbtn = popupView.findViewById(R.id.mpleftbtn);//左转码盘 测试
        mmpbzleftbtn = popupView.findViewById(R.id.mpbzleftbtn);//  左半转
        mmpdtleftbtn = popupView.findViewById(R.id.mpleftdtbtn);//左掉头
        mmprightbtn = popupView.findViewById(R.id.mprightbtn);//  右转 码盘 测试
        mmpbzrightbtn = popupView.findViewById(R.id.mpbzrightbtn);//  右半转
        mmpdtrightbtn = popupView.findViewById(R.id.mprightdtbtn);//右掉头
        mmpvalupbtn = popupView.findViewById(R.id.mpvalup);//  按键 微调码盘 值 +++
        mmpvaldownbtn = popupView.findViewById(R.id.mpvaldown);///按键 微调码盘 ---
        clemptext = popupView.findViewById(R.id.mpcle);

        mmpsendbtn.setOnClickListener(popbtn);//  绑定popupwindow 的按键监听测试码盘 发送到 小车底层 暂未使用
        mmpsavebtn.setOnClickListener(popbtn);// 码盘 保存为  文本
        mmpupbtn.setOnClickListener(popbtn);// 前进码盘测试
        mmpdownbtn.setOnClickListener(popbtn);//后退码盘 测试
        mmpleftbtn.setOnClickListener(popbtn);//   左转码盘 测试
        mmpbzleftbtn.setOnClickListener(popbtn);//  左半
        mmpdtleftbtn.setOnClickListener(popbtn);//   左掉头
        mmpbzrightbtn.setOnClickListener(popbtn);//右半转
        mmpdtrightbtn.setOnClickListener(popbtn);// 右转掉头
        mmprightbtn.setOnClickListener(popbtn);//   右转码盘测试
        mmpvaldownbtn.setOnClickListener(popbtn);//--
        mmpvalupbtn.setOnClickListener(popbtn);//  按键微调码盘 的按键 ++
        clemptext.setOnClickListener(popbtn);
        //   SeekBar  监听

        mmp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                text.setText(i + "");
                // mmp.setProgress(s);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //   text.setText(  mmp.getMax());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //   text.setText(  mmp.getMax());

            }
        });






    }

    // @SuppressLint("WrongConstant")
    void intiPopuoWindow()//  初始化 popupwindow
    {
        // 设置PopupWindow是否可触摸(设置为不可触摸，那弹出框内的任何控件都不能进行任何点击等等类似操作)
        mPopupWindow.setTouchable(true);

        // 设置非PopupWindow区域是否可触摸
        // 1.若设置PopupWindow获得焦点和非PopupWindow区域可触摸，但实际上非PopupWindow区域的控件并不能响应点击事件等等
        // 2.若设置PopupWindow不可获得焦点，则不管非PopupWindow区域被设置能否触摸，实际上非PopupWindow区域的控件都能响应点击事件等等
        // 3.若设置PopupWindow不可获得焦点，非PopupWindow区域被设置能触摸,当点击非PopupWindow区域时能隐藏PopupWindow，而点击返回键并不能隐藏窗口，
        //  此时通过按钮只能控制窗口的弹出，并不能控制消失，消失只能通过点击其他非PopupWindow区域
        mPopupWindow.setOutsideTouchable(true);

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框(但目前并没有发现此问题)
        // mPopupWindow.setBackgroundDrawable( new BitmapDrawable( getResources(), (Bitmap) null));
        // 设置PopupWindow显示和隐藏时的动画
        //  mPopupWindow.setAnimationStyle(R.style.anim_popup_window);
        // 设置PopupWindow是否可获得焦点
        // 1.如果设置为可获得焦点，不管非PopupWindow区域被设置能否触摸，也会在点击屏幕非PopupWindow区域和点击返回键时，使PopupWindow隐藏
        // 2.相反，如果设置为不可获得焦点，在点击屏幕非PopupWindow区域或点击返回键时，都不能使PopupWindow隐藏
        mPopupWindow.setFocusable(true);

//        mPopupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
//
//        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //   mPopupWindow.showAtLocation(getView(), Gravity.BOTTOM,0,0);

    }

    public void Toggle_button()//开关监听初始化
    {
        //主从车切换
        z_c_control.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Z_C_control(b);
            }
        });
        //蜂鸣器  开关
        buzz_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                BuzzControl(b);
            }
        });
        control_flag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Control_flag(b);
            }
        });
    }

    private void Control_flag(boolean b) {

        ToggleButton button2 = (ToggleButton) getView().findViewById(R.id.control_flag_button);//   绑定id
        button2.setChecked(b);
        //判断   state 的两种状态
        if (b) {
            controlCommand.control_way_flag = 0;//0为   按照 固定值转弯  寻迹前进

            Snackbar.make(getView(), "普通模式", Snackbar.LENGTH_SHORT).show();

        } else {

            controlCommand.control_way_flag = 1;//1为  传入 参数 转弯 和寻迹

            Snackbar.make(getView(), "参数模式", Snackbar.LENGTH_SHORT).show();
        }
    }

    /*
            state  为  开关状态
            主从控制执行方法
     */
    public void Z_C_control(boolean state) {
        ToggleButton button2 = (ToggleButton) getView().findViewById(R.id.z_c_control);
        button2.setChecked(state);
        //判断   state 的两种状态
        if (state) {

            controlCommand.z_c_flag = 0; //0为  主车控制

        } else {

            controlCommand.z_c_flag = 1; //1   为   从车   控制
        }

    }

    /*
        蜂鸣器  state  为 开关状态
        开关状态选择    bool  型变量
    */
    public void BuzzControl(boolean state) {

        ToggleButton button3 = (ToggleButton) getView().findViewById(R.id.buzz);
        button3.setChecked(state);

        if (state) {
            if (controlCommand.z_c_flag == 0) {
                controlCommand.comDataTest(1, controlCommand.buzz, controlCommand.on, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                Snackbar.make(getView(), "蜂鸣器打开", Snackbar.LENGTH_SHORT).show();

            } else if (controlCommand.z_c_flag == 1)

            {
                Snackbar.make(getView(), "从车蜂鸣器打开", Snackbar.LENGTH_SHORT).show();
                controlCommand.C_ComData(controlCommand.CC_ORDER, controlCommand.buzz, controlCommand.on, 0, 0, false);
            }

        } else {
            if (controlCommand.z_c_flag == 0) {
                controlCommand.comDataTest(1, controlCommand.buzz, controlCommand.off, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                Snackbar.make(getView(), "蜂鸣器关闭", Snackbar.LENGTH_SHORT).show();
            } else if (controlCommand.z_c_flag == 1) {

                Snackbar.make(getView(), "从车蜂鸣器关闭", Snackbar.LENGTH_SHORT).show();
                controlCommand.C_ComData(controlCommand.CC_ORDER, controlCommand.buzz, controlCommand.off, 0, 0, false);
            }
        }
    }

    /*
     *按键监听
     */
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.up://前进
                vibrator.vibrate(100);
                if (controlCommand.z_c_flag == 0) {
                    if (controlCommand.control_way_flag == 0) {
                        Snackbar.make(view, "循迹前进", Snackbar.LENGTH_SHORT).show();
                        vibrator.vibrate(100);
                        controlCommand.comDataTest(1, controlCommand.track, controlCommand.x, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                    } else {
                        int speed1 = GetEncoder();

                        Snackbar.make(getView(), "前进" + speed1, Snackbar.LENGTH_SHORT).show();
                        controlCommand.comDataTest(1, controlCommand.go, speed1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                    }
                } else if (controlCommand.z_c_flag == 1)//从车控制
                {
                    if (controlCommand.control_way_flag == 0) {

                        vibrator.vibrate(100);
                        Snackbar.make(view, "从车前进", Snackbar.LENGTH_SHORT).show();
                        controlCommand.C_ComData(controlCommand.CC_ORDER, controlCommand.track, controlCommand.x, 0, 0, false);
                    } else {
                        int speed1 = GetEncoder();

                        Snackbar.make(getView(), "从车前进" + speed1, Snackbar.LENGTH_SHORT).show();
                        controlCommand.C_ComData(controlCommand.CC_ORDER, controlCommand.go, speed1, 0, 0, false);
                    }
                }
                break;
            case R.id.down://后退
                int speed = GetEncoder();
                if (controlCommand.z_c_flag == 0) {
                    vibrator.vibrate(100);
                    controlCommand.comDataTest(1, controlCommand.back, speed, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                    Snackbar.make(view, "后退" + speed, Snackbar.LENGTH_SHORT).show();
                } else if (controlCommand.z_c_flag == 1)//从车控制
                {
                    int speed1 = GetEncoder();
                    vibrator.vibrate(100);
                    Snackbar.make(view, "从车后退", Snackbar.LENGTH_SHORT).show();
                    controlCommand.C_ComData(controlCommand.CC_ORDER, controlCommand.back, speed1, 0, 0, false);
                }
                break;
            case R.id.left://左转
                vibrator.vibrate(100);
                if (controlCommand.z_c_flag == 0) {
                    if (controlCommand.control_way_flag == 0) {
                        controlCommand.comDataTest(1, controlCommand.left, controlCommand.p_z, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                        Snackbar.make(getView(), "左转", Snackbar.LENGTH_SHORT).show();

                    } else {
                        if (GetEncoder() == 0) {
                            Snackbar.make(getView(), "请输入码盘值" + GetEncoder(), Snackbar.LENGTH_SHORT).show();
                        } else {
                            controlCommand.comDataTest(1, controlCommand.left, GetEncoder(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                            Snackbar.make(getView(), "左转码盘" + GetEncoder(), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                } else if (controlCommand.z_c_flag == 1) {
                    if (controlCommand.control_way_flag == 0) {

                        Snackbar.make(view, "从车左转", Snackbar.LENGTH_SHORT).show();
                        controlCommand.C_ComData(controlCommand.CC_ORDER, controlCommand.left, controlCommand.p_z, 0, 0, false);

                    } else {

                        if (GetEncoder() == 0) {
                            Snackbar.make(getView(), "请输入码盘值" + GetEncoder(), Snackbar.LENGTH_SHORT).show();
                        } else {
                            Snackbar.make(getView(), "右转码盘" + GetEncoder(), Snackbar.LENGTH_SHORT).show();
                            controlCommand.C_ComData(controlCommand.CC_ORDER, controlCommand.left, GetEncoder(), 0, 0, false);
                        }
                    }
                }
                break;
            case R.id.right://右转
                vibrator.vibrate(100);
                if (controlCommand.z_c_flag == 0) {
                    if (controlCommand.control_way_flag == 0) {
                        controlCommand.comDataTest(1, controlCommand.right, controlCommand.p_z, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                        Snackbar.make(getView(), "右转", Snackbar.LENGTH_SHORT).show();
                    } else {
                        if (GetEncoder() == 0) {
                            Snackbar.make(getView(), "请输入码盘值" + GetEncoder(), Snackbar.LENGTH_SHORT).show();
                        } else {
                            controlCommand.comDataTest(1, controlCommand.right, GetEncoder(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                            Snackbar.make(getView(), "右转码盘" + GetEncoder(), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                } else if (controlCommand.z_c_flag == 1) {
                    if (controlCommand.control_way_flag == 0) {

                        vibrator.vibrate(100);
                        Snackbar.make(view, "从车右转", Snackbar.LENGTH_SHORT).show();
                        controlCommand.C_ComData(controlCommand.CC_ORDER, controlCommand.right, controlCommand.p_z, 0, 0, false);
                    } else {
                        if (GetEncoder() == 0) {
                            Snackbar.make(getView(), "请输入码盘值" + GetEncoder(), Snackbar.LENGTH_SHORT).show();
                        } else {

                            Snackbar.make(getView(), "右转码盘" + GetEncoder(), Snackbar.LENGTH_SHORT).show();
                            controlCommand.C_ComData(controlCommand.CC_ORDER, controlCommand.right, GetEncoder(), 0, 0, false);
                        }
                    }
                }
                break;
            case R.id.stop: //停止
                vibrator.vibrate(100);
                ControlCommand.mark = -50;
                Snackbar.make(view, "小车已停止", Snackbar.LENGTH_LONG).show();
                break;
            case R.id.auto: //全自动执行任务
                vibrator.vibrate(100);
                AutoDialog(view);
                break;
            case R.id.software_res:
                vibrator.vibrate(100);
                controlCommand.comDataTest(1, controlCommand.software_res, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                break;
            case R.id.light:  //转向灯
                LightDialog(view);   //显示对话框 view
                break;
            case R.id.camera:  //摄像头调整
                CameraControl(view);
                break;
            case R.id.set_z_w_val: //打开 码盘 调试窗口

                // mPopupWindow.showAsDropDown(view);//默认在view（tv_show_popup_window）的下方出现
                mPopupWindow.showAtLocation(getView(), Gravity.BOTTOM, 0, 0);//  控制 popupwindow 显示 的  位置
                break;
            case R.id.parameter_button: //打开


                break;
        }
    }

    //  增加 从车 的 码盘 测试  从车 arduino  可以很方便的 把数据 保存到 内部eeprom    从车 码盘 保存到 eeprom
    //  主车 的码盘 也 保存到 stm32  的 内部 的  flash
    Button.OnClickListener popbtn = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.mpsend://  发送码盘按键// reset
                    vibrator.vibrate(100);
                    Snackbar.make(getView(), "码盘发送到STM32FLASH保存", Snackbar.LENGTH_SHORT).show();
                    break;
                case R.id.mpupbtn://前进码盘测试
                    try {
                        mmpvalue = Integer.parseInt(text.getText().toString());// 将  码盘 值 赋给全局变量
                    } catch (Exception e) {
                        // TODO: handle exception
                        vibrator.vibrate(500);
                        Snackbar.make(getView(), "还没有输入码盘呢", Snackbar.LENGTH_SHORT).show();
                        break;
                    }
                    if(controlCommand.z_c_flag == 0)//主车
                    {
                        controlCommand.comDataTest(1, controlCommand.pt_go, mmpvalue / 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                        Snackbar.make(getView(), "前进码盘测试" + mmpvalue, Snackbar.LENGTH_SHORT).show();
                    }
                    else
                    {
                        controlCommand.C_ComData(controlCommand.CC_ORDER, controlCommand.pt_go, mmpvalue / 10, 0, 0, false);
                        Snackbar.make(getView(), "从车前进码盘测试" + mmpvalue, Snackbar.LENGTH_SHORT).show();
                    }

                    mmpvaluego = mmpvalue;
                    vibrator.vibrate(100);
                    break;
                case R.id.mpdownbtn://  后退码盘测试
                    try {
                        mmpvalue = Integer.parseInt(text.getText().toString());// 获取将  码盘 值 赋给全局变量
                    } catch (Exception e) {
                        // TODO: handle exception
                        vibrator.vibrate(500);
                        Snackbar.make(getView(), "还没有输入码盘呢", Snackbar.LENGTH_SHORT).show();
                        break;
                    }
                    if(controlCommand.z_c_flag == 0)//主车
                    {
                        controlCommand.comDataTest(1, controlCommand.pt_back, mmpvalue / 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                        Snackbar.make(getView(), "后退码盘测试" + mmpvalue, Snackbar.LENGTH_SHORT).show();
                    }
                    else
                    {
                        controlCommand.C_ComData(controlCommand.CC_ORDER, controlCommand.pt_back, mmpvalue / 10, 0, 0, false);
                        Snackbar.make(getView(), "从车后退码盘测试" + mmpvalue, Snackbar.LENGTH_SHORT).show();
                    }

                    mmpvalueback = mmpvalue;
                    vibrator.vibrate(100);
                    break;
                case R.id.mpleftbtn://左转码盘测试 left
                    try {
                        mmpvalue = Integer.parseInt(text.getText().toString());// 将  码盘 值 赋给全局变量
                    } catch (Exception e) {
                        // TODO: handle exception
                        vibrator.vibrate(500);
                        Snackbar.make(getView(), "还没有输入码盘呢", Snackbar.LENGTH_SHORT).show();
                        break;
                    }
                    if(controlCommand.z_c_flag == 0)//主车
                    {
                        controlCommand.comDataTest(1, controlCommand.left, mmpvalue / 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                        Snackbar.make(getView(), "左转码盘测试" + mmpvalue, Snackbar.LENGTH_SHORT).show();
                    }
                    else
                    {
                        controlCommand.C_ComData(controlCommand.CC_ORDER, controlCommand.left, mmpvalue / 10, 0, 0, false);
                        Snackbar.make(getView(), "从车左转码盘测试" + mmpvalue, Snackbar.LENGTH_SHORT).show();
                    }

                    mmpvalueleft = mmpvalue;
                    vibrator.vibrate(100);
                    break;
                case R.id.mpbzleftbtn://左半转码盘测试 left
                    try {
                        mmpvalue = Integer.parseInt(text.getText().toString());// 将  码盘 值 赋给全局变量
                    } catch (Exception e) {
                        // TODO: handle exception
                        vibrator.vibrate(500);
                        Snackbar.make(getView(), "还没有输入码盘呢", Snackbar.LENGTH_SHORT).show();
                        break;
                    }
                    if(controlCommand.z_c_flag == 0)//主车
                    {
                        controlCommand.comDataTest(1, controlCommand.left, mmpvalue / 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                        Snackbar.make(getView(), "左半转码盘测试" + mmpvalue, Snackbar.LENGTH_SHORT).show();
                    }
                    else
                    {
                        controlCommand.C_ComData(controlCommand.CC_ORDER, controlCommand.left, mmpvalue / 10, 0, 0, false);
                        Snackbar.make(getView(), "从车左半转码盘测试" + mmpvalue, Snackbar.LENGTH_SHORT).show();
                    }

                    mmpvaluebzleft = mmpvalue;
                    vibrator.vibrate(100);
                    break;
                case R.id.mpleftdtbtn://左转掉头码盘测试 left
                    try {
                        mmpvalue = Integer.parseInt(text.getText().toString());// 将  码盘 值 赋给全局变量
                    } catch (Exception e) {
                        // TODO: handle exception
                        vibrator.vibrate(500);
                        Snackbar.make(getView(), "还没有输入码盘呢", Snackbar.LENGTH_SHORT).show();
                        break;
                    }
                    if(controlCommand.z_c_flag == 0)//主车
                    {
                        controlCommand.comDataTest(1, controlCommand.left, mmpvalue / 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                        Snackbar.make(getView(), "左掉头码盘测试" + mmpvalue, Snackbar.LENGTH_SHORT).show();
                    }
                    else
                    {
                        controlCommand.C_ComData(controlCommand.CC_ORDER, controlCommand.left, mmpvalue / 10, 0, 0, false);
                        Snackbar.make(getView(), "从车左掉头转码盘测试" + mmpvalue, Snackbar.LENGTH_SHORT).show();
                    }

                    mmpvaluedtleft = mmpvalue;
                    vibrator.vibrate(100);
                    break;
                case R.id.mpbzrightbtn://右转码盘测试 left
                    try {
                        mmpvalue = Integer.parseInt(text.getText().toString());// 将  码盘 值 赋给全局变量
                    } catch (Exception e) {
                        // TODO: handle exception
                        vibrator.vibrate(500);
                        Snackbar.make(getView(), "还没有输入码盘呢", Snackbar.LENGTH_SHORT).show();
                        break;
                    }
                    if(controlCommand.z_c_flag == 0)//主车
                    {
                        controlCommand.comDataTest(1, controlCommand.right, mmpvalue / 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                        Snackbar.make(getView(), "右半转码盘测试" + mmpvalue, Snackbar.LENGTH_SHORT).show();
                    }
                    else
                    {
                        controlCommand.C_ComData(controlCommand.CC_ORDER, controlCommand.right, mmpvalue / 10, 0, 0, false);
                        Snackbar.make(getView(), "从车右半转码盘测试" + mmpvalue, Snackbar.LENGTH_SHORT).show();
                    }

                    mmpvaluebzright = mmpvalue;
                    vibrator.vibrate(100);
                    break;
                case R.id.mprightdtbtn://you转diantou码盘测试 left
                    try {
                        mmpvalue = Integer.parseInt(text.getText().toString());// 将  码盘 值 赋给全局变量
                    } catch (Exception e) {
                        // TODO: handle exception
                        vibrator.vibrate(500);
                        Snackbar.make(getView(), "还没有输入码盘呢", Snackbar.LENGTH_SHORT).show();
                        break;
                    }
                    if(controlCommand.z_c_flag == 0)//主车
                    {
                        controlCommand.comDataTest(1, controlCommand.right, mmpvalue / 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                        Snackbar.make(getView(), "右掉头码盘测试" + mmpvalue, Snackbar.LENGTH_SHORT).show();
                    }
                    else
                    {
                        controlCommand.C_ComData(controlCommand.CC_ORDER, controlCommand.right, mmpvalue / 10, 0, 0, false);
                        Snackbar.make(getView(), "从车右掉头码盘测试" + mmpvalue, Snackbar.LENGTH_SHORT).show();
                    }

                    mmpvaluedtright = mmpvalue;
                    vibrator.vibrate(100);
                    break;
                case R.id.mprightbtn://右转码盘测试
                    try {
                        mmpvalue = Integer.parseInt(text.getText().toString());// 将  码盘 值 赋给全局变量
                    } catch (Exception e) {
                        // TODO: handle exception
                        vibrator.vibrate(500);
                        Snackbar.make(getView(), "还没有输入码盘呢", Snackbar.LENGTH_SHORT).show();
                        break;
                    }
                    if(controlCommand.z_c_flag == 0)//主车
                    {
                        controlCommand.comDataTest(1, controlCommand.right, mmpvalue / 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                        Snackbar.make(getView(), "右转码盘测试" + mmpvalue, Snackbar.LENGTH_SHORT).show();
                    }
                    else
                    {
                        controlCommand.C_ComData(controlCommand.CC_ORDER, controlCommand.right, mmpvalue / 10, 0, 0, false);
                        Snackbar.make(getView(), "从车右转码盘测试" + mmpvalue, Snackbar.LENGTH_SHORT).show();
                    }

                    mmpvalueright = mmpvalue;
                    vibrator.vibrate(100);
                    break;
                case R.id.mpsave://  保存码盘文本 go
                    String res = "左半转:" + mmpvaluebzleft + "左转:" + mmpvalueleft + "左掉头:" + mmpvaluedtleft + "\n右半转" + mmpvaluebzright +
                            "右转" + mmpvalueright + "右掉头" + mmpvaluedtright + "\n前进" + mmpvaluego + "后退" + mmpvalueback;

                    vibrator.vibrate(100);

                    if(controlCommand.z_c_flag == 0)//主车
                    {
                        new Fill().saveFile(File_Name.MPVALUE, res);  //保存识别结果
                        Snackbar.make(getView(), "主车码盘已保存" + mmpvalue, Snackbar.LENGTH_SHORT).show();
                    }
                    else
                    {
                        new Fill().saveFile(File_Name.C_MPVALUE, res);  //保存识别结果
                        Snackbar.make(getView(), "从车码盘已保存" + mmpvalue, Snackbar.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.mpvalup://  按键 微调码盘 ++
                    try {
                        mmpvalue = Integer.parseInt(text.getText().toString());// 将  码盘 值 赋给全局变量
                    } catch (Exception e) {
                        // TODO: handle exception
                        vibrator.vibrate(500);
                        Snackbar.make(getView(), "还没有输入码盘呢", Snackbar.LENGTH_SHORT).show();
                        break;
                    }

                    mmpvalue += 10;
                    if (mmpvalue > 2500) {
                        mmpvalue = 2500;
                    }
                    text.setText(mmpvalue + "");
                    mmp.setProgress(mmpvalue);
                    vibrator.vibrate(50);

                    break;
                case R.id.mpvaldown://  按键 微调码盘 --
                    try {
                        mmpvalue = Integer.parseInt(text.getText().toString());// 将  码盘 值 赋给全局变量
                    } catch (Exception e) {
                        // TODO: handle exception
                        vibrator.vibrate(500);
                        Snackbar.make(getView(), "还没有输入码盘呢", Snackbar.LENGTH_SHORT).show();
                        break;
                    }

                    mmpvalue -= 10;
                    if (mmpvalue < 0) {
                        mmpvalue = 0;
                    }
                    text.setText(mmpvalue + "");
                    mmp.setProgress(mmpvalue);//  设置seekbar的值 滑动条可随数值滑动
                    vibrator.vibrate(50);
                    break;
                case R.id.mpcle://  码盘清楚按键
                    text.setText("");
                    break;

            }
        }
    };
    /*
     */

    //获得文本框码盘方法
    private int GetEncoder() {
        String src = et_mp.getText().toString(); // 从文本框中获取码盘信息
        int encoder = 0;
        if (!src.equals("")) {
            encoder = Integer.parseInt(src); // 把字符转换为数字
            Log.e("码盘码盘", Integer.toString(encoder));
        } else {
            makeText(getContext(), "请输入码盘", Toast.LENGTH_SHORT).show();
        }
        return encoder;
    }



    Thread mthread=new Thread(new Runnable() {
        @Override
        public void run() {
            controlCommand.Discern(4, 2); //二维码识别
            controlCommand.comData(1, controlCommand.OUT_UPPER_MODE, 1, 0, 0, 0,0, 0, 0, 0, 0, 0, 0, true);
        }
    });


    Thread Tthread=new Thread(new Runnable() {
        @Override
        public void run() {

            String str = "ffffffffffff";
            Log.e( "白卡密钥: ",str );
            int rfidmy_num1 = Integer.valueOf(str.substring(0,2),16);
            int rfidmy_num2 = Integer.valueOf(str.substring(2,4),16);
            int rfidmy_num3 = Integer.valueOf(str.substring(4,6),16);
            int rfidmy_num4 = Integer.valueOf(str.substring(6,8),16);
            int rfidmy_num5 = Integer.valueOf(str.substring(8,10),16);
            int rfidmy_num6 = Integer.valueOf(str.substring(10,12),16);
            controlCommand.comData(1, controlCommand.trends_data, controlCommand.rfid_my, 1, rfidmy_num1, rfidmy_num2, rfidmy_num3, rfidmy_num4,rfidmy_num5, rfidmy_num6, 0, 0, 0,true);


            int RFID_SQ1 = 2;
            //      RFID_SQ1 = Way.BS_RFID_SQ();   //获取扇区块地址
//                /*副指令第一位是指定那一张白卡1，2，3填入其他默认为1
//                        后三个指令对应块地址 0 不读卡  0x60 KEYA  0x61 KEYB*/
            controlCommand.comData(1, controlCommand.rfid_sq, 1, RFID_SQ1, RFID_SQ1, RFID_SQ1, 0, 0, 0, 0, 0, 0, 0, true);


            //           Way.BS_Tu_xing();

//            controlCommand.comData(1, controlCommand.xsjl,controlCommand.smg_jl+1,0,0,0,0,0,0,0,0,0,0,true);

//            Way.BS_CP_DISPOSE();

//            controlCommand.comData(1,controlCommand.qh_AB,controlCommand.bzw_A,controlCommand.xsjl,controlCommand.tft_jl+1,0,0,0,0,0,0,0,0,true);
//            controlCommand.comData(1, controlCommand.trends_data, controlCommand.tft_data_3, 0, 0x12, 0x34, 0x56, 0, 0, 0, 0, 0, 0, true);
//            controlCommand.comData(1,controlCommand.trends_data, controlCommand.tft_cp_6, controlCommand.TFT_CP_6[1], controlCommand.TFT_CP_6[2], controlCommand.TFT_CP_6[3], controlCommand.TFT_CP_6[4], controlCommand.TFT_CP_6[5],0,0,0,0,0,true);
//            controlCommand.comData(1, controlCommand.trends_data, controlCommand.xz_cp, controlCommand.GATE_CP_6[0], controlCommand.GATE_CP_6[1], controlCommand.GATE_CP_6[2], controlCommand.GATE_CP_6[3], controlCommand.GATE_CP_6[4],controlCommand. GATE_CP_6[5], 0, 0, 0, 0, true);


//
//            controlCommand.comData(1, controlCommand.trends_data, controlCommand.xz_cp, controlCommand.XZ_CP_8[0],controlCommand.XZ_CP_8[1], controlCommand.XZ_CP_8[2],controlCommand. XZ_CP_8[3], controlCommand.XZ_CP_8[4], controlCommand.XZ_CP_8[5], controlCommand.XZ_CP_8[6], controlCommand.XZ_CP_8[7], 0, 0, true);

            controlCommand.comData(1, controlCommand.OUT_UPPER_MODE, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);




        }
    });



    Thread thread=new Thread(new Runnable() {
        @Override
        public void run() {

            controlCommand.comData(1,controlCommand.smg_js,controlCommand.jsk,controlCommand.track,controlCommand.bk_xj,controlCommand.right,controlCommand.p_z,controlCommand.gate,controlCommand.on,controlCommand.track,controlCommand.bk_xj,controlCommand.left,controlCommand.p_z,true);
            controlCommand.comData(1,controlCommand.track,controlCommand.bk_xj,controlCommand.left,controlCommand.bz,0,0,0,0,0,0,0,0,true);
            controlCommand.Voice("第一次二维码识别", 3, true);
            controlCommand.Discern(4, 2); //二维码识别

            controlCommand.comData(1,controlCommand.right, controlCommand.bz, controlCommand.track,controlCommand.bk_xj, controlCommand.right,controlCommand.p_z, controlCommand.qh_AB,controlCommand.bzw_B,controlCommand.tft_js,controlCommand.jsk,controlCommand.left,controlCommand.p_z, true);
            controlCommand.comData(1, controlCommand.cj, 1, controlCommand.xsjl, controlCommand.xz_jl+1, controlCommand.xsjl,controlCommand.smg_jl+1, controlCommand.left,controlCommand.p_z, controlCommand.track,controlCommand.bk_xj, 0,0,true);
            controlCommand.yanchi(1100);
            controlCommand.comData(1,controlCommand.track,controlCommand.bk_xj,controlCommand.right,controlCommand.p_z,controlCommand.tg, 6 , ControlCommand.left,controlCommand.p_z, ControlCommand.left,controlCommand.p_z,controlCommand.track,controlCommand.bk_xj,true);
            controlCommand.comData(1,controlCommand.left,controlCommand.bz,0,0,0,0,0,0,0,0,0,0,true);


            Way.BS_CP_DISPOSE();
            controlCommand.comData(1, controlCommand.trends_data, controlCommand.xz_cp, controlCommand.GATE_CP_6[0], controlCommand.GATE_CP_6[1], controlCommand.GATE_CP_6[2], controlCommand.GATE_CP_6[3], controlCommand.GATE_CP_6[4],controlCommand. GATE_CP_6[5], 0, 0, 0, 0, true);

            controlCommand.comData(1, controlCommand.trends_data, controlCommand.xz_cp, controlCommand.XZ_CP_8[0], controlCommand.XZ_CP_8[1], controlCommand.XZ_CP_8[2],controlCommand. XZ_CP_8[3], controlCommand.XZ_CP_8[4], controlCommand.XZ_CP_8[5], controlCommand.XZ_CP_8[6], controlCommand.XZ_CP_8[7], 0, 0, true);



            controlCommand.comData(1,controlCommand.left,controlCommand.bz,controlCommand.track,controlCommand.bk_xj,controlCommand.right,controlCommand.bz,controlCommand.beeper,controlCommand.on,controlCommand.right,controlCommand.bz,controlCommand.track,controlCommand.bk_xj,true);
            controlCommand.comData(1,controlCommand.left,controlCommand.dt,controlCommand.auto_rk,1,0,0,0,0,0,0,0,0,true);
            //     comData(1, ltck_layer, 1, back, 120, ltck_layer, 2, 0, 0, 0, 0, 0, 0, true);
            //     comData(1, ltck_layer, 1, go, 50, 0, 0, 0, 0, 0, 0, 0, 0, true);













        }
    });


    Thread hthread=new Thread(new Runnable() {
        @Override
        public void run() {


            controlCommand.comData(1,controlCommand.smg_js,controlCommand.jsk,controlCommand.track,controlCommand.x,controlCommand.left,controlCommand.bz,controlCommand.right,controlCommand.bz,controlCommand.right,controlCommand.p_z,controlCommand.gate,controlCommand.on,true);
            controlCommand.comData(1,controlCommand.track,controlCommand.x,controlCommand.left,controlCommand.bz,0,0,0,0,0,0,0,0,true);
            controlCommand.Voice("第一次二维码识别", 3, true);
            controlCommand.Discern(4, 0); //二维码识别
            controlCommand.yanchi(1000);
            controlCommand.comData(1,controlCommand.right,controlCommand.bz,controlCommand.track,controlCommand.x,controlCommand.left,controlCommand.bz,0,0,0,0,0,0,true);
            controlCommand.Voice("图形识别", 3, true);
            controlCommand.yanchi(1000);
            controlCommand.comData(1,controlCommand.left,controlCommand.bz,controlCommand.track,controlCommand.x,controlCommand.right,controlCommand.p_z,0,0,0,0,0,0,true);
            controlCommand.Voice("语音播报", 3, true);
            controlCommand.yanchi(1000);
            controlCommand.comData(1, controlCommand.voice_sb, 0,controlCommand.left, controlCommand.p_z, controlCommand.left, controlCommand.p_z, 0,0, 0, 0,  0, 0,true);
            controlCommand.yanchi(2000);
            controlCommand.comData(1,controlCommand.track,controlCommand.bk_xj,0,0,0,0,0,0,0,0,0,0,true);
            controlCommand.yanchi(1100);
            controlCommand.comData(1,controlCommand.track,controlCommand.bk_xj,0,0,0,0,0,0,0,0,0,0,true);
            controlCommand.Voice("测距", 3, true);
            controlCommand.comData(1,controlCommand.cj,1,controlCommand.xsjl,controlCommand.xz_jl+1, controlCommand.xsjl,controlCommand.smg_jl+1,controlCommand.right,controlCommand.p_z,controlCommand.track,controlCommand.bk_xj,controlCommand.right,controlCommand.p_z,true);
            controlCommand.Voice("红绿灯",3,true);
            controlCommand.yanchi(2000);
            controlCommand.comData(1, controlCommand.jtd_open, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
            controlCommand.yanchi(1000);
            int num1 = Traffic.Traffic_Out();  //交通灯结果
            controlCommand.comData(1, controlCommand.jtd_jg, num1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
            controlCommand.comData(1,controlCommand.track,controlCommand.time_x,controlCommand.left,controlCommand.p_z,0,0,0,0,0,0,0,0,true);
            controlCommand.Voice("调光", 3, true);

            controlCommand.comData(1,controlCommand.tg,3,0,0,0,0,0,0,0,0,0,0,true);  //主车调光
            controlCommand.comData(1,controlCommand.right,controlCommand.p_z,controlCommand.track,controlCommand.x,controlCommand.left,controlCommand.p_z,0,0,0,0,0,0,true);











            //controlCommand.comData(1,controlCommand.track,controlCommand.x,controlCommand.left,controlCommand.bz,0,0,0,0,0,0,0,0,true);

            //controlCommand.Voice("TFT车牌识别", 3, true);




            //controlCommand.Discern(2, 0);

            //controlCommand.comData(1, controlCommand.trends_data, controlCommand.tft_cp_6, controlCommand.TFT_CP_6[0], controlCommand.TFT_CP_6[1], controlCommand.TFT_CP_6[2], controlCommand.TFT_CP_6[3], controlCommand.TFT_CP_6[4], controlCommand.TFT_CP_6[5], 0, 0, 0, 0, true);

            //controlCommand.comData(1,controlCommand.right,controlCommand.bz,controlCommand.right,controlCommand.p_z,0,0,0,0,0,0,0,0,true);

            //             controlCommand.Voice("道闸", 3, true);
            //             controlCommand.yanchi(1500);
            //            String cp_str = controlCommand.Discern_plate();
            //             Way.Get_DZ_CP();
            //             controlCommand.comData(1, controlCommand.trends_data, controlCommand.gate_cp_6, controlCommand.GATE_CP_6[0],controlCommand. GATE_CP_6[1], controlCommand.GATE_CP_6[2], controlCommand.GATE_CP_6[3], controlCommand.GATE_CP_6[4], controlCommand.GATE_CP_6[5], controlCommand.gate, controlCommand.on, 0, 0, true);        //主车识别车牌
            //         controlCommand.Voice(cp_str, 3, true);

            //        controlCommand.comData(1,controlCommand.track,controlCommand.x,controlCommand.left,controlCommand.bz,0,0,0,0,0,0,0,0,true);
            //
            //           controlCommand.Voice("第一次二维码识别", 3, true);
            //        controlCommand.Discern(4, 2); //二维码识别

            //     controlCommand.comData(1,controlCommand.right,controlCommand.bz,controlCommand.track,controlCommand.x,controlCommand.left,controlCommand.bz,0,0,0,0,0,0,true);

            //        controlCommand.comData(1, controlCommand.tft_fy, controlCommand.u_f + 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
            //        controlCommand.yanchi(2000);
            //        String str = controlCommand.Discern(1, 0);
            //                 str = str.substring(0,5);
            //     controlCommand.Voice(str, 3, true);

            //      controlCommand.comData(1,controlCommand.left,controlCommand.bz,controlCommand.track,controlCommand.x,controlCommand.right,controlCommand.p_z,0,0,0,0,0,0,true);

            //       String voice_sb="";
            //       if(voice_sb.equals("")){
            //            controlCommand.Voice("aa", 3, true);//2语音播报标志物操作，3小车播报语音
            //        }else{
            //           controlCommand.Voice(voice_sb, 3, true);//2语音播报标志物操作，3小车播报语音
            //       }

            //      controlCommand.comData(1,controlCommand.left,controlCommand.p_z,controlCommand.left,controlCommand.p_z,controlCommand.track,controlCommand.bk_xj,0,0,0,0,0,0,true);
            //     controlCommand.yanchi(1100);
            //     controlCommand.comData(1,controlCommand.track,controlCommand.bk_xj,0,0,0,0,0,0,0,0,0,0,true);
            //     controlCommand.comData(1, controlCommand.cj, 1, controlCommand.xsjl, controlCommand.xz_jl+1, controlCommand.xsjl,controlCommand.smg_jl+1,0,0,0,0,0,0,true);
            //     controlCommand.comData(1,controlCommand.right,controlCommand.p_z,controlCommand.track,controlCommand.x,controlCommand.right,controlCommand.p_z,0,0,0,0,0,0,true);
            //     controlCommand.Voice("交通灯识别A", 3, true);
            //     controlCommand.Discern(3, 3); //交通灯识别  预设位对应为3





        }
    });



    /*
                对话框 部分
                对话框 部分
                对话框 部分
     */
    //全自动对话框及监听事件

    Thread tthread=new Thread(new Runnable() {
        @Override
        public void run() {

            controlCommand.comData(1, controlCommand.smg_js, controlCommand.jsk, controlCommand.track, controlCommand.x, controlCommand.left, controlCommand.bz, 0, 0, 0, 0, 0, 0, true);
            controlCommand.Voice("车牌识别", 3, true);
            controlCommand.comData(1, controlCommand.qh_AB, controlCommand.bzw_A, controlCommand.tft_zd, 4, 0, 0, 0, 0, 0, 0, 0, 0, true);
            controlCommand.yanchi(1500);
            controlCommand.Discern(2, 1);
            String cp_str = controlCommand.Discern_plate();
            Way.Get_DZ_CP();
            controlCommand.comData(1, controlCommand.right, controlCommand.bz, controlCommand.right, controlCommand.p_z, 0, 0, 0, 0, 0, 0, 0, 0, true);
            controlCommand.comData(1, controlCommand.trends_data, controlCommand.gate_cp_6, controlCommand.GATE_CP_6[0], controlCommand.GATE_CP_6[1], controlCommand.GATE_CP_6[2], controlCommand.GATE_CP_6[3], controlCommand.GATE_CP_6[4], controlCommand.GATE_CP_6[5], controlCommand.gate, controlCommand.on, 0, 0, true);        //主车识别车牌
            controlCommand.Voice(cp_str, 3, true);
            controlCommand.comData(1, controlCommand.track, controlCommand.x, controlCommand.left, controlCommand.bz, 0, 0, 0, 0, 0, 0, 0, 0, true);
            controlCommand.Voice("第一次二维码识别", 3, true);
            controlCommand.Discern(4, 0); //二维码识别
            controlCommand.yanchi(1000);
            controlCommand.comData(1, controlCommand.right, controlCommand.bz, controlCommand.track, controlCommand.x, controlCommand.left, controlCommand.bz, 0, 0, 0, 0, 0, 0, true);

            controlCommand.Voice("图形识别", 3, true);
            controlCommand.yanchi(1000);
            controlCommand.comData(1, controlCommand.qh_AB, controlCommand.bzw_B, controlCommand.tft_zd, 2, 0, 0, 0, 0, 0, 0, 0, 0, true);
            controlCommand.yanchi(2000);
            String str = controlCommand.Discern(1, 0);
            controlCommand.yanchi(1000);
            controlCommand.Voice(str, 3, true);

            controlCommand.comData(1, controlCommand.left, controlCommand.bz, controlCommand.track, controlCommand.x, controlCommand.right, controlCommand.p_z, 0, 0, 0, 0, 0, 0, true);
            controlCommand.Voice("语音播报", 3, true);
            controlCommand.yanchi(1000);
            controlCommand.comData(1, controlCommand.voice_sb, 0, controlCommand.left, controlCommand.p_z, controlCommand.left, controlCommand.p_z, 0, 0, 0, 0, 0, 0, true);
            controlCommand.yanchi(2000);
            controlCommand.comData(1, controlCommand.track, controlCommand.bk_xj, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
            controlCommand.yanchi(1100);
            controlCommand.comData(1, controlCommand.track, controlCommand.bk_xj, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
            controlCommand.Voice("测距", 3, true);
            controlCommand.comData(1,controlCommand.cj,1,controlCommand.xsjl,controlCommand.xz_jl + 1,controlCommand.xsjl,controlCommand.smg_jl + 1,0,0,0,0,0,0,true);
            if(controlCommand.xsjl<300)
            {
                controlCommand.comData(1,controlCommand.back,40,0,0,0,0,0,0,0,0,0,0,true);
                controlCommand.Voice("第二次二维码识别", 3, true);
                controlCommand.Discern(5, 0); //二维码识别
                controlCommand.comData(1,controlCommand.track,controlCommand.x,controlCommand.right, controlCommand.p_z, controlCommand.track, controlCommand.bk_xj, controlCommand.right, controlCommand.p_z,0,0,0,0,true);

            }else{
                controlCommand.Voice("第二次二维码识别", 3, true);
                controlCommand.Discern(5, 0); //二维码识别
                controlCommand.comData(1,controlCommand.right, controlCommand.p_z, controlCommand.track, controlCommand.bk_xj, controlCommand.right, controlCommand.p_z,0,0,0,0,0,0,true);
            }


            controlCommand.comData(1,controlCommand.right,controlCommand.bz,controlCommand.trends_data, controlCommand.xz_cp, controlCommand.GATE_CP_6[0], controlCommand.GATE_CP_6[1], controlCommand.GATE_CP_6[2], controlCommand.GATE_CP_6[3], controlCommand.GATE_CP_6[4], controlCommand.GATE_CP_6[5],  0, 0, true);
            controlCommand.comData(1,controlCommand.left,controlCommand.bz,0,0,0,0,0,0,0,0,0,0,true);


            controlCommand.comData(1, controlCommand.track, controlCommand.x, controlCommand.right, controlCommand.p_z, controlCommand.track, controlCommand.x, controlCommand.left, controlCommand.dt, 0, 0, 0, 0, true);

            controlCommand.C_ComData(controlCommand.CC_ORDER, controlCommand.track, controlCommand.x, controlCommand.right, controlCommand.p_z, true);
            controlCommand.C_ComData(controlCommand.CC_ORDER, controlCommand.track, controlCommand.x, controlCommand.back, 30, true);
            controlCommand.Voice("红绿灯", 3, true);
            controlCommand.yanchi(2000);
            controlCommand.C_ComData(controlCommand.CC_ORDER, controlCommand.qh_AB, controlCommand.bzw_A, controlCommand.jtd_open, 0, true);
            controlCommand.yanchi(1000);
            int num1 = Traffic.Traffic_Out();  //交通灯结果
            controlCommand.C_ComData(controlCommand.CC_ORDER, controlCommand.qh_AB, controlCommand.bzw_A, controlCommand.jtd_jg, num1, true);
            controlCommand.C_ComData(controlCommand.CC_ORDER,controlCommand.track,controlCommand.x,0,0,true);

            controlCommand.C_ComData(controlCommand.CC_ORDER, controlCommand.track, controlCommand.x, controlCommand.left, controlCommand.p_z, true);
            controlCommand.C_ComData(controlCommand.CC_ORDER, controlCommand.track, controlCommand.x, controlCommand.right, controlCommand.p_z, true);
            controlCommand.C_ComData(controlCommand.CC_ORDER, controlCommand.cj, 1, controlCommand.xsjl, controlCommand.xz_jl + 1, true);
            controlCommand.C_ComData(controlCommand.CC_ORDER, controlCommand.xsjl, controlCommand.smg_jl + 1, controlCommand.left, controlCommand.p_z, true);
            controlCommand.C_ComData(controlCommand.CC_ORDER,controlCommand.track,controlCommand.x,controlCommand.track,controlCommand.x,true);
            controlCommand.C_ComData(controlCommand.CC_ORDER,controlCommand.right,controlCommand.dt,0,0,true);


            controlCommand.comData(1, ControlCommand.track,controlCommand.x,0,0,0,0,0,0,0,0,0,0,true);

            controlCommand.Voice("调光", 3, true);
            controlCommand.yanchi(1500);
            controlCommand.comData(1,controlCommand.tg,3,0,0,0,0,0,0,0,0,0,0,true);  //主车调光

            controlCommand.comData(1,controlCommand.right, ControlCommand.p_z,controlCommand.right,controlCommand.bz,controlCommand.beeper,controlCommand.on,controlCommand.left,controlCommand.bz,controlCommand.track,controlCommand.x,controlCommand.right,controlCommand.p_z,true);
            controlCommand.comData(1,controlCommand.auto_ck,0,controlCommand.qh_AB, controlCommand.bzw_B, controlCommand.ltck_layer, 2,controlCommand.cxf,1,controlCommand.smg_js,controlCommand.jsg,0,0,true);
            controlCommand.comData(1, controlCommand.zxd, 0x11, controlCommand.buzz, controlCommand.on, 0, 0, 0, 0, 0, 0, 0, 0, true);
            controlCommand.yanchi(1100);
            controlCommand.comData(1, controlCommand.zxd, 0x00, controlCommand.buzz, controlCommand.off, 0, 0, 0, 0, 0, 0, 0, 0, true);
            // controlCommand.comData(1,controlCommand.right,controlCommand.p_z,controlCommand.track,controlCommand.x,controlCommand.right,controlCommand.bz,controlCommand.beeper,controlCommand.on,0,0,0,0,true);
            // controlCommand.comData(1,controlCommand.left,controlCommand.bz,controlCommand.right,controlCommand.p_z,controlCommand.auto_ck,0,controlCommand.cxf,controlCommand.on,0,0,0,0,true);











        }
    });

    Thread mmthread=new Thread(new Runnable() {
        @Override
        public void run() {



            Way.BS_LTXS_DISPOSE();    //立体显示
            //    controlCommand.comData(1, controlCommand.trends_data,  controlCommand.xz_cp,  controlCommand.XZ_CP_8[0],  controlCommand.XZ_CP_8[1],  controlCommand.XZ_CP_8[2],  controlCommand.XZ_CP_8[3],  controlCommand.XZ_CP_8[4],  controlCommand.XZ_CP_8[5],  controlCommand.XZ_CP_8[6],  controlCommand.XZ_CP_8[7], 0, 0, true);

            controlCommand.comData(1, controlCommand.trends_data,  controlCommand.xz_cp, controlCommand.XZ_CP_8[0],  controlCommand.XZ_CP_8[1], controlCommand.XZ_CP_8[2],controlCommand.XZ_CP_8[3],  controlCommand.XZ_CP_8[4],controlCommand.XZ_CP_8[5],  controlCommand.XZ_CP_8[6],controlCommand.XZ_CP_8[7], 0,0, true);

        }
    });




    Thread poathread=new Thread(new Runnable() {
        @Override
        public void run() {

//            controlCommand.Z_B_Init("F6D", false);
//
//
//            controlCommand.yanchi(2000);
//
//
//            String zb_text = "";//坐标变量
//            zb_text = "F7D" + "0".toString();
//
//            //自动
//            controlCommand.Auto_Run(zb_text, false);

            controlCommand.Voice("车牌识别", 3, true);
            controlCommand.comData(1, controlCommand.qh_AB, controlCommand.bzw_A, controlCommand.tft_zd, 4, 0, 0, 0, 0, 0, 0, 0, 0, true);
            controlCommand.yanchi(1500);
            String cp_str = controlCommand.Discern_plate();
            Way.Get_DZ_CP();


            long beginTime = System.currentTimeMillis();//开始时间
            long overTime = 10 * 1000;//运行时间
            while (cp_str.equals("国H358B2"))
            {
                controlCommand.comData(1, controlCommand.qh_AB, controlCommand.bzw_A, controlCommand.tft_fy,controlCommand.d_f+1, 0, 0, 0, 0, 0, 0, 0, 0, true);
                String cp_ctr = controlCommand.Discern_plate();
                cp_str=cp_ctr;

                long nowTime = System.currentTimeMillis();
                if((nowTime - beginTime) > overTime){
                    controlCommand.Voice("识别失败",3,true);
                    break;
                }
            }
            controlCommand.Voice(cp_str, 3, true);
            controlCommand.comData(1,controlCommand.track,controlCommand.x,0,0,0,0,0,0,0,0,0,0,true);

        }
    });



    Thread idthread=new Thread(new Runnable() {
        @Override
        public void run() {


//            controlCommand.Voice("图形识别", 3, true);
//            controlCommand.yanchi(1000);
//
//
//            controlCommand.comData(1, controlCommand.qh_AB, controlCommand.bzw_B, controlCommand.tft_zd, 4, 0, 0, 0, 0, 0, 0, 0, 0, true);
//
//            Looper.prepare();
//            String strd = controlCommand.Discern(1, 0);
//            controlCommand.yanchi(35000);
//            Way.BS_Tu_xing();
//            controlCommand.yanchi(2000);
//            controlCommand.comData(1, controlCommand.trends_data,controlCommand.tftb_data_3 ,0, ControlCommand.TFT_Data_3[0] , ControlCommand.TFT_Data_3[1], ControlCommand.TFT_Data_3[2], 0, 0, 0, 0, 0, 0,true);
//            Log.i("111111111111","11111");
//            controlCommand.Voice("完成", 3, true);;
//            controlCommand.comData(1,controlCommand.track,controlCommand.x,0,0,0,0,0,0,0,0,0,0,true);
//
//
//            controlCommand.yanchi(1000);

//            controlCommand.Voice("交通标志识别", 3, true);
//            controlCommand.comData(1, controlCommand.qh_AB, controlCommand.bzw_A, controlCommand.tft_zd, 8, 0, 0, 0, 0, 0, 0, 0, 0, true);
//            String JTBZ = controlCommand.Discern(8, 0);
//            controlCommand.yanchi(7000);
//            controlCommand.Voice(JTBZ,3,true);


            //       报警台
            //         Way.BS_beeper_HW();
            //         controlCommand.comData(1,controlCommand.trends_data,controlCommand.hw_data_6,controlCommand.HW_Data_6[0],controlCommand.HW_Data_6[1],controlCommand.HW_Data_6[2], controlCommand.HW_Data_6[3],controlCommand.HW_Data_6[4],controlCommand.HW_Data_6[5],0,0,0,0,true);
            //        controlCommand.comData(1, controlCommand.voice_sb, 0,0, 0, 0, 0, 0,0, 0, 0,  0, 0,true);
            String str = "ffffffffffff";
            Log.e( "白卡密钥: ",str );
            int rfidmy_num1 = Integer.valueOf(str.substring(0,2),16);
            int rfidmy_num2 = Integer.valueOf(str.substring(2,4),16);
            int rfidmy_num3 = Integer.valueOf(str.substring(4,6),16);
            int rfidmy_num4 = Integer.valueOf(str.substring(6,8),16);
            int rfidmy_num5 = Integer.valueOf(str.substring(8,10),16);
            int rfidmy_num6 = Integer.valueOf(str.substring(10,12),16);
            controlCommand.comDataTest(1, controlCommand.trends_data, controlCommand.rfid_my, 1, rfidmy_num1, rfidmy_num2, rfidmy_num3, rfidmy_num4,rfidmy_num5, rfidmy_num6, 0, 0, 0);

        }
    });


    Thread operthread=new Thread(new Runnable() {
        @Override
        public void run() {

            controlCommand.Z_B_Init("D6D",true);

            String car_smg_text ="D6";
            int datas=Integer.valueOf(car_smg_text.substring(0,2),16);//  将字符转换成16

            controlCommand.comData(1,controlCommand.smg_js,controlCommand.jsk,controlCommand.car_smg,datas,0,0,0,0,0,0,0,0,true);
            controlCommand.yanchi(1000);
            controlCommand.comData(1, controlCommand.trends_data, controlCommand.zigbee_data_8, 0x55,0x0a,0x01,0x01,0x00,0x00,0x02,0xBB, 0, 0, true);
            controlCommand.yanchi(7000);
            controlCommand.comData(1,controlCommand.go,50,controlCommand.left,controlCommand.p_z,0,0,0,0,0,0,0,0,true);

            controlCommand.Voice("红绿灯",3,true);
            controlCommand.comData(1,controlCommand.qh_AB,controlCommand.bzw_A,controlCommand.jtd_open,0,0,0,0,0,0,0,0,0,true);
            int num1 = Traffic.Traffic_Out();  //交通灯结果
            controlCommand.comData(1, controlCommand.qh_AB, controlCommand.bzw_A, controlCommand.jtd_jg, num1, controlCommand.track,controlCommand.x, 0, 0, 0, 0, 0, 0, true);

            controlCommand.Voice("测距", 3, true);
            controlCommand.comData(1,controlCommand.cj,1,controlCommand.xsjl,controlCommand.xz_jl + 1,controlCommand.xsjl,controlCommand.smg_jl + 1,0,0,0,0,0,0,true);

            int nums=0;   //距离自动
            String data=null;
            try {
                data =  new Fill().carRead(File_Name.HOST_RANGE+ 1 + ".txt");
            } catch (IOException e) {
                e.printStackTrace();
            }

            Integer i=new Integer(data);
            nums=i.intValue();
            nums=nums/10;

            if(nums<40)
            {
                int sume=40-nums;
                controlCommand.comData(1,controlCommand.back,sume,0,0,0,0,0,0,0,0,0,0,true);
                controlCommand.Voice("第一次二维码识别", 3, true);
                controlCommand.Discern(5, 0); //二维码识别
                controlCommand.comData(1,controlCommand.go,sume,controlCommand.right,controlCommand.p_z,controlCommand.track, controlCommand.x,0,0,0,0,0,0,true);

            }else{
                controlCommand.Voice("第一次二维码识别", 3, true);
                controlCommand.Discern(5, 0); //二维码识别
                controlCommand.comData(1,controlCommand.right, controlCommand.p_z, controlCommand.track, controlCommand.x,0,0,0,0,0,0,0,0,true);
            }


            controlCommand.comData(1,controlCommand.right,controlCommand.p_z,controlCommand.track,controlCommand.x,0,0,0,0,0,0,0,0,true);


            controlCommand.Voice("调光", 3, true);

            int nugt= Way.basyunt();


            controlCommand.comData(1,controlCommand.tg_gz_get,0x88,controlCommand.tg,nugt,0,0,0,0,0,0,0,0,true);
            controlCommand.yanchi(1000 );
            controlCommand.comData(1,controlCommand.left,controlCommand.p_z,controlCommand.track,controlCommand.x,controlCommand.right,controlCommand.p_z,0,0,0,0,0,0,true);

            controlCommand.Voice("图形识别", 3, true);
            controlCommand.yanchi(1000);

            controlCommand.comData(1, controlCommand.qh_AB, controlCommand.bzw_B, controlCommand.tft_zd, 2, 0, 0, 0, 0, 0, 0, 0, 0, true);

            Looper.prepare();
            String strd = controlCommand.Discern(1, 0);
            controlCommand.yanchi(35000);
            Way.BS_Tu_xing();
            controlCommand.yanchi(2000);
            controlCommand.comData(1, controlCommand.trends_data,controlCommand.tftb_data_3 ,0, ControlCommand.TFT_Data_3[0] , ControlCommand.TFT_Data_3[1], ControlCommand.TFT_Data_3[2], 0, 0, 0, 0, 0, 0,true);

            controlCommand.Voice("完成", 3, true);;


            controlCommand.comData(1,controlCommand.left,controlCommand.p_z,0,0,0,0,0,0,0,0,0,0,true);
            controlCommand.Voice("语音播报", 3, true);
            controlCommand.yanchi(1000);
            controlCommand.comData(1, controlCommand.voice_sb,0,0,0,0,0,0,0,0,0,0,0,true);

            controlCommand.comData(1,controlCommand.left,controlCommand.p_z,controlCommand.track,controlCommand.bk_xj,controlCommand.track,controlCommand.bk_xj,controlCommand.right,controlCommand.p_z,0,0,0,0,true);


            controlCommand.Voice("车牌识别", 3, true);
            controlCommand.comData(1, controlCommand.qh_AB, controlCommand.bzw_A, controlCommand.tft_zd, 4, 0, 0, 0, 0, 0, 0, 0, 0, true);
            controlCommand.yanchi(1500);
            String cp_str = controlCommand.Discern_plate();
            Way.Get_DZ_CP();


            long beginTime = System.currentTimeMillis();//开始时间
            long overTime = 30 * 1000;//运行时间
            while (cp_str.equals("国H358B2"))
            {
                controlCommand.comData(1, controlCommand.qh_AB, controlCommand.bzw_A, controlCommand.tft_fy,controlCommand.d_f+1, 0, 0, 0, 0, 0, 0, 0, 0, true);
                String cp_ctr = controlCommand.Discern_plate();
                cp_str=cp_ctr;

                long nowTime = System.currentTimeMillis();
                if((nowTime - beginTime) > overTime){
                    controlCommand.Voice("识别失败",3,true);
                    break;
                }
            }

            controlCommand.Voice(cp_str, 3, true);
            controlCommand.yanchi(1000);

            controlCommand.Voice("交通标志识别", 3, true);
            controlCommand.comData(1, controlCommand.qh_AB, controlCommand.bzw_A, controlCommand.tft_zd, 8, 0, 0, 0, 0, 0, 0, 0, 0, true);
            String JTBZ = controlCommand.Discern(8, 0);
            controlCommand.yanchi(7000);
            controlCommand.Voice(JTBZ,3,true);
            controlCommand.comData(1,controlCommand.left,controlCommand.dt,0,0,0,0,0,0,0,0,0,0,true);
            controlCommand.comData(1, controlCommand.trends_data, controlCommand.gate_cp_6, controlCommand.GATE_CP_6[0], controlCommand.GATE_CP_6[1], controlCommand.GATE_CP_6[2], controlCommand.GATE_CP_6[3], controlCommand.GATE_CP_6[4], controlCommand.GATE_CP_6[5], 0, 0, 0, 0, true);        //主车识别车牌
            controlCommand.comData(1,controlCommand.track,controlCommand.x,controlCommand.left,controlCommand.dt,controlCommand.right,controlCommand.bz,0,0,0,0,0,0,true);
            controlCommand.Voice("立体显示",3,true);
            Way.BS_LTXS_DISPOSE();    //立体显示
            controlCommand.comData(1, controlCommand.trends_data,  controlCommand.xz_cp, controlCommand.XZ_CP_8[0],  controlCommand.XZ_CP_8[1], controlCommand.XZ_CP_8[2],controlCommand.XZ_CP_8[3],  controlCommand.XZ_CP_8[4],controlCommand.XZ_CP_8[5],  controlCommand.XZ_CP_8[6],controlCommand.XZ_CP_8[7], 0,0, true);

            controlCommand.comData(1,controlCommand.left,controlCommand.bz,controlCommand.right,controlCommand.dt,controlCommand.track,controlCommand.x,controlCommand.left,controlCommand.p_z,controlCommand.track,controlCommand.x,controlCommand.left,controlCommand.p_z,true);
            controlCommand.comData(1, controlCommand.qh_AB, controlCommand.bzw_B,controlCommand.ltck_auto,3,controlCommand.zxd, 0x11, controlCommand.buzz, controlCommand.on,0,0,0,0,true);

        }
    });




    Thread pfthread=new Thread(new Runnable() {
        @Override
        public void run() {

            controlCommand.Z_B_Init("F6D", true);
            controlCommand.comData(1,controlCommand.smg_js, controlCommand.jsk,controlCommand.go,2000,controlCommand.right,controlCommand.bz,0,0,0,0,0,0,true);
            controlCommand.Voice("车牌识别", 3, true);
            controlCommand.comData(1, controlCommand.qh_AB, controlCommand.bzw_A, controlCommand.tft_zd, 4, 0, 0, 0, 0, 0, 0, 0, 0, true);
            controlCommand.yanchi(1500);
            String cp_str = controlCommand.Discern_plate();
            Way.Get_DZ_CP();

            while (cp_str.equals("国H358B2"))
            {
                controlCommand.comData(1, controlCommand.qh_AB, controlCommand.bzw_A, controlCommand.tft_fy,controlCommand.d_f+1, 0, 0, 0, 0, 0, 0, 0, 0, true);
                String cp_ctr = controlCommand.Discern_plate();
                cp_str=cp_ctr;
            }

            controlCommand.Voice(cp_str, 3, true);

            controlCommand.comData(1,controlCommand.left,controlCommand.bz,controlCommand.left,controlCommand.p_z,controlCommand.track,controlCommand.x,0,0,0,0,0,0,true);

            controlCommand.Voice("测距", 3, true);

            controlCommand.comData(1,controlCommand.cj,1,controlCommand.xsjl,controlCommand.xz_jl + 1,controlCommand.xsjl,controlCommand.smg_jl + 1,0,0,0,0,0,0,true);

            int nums=0;   //距离自动
            String data=null;
            try {
                data =  new Fill().carRead(File_Name.HOST_RANGE+ 1 + ".txt");
            } catch (IOException e) {
                e.printStackTrace();
            }

            Integer i=new Integer(data);
            nums=i.intValue();
            nums=nums/10;

            if(nums<40)
            {
                int sume=40-nums;
                controlCommand.comData(1,controlCommand.back,sume,0,0,0,0,0,0,0,0,0,0,true);
                controlCommand.Voice("第一次二维码识别", 3, true);
                controlCommand.Discern(5, 0); //二维码识别
                controlCommand.comData(1,controlCommand.go,sume,controlCommand.right,controlCommand.p_z,controlCommand.track, controlCommand.bk_xj,0,0,0,0,0,0,true);

            }else{
                controlCommand.Voice("第一次二维码识别", 3, true);
                controlCommand.Discern(5, 0); //二维码识别
                controlCommand.comData(1,controlCommand.right, controlCommand.p_z, controlCommand.track, controlCommand.bk_xj,0,0,0,0,0,0,0,0,true);
            }

            controlCommand.comData(1,controlCommand.left,controlCommand.p_z,0,0,0,0,0,0,0,0,0,0,true);



            controlCommand.Voice("图形识别", 3, true);
            controlCommand.yanchi(1000);
            controlCommand.comData(1, controlCommand.qh_AB, controlCommand.bzw_B, controlCommand.tft_zd, 2, 0, 0, 0, 0, 0, 0, 0, 0, true);
            controlCommand.yanchi(1000);
            String strd = controlCommand.Discern(1, 0);

            Way.BS_Tu_xing();//tft
            controlCommand.comData(1, controlCommand.trends_data,controlCommand.tftb_data_3 ,0, ControlCommand.TFT_Data_3[0] , ControlCommand.TFT_Data_3[1], ControlCommand.TFT_Data_3[2], 0, 0, 0, 0, 0, 0,true);
            controlCommand.Voice(strd, 3, true);




            controlCommand.comData(1,controlCommand.right,controlCommand.dt,controlCommand.track,controlCommand.bk_xj,controlCommand.left,controlCommand.p_z,controlCommand.left,controlCommand.bz,0,0,0,0,true);

            controlCommand.Voice("立体显示",3,true);

            Way.BS_LTXS_DISPOSE();    //立体显示
            controlCommand.comData(1, controlCommand.trends_data,  controlCommand.xz_cp, controlCommand.XZ_CP_8[0],  controlCommand.XZ_CP_8[1], controlCommand.XZ_CP_8[2],controlCommand.XZ_CP_8[3],  controlCommand.XZ_CP_8[4],controlCommand.XZ_CP_8[5],  controlCommand.XZ_CP_8[6],controlCommand.XZ_CP_8[7], 0,0, true);


            controlCommand.comData(1,controlCommand.right,controlCommand.bz,controlCommand.right,controlCommand.p_z,controlCommand.track,controlCommand.x,controlCommand.left,controlCommand.p_z,0,0,0,0,true);

            controlCommand.comData(1, controlCommand.trends_data, controlCommand.gate_cp_6, controlCommand.GATE_CP_6[0], controlCommand.GATE_CP_6[1], controlCommand.GATE_CP_6[2], controlCommand.GATE_CP_6[3], controlCommand.GATE_CP_6[4], controlCommand.GATE_CP_6[5], controlCommand.gate, controlCommand.on, 0, 0, true);

            controlCommand.comData(1,controlCommand.track,controlCommand.x,0,0,0,0,0,0,0,0,0,0,true);

            controlCommand.Voice("语音播报", 3, true);
            controlCommand.yanchi(1000);
            controlCommand.comData(1, controlCommand.voice_sb,0,controlCommand.left,controlCommand.p_z,controlCommand.back,20,0,0,0,0,0,0,true);
            controlCommand.yanchi(1000);

            controlCommand.C_ComData(controlCommand.CC_ORDER,controlCommand.track,controlCommand.x,controlCommand.track,controlCommand.x,true);
            controlCommand.C_ComData(controlCommand.CC_ORDER,controlCommand.track,controlCommand.x,controlCommand.left,controlCommand.dt,true);

            controlCommand.C_ComData(controlCommand.CC_ORDER,controlCommand.right,controlCommand.bz,0,0,true);

            controlCommand.C_ComData(controlCommand.CC_ORDER,controlCommand.right,controlCommand.bz,controlCommand.track,controlCommand.x,true);


            controlCommand.C_ComData(controlCommand.CC_ORDER,controlCommand.right,controlCommand.p_z,controlCommand.track,controlCommand.x,true);
            controlCommand.C_ComData(controlCommand.CC_ORDER,controlCommand.right,controlCommand.p_z,controlCommand.right,controlCommand.dt,true);



            controlCommand.comData(1,controlCommand.track,controlCommand.x,0,0,0,0,0,0,0,0,0,0,true);
            controlCommand.yanchi(2000);
            controlCommand.comData(1, ControlCommand.track, ControlCommand.x,controlCommand.back,20,0,0,0,0,0,0,0,0,true);

            controlCommand.Voice("红绿灯",3,true);
            controlCommand.yanchi(2000);
            controlCommand.comData(1,controlCommand.qh_AB,controlCommand.bzw_A,controlCommand.jtd_open,0,0,0,0,0,0,0,0,0,true);
            int num1 = Traffic.Traffic_Out();  //交通灯结果
            controlCommand.yanchi(4000);
            controlCommand.comData(1, controlCommand.qh_AB, controlCommand.bzw_A, controlCommand.jtd_jg, num1, controlCommand.track,controlCommand.x, 0, 0, 0, 0, 0, 0, true);
            controlCommand.comData(1,controlCommand.track,controlCommand.x,0,0,0,0,0,0,0,0,0,0,true);
            controlCommand.Voice("调光", 3, true);

            int nugt= Way.bsunt();

            controlCommand.comData(1,controlCommand.tg_gz_get,0x88,controlCommand.tg,nugt,controlCommand.left,controlCommand.p_z,0,0,0,0,0,0,true);

            controlCommand.yanchi(4000);

            int sumer= Way.autorukit();

            if(sumer==0)
            {
                controlCommand.comData(1,controlCommand.track,controlCommand.x,controlCommand.track,controlCommand.x,controlCommand.right,controlCommand.dt,controlCommand.auto_rk,25,0,0,0,0,true);
            }else if(sumer==2)
            {
                controlCommand.comData(1,controlCommand.go,10,controlCommand.ltck_auto,0,0,0,0,0,0,0,0,0,true);
            }else{
                controlCommand.comData(1,controlCommand.go,10,controlCommand.ltck_auto,0,0,0,0,0,0,0,0,0,true);
            }



        }
    });




    Thread watthread =new Thread(new Runnable() {
        @Override   //调光
        public void run() {
            //    controlCommand.comData(1,controlCommand. tg_gz_get,0,0,0,0,0,0,0,0,0,0,0,true);

//            controlCommand.Voice("立体显示",3,true);
//
//            Way.BS_LTXS_DISPOSE();    //立体显示
//            controlCommand.comData(1, controlCommand.trends_data,  controlCommand.xz_cp, controlCommand.XZ_CP_8[0],  controlCommand.XZ_CP_8[1], controlCommand.XZ_CP_8[2],controlCommand.XZ_CP_8[3],  controlCommand.XZ_CP_8[4],controlCommand.XZ_CP_8[5],  controlCommand.XZ_CP_8[6],controlCommand.XZ_CP_8[7], 0,0, true);


//            controlCommand.Voice("语音播报", 3, true);
//            controlCommand.yanchi(1000);
//            controlCommand.comData(1, controlCommand.voice_sb,0,controlCommand.left,controlCommand.p_z,controlCommand.back,20,0,0,0,0,0,0,true);


            //道闸显示车牌
            Way.Get_DZ_CP();
            controlCommand.comData(1,  controlCommand.trends_data,  controlCommand.gate_cp_6,  controlCommand.GATE_CP_6[0],  controlCommand.GATE_CP_6[1],  controlCommand.GATE_CP_6[2], controlCommand. GATE_CP_6[3],  controlCommand.GATE_CP_6[4],  controlCommand.GATE_CP_6[5], 0, 0, 0, 0, true);

        }
    });





    Thread wanteyds=new Thread(new Runnable() {
        @Override //白卡
        public void run() {


            controlCommand.Voice("发送",3,true);
            controlCommand.comData(1, controlCommand.trends_data, controlCommand.zigbee_data_8, 0x55,0x0a,0x01,0x01,0x00,0x00,0x02,0xBB, 0, 0, true);
            //  controlCommand.comData(1,controlCommand.cxf,controlCommand.on,0,0,0,0,0,0,0,0,0,0,true);

            //       controlCommand.Voice("测距", 3, true);

            //      controlCommand.comData(1,controlCommand.cj,1,controlCommand.xsjl,controlCommand.xz_jl + 1,controlCommand.xsjl,controlCommand.smg_jl + 1,0,0,0,0,0,0,true);

            //  controlCommand.comData(1,controlCommand.track,controlCommand.p_x,controlCommand.pt_go,20,controlCommand.track,controlCommand.x,controlCommand.right,controlCommand.p_z,controlCommand.track,controlCommand.bk_xj,0,0,true);
        }
    });

    Thread soosi=new Thread(new Runnable() {
        @Override
        public void run() {
            controlCommand.Voice("红绿灯", 3, true);
            controlCommand.yanchi(2000);
            controlCommand.C_ComData(controlCommand.CC_ORDER, controlCommand.qh_AB, controlCommand.bzw_A, controlCommand.jtd_open, 0, true);
            controlCommand.yanchi(1000);
            int num1 = Traffic.Traffic_Out();  //交通灯结果
            controlCommand.C_ComData(controlCommand.CC_ORDER, controlCommand.qh_AB, controlCommand.bzw_A, controlCommand.jtd_jg, num1, true);
//            controlCommand.comData(1,controlCommand.qh_AB,controlCommand.bzw_A,controlCommand.jtd_open,0,0,0,0,0,0,0,0,0,true);
//            int num1 = Traffic.Traffic_Out();  //交通灯结果
//            controlCommand.yanchi(4000);
//            controlCommand.comData(1, controlCommand.qh_AB, controlCommand.bzw_A, controlCommand.jtd_jg, num1, controlCommand.track,controlCommand.x, 0, 0, 0, 0, 0, 0, true);

        }
    });




    private void AutoDialog(View view) {

        builder = new AlertDialog.Builder(getContext());//使用  getContext
        builder.setIcon(R.mipmap.ckback);
        builder.setTitle("全自动");
        /**
         * 设置内容区域为单选列表项
         */
        final String[] items = {"0:", "1:", "2:", "3:"};
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            //checkeditem     -1   代表默认不选中任何选项
            public void onClick(DialogInterface dialogInterface, int i) {

                switch (i) {
                    case 0:
                        Snackbar.make(getView(), "0", Snackbar.LENGTH_SHORT).show();
                        operthread.start();

                        dialogInterface.cancel();
                        break;
                    case 1:
                        Snackbar.make(getView(), "1", Snackbar.LENGTH_SHORT).show();
                        idthread.start();
                        dialogInterface.cancel();
                        break;
                    case 2:
                        Snackbar.make(getView(), "2", Snackbar.LENGTH_SHORT).show();
                        Tthread.start();
                        dialogInterface.cancel();
                        break;
                    case 3:
                        Snackbar.make(getView(), "3", Snackbar.LENGTH_SHORT).show();
                        watthread.start();
                        dialogInterface.cancel();
                        break;
                }
            }
        });
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * 设置内容区域为单选列表项
     */
    //转向灯对话框及监听事件
    private void LightDialog(View view) {

        builder = new AlertDialog.Builder(getContext());//使用  getContext
        builder.setIcon(R.mipmap.ckback);
        builder.setTitle("转向灯设置");

        final String[] items = {"左亮", "右亮", "全亮", "全灭"};
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Toast.makeText(getContext(), "You clicked "+items[i], Toast.LENGTH_SHORT).show();

                switch (i) {
                    case 0:
                        if (controlCommand.z_c_flag == 0) {

                            Snackbar.make(getView(), "左亮", Snackbar.LENGTH_SHORT).show();
                            controlCommand.comDataTest(1, controlCommand.zxd, 0x10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                        } else if (controlCommand.z_c_flag == 1) {

                            Snackbar.make(getView(), "从车左亮", Snackbar.LENGTH_SHORT).show();
                            controlCommand.C_ComData(controlCommand.CC_ORDER, controlCommand.zxd, 0x10, 0, 0, false);
                        }
                        dialogInterface.cancel();
                        break;
                    case 1:
                        if (controlCommand.z_c_flag == 0) {

                            Snackbar.make(getView(), "右亮", Snackbar.LENGTH_SHORT).show();
                            controlCommand.comDataTest(1, controlCommand.zxd, 0x01, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                        } else if (controlCommand.z_c_flag == 1) {
                            Snackbar.make(getView(), "从车右亮", Snackbar.LENGTH_SHORT).show();
                            controlCommand.C_ComData(controlCommand.CC_ORDER, controlCommand.zxd, 0x01, 0, 0, false);
                        }
                        dialogInterface.cancel();
                        break;
                    case 2:
                        if (controlCommand.z_c_flag == 0) {

                            Snackbar.make(getView(), "全亮", Snackbar.LENGTH_SHORT).show();
                            controlCommand.comDataTest(1, controlCommand.zxd, 0x11, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                        } else if (controlCommand.z_c_flag == 1) {

                            Snackbar.make(getView(), "从车全亮", Snackbar.LENGTH_SHORT).show();
                            controlCommand.C_ComData(controlCommand.CC_ORDER, controlCommand.zxd, 0x11, 0, 0, false);
                        }
                        dialogInterface.cancel();
                        break;
                    case 3:
                        if (controlCommand.z_c_flag == 0) {

                            Snackbar.make(getView(), "全灭", Snackbar.LENGTH_SHORT).show();
                            controlCommand.comDataTest(1, controlCommand.zxd, 0x00, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

                        } else if (controlCommand.z_c_flag == 1) {


                            Snackbar.make(getView(), "从车全灭", Snackbar.LENGTH_SHORT).show();
                            controlCommand.C_ComData(controlCommand.CC_ORDER, controlCommand.zxd, 0x00, 0, 0, false);
                        }
                        dialogInterface.cancel();
                        break;
                }
            }
        });
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //摄像头预设位对话框及监听事件
    private void CameraControl(View view) {
        builder = new AlertDialog.Builder(getContext());//使用  getContext
        builder.setIcon(R.mipmap.ckback);
        builder.setTitle("摄像头预设位");
        /**
         * 设置内容区域为单选列表项
         */
        final String[] items = {"预设位1", "预设位2", "预设位3","预设位4", "预设位5", "预设位6", "调用位1", "调用位2", "调用位3", "调用位4", "调用位5", "调用位6", "复位","复位设置",  "快速/慢速"};//,"复位状态设置"
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i)
                {
                    //预设位
                    case 0:
                        mainactivity.state_camera = 1;
                        break;
                    case 1:
                        mainactivity.state_camera = 2;
                        break;
                    case 2:
                        mainactivity.state_camera = 3;
                        break;
                    case 3:
                        mainactivity.state_camera = 4;
                        break;
                    case 4:
                        mainactivity.state_camera = 9;
                        break;
                    case 5:
                        mainactivity.state_camera = 10;
                        break;
                    //调用位
                    case 6:
                        mainactivity.state_camera = 11;
                        break;
                    case 7:
                        mainactivity.state_camera = 12;
                        break;
                    case 8:
                        mainactivity.state_camera = 13;
                        break;
                    case 9:
                        mainactivity.state_camera = 14;
                        break;
                    case 10:
                        mainactivity.state_camera = 15;
                        break;
                    case 11:
                        mainactivity.state_camera = 16;
                        break;
                    case 12:
                        mainactivity.state_camera = 17;//调用居中
                        break;
                    case 13:
                        mainactivity.state_camera = 18;//设置居中
                        break;
                    case 14:
                        mainactivity.camera_mode = ~mainactivity.camera_mode;
                        if (mainactivity.camera_mode == 0) {
                            Snackbar.make(getView(), "快速模式", Snackbar.LENGTH_SHORT).show();

                        } else {
                            Snackbar.make(getView(), "慢速模式", Snackbar.LENGTH_SHORT).show();
                        }
                }
                dialogInterface.cancel();
            }
        });

        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
