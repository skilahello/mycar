package com.example.newcar2022;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

public class Zigbee_Activity extends Fragment implements View.OnClickListener{
    private ControlCommand controlCommand;
    private EditText xzled_text,dimming_text,infrared04_text,infrared06_text;
    private Button dimming_button, dimminggain_button,distance_button,alertor_button,
            infrared04_button,infrared06_button,road_button,lcdup_button,lcddown_button,
            lcddt_button,xzcp_button,car_smg_btn;
    private Vibrator vibrator;//震动
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View infrared_view = inflater.inflate(R.layout.activity_infrared, null);
        controlCommand = new ControlCommand();
        Context context = this.getContext();
        vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);//获取震动服

        /*
        为控件绑定ID
        * */
        xzled_text = (EditText)infrared_view.findViewById(R.id.xzled_text);         //旋转LED文本框
        dimming_text = (EditText)infrared_view.findViewById(R.id.tg_text) ;         //调光文本框
        infrared04_text = (EditText)infrared_view.findViewById(R.id.swdt_text);     //四位动态红外数据
        infrared06_text = (EditText)infrared_view.findViewById(R.id.lwdt_text);     //六位动态红外数据

        dimming_button = (Button) infrared_view.findViewById(R.id.tg_button);       //调光按键
        dimminggain_button = (Button) infrared_view.findViewById(R.id.tghq_button); //调光获取按键
        distance_button = (Button) infrared_view.findViewById(R.id.bjq_button);     //报警器按键
        alertor_button = (Button) infrared_view.findViewById(R.id.xzledjl_button);  //距离按键
        road_button = (Button) infrared_view.findViewById(R.id.xzledlk_button);     //路况按键
        xzcp_button = (Button) infrared_view.findViewById(R.id.cpxs_button);        //旋转车牌显示
        lcddt_button = (Button) infrared_view.findViewById(R.id.dtxs_button);       //旋转LED动态显示
        infrared04_button = (Button)infrared_view.findViewById(R.id.swdt_button) ;  //四位动态红外数据按键
        infrared06_button = (Button)infrared_view.findViewById(R.id.lwdt_button) ;  //六位动态红外数据按键
        lcdup_button = (Button)infrared_view.findViewById(R.id.lcdup_button);       //LCD上翻
        lcddown_button = (Button)infrared_view.findViewById(R.id.lcddown_button);   //LCD下翻
        car_smg_btn=infrared_view.findViewById(R.id.car_smg_button);//  小车任务板 数码管显示 数据

        /*
         * 绑定监听器
         * */
        dimming_button.setOnClickListener(this);                    //调光按键监听器
        dimminggain_button.setOnClickListener(this);                //调光获取按键监听器
        distance_button.setOnClickListener(this);                   //报警器按键监听器
        alertor_button.setOnClickListener(this);                    //距离按键监听器
        road_button.setOnClickListener(this);                       //路况按键监听器
        infrared04_button.setOnClickListener(this);                 //四位动态红外数据按键监听器
        infrared06_button.setOnClickListener(this);                 //六位动态红外数据按键监听器
        lcddt_button.setOnClickListener(this);                      //旋转LED动态显示
        xzcp_button.setOnClickListener(this);                       //旋转车牌显示
        lcddown_button.setOnClickListener(this);                    //LCD下翻监听器
        lcdup_button.setOnClickListener(this);                      //LCD上翻监听器
        car_smg_btn.setOnClickListener(this);
        // RadioGroup Infrared_RadioGroup = (RadioGroup)infrared_view.findViewById(R.id.Infrared_RadioGroup);
        //Infrared_RadioGroup.setOnCheckedChangeListener(this);                                               //找到单选按键组并绑定监听器
        return infrared_view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tg_button:
                Log.e( "调光: ","调光默认" );
                try {
                    vibrator.vibrate(50);
                    int num = Integer.parseInt(dimming_text.getText().toString());
                    if( controlCommand.z_c_flag == 1){
                        controlCommand.C_ComData(controlCommand.CC_ORDER,controlCommand.tg,4 + num,0,0,false);
                    }else if( controlCommand.z_c_flag == 0){
                        controlCommand.comDataTest(1, controlCommand.tg, 4 + num, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                    }
                    int tg = num;
                    Toast.makeText(getContext(),"调节光照档位为"+tg+"档",Toast.LENGTH_SHORT).show();
                }
                catch (Exception e) {
                    // TODO: handle exception
                    vibrator.vibrate(500);
                    Toast.makeText(getContext(),"请输入光照档位",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tghq_button:
                vibrator.vibrate(50);
                Log.e( "调光: ","调光获取" );
                Snackbar.make(v, "调光获取", Snackbar.LENGTH_SHORT).show();
                if( controlCommand.z_c_flag == 1){
                    controlCommand.C_ComData(controlCommand.CC_ORDER,controlCommand.tg_gz_get,0,0,0,false);
                }else if( controlCommand.z_c_flag == 0){
                    controlCommand.comDataTest(1, controlCommand.tg_gz_get, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                }
                break;
            case R.id.xzledjl_button:
                Snackbar.make(v, "距离", Snackbar.LENGTH_SHORT).show();
                vibrator.vibrate(50);
                Log.e( "距离: ","距离" );
                if(controlCommand.z_c_flag == 1){
                    controlCommand.C_ComData(controlCommand.CC_ORDER,controlCommand.cj, 1,0,0,false);
                }else if(controlCommand.z_c_flag == 0){
                    controlCommand.comDataTest(1, controlCommand.cj, 1, controlCommand.xsjl, controlCommand.xz_jl+1, 0, 0, 0, 0, 0, 0, 0, 0);
                }
                break;
            case R.id.xzledlk_button:
                vibrator.vibrate(50);
                Snackbar.make(v, "路况", Snackbar.LENGTH_SHORT).show();
                Log.e( "路况: ","路况" );
                if(controlCommand.z_c_flag == 1){
                    controlCommand.C_ComData(controlCommand.CC_ORDER,controlCommand.xz_tx, controlCommand.lk1,0,0,false);
                }else if(controlCommand.z_c_flag == 0){
                    controlCommand.comDataTest(1, controlCommand.xz_tx, controlCommand.lk1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                }
                break;
            case R.id.bjq_button:
                vibrator.vibrate(50);
                Snackbar.make(v, "报警器", Snackbar.LENGTH_SHORT).show();
                Log.e( "报警器: ","报警器" );
                if(controlCommand.z_c_flag == 1){
                    controlCommand.C_ComData(controlCommand.CC_ORDER,controlCommand.beeper, 1,0,0,false);
                }else if(controlCommand.z_c_flag == 0){
                    controlCommand.comDataTest(1, controlCommand.beeper, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                }
                break;
            case R.id.swdt_button:
                vibrator.vibrate(50);
                Snackbar.make(v, "四位动态红外数据", Snackbar.LENGTH_SHORT).show();
                Log.e( "四位动态红外数据: ","四位动态红外数据" );
                try {
                    int hwdt_num1 = Integer.valueOf(infrared04_text.getText().toString().substring(0,2),16);
                    int hwdt_num2 = Integer.valueOf(infrared04_text.getText().toString().substring(2,4),16);
                    int hwdt_num3 = Integer.valueOf(infrared04_text.getText().toString().substring(4,6),16);
                    int hwdt_num4 = Integer.valueOf(infrared04_text.getText().toString().substring(6,8),16);
                    if(controlCommand.z_c_flag == 1){
                        controlCommand.comDataTest(1, controlCommand.trends_data, controlCommand.c_hw_data_4, hwdt_num1, hwdt_num2, hwdt_num3, hwdt_num4,0, 0, 0, 0, 0, 0);
                    }else if(controlCommand.z_c_flag == 0){
                        controlCommand.comDataTest(1, controlCommand.trends_data, controlCommand.hw_data_4, hwdt_num1, hwdt_num2, hwdt_num3, hwdt_num4,0, 0, 0, 0, 0, 0);
                    }

                } catch (Exception e) {
                    // TODO: handle exception

                }
                break;
            case R.id.lwdt_button:
                vibrator.vibrate(50);
                Snackbar.make(v, "六位动态红外数据", Snackbar.LENGTH_SHORT).show();
                Log.e( "六位动态红外数据: ","六位动态红外数据" );
                try {
                    int hwdt_num1 = Integer.valueOf(infrared06_text.getText().toString().substring(0,2),16);
                    int hwdt_num2 = Integer.valueOf(infrared06_text.getText().toString().substring(2,4),16);
                    int hwdt_num3 = Integer.valueOf(infrared06_text.getText().toString().substring(4,6),16);
                    int hwdt_num4 = Integer.valueOf(infrared06_text.getText().toString().substring(6,8),16);
                    int hwdt_num5 = Integer.valueOf(infrared06_text.getText().toString().substring(8,10),16);
                    int hwdt_num6 = Integer.valueOf(infrared06_text.getText().toString().substring(10,12),16);
                    if(controlCommand.z_c_flag == 1){
                        controlCommand.comDataTest(1, controlCommand.trends_data, controlCommand.c_hw_data_6, hwdt_num1, hwdt_num2, hwdt_num3, hwdt_num4,hwdt_num5, hwdt_num6, 0, 0, 0, 0);
                    }
                    else if(controlCommand.z_c_flag == 0){
                        controlCommand.comDataTest(1, controlCommand.trends_data, controlCommand.hw_data_6, hwdt_num1, hwdt_num2, hwdt_num3, hwdt_num4,hwdt_num5, hwdt_num6, 0, 0, 0, 0);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
                break;
            case R.id.dtxs_button:
                vibrator.vibrate(50);
                Snackbar.make(v, "旋转LED动态显示", Snackbar.LENGTH_SHORT).show();
                Log.e( "旋转LED动态显示: ","旋转LED动态显示" );
                try {
                    int data = Integer.parseInt(xzled_text.getText().toString());
//                    controlCommand.comDataTest(1, controlCommand.tg, 4 + num, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
//                    Toast.makeText(getContext(),"调节光照档位为"+tg+"档",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    // TODO: handle exception
                    Log.e( "旋转LED动态显示: ","旋转LED动态显示" );
                }
                break;
            case R.id.cpxs_button:
                vibrator.vibrate(50);
                Snackbar.make(v, "显示车牌", Snackbar.LENGTH_SHORT).show();
                Log.e( "车牌: ","车牌默认" );
                try {
                    String str_cp = (xzled_text.getText()).toString();
                    char[] cp="A53554".toCharArray();
                    if(controlCommand.z_c_flag == 1){
                        controlCommand.comDataTest(1, controlCommand.trends_data, controlCommand.c_xz_cp, cp[0], cp[1], cp[2], cp[3], cp[4],cp[5], 0, 0, 0, 0);
                    }else if(controlCommand.z_c_flag == 0){
                        controlCommand.comDataTest(1, controlCommand.trends_data, controlCommand.xz_cp, cp[0], cp[1], cp[2], cp[3], cp[4],cp[5], 0, 0, 0, 0);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
                break;
//            case R.id.lcdup_button:
//                vibrator.vibrate(50);
//                Log.e( "LCD: ","上翻" );
//                Snackbar.make(v, "上翻", Snackbar.LENGTH_SHORT).show();
//                controlCommand.comDataTest(1, controlCommand.lcd_fy, controlCommand.u_f+1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
//                break;
//            case R.id.lcddown_button:
//                vibrator.vibrate(50);
//                Log.e( "LCD:", "" );
//                Snackbar.make(v, "下翻", Snackbar.LENGTH_SHORT).show();
//                controlCommand.comDataTest(1, controlCommand.lcd_fy, controlCommand.d_f+1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
//                break;
            case R.id.car_smg_button:
                vibrator.vibrate(50);
                Log.e( "LCD:", "" );
                Snackbar.make(v, "任务板数码管显示数据", Snackbar.LENGTH_SHORT).show();
                String car_smg_text =infrared06_text.getText().toString();
                int data=Integer.valueOf(car_smg_text.substring(0,2),16);//  将字符转换成16
                controlCommand.comDataTest(1, controlCommand.car_smg, data, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                break;
        }
    }




//    @Override
//    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//
//            switch (checkedId){
//                case  R.id.xzhq_button :
//                    Log.e( "形状: ","形状获取" );
//                    break;
//                case  R.id.xzmr_button :
//                    Log.e( "形状: ","形状默认" );
//                    controlCommand.comDataTest(1, controlCommand.xz_tx, controlCommand.tx, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
//                    break;
//                case  R.id.yshq_button :
//                    Log.e( "颜色: ","颜色获取" );
//                    break;
//                case  R.id.ysmr_button :
//                    Log.e( "颜色: ","颜色默认" );
//                    controlCommand.comDataTest(1, controlCommand.xz_tx, controlCommand.ys, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
//                    break;
//                case  R.id.cphq_button :
//                    Log.e( "车牌: ","车牌获取" );
////                    char[] cp=xzled_text.getText().toString().toCharArray();
////                    controlCommand.comDataTest(1, controlCommand.trends_data, controlCommand.tft_cp_6, cp[0], cp[1], cp[2], cp[3], cp[4],cp[5], 0, 0, 0, 0);
//                    break;
//                case  R.id.cpmr_button :
//                    Log.e( "车牌: ","车牌默认" );
//                    xzled_text.setText("A53554");
//                    char[] cp="A53554".toCharArray();
//                    controlCommand.comDataTest(1, controlCommand.trends_data, controlCommand.xz_cp, cp[0], cp[1], cp[2], cp[3], cp[4],cp[5], 0, 0, 0, 0);
//                    break;
//            }
//
//    }

}
