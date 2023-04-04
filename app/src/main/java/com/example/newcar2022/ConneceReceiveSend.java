package com.example.newcar2022;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class ConneceReceiveSend {
    private static final String TAG = "<<ConneceReceiveSend>>";
    private int port = 60000;
    private static DataInputStream bInputStream;
    private static DataOutputStream bOutputStream;
    private static Socket socket;
    private static byte[] rbyte = new byte[40];
    public static Handler reHandler;
    public static int qAutoFlag = 0;
    public static int ycData = 0;

    public void connect(Handler handler, String IP) {
        try {
            reHandler = handler;
            socket = new Socket(IP, port);
            bInputStream = new DataInputStream(socket.getInputStream());
            bOutputStream = new DataOutputStream(socket.getOutputStream());
            // reThread.start();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public Thread reThread = new Thread(new Runnable() {
        @Override
        public void run() {
            // TODO Auto1-generated method stub
            while (socket != null && !socket.isClosed()) {
                try {
                    bInputStream.read(rbyte);
                    //  bInputStream.;
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = rbyte;
                    reHandler.sendMessage(msg);
                    ConneceReceiveSend.Feed_Back_Handle(rbyte);
                    // receiveHandle();   //反馈数据处理
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            Log.e(TAG, "读取的线程");
        }
    });

    /*
    串口发送数据流
     */
    public static void Serial_send(final byte[] data) {

        try {
            MainActivity.sPort.write(data, 5000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * 发送数据流
     * @param data
     */
    public static void Send(final byte[] data) {
        try {
            if (socket != null && !socket.isClosed()) {
                bOutputStream.write(data, 0, data.length);
                bOutputStream.flush();
            } else {
                Log.e(TAG, "输出为空");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * 数据发送及反馈调整
     *
     * @param dpwata
     * @param flag
     */
    public static void dataSend(final byte[] dpwata, boolean flag){
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (MainActivity.usbOrwifi == 0) {
                    Send(dpwata);
                    //Log.e("发送数据",Arrays.toString(dpwata));
                } else {
                    Serial_send(dpwata); // usb  串口 发送
                }
                // Log.e(TAG, "发送线程");
            }
        }).start();
        qAutoFlag = 0;
        ycData = 0;
        if (flag) {
            while (qAutoFlag != 1) {
//                Log.e("数据接收","数据接收");
                if (ycData == 1) {
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (MainActivity.usbOrwifi == 0) {
                                Send(dpwata);
                                Log.e("重新发送数据", Arrays.toString(dpwata));
                            } else {
                                Serial_send(dpwata);  //usb  串口 发送
                            }  //100毫秒后发送数据
                        }
                    }, 100);
                    ycData = 0;
                }
            }
            qAutoFlag = 0;
        }
    }

    /***处理发送的数据***/
    public static void CodeSend(final byte[] data, boolean flag) {

        byte top_data = (byte) 0x66;    //帧头
        byte dtat_key = (byte) 0x32;    //数据密钥
        byte end_data = (byte) 0x88;    //帧尾
        int len_data = data.length;
        byte[] command = new byte[len_data + 4];
        command[0] = top_data;
        command[1] = (byte) len_data;   //数据长度
        Log.e("len: ",len_data+"" );
        for (int i = 0; i < len_data; i++) {
            command[2 + i] = (byte) (data[i]);   //数据加密
        }
        command[len_data + 2] = command[2];    //先把校验位等于第三个数据，再从第四个开始异或
        for (int i = 1; i < len_data; i++) {
            command[len_data + 2] = (byte) (command[len_data + 2] ^ command[2 + i]);   //计算校验位
        }
        command[len_data + 3] = end_data;  //帧尾
        dataSend(command, flag);  //发送数据等待执行完成

        System.out.println("11111111111开始");
        for (int i=0;i<data.length;i++)
        {
            System.out.println(data[i]);
        }

        for (int i=0; i<command.length;i++){
            System.out.println("sssssssaass"+command[i]);
        }

    }


    /***
     * 处理底层数据  的方法
     * 底层反馈数据操作
     * @param data
     */
    public static int zJlNum = 1;
    public static int cJlNum = 0;
    public static void Feed_Back_Handle(byte[] data){
        long buf = 0;
        Message mess = new Message();
        String str = " ";
        String coordinate_now = ""; //坐标
        String Direction_now = "";  //车头
        int direction_now = 0;
        //Log.e("任务完成标志位", Arrays.toString(data));
        switch (data[0]){
            case (byte)0x33:    //主车距离信息
                data[0] = 0;
                buf = data[1] & 0xff;
                buf = buf << 8;
                buf += data[2] & 0xff;
                str = buf + "";
                //z_jl = (int) buf;
                Fill.saveFile(File_Name.HOST_RANGE + zJlNum + ".txt", str);  //保存内容
                zJlNum++;
                if (zJlNum > 3)
                    zJlNum = 1;
                break; // 保存主车距离信息
            case (byte) 0xfc: // 第一次RFID读到的数据
                data[0] = 0;
                byte[] by = new byte[16];
                for (int i = 2; i < 18; i++){
                    by[i - 2] = data[i];
                }
                str = new String(by);
                str = str.trim();  //移除字符 串两边的空白字符
                Log.e("RFID-1", Arrays.toString(by));
                Fill.saveFile(File_Name.RFID_INF + data[1]+"-1.txt", str);  //保存内容
                for (int i = 1; i < 17; i++) {            //清空数据
                    by[i - 1] = 0;
                }
                break; // RFID信息
            case (byte) 0xfd: // 第二次RFID读到的数据
                data[0] = 0;
                by = new byte[16];
                for (int i = 2; i < 18; i++){
                    by[i - 2] = data[i];
                }
                str = new String(by);
                str = str.trim();  //移除字符串两边的空白字符
                Log.e("RFID-2", Arrays.toString(by));
                Fill.saveFile(File_Name.RFID_INF + data[1]+"-2.txt", str);  //保存内容
                for (int i = 1; i < 17; i++) {            //清空数据
                    by[i - 1] = 0;
                }
                break; // RFID信息
            case (byte) 0xfe: // 第三次RFID读到的数据
                data[0] = 0;
                by = new byte[16];
                for (int i = 2; i < 18; i++){
                    by[i - 2] = data[i];
                }
                str = new String(by);
                str = str.trim();  //移除字符串两边的空白字符
                Log.e("RFID-3", Arrays.toString(by));
                Fill.saveFile(File_Name.RFID_INF + data[1]+"-3.txt", str);  //保存内容
                for (int i = 1; i < 17; i++) {            //清空数据
                    by[i - 1] = 0;
                }
                break; // RFID信息

            case (byte) 0x88:   //主车光照挡位
                data[0] = 0;
                str = data[1] + "";
                Fill.saveFile(File_Name.HOST_LIGHT_GEAR, str);  //保存内容
                break;
            case (byte)0xa5: //立体车库当前层数
                data[0] = 0;
                str = data[1] + "";
                Fill.saveFile(File_Name.LTCKA_LAYER, str);  //保存内容
                break;
            case (byte)0xb5: //立体车库当前层数
                data[0] = 0;
                str = data[1] + "";
                Fill.saveFile(File_Name.LTCKB_LAYER, str);  //保存内容
                break;
            case (byte) 0x12: // 底层命令执行完成
                Log.e("应答",data[0]+"");
                data[0] = 0;
                if (data[1] == data[2]) // 验证数据
                {
                    qAutoFlag = 1; // 开启标志位

                } else if (data[1] < data[2]) {
                    ycData = 1; // 数据发送异常
                }

                break;
            case (byte) 0xdf: //从车命令执行完成//   从车底层数据接收
                data[0] = 0;
                if (data[1] != 0) {
                    str = data[1] + "";
                    Fill.saveFile(File_Name.OBEY_LIGHT_GEAR, str);  //从车光照
                }
//                Log.e("从车光照", str);
                if ((data[2] != 0) || (data[3] != 0)) {
                    buf = data[2] & 0xff;
                    buf = buf << 8;
                    buf += data[3] & 0xff;
                    str = buf + "";
                    Fill.saveFile(File_Name.OBEY_RANGE, str);  //从车距离
//                    Log.e("从车距离", "距离");
                }
                if (data[4] != 0) {
                    str =data[4] + "";
                    Log.e("从车语音",str);
                    Fill.saveFile(File_Name.C_VOICE_SB, str);  //从车语音识别结果
                }
                qAutoFlag = 1; // 开启标志位
                Log.e("从车距离", str);
                break;
            case (byte) 0xcf:  //从车二维码信息
                data[0] = 0;
                char[] dat_buf = new char[data[1]];
                for (int index = 0; index < data[1]; index++) {
                    dat_buf[index] = (char) data[2+index];
                }
                str = String.valueOf(dat_buf);
                Fill.saveFile(File_Name.C_QR_CODE,str);
                Log.e("从车反馈二维码数据",  str);
                break;
            case (byte)0x38: //接收白卡坐标
                data[0] = 0;
                Direction_now = "";
                try {
                    coordinate_now = (char) (data[1] + 0x40) + "";
                    coordinate_now += (char) (data[2] + 0x30);
                    Log.e("白卡坐标", coordinate_now);
                    str = coordinate_now ;
                    Fill.saveFile(File_Name.RFID_COORDINATE+data[3]+".txt", str);  //保存内容
                    mess.what = 18;
                    mess.obj = coordinate_now + Direction_now;
                    reHandler.sendMessage(mess);
                }catch (Exception e){
                    Log.e( "白卡坐标保存 ","异常" );
                }
                break;
            case (byte)0x45:
                data[0] = 0;
                try {
                    coordinate_now = (char) (data[1] ) + "";
                    coordinate_now += (char) (data[2]);
                    Log.e("坐标", coordinate_now);
                    direction_now = data[3];
                    switch (direction_now) {
                        case 0:
                            Direction_now = "U";
                            break;
                        case 1:
                            Direction_now = "L";
                            break;
                        case 2:
                            Direction_now = "D";
                            break;
                        case 3:
                            Direction_now = "R";
                            break;
                    }
                    mess.what = 18;
                    mess.obj = coordinate_now + Direction_now;
                    reHandler.sendMessage(mess);
                }catch (Exception e){
                    Log.e( "主车坐标保存 ","异常" );
                }
                break;
            case (byte)0x39:   //特殊路段反馈坐标
                data[0] = 0;
                Direction_now = "";
                try {
                    coordinate_now = (char) (data[1] + 0x40) + "";
                    coordinate_now += (char) (data[2] + 0x30);
                    Log.e("特殊路段坐标", coordinate_now);
                    direction_now = data[3];
                    switch (direction_now) {
                        case 0:
                            Direction_now = "U";
                            break;
                        case 1:
                            Direction_now = "L";
                            break;
                        case 2:
                            Direction_now = "D";
                            break;
                        case 3:
                            Direction_now = "R";
                            break;
                    }
                    str = coordinate_now + Direction_now;
                    Fill.saveFile(File_Name.TSLD_COORDINATE, str);  //保存内容
                }catch (Exception e){
                    Log.e( "特殊路段坐标保存 ","异常" );
                }
                break;
            case (byte)0x6f:   //上位机操作
                data[0] = 0;
                ControlCommand.my_auto_mark = data[1];    //操作上位机指令
                Log.e("上位机操作", "my_auto_mark" + ControlCommand.my_auto_mark + " " + " data" + data[1]);
                break;
            case (byte)0xa4:   //语音识别结果保存
                data[0] = 0;
                str= data[1] +"";
                Fill.saveFile(File_Name.VOICE_SB, str);  //保存内容
                Log.e("语音识别结果返回", "");
                break;
            case (byte)0xd1:    //预留动态接收
//                data[0] = 0;
//                for (int index = 1; index < data.length; index++) {
//                    str += data[index] & 0xff;
                    data[0] = 0;
                    byte[] bys = new byte[32];
                    for (int i = 2; i < 33; i++){
                        bys[i] = data[i];
                    }
                    str = new String(bys);
                    str = str.trim();  //移除字符 串两边的空白字符
                    Log.e("RFID-1", Arrays.toString(bys));
                    Fill.saveFile(File_Name.RFID_INF + data[1]+"-1.txt", str);  //保存内容
                    for (int i = 1; i < 17; i++) {            //清空数据
                        bys[i - 1] = 0;
                    }
//                Fill.saveFile(File_Name.DT_Data,str);


//                char[] dat_buf = new char[data[1]];
//                for (int index = 0; index < data[1]; index++) {
//                    dat_buf[index] = (char) data[2+index];
//                }
//                str = String.valueOf(dat_buf);
                break;
            default:
                break;
        }
    }

}
