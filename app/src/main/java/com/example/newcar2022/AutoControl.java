package com.example.newcar2022;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Vibrator;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;


public class AutoControl extends Fragment implements View.OnClickListener {
    //坐标按键
    @Nullable
    private ControlCommand controlCommand;
    private EditText et_zbtext;//显示坐标文本框
    public String auto_text_5;//坐标字符缓冲
    public int mode_flag = 1;//  全自动模式  切换标志位 默认为1   初始化模式
    public int route_flag = 0;//0为  先走   字母  1  为先走数字
    public int routedesignFlag = 0;//  路线设计0 关闭
    public String route_text_new = "";//   路线设计保存的文本
    private Vibrator vibrator;//震动
    private AlertDialog.Builder builder;  //声明对话框对象

    /*
    ；路线设计   文本框   //   路线设计  文本变量
     */

    private EditText route_design_et;//
    public String route_text = "";//路线变量
    /*

    路线选择按钮
     */
    private ToggleButton routr_button;
    /*
    地图坐标
     */
    private Button zb_f7;
    private Button zb_d7;
    private Button zb_b7;

    private Button zb_g6;
    private Button zb_f6;
    private Button zb_d6;
    private Button zb_b6;
    private Button zb_a6;

    private Button zb_g4;
    private Button zb_f4;
    private Button zb_d4;
    private Button zb_b4;
    private Button zb_a4;

    private Button zb_g2;
    private Button zb_f2;
    private Button zb_d2;
    private Button zb_b2;
    private Button zb_a2;

    private Button zb_f1;
    private Button zb_d1;
    private Button zb_b1;

    /*
    车头方向
     */
    private Button car_up;
    private Button car_down;
    private Button car_left;
    private Button car_right;

    /*
    自动行走控制
     */
    private Button auto_start_5;
    private Button auto_cler;

    /*
       坐标  初始化  及 模式 选择  button

     */
    private Button auto_mode;//全自动模式切换

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.auto_activity, container, false);
        Context context = this.getContext();
        controlCommand = new ControlCommand();
        vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);//获取震动服务

        auto_mode = (Button) view.findViewById(R.id.auto_mode);//全自动模式切换
        routr_button = (ToggleButton) view.findViewById(R.id.route_select);

        et_zbtext = (EditText) view.findViewById(R.id.et_zb_text);//坐标显示  文本框
        route_design_et = (EditText) view.findViewById(R.id.route_design_et);//路线设计文本框
        zb_f7 = (Button) view.findViewById(R.id.f7_button);
        zb_d7 = (Button) view.findViewById(R.id.d7_button);
        zb_b7 = (Button) view.findViewById(R.id.b7_button);

        zb_g6 = (Button) view.findViewById(R.id.g6_button);
        zb_f6 = (Button) view.findViewById(R.id.f6_button);
        zb_d6 = (Button) view.findViewById(R.id.d6_button);
        zb_b6 = (Button) view.findViewById(R.id.b6_button);
        zb_a6 = (Button) view.findViewById(R.id.a6_button);

        zb_g4 = (Button) view.findViewById(R.id.g4_button);
        zb_f4 = (Button) view.findViewById(R.id.f4_button);
        zb_d4 = (Button) view.findViewById(R.id.d4_button);
        zb_b4 = (Button) view.findViewById(R.id.b4_button);
        zb_a4 = (Button) view.findViewById(R.id.a4_button);

        zb_g2 = (Button) view.findViewById(R.id.g2_button);
        zb_f2 = (Button) view.findViewById(R.id.f2_button);
        zb_d2 = (Button) view.findViewById(R.id.d2_button);
        zb_b2 = (Button) view.findViewById(R.id.b2_button);
        zb_a2 = (Button) view.findViewById(R.id.a2_button);

        zb_f1 = (Button) view.findViewById(R.id.f1_button);
        zb_d1 = (Button) view.findViewById(R.id.d1_button);
        zb_b1 = (Button) view.findViewById(R.id.b1_button);

        car_up = (Button) view.findViewById(R.id.car_up);
        car_down = (Button) view.findViewById(R.id.car_down);
        car_left = (Button) view.findViewById(R.id.car_left);
        car_right = (Button) view.findViewById(R.id.car_right);

        auto_start_5 = (Button) view.findViewById(R.id.auto_start_5); //开始自动行走
        auto_cler = (Button) view.findViewById(R.id.et_auto_cler);    //清空文本框

        auto_mode.setOnClickListener(this); //模式切换

        zb_f7.setOnClickListener(this);
        zb_d7.setOnClickListener(this);
        zb_b7.setOnClickListener(this);

        zb_g6.setOnClickListener(this);
        zb_f6.setOnClickListener(this);
        zb_d6.setOnClickListener(this);
        zb_b6.setOnClickListener(this);
        zb_a6.setOnClickListener(this);

        zb_g4.setOnClickListener(this);
        zb_f4.setOnClickListener(this);
        zb_d4.setOnClickListener(this);
        zb_b4.setOnClickListener(this);
        zb_a4.setOnClickListener(this);

        zb_g2.setOnClickListener(this);
        zb_f2.setOnClickListener(this);
        zb_d2.setOnClickListener(this);
        zb_b2.setOnClickListener(this);
        zb_a2.setOnClickListener(this);

        zb_f1.setOnClickListener(this);
        zb_d1.setOnClickListener(this);
        zb_b1.setOnClickListener(this);

        car_up.setOnClickListener(this);
        car_down.setOnClickListener(this);
        car_left.setOnClickListener(this);
        car_right.setOnClickListener(this);


        auto_start_5.setOnClickListener(this);
        auto_cler.setOnClickListener(this);//清空
        route_button_inti();//

        return view;
    }


    public void route_button_inti()//开关监听初始化
    {
        //路线选择
        routr_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                route_select(b);
            }
        });
    }


    public void route_select(boolean state) {
        ToggleButton button = (ToggleButton) getView().findViewById(R.id.route_select);
        button.setChecked(state);
        //判断   state 的两种状态
        if (state) {
            route_flag = 1;//横
            Snackbar.make(getView(), "数字", Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(getView(), "字母", Snackbar.LENGTH_SHORT).show();
            route_flag = 0;//竖
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.f7_button:
            case R.id.d7_button:
            case R.id.b7_button:

            case R.id.g6_button:
            case R.id.f6_button:
            case R.id.d6_button:
            case R.id.b6_button:
            case R.id.a6_button:

            case R.id.g4_button:
            case R.id.f4_button:
            case R.id.d4_button:
            case R.id.b4_button:
            case R.id.a4_button:

            case R.id.g2_button:
            case R.id.f2_button:
            case R.id.d2_button:
            case R.id.b2_button:
            case R.id.a2_button:

            case R.id.f1_button:
            case R.id.d1_button:
            case R.id.b1_button:

                vibrator.vibrate(50);
                if (mode_flag == 1 || mode_flag == 2) {
                    auto_text_5 = et_zbtext.getText().toString();
                    et_zbtext.setText(auto_text_5 + ((Button) view).getText());  //获得文本   显示到文本框
                } else if (mode_flag == 3)//路线设计  模式
                {
                    if (route_flag == 0)//判断 路线选择标志位
                    {
                        route_text = route_design_et.getText().toString();
                        route_design_et.setText(route_text + ((Button) view).getText()); //获得文本   显示到文本框
                    } else if (route_flag == 1) {
                        route_text = route_design_et.getText().toString();
                        route_design_et.setText(route_text + ((Button) view).getText()); //获得文本   显示到文本框
                    }
                } else if (mode_flag == 4)//指定路线
                {
                    if (route_flag == 0)//判断 路线选择标志位
                    {
                        route_text = route_design_et.getText().toString();
                        route_design_et.setText(route_text + ((Button) view).getText()); //获得文本   显示到文本框
                    } else if (route_flag == 1) {
                        route_text = route_design_et.getText().toString();
                        route_design_et.setText(route_text + ((Button) view).getText()); //获得文本   显示到文本框
                    }

                }
                break;
            case R.id.car_up:
            case R.id.car_down:
            case R.id.car_left:
            case R.id.car_right:
                vibrator.vibrate(50);
                if (mode_flag == 1 || mode_flag == 2) {
                    auto_text_5 = et_zbtext.getText().toString();
                    et_zbtext.setText(auto_text_5 + ((Button) view).getText());//获得文本   显示到文本框
                } else if (mode_flag == 3)//路线设计  模式
                {
                    if (route_flag == 0)//判断 路线选择标志位
                    {
                        route_text = route_design_et.getText().toString();
                        route_design_et.setText(route_text + ((Button) view).getText() + "0 ");//获得文本   显示到文本框
                    } else if (route_flag == 1) {
                        route_text = route_design_et.getText().toString();
                        route_design_et.setText(route_text + ((Button) view).getText() + "1 ");//获得文本   显示到文本框
                    }
                } else if (mode_flag == 4)//指定路线
                {
                    if (route_flag == 0)//判断 路线选择标志位
                    {
                        route_text = route_design_et.getText().toString();
                        route_design_et.setText(route_text + ((Button) view).getText() + "0 ");//获得文本   显示到文本框
                    } else if (route_flag == 1) {
                        route_text = route_design_et.getText().toString();
                        route_design_et.setText(route_text + ((Button) view).getText() + "1 ");//获得文本   显示到文本框
                    }
                }
                break;
            case R.id.et_auto_cler:
                et_zbtext.setText("");
                route_design_et.setText("");
                vibrator.vibrate(50);
                break;
            case R.id.auto_mode:
                auto_mode(getView());
                break;
            case R.id.auto_start_5://   ||OK 按键      ok
                vibrator.vibrate(50);
                if (mode_flag == 1)//坐标初始化  模式
                {
                    try {
                        String zb_text = et_zbtext.getText().toString();//获取文本框   内容
                        if (controlCommand.z_c_flag == 1) {
                            controlCommand.C_Z_B_Init(zb_text, false);
                        } else if (controlCommand.z_c_flag == 0) {
                            controlCommand.Z_B_Init(zb_text, false);
                        }
                        Snackbar.make(view, "坐标初始化为" + zb_text, Snackbar.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        // TODO: handle exception
                        Snackbar.make(view, "输入的坐标不正确", Snackbar.LENGTH_SHORT).show();
                    }
                    et_zbtext.setText("");
                    mode_flag = 2;//初始化坐标后自动跳转为 自动行走模式
                } else if (mode_flag == 2)//  自动行走模式
                {
                    String zb_text = "";//坐标变量
                    if (route_flag == 0) {
                        zb_text = et_zbtext.getText() + "0".toString();//获取文本框   横着  先走字母
                    } else if (route_flag == 1) {
                        zb_text = et_zbtext.getText() + "1".toString();//获取文本框   竖着走 先走数字
                    }
                    try {
                        if (controlCommand.z_c_flag == 0) {
                            controlCommand.Auto_Run(zb_text, false);
                        } else if (controlCommand.z_c_flag == 1) {
                            controlCommand.C_Auto_Run(zb_text, false);
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        Snackbar.make(view, "输入的坐标不正确", Snackbar.LENGTH_SHORT).show();
                        et_zbtext.setText("");
                    }
                    et_zbtext.setText("");
                } else if (mode_flag == 3)// 路线设计 模式
                {
                    route_text_new = route_design_et.getText().toString();
//                    route_design_et.setText("");
//                    routedesignFlag=1;//  开启


                    //    Log.e("启动路线设计线程", "启动路线设计线程");
                    String route[] = route_text_new.split(" ");

                    // Toast.makeText(getContext(),"共"+route.length+"条路线", Toast.LENGTH_SHORT).show();
                    try {
                        for (int i = 0; i < route.length; i++) {

                            //  Toast.makeText(getContext(),"第"+s+"条路线", Toast.LENGTH_SHORT).show();
                            if (controlCommand.z_c_flag == 0) {
                                controlCommand.Auto_Run(route[i], true);
                            } else if (controlCommand.z_c_flag == 1) {
                                controlCommand.C_Auto_Run(route[i], true);
                            }
                        }
                    } catch (Exception e) {
                        // TODO: handle exception

                        // route_design_et.setText("");
                    }


                } else if (mode_flag == 4)// 指定路线模式
                {

                    try {

                    } catch (Exception e) {
                        // TODO: handle exception
                        Snackbar.make(view, "输入的坐标不正确", Snackbar.LENGTH_SHORT).show();
                        et_zbtext.setText("");
                    }
                }
                break;
        }
    }

    private void auto_mode(View view) {
        builder = new AlertDialog.Builder(getContext());//使用  getContext
        //   builder.setIcon(R.mipmap.ic_launcher);
        // builder.setTitle("模式选择");

        /**
         * 设置内容区域为单选列表项
         */
        final String[] items = {"初始化", "自动行走", "路线设计", "指定路线"};
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        mode_flag = 1;//  初始化  模式
                        Snackbar.make(getView(), "初始化模式", Snackbar.LENGTH_SHORT).show();
                        break;
                    case 1:
                        mode_flag = 2;//自动行走模式
                        Snackbar.make(getView(), "自动行走模式", Snackbar.LENGTH_SHORT).show();
                        break;
                    case 2:
                        mode_flag = 3;  //  路线设计模
                        Snackbar.make(getView(), "路线设计模式", Snackbar.LENGTH_SHORT).show();
                        break;
                    case 3:
                        mode_flag = 4;  //  指定路线
                        Snackbar.make(getView(), "指定路线", Snackbar.LENGTH_SHORT).show();
                        break;
                }
                dialogInterface.cancel();
            }
        });
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public Thread RouteDesign = new Thread(new Runnable() {//这个线程现在还未 启动 启动已被屏蔽
        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (true) {
                if (routedesignFlag == 1)

                {
                    Log.e("启动路线设计线程", "启动路线设计线程");
                    String route[] = route_text_new.split(" ");
                    int i = 0;
                    // Toast.makeText(getContext(),"共"+route.length+"条路线", Toast.LENGTH_SHORT).show();
                    try {
                        for (; i < route.length; i++) {
                            int s = i + 1;
                            //  Toast.makeText(getContext(),"第"+s+"条路线", Toast.LENGTH_SHORT).show();
                            if (controlCommand.z_c_flag == 0) {
                                controlCommand.Auto_Run(route[i], true);
                            } else if (controlCommand.z_c_flag == 1) {
                                controlCommand.C_Auto_Run(route[i], true);
                            }
                        }
                    } catch (Exception e) {
                        // TODO: handle exception

                        // route_design_et.setText("");
                    }

                    routedesignFlag = 0;
                }

                //route_design_et.setText("");
            }
        }
    });


}