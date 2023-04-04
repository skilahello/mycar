package com.example.newcar2022;

import android.util.Log;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by JIA on 2018/5/16.
 */

public class Way {
    /**********************比赛的方法************************/
    /**
     * TFT显示数据           将数据转换为16进制数。·
     * TFT显示车牌           将数据转换为char型。      （char=string.tocharArry）
     *  byte 数据类型是8位、有符号的，以二进制补码表示的整数； 最小值是 -128（-2^7）；最大值是 127（2^7-1）；默认值是 0；
     *             String data =  new Fill().carRead(File_Name.FIGURE_FRUIT); // 图形识别结果
     *             String data =  new Fill().carRead(File_Name.HOST_RANGE+ 1 + ".txt"); // 主车距离  数字代表距离信息次数
     *             String data =  new Fill().carRead(File_Name.OBEY_RANGE); // 从车测距距离
     *             String data =  new Fill().carRead(File_Name.QR_CODE+ 1 + ".txt"); // 主车二维码  数字代表二维码信息次数
     *             String data =  new Fill().carRead(File_Name.C_QR_CODE); // 从车二维码
     *             String data =  new Fill().carRead(File_Name.RFID_INF+"1-1.txt"); // R8ID 1     2-1.txt  3-1.txt
     *             String data =  new Fill().carRead(File_Name.PLATE_FRUIT); // 车牌识别结果
     *             String data =  new Fill().carRead(File_Name.OBEY_LIGHT_GEAR); // 从车光照挡位
     *             String data =  new Fill().carRead(File_Name.HOST_LIGHT_GEAR); // 主车光照挡位
     *             String data =  new Fill().carRead(File_Name.LTCKA_LAYER); // 立体车库A层数  1 2 3 4
     *             String data =  new Fill().carRead(File_Name.LTCKB_LAYER); // 立体车库B层数
     *             String rfid_data =  new Fill().carRead(File_Name.RFID_COORDINATE+"1.txt"); // 白卡坐标
     *             String data =  new Fill().carRead(File_Name.VOICE_SB); // 主车语音识别结果
     *             String data =  new Fill().carRead(File_Name.C_VOICE_SB); // 从车语音识别结果
     */
    /*********************************************************/
    public static String C_ONE_QR_DATA = "";  //从车第一次二维码数据
    public static String ONE_CP_DATA = "";  //第一次车牌数据
    public static String OPppp="";   //
    private static String s;
    private static int numse;
    private static int ops;
    private static int numrenwu7;

    /*************储存从车第一次二维码数据****************/
    //位与（&）
    //位或(|)
    //位异或（^）
    //(~)位非

    //a1=Integer.valueOf(datas.substring(0,2),16); 16进制方法
    //arr[i]=arr[i].substring(0,2);位置查找
    //String []arr=jie.split(",");//分割
    // String jie=jiequ.replaceAll("[|]",","); //替换
    //String  jiequ=s.substring(s.indexOf("<")+1,s.indexOf(">")); //截取
    //1、 与：要求所有人都投票同意，才能通过某议题
    //
    //2、 或：只要求一个人投票同意就可以通过某议题
    //
    //3、 非：某人原本投票同意，通过非运算符，可以使其投票无效
    //
    //4、 异或：有且只能有一个人投票同意，才可以通过某议题


    //正则表达式String s=sb1.replaceAll("[^a-zA-Z0-9]","");//
    //String buffer接受数组的值用append方法 再用一个String 接受to String
    //String转int 包装类Integer ,intvalue();
    //判断含有某个值boolean statusde = b[m].contains(c);  //判断含有某个值
    //不四舍五入Math.round（）；
    //Java 中的取余运算是 %，而取模运算是 Math.floorMod()
    //String toLowerCase()：转换为小写字母
    //      String toUpperCase()：转换为大写字母

    private static int find(String  a,String  array[])
    {                                      //查找
        String temp = "";
        int io=0;
        for (int i=0;i< array.length;i++){
            if(array[i].equals(a)){
                io=i;
            }
        }
        return io;
    }


    //插入
    private static String[] adshuzu(String[] arrgs,int start,String nom) {

        int index = start;
        String value = nom;
        String[] newArray = new String[arrgs.length + 1];
        for (int i = 0; i < arrgs.length; i++) {
            newArray[i] = arrgs[i];
        }
        for (int i = newArray.length - 1; i > index; i--) {
            newArray[i] = newArray[i - 1];
        }
        newArray[index] = value;
        arrgs = newArray;
        return arrgs;
    }

    //逆置
    private static String reverse(String string, int startIndex, int endIndex)
    {
        if (startIndex >= endIndex)
            return string;

// swap char at startIndex and endIndex
        char characterAtEnd = string.charAt(endIndex);

        if ((endIndex + 1) < string.length())
            string = string.substring(0, endIndex) +
                    string.charAt(startIndex) +
                    string.substring(endIndex + 1, string.length());
        else
            string = string.substring(0, endIndex) + string.charAt(startIndex);


        string = string.substring(0, startIndex) +
                characterAtEnd +
                string.substring(startIndex + 1, string.length());


        return reverse(string, ++startIndex, --endIndex);
    }



    public static int autorukit(){

        //车库

        int sum=0;
        try {
            String data =  new Fill().carRead(File_Name.OBEY_LIGHT_GEAR);
            Integer i=new Integer(data);
            int num=i.intValue();
            sum=num%3;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sum;
    }


    public static void C_ONE_QR_SAVE(){
        try{
            String data =  new Fill().carRead(File_Name.C_QR_CODE); // 从车二维码
            C_ONE_QR_DATA = data;
            Log.e( "C_ONE_QR_SAVE: ", data);
        }catch (Exception e){
            Log.e( "C_ONE_QR_SAVE: ", "异常");
        }
    }

    public static void ONE_CP_SAVE(){
        try{
            String data =  new Fill().carRead(File_Name.PLATE_FRUIT); // 车牌识别结果
            ONE_CP_DATA = data;
            Log.e( "ONE_CP_SAVE: ", data);
        }catch (Exception e){
            Log.e( "ONE_CP_SAVE: ", "异常");
        }
    }

    public static  void Bs_beernnid(){
        String sumer=null;
        String pre="0";
        char a='0';
        try {
            String data =  new Fill().carRead(File_Name.RFID_INF+"1-1.txt");
            sumer=data;
            sumer.toCharArray();
            for (int i=0;i<sumer.length();i++)
            {
                a=sumer.charAt(i);
                pre+=a;

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*************报警器红外码****************/
    public static void BS_beeper_HW(){
//        Log.e( "BS_beeper_HW: ", "111");
//        try{
//            String data =  new Fill().carRead(File_Name.C_QR_CODE); // 从车二维码
//            C_ONE_QR_SAVE();
////            String data =  new Fill().carRead(File_Name.RFID_INF+"2-1.txt); // RFID 2
////            String data =  new Fill().carRead(File_Name.RFID_INF+"3-1.txt); // RFID 3
////            String data =  new Fill().carRead(File_Name.QR_CODE+ 1 + ".txt"); // 主车二维码  数字代表二维码信息次数
//            Log.e( "BS_beeper_HW: ", data);
//            String[] Data = data.split("-");
//            System.out.println(Arrays.toString(Data));
//            String q_daxie = "";
//            String q_xiaoxie = "";
//            String h_daxie = "";
//            String h_xiaoxie = "";
//            String q = "";
//            String h = "";
//            if (Data[0].charAt(0) >= 'A' && Data[0].charAt(0) <= 'Z'){
//                q_daxie = Data[0].substring(0,6);
//                q_xiaoxie = Data[0].substring(6);
//            }else{
//                q_xiaoxie = Data[0].substring(0,6);
//                q_daxie = Data[0].substring(6);
//            }
//            if (Data[1].charAt(0) >= 'A' && Data[1].charAt(0) <= 'Z'){
//                h_daxie = Data[1].substring(0,6);
//                h_xiaoxie = Data[1].substring(6);
//            }else{
//                h_xiaoxie = Data[1].substring(0,6);
//                h_daxie = Data[1].substring(6);
//            }
//            System.out.println(q_daxie);
//            System.out.println(q_xiaoxie);
//            System.out.println(h_daxie);
//            System.out.println(h_xiaoxie);
//            for (int i = 0; i < 6; i+=2) {
//                q += q_daxie.substring(i,i+2);
//                q += h_xiaoxie.substring(i,i+2);
//            }
//            for (int i = 0; i < 6; i+=2) {
//                h += h_daxie.substring(i,i+2);
//                h += q_xiaoxie.substring(i,i+2);
//            }
//            System.out.println(q);
//            System.out.println(h);
//            char[] qq = q.toCharArray();
//            char[] hh = h.toCharArray();
//            char[] jj = new char[q.length()];
//            for (int i = 0; i < q.length(); i++) {
//                if(qq[i] >= 'a' && qq[i]<='z'){
//                    jj[i] = (char) (qq[i]-'a'+1 + hh[i]-1 -32);
//                }else if(qq[i] >= 'A' && qq[i] <= 'Z'){
//                    jj[i] = (char) (qq[i] - 'A'+1 + hh[i]);
//                }else {
//                    jj[i] = (char) (qq[i] - '0' + hh[i]);
//                    if (jj[i] > '9'){
//                        jj[i] = (char) (jj[i]-10);
//                        jj[i-1] = (char) (jj[i-1]+1);
//                    }
//                }
//            }
//            int[] rest = new int[6];
//            String str = new String(jj);
//            System.out.println(str);
//            System.out.println(Arrays.toString(jj));
//            int aaaa = 0;
//            for (int i = 0; i < jj.length-1; i++) {
//                if (jj[i] >= 'A' && jj[i] <= 'F')
//                {
//                    rest[aaaa] = Integer.valueOf(jj[i]+""+jj[i+1],16);
//                    aaaa += 1;
//                }else if(jj[i] >= 'G' && jj[i] <= 'Z'){
//                    rest[aaaa] = Integer.valueOf(jj[i]+"");
//                    aaaa += 1;
//                }
//
//            }

//            System.out.println(Arrays.toString(rest));
//            ControlCommand.HW_Data_6[0] = rest[0];
//            ControlCommand.HW_Data_6[1] = rest[1];
//            ControlCommand.HW_Data_6[2] = rest[2];
//            ControlCommand.HW_Data_6[3] = rest[3];
//            ControlCommand.HW_Data_6[4] = rest[4];
//            ControlCommand.HW_Data_6[5] = rest[5];
//            ControlCommand.HW_Data_6[0] = Integer.valueOf(a+"",16);
//            ControlCommand.HW_Data_6[1] = Integer.valueOf(b+"",16);
//            ControlCommand.HW_Data_6[2] = Integer.valueOf(c+"",16);
//            ControlCommand.HW_Data_6[3] = Integer.valueOf(d+"",16);
//            ControlCommand.HW_Data_6[4] = Integer.valueOf(e+"",16);
//            ControlCommand.HW_Data_6[5] = Integer.valueOf(f+"",16);


//            //	//十进制转成十六进制：
//            //	Integer.toHexString(int i)
//        }catch (Exception e){
//            Log.e( "BS_beeper_HW: ", "异常");
//        }


        String sb1="*******<***01&&**3|**&&B**&&2|**&&C3|D*&^%4|E&%%^*5|F*&(^6C>,<osd32434*&$*$&>";
        String s=sb1.replaceAll("[^<>|a-zA-Z0-9]","");

        String data=s;

        String  jiequ=s.substring(s.indexOf("<")+1,s.indexOf(">"));

        String jie=jiequ.replaceAll("[|]",",");

        String []arr=jie.split(",");

        String opi="";
        String pppooo="";
        int a1,a2,a3,a4,a5,a6;
        for (int i=0;i<arr.length;i++){
            arr[i]=arr[i].substring(0,2);
            ControlCommand.HW_Data_6[i]=Integer.valueOf(arr[i].substring(0,2),16);
//            pppooo+=arr[i];
        }
//        a1=Integer.valueOf(pppooo.substring(0,2),16);
//        a2=Integer.valueOf(pppooo.substring(2,4),16);
//        a3=Integer.valueOf(pppooo.substring(4,6),16);
//        a4=Integer.valueOf(pppooo.substring(6,8),16);
//        a5=Integer.valueOf(pppooo.substring(8,10),16);
//        a6=Integer.valueOf(pppooo.substring(10,12),16);
//
//            ControlCommand.HW_Data_6[0] = a1;
//            ControlCommand.HW_Data_6[1] = a2;
//            ControlCommand.HW_Data_6[2] = a3;
//            ControlCommand.HW_Data_6[3] = a4;
//            ControlCommand.HW_Data_6[4] = a5;
//            ControlCommand.HW_Data_6[5] = a6;
    }




    /*************白卡扇区****************/
    public static int  BS_RFID_SQ(){
        int RFID_SQ = 0;
        try{
            String data =  new Fill().carRead(File_Name.HOST_LIGHT_GEAR); // 主车光照挡位
            int num = BS_DIMMING()-4;
            RFID_SQ = num + Integer.valueOf(data);
            //	//十进制转成十六进制：
            //	Integer.toHexString(int i)

        }catch (Exception e){
            Log.e( "BS_RFID_SQ: ", "异常");
        }
        return RFID_SQ;
    }


    public static int guangzhao(){
        int sum=0;


        try {
            String data =  new Fill().carRead(File_Name.HOST_LIGHT_GEAR);
            Integer integer=new Integer(data);
            sum=integer;
        } catch (IOException e) {
            e.printStackTrace();
        }


        return sum;
    }

    public static int basyunt(){
        numrenwu7 = 0;
        String s="";
        try {
            String data =  new Fill().carRead(File_Name.RFID_INF+"1-2.txt");
            String datas =  new Fill().carRead(File_Name.RFID_INF+"2-2.txt");

            boolean statusde = datas.contains("^");
            boolean statusdes = datas.contains(".");
            if (statusde && statusdes && datas.length()<=16){
                String jiequ=datas.substring(datas.indexOf("^")+1,datas.indexOf("."));
                s=jiequ.replaceAll("[^0-9]","");
                Integer integer=new Integer(s);
                numrenwu7 =integer;

            }

            boolean statusded = data.contains("^");
            boolean statusdesd = data.contains(".");
            if (statusded && statusdesd && data.length()<=16){
                String jiequ=data.substring(data.indexOf("^")+1,data.indexOf("."));
                s=jiequ.replaceAll("[^0-9]","");
                Integer integer=new Integer(s);
                numrenwu7 =integer;

            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return numrenwu7;
    }

    //调光
    public static  int bsunt(){

        int  sum=0;
        try {
            String daota=new Fill().carRead(File_Name.QR_CODE+"2.txt");

            char a=daota.charAt(10);
            String str=String.valueOf(a);
            Integer i=new Integer(str);
            sum=i.intValue();

            sum=sum%4+1;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return sum;
    }

    /*************调光挡位****************/
    public static int BS_DIMMING(){
        int num = 0;
        try{
            String data =  new Fill().carRead(File_Name.RFID_INF+"1-1.txt"); // RFID 1     2-1.txt  3-1.txt

            String sss = data.replaceAll("\\D","");
            char[] ss = sss.toCharArray();
            Arrays.sort(ss);
            String fff = data.replaceAll("[^+\\-*/]","");
            int zuida = 0,zuixiao = 0,last = 0;
            if (fff.charAt(0) == '*'){
                zuida = (ss[ss.length-1]-48) * (ss[ss.length-2]-48);
                zuixiao = (ss[0]-48) * (ss[1]-48);
            }else if (fff.charAt(0) == '-'){
                zuida = (ss[ss.length-1]-48) - (ss[0]-48);
                zuixiao = (ss[0]-48) - (ss[ss.length-1]-48);
            }else if (fff.charAt(0) == '/'){
                zuida = (ss[ss.length-1]-48) / (ss[0]-48);
                zuixiao = (ss[0]-48) * (ss[ss.length-1]-48);
            }
            last = zuida - zuixiao;
            num = last %4+1;

//            System.out.println(Arrays.toString(a));
            Log.e( "BS_DIMMING: ", num+" ");
        }catch (Exception e){
            Log.e( "BS_DIMMING: ", "异常");
        }
        return num+4;
    }


    public static void BS_Tu_xing(){
        ControlCommand controlCommand=new ControlCommand();
        try {
            String data =  new Fill().carRead(File_Name.CarRES_txsb); // 图形识别结果
            String datas =  new Fill().carRead(File_Name.Car_jtbz);
            int resooo=0;

            if (datas.equals("jinzhitongxing") ) {   //"jinzhitongxing","jinzhizhixing","zhixing","diaotou","zuozhuan","youzhuan"
                resooo=0x05;
            }else if(datas.equals("zhixing")){
                resooo=0x01;
            }else if (datas.equals("diaotou")){
                resooo=0x04;
            }else if (datas.equals("zuozhuan")){
                resooo=0x02;
            }else if (datas.equals("youzhuan")){
                resooo=0x03;
            }else if (datas.equals("jinzhizhixing")){
                resooo=0x05;
            }
            String A="A"+graph_get(data,0,"五角形");
            String B="B"+graph_get(data,0,"三角形");
            String C="C"+graph_get(data,0,"圆形");

//            String D="D"+graph_get(data,0,"菱形");
//            String E="E"+graph_get(data,0,"矩形");
//            String G="A3";
//            String B="D2";
//            String E="E2";

            String str=A+B+C;

          //  char c[]=str.toCharArray();


        //    String R="A"+graph_get()

            //         String R = "A" + graph_get(data,1,"菱形");
//            String G = "B" + graph_get(data,3,"全部");
//            String Y = "C" + graph_get(data,4,"全部");
//            String str = R+G+Y;     //获取信息

            int a1,a2,a3;
            a1=Integer.valueOf(str.substring(0,2),16);
            a2=Integer.valueOf(str.substring(2,4),16);
            a3=Integer.valueOf(str.substring(4,6),16);


            ControlCommand.TFT_Data_3[0]=a1;   //TFT显示
            ControlCommand.TFT_Data_3[1]=a2;
            ControlCommand.TFT_Data_3[2]=a3;
     //       ControlCommand.TFT_Data_3[3]=a4;
   //         ControlCommand.TFT_Data_3[4]=a5;



            controlCommand.comData(1,ControlCommand.intest,a1,a2,a3,resooo,0,0,0,0,0,0,0,true);



//            String h="F"+graph_get(data,1,"全部");
//            String g="F"+graph_get(data,2,"全部");
//            String b="F"+graph_get(data,3,"全部");

//            String h="F2";
//            String g="F2";
//            String b="F4";
////
//            String res=h+g+b;
////            String datas="F1F2F3";
//
//            int a1s,a2s,a3s;
//            a1s=Integer.valueOf(res.substring(0,2),16);            //LED显示
//            a2s=Integer.valueOf(res.substring(2,4),16);
//            a3s=Integer.valueOf(res.substring(4,6),16);
//            controlCommand.comData(1,ControlCommand.trends_data,ControlCommand.smg_data,2,a1s,a2s,a3s,0,0,0,0,0,0,true);

    //        int tft_num1 = Integer.valueOf(TFT_text.substring(0,2),16);
     //       int tft_num2 = Integer.valueOf(TFT_text.substring(2,4),16);
     //       int tft_num3 = Integer.valueOf(TFT_text.substring(4,6),16);



        } catch (IOException e) {
            Log.e( "tuxing: ", "异常");
        }


    }


    public static int BS_Tu_xings() {

        int sum=0;

        try {
            String data =  new Fill().carRead(File_Name.FIGURE_FRUIT); // 图形识别结果
            sum=graph_get(data,1,"全部");
        } catch (IOException e) {
            e.printStackTrace();
        }


        return sum;
    }

    /*************立体车库层数A****************/
    public static int BS_3D_CK_A(){
        int num = 1;
        try{
            String data =  new Fill().carRead(File_Name.C_QR_CODE); // 从车二维码
            data = data.replaceAll("\\D","");

            char[] Data = data.toCharArray();
            Arrays.sort(Data);
            num = (Data[Data.length-1]-Data[0]);
            System.out.println(Arrays.toString(Data));
            if(num > 3){
                num = num%3;
            }
            if(num == 0){
                num = 1;
            }
        }catch (Exception e){
            Log.e( "BS_3D_CK: ", "异常");
        }
        return num;
    }

    /*************立体车库层数B****************/
    public static int BS_3D_CK_B(){
        int num = 1;
        int z=0;
        int k=0;
        int es=0;
//        try{
//            String data =  new Fill().carRead(File_Name.C_QR_CODE); // 从车二维码
//            String data3 =  new Fill().carRead(File_Name.OBEY_LIGHT_GEAR); // 从车光照挡位
//            int c = Integer.valueOf(data3);
//
//            String data2 = "";
//            char[] aa = data.toCharArray();
//            for (int i = 0; i < data.length(); i++) {
//                data2 += String.valueOf(aa[i]);
//                if(i == data.length()-1) break;
//                if (aa[i] >=48 &&aa[i] <= 57){
//                    if (aa[i+1] == 40){
//                        data2 += "*";
//                    }
//                }
//            }
//            int d = DataProcessing.comput1(data2);
//            num = d%c+1;
//
//        }catch (Exception e){
//            Log.e( "BS_3D_CK: ", "异常");
//        }

        try {
            String data =  new Fill().carRead(File_Name.LTCKB_LAYER);
            String datas =  new Fill().carRead(File_Name.FIGURE_FRUIT); // 图形识别结果
            es=graph_get(datas,0,"五角星");

            Integer integer=new Integer(data);
         //   z=integer;
            z=2;





        } catch (IOException e) {
            e.printStackTrace();
        }


        k=numrenwu7;

        num=((k+es)^z)%4+1;
        return num;
    }


    /*************主车车库坐标****************/
    public static String BS_ZC_CKCOORD(){

        String ZC_CKCOORD = "";
        try{

            String data =  new Fill().carRead(File_Name.HOST_LIGHT_GEAR); // 主车光照挡位
//            String data =  new Fill().carRead(File_Name.QR_CODE+ 1 + ".txt"); // 主车二维码  数字代表二维码信息次数
            ZC_CKCOORD=data;
        }catch (Exception e){
            Log.e( "BS_CC_COORD: ", "异常");
        }
        return ZC_CKCOORD;
    }






    /*************从车车库坐标****************/
    public static String BS_CC_CKCOORD(){

        String CC_CKCOORD = "B7U";
        try{
            String data =  new Fill().carRead(File_Name.C_QR_CODE); // 从车二维码
            String[] s = data.split("<");
            String ss = s[s.length-1];
            System.out.println(ss);
            for (int i = 0; i < ss.length()-1; i++) {
                if (ss.charAt(i) >= 65 && ss.charAt(i) <= 71){
                    if(ss.charAt(i+1) >= 49 && ss.charAt(i+1) <= 55){
                        CC_CKCOORD = ss.substring(i,i+2)+"U";
                        break;
                    }
                }
            }
            System.out.println("从车入库"+CC_CKCOORD);

        }catch (Exception e){
            Log.e( "BS_CC_COORD: ", "异常");
        }
        return CC_CKCOORD;
    }





    /*************从车起始坐标****************/
    public static String BS_CC_QSCOORD(){

        String CC_QSCOORD = "G2D";
        try{
            String data =  new Fill().carRead(File_Name.RFID_INF); // RFID 1
            String data2 =  new Fill().carRead(File_Name.RFID_INF); // RFID 2
//            String data =  new Fill().carRead(File_Name.RFID_INF3); // RFID 3
//            String data =  new Fill().carRead(File_Name.HOST_LIGHT_GEAR); // 主车光照挡位
//            String data =  new Fill().carRead(File_Name.QR_CODE+ 1 + ".txt"); // 主车二维码  数字代表二维码信息次数

            String str = data.replaceAll("[^A-Z]","").substring(0,1); //获取信息
            int last = Integer.parseInt(data.substring(data.length()-2,data.length()-1));
            int frist = Integer.parseInt(data.substring(0,1));
            int str1 = (last % frist);

            if(str.equals("F")) CC_QSCOORD = "G2R" ;
            else if (str1 == 1) CC_QSCOORD = "G2D";
            else if(str1 == 3) CC_QSCOORD = "G2U";
            else  if(str1 == 2) CC_QSCOORD = "G2L";

        }catch (Exception e){
            Log.e( "BS_CC_COORD: ", "异常");
        }
        return CC_QSCOORD;
    }

    /*************从车指定路线****************/
    public static String BS_CC_LXCOORD(){

        String CC_LXCOORD = "";
        try{
            String data =  new Fill().carRead(File_Name.QR_CODE+ 2 + ".txt"); // 主车二维码  数字代表二维码信息次数
            data = data.replaceAll("[^ABCDEF1234567]","");
            System.out.println(data);
            char[] aa = data.toCharArray();
            for (int i = 0; i < aa.length-1; i++) {
                if (aa[i] >= 65 && aa[i] <= 71){
                    if (aa[i+1] >= 49 && aa[i+1] <= 55){
                        CC_LXCOORD += String.valueOf(aa[i])+String.valueOf(aa[i+1]) +"U ";
                    }
                }
            }
            Log.e( "CC_LXCOORD: ", CC_LXCOORD);
        }catch (Exception e){
            Log.e( "CC_LXCOORD: ", "异常");
        }
        return CC_LXCOORD;
    }

    /*************图形结果LED显示****************/
    public static void BS_GRAPH_DISPOSE(){

        try{

            String juli =  new Fill().carRead(File_Name.HOST_RANGE+ 1 + ".txt");
            int JL=Integer.valueOf(juli);
            ControlCommand.SMG_Data_3[0]=JL;


//            String data =  new Fill().carRead(File_Name.FIGURE_FRUIT); // 图形识别结果
//            String rfid_data =  new Fill().carRead(File_Name.RFID_COORDINATE+"1.txt"); // 白卡坐标
//            String data =  new Fill().carRead(File_Name.C_QR_CODE); // 从车二维码
//            int a1,a2,a3;
//            a1=Integer.valueOf(data.substring(0,2),16);            //LED显示
//            a2=Integer.valueOf(data.substring(2,4),16);
//            a3=Integer.valueOf(data.substring(4,6),16);
//
//            ControlCommand.SMG_Data_3[0]=a1;
//            ControlCommand.SMG_Data_3[1]=a2;
//            ControlCommand.SMG_Data_3[2]=a3;

       //     String R="A"+graph_get()

   //         String R = "A" + graph_get(data,1,"菱形");
//            String G = "B" + graph_get(data,3,"全部");
//            String Y = "C" + graph_get(data,4,"全部");
//            String str = R+G+Y;     //获取信息
//
//            char []c = str.toCharArray();
//            ControlCommand.XZ_CP_8[0]=c[0];     //旋转LED显示
//            ControlCommand.XZ_CP_8[1]=c[1];
//            ControlCommand.XZ_CP_8[2]=c[2];
//            ControlCommand.XZ_CP_8[3]=c[3];
//            ControlCommand.XZ_CP_8[4]=c[4];
//            ControlCommand.XZ_CP_8[5]=c[5];
//            ControlCommand.XZ_CP_8[6]=0;
//            ControlCommand.XZ_CP_8[7]=0;

//            int a1,a2,a3;
//            a1=Integer.valueOf(str.substring(0,2),16);            //LED显示
//            a2=Integer.valueOf(str.substring(2,4),16);
//            a3=Integer.valueOf(str.substring(4,6),16);
//
//            ControlCommand.SMG_Data_3[0]=a1;
//            ControlCommand.SMG_Data_3[1]=a2;
//            ControlCommand.SMG_Data_3[2]=a3;
        }catch (Exception e){
            Log.e( "BS_GRAPH_DISPOSE: ", "异常");
        }

    }




    /*************图形结果语音播报****************/
    public static String BS_GRAPH_VOICE(){
        String str = "";     //获取信息
        try{
//            String data =  new Fill().carRead(File_Name.FIGURE_FRUIT); // 图形识别结果
//            String data =  new Fill().carRead(File_Name.HOST_RANGE+ 1 + ".txt"); // 主车距离  数字代表距离信息次数
//            String data =  new Fill().carRead(File_Name.OBEY_RANGE); // 从车测距距离
//            String data =  new Fill().carRead(File_Name.QR_CODE+ 1 + ".txt"); // 主车二维码  数字代表二维码信息次数
//            String data =  new Fill().carRead(File_Name.C_QR_CODE); // 从车二维码
//            String data =  new Fill().carRead(File_Name.RFID_INF+"1-1.txt"); // RFID 1     2-1.txt  3-1.txt
            String data =  new Fill().carRead(File_Name.PLATE_FRUIT); // 车牌识别结果
//            String data =  new Fill().carRead(File_Name.OBEY_LIGHT_GEAR); // 从车光照挡位
//            String data =  new Fill().carRead(File_Name.HOST_LIGHT_GEAR); // 主车光照挡位
//            String data =  new Fill().carRead(File_Name.LTCKA_LAYER); // 立体车库A层数  1 2 3 4
//            String data =  new Fill().carRead(File_Name.LTCKB_LAYER); // 立体车库B层数
//            String data =  new Fill().carRead(File_Name.VOICE_SB); // 主车语音识别结果
//            String data =  new Fill().carRead(File_Name.C_VOICE_SB); // 从车语音识别结果
            str = data;
            Log.e( "BS_GRAPH_DISPOSE: ", data.substring(2));
        }catch (Exception e){
            Log.e( "BS_GRAPH_DISPOSE: ", "异常");
        }
        return str;
    }


    /*************车牌预处理****************/
    public static int BS_CP_YCL(){
        int rest = 0;
        try{
            String data =  new Fill().carRead(File_Name.C_QR_CODE); // 从车二维码
            String[] array = data.split("\\D");
//            System.out.println(Arrays.toString(array));
            int num = 0;
            for (int i = 0; i <= array.length-1; i++) {
//                System.out.println(array[i].length());
                if (array[i].length() == 3){
                    if(Integer.valueOf(array[i]) > 200)
                        rest = 1;
                    else rest = 0;
                    break;
                }
            }
        }catch (Exception e){
            Log.e( "BS_CP_YCL: ", "异常");
        }
        return rest;
    }


    public static String fenghuotai(){
        String feng="";

        int  q=erweimaer()&0xFFFF;
        int qs=q&0xFF00<<2;   //高
        int qe=q&0x00FF;   //低

        int oppp= basyunt();  //r
        int oppps=oppp&0xFF00<<2;   //高
        int opppsd=oppp&0x00FF;   //低


        String daou= null;
        try {
            daou = new Fill().carRead(File_Name.HOST_LIGHT_GEAR); //n
            String cheku=new Fill().carRead(File_Name. LTCKA_LAYER); //y

            Integer io =new Integer(cheku);
            int nus=io.intValue();
            int wan=nus;


            Integer is =new Integer(daou);
            int num=is.intValue();
            int nuu=num;

            //低  //n
            numse = num&0x00FF;

            //低   //y
            ops = nus&0x00FF;



        } catch (IOException e) {
            e.printStackTrace();
        }

        String popo=qs+qe+oppps+opppsd+numse+ops+"";


        return feng=popo;


    }



    public static void erweimashi() {
        ControlCommand controlCommand=new ControlCommand();
        String iooo="";
        try {
            String data=new Fill().carRead(File_Name.QR_CODE+"1.txt");
            data=data.replaceAll("[^A-Z0-9]","");

            iooo= data;




        } catch (IOException e) {
            e.printStackTrace();
        }





    }



    public static int fenghuot(){

        int suo=0;

        String dters= null;
        try {
            dters = new Fill().carRead(File_Name.QR_CODE+"1.txt");
            s = dters.replaceAll("[a-zA-Z0-9]","");



        } catch (IOException e) {
            e.printStackTrace();
        }



        return suo;
    }



    public static int erweimaer(){  //二维码
        int x=0;

        int oppp= basyunt();  //r
        try {
            String daou= new Fill().carRead(File_Name.HOST_LIGHT_GEAR);  //n
            String cheku=new Fill().carRead(File_Name. LTCKA_LAYER); //y

            Integer io =new Integer(cheku);
            int nus=io.intValue();
            int wan=nus;

            int sums=1;

            Integer is =new Integer(daou);
            int num=is.intValue();
            int nuu=num;

            int sss=wan*nuu+oppp;

            for (int i1=0;i1<4;i1++)
            {
                sums*=sss;
            }
            x=sums/100;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return x;
    }

    //主车
    public static int  cheku(){

        int oopppss=0;
        try {
            String daou= new Fill().carRead(File_Name.LTCKB_LAYER);  //z
            String cheksu=new Fill().carRead(File_Name. LTCKA_LAYER); //y
            String daous= new Fill().carRead(File_Name.HOST_LIGHT_GEAR);  //n

            Integer is =new Integer(daous);//daous
            int num=is.intValue();

            Integer ifs =new Integer(daou);  //z
            int nums=ifs.intValue();

            Integer ifss =new Integer(cheksu);  //z
            int numse=ifss.intValue();

            int upo=nums+numse;

            int ooooo=1;

            for (int iop=0;iop<num;iop++)
            {
                ooooo*=upo;
            }
            int sumesa=ooooo%4+1;

            oopppss=sumesa;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return oopppss;

    }


    public static void chekuzhu(){
        ControlCommand controlCommand=new ControlCommand();
        try {
            String data=new Fill().carRead(File_Name.JTD_FRUIT);

            switch (data)
            {
                case "红色":
                    controlCommand.Auto_Run("B2U",true);
                case "绿色":
                    controlCommand.Auto_Run("F2U",true);
                case "黄色":
                    controlCommand.Auto_Run("D2U",true);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void daozha(){


        String data= null;
        try {
            data = new Fill().carRead(File_Name.PLATE_FRUIT);

        char[] c = data.toCharArray();

        ControlCommand.GATE_CP_6[0]=c[0];    //道闸显示
        ControlCommand.GATE_CP_6[1]=c[1];
        ControlCommand.GATE_CP_6[2]=c[2];
        ControlCommand.GATE_CP_6[3]=c[3];
        ControlCommand.GATE_CP_6[4]=c[4];
        ControlCommand.GATE_CP_6[5]=c[5];
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void litixian(){

        try {
            String dters=new Fill().carRead(File_Name.C_QR_CODE);

            s = dters.replaceAll("[a-zA-Z0-9]","");
            ControlCommand.XZ_CP_8[0]= s.charAt(0);
            ControlCommand.XZ_CP_8[1]= s.charAt(1);
            ControlCommand.XZ_CP_8[2]= s.charAt(2);
            ControlCommand.XZ_CP_8[3]= s.charAt(3);
            ControlCommand.XZ_CP_8[4]= s.charAt(4);
            ControlCommand.XZ_CP_8[5]= s.charAt(5);

    //        String ous=new Fill().carRead(File_Name.)
    //        ControlCommand.XZ_CP_8[6]=.charAt(0);
     //       ControlCommand.XZ_CP_8[7]=s1.charAt(1);
            ControlCommand.XZ_CP_8[6]=OPppp.charAt(7);
            ControlCommand.XZ_CP_8[7]=OPppp.charAt(8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void congche(){

        ControlCommand controlCommand=new ControlCommand();
        String ous= null;
        try {
            ous = new Fill().carRead(File_Name.RFID_INF+"1-1.txt");
            String ss=ous;
            boolean statusd = ss.contains("B4");//判断含有某个值

            if(statusd){
                String oupp =ss.substring(2,4);
                String osui=ous.substring(7,9);
                osui+="U";

                controlCommand.C_Z_B_Init("F1U", true);
                controlCommand.C_Auto_Run(osui, true);

            }else{
                String ouus = new Fill().carRead(File_Name.RFID_INF+"2-1.txt");
                boolean oup=ouus.contains("B4");

                if(oup){
                    String oii=ouus.substring(2,4);
                    String osui=ous.substring(7,9);
                    osui+="U";

                    controlCommand.C_Z_B_Init("F1U", true);
                    controlCommand.C_Auto_Run(osui, true);


                } else{
                    String ouusi = new Fill().carRead(File_Name.RFID_INF+"3-1.txt");
                    boolean oups=ouusi.contains("B4");
                    if(oups){
                        String oiis=ouusi.substring(2,4);
                        String osui=ous.substring(7,9);
                        osui+="U";

                        controlCommand.C_Z_B_Init("F1U", true);
                        controlCommand.C_Auto_Run(osui, true);


                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void ousy(){
        ControlCommand controlCommand=new ControlCommand();
        String ous= null;
        try {
            ous = new Fill().carRead(File_Name.RFID_INF+"1-1.txt");
            String ss=ous;
            boolean statusd = ss.contains("B4");//判断含有某个值

            if(statusd){
                String oupp =ss.substring(2,4);
                controlCommand.C_Z_B_Init("B4", true);
                controlCommand.C_Auto_Run(oupp, true);
                OPppp=ous;
            }else{
              String ouus = new Fill().carRead(File_Name.RFID_INF+"2-1.txt");
              boolean oup=ouus.contains("B4");

              if(oup){
                  String oii=ouus.substring(2,4);
                  controlCommand.C_Z_B_Init("B4", true);
                  controlCommand.C_Auto_Run(oii, true);
                  OPppp=ouus;

              } else{
                  String ouusi = new Fill().carRead(File_Name.RFID_INF+"3-1.txt");
                  boolean oups=ouusi.contains("B4");
                  if(oups){
                      String oiis=ouusi.substring(2,4);
                      controlCommand.C_Z_B_Init("B4", true);
                      controlCommand.C_Auto_Run(oiis, true);
                      OPppp=ouusi;

                  }
              }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    /*************立体显示****************/
    public static void BS_LTXS_DISPOSE(){
        try{

            String data=new Fill().carRead(File_Name.RFID_COORDINATE+"1.txt");///白卡坐标
            String daota=new Fill().carRead(File_Name.QR_CODE+"1.txt");
            String aaa=new Fill().carRead(File_Name.HOST_RANGE+"1.txt");



            Integer i =new Integer(aaa);
            int num=i.intValue();
            num=num/10;

            String s1=""+num;



      //      String data =  new Fill().carRead(File_Name.OBEY_RANGE); // 从车测距距离
     //       String rfid_data =  new Fill().carRead(File_Name.RFID_COORDINATE+"1.txt"); // 白卡坐标

            ControlCommand.XZ_CP_8[0]=data.charAt(0);     //旋转LED显示
            ControlCommand.XZ_CP_8[1]=data.charAt(1);

            ControlCommand.XZ_CP_8[2]=daota.charAt(3);
            ControlCommand.XZ_CP_8[3]=daota.charAt(4);
            ControlCommand.XZ_CP_8[4]=daota.charAt(6);
            ControlCommand.XZ_CP_8[5]=daota.charAt(10);

       //     ControlCommand.XZ_CP_8[6]='|';
            ControlCommand.XZ_CP_8[6]=s1.charAt(0);
            ControlCommand.XZ_CP_8[7]=s1.charAt(1);



        }catch (Exception e){
            Log.e( "BS_GRAPH_DISPOSE: ", "异常");
        }
    }




    /*************车牌显示****************/
    public static void BS_CP_DISPOSE(){
        try{
//            String  str =  new Fill().carRead(File_Name.PLATE_FRUIT);
//            Log.e("读取的车牌",str);

            String data =  new Fill().carRead(File_Name.PLATE_FRUIT); // 车牌识别结果
            char[] c = data.toCharArray();

            ControlCommand.GATE_CP_6[0]=c[0];    //道闸显示
            ControlCommand.GATE_CP_6[1]=c[1];
            ControlCommand.GATE_CP_6[2]=c[2];
            ControlCommand.GATE_CP_6[3]=c[3];
            ControlCommand.GATE_CP_6[4]=c[4];
            ControlCommand.GATE_CP_6[5]=c[5];

            ControlCommand.TFT_CP_6[0]=c[0];   //TFT显示
            ControlCommand.TFT_CP_6[1]=c[1];
            ControlCommand.TFT_CP_6[2]=c[2];
            ControlCommand.TFT_CP_6[3]=c[3];
            ControlCommand.TFT_CP_6[4]=c[4];
            ControlCommand.TFT_CP_6[5]=c[5];
//            ControlCommand.TFT_CP_6[6]=0;
//            ControlCommand.TFT_CP_6[7]=0;

            ControlCommand.XZ_CP_8[0]=c[1];     //旋转LED显示
            ControlCommand.XZ_CP_8[1]=c[2];
            ControlCommand.XZ_CP_8[2]=c[3];
            ControlCommand.XZ_CP_8[3]=c[4];
            ControlCommand.XZ_CP_8[4]=c[5];
            ControlCommand.XZ_CP_8[5]=c[6];
   //         ControlCommand.XZ_CP_8[6]=c[6];
   //         ControlCommand.XZ_CP_8[7]=c[7];


            try {
                String datas=new Fill().carRead(File_Name.HOST_RANGE+ 1 + ".txt");
                Integer i=new Integer(datas);
                int nums=i.intValue();
                nums=nums/10;
                String rest=nums+"";
                char a[]=rest.toCharArray();
      //          ControlCommand. XZ_CP_8[6]=a[6];
                ControlCommand. XZ_CP_8[6]=a[0];
                ControlCommand. XZ_CP_8[7]=a[1];
            } catch (IOException e) {
                e.printStackTrace();
            }
//
//            ControlCommand.XZ_CP_8[6]=1;                //6，7 为坐标信息
//            ControlCommand.XZ_CP_8[7]=2;


        }catch (Exception e){
            Log.e( "BS_GRAPH_DISPOSE: ", "异常");
        }
    }


    /*************单独测试****************/
    public static void BS_TEST(){
        try{
            double[][] key3 = {{6,24,1},{13,16,10},{20,17,15}};
            double[][] key4 = { {8, 6, 9, 5}, {6, 9, 5, 10}, {5, 8, 4, 9}, {10, 6, 11, 4} };
            String miwen = DataProcessing.Hill_encrypt("BKRC",key4);
            Log.e( "BS_TEST_密文: ", miwen);
            String mingwen = DataProcessing.Hill_decode4(miwen,key4);
            Log.e( "BS_TEST_明文: ", mingwen);

            Log.e( "BS_TEST-: ", "/*****************Base64****************/");
            String Base64_miwen = DataProcessing.encode("123asd");
            Log.e( "BS_TEST_密文: ", Base64_miwen);
            String Base64_mingwen = DataProcessing.decode((Base64_miwen));
            Log.e( "BS_TEST_明文: ", Base64_mingwen);

            Log.e( "BS_TEST-: ", "/*****************DES****************/");
            System.out.println("----------------------start--------------------");
            DataProcessing.DesEncrypt des = new DataProcessing.DesEncrypt();//实例化一个对像
            des.getKey("1234567");//生成密匙
            String strEnc = des.getEncString("123456srthdsfhadf");//加密字符串,返回String的密文
            System.out.println(strEnc);//打印加密后的信息
            String strDes = des.getDesString(strEnc);//把String 类型的密文解密
            System.out.println(strDes);//解密后的信息
            //new DesEncrypt();
            System.out.println("----------------------end--------------------");

        }catch (Exception e){
            Log.e( "BS_TEST: ", "异常");
        }
    }




    /*********************************************************/
    /*********************************************************/



    /**********************要使用的方法************************/
    /**
     * TFT显示数据           将数据转换为16进制数。
     * TFT显示车牌           将数据转换为char型。      （char=string.tocharArry）
     *  byte 数据类型是8位、有符号的，以二进制补码表示的整数； 最小值是 -128（-2^7）；最大值是 127（2^7-1）；默认值是 0；
     *
     *
     */
    /*********************************************************/


    /****主车红外数据4位****/
    public void Get_ZC_HW_4()
    {
        try{
            String str = "";     //获取信息
            int a1=0, a2=0, a3=0, a4=0;
            ControlCommand.HW_Data_4[0]=a1;
            ControlCommand.HW_Data_4[1]=a2;
            ControlCommand.HW_Data_4[2]=a3;
            ControlCommand.HW_Data_4[3]=a4;
        }catch (Exception e){
            Log.e("Get数据: ","主车红外4位数据出错" );
        }
    }



    /****从车红外数据4位****/
    public void Get_CC_HW_4()
    {
        try{
            String str = "";     //获取信息
            int a1=0, a2=0, a3=0, a4=0;
            ControlCommand.C_HW_Data_4[0]=a1;
            ControlCommand.C_HW_Data_4[1]=a2;
            ControlCommand.C_HW_Data_4[2]=a3;
            ControlCommand.C_HW_Data_4[3]=a4;
        }catch (Exception e){
            Log.e("Get数据: ","从车红外4位数据出错" );
        }
    }



    /***主车红外数据6位****/
    public void Get_ZC_HW_6()
    {
        try{
            String str = "";     //获取信息
            int a1=0, a2=0, a3=0, a4=0, a5=0, a6=0;
            ControlCommand.HW_Data_6[0]=a1;
            ControlCommand.HW_Data_6[1]=a2;
            ControlCommand.HW_Data_6[2]=a3;
            ControlCommand.HW_Data_6[3]=a4;
            ControlCommand.HW_Data_6[4]=a5;
            ControlCommand.HW_Data_6[5]=a6;
        }catch (Exception e){
            Log.e("Get数据: ","主车红外6位数据出错" );
        }
    }



    /***从车红外数据6位****/
    public void Get_CC_HW_6()
    {
        try{
            String str = "";     //获取信息
            int a1=0, a2=0, a3=0, a4=0, a5=0, a6=0;
            ControlCommand.C_HW_Data_6[0]=a1;
            ControlCommand.C_HW_Data_6[1]=a2;
            ControlCommand.C_HW_Data_6[2]=a3;
            ControlCommand.C_HW_Data_6[3]=a4;
            ControlCommand.C_HW_Data_6[4]=a5;
            ControlCommand.C_HW_Data_6[5]=a6;
        }catch (Exception e){
            Log.e("Get数据: ","从车红外6位数据出错" );
        }
    }




    /***白卡密钥****/
    public void Get_RFID_MY()
    {
        try{

        }catch (Exception e){
            Log.e("Get数据: ","白卡密钥数据出错" );
        }
    }




    /***旋转LED车牌数据***/
    public void Get_XZ_CP()
    {
        try{
            String  str =  new Fill().carRead(File_Name.PLATE_FRUIT);
            String data = str.substring(1);
            char[] c= data.toUpperCase().toCharArray(); //小写转化大写;
            ControlCommand.XZ_CP_8[0]=c[0];
            ControlCommand.XZ_CP_8[1]=c[1];
            ControlCommand.XZ_CP_8[2]=c[2];
            ControlCommand.XZ_CP_8[3]=c[3];
            ControlCommand.XZ_CP_8[4]=c[4];
            ControlCommand.XZ_CP_8[5]=c[5];

            ControlCommand.XZ_CP_8[6]=1;                //6，7 为坐标信息
            ControlCommand.XZ_CP_8[7]=2;
            Log.e("旋转LED车牌", c[0]+","+c[1]+","+c[2]+","+c[3]+","+c[4]+","+c[5]);
        }catch (Exception e){
            Log.e("Get数据: ","旋转LED车牌数据出错" );
        }
    }



    /***道闸车牌数据***/
    public static void Get_DZ_CP()
    {

        try{
            String  str =  new Fill().carRead(File_Name.PLATE_FRUIT);
            String data = str.substring(1);
            char[] c= data.toUpperCase().toCharArray(); //小写转化大写;
            ControlCommand.GATE_CP_6[0]=c[0];
            ControlCommand.GATE_CP_6[1]=c[1];
            ControlCommand.GATE_CP_6[2]=c[2];
            ControlCommand.GATE_CP_6[3]=c[3];
            ControlCommand.GATE_CP_6[4]=c[4];
            ControlCommand.GATE_CP_6[5]=c[5];
        }catch (Exception e){
            Log.e("Get数据: ","道闸车牌数据出错" );
        }
//        Log.e("旋转LED车牌", c[0]+","+c[1]+","+c[2]+","+c[3]+","+c[4]+","+c[5]);
    }



    /***TFT车牌数据***/
    public void Get_TFT_CP()
    {

        try{
            String  str =  new Fill().carRead(File_Name.PLATE_FRUIT);
            String data = str.substring(1);
            char[] c= " ".toUpperCase().toCharArray(); //小写转化大写;
            ControlCommand.TFT_CP_6[0]=c[0];
            ControlCommand.TFT_CP_6[1]=c[1];
            ControlCommand.TFT_CP_6[2]=c[2];
            ControlCommand.TFT_CP_6[3]=c[3];
            ControlCommand.TFT_CP_6[4]=c[4];
            ControlCommand.TFT_CP_6[5]=c[5];
            Log.e("TFT车牌", c[0]+","+c[1]+","+c[2]+","+c[3]+","+c[4]+","+c[5]);
        }catch (Exception e){
            Log.e("Get数据: ","TFT车牌数据出错" );
        }
    }



    /*** TFT数据3位***/
    public void Get_TFT_Data()
    {
        try{
            int a1=0, a2=0, a3=0;
            String s="";
            for(int i=0;i<16;i++)
            {
                if(s.length()<16)
                {
                    s = s + "0";
                }

                else
                    break;
            }
            a1=Integer.valueOf(s.substring(0,2),16);
            a2=Integer.valueOf(s.substring(2,4),16);
            a3=Integer.valueOf(s.substring(4,6),16);

            ControlCommand.SMG_Data_3[0]=a1;
            ControlCommand.SMG_Data_3[1]=a2;
            ControlCommand.SMG_Data_3[2]=a3;
            Log.e("TFT", a1+","+a2+","+a3);
        }catch (Exception e){
            Log.e("Get数据: ","TFT3位数据出错" );
        }
    }

    /****zigbee数据8位****/
    public static void Get_Zigbee()
    {
        try {
            String data = C_ONE_QR_DATA; // 从车二维码

//            String data =  new Fill().carRead(File_Name.RFID_INF+"2-1.txt); // RFID 2
//            String data =  new Fill().carRead(File_Name.RFID_INF+"3-1.txt); // RFID 3
//            String data =  new Fill().carRead(File_Name.QR_CODE+ 1 + ".txt"); // 主车二维码  数字代表二维码信息次数
            Log.e( "BS_beeper_HW: ", data);
            String[] Data = data.split("-");
            System.out.println(Arrays.toString(Data));
            String q_daxie = "";
            String q_xiaoxie = "";
            String h_daxie = "";
            String h_xiaoxie = "";
            String q = "";
            String h = "";
            if (Data[0].charAt(0) >= 'A' && Data[0].charAt(0) <= 'Z'){
                q_daxie = Data[0].substring(0,6);
                q_xiaoxie = Data[0].substring(6);
            }else{
                q_xiaoxie = Data[0].substring(0,6);
                q_daxie = Data[0].substring(6);
            }
            if (Data[1].charAt(0) >= 'A' && Data[1].charAt(0) <= 'Z'){
                h_daxie = Data[1].substring(0,6);
                h_xiaoxie = Data[1].substring(6);
            }else{
                h_xiaoxie = Data[1].substring(0,6);
                h_daxie = Data[1].substring(6);
            }
            System.out.println(q_daxie);
            System.out.println(q_xiaoxie);
            System.out.println(h_daxie);
            System.out.println(h_xiaoxie);
            for (int i = 0; i < 6; i+=2) {
                q += q_daxie.substring(i,i+2);
                q += h_xiaoxie.substring(i,i+2);
            }
            for (int i = 0; i < 6; i+=2) {
                h += h_daxie.substring(i,i+2);
                h += q_xiaoxie.substring(i,i+2);
            }
            System.out.println(q);
            System.out.println(h);
            char[] qq = q.toCharArray();
            char[] hh = h.toCharArray();
            char[] jj = new char[q.length()];
            for (int i = 0; i < q.length(); i++) {
                if(qq[i] >= 'a' && qq[i]<='z'){
                    jj[i] = (char) (qq[i]-'a'+1 + hh[i]-1 -32);
                }else if(qq[i] >= 'A' && qq[i] <= 'Z'){
                    jj[i] = (char) (qq[i] - 'A'+1 + hh[i]);
                }else {
                    jj[i] = (char) (qq[i] - '0' + hh[i]);
                    if (jj[i] > '9'){
                        jj[i] = (char) (jj[i]-10);
                        jj[i-1] = (char) (jj[i-1]+1);
                    }
                }
            }
            int[] rest = new int[6];
            String str = new String(jj);
            System.out.println(str);
            System.out.println(Arrays.toString(jj));
            int aaaa = 0;
            for (int i = 0; i < jj.length-1; i++) {
                if (jj[i] >= 'A' && jj[i] <= 'F')
                {
                    rest[aaaa] = Integer.valueOf(jj[i]+""+jj[i+1],16);
                    aaaa += 1;
                }else if(jj[i] >= 'G' && jj[i] <= 'Z'){
                    rest[aaaa] = Integer.valueOf(jj[i]+"");
                    aaaa += 1;
                }

            }

            System.out.println(Arrays.toString(rest));

            int a1=0, a2=0, a3=0, a4=0, a5=0, a6=0, a7=0, a8=0;



            ControlCommand.Zig_Data_8[0]=0x55;
            ControlCommand.Zig_Data_8[1]=0x0a;
            ControlCommand.Zig_Data_8[2]=rest[1];
            ControlCommand.Zig_Data_8[3]=rest[2];
            ControlCommand.Zig_Data_8[4]=rest[3];
            ControlCommand.Zig_Data_8[5]=rest[4];
            ControlCommand.Zig_Data_8[6]=(rest[1]+rest[2]+rest[3]+rest[4])%256;
            ControlCommand.Zig_Data_8[7]=0xbb;
        }catch (Exception e){
            Log.e("Get数据: ","Zigbee8位数据出错" );
        }
    }


    /***自动行走****/
    public void Get_Auto_Data(char c1,int a1,char c2)
    {
//        ControlCommand.AUTO_Data_6[0]=c1;
//        ControlCommand.AUTO_Data_6[1]=a1;
//        ControlCommand.AUTO_Data_6[2]=get_rfid_zb();
//        ControlCommand.AUTO_Data_6[3]=get_ewm2();
//        ControlCommand.AUTO_Data_6[4]=c2;
//        ControlCommand.AUTO_Data_6[5]=che_tou(get_rfid_zb()+""+get_ewm2());
    }

    /***数码管三位数据***/
    public void Get_SMG_Data()
    {
        try {
            int a1=0, a2=0, a3=0;
            String s="";
            for(int i=0;i<16;i++)
            {
                if(s.length()<16)
                {
                    s = s + "0";
                }
                else
                    break;
            }
            a1=Integer.valueOf(s.substring(0,2),16);
            a2=Integer.valueOf(s.substring(2,4),16);
            a3=Integer.valueOf(s.substring(4,6),16);

            ControlCommand.SMG_Data_3[0]=a1;
            ControlCommand.SMG_Data_3[1]=a2;
            ControlCommand.SMG_Data_3[2]=a3;
            Log.e("a", a1+","+a2+","+a3);
        }catch (Exception e){
            Log.e("Get数据: ","数码管3位数据出错" );
        }
    }


    /**
     * 提取RFID信息  mum 第几次白卡信息
     * */
    public static String GetRFID( int num)
    {

        String s="";
        switch (num)
        {
            case 1: s = File_Name.RFID_INF;break;
            case 2: s = File_Name.RFID_INF;break;
            case 3: s = File_Name.RFID_INF;break;
        }
        String str="";
        try {
            str = new Fill().carRead(s);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("Get数据: ","GetRFID数据出错" );
        }
        if(str.equals(""))
        {
            str = "A6";
        }
        Log.e("GetRFID: ",str );
        return str;
    }


    /****白卡扇区****/
    public int Get_RFID_SQ(String a)
    {
        int num = 0;
        try{
            String str = "";                    //提取信息
            String data = a.substring(1);
            String[] shuzi = data.split("\\D");
            for(int i = 0;i < shuzi.length;i++) //提取第二个数字
            {
                str += shuzi[i];
            }
            num = Integer.parseInt(str.substring(str.length()-1));
        }catch (Exception e){
            Log.e("Get数据: ","白卡扇区数据出错" );
        }

        return num;
    }



    /****主车二维码****/
    public String Get_ZC_QR_Code()
    {
        String data = "";
        try {
            data =  new Fill().carRead(File_Name.QR_CODE+ 1 + ".txt"); // 主车二维码  数字代表二维码信息次数
        }catch (Exception e){
            Log.e("Get数据: ","主车二维码数据出错" );
        }
        return data;
    }


    /****从车二维码****/
    public String Get_CC_QR_Code()
    {
        String data = "";
        try {
            String  str = new Fill().carRead(File_Name.C_QR_CODE);

        }catch (Exception e){
            Log.e("Get数据: ","从车二维码数据出错" );
        }
        return data;
    }




    /****主车光照****/
    public int Get_ZC_DW()
    {
        int a = 0;
        try {
            String  str = new Fill().carRead(File_Name.HOST_LIGHT_GEAR);
        }catch (Exception e){
            Log.e("Get数据: ","主车光照数据出错" );
        }
        return a;
    }


    /****从车光照****/
    public int Get_CC_DW()
    {
        int a = 0;
        try {
            String  str = new Fill().carRead(File_Name.OBEY_LIGHT_GEAR);
        }catch (Exception e){
            Log.e("Get数据: ","从车光照数据出错" );
        }
        return a;
    }



    /****主车距离****/
    public int Get_ZC_JL(int num)
    {
        int a = 0;
        try {
            String  str = new Fill().carRead(File_Name.HOST_RANGE + num +".txt");
            a = Integer.parseInt(str);
        }catch (Exception e){
            Log.e("Get数据: ","主车距离数据出错" );
        }
        return a;
    }




    /****从车距离****/
    public int Get_CC_JL()
    {
        int a = 0;
        try {
            String  str = new Fill().carRead(File_Name.OBEY_RANGE);
            a = Integer.parseInt(str);
        }catch (Exception e){
            Log.e("Get数据: ","从车距离数据出错" );
        }
        return a;
    }





    /****交通灯****/
    public static int Get_traffic_ligh()
    {
        int num = 0;
        try{
            String  str = new Fill().carRead(File_Name.JTD_FRUIT);
            if(str.equals("红色")) num = 1;
            else if (str.equals("黄色")) num = 2;
            else if (str.equals("绿色")) num = 3;
        }catch (Exception e){

        }
        return num;
    }



    /****主车车库****/
    public static String Get_ZC_CK()
    {
        String  str = "B7U";
        try {
            String data =  new Fill().carRead(File_Name.RFID_INF+"1-1.txt");
            data = data.replaceAll("\\D","");
            System.out.println(data);
            int c,d;
            c = data.charAt(data.length()-2 )- 48;
            d = data.charAt(data.length()-1 )- 48;
            int sq = BS_RFID_SQ();
            int num = (c*d-d)%sq;
            if(num == 0){
                str = "B7U";
            }else {
                str = "D1U";
            }
        }catch (Exception e){

        }

        return str;
    }



    /****从车起始位置****/
    public String Get_CC_Start()
    {
        String  str = "";
        try {

        }catch (Exception e){

        }
        return str;
    }




    /****从车车库****/
    public String Get_CC_CK()
    {
        String  str = "";
        try {

        }catch (Exception e){

        }
        return str;
    }


    /****从车指定路线行驶****/
    public static String[] Set_CC_Line()
    {
        String str = "";
        String[] rest = null;

        try {
                rest = str.split(" ");
            Log.e("Set_CC_Line: ",Arrays.toString(rest) );
        }catch (Exception e){

        }
        return rest;
    }


    /****主车指定路线行驶****/
    public static String[] Set_ZC_Line()
    {
        String[] rest = null;
        try {
            String data =  new Fill().carRead(File_Name.RFID_INF+"2-1.txt"); // RFID 2     2-1.txt  3-1.txt
            char[] dd = data.toCharArray();
            String rest2 = "";
            for (int i = 0; i <= dd.length-2; i++) {
                if (dd[i] >= 65 && dd[i] <= 90){
                    if (dd[i+1] > 48 && dd[i+1] <= 55){
                        rest2 += String.valueOf(dd[i]) + String.valueOf(dd[i+1]) + "U0 ";
                    }
                }
            }
            rest = rest2.split(" ");
            Log.e("Set_ZC_Line: ",Arrays.toString(rest) );
        }catch (Exception e){

        }
        return rest;
    }



    /**********************************************************************/
    /**********************************************************************/
    /**********************************************************************/
    /**********************************************************************/




    /**图形识别信息提取方法*/
    public String graph_tq()
    {
        String str = "";
        try{

            str  = new Fill().carRead(File_Name.FIGURE_FRUIT);        //读取图形识别信息

        }catch (Exception e){

        }
        return str;
    }


    /**车牌识别信息提取方法*/
    public String cp_tq()
    {
        String str = "";
        try{

            str = new Fill().carRead(File_Name.PLATE_FRUIT);        //读取车牌信息
            str = str.substring(2);
        }catch (Exception e){

        }
        return str;
    }








    /**rfid扇区提取方法*/
    public int rfid_sq_tq()
    {

        return 0;
    }


    /**交通灯提取方法*/
    public static String traffic_light_tq()
    {
        String str = "";
        try{
            str = new Fill().carRead(File_Name.JTD_FRUIT);

        }catch (Exception e){

        }
        return str;
    }





    /***提取RFID坐标信息***/
    public static String get_rkzb()
    {
        String str="";
        try {
            str = new Fill().carRead(File_Name.RFID_COORDINATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(str.equals(""))
        {
            str="22";
        }
        String s1=str.substring(0, 2);
//		char[] ch=s1.toCharArray();
        Log.e("坐标信息", s1);
        return s1;
    }



    /*****************车牌任意提取********************
     ************ data 车牌提取信息*******************
     ************ mode 1是提取数字，2是提取字母 *******
     ************ num 第几位  ************************/
    public static String random_getcp(String data,int mode,int num)
    {
        String str = "";
//        String data = "";
        try {
//            data = new Fill().carRead(File_Name.PLATE_FRUIT);        //读取车牌信息
            switch (mode)           //1是提取数字，2是提取字母
            {
                case 1:
                    str = num_getcp(data,num);
                    break;

                case 2:
                    str = zm_getcp(data,num);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }


    public static String num_getcp(String cp,int num) {         //获取车牌任意位置数字
        String str = "";
        try{
            String data = cp.substring(1);
            String shuzi = data.replaceAll("\\D","");
            str = shuzi.substring(num-1, num);
        }
        catch (Exception e){
        }
        return str;
    }

    public static String zm_getcp(String cp,int num) {          //获取车牌任意位置字母
        String str = "";
        try{
            String data = cp.substring(1).toUpperCase();
            String zm = data.replaceAll("\\d","");
            str = zm.substring(num-1, num);
        }
        catch (Exception e){
        }
        return str;
    }






/************************END*************************/



    /***************图形识别任意提取*********************
     * ********** strdata 图形信息**********************
     ************ colour 要提取的颜色 0全部，1红色，2绿色
     ****************** 3蓝色，4黄色，5紫色，6浅蓝，7黑色
     ************ shape 形状  例如“三角形”“全部”****/
    public static int graph_get(String strdata,int colour,String shape)
    {
        int num = 0;
        String graph = "";
        try {
//            String strdata = new Fill().carRead(File_Name.FIGURE_FRUIT);        //读取图形识别信息
            switch (colour){
                case 0:   //全部颜色,shape形状个数
                    num = graph_get_allshape(strdata,shape);
                    break;
                case 1:
                    graph = graph_get_colour(strdata,"红色");
                    num = graph_get_shape(graph,shape);
                    break;
                case 2:
                    graph = graph_get_colour(strdata,"绿色");
                    num = graph_get_shape(graph,shape);
                    break;
                case 3:
                    graph = graph_get_colour(strdata,"蓝色");
                    num = graph_get_shape(graph,shape);
                    break;
                case 4:
                    graph = graph_get_colour(strdata,"黄色");
                    num = graph_get_shape(graph,shape);
                    break;
                case 5:
                    graph = graph_get_colour(strdata,"品色");
                    num = graph_get_shape(graph,shape);
                    break;
                case 6:
                    graph = graph_get_colour(strdata,"青色");
                    num = graph_get_shape(graph,shape);
                    break;
                case 7:
                    graph = graph_get_colour(strdata,"黑色");
                    num = graph_get_shape(graph,shape);
                    break;

                case 8:
                    graph = graph_get_colour(strdata,"白色");
                    num = graph_get_shape(graph,shape);
                    break;

            }
        }catch (Exception e){

        }
        return num;
    }





    /**************获取全部颜色任意形状个数**************/
    /*********** graph 提取结果 shape 形状**************/
    public static int graph_get_allshape(String graph,String shape) {         //获取全部颜色任意形状个数
        String str = "";
        int num = 0;
        try{
            if(shape.equals("全部")){
                num = graph_get_num(graph);
            }
            else{
                graph = graph.replaceAll(shape, "A");
//	        	System.out.println(graph);
                String[] data = graph.split("A");
                for (int i = 1; i < data.length; i++) {
//	            	System.out.println(data[i]);
                    num += Integer.parseInt(data[i].substring(0, 1));
                }
            }
        }
        catch (Exception e){
        }
        return num;
    }



/******************获取任意单一颜色结果*******************/
    /*********** graph 图形识别结果 colour 颜色**************/
    public static String graph_get_colour(String graph,String colour) {         //获取任意单一颜色结果
        String str = "";
        try{
            String[] data = graph.split("\n");
            for(int i=0;i<data.length;i++) {
                if(data[i].indexOf(colour)>=0) {
                    str = data[i];
                    break;
//			if(data[i].startsWith(colour)) {
//				colour = data[i];
//				break;
                }
            }
        }
        catch (Exception e){
        }
        return str;
    }

/***************获取一种颜色任意形状个数*****************/
    /*********** graph 颜色提取结果 shape 形状**************/
    public static int graph_get_shape(String graph,String shape) {         //获取一种颜色任意形状个数
        String str = "";
        int num = 0;
        try {
            if (shape.equals("全部")) {
                num = graph_get_num(graph);
            }
            else {
                String[] data = graph.split("\\s");
                for (int i = 0; i < data.length; i++) {
                    if (data[i].indexOf(shape) >= 0) {
                        str = data[i];
                        break;
//			if(data[i].startsWith(shape)) {
//				str = data[i];
//				break;
                    }
                }
                num = Integer.parseInt(str.substring(shape.length(), shape.length() + 1));
            }
        }
        catch (Exception e){
        }
        return num;
    }

/***************获取当前颜色所有形状个数*****************/
    /*************** graph 颜色提取结果*********************/
    public static int graph_get_num(String graph) {
        int num = 0;
        try {
            String str = graph.replaceAll("\\D", " ");
            String[] data = str.split("\\s+");
            for (int i = 0; i < data.length; i++) {
                if (!data[i].equals("")) {
                    num += Integer.parseInt(data[i]);
                }
            }
        }catch(Exception e){
        }
        return num;
    }




/****************图形提取END*****************/







    /************第一次测试***************/




    /***************从车车头提取***************/
    public static String Get_cczb(String cp)
    {
        String bk = GetRFID(1);
        String ct = "";
        int num = 0;
        String zm = "";
        zm = TiquStr(bk);
        num = TiquNum(cp);
        if(zm.equals("D"))
        {
            if(num > 4)  ct = "D4U";
            else if (num < 4) ct = "D4D";
        }
        if(zm.equals("B")) ct = "D4R";
        if(zm.equals("F")) ct = "D4L";
        return ct;
    }



    public static int TiquNum(String a)
    {
        int num = 0;
        String data = a.substring(1);
        String[] shuzi = data.split("\\D");
        char S1 = 0;
        char S2 = 0;
        for(int i = 0;i < shuzi.length;i++) //提取第二个数字
        {
            if(!shuzi[i].equals(""))
            {
                S1 = shuzi[i].substring(1, 2).charAt(0);
                break;
            }
        }
//		 System.out.println((int)S1);

        char[] c= data.toUpperCase().toCharArray(); //小写转化大写;
        S2 = c[c.length-1];						 //提取最后一个字符
//	     System.out.println((int)S2);
        num = S1-S2;
//	     System.out.println(num);
        return num;
    }



    public static String TiquStr(String a)
    {
        String data = "";
        String[] str = a.split("\"");
        for(int i = 0;i < str.length;i++)
        {
            if(str[i].length()==5)
            {
                data = str[i];
                break;
            }
        }
        data = data.substring(0,1).toUpperCase();
        return data;
    }




    /*****************从车车库******************/
    public static String GET_CK(String bk)
    {
        String ck = "";
        String str  = "";
        str = TiquStr(bk);
        if(str.equals("A")) ck = "G2R1";
        if(str.equals("B")) ck = "G4R1";
        if(str.equals("C")) ck = "G6R1";
        if(str.equals("D")) ck = "F7D1";
        if(str.equals("E")) ck = "D7U1";
        if(str.equals("F")) ck = "B7U1";
        return ck;
    }




/******************END*********************/












/*******************END**************************/

}
