package com.example.newcar2022;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/*

这个类 包含 启动模式 选择 配置摄像头本地ip


 */
public class XcApplication extends Application {

    public enum Mode {
        SOCKET, SERIAL, USB_SERIAL
    }

    public  static String cameraip = "192.168.1.101:81";

    private static XcApplication app;

    public static ExecutorService executorServicetor = Executors.newCachedThreadPool();

    public static Mode isserial = Mode.SOCKET;

    public static XcApplication getApp() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        Intent sintent = new Intent();
        //ComponentName的参数1:目标app的包名,参数2:目标app的Service完整类名
        sintent.setComponent(new ComponentName("com.android.settings", "com.android.settings.ethernet.CameraInitService"));
        //设置要传送的数据
        sintent.putExtra("purecameraip","0.0.0.0");
        startService(sintent);
    }
}
