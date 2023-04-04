package com.example.newcar2022;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CameraCommandUtil {
    private String httpUrl1 = null;
    private String httpUrl2 = null;

    public CameraCommandUtil() {
    }

    public Bitmap httpForImage(String IP) {
        this.httpUrl1 = "http://" + IP + "/snapshot.cgi?loginuse=admin&loginpas=888888&res=0";
        Log.e("ASDASDASD", this.httpUrl1);
        URL imageUrl = null;
        Bitmap bitmap = null;

        try {
            imageUrl = new URL(this.httpUrl1);
        } catch (MalformedURLException var7) {
            var7.printStackTrace();
        }

        if(imageUrl != null) {
            try {
                HttpURLConnection e = (HttpURLConnection)imageUrl.openConnection();
                e.setDoInput(true);
                e.setConnectTimeout(2000);
                InputStream in = e.getInputStream();
                bitmap = BitmapFactory.decodeStream(in);
                in.close();
            } catch (IOException var6) {
                var6.printStackTrace();
            }
        }
        return bitmap;
    }

    public void postHttp(String IP, int command, int onestep) {
        this.httpUrl2 = "http://" + IP + "/decoder_control.cgi?loginuse=admin&loginpas=888888&command=" + command + "&onestep=" + onestep;
        Log.e("ASDASDASD", "postHttp: "+this.httpUrl2);
        URL getUrl = null;
        try {
            getUrl = new URL(this.httpUrl2);
        } catch (MalformedURLException var8) {
            var8.printStackTrace();
        }

        try {
            HttpURLConnection e = (HttpURLConnection)getUrl.openConnection();
            InputStream in = e.getInputStream();
            in.close();
        } catch (IOException var7) {
            var7.printStackTrace();
        }
    }
}
