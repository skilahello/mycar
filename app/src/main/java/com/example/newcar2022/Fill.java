package com.example.newcar2022;

/**
 * Created by dell on 2017/9/27.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Fill {

    private  static  final  String FILE_PATH = Environment.getExternalStorageDirectory().getPath() + "/";

    /***
     * 创建文件目录
     */
    public  static File createDir(String  dirName) throws  IOException{
        File dir = new File(FILE_PATH + dirName);
        dir.mkdirs();       //mkdir(创建单个目录) mkdirs(创建多级目录)
        return dir;
    }

    /**
     * 判断目录是否存在
     */
     public static boolean isFileExist(String fileName){
         File  file = new File(FILE_PATH + fileName);
         return file.exists();
     }


    /**
     * 保存文件
     *
     * @param fileName
     *            文件名称
     * @param content
     *            文件内容
     * @throws IOException
     */
    private  final  String  nameFile_text =  "图形识别/结果/";
    private  final  String  nameFile_tp =    "图形识别/图片/";

    public void saveToSDCard(String fileName, String content) throws IOException{
        // 考虑不同版本的sdCard目录不同，采用系统提供的API获取SD卡的目录
        if(!isFileExist(nameFile_text)){   //判断目录是否存在
            createDir(nameFile_text);      //创建目录
        }
        File file = new File(FILE_PATH + nameFile_text,
                fileName);
        if (!file.isDirectory()) {    //判断该文件是否存在
            file.createNewFile();     //创建该文件
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);  //创建过滤器
        fileOutputStream.write(content.getBytes());
        fileOutputStream.close();
    }

    /**
     * SD卡存文件
     * @param folder  文件夹
     * @param fileName 文件名
     * @param content  内容
     * @throws IOException
     */
    public static void saveToSDCard(String folder ,String fileName, String content) throws IOException{
        // 考虑不同版本的sdCard目录不同，采用系统提供的API获取SD卡的目录
        if(!isFileExist(folder)){   //判断目录是否存在
            createDir(folder);      //创建目录
        }
        File file = new File(FILE_PATH + folder,
                fileName);
        if (!file.isDirectory()) {    //判断该文件是否存在
            file.createNewFile();     //创建该文件
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);  //创建过滤器
        fileOutputStream.write(content.getBytes());
        fileOutputStream.close();
    }


    /**
     * 读取文件内容
     * @param fileName
     *            文件名称
     * @return 文件内容
     * @throws IOException
     */
    public String read(String fileName) throws IOException {
        File file = new File(FILE_PATH + nameFile_text,fileName);
        if (file.exists()) {
            FileInputStream fileInputStream = new FileInputStream(file);
            // 把每次读取的内容写入到内存中，然后从内存中获取
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            // 只要没读完，不断的读取
            while((len = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            // 得到内存中写入的所有数据
            byte[] data = outputStream.toByteArray();
            fileInputStream.close();
            return new String(data);
        } else
            return "";
    }

    /**
     * 读取文件
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String fileRead(String folder, String fileName) throws IOException {
        File file = new File(FILE_PATH + folder,fileName);
        if (file.exists()) {
            FileInputStream fileInputStream = new FileInputStream(file);
            // 把每次读取的内容写入到内存中，然后从内存中获取
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            // 只要没读完，不断的读取
            while((len = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            // 得到内存中写入的所有数据
            byte[] data = outputStream.toByteArray();
            fileInputStream.close();
            return new String(data);
        } else
            return "";
    }



    /**
     * 保存图片
     *
     * @param bitmap
     *            图片资源
     * @param strFileName
     *            图片名称
     * @throws IOException
     */
    public void savePhoto(Bitmap bitmap, String strFileName) {
        try {
            if(!isFileExist(nameFile_tp)) {   //判断目录是否存在
                createDir(nameFile_tp);      //创建目录
            }
            File file = new File(FILE_PATH + nameFile_tp,strFileName);
            if (!file.isDirectory()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            if (fos != null) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 80, fos);   //图片编码
                fos.flush();    //刷新
                fos.close();    //关闭
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void savePhoto(String folder, String strFileName,Bitmap bitmap) {
        try {
            if(!isFileExist(folder)) {   //判断目录是否存在
                createDir(folder);      //创建目录
            }
            File file = new File(FILE_PATH + folder,strFileName);
            if (!file.isDirectory()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            if (fos != null) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 80, fos);   //图片编码
                fos.flush();    //刷新
                fos.close();    //关闭
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 读取图片
     * @param strFileName
     *            图片名称
     * @return 图片内容
     * @throws IOException
     */
    @SuppressWarnings("unused")
    public static Bitmap readPhoto(String strFileName) {

        String path = FILE_PATH + "小车数据/"+strFileName;
        if (path != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            return bitmap;
        } else
            return null;
    }

    public static Bitmap readPhoto(String folder,String strFileName) {
        String path = FILE_PATH + folder + strFileName;
        if (path != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            return bitmap;
        } else
            return null;
    }

    /**
     * 保存文件
     * @param file_name  文件名
     * @param content    内容
     */
    public static void  saveFile(String file_name, String content){
        final  String  FOLDER = "小车数据/";
        try {
            saveToSDCard(FOLDER,file_name,content);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 读取文件
     * @param file_name  文件名
     */
    public static String  readFile(String file_name){
        String str = "";
        final  String  FOLDER = "小车数据/";
        try {
            str = fileRead(FOLDER,file_name);
        }catch (IOException e){
            e.printStackTrace();
        }
        return  str;
    }

    /**
     * 保存图片
     * @param file_name  文件名
     * @param bitmap    内容
     */
    public static   void  savePhoths(String file_name, Bitmap bitmap){
        final  String  FOLDER = "拍照程序/";
        savePhoto(FOLDER,file_name,bitmap);
    }

    public static   void  savePhoth(String file_name, Bitmap bitmap){
        final  String  FOLDER = "小车数据/";
        savePhoto(FOLDER,file_name,bitmap);
    }


    /********读取图片********/
    public static void readPhoth(String file_name){
        final String FOLDER =  "小车数据/";
        readPhoto(FOLDER,file_name);
    }




    /**
     * 读取文件内容
     */
    public static String carRead(String fileName) throws IOException {
        File file = new File(FILE_PATH + "小车数据/",fileName);
        if (file.exists()) {
            FileInputStream fileInputStream = new FileInputStream(file);
            // 把每次读取的内容写入到内存中，然后从内存中获取
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            // 只要没读完，不断的读取
            while((len = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            // 得到内存中写入的所有数据
            byte[] data = outputStream.toByteArray();
            fileInputStream.close();
            return new String(data);
        } else
            return "";
    }




    /*********获取摄像头图片********/
    public static Bitmap getBitmap(){
        return MainActivity.bitmap;
    }

}

