package com.example.newcar2022;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class SearchService extends Service {
    //搜索摄像头ip类
    private SearchCameraUtil searchCameraUtil=null;
    //摄像头ͷIP
    private String IP=null;
    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        thread.start();
    }
    private Thread thread=new Thread(new Runnable() {

        public void run() {
            // TODO Auto-generated method stub

            while(IP==null||IP.equals("")){
                searchCameraUtil=new SearchCameraUtil();




                try {
                    IP=searchCameraUtil.send();
                }
                catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            handler.sendEmptyMessage(10);
        }
    });

    /////////////////
    private Handler handler=new Handler(){
        public void handleMessage(Message msg) {
            if(msg.what==10){
                Log.e("ipcamipcamipcam","  "+IP+":80");
                Intent intent=new Intent(MainActivity.A_S);
                intent.putExtra("IP", XcApplication.cameraip);
                intent.putExtra("IP", IP+":81");
                intent.putExtra("pureip",IP);
                sendBroadcast(intent);
                SearchService.this.stopSelf();
            }
        };
    };
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

}
