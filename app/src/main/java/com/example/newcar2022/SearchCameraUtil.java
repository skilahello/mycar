package com.example.newcar2022;

import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by dell on 2017/10/31.
 */

public class SearchCameraUtil {
    private String IP = "";
    private String TAG = "UDPClient";
    private int PORT = 3565;
    private int SERVER_PORT = 8600;
    private byte[] mbyte = new byte[]{68, 72, 1, 1};
    private DatagramSocket dSocket = null;
    private byte[] msg = new byte[1024];

    public String send() {
        InetAddress local = null;
        try {
            local = InetAddress.getByName("255.255.255.255");
           // Log.e(this.TAG, "已找到服务器,连接中...");
        } catch (UnknownHostException var7) {
           // Log.e(this.TAG, "未找到服务器.");
            var7.printStackTrace();
        }

        try {
            if(this.dSocket != null) {
                this.dSocket.close();
                this.dSocket = null;
            }

            this.dSocket = new DatagramSocket(this.PORT);
          //  Log.e(this.TAG, "正在连接服务器...");
        } catch (SocketException var6) {
            var6.printStackTrace();
          //  Log.e(this.TAG, "服务器连接失败.");
        }

        DatagramPacket sendPacket = new DatagramPacket(this.mbyte, 4, local, this.SERVER_PORT);
        DatagramPacket recPacket = new DatagramPacket(this.msg, this.msg.length);

        try {
            this.dSocket.send(sendPacket);
            this.dSocket.receive(recPacket);
            String e = new String(this.msg, 0, recPacket.getLength());
            if(e.substring(0, 2).equals("DH")) {
                this.getIP(e);
            }

            Log.e("IP值", this.IP);
            this.dSocket.close();
            Log.e(this.TAG, "消息发送成功!");
        } catch (IOException var5) {
            var5.printStackTrace();
            Log.e(this.TAG, "消息发送失败.");
        }

        return this.IP;
    }

    private void getIP(String text) {
        try {
            byte[] e = text.getBytes("UTF-8");

            for(int i = 4; i < 22 && e[i] != 0; ++i) {
                if(e[i] == 46) {
                    this.IP = this.IP + ".";
                } else {
                    this.IP = this.IP + (e[i] - 48);
                }
            }
        } catch (UnsupportedEncodingException var4) {
            var4.printStackTrace();
        }
    }
}
