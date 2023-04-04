package com.example.newcar2022;

import static com.example.newcar2022.QR_Code.Qrcode;

import android.graphics.Bitmap;
import android.os.Looper;
import android.os.Vibrator;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class ControlCommand {
    public static final int tx_classification_num = 9;
    public static final int software_res = 0x69;   //软件复位
    /*----------------------------行进控制-------------------------------*/

    //第二参数码盘值
    public static final int go = 0x01;   //厘米前进
    public static final int pt_go = 0xfe;   //码盘前进
    public static final int back = 0x02; //厘米后退
    public static final int pt_back = 0xfd; //码盘后退
    /*************转弯************/
    //第二参数填写码盘值时为只转动码盘值距离
    public static final int left = 0x04; // 左转
    public static final int right = 0x05; // 右转
    //第二参数（大于2以后为码盘值）
    public static final int p_z = 0x00;  //普通转弯
    public static final int dt = 0x01;   // 掉头
    public static final int bz = 0x02;   //半转
    public static final int mp_z = 0x00;//码盘值转
    /*************END************/

    /********循迹控制************/
    public static final int track = 0x03;  //循迹（第二参数大于10时位前进循迹）

    // 循迹第二参数
    public static final int x = 0x00;     //普通循迹
    public static final int xl = 0x01;    //循迹左转
    public static final int xr = 0x02;    //循迹右转
    public static final int lx = 0x03;    //左转循迹
    public static final int rx = 0x04;    //右转循迹
    public static final int l_x = 0x05;   //连续循迹
    public static final int l_xl = 0x06;  //连续循迹左转
    public static final int l_xr = 0x07;  //连续循迹右转
    public static final int b_x = 0x08;   //后退循迹
    public static final int p_x = 0x09;   //单独循迹（没有前进一小步）
    public static final int bk_xj=0x11   ;
    public static final int time_x = 0x10;  //时间循迹 时间固定700ms
    /**************END************/

    /*----------------------------------END-----------------------------------*/

    public static final int on = 0x01;   //开
    public static final int off = 0x00;  //关
    public static final int no = 0x00;   //空


    /*******红外 *******/
    public static final int beeper = 0x06; //报警器
    public static final int tg = 0x08; // 调光（第二参数大于4之后为自动调光）
    public static final int tg_gz_get = 0xc8; //获取当前 （第二参数空）

    public static final int c_gz = 0;  //从车调光当前挡位
    public static final int z_gz = 0;  //主车调光当前挡位

    public static final int zxd = 0x19;  //转向灯控制(第二参数高位左灯控制，低位右灯控制)
    public static final int buzz = 0x20; //蜂鸣器
    public static final int car_smg = 0xb1; //任务板 数码管显示数据 第二参数 为 十六进制格式 显示 数据 12   需要发送 0x12 d4需要 发送 0xd4
    public static final int gate = 0x09; //道闸
    public static final int cxf = 0x11;  // 磁悬浮标志物控制（第二参数 1为开）
    public static final int etc = 0x15;  //ETC(第二参数为空)
    public static final int ltck_auto = 0xa3;  //自动上立体车库（第二参数为层数）
    public static final int ltck_layer = 0xa4; //设置立体车库层数（第二参数为层数）

    public static final int ltck_get_layer = 0xa5; //获取立体车库的层数


    /**************/

    /********RFID写卡*******/
    public static final int rfid_write = 0x26;     //rfid写卡
    //第二参数
    public static final int rfid_g_10 = 0x01;     //rfid高十位
    public static final int rfid_d_6 = 0x02;      //rfid低6位

    /********RFID读卡*******/
    public static final int rfid_read = 0x28;  //RFID读取
    public static final int rfid_sq = 0x21;   //RFID指定扇区（第二参数为指定的扇区，只要扇区不为空循迹过程中就会读卡）
    // public final int sq_data = new Way().rfid_sq_tq(); //rfid第二参数

    public static final int jtd_open = 0x13;   //交通灯开启指令
    public static final int jtd_jg = 0x14;    //(第二参数为1红色 2绿色 3黄色)
    //public static final int start = 0x11;     //全自动开始的成套指令// 已弃用
    public static final int end = 0x0b;       //结束时的成套指令

    /********距离部分*********/
    public static final int cj = 0x0d;        //测距
    public static final int voice_jl = 0x1d; //播报距离信息
    public static final int cs_jl_data = 0x36;  //动态距离信息显示（第二参数同显示距离一样）

    public static final int c_jl = 0;          //从车距离信息
    public static final int z_jl = 0;          //主车距离信息
    public static final int xsjl = 0x2d; //显示距离

    //副指令（低4位为第几次的距离）
    public static final int smg_jl = 0x10;   //数码管显示距离
    public static final int xz_jl = 0x20;    //立体标志物显示距离
    public static final int tft_jl = 0x30;   //TFT显示距离
    public static final int jl_data_voice = 0x50; //语音播报动态距离信息
    /***********END***********/


    /*********立体标志物显示图形信息*******/
    public static final int xz_tx = 0x0e;
    //第二参数(低4位为实际参数)
    public static final int tx = 0x10;   //图形xz
    public static final int ys = 0x20;   //颜色xz
    public static final int lk1 = 0x31;  //路况   隧道有事故
    public static final int lk2 = 0x32;  //前方施工
    /*************END*************/

    ///////////////////////////////
    public static final int intest= 0xaa;                       ///////////////////////////////////

    /*********切换选择AB标志物*******/
    public static final int qh_AB = 0x07; //切换选择AB标志物
    //第二参数
    public static final int bzw_A = 0x0a;
    public static final int bzw_B = 0x0b;
    public static final int A = 0x0a;
    public static final int B = 0x0b;

    /**
     * 语音播报特定指令
     */
    public static final int voice_td_com = 0xf6;  //(第二参数高位为1语音识别，低位为播报指令)


    public static final int voice_sb = 0x1f; // 开启语音识别

    public static int z_c_flag = 0;//默认为 0  代表控制主车  主从   切换标志位//全局变量
    public static int switch_AB_flag=0;//   切换AB标志物 A = 0  B = 1
    /*********************END*******************/


    /*************图片翻页控制***********/
    public static final int tft_fy = 0x17; //TFT显示屏翻页（第二参数高位上翻或下翻，低位次数）
    public static final int tft_zd = 0x27; //TFT指定显示图片（第二参数指定图片数据）
    //第二参数
    public static final int u_f = 0x10;      //上翻   低四位为次数（需手动修改）
    public static final int d_f = 0x20;      //下翻   第四位为次数（按需修改）
    public static final int zd_fy = 0x30;    //自动翻页
    /***************END***************/


    /********计时器控制*********/
    public static final int tft_js = 0x16; //TFT显示器计时
    public static final int smg_js = 0x0a; //数码管计时
    //第二参数（TFT计时/SMG计时）
    public static final int jsk = 0x01; //开
    public static final int jsg = 0x02; //关
    public static final int jsr = 0x03; //复位
    /***********END**********/

    public static final int auto_run = 0xa2;  //自动行走指令（参数3位 字母 数字 车头朝向（高4位控制路线））
    public static final int auto_ck = 0xa1;//也可以入库提取到的坐标, 反之, 可以选择预定的入库坐标, 如果存储坐标的数组(parameter[0]==0), 就不调用预定的入库坐标, 是单独用来任务最后入库的方法, 此方法和自动行走相同
    public static final int z_b_init = 0x68;  //初始化坐标指令（参数3位 字母 数字 车头朝向）

    /***************动态数据控制************/
    //动态数据
    public static final int trends_data = 0x18;
    //第二参数
    public static final int smg_data = 0x01;    //数码管显示数据
    public static final int xz_cp = 0x02;       //旋转LED显示车牌及坐标
    public static final int tft_cp_6 = 0x03;   //TFTA显示车牌
    public static final int gate_cp_6 = 0x10;  //道闸显示车牌
    public static final int tft_data_3 = 0x05; //TFTA显示数据
    public static final int tftb_cp_6 = 0x0b;    //TFTB显示车牌
    public static final int tftb_data_3 = 0x0c;  //TFTB显示数据
    public static final int hw_data_6 = 0x06;  //红外6位数据
    public static final int hw_data_4 = 0x09;  //红外4位数据
    public static final int rfid_my = 0xf0;    //RFID密钥
    public static final int auto_rk=0xa1; //自动入库



    public static final int c_xz_cp = 0xca;  //从车旋转LED显示车牌
    public static final int c_tfta_cp = 0xcb;  //从车TFTA显示车牌  A 81 82
    public static final int c_tfta_data = 0xc1;  //从车TFTA显示数据 A 0x77
    public static final int c_dz_cp = 0xcc;  //从车道闸显示车牌
    public static final int c_wx_kq = 0xcd;  //从车无线充电开启码
    public static final int c_tftb_cp = 0xc2;    //从车TFTB显示车牌 B 83 84
    public static final int c_tftb_data = 0xc3;  //从车TFTB显示数据 B 0x78
    public static final int c_led_data = 0xc4;  //从车led显示

    public static final int c_hw_data_4 = 0xc9; //从车红外4数据
    public static final int c_hw_data_6 = 0xc6; //从车红外6数据
    public static final int zigbee_data_8 = 0x08; //主车zigbee数据
    public static final int c_zigbee_data_8 = 0x07; //从车zigbee数据

    /****************** END **************/

    /*********数据存储*******/
    public static int HW_Data_4[] = new int[4];   //红外4 (Get_HW_4())
    public static int HW_Data_6[] = new int[6];   //红外6 (Get_HW_6())
    public static int C_HW_Data_4[] = new int[4]; //从车红外4数据 （Get_C_HW_4()）
    public static int C_HW_Data_6[] = new int[6]; //从车红外6数据（Get_C_HW_6()）
    public static int TFT_CP_6[] = new int[6];    //TFT车牌数据 (Get_TFT_CP())
    public static int GATE_CP_6[] = new int[6];   //道闸显示车牌（Get_Gate_Cp()）
    public static int XZ_CP_8[] = new int[8];     //旋转LED车牌数据(Get_XZ_LED_Cp())
    public static int TFT_Data_3[] = new int[3];  //TFT数据   (Get_TFT_Data())
    public static int Zig_Data_8[] = new int[8];  //zigbee数据 (Get_Zigbee())
    public static int AUTO_Data_6[] = new int[6]; //自动行走数据 (Get_Auto_Data(1,2,3))
    public static int SMG_Data_3[] = new int[3];  //数码管显示数据 (Get_SMG_Data())
    public static int RFID_MY_6[] = new int[6];   //RFID密钥





    /***从车 扫描二维码*/
    public static final int cc_ewm = 0xce;   //从车扫二维码主车 指令
    public static final int C_cc_jtd = 0x15;   //从车交通灯从车 指令

    public static final int CC_AUTO = 0x66;   //从车自动行走
    public static final int CC_ORDER = 0x88; //从车指令控制
//    public static final int CC_HW_Q4 = 0x71; //从车红外前4（发送方法长度限制）
//    public static final int CC_HW_H2 = 0x72; //从车红外后2
//    public static final int CC_DZ_Q4 = 0x73; //从车道闸前4（发送方法长度限制）
//    public static final int CC_DZ_H2 = 0x74; //从车道闸后2
//    public static final int CC_WXKQ_Q4 = 0x75; //从车无线充电开启码前4（发送方法长度限制）
//    public static final int CC_WXKQ_H4 = 0x76; //从车无线充电开启码后4
//    public static final int CC_TFTA_DT = 0x77; //从车TFTA动态显示
//    public static final int CC_TFTB_DT = 0x78; //从车TFTB动态显示
//    public static final int CC_LED_S = 0x79;    //从车LED第一行显示
//    public static final int CC_LED_X = 0x80;    //从车LED第二行显示
//    public static final int CC_TFTA_CP_Q4 = 0x81; //从车TFTA车牌前四
//    public static final int CC_TFTA_CP_H2 = 0x82; //从车TFTA车牌后二
//    public static final int CC_TFTB_CP_Q4 = 0x83; //从车TFTB车牌前四
//    public static final int CC_TFTB_CP_H2 = 0x84; //从车TFTB车牌后二


    public static final int OUT_UPPER_MODE = 0xb8; //退出上位机控制模式

    private String TAG = "<ControlCommand>";
    public byte[] order = new byte[17];
    public byte[] c_order = new byte[17];
    public static int mark = -50;
    public static int my_auto_mark = -50;
    private Vibrator vibrator;//震动
    public int control_way_flag = 0;
    private String jt_sb;
    private String jtbz;
    private String erweima111;

    QR_Code qr_code= new QR_Code();
    private String cxsb;
    private String jtbzsb;


    /**
     * 自动行走控制
     *
     * @param str  数据
     * @param flag 反馈控制
     */
    public void Auto_Run(String str, boolean flag) {
        char[] str_data = str.toCharArray();
        byte[] data_4 = new byte[5];
        data_4[0] = (byte) 1;
        data_4[1] = (byte) auto_run;
        data_4[2] = (byte) (str_data[0] - 0x40);
        data_4[3] = (byte) (str_data[1] - 0x30);
        switch (str_data[2]) {
            case 'U':
                data_4[4] = 0;
                break;
            case 'L':
                data_4[4] = 1;
                break;
            case 'D':
                data_4[4] = 2;
                break;
            case 'R':
                data_4[4] = 3;
                break;
            default:
                break;
        }
        if (str_data[3] == '1') {   //改变路线
            data_4[4] |= 0x10;
        }
        ConneceReceiveSend.CodeSend(data_4, flag);
    }
    // 例 使用方法  F2U 不修改先走还是字母 先走 数字 如有需要 将  data_4[4] |= 0x10; 先走数字
    public void C_Auto_RK(String str, boolean flag) {
        char[] str_data = str.toUpperCase().toCharArray();
        byte[] data = new byte[6];
        data[0] = (byte) 0x06;
        data[1] = (byte) CC_ORDER;//0x88  指令控制
        data[2] = (byte) auto_ck;//0xa1  自动入库指令
        data[3] = (byte) (str_data[0] - 0x40);
        data[4] = (byte) (str_data[1] - 0x30);
        switch (str_data[2]) {
            case 'U':
                data[5] = (byte) 0;
                break;
            case 'L':
                data[5] = (byte) 1;
                break;
            case 'D':
                data[5] = (byte) 2;
                break;
            case 'R':
                data[5] = (byte) 3;
                break;
            default:
                break;
        }
//            data[5] |= 0x10;//   车头或1

        ConneceReceiveSend.CodeSend(data, flag);
    }
    // 例 使用方法  F2U 不修改先走还是字母 先走 数字 如有需要 将  data_4[4] |= 0x10; 先走数字
    public void Auto_RK(String str, boolean flag) {


        char[] str_data = str.toUpperCase().toCharArray();
        byte[] data_4 = new byte[5];
        data_4[0] = (byte) 1;
        data_4[1] = (byte) auto_ck;//  自动入库方法
        data_4[2] = (byte) (str_data[0] - 0x40);
        data_4[3] = (byte) (str_data[1] - 0x30);
        switch (str_data[2]) {
            case 'U':
                data_4[4] = 0;
                break;
            case 'L':
                data_4[4] = 1;
                break;
            case 'D':
                data_4[4] = 2;
                break;
            case 'R':
                data_4[4] = 3;
                break;
            default:
                break;
        }
//        if (str_data[3] == '1') {   //改变路线
//            data_4[4] |= 0x10;
//        }
        data_4[4] |= 0x10;
        ConneceReceiveSend.CodeSend(data_4, flag);
    }


    /**
     * 坐标初始化
     *
     * @param str  数据
     * @param flag 反馈控制
     */
    public void Z_B_Init(String str, boolean flag) {
        char[] str_data = str.toCharArray();
        byte[] data_4 = new byte[5];
        data_4[0] = (byte) 1;
        data_4[1] = (byte) z_b_init;
        data_4[2] = (byte) (str_data[0] - 0x40);
        data_4[3] = (byte) (str_data[1] - 0x30);
        switch (str_data[2]) {
            case 'U':
                data_4[4] = 0;
                break;
            case 'L':
                data_4[4] = 1;
                break;
            case 'D':
                data_4[4] = 2;
                break;
            case 'R':
                data_4[4] = 3;
                break;
            default:
                break;
        }

        ConneceReceiveSend.CodeSend(data_4, flag);
    }

    /*************************
     * 语音播报操作程序代码
     *************************/
    /**
     * 说明： GBK 为汉字编码标志之一 语音播报（自定义内容）
     *
     * @param src 内容
     */
    public void Voice(String src, int mode, boolean flag) {
        yanchi(500);
        try {
            // getBytes("GBK"); 通过指定的字符编码转换为字节数组
            byte[] sbyte = src.getBytes("GBK"); // 转换为字节数据
            if ((mode != 2) && (mode != 3))
                mode = 2;  //2语音播报标志物操作，3小车播报语音
            byte a = (byte) mode;
            byte[] sbyte1 = new byte[sbyte.length + 1];
            sbyte1[0] = a;
            for (int i = 0; i < sbyte.length; i++) {
                sbyte1[i + 1] = sbyte[i];
            }
            Log.e("len: ", sbyte.length + "");
            ConneceReceiveSend.CodeSend(sbyte1, flag);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        yanchi(1000);
    }

    /*************************
     * 固定长度批量执行程序代码
     *************************/
    public void comData(int type, int com1, int dat1, int com2, int dat2,
                        int com3, int dat3, int com4, int dat4, int com5, int dat5, int com6, int dat6, boolean flag) {
        //connect_thread();
        byte[] orderbuf = new byte[13];
        orderbuf[0] = (byte) 1;
        orderbuf[1] = (byte) com1;
        orderbuf[2] = (byte) dat1;
        orderbuf[3] = (byte) com2;
        orderbuf[4] = (byte) dat2;
        orderbuf[5] = (byte) com3;
        orderbuf[6] = (byte) dat3;
        orderbuf[7] = (byte) com4;
        orderbuf[8] = (byte) dat4;
        orderbuf[9] = (byte) com5;
        orderbuf[10] = (byte) dat5;
        orderbuf[11] = (byte) com6;
        orderbuf[12] = (byte) dat6;


        ConneceReceiveSend.CodeSend(orderbuf, flag);  //数据发送

    }

    /*************************
     * 处理批量自动行走代码
     *************************/
    public void comData(String src, boolean flag) {
        String[] data = src.split(",");
        int num = data.length;
        for (int i = 0; i < num; i++) {
            Auto_Run(data[i], true);
        }
    }

    /*******************从车控制方法**********************/
    public void C_ComData(int mode, int com1, int dat1, int com2, int dat2, boolean flag) {

        byte[] orderbuf = new byte[6]; // 数据缓冲值
        orderbuf[0] = (byte) 0x06;
        orderbuf[1] = (byte) mode;
        orderbuf[2] = (byte) com1;
        orderbuf[3] = (byte) dat1;
        orderbuf[4] = (byte) com2;
        orderbuf[5] = (byte) dat2;
        ConneceReceiveSend.CodeSend(orderbuf, flag);  //数据发送
    }


    /**
     * 从车自动行走控制
     *
     * @param str  数据
     * @param flag 反馈控制
     */
    public void C_Auto_Run(String str, boolean flag) {
        char[] str_data = str.toCharArray();
        byte[] data = new byte[5];
        data[0] = (byte) 0x06;
        data[1] = (byte) 0x66;
        data[2] = (byte) (str_data[0] - 0x40);
        data[3] = (byte) (str_data[1] - 0x30);
        switch (str_data[2]) {
            case 'U':
                data[4] = (byte) 0;
                break;
            case 'L':
                data[4] = (byte) 1;
                break;
            case 'D':
                data[4] = (byte) 2;
                break;
            case 'R':
                data[4] = (byte) 3;
                break;
            default:
                break;
        }
        if (str_data[3] == '1') {   //改变路线
            data[4] |= 0x10;
        }
        ConneceReceiveSend.CodeSend(data, flag);
    }



    /**
     * 从车初始化坐标
     *
     * @param str  数据
     * @param flag 反馈控制
     */
    public void C_Z_B_Init(String str, boolean flag) {
        char[] str_data = str.toCharArray();
        byte[] data = new byte[6];
        data[0] = (byte) 0x06;
        data[1] = (byte) 0x88;
        data[2] = (byte) 0x68;
        data[3] = (byte) (str_data[0] - 0x40);
        data[4] = (byte) (str_data[1] - 0x30);
        switch (str_data[2]) {
            case 'U':
                data[5] = (byte) 0;
                break;
            case 'L':
                data[5] = (byte) 1;
                break;
            case 'D':
                data[5] = (byte) 2;
                break;
            case 'R':
                data[5] = (byte) 3;
                break;
        }
        ConneceReceiveSend.CodeSend(data, flag);
    }


    /**
     * 指令发送方法
     *
     * @param type 主从切换
     * @param com1 指令1
     * @param dat1 参数1
     * @param com2 指令2
     * @param dat2 参数2
     * @param com3 指令3
     * @param dat3 参数3
     * @param com4 指令4
     * @param dat4 参数4
     * @param com5 指令5
     * @param dat5 参数5
     * @param com6 指令6
     * @param dat6 参数6
     */
    public void comDataTest(int type, int com1, int dat1, int com2, int dat2,
                            int com3, int dat3, int com4, int dat4, int com5, int dat5, int com6, int dat6) {
        //connect_thread();
        byte[] orderbuf = new byte[13];

        orderbuf[0] = (byte) type;
        orderbuf[1] = (byte) com1;
        orderbuf[2] = (byte) dat1;
        orderbuf[3] = (byte) com2;
        orderbuf[4] = (byte) dat2;
        orderbuf[5] = (byte) com3;
        orderbuf[6] = (byte) dat3;
        orderbuf[7] = (byte) com4;
        orderbuf[8] = (byte) dat4;
        orderbuf[9] = (byte) com5;
        orderbuf[10] = (byte) dat5;
        orderbuf[11] = (byte) com6;
        orderbuf[12] = (byte) dat6;

        ConneceReceiveSend.CodeSend(orderbuf, false);  //数据发送
        //发送方法里面嵌套Hander
        //添加发送文字提示
    }

    public Thread AutoThread = new Thread(new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (true) {
                //  Log.e("run: ","启动全自动线程" );
                Full_Auto();
            }
        }
    });


    /**
     * 延迟函数
     *
     * @param time
     */
    public void yanchi(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*************************
     * 全自动执行程序代码（str 是输入的路线， num 是选择的任务）
     *************************/
    public void Automatic_execute(String str, int num) {
        String[] line = str.split(",");
        Log.e("路线数组", Arrays.toString(line));
        int task = num;
        for (int i = 0; i < line.length; i++) {
            Log.e("次数", i + "");
            Automatic_line(line, i); //路线
            Automatic_task(task, i);  //任务
        }
    }


    /*************************
     * 全自动路线程序代码
     *************************/
    public void Automatic_line(final String[] str, int num) {
        // String [] route = str;
        if (num == 0) {
            Z_B_Init(str[0], true);
            yanchi(500);
        } else {
            Auto_Run(str[num], true);
        }
    }


    /*************************
     * 交通标志识别程序代码
     *************************/
    public String Discern_JTBZ(){
        yanchi(1000);
        pingmujiequ pingmujiequ=new pingmujiequ();
        Mat dsmat=pingmujiequ.init_carpingmujiequ(MainActivity.bitmap);

        Bitmap resultbitmap = Bitmap.createBitmap(dsmat.width(), dsmat.height(),Bitmap.Config.ARGB_8888);

        Utils.matToBitmap(dsmat, resultbitmap);

        yanchi(1000);
        carjtbz carjtbzs=new carjtbz();
        String str=carjtbzs.init_carjtbz(resultbitmap);
        return str;
    }


    /*************************
     * 图形识别程序代码
     *************************/
    public String Discern_graph() {
        yanchi(5000);
        String str = new Figure().figuteHandle(MainActivity.bitmap);
        return str;
    }

    /*************************
     * 车牌识别程序代码
     *************************/
    public String Discern_plate() {
        yanchi(5000);
        carppocr carppocr=new carppocr();
        String str = carppocr.init_carppocr(MainActivity.bitmap,1);
        return str;
    }

    /*************************
     * 车型识别程序代码
     *************************/

    public String Discern_detecar() {
        yanchi(1000);
        pingmujiequ pingmujiequ=new pingmujiequ();
        Mat dsmat=pingmujiequ.init_carpingmujiequ(MainActivity.bitmap);

        Bitmap resultbitmap = Bitmap.createBitmap(dsmat.width(), dsmat.height(),Bitmap.Config.ARGB_8888);

        Utils.matToBitmap(dsmat, resultbitmap);

        yanchi(1000);
        cardetect cardetects=new cardetect();
        String strsss=cardetects.init_cardete(resultbitmap);


        return strsss;
    }


//            Looper.prepare();//增加部分
//    Bitmap res=new Image().getCutRectangleBitmap();
//
//    yanchi(1000);
//    cardetect cardetects=new cardetect();
//    String str=cardetects.init_cardete(res);
//        Looper.loop();//增加部分

//    /*************************
//     * 交通灯拍照程序代码
//     *************************/
//    public int Traffic_photo(int A_B) {
//        String Traffic_nameFile = "图形识别/图片/";
//        yanchi(3000);
//        int num = 0;
//        for (int i = 0; i < 15; i++) {
//            try {
//                Fill.savePhoto(Traffic_nameFile, "交通灯定位1.png", MainActivity.bitmap);
//                yanchi(900);
//                Fill.savePhoto(Traffic_nameFile, "交通灯定位2.png", MainActivity.bitmap);
//                num = Figure.Traffic_place();
//                if (num == 1) {
//                    if(A_B == 1){
//                        comData(1, qh_AB,bzw_A, jtd_open, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);  //开启交通灯识别
//                    }else {
//                        comData(1, qh_AB,bzw_B, jtd_open, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);  //开启交通灯识别
//                    }
//                    yanchi(1100);
//                    Fill.savePhoto(Traffic_nameFile, "交通灯识别.png", MainActivity.bitmap);
//                    break;
//                }
//            } catch (Exception e) {
//                Log.e("交通灯: ", "交通灯定位拍照报错");
//            }
//            Log.e("交通灯: ", "交通灯定位" + i + "");
//        }
//        if (num != 1) num = -1;
//        return num;
//    }


    /**
     * 图像识别 mode: 1图形识别 2红绿灯识别 3车牌识别
     * preinstall  预设位 0不动
     */
    public String Discern(int mode, int preinstall) {
        String str = "";
        switch (preinstall) {
            case 0:
                break;
            case 1:
                MainActivity.state_camera = 11;// 11   第一个预设位
                break;
            case 2:
                MainActivity.state_camera = 12;
                break;
            case 3:
                MainActivity.state_camera = 13;
                break;
            case 4:
                MainActivity.state_camera = 14;
                break;
            case 5:
                MainActivity.state_camera = 15;
                break;
            case 6:
                MainActivity.state_camera = 16;
                break;
        }
        switch (mode) {
            case 1: // 图片图形颜色形状
                yanchi(5000);
                str=Discern_graph();
                break;

            case 8:  //交通标志物识别
                yanchi(5000);
                str=Discern_JTBZ();
                break;

            case 2: // 车牌识别
                yanchi(5000);
                str = Discern_plate();
                break;
            case 3: // 交通灯识别A
                Voice("交通灯识别B", 1, true);
//                yanchi(3000);
//                comData(1,jtd_open,0,0,0,0,0,0,0,0,0,0,0,true);  //开启交通灯识别
//                yanchi(1000);
                comData(1,qh_AB,bzw_A,jtd_open,0,0,0,0,0,0,0,0,0,true);
                yanchi(3000);
                int num1 = Traffic.Traffic_Out();  //交通灯结果
                System.out.println("ssss:"+num1);
                comData( 1,qh_AB, bzw_A, jtd_jg, num1,0,0,0,0,0,0,0,0, true);
                str = num1 + "";
                MainActivity.state_camera = 2;
                break;
            case 4: // 二维码识别1
                yanchi(5000);
                str = qr_code.Qr_11code(1);
                break;

            case 5: // 二维码识别2
                yanchi(5000);
                str = qr_code.Qr_11code(2);
                break;
            case 7: // 二维码识别3  TFT二维码截取屏幕识别
                yanchi(5000);
                str = qr_code.Qr_11code(3);
               // str = QR_Code.TFT_Qrcode(3);
                break;
            case 6: // 交通灯识别B
                Voice("交通灯识别B", 1, true);
                comData(1,qh_AB,bzw_B,jtd_open,0,0,0,0,0,0,0,0,0,true);
                yanchi(3000);
                int numb = Traffic.Traffic_Out();  //交通灯结果
                System.out.println("ssss:"+numb);
                comData(1, qh_AB, bzw_B, jtd_jg, numb, 0,0, 0, 0, 0, 0, 0, 0, true);
                str = numb + "";
                MainActivity.state_camera = 2;
                break;

            case 9:  ///车型识别
                yanchi(5000);
                str=Discern_detecar();


            case 19:
                Voice("文本识别", 3, true);
                Looper.prepare();
                Image image=new Image();
                Bitmap resss=image.getCutRectangleBitmap();
                carppocr carppocr=new carppocr();
                String os=carppocr.init_carppocr(resss,2);
         //       String s=os.replaceAll("[^a-zA-Z0-9文字文字文字-文字]","");
                System.out.println(os);
                Voice(os, 3, true);
            default:
                break;
        }
        return str;
    }


    /*************************
     * 全自动任务程序代码
     *************************/
    public void Automatic_task(int task, int num) {
        int number = num;
        int mode = task;
        switch (mode) {
            case 0:
                switch (number) {
                    case 0:
                        comData(1, smg_js, jsk, gate, on, tft_js, on, cxf, on, 0, 0, 0, 0, true);
                        break;//数码管计时开道闸
                    case 1:
                        comData(1, zxd, 0x11, buzz, on, 0, 0, 0, 0, 0, 0, 0, 0, true);
                        yanchi(1100);
                        comData(1, zxd, 0x00, buzz, off, 0, 0, 0, 0, 0, 0, 0, 0, true);
                        break;  //双闪灯 蜂鸣器 开关
                    case 2:
                        yanchi(1500);
                        Voice(Qrcode(1), 3, true);     //二维码识别并播报
                        comData(1, cj, 1, right, p_z, right, mp_z + 42, xsjl, xz_jl + 1, left, mp_z + 42, 0, 0, true);
                        break;
                    case 3:
                        comData(1, tft_fy, u_f + 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                        yanchi(2000);
                        String str = Discern(1, 0);
//                        str = str.substring(0,5);
                        Voice(str, 3, true);                                  //主车识别图形
                        comData(1, tft_fy, d_f + 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);        //主车识别车牌
                        yanchi(1500);
                        String cp_str = Discern_plate();
                        Way.Get_DZ_CP();
                        comData(1, trends_data, gate_cp_6, GATE_CP_6[0], GATE_CP_6[1], GATE_CP_6[2], GATE_CP_6[3], GATE_CP_6[4], GATE_CP_6[5], 0, 0, 0, 0, true);        //主车识别车牌
                        Voice(cp_str, 3, true);
                        break;
                    case 4:
                        comData(1, tg, 6, left, p_z, left, mp_z + 42, beeper, on, right, mp_z + 42, 0, 0, true);
                        break; //调光获取挡位，开报警器
                    case 5:
                        yanchi(2000);
                        comData(1, jtd_open, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                        yanchi(1000);
                        int num1 = Traffic.Traffic_Out();  //交通灯结果
                        comData(1, jtd_jg, num1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                        break;
                    case 6:
                        comData(1, rfid_sq, 0x04, 0x05, 0x06, 0, 0, 0, 0, 0, 0, 0, 0, true);
                        break; //读白卡
                    case 7:
                        comData(1, ltck_layer, 1, back, 120, ltck_layer, 2, 0, 0, 0, 0, 0, 0, true);
                        comData(1, ltck_layer, 1, go, 50, 0, 0, 0, 0, 0, 0, 0, 0, true);
                        break; //自动入库
                    case 8:
                        comData(1, trends_data, smg_data, 1, 0x12, 0x34, 0x56, 0, 0, 0, 0, 0, 0, true); //数码管显示信息，tft显示信息
                        comData(1, trends_data, smg_data, 2, 0x12, 0x34, 0x56, 0, 0, 0, 0, 0, 0, true);
                        comData(1, trends_data, tft_data_3, 0, 0x12, 0x34, 0x56, 0, 0, 0, 0, 0, 0, true);
                        break;
                    case 9:
                        C_Z_B_Init("B1U", true);
                        C_Auto_Run("B4R1", true);
                        C_ComData(0x88, cj, 1, right, p_z, true);
                        C_ComData(0x88, right, 2, xsjl, xz_jl + 1, true);
                        C_ComData(0x88, left, 2, left, p_z, true);
                        yanchi(500);
                        comData(1, cc_ewm, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);// 从车二维码 结果保存文本
                        C_Auto_Run("B1U1", true);
                        C_ComData(0x88, back, 20, 0, 0, true);
                        break;   //控制从车方法
                    case 10:
                        Voice("交通灯识别A", 3, true);
//                String jtd_str = Way.traffic_light_tq();
//                comData(1, qh_AB, bzw_A, jtd_open, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
//                Discern(3, 0); //交通灯识别  预设位对应为3
                        comData(1,qh_AB,bzw_A,jtd_open,0,0,0,0,0,0,0,0,0,true);
                        int num1s = Traffic.Traffic_Out();  //交通灯结果
                        comData(1, qh_AB, bzw_A, jtd_jg, num1s, 0,0, 0, 0, 0, 0, 0, 0, true);
                        break;
                }
                break;
            case 1:
                switch (number) {
                    case 0:
                        Voice("牛牛要吃河边柳", 3, true);
                        yanchi(3000);
                        break;
                    case 1:
                        comData(1, left, p_z, beeper, on, 0, 0, left, p_z, 0, 0, 0, 0, true);
                        break;
                    case 2:
                        Voice("牛牛扭头瞅妞妞", 3, true);
                        break;
                }
                break;
            case 2:
                switch (number) {
                    case 0:
                        Voice("牛牛要吃河边柳", 3, true);
                        yanchi(3000);
                        break;
                    case 1:
                        comData(1, left, p_z, beeper, on, 0, 0, left, p_z, 0, 0, 0, 0, true);
                        break;
                    case 2:
                        Voice("牛牛扭头瞅妞妞", 3, true);
                        break;
                }
                break;
            case 3:
                switch (number) {

                }
                break;
        }
    }





    /**
     * 上位机任务执行方法
     */
    private int cs_flag = 1;

    public void Task_Out() {
        switch ((byte) my_auto_mark) {
            case (byte) 0x00:
                if (cs_flag == 1)
                    Voice("开启全自动", 3, true);


                //     comData(1,smg_js,jsk,car_smg,datas,0,0,0,0,0,0,0,0,true);
                comData(1, trends_data, zigbee_data_8, 0x55,0x0a,0x01,0x01,0x00,0x00,0x02,0xBB, 0, 0, true);
                //   comData(1,go,50,0,0,0,0,0,0,0,0,0,0,true);
                comData(1, OUT_UPPER_MODE, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);  //开启全自动
                my_auto_mark = -30;
                break;
            case (byte) 0x01:

                if (cs_flag == 1)
                    Voice("报警器", 3, true);


                Way.erweimashi();

//                String DATA="FF15XX00000000"; // 比赛红外码
//
//                ControlCommand.HW_Data_6[0] = Integer.valueOf(DATA.substring(0, 2), 16);
//                ControlCommand.HW_Data_6[1] = Integer.valueOf(DATA.substring(2, 4), 16);
//                ControlCommand.HW_Data_6[2] = Integer.valueOf(DATA.substring(4, 6), 16);
//                ControlCommand.HW_Data_6[3] = Integer.valueOf(DATA.substring(6, 8), 16);
//                ControlCommand.HW_Data_6[4] = Integer.valueOf(DATA.substring(8,10), 16);
//                ControlCommand.HW_Data_6[5] = Integer.valueOf(DATA.substring(10,12), 16);

                //主车报警器

                comData(1,trends_data,hw_data_6,0x69,0x68,0x04, 0x65,0x66,0xc4,0,0,0,0,true);

                // 从车红外打开报警器
//              comData(1, trends_data, c_hw_data_6, HW_Data_6[0], HW_Data_6[1], HW_Data_6[2], HW_Data_6[3], HW_Data_6[4], HW_Data_6[5], 0, 0, 0, 0, true);//  从车红外数据
                //              comData(1,trends_data,c_hw_data_6,0x03,0x05,0x14,0x45,0xDE,0x92,0,0,0,0,true);

                comData(1, OUT_UPPER_MODE, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                Log.e("上位机命令", "报警器");
                my_auto_mark = -10;
                break;
            case (byte) 0x02:
                if (cs_flag == 1)
                    Voice("无线充电开启码", 3, true);
                Way.Get_Zigbee(); //Zigbee8位数据
                //主车
                //              comData(1, trends_data, zigbee_data_8, Zig_Data_8[0], Zig_Data_8[1], Zig_Data_8[2], Zig_Data_8[3], Zig_Data_8[4], Zig_Data_8[5], Zig_Data_8[6], Zig_Data_8[7], 0, 0, true);
                //           comData(1, trends_data, zigbee_data_8,0x55,0x03,0x01,0x01,0x00,0x00,0x02,0xBB, 0, 0, true);

                //从车动态zigbee
//                comData(1, trends_data, c_zigbee_data_8, Zig_Data_8[0], Zig_Data_8[1], Zig_Data_8[2], Zig_Data_8[3], Zig_Data_8[4], Zig_Data_8[5], Zig_Data_8[6], Zig_Data_8[7], 0, 0, true);
//                comData(1, trends_data, c_zigbee_data_8, 0x55,0x0a,0x01,0x01,0x00,0x00,0x02,0xbb, 0, 0, true);

                comData(1, OUT_UPPER_MODE, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                Log.e("上位机命令", "无线充电开启码");
                my_auto_mark = -20;
                break;
            case (byte) 0x03:       //底层调整
                if (cs_flag == 1)
                    Voice("条光", 3, true);
                int m=0;
                comData(1,tg_gz_get,0x88,0,0,0,0,0,0,0,0,0,0,true);
                int nugt= Way.basyunt();
                int n=Way.guangzhao();
                int r=Way.BS_Tu_xings();


                m=((nugt+r)^n)%4+1;

                comData(1,tg,m,0,0,0,0,0,0,0,0,0,0,true);  //主车调光

//                int TG_DW = 0;
//                    TG_DW = Way.BS_DIMMING(); //获取调光挡位
//
//                comData(1,tg,TG_DW,0,0,0,0,0,0,0,0,0,0,true);  //主车调光

//                C_ComData(CC_ORDER, tg, TG_DW, 0, 0, true);  //从车调光

                comData(1, OUT_UPPER_MODE, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                Log.e("上位机命令", "调光");
                my_auto_mark = -30;
                break;
            case (byte) 0x04://  a立体车库调整指定层数
                if (cs_flag == 1)
                    Voice("层数A", 3, true);
//                int CK_CS = 1;
//                CK_CS = Way.BS_3D_CK_A();  //获取立体车库停靠层数
//                comData(1, qh_AB, bzw_A, ltck_layer, 1, 0, 0, 0, 0, 0, 0, 0, 0, true);

//                if(BasicControl.parameter_array[4] != 0){
////                    C_ComData(CC_ORDER, qh_AB, bzw_A, ltck_layer, BasicControl.parameter_array[4], true);//从车控制
//                    comData(1, qh_AB, bzw_A, ltck_layer, BasicControl.parameter_array[4], 0, 0, 0, 0, 0, 0, 0, 0, true);
//                }else{
////                    C_ComData(CC_ORDER, qh_AB, bzw_A, ltck_layer, CK_CS, true);//从车控制
//                    comData(1, qh_AB, bzw_A, ltck_layer, CK_CS, 0, 0, 0, 0, 0, 0, 0, 0, true);
//                }
                Integer integer=new Integer(jt_sb);
                comData(1, qh_AB, bzw_A, ltck_layer,integer, 0, 0, 0, 0, 0, 0, 0, 0, true);

                comData(1, OUT_UPPER_MODE, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                Log.e("上位机命令", "层数A");
                my_auto_mark = -30;
                break;
            case (byte) 0x05://b立体车库调整指定层数
                if (cs_flag == 1)
                    Voice("层数B", 3, true);
                comData(1, qh_AB, bzw_B, ltck_get_layer, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);

                int CK_CS_B = Way.BS_3D_CK_B();  //获取立体车库停靠层数

                comData(1, qh_AB, bzw_B, ltck_layer, CK_CS_B, 0, 0, 0, 0, 0, 0, 0, 0, true);
//                if(BasicControl.parameter_array[7] != 0){ //设定参数
////                    C_ComData(CC_ORDER, qh_AB, bzw_B, ltck_layer, BasicControl.parameter_array[7], true);//从车控制
//                    comData(1, qh_AB, bzw_B, ltck_layer, BasicControl.parameter_array[7], 0, 0, 0, 0, 0, 0, 0, 0, true);
//                }else{
////                    C_ComData(CC_ORDER, qh_AB, bzw_B, ltck_layer, CK_CS_B, true);//从车控制
//                    comData(1, qh_AB, bzw_B, ltck_layer, CK_CS_B, 0, 0, 0, 0, 0, 0, 0, 0, true);

                comData(1, OUT_UPPER_MODE, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                Log.e("上位机命令", "层数B");
                my_auto_mark = -30;
                break;
            case (byte) 0x06:
                if (cs_flag == 1)
                    Voice("任务板数码管指定显示", 3, true);
                comData(1, car_smg, 0x11, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                Log.e("上位机命令", "任务板数码管指定显示");

                comData(1, OUT_UPPER_MODE, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                my_auto_mark = -30;
                break;
            case (byte) 0x07:
                if (cs_flag == 1)
                    Voice("交通灯识别A", 3, true);
//                String jtd_str = Way.traffic_light_tq();
//                comData(1, qh_AB, bzw_A, jtd_open, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
//                Discern(3, 0); //交通灯识别  预设位对应为3
//                comData(1,back,10,0,0,0,0,0,0,0,0,0,0,true);
//                comData(1,qh_AB,bzw_A,jtd_open,0,0,0,0,0,0,0,0,0,true);
//                int num1 = Traffic.Traffic_Out();  //交通灯结果
//                comData(1, qh_AB, bzw_A, jtd_jg, num1, 0,0, 0, 0, 0, 0, 0, 0, true);

//                jt_sb = Discern(3,1);

//                C_ComData(CC_ORDER,qh_AB,bzw_A,jtd_jg,1,true);
                jt_sb = Discern(3,2);

                comData(1, OUT_UPPER_MODE, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                Log.e("上位机命令", "交通灯识别A");
                my_auto_mark = -30;
                break;
            case (byte) 0x08:
                if (cs_flag == 1)
                    Voice("交通灯识别B", 1, true);
//                String jtd_str = Way.traffic_light_tq();
//                comData(1,back,5,0,0,0,0,0,0,0,0,0,0,true);

                comData(1,qh_AB,bzw_B,jtd_open,0,0,0,0,0,0,0,0,0,true);
//                int numb = Traffic.Traffic_Out();  //交通灯结果
//                comData(1, qh_AB, bzw_B, jtd_jg, numb, 0,0, 0, 0, 0, 0, 0, 0, true);
                Discern(6,2);
                comData(1, OUT_UPPER_MODE, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                Log.e("上位机命令", "交通灯识别B");
                my_auto_mark = -30;
                break;
            case (byte) 0x09:
                if (cs_flag == 1)
                    Voice("图形识别", 3, true);

                String strsss=null;
                jtbzsb=null;

         //       comData(1, qh_AB, bzw_A, tft_fy,d_f+1, 0, 0, 0, 0, 0, 0, 0, 0, true);

                //            Looper.prepare();
                //            Discern(1, 0);


                for (int i=0;i<=10;i++) {
                    if (jtbzsb == null || jtbzsb.length() <= 1) {

                        comData(1, qh_AB, bzw_A, tft_fy,d_f+1, 0, 0, 0, 0, 0, 0, 0, 0, true);
                        jtbzsb = Discern(8, 0);

                        if (i==9){
                            String result="zuozhuan";  //  "jinzhitongxing","jinzhizhixing","zhixing","diaotou","zuozhuan","youzhuan"
                            new Fill().saveFile(File_Name.PLATE_FRUIT, result);  //保存识别结果
                            //     comData(1, intest, GATE_CP_6[0], GATE_CP_6[1], GATE_CP_6[2], GATE_CP_6[3], GATE_CP_6[4], GATE_CP_6[5],0, 0, 0, 0, 0, true);
                            Voice("完成",3,true);
                        }
                    }else {

                     //   comData(1, intest, GATE_CP_6[0], GATE_CP_6[1], GATE_CP_6[2], GATE_CP_6[3], GATE_CP_6[4], GATE_CP_6[5],0, 0, 0, 0, 0, true);
                        if (strsss == null  || strsss.equals("") || strsss.length()<3) {
                            comData(1, qh_AB, bzw_A, tft_fy, d_f + 1, 0, 0, 0, 0, 0, 0, 0, 0, true);
                            strsss = Discern(1, 0);
                            if (i==9){
                                String esprust="A1B2C3";
                                new Fill().saveFile(File_Name.CarRES_txsb,esprust);  //保存图形识别标志结果
                                Way.BS_Tu_xing();
                                Voice("完成1", 3, true);
                                break;
                            }
                        } else {
                            Way.BS_Tu_xing();
                            Voice("完成2", 3, true);
                            break;
                        }

                    }


                }



                //           comData(1, trends_data,tftb_data_3 ,0, ControlCommand.TFT_Data_3[0] , ControlCommand.TFT_Data_3[1], ControlCommand.TFT_Data_3[2], ControlCommand.TFT_Data_3[3], ControlCommand.TFT_Data_3[4], 0, 0, 0, 0,true);
                Voice("完成", 3, true);

                comData(1, OUT_UPPER_MODE, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                Log.e("上位机命令", "主车图形识别");
                my_auto_mark = -30;

                break;
            case (byte) 0x10:
                if (cs_flag == 1)
                    Voice("从车图形识别", 3, true);


                comData(1, OUT_UPPER_MODE, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                Log.e("上位机命令", "从车图形识别");
                my_auto_mark = -30;
                break;
            case (byte) 0x11:  //二维码识别
                if (cs_flag == 1)
                    Voice("二维码识别", 3, true);
                //
                //          Way.BS_beeper_HW();
                Discern(4, 0); //二维码识别
                //      Way.erweima();

                //          erweima111 = Way.erweimashi();






                comData(1, OUT_UPPER_MODE, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                my_auto_mark = -30;
                break;
            case (byte) 0x12:  //二维码识别
                if (cs_flag == 1)
                    Voice("二维码识别", 3, true);


                Discern(5, 1); //二维码识别

                //        Way.erweimashi();

                comData(1, OUT_UPPER_MODE, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                my_auto_mark = -30;
                break;
            case (byte) 0x13:  //二维码识别
                if (cs_flag == 1)
                    Voice("第三次二维码识别", 3, true);
                int numse=0;   //距离自动
                String datae=null;
                try {
                    datae =  new Fill().carRead(File_Name.HOST_RANGE+ 3 + ".txt");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Integer iso=new Integer(datae);
                numse=iso.intValue();
                numse=numse/10;

                if(numse<40)
                {
                    int sume=40-numse;
                    comData(1,back,sume,0,0,0,0,0,0,0,0,0,0,true);
                    Voice("第三次二维码识别", 3, true);
                    Discern(7, 0); //二维码识别
                    comData(1,go,sume,0,0,0, 0,0,0,0,0,0,0,true);

                }else{
                    Voice("第三次二维码识别", 3, true);
                    Discern(7, 0); //二维码识别

                }

                comData(1, OUT_UPPER_MODE, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                Log.e("上位机命令", "第三次二维码识别");
                my_auto_mark = -30;
                break;
                /////
            case (byte) 0x14:  //主车车牌识别
                if (cs_flag == 1)
                    Voice("车牌识别", 3, true);
                String strss="";
                comData(1, qh_AB, bzw_A, tft_fy,d_f+1, 0, 0, 0, 0, 0, 0, 0, 0, true);
                cxsb=null;


                for (int i=0;i<=10;i++) {

                    if (cxsb == null || cxsb.length() <= 1) {

                        comData(1, qh_AB, bzw_A, tft_fy,d_f+1, 0, 0, 0, 0, 0, 0, 0, 0, true);
                        cxsb = Discern(9, 0);
                        if (i==9){
                            String result="国A12345";
                            new Fill().saveFile(File_Name.PLATE_FRUIT, result);  //保存识别结果
                            comData(1, intest, GATE_CP_6[0], GATE_CP_6[1], GATE_CP_6[2], GATE_CP_6[3], GATE_CP_6[4], GATE_CP_6[5],0, 0, 0, 0, 0, true);
                            Voice("完成",3,true);
                        }
                    }else {
                        Way.Get_DZ_CP();
                        comData(1, intest, GATE_CP_6[0], GATE_CP_6[1], GATE_CP_6[2], GATE_CP_6[3], GATE_CP_6[4], GATE_CP_6[5],0, 0, 0, 0, 0, true);
                        Voice("完成",3,true);
                        break;

                    }
                }








                //      Voice(cp_str, 3, true);

                comData(1, OUT_UPPER_MODE, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                my_auto_mark = -30;
                break;
            case (byte) 0x15:  //主车指定路线行驶
                if (cs_flag == 1)
                    Voice("主车指定路线行驶", 3, true);
                try {

                    String[] rest = Way.Set_ZC_Line();
                    for(int id=0;id<rest.length;id++){
                        //                    if(i==rest.length-1)
                        //                        comData(1, gate, on, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                        Log.e( "Task_Out: ", rest[id]);
                        Auto_Run(rest[id],true);
                    }
                    comData(1, right, dt, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);

                    //                    Auto_Run(rest[i],true);

                }catch (Exception e){
                }

                comData(1, OUT_UPPER_MODE, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                Log.e("上位机命令", "主车指定路线行驶");
                my_auto_mark = -30;
                break;
            case (byte) 0x16:  //主车入库
                if (cs_flag == 1)
                    Voice("主车入库", 3, true);


                Way.chekuzhu();
//                Auto_Run("G6R0",true);
//                comData(1, auto_ck, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);//auto_ck底层可能会设置预先设置的车库,及指定的车库层数

                //        int RK = Way.cheku();

                //      comData(1, qh_AB, bzw_B, ltck_layer, RK, 0, 0, 0, 0, 0, 0, 0, 0, true);
                // 将车头坐标|0x10 即可

                comData(1, OUT_UPPER_MODE, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);

                Log.e("上位机命令", "主车入库");
                my_auto_mark = -30;
                break;
            case (byte) 0x17:
                if (cs_flag == 1)
                    Voice("从车起始", 3, true);
//                String CC_QSCOORD = "";
//                CC_QSCOORD = Way.BS_CC_QSCOORD();  //获取从车起始坐标
//                C_Z_B_Init("F7D", true);
//                yanchi(1000);  //控制从车时延迟一下

                comData(1, OUT_UPPER_MODE, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                Log.e("上位机命令", "从车起始");
                my_auto_mark = -30;
                break;
            case (byte) 0x18:
                if (cs_flag == 1)
                    Voice("从车指定路线", 3, true);
                try {

                    String line = Way.BS_CC_LXCOORD();
                    String[] rest = line.split(" ");
                    Log.e("从车指定路线: ",Arrays.toString(rest) );
                    for(int isd=0;isd<rest.length;isd++){
                        if(isd==rest.length-1)
                            comData(1, gate, on, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                        C_Auto_Run(rest[isd],true);

                    }
                }catch (Exception e){
                }

                comData(1, OUT_UPPER_MODE, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                Log.e("上位机命令", "从车指定路线");
                my_auto_mark = -30;
                break;
            case (byte) 0x19:
                if (cs_flag == 1)
                    Voice("从车入库", 3, true);
                String CC_CKCOORD = "B7U";
                CC_CKCOORD = Way.BS_CC_CKCOORD();  //获取从车入库坐标
//                String str1[] = CC_CKCOORD.split(",");
//                for(int i=0;i<str1.length;i++){
//                    if(i==str1.length-1)
//                        comData(1, gate, on, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
//                    C_Auto_Run(str1[i],true);
//                    Log.i(TAG, "Task_Out: 从车坐标"+str1[i]);
//                }
                C_Auto_RK(CC_CKCOORD,true);//主车自动回库车库 参数 填写坐标即可 默认 先走字母 即横坐标 BDF 如需修改 请修改方法内
                // 将车头坐标|0x10 即可
                // C_ComData(CC_ORDER, 2, 2, 0, 0, true); //b2u


                comData(1, OUT_UPPER_MODE, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                Log.e("上位机命令", "从车入库");
                my_auto_mark = -30;
                break;
            case (byte) 0x20:
                if (cs_flag == 1)
                    Voice("结果播报显示", 3, true);
//                String str = "";
//                str = Way.BS_GRAPH_VOICE();    //主车图形特定格式播报_data,smg_data,2,SMG_Data_3[0],SMG_Data_3[1],SMG_Data_3[2],OUT_UPPER_MODE
                String voice_sb="";
                try {
                    voice_sb =  new Fill().carRead(File_Name.C_QR_CODE); // 从车二维码

                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(voice_sb.equals("")){
                    Voice("aa", 3, true);//2语音播报标志物操作，3小车播报语音
                }else{
                    Voice(voice_sb, 3, true);//2语音播报标志物操作，3小车播报语音
                }
                comData(1, OUT_UPPER_MODE, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                Log.e("上位机命令", "图形结果播报");
                my_auto_mark = -30;
                break;

            case (byte) 0x21:
                if (cs_flag == 1)
                    Voice("白卡", 3, true);

                String str = "ffffffffffff";
                Log.e( "白卡密钥: ",str );
                int rfidmy_num1 = Integer.valueOf(str.substring(0,2),16);
                int rfidmy_num2 = Integer.valueOf(str.substring(2,4),16);
                int rfidmy_num3 = Integer.valueOf(str.substring(4,6),16);
                int rfidmy_num4 = Integer.valueOf(str.substring(6,8),16);
                int rfidmy_num5 = Integer.valueOf(str.substring(8,10),16);
                int rfidmy_num6 = Integer.valueOf(str.substring(10,12),16);
                comData(1, trends_data, rfid_my, 1, rfidmy_num1, rfidmy_num2, rfidmy_num3, rfidmy_num4,rfidmy_num5, rfidmy_num6, 0, 0, 0,true);


                int RFID_SQ1 = 9;
                //      RFID_SQ1 = Way.BS_RFID_SQ();   //获取扇区块地址
//                /*副指令第一位是指定那一张白卡1，2，3填入其他默认为1
//                        后三个指令对应块地址 0 不读卡  0x60 KEYA  0x61 KEYB*/
                comData(1, rfid_sq, 1, RFID_SQ1, RFID_SQ1, RFID_SQ1, 0, 0, 0, 0, 0, 0, 0, true);
////                comData(1, trends_data, rfid_my, 1, 0x22, 0x22, 0x22, 0x22, 0x22, 0x22, 0, 0, 0, true);
////                comData(1, 0x22, 1, 0x61, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
//                Way.ousy();


                comData(1, OUT_UPPER_MODE, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                Log.e("上位机命令", "白卡1");
                my_auto_mark = -20;
                break;
            case (byte) 0x22:
                if (cs_flag == 1)
                    Voice("白卡2", 3, true);
                String strs = "ffffffffffff";
                Log.e( "白卡密钥: ",strs );
                int rfidmy_num1s = Integer.valueOf(strs.substring(0,2),16);
                int rfidmy_num2s = Integer.valueOf(strs.substring(2,4),16);
                int rfidmy_num3s = Integer.valueOf(strs.substring(4,6),16);
                int rfidmy_num4s = Integer.valueOf(strs.substring(6,8),16);
                int rfidmy_num5s = Integer.valueOf(strs.substring(8,10),16);
                int rfidmy_num6s = Integer.valueOf(strs.substring(10,12),16);
                comData(1, trends_data, rfid_my, 1, rfidmy_num1s, rfidmy_num2s, rfidmy_num3s, rfidmy_num4s,rfidmy_num5s, rfidmy_num6s, 0, 0, 0,true);


                int RFID_SQ1s = 30;
                //      RFID_SQ1 = Way.BS_RFID_SQ();   //获取扇区块地址
//                /*副指令第一位是指定那一张白卡1，2，3填入其他默认为1
//                        后三个指令对应块地址 0 不读卡  0x60 KEYA  0x61 KEYB*/
                comData(1, rfid_sq, 2, RFID_SQ1s, RFID_SQ1s, RFID_SQ1s, 0, 0, 0, 0, 0, 0, 0, true);

                comData(1, OUT_UPPER_MODE, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                Log.e("上位机命令", "白卡2");
                my_auto_mark = -20;
                break;
            case (byte) 0x23:
                if (cs_flag == 1)
                    Voice("白卡3", 3, true);
                String strsd = "ffffffffffff";
                Log.e( "白卡密钥: ",strsd );
                int rfidmy_num1se = Integer.valueOf(strsd.substring(0,2),16);
                int rfidmy_num2se = Integer.valueOf(strsd.substring(2,4),16);
                int rfidmy_num3se = Integer.valueOf(strsd.substring(4,6),16);
                int rfidmy_num4se = Integer.valueOf(strsd.substring(6,8),16);
                int rfidmy_num5se = Integer.valueOf(strsd.substring(8,10),16);
                int rfidmy_num6se = Integer.valueOf(strsd.substring(10,12),16);
                comData(1, trends_data, rfid_my, 1, rfidmy_num1se, rfidmy_num2se, rfidmy_num3se, rfidmy_num4se,rfidmy_num5se, rfidmy_num6se, 0, 0, 0,true);


                int RFID_SQ1es = 2;
                //      RFID_SQ1 = Way.BS_RFID_SQ();   //获取扇区块地址
//                /*副指令第一位是指定那一张白卡1，2，3填入其他默认为1
//                        后三个指令对应块地址 0 不读卡  0x60 KEYA  0x61 KEYB*/
                comData(1, rfid_sq, 1, RFID_SQ1es, RFID_SQ1es, RFID_SQ1es, 0, 0, 0, 0, 0, 0, 0, true);

                comData(1, OUT_UPPER_MODE, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                Log.e("上位机命令", "白卡3");
                my_auto_mark = -20;
                break;
            case (byte) 0x24:  //立体显示
                if (cs_flag == 1)
                    Voice("立体显示", 3, true);


                try {
                    String data=new Fill().carRead(File_Name.JT_BZ);

                    switch (data){
                        case "直行":
                            comData(1, trends_data,hw_data_6, 0xFF, 0x11, 0x00, 0x01, 0x00, 0x00, 0, 0, 0, 0, true);

                        case "左转":
                            comData(1, trends_data,hw_data_6, 0xFF, 0x11, 0x00, 0x02, 0x00, 0x00, 0, 0, 0, 0, true);

                        case "右转":

                            comData(1, trends_data,hw_data_6, 0xFF, 0x11, 0x00, 0x03, 0x00, 0x00, 0, 0, 0, 0, true);

                        case "掉头":
                            comData(1, trends_data,hw_data_6, 0xFF, 0x11, 0x00, 0x04, 0x00, 0x00, 0, 0, 0, 0, true);

                        case "禁止直行":
                            comData(1, trends_data,hw_data_6, 0xFF, 0x11, 0x00, 0x05, 0x00, 0x00, 0,  0, 0, 0, true);

                        case "禁止通行":
                            comData(1, trends_data,hw_data_6, 0xFF, 0x11, 0x00, 0x06, 0x00, 0x00, 0, 0, 0, 0, true);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


//               if(P_XZ_CP_8[0]!=0){
                //                   comData(1, trends_data, xz_cp, P_XZ_CP_8[0], P_XZ_CP_8[1], P_XZ_CP_8[2], P_XZ_CP_8[3], P_XZ_CP_8[4], P_XZ_CP_8[5], P_XZ_CP_8[6], P_XZ_CP_8[7], 0, 0, true);
////                      comData(1, trends_data, xz_cp, P_XZ_CP_8[0], P_XZ_CP_8[1], P_XZ_CP_8[2], 'C', 'M', '0', P_XZ_CP_8[6], P_XZ_CP_8[7], 0, 0, true);
//                }else {
////                    char[] data = "12345622".toCharArray();
////                    for (int i = 0; i < data.length; i++) {
////                        XZ_CP_8[i] = data[i];
////                    }
//
//                    Way.BS_CP_DISPOSE();    //主车车牌显示
                //                  comData(1, trends_data, xz_cp, XZ_CP_8[0], XZ_CP_8[1], XZ_CP_8[2], XZ_CP_8[3], XZ_CP_8[4], XZ_CP_8[5], XZ_CP_8[6], XZ_CP_8[7], 0, 0, true);
                //                    comData(1, trends_data, xz_cp, XZ_CP_8[0], XZ_CP_8[1], XZ_CP_8[2], XZ_CP_8[3], XZ_CP_8[4], XZ_CP_8[5], XZ_CP_8[6], XZ_CP_8[7], 0, 0, true);
//                }


                //       Way.litixian();    //立体显示
//                //从车显示车牌
                //         comData(1, trends_data, c_xz_cp, XZ_CP_8[0], XZ_CP_8[1], XZ_CP_8[2], XZ_CP_8[3], XZ_CP_8[4], XZ_CP_8[5], XZ_CP_8[6], XZ_CP_8[7], 0, 0, true);
                //      comData(1, trends_data, c_xz_cp, XZ_CP_8[0], XZ_CP_8[1], XZ_CP_8[2], XZ_CP_8[3], XZ_CP_8[4], XZ_CP_8[5], XZ_CP_8[6], XZ_CP_8[7], 0, 0, true);

                comData(1, OUT_UPPER_MODE, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                Log.e("上位机命令", "立体显示");
                my_auto_mark = -30;
                break;
            case (byte) 0x25:  //LED显示
                if (cs_flag == 1)
                    Voice("LED显示", 3, true);
                String smg_text = "E40000";
                int smg1_num1=Integer.valueOf(smg_text.substring(0,2),16);
                int smg1_num2=Integer.valueOf(smg_text.substring(2,4),16);
                int smg1_num3=Integer.valueOf(smg_text.substring(4,6),16);
//                comData(1, trends_data, smg_data, 1, smg1_num1, smg1_num2, smg1_num3, 0, 0, 0, 0, 0, 0, true);
//                comData(1, trends_data, c_led_data, 2, smg1_num3, smg1_num2, smg1_num1, 0, 0, 0, 0, 0, 0, true);
                Way.BS_GRAPH_DISPOSE();//图形结果LED显示
                //              comData(1, trends_data, smg_data, 2, SMG_Data_3[0], SMG_Data_3[1], SMG_Data_3[2], 0, 0, 0, 0, 0, 0, true);
                comData(1, trends_data, c_led_data, 2, smg1_num3, smg1_num2, smg1_num1, 0, 0, 0, 0, 0, 0, true);


                comData(1, OUT_UPPER_MODE, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                Log.e("上位机命令", "LED显示");
                my_auto_mark = -30;
                break;
            case (byte) 0x26:  //TFTA显示
                if (cs_flag == 1)
                    Voice("TFTA显示", 3, true);

                //TFTA车牌显示



                Way.BS_CP_DISPOSE();
                comData(1, trends_data, tft_cp_6, TFT_CP_6[0], TFT_CP_6[1], TFT_CP_6[2], TFT_CP_6[3], TFT_CP_6[4], TFT_CP_6[5], 0, 0, 0, 0, true);
//                    comData(1, trends_data, c_tfta_cp, TFT_CP_6[0], TFT_CP_6[1], TFT_CP_6[2], TFT_CP_6[3], TFT_CP_6[4], TFT_CP_6[5], 0, 0, 0, 0, true);



                //TFTA动态数据
                //               if(P_TFT_Data_3[0]!=0){
//                    comData(1, trends_data, tft_data_3, 0, P_TFT_Data_3[0], P_TFT_Data_3[1], P_TFT_Data_3[2], 0, 0, 0, 0, 0, 0, true);
//                    comData(1, trends_data, c_tfta_data, 0, P_TFT_Data_3[0], P_TFT_Data_3[1], P_TFT_Data_3[2], 0, 0, 0, 0, 0, 0, true);
//                }else{
//
//                    comData(1, trends_data, tft_data_3, 0, 0x12, 0x34, 0x56, 0, 0, 0, 0, 0, 0, true);
//                    comData(1, trends_data, c_tfta_data, 0, 0x65, 0x43, 0x21, 0, 0, 0, 0, 0, 0, true);
//                }


                comData(1, OUT_UPPER_MODE, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                Log.e("上位机命令", "TFT显示");
                my_auto_mark = -30;
                break;
            case (byte) 0x31:  //TFTB显示
                if (cs_flag == 1)
                    Voice("TFTB显示", 3, true);

                //TFTB显示车牌 主 从
//                comData(1, trends_data, tftb_cp_6, GATE_CP_6[0], GATE_CP_6[1], GATE_CP_6[2], GATE_CP_6[3], GATE_CP_6[4], GATE_CP_6[5],0, 0, 0, 0,  true);
//                comData(1, trends_data, c_tftb_cp, GATE_CP_6[0], GATE_CP_6[1], GATE_CP_6[2], GATE_CP_6[3], GATE_CP_6[4], GATE_CP_6[5],0, 0, 0, 0,  true);
                //TFTB显示数据 主 从  TFT_Data_3
//                comData(1, trends_data, tftb_data_3, 0, 0x12, 0x34, 0x56, 0, 0,0, 0, 0, 0,  true);


                comData(1, trends_data, c_tftb_data, 0, 0x65, 0x43, 0x21, 0, 0,0, 0, 0, 0,  true);
                //TFTB车牌显示

                Way.BS_CP_DISPOSE();
                comData(1, trends_data, tftb_cp_6, TFT_CP_6[0], TFT_CP_6[1], TFT_CP_6[2], TFT_CP_6[3], TFT_CP_6[4], TFT_CP_6[5], 0, 0, 0, 0, true);
//                    comData(1, trends_data, c_tftb_cp, TFT_CP_6[0], TFT_CP_6[1], TFT_CP_6[2], TFT_CP_6[3], TFT_CP_6[4], TFT_CP_6[5], 0, 0, 0, 0, true);


                //TFTB动态数据
//                if(P_TFT_Data_3[0]!=0){
//                    comData(1, trends_data, tftb_data_3, 0, P_TFT_Data_3[0], P_TFT_Data_3[1], P_TFT_Data_3[2], 0, 0, 0, 0, 0, 0, true);
//                    comData(1, trends_data, c_tftb_data, 0, P_TFT_Data_3[0], P_TFT_Data_3[1], P_TFT_Data_3[2], 0, 0, 0, 0, 0, 0, true);
//                }else{
//
//                    comData(1, trends_data, tftb_data_3, 0, 0x12, 0x34, 0x56, 0, 0, 0, 0, 0, 0, true);
//                    comData(1, trends_data, c_tftb_data, 0, 0x65, 0x43, 0x21, 0, 0, 0, 0, 0, 0, true);
//                }


                comData(1, OUT_UPPER_MODE, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                Log.e("上位机命令", "TFT显示");
                my_auto_mark = -30;
                break;
            case (byte) 0x27:  //道闸显示
                if (cs_flag == 1)
                    Voice("道闸显示", 3, true);

                Way.daozha();
                comData(1, trends_data, gate_cp_6, GATE_CP_6[0], GATE_CP_6[1], GATE_CP_6[2], GATE_CP_6[3], GATE_CP_6[4], GATE_CP_6[5], 0, 0, 0, 0, true);





//                if(P_C_GATE_CP_6[0]!=0){
//                    comData(1, trends_data, c_dz_cp, P_C_GATE_CP_6[0], P_C_GATE_CP_6[1], P_C_GATE_CP_6[2], P_C_GATE_CP_6[3], P_C_GATE_CP_6[4], P_C_GATE_CP_6[5], 0, 0, 0, 0, true);
//                }else{
//                    char[] data = "654321".toCharArray();
//                    for (int i = 0; i < data.length; i++) {
//                        GATE_CP_6[i] = data[i];
//                    }
//                    comData(1, trends_data, c_dz_cp, GATE_CP_6[0], GATE_CP_6[1], GATE_CP_6[2], GATE_CP_6[3], GATE_CP_6[4], GATE_CP_6[5], 0, 0, 0, 0, true);
//                }

//                comData(1, trends_data, gate_cp_6, GATE_CP_6[0], GATE_CP_6[1], GATE_CP_6[2], GATE_CP_6[3], GATE_CP_6[4], GATE_CP_6[5], 0, 0, 0, 0, true);
//               comData(1, trends_data, c_dz_cp, GATE_CP_6[0], GATE_CP_6[1], GATE_CP_6[2], GATE_CP_6[3], GATE_CP_6[4], GATE_CP_6[5], 0, 0, 0, 0, true);

                comData(1, OUT_UPPER_MODE, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                Log.e("上位机命令", "道闸显示");
                my_auto_mark = -30;
                break;
            case (byte) 0x28:  //预设位1
                if (cs_flag == 1)
                    Voice("预设位1", 3, true);
                MainActivity.state_camera = 11;

                comData(1, OUT_UPPER_MODE, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                Log.e("上位机命令", "预设位1");
                my_auto_mark = -30;
                break;
            case (byte) 0x29:  //预设位2
                if (cs_flag == 1)
                    Voice("预设位2", 3, true);
                MainActivity.state_camera = 12;

                comData(1, OUT_UPPER_MODE, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                Log.e("上位机命令", "预设位2");
                my_auto_mark = -30;
                break;
            case (byte) 0x30:  //预设位3
                if (cs_flag == 1)
                    Voice("预设位3", 3, true);
                MainActivity.state_camera = 13;

                comData(1, OUT_UPPER_MODE, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                Log.e("上位机命令", "预设位3");
                my_auto_mark = -30;
                break;
            case (byte) 0xa1:  //预留1
                if (cs_flag == 1)
                    Voice("预留1", 3, true);
                Way.BS_CP_DISPOSE();
                comData(1, trends_data, xz_cp, GATE_CP_6[0], GATE_CP_6[1], GATE_CP_6[2], GATE_CP_6[3], GATE_CP_6[4], GATE_CP_6[5], 0, 0, 0, 0, true);

                comData(1, trends_data, xz_cp, XZ_CP_8[0], XZ_CP_8[1], XZ_CP_8[2], XZ_CP_8[3], XZ_CP_8[4], XZ_CP_8[5], XZ_CP_8[6], XZ_CP_8[7], 0, 0, true);

                comData(1, OUT_UPPER_MODE, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                Log.e("上位机命令", "预留1");
                my_auto_mark = -30;
                break;
            case (byte) 0xa2:  //从车
                if (cs_flag == 1)
                    Voice(" 2", 3, true);

                Way.congche();

                comData(1, OUT_UPPER_MODE, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                Log.e("上位机命令", "预留2");
                my_auto_mark = -30;
                break;
            case (byte) 0xa3:  //预留3
                if (cs_flag == 1)
                    Voice("预留3", 3, true);
                comData(1,intest,0x13,0x14,0, 0, 0, 0, 0, 0, 0, 0, 0, true);

                comData(1, OUT_UPPER_MODE, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                Log.e("上位机命令", "预留3");
                my_auto_mark = -30;
                break;

            case (byte) 0xa4:
                if(cs_flag==1)
                    Voice("交通标志识别", 3, true);
                comData(1, qh_AB, bzw_A, tft_fy,d_f+1, 0, 0, 0, 0, 0, 0, 0, 0, true);
                jtbz = Discern(8, 0);

                comData(1, OUT_UPPER_MODE, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                Log.e("上位机命令", "交通标志物识别");
                my_auto_mark = -30;
                break;


            case (byte) 0xa5:
                if(cs_flag==1)
                    Voice("车型识别", 3, true);
                cxsb=null;
                for (int i=0;i<=6;i++) {
                    if (cxsb == null || cxsb.length() <= 1) {

                        comData(1, qh_AB, bzw_A, tft_fy,d_f+1, 0, 0, 0, 0, 0, 0, 0, 0, true);
                        cxsb = Discern(9, 0);
                    }else {
                        break;
                    }
                }


                comData(1, OUT_UPPER_MODE, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                Log.e("上位机命令", "车型识别");
                my_auto_mark = -30;



            case (byte) 0xe5:   //文本识别
                if(cs_flag==1)
                    cxsb = Discern(19, 0);







                comData(1, OUT_UPPER_MODE, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
                Log.e("上位机命令", "文本识别");
                my_auto_mark = -30;

            default:
                break;


        }
    }






    public void Full_Auto() {
        Task_Out();    //上位机任务执行



        //   switch (mark) {
        //        case 10:

        //               if(!P_AUTO_ZB.equals(""))
//                {
//                    String line=P_AUTO_ZB;//设置好的路线
//                    String[] rest = line.split(" ");
//                    Log.e("主车指定路线: ",Arrays.toString(rest) );
//                    for(int i=0;i<rest.length;i++){
//                                            if(i==rest.length-1)
//                                              comData(1, gate, on, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, true);
//                        Auto_Run(rest[i],true);
//                    }
//                }

//                Way.BS_beeper_HW();
//                String str =Way.BS_GRAPH_VOICE();
//                Voice(str,3,false);


        //               comData(1, trends_data, c_zigbee_data_8, 0x55,0x0a,0x74,0x68,0x00,0x00,0xdc,0xbb, 0, 0, true);
//                Way.BS_TEST();


        //      mark = -50;
        //     break;
        // comData(1,track,p_x,0,0,0,0,0,0,0,0,0,0,true);//  主车 显示 从车1测距//
        //   }
    }
}
