package com.example.newcar2022;

import android.app.Activity;
import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class PlateRecognition extends Activity{


    public static final String ApplicationDir = "hyperlpr";
    public static final String cascade_filename = "cascade.xml";
    public static final String finemapping_prototxt = "HorizonalFinemapping.prototxt";
    public static final String finemapping_caffemodel = "HorizonalFinemapping.caffemodel";
    public static final String segmentation_prototxt = "Segmentation.prototxt";
    public static final String segmentation_caffemodel = "Segmentation.caffemodel";
    public static final String character_prototxt = "CharacterRecognization.prototxt";
    public static final String character_caffemodel= "CharacterRecognization.caffemodel";





    /***
     * 将模型文件复制在SD卡
     * @param context  当前内容
     * @param assetDir 文件
     * @param dir       目录
     */
    public static void CopyAssets(Context context, String assetDir, String dir) {
        String[] files;
        try {
            // 获得Assets一共有几多文件
            files = context.getAssets().list(assetDir);
        } catch (IOException e1) {
            return;
        }
        File mWorkingPath = new File(dir);
        // 如果文件路径不存在
        if (!mWorkingPath.exists()) {
            // 创建文件夹
            if (!mWorkingPath.mkdirs()) {
                // 文件夹创建不成功时调用
            }
        }

        for (int i = 0; i < files.length; i++) {
            try {
                // 获得每个文件的名字
                String fileName = files[i];
                // 根据路径判断是文件夹还是文件
                if (!fileName.contains(".")) {
                    if (0 == assetDir.length()) {
                        CopyAssets(context,fileName, dir + fileName + "/");
                    } else {
                        CopyAssets(context,assetDir + "/" + fileName, dir + "/" + fileName + "/");
                    }
                    continue;
                }
                File outFile = new File(mWorkingPath, fileName);
                if (outFile.exists())
                    continue;
                InputStream in = null;
                if (0 != assetDir.length())
                    in = context.getAssets().open(assetDir + "/" + fileName);
                else
                    in = context.getAssets().open(fileName);
                OutputStream out = new FileOutputStream(outFile);

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
