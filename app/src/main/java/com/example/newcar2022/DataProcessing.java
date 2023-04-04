package com.example.newcar2022;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

import java.math.BigInteger;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

/**
 * Created by JIA on 2018/5/8.
 */

public class DataProcessing {


/**
 * 以下为类型转换------------------------------------------------------------------------------------------------------------------
 */

    /*************************
     * 文本转换成数字    （str 需转换的字符串， radix (2 8 10 16) 基数 以几进制整数输出）
     *************************/
/*
* 字符串型转换成各种数字类型：
* 1. 调用包装类的 parseXxx 静态方法
String s = "169";
byte b = Byte.parseByte( s );
short t = Short.parseShort( s );
int i = Integer.parseInt( s );
long l = Long.parseLong( s );
Float f = Float.parseFloat( s );
Double d = Double.parseDouble( s );
*2. 调用包装类的 valueOf() 方法转换为基本类型的包装类，会自动拆箱
*   int e = Integer.volueof(str);
* */
    public int STRtoNUM(String STR, int radix) {
        int result = 0;
        result = Integer.parseInt(STR, radix);
        return result;
    }

    /*************************
     * 数字转换成文本
     *************************/
// 1. 用一个空字符串加上基本类型，得到的就是基本类型数据对应的字符串
    public String NUMtoSTR(int X) {
        String result = null;
        result = X + "";
        return result;
    }
//2.使用String类的 valueOf() 方法
    //  String s = String.valueOf( value ); // 其中 value 为任意一种数字类型。

//3.使用包装类的 toString() 方法
//    String str = Integer.valueOf( value );


    /*************************
     * string 转 byte[]
     *************************/
    public static byte[] strToByteArray(String str) {

        if (str == null) {
            return null;
        }
        byte[] byteArray = str.getBytes();
        return byteArray;
    }


/*************************
 * int类型进制转换
 *************************/
//	//十进制转成十六进制：
//	Integer.toHexString(int i)
//	//十进制转成八进制
//	Integer.toOctalString(int i)
//	//十进制转成二进制
//	Integer.toBinaryString(int i)
//	//十六进制转成十进制
//	Integer.valueOf("FFFF",16).toString()
//	//八进制转成十进制
//	Integer.valueOf("876",8).toString()
//	//二进制转十进制
//	Integer.valueOf("0101",2).toString()



    /**
     * 把文本转换为字节
     * @param licString
     * @return
     */
    public byte[] StringToBytes(String licString)
    {
        if (licString == null || licString.equals(""))
        {
            return null;
        }
        licString = licString.toUpperCase(); //小写转化大写
        int length = licString.length();  //字符串的长度
        char[] hexChars = licString.toCharArray();//把字符串转换为字符
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++)
        {
            d[i] = (byte) hexChars[i];
        }
        return d;
    }

    /***
     *    byte数组转为hexstring        字节型转换为十六进制
     *
     */
    public String ByteToHexString(byte[] by)
    {
        String str="";
        try{
        for (int i = 0; i < by.length; i++) {
            String hex = Integer.toHexString(by[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            hex="0x"+hex+",";
            str=str+hex;
        }
        }catch (Exception e){

        }
        return str;
    }




/************** END ***********************/


/**
 * 以下为常用数学处理------------------------------------------------------------------------------------------------------------------
 */



//（1）求最大值，可以用于求int类型，long类型，float类型，double类型的最大值，下面仅仅下求整数最大值的方法的定义：
//    public static int max(int a,int b);
//（2）求最小值，和求最大值基本相同。
//    public static int min(int a,int b);
//（3）求绝对值，和求最大值的方法基本相同。
//    public static int abs(int a)
//（4）四舍五入的方法
//    public static int round(float a)
//    public static long round(double d)
//（5）计算幂
//    public static double pow(double a,double b)
//（6）求下限值
//    public static double floor(double d)
//（7）求上限值
//    public static double ceil(double d)
//（8）求平方根
//    public static double sqrt(double d)
//    下面的例子包含了上面的8个方法：
//    double d1 = 5.7;
//    double d2 = 12.3;
//    double d3 = -5;
//
//    System.out.println(d1+"和"+d2+"的最大值为："+Math.max(d1,d2));
//    System.out.println(d1+"和"+d2+"的最小值为："+Math.min(d1,d2));
//    System.out.println(d3+"的绝对值为："+Math.abs(d3));
//    System.out.println(d2+"四舍五入之后为："+Math.round(d2));
//    System.out.println(d2+"的2次幂为："+Math.pow(d2,2));
//    System.out.println(d2+"的下限为："+Math.floor(d2));
//    System.out.println(d2+"的上限为："+Math.ceil(d2));
//    System.out.println(d2+"的平方根为："+Math.sqrt(d2));
//    运行结果为：
//            5.7和12.3的最大值为：12.3
//            5.7和12.3的最小值为：5.7
//            -5.0的绝对值为：5.0
//            12.3四舍五入之后为：12
//            12.3的2次幂为：151.29000000000002
//            12.3的下限为：12.0
//            12.3的上限为：13.0
//            12.3的平方根为：3.5071355833500366
//            （9）要获取一个随机数，如果是0到1之间的随机数，可以直接使用下面的方法：
//    public static double random();
//    如果希望得到某个范围的随机数，例如60到100，可以这样处理：
//    int min=60;
//    int max=100;
//    int random;
//    random = min + (int) ( (max - min) * (Math.random()));


    /***************众数*****************/
    /****从小到大 最小的出现次数最多******/
public static int zhongshu(int[] a) {
    Arrays.sort(a);
    int temp = 1;//记录某个数字出现的次数
    int max = 1;//记录最多的出现次数
    int n = a.length;
    int result = a[0];//记录出现次数最多的数字即输出结果
    for(int i=1;i<n;i++)
    {
        if(a[i-1]==a[i])
            temp++;
        else
            temp=1;
        if(max<temp)
        {
            max=temp;
            result=a[i];
        }
    }
//        System.out.println(result);
    return result;
}




    /**********判断互质**********************/
    /****互质返回 1 ，不是返回最大公约数******/
    public static int gcd(int a, int b)
    {
        if(a < b){ //保证a大于等于b，便于a%b的运算
            int temp;
            temp = a;
            a = b;
            b = temp;
        }
        while(b != 0){
            int rem = a%b;
            a = b;
            b = rem;
        }
        // a 为最大共最大公约数 ， 如果a = 1  则两个数互质
        return a;
    }

    /**********判断质数************/
    /****是质数返回本身，不是返回0**/
    public static int zhishu(int s)
    {
        int data = 0;
        if(s == 3 || s==2)
        {
            return s;
        }
        for(int i=2;i<=(s/2);i++)
        {
            if(s % i == 0)
            {
                data = 0;
                break;
            }
            else data = s;
        }
        return data;
    }

    /**************平均数*************/
    public static double mean_value(String data) {
        double num = 0;
        String[] Data = data.split(",");  //分割字符串 根据实际更改
        int n = Data.length;
        int[] array = new int[n];
        array = Stringtoint(Data);
        for(int i=0;i<n;i++) {
            num += array[i];
        }
        num = (num*1.0) / n;
        return num;
    }
    /**************平均数*************/
    public static double mean_value(int[] data) {
        double num = 0;
        int n = data.length;
        for(int i=0;i<n;i++) {
            num += data[i];
        }
        num = (num*1.0) / n;
        return num;
    }


    /**********最小公倍数**********/
    public static int Min_GBS(int a, int b) {
        int data = 0;
        int num = 0;
        if(a > b) data = a;
        else data = b;
        for(int i=data;i<=a*b;i++)
        {
            if(i%a==0&&i%b==0)
            {num = i;break;}
        }
        return num;
    }



    /************字母转对应数字*************/
    /***************单个转换****************/
    public static int Alphabet_number(String data) {
        int num = 0;
        num = (int)data.toUpperCase().charAt(0) - 64;
        return num;
    }



    /************数字转对应字母*************/
    /***********单个转换大写字母************/
    public static String Number_alphabet(String data) {
        int num = Integer.parseInt(data);
        String ZM = "";
        String [] alphabet = ",A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z".split(",");
        if(num <= 26) {
            ZM = alphabet[num];
        }
        return ZM;
    }

/************** END ***********************/



/**
 * 以下为数组处理------------------------------------------------------------------------------------------------------------------
 * 在API 文档中  java.math.BigInteger中
 *
 */





    /*********String数组转int型数组*************/
    public static int[] Stringtoint(String[] array)
    {
        int len = array.length;
        int data[] = new int[len];
        for(int i=0;i<len;i++)
        {
            data[i] = Integer.parseInt(array[i]);
        }
        return data;
    }

    /*********去除String数组中重复的元素*************/
    public static String[] ifRepeat(String[] array){        //乱序
        Set<String> set = new HashSet<>();
        for(int i=0;i<array.length;i++){
            set.add(array[i]);
        }
        String[] arrayResult =  set.toArray(new String[set.size()]);
//        System.out.println(Arrays.toString(arrayResult));
        return arrayResult;
    }
    public static String str_quchongfu(String str){         //之前的顺序
        String [] Str = str.split("");
        String rest = "";
        for(int i = 0; i < Str.length; i++){
            for(int j = i+1; j < Str.length; j++){
                if (Str[i].equals(Str[j]))
                    Str[j] = "";
            }
            rest += Str[i];
        }
//        System.out.println(rest);
        return rest;
    }

    /*********String转16进制(只保留低八位)*************/
    public static String Str_HEX(String str)
    {
        String HEX_Data = "";
        int data = Integer.parseInt(str);
        if(data > 255)  data = data - 255;
        HEX_Data = Integer.toHexString(data);
//		if (HEX_Data.length() == 1)
//			{
//				HEX_Data = "0x0" + HEX_Data;
//		    }
//		else HEX_Data = "0x" + HEX_Data;
        return HEX_Data;
    }


/************** END ***********************/



/**
 * 以下为较大长度数据处理------------------------------------------------------------------------------------------------------------------
 * 在API 文档中  java.math.BigInteger中
 *
 */


    /*********BigInteger加法*************/
    public static String Bignum_add(String a, String b) {
        BigInteger A = new BigInteger(a);
        BigInteger B = new BigInteger(b);
        String C = A.add(B).toString();
        return C;
    }



    /*********BigInteger减法*************/
    public static String Bignum_subtract(String a, String b) {
        BigInteger A = new BigInteger(a);
        BigInteger B = new BigInteger(b);
        String C = A.subtract(B).toString();
        return C;
    }


    /*********BigInteger乘法*************/
    public static String Bignum_multiply(String a, String b) {
        BigInteger A = new BigInteger(a);
        BigInteger B = new BigInteger(b);
        String C = A.multiply(B).toString();
        return C;
    }


    /*********BigInteger除法*************/
    public static String Bignum_divide(String a, String b) {
        BigInteger A = new BigInteger(a);
        BigInteger B = new BigInteger(b);
        String C = A.divide(B).toString();
        return C;
    }


    /*********BigInteger取余**************
    ******取余结果的正负取决于被除数******/
    public static String Bignum_remainder(String a, String b) {
        BigInteger A = new BigInteger(a);
        BigInteger B = new BigInteger(b);
        String C = A.remainder(B).toString();
        return C;
    }


    /*********BigInteger阶乘*************/
    public static String Bignum_pow(String a, int b) {
        BigInteger A = new BigInteger(a);
        String C = A.pow(b).toString();
        return C;
    }
/************** END ***********************/






/****************字符串表示四则计算**************/
    /****************带括号优先运算级****************/

    public static int comput1(String s) {
        String str= s ;
        Stack od=new Stack();//数字栈
        Stack op=new Stack();//符号栈
        int index=0;//记录已经执行的符号数
        int length=str.length();
        //System.out.println(length);
        while(index<length)
        {
            //System.out.println("第"+index+"步++++++++++");
            char c=str.charAt(index);//取出这一步的符号
            if(c=='(')
            {
                //System.out.println(c+"-------进栈");
                op.push(c);//若是左括号就进栈
            }
            //否则要先判断优先级
            else if(c=='+' || c=='-' || c=='*'|| c=='/'|| c=='%')
            {
                int currOplevel=getOplevel(c);//当前符号的优先级
                while(true)
                {
                    int stackOplevel=0;//栈顶元素的优先级
                    if(op.isEmpty()==false)
                    {
                        Object obj=op.peek();
                        stackOplevel=getOplevel((char)obj);
                    }
                    //若当前元素优先级大于栈顶元素的优先级则入栈
                    if(currOplevel>stackOplevel)
                    {
                        //  System.out.println(c+"-------进栈");
                        op.push(c);
                        break;//直到让比自己优先级高的符号都出栈运算了再把自己进栈
                    }
                    else//不能入栈就进行计算
                    {
                        try{
                            char optemp='0';
                            int odnum1=0;
                            int odnum2=0;
                            if(op.isEmpty()==false)
                            {
                                optemp=(char)op.pop();//取出优先级大的那个符号
                            }
                            if(od.isEmpty()==false)
                            {
                                odnum1=(int)od.pop();
                                odnum2=(int)od.pop();//取出数据栈中的两个数
                            }
                            //  System.out.println(optemp+" "+odnum1+" "+odnum2);
                            //System.out.println(cacuResult(optemp,odnum2,odnum1)+"-------进栈");
                            od.push(cacuResult(optemp,odnum2,odnum1));//将算出来的结果数据再次入数据栈
                        }catch(Exception e){
                            //System.out.println("多项式不正确1"+str+" "+c);
                            e.printStackTrace();
                        }
                    }
                }
            }else if(c==')')//右括号就返回找栈顶元素，右括号是不进栈的
            {
                while(true)
                {
                    char theop=(char)op.pop();
                    if(theop=='(')
                    {
                        break;
                    }
                    else
                    {
                        try{
                            int odnum1=(int)od.pop();
                            int odnum2=(int)od.pop();
                            //System.out.println(" "+odnum1+" "+odnum2);
                            //System.out.println(cacuResult(theop,odnum2,odnum1)+"-------进栈");
                            od.push(cacuResult(theop,odnum2,odnum1));//运算括号内的内容

                        }catch(Exception e)
                        {
                            //System.out.println("多项式不正确2"+str);
                            e.printStackTrace();
                        }
                    }
                }
            }else if(c>='0' && c<='9')
            {
                int tempindex=index+1;
                while(tempindex<length)
                {
                    char tempc=str.charAt(tempindex);//取字符串中处于当前字符的下一位
                    if(tempc>='0'  && tempc<='9')
                    {
                        tempindex++;//若为数字则继续向后取
                    }
                    else
                    {
                        break;//证明数字取完
                    }
                }
                String odstr=str.substring(index,tempindex);//截取这个字符串则为两个符号之间的数字
                //System.out.println("---------"+odstr+"------------");
                try{
                    int odnum=Integer.parseInt(odstr);//将数字转化成整型便于运算
                    //System.out.println(odnum+"-------进栈");
                    od.push(odnum);
                    index=tempindex-1;
                }catch(Exception e)
                {
                    //System.out.println("多项式不正确3"+str);
                    e.printStackTrace();
                }
            }
            index++;
        }
        //检查op栈是否为空
        while(true)
        {
            Object obj=null;
            if(op.isEmpty()==false)
            {
                obj=op.pop();
            }
            if(obj==null)
            {
                break;//为空证明运算已结束
            }
            else//不为空就出栈运算
            {
                char optemp=(char)obj;
                int odnum1=(int)od.pop();
                int odnum2=(int)od.pop();
                //  System.out.println(cacuResult(optemp,odnum2,odnum1)+"-------进栈");
                od.push(cacuResult(optemp,odnum2,odnum1));
            }
        }
        int result=0;
        try{
            result=(int)od.pop();
        }catch(Exception e)
        {
            //System.out.println("多项式不正确4"+str);
            e.printStackTrace();
        }
//        System.out.println(result);
        return result;
    }




    //计算加减乘除余
    public static int cacuResult(char op,int od1,int od2)
    {
        switch(op)
        {
            case '+':return od1+od2;
            case '-':return od1-od2;
            case '*':return od1*od2;
            case '/':return od1/od2;
            case '%':return od1%od2;
        }
        return 0;
    }

    //返回符号优先级
    public static int getOplevel(char op)
    {
        switch(op)
        {
            case '(':return 0;
            case '+':
            case '-':return 1;
            case '%':
            case '*':
            case '/':return 2;
            default:return 0;
        }
    }








    //计算字符串表示的四则运算表达式 ，数字不限于1位的
//扫描三遍完成，第一遍确定数字和运算符，第二遍可以计算出乘除，第三遍计算加减
    public static int comput2(String s){

        char[] origin=s.toCharArray();

        String[] tmp=new String[s.trim().length()];
        int count=0;
        int j=0;

        for(int i=0;i<s.length();i++)
        {
            char a=origin[i];
            if(a=='+' || a=='-' || a=='*' || a=='/')
            {
                tmp[count++]=s.substring(j,i);
                tmp[count++]=String.valueOf(a);
                j=i+1;
            }
            if(i==s.length()-1)
                tmp[count++]=s.substring(j,i+1);

        }
        int index=-1;
        for(int i=0;i<tmp.length&& tmp[i]!=null ;i++)
        {
            if(tmp[i].equals("/"))
            {
                int var=Integer.parseInt(tmp[i-1])/Integer.parseInt(tmp[i+1]);
                i=i+1;
                tmp[index]=var+"";
            }else if(tmp[i].equals("*")){

                int var=Integer.parseInt(tmp[i-1])*Integer.parseInt(tmp[i+1]);
                i=i+1;
                tmp[index]=var+"";
            }else
                tmp[++index]=tmp[i];
        }
        tmp[++index]=null;

        int i=0;
        for(;i<tmp.length && tmp[i]!=null;i++)
        {
            if(tmp[i].equals("+"))
            {
                int var=Integer.parseInt(tmp[i-1]) + Integer.parseInt(tmp[i+1]);
                tmp[++i]=var+"";
            }
            if(tmp[i].equals("-"))
            {
                int var=Integer.parseInt(tmp[i-1]) - Integer.parseInt(tmp[i+1]);
                tmp[++i]=var+"";
            }
        }
        int result=Integer.valueOf(tmp[--i]);
        return result;
    }


/*********************END****************************/

/**
 * 以下为仿射密码算法----------------------------
 *
 */
    /**********仿射密码**********/
    public static void FS(String ss) {
        String data = Miwen(ss);
        int k3 = K3(ss);
        int k2 = K2(ss);
        String [] Str_data = data.split("\\B");
        int [] num_data = new int [Str_data.length];
        int [] int_data = new int[Str_data.length];
        for(int i=0;i<Str_data.length;i++) {
            num_data[i] = DataProcessing.Alphabet_number(Str_data[i])-1;

            if(num_data[i] < k2) {
                num_data[i] = (k3*(num_data[i] - k2) % 26) + 26;
                Str_data[i] = DataProcessing.Number_alphabet(num_data[i]+1+"").toLowerCase();
                int_data[i] = (int)Str_data[i].charAt(0);
            }
            else{
                num_data[i] = (k3*(num_data[i] - k2) % 26);
                Str_data[i] = DataProcessing.Number_alphabet(num_data[i]+1+"").toLowerCase();
                int_data[i] = (int)Str_data[i].charAt(0);
            }
//			System.out.println(int_data[i]);

        }
        int [] HW_ma = new int[6];
        HW_ma[0] = Math.abs(int_data[0] - int_data[1]);
        HW_ma[1] = int_data[1] + int_data[2];
        HW_ma[2] = Math.abs(int_data[2] - int_data[3]);
        HW_ma[3] = int_data[3] + int_data[4];
        HW_ma[4] = Math.abs(int_data[4] - int_data[5]);
        HW_ma[5] = int_data[5] + int_data[6];

        ControlCommand.HW_Data_6[0]=HW_ma[0];
        ControlCommand.HW_Data_6[1]=HW_ma[1];
        ControlCommand.HW_Data_6[2]=HW_ma[2];
        ControlCommand.HW_Data_6[3]=HW_ma[3];
        ControlCommand.HW_Data_6[4]=HW_ma[4];
        ControlCommand.HW_Data_6[5]=HW_ma[5];
//		for(int j=0;j<6;j++)
//			System.out.println(HW_ma[j]);
    }




    /**********密文**********/
    public static String Miwen(String ss) {
        String data = "";
        data = ss.replaceAll("[^A-Z]", "");
        return data;
    }


    /**********K1**********/
    public static int   K1(String ss) {
        String data = "";
        int num = 0;
        data = ss.replaceAll("[^0-9]", "");
        String [] str = data.split("\\B");
        int [] number = new int[str.length];
        number = DataProcessing.Stringtoint(str);
        for(int i=0;i<number.length;i++) {
            if(number[i]==0||number[i]==3||number[i]==5||number[i]==7||number[i]==9) {
                num = number[i];
                break;
            }
        }
        return num;
    }


    /**********K2**********/
    public static int K2(String ss) {
        String data = "";
        int num = 0;
        data = ss.replaceAll("[^0-9]", "");
        String [] str = data.split("\\B");
        int [] number = new int[str.length];
        number = DataProcessing.Stringtoint(str);
        num = number[number.length-1];
        return num;
    }

    /**********K3**********/
    public static int K3(String ss) {
        int k1 = K1(ss);
//		int k2 = K2(ss);
        int K3 = 0;
        for(int i=1;i<65535;i++)
        {
            if((i*k1)%26 == 1) {
                K3 = i;
                break;
            }
        }
        return K3;
    }

/******************EnD*********************/


/**
 * 以下为RSA数据算法----------------------------
 *
 */
public static void RSA() {
    String sss = "3,4,5,7,8,9,11";//参数区
    String aaa = "1,2,3,4,5,6";     //密文
    String [] ss = aaa.split(",");
    int c = C(sss);
    int d = D(sss);
    int n = N(sss);
//		System.out.println(c);
//		System.out.println(d);
//		System.out.println(n);
//		for(int i =0;i<ss.length;i++)
//		{
//			System.out.println(Str_HEX(Bignum_count(ss[i],d+"",n+"")+""));
//		}
    int a0 = Integer.valueOf(Str_HEX(Bignum_count(ss[0],d+"",n+"")+""),16);
    int a1 = Integer.valueOf(Str_HEX(Bignum_count(ss[1],d+"",n+"")+""),16);
    int a2 = Integer.valueOf(Str_HEX(Bignum_count(ss[2],d+"",n+"")+""),16);
    int a3 = Integer.valueOf(Str_HEX(Bignum_count(ss[3],d+"",n+"")+""),16);
    int a4 = Integer.valueOf(Str_HEX(Bignum_count(ss[4],d+"",n+"")+""),16);
    int a5 = Integer.valueOf(Str_HEX(Bignum_count(ss[5],d+"",n+"")+""),16);

}



    public static int Bignum_count(String a,String b,String c)
    {
        BigInteger A = new BigInteger(a);
        BigInteger B = new BigInteger(b);
        BigInteger C = new BigInteger(c);
        BigInteger D = new BigInteger(a);

        int m = 0;

        //阶乘
        D = D.pow(Integer.parseInt(b));
        //取余
        BigInteger E = D. remainder(C);
//		E = D.subtract(E);
        m = E.intValue();
        return m;
    }


    public static int N(String str)
    {
        int n = 0;
        int N = 0;
        int [] pq = new int[2];
        String [] ss = str.split(",");
        String [] s = ifRepeat(ss);
        int []data = Stringtoint(s);
        Arrays.sort(data);
        for(int i=data.length-1;i>=0;i--)
        {
            if(zhishu(data[i]) != 0)
            {
                pq[n] = data[i];
                n++;
                if(n>1) break;
            }
        }
        N = pq[0] * pq[1];
        return N;
    }


    public static int ola(String str)
    {
        int n = 0;
        int N = 0;
        int [] pq = new int[2];
        String [] ss = str.split(",");
        String [] s = ifRepeat(ss);
        int []data = Stringtoint(s);
        Arrays.sort(data);
        for(int i=data.length-1;i>=0;i--)
        {
            if(zhishu(data[i]) != 0)
            {
                pq[n] = zhishu(data[i]);
                n++;
                if(n>1) break;
            }
        }
        N = (pq[0] - 1) * (pq[1] - 1);
        return N;
    }

    public static int C (String str)
    {
        int o = ola(str);
        for(int i=2;i<o;i++)
        {
            if(gcd(i,o) == 1)
            {
                return i;
            }
        }
        return 1;
    }

    public static int D (String str)
    {
        int D = 0;
        int c = C(str);
        int o = ola(str);
        for(int d = 1;d<=65563;d++)
        {
            if(((c*d)%o) == 1 )
            {
                D = d;
                break;
            }
        }
        return D;
    }






/*********************END****************************/


    /**
     * 以下为汉明码数据算法----------------------------
     *
     */

public static void HMM(String ss) {
//		String data = "ABC123";
    String str = "";
    String ls = "";
    try {
        String[] num = new String[48];
//    char []a = data.toCharArray();
        char[] a = ss.toCharArray();
        for (int i = 0; i < a.length; i++) {
            ls = Integer.toBinaryString(a[i]);
            if (ls.length() < 7) {
                str += "0" + ls;
            } else {
                str += ls;
            }
        }
//		System.out.println(str);
        char[] s = str.toCharArray();
        int number = 0;
        for (int i = 1; i <= 48; i++) {
            if (i == 1 || i == 2 || i == 4 || i == 8 || i == 16 || i == 32) {
                num[i - 1] = " ";
            } else {
                num[i - 1] = String.valueOf(s[number]);
                number++;
            }

//			System.out.print(num[i-1]);
        }
//		System.out.println();
        ls = "";
        for (int i = 0; i < 48; i++) {
            if (num[i].equals("1")) {
                ls += i + 1 + ",";
            }
        }
//		System.out.println(ls);
        String[] Str = ls.split(",");
        String[] sss = new String[Str.length];
        int jym = Integer.parseInt(Str[0]);
        for (int i = 1; i < Str.length; i++) {
            jym = jym ^ Integer.parseInt(Str[i]);
        }
//		System.out.println(jym);
        String aaa = Integer.toBinaryString(jym);
        while (aaa.length() < 6) {
            aaa = "0" + aaa;
        }
        String[] JYM = aaa.split("\\B");
//		System.out.println(JYM.length);
        num[0] = JYM[JYM.length - 1];
        num[1] = JYM[JYM.length - 2];
        num[3] = JYM[JYM.length - 3];
        num[7] = JYM[JYM.length - 4];
        num[15] = JYM[JYM.length - 5];
        num[31] = JYM[JYM.length - 6];
        String DATA = "";
        for (int i = 0; i < 48; i++) {
            DATA += num[i];
        }

        ControlCommand.HW_Data_6[0] = Integer.valueOf(DATA.substring(0, 8), 2);
        ControlCommand.HW_Data_6[1] = Integer.valueOf(DATA.substring(8, 16), 2);
        ControlCommand.HW_Data_6[2] = Integer.valueOf(DATA.substring(16, 24), 2);
        ControlCommand.HW_Data_6[3] = Integer.valueOf(DATA.substring(24, 32), 2);
        ControlCommand.HW_Data_6[4] = Integer.valueOf(DATA.substring(32, 40), 2);
        ControlCommand.HW_Data_6[5] = Integer.valueOf(DATA.substring(40, 48), 2);
    }catch (Exception e){

    }
}


/**
 * 以下为CRC数据算法----------------------------
 *
 */



public static int CRC(String str) {

    char[] data = TiquCs(str).toCharArray();
    int dxsm = Integer.valueOf(TiquGs(str),2);
    int crc = Integer.valueOf("FFFF",16);
    for(int i=0;i<4;i++)
    {
        crc = crc ^ (int)data[i];
        for(int j=0;j<8;j++)
        {
            int a = crc %2;
            crc = crc >> 1;
            if(a == 1) {
                crc ^= dxsm;
            }
        }
    }
    return crc;
}

/******************提取参数码*******************/
    public static String TiquCs(String ewm)  //提取字母
    {
        String[] str = ewm.split("<");
        String csdata = "";
        for(int i = 0;i < str.length;i++)
        {
//	    	 System.out.println(str[i]);
            if(str[i].indexOf(">")==8)	{
                csdata = str[i].substring(0, 8);
                break;
            }
        }
        csdata = csdata.replaceAll("[^a-zA-Z]", "");
        csdata = csdata.substring(0, 4);
        return csdata;
    }



/****************提取公式码*****************/
    public static String TiquGs(String ewm)  //提取多项式码
    {
        String[] Str = ewm.split(">");
        String gsdata = "";
        int[] num = new int[16];
        for(int i=0;i<16;i++) {
            num[i] = 0;
        }
        for(int i = 0;i < Str.length;i++)
        {
//	    	 System.out.println(Str[i]);
            if(Str[i].indexOf("=")>=0) {
                gsdata = Str[i].substring(Str[i].indexOf("=")+1, Str[i].length());
            }
        }
        String[] data = gsdata.split("\\D");
        gsdata = "";
        for(int i=0;i<data.length;i++)
        {
            if((!data[i].equals(""))&& (Integer.parseInt(data[i])<16)) {

                if(Integer.parseInt(data[i])==1) {
                    num[0] = 1;
                }else {
                    num[Integer.parseInt(data[i])] = 1;
                }
            }
        }
        for(int i=0;i<=15;i++) {
            gsdata += String.valueOf(num[i]);
        }
        return gsdata;
    }



    /**
     * 以下为Base64算法----------------------------
     *
     */

    private static final String base64Code= "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    public static String encode(String srcStr) {
        //有效值检查
        if(srcStr == null || srcStr.length() == 0) {
            return srcStr;
        }
        //将明文的ASCII码转为二进制位字串
        char[] srcStrCh= srcStr.toCharArray();
        StringBuilder asciiBinStrB= new StringBuilder();
        String asciiBin= null;
        for(int i= 0; i< srcStrCh.length; i++) {
            asciiBin= Integer.toBinaryString((int)srcStrCh[i]);
            while(asciiBin.length()< 8) {
                asciiBin= "0"+ asciiBin;
            }
            asciiBinStrB.append(asciiBin);
        }
        //跟据明文长度在二进制位字串尾部补“0”
        while(asciiBinStrB.length()% 6!= 0) {
            asciiBinStrB.append("0");
        }
        String asciiBinStr= String.valueOf(asciiBinStrB);
        //将上面得到的二进制位字串转为Value，再跟据Base64编码表将之转为Encoding
        char[] codeCh= new char[asciiBinStr.length()/ 6];
        int index= 0;
        for(int i= 0; i< codeCh.length; i++) {
            index= Integer.parseInt(asciiBinStr.substring(0, 6), 2);
            asciiBinStr= asciiBinStr.substring(6);
            codeCh[i]= base64Code.charAt(index);
        }
        StringBuilder code= new StringBuilder(String.valueOf(codeCh));
        //跟据需要在尾部添加“=”
        if(srcStr.length()% 3 == 1) {
            code.append("==");
        } else if(srcStr.length()% 3 == 2) {
            code.append("=");
        }
        //每76个字符加一个回车换行符（CRLF）
        int i= 76;
        while(i< code.length()) {
            code.insert(i, "\r\n");
            i+= 76;
        }
        code.append("\r\n");
        return String.valueOf(code);
    }


    public static String decode(String srcStr) {
        //有效值检查
        if(srcStr == null || srcStr.length() == 0) {
            return srcStr;
        }
        //检测密文中“=”的个数后将之删除，同时删除换行符
        int eqCounter= 0;
        if(srcStr.endsWith("==")) {
            eqCounter= 2;
        } else if(srcStr.endsWith("=")) {
            eqCounter= 1;
        }
        srcStr= srcStr.replaceAll("=", "");
        srcStr= srcStr.replaceAll("\r\n", "");
        //跟据Base64编码表将密文（Encoding）转为对应Value，然后转为二进制位字串
        char[] srcStrCh= srcStr.toCharArray();
        StringBuilder indexBinStr= new StringBuilder();
        String indexBin= null;
        for(int i= 0; i< srcStrCh.length; i++) {
            indexBin= Integer.toBinaryString(base64Code.indexOf((int)srcStrCh[i]));
            while(indexBin.length()< 6) {
                indexBin= "0"+ indexBin;
            }
            indexBinStr.append(indexBin);
        }
        //删除因编码而在尾部补位的“0”后得到明文的ASCII码的二进制位字串
        if(eqCounter == 1) {
            indexBinStr.delete(indexBinStr.length()- 2, indexBinStr.length());
        } else if(eqCounter == 2) {
            indexBinStr.delete(indexBinStr.length()- 4, indexBinStr.length());
        }
        String asciiBinStr= String.valueOf(indexBinStr);
        //将上面得到的二进制位字串分隔成字节后还原成明文
        String asciiBin= null;
        char[] ascii= new char[asciiBinStr.length()/ 8];
        for(int i= 0; i< ascii.length; i++) {
            asciiBin= asciiBinStr.substring(0, 8);
            asciiBinStr= asciiBinStr.substring(8);
            ascii[i]= (char)Integer.parseInt(asciiBin, 2);
        }
        return String.valueOf(ascii);
    }

/******************Base64 END***********************/







    /**
     * 以下为波雷费密码算法----------------------------
     *
     */
    public static char[] boleifu(String Str){
//        String a = "<hidethegold|play5fair9example>";
        char[] data = null;
        try{
            String [] str = Str.split("\\|");
            String mingwen = str[0].replaceAll("[^a-zA-Z]","");
            String miwen = str[1].replaceAll("[^a-zA-Z]","");
            miwen = miwen.toUpperCase();
            mingwen = mingwen.toUpperCase();
            char [][] Miwen = get_juzhen(miwen);
            data = Get_miwen(mingwen,Miwen);
            System.out.println(Arrays.toString(data));
            for(int i=0;i<data.length;i++)
                System.out.println((int)data[i]);

        }catch (Exception E){

        }
        return data;
    }



    public static String addX(String str){
        String rest = "";
        int i=0;
        while(i<=str.length()-1){
            if(i == str.length()-1){        //截取到最后一位+X 结束
                rest += str.substring(i,i+1) + "X";
                break;
            }
            String s = str.substring(i,i+2);    //两两截取
            if(s.charAt(0) != s.charAt(1)){     //两两不同 就截取保存 i+2
                rest += s;
                i += 2;
            }
            else{
                rest += str.substring(i,i+1) + "X"; //相同就插入X i+1
                i += 1;
            }
        }
        return rest;
    }


    public static int[] Get_index(char str,char[][]juzhen){
        int [] num = new int[2];
        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                if(juzhen[i][j] == str) {
                    num[0] = i;
                    num[1] = j;
                    break;
                }
            }
        }
        return  num;
    }

    public static char[] Get_miwen(String str,char[][] juzhen){
        String Str = addX(str);
        char[] rest = new char[Str.length()];
        for(int i=0;i<Str.length();i+=2)
        {
            int[] a = Get_index(Str.charAt(i),juzhen);
            int[] b = Get_index(Str.charAt(i + 1),juzhen);
            if(a[0] == b[0]){
                rest[i] = juzhen[a[0]][(a[1]+1)%5];
                rest[i+1] = juzhen[b[0]][(b[1]+1)%5];
            }
            if(a[1] == b[1]){
                rest[i] = juzhen[(a[0]+1)%5][a[1]];
                rest[i+1] = juzhen[(b[0]+1)%5][b[1]];
            }else{
                rest[i] = juzhen[a[0]][b[1]];
                rest[i+1] = juzhen[b[0]][a[1]];
            }
        }
        return rest;
    }


//    public static String str_quchongfu(String str){
//        String [] Str = str.split("");
//        String rest = "";
//        for(int i = 0; i < Str.length; i++){
//            for(int j = i+1; j < Str.length; j++){
//                if (Str[i].equals(Str[j]))
//                    Str[j] = "";
//            }
//            rest += Str[i];
//        }
////        System.out.println(rest);
//        return rest;
//    }


    public static char[][] get_juzhen(String str){
        char []Miwen = str_quchongfu(str).toCharArray();
        char []zm = new char[26];
        for(int i=0;i<26;i++){
            zm[i] = (char) ('A'+i);
        }
        for(int i=0;i<26;i++){          //去除26个英文中的密钥出现过的字母
            for(int j=0;j<Miwen.length;j++){
                if(zm[i]==Miwen[j])
                    zm[i] = 'Q';
            }
        }
        String Str = new String(Miwen) + new String(zm);
//        System.out.println(Str);
        char [] data = Str.toCharArray();
        char [][]rest = new char[5][5];
        int k = 0;
        for(int i=0;i<data.length;i++){
            if(data[i] == 'Q')
                continue;
            else{
                rest[k/5][k%5] = data[i];
                k++;
            }
        }
//        for(int i=0;i<5;i++)
//        System.out.println(Arrays.toString(rest[i]));
        return rest;
    }


/*********************END****************************/

/**
 * 以下为希尔加密算法----------------------------
 *
 */
public static char[] zimubiao = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
        'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
public static String[] zhihuanbiao = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"
        , "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25",
        "26"};

public static String Hill_encrypt(String input,double[][] KEY){
    String rest = "";
    String a = input;
        char[] chars = new char[a.length()];
        for (int i = 0; i < a.length(); i++) {
            chars[i] = a.charAt(i);
        }
//            double[][] b = new double[1][a.length()];
        double[][] b = new double[a.length()][1];
        for (int i = 0; i < a.length(); i++) {
            for (int j = 0; j < zimubiao.length; j++) {
                if (zimubiao[j] == chars[i]) {
//                        b[0][i] = Integer.parseInt(zhihuanbiao[j]);
                    b[i][0] = Integer.parseInt(zhihuanbiao[j]);
                }
            }
        }
        System.out.println("明文序列对应的数字矩阵是：");
        printArray(b);
        System.out.println();
        double[][] key = KEY;
        System.out.println("选择的密钥矩阵是：");
        printArray(key);
        double[][] miwen = encrypt(b, key);
        System.out.println("密文矩阵序列为：");
        printArray(miwen);

        for (int i = 0; i < miwen.length; i++) {
            rest += zimubiao[(int) miwen[i][0]];
        }
        System.out.println(rest);

    return rest;
}

    public static String Hill_decode4(String input,double[][] KEY){
        String rest = "";
        if (input.length()==4) {
            //        double[][] b = new double[1][a.length()];
            char[] chars = input.toCharArray();
            double[][] b = new double[input.length()][1];
            for (int i = 0; i < input.length(); i++) {
                for (int j = 0; j < zimubiao.length; j++) {
                    if (zimubiao[j] == chars[i]) {
                        //                        b[0][i] = Integer.parseInt(zhihuanbiao[j]);
                        b[i][0] = Integer.parseInt(zhihuanbiao[j]);
                    }
                }
            }
            System.out.println("解密后，明文矩阵序列为：");
            double[][] mingwen = decrypt4(b, KEY);
            printArray(mingwen);

            for (int i = 0; i < mingwen.length; i++) {
                rest += zimubiao[(int) mingwen[i][0]];
            }
            System.out.println(rest);
        }else {
            System.out.println("长度不正确请重新输入");
        }
        return rest;
    }

    public static String Hill_decode3(String input,double[][] KEY){
        String rest = "";
        if (input.length()==3) {
            //        double[][] b = new double[1][a.length()];
            char[] chars = input.toCharArray();
            double[][] b = new double[input.length()][1];
            for (int i = 0; i < input.length(); i++) {
                for (int j = 0; j < zimubiao.length; j++) {
                    if (zimubiao[j] == chars[i]) {
                        //                        b[0][i] = Integer.parseInt(zhihuanbiao[j]);
                        b[i][0] = Integer.parseInt(zhihuanbiao[j]);
                    }
                }
            }
            System.out.println("解密后，明文矩阵序列为：");
            double[][] mingwen = decrypt3(b, KEY);
            printArray(mingwen);

            for (int i = 0; i < mingwen.length; i++) {
                rest += zimubiao[(int) mingwen[i][0]];
            }
            System.out.println(rest);
        }else {
            System.out.println("长度不正确请重新输入");
        }
        return rest;
    }

    static int[]temp = {
            1, 0, 0,0,0,0,0,0,0,0,0,0
    };

    /*
       这里使用满足逆矩阵与原矩阵相乘必为单位矩阵
    */
    private static double[][] getReverseMartrix3(double[][] key2) {
        // TODO Auto-generated method stub
        double[][] key = new double[key2.length][key2[0].length];
        for(int num=0; num<3; num++)
            for(int i=0; i<26; i++)
                for(int j=0; j<26; j++)
                    for(int k=0; k<26; k++) {
//                        if((i*17+j*21+k*2)%26 == temp[num%3]
//                                && ((i*17+j*18+k*2)%26 == temp[(num+2)%3])
//                                && ((i*5+j*21+k*19)%26 == temp[(num+1)%3])) {
                        if((i*key2[0][0]+j*key2[1][0]+k*key2[2][0])%26 == temp[num%3]
                                && ((i*key2[0][1]+j*key2[1][1]+k*key2[2][1])%26 == temp[(num+2)%3])
                                && ((i*key2[0][2]+j*key2[1][2]+k*key2[2][2])%26 == temp[(num+1)%3])) {
                            key[num][0] = i;
                            key[num][1] = j;
                            key[num][2] = k;
                        }
                    }
        return key;
    }


    /*
       这里使用满足逆矩阵与原矩阵相乘必为单位矩阵
    */
    private static double[][] getReverseMartrix4(double[][] key2) {
        // TODO Auto-generated method stub
        double[][] key = new double[key2.length][key2[0].length];
        for(int num=0; num<4; num++)
            for(int i=0; i<26; i++)
                for(int j=0; j<26; j++)
                    for(int k=0; k<26; k++) {
                        for(int m=0; m<26; m++) {
                            if ((i * key2[0][0] + j * key2[1][0] + k * key2[2][0] + m *key2[3][0]) % 26 == temp[num % 4]
                                    && ((i * key2[0][1] + j * key2[1][1] + k * key2[2][1] + m *key2[3][1]) % 26 == temp[(num + 3) % 4])
                                    && ((i * key2[0][2] + j * key2[1][2] + k * key2[2][2] + m *key2[3][2]) % 26 == temp[(num + 2) % 4])
                                    && ((i * key2[0][3] + j * key2[1][3] + k * key2[2][3] + m *key2[3][3]) % 26 == temp[(num + 1) % 4])) {
                                key[num][0] = i;
                                key[num][1] = j;
                                key[num][2] = k;
                                key[num][3] = m;
                            }
                        }
                    }
        return key;
    }




    /**
     * @return double[][]
     * @author Fever1
     * @Description 解密运算4
     * @Date 12:33 2019/1/7
     * @Param [miwen, key]
     **/
    private static double[][] decrypt4(double[][] miwen, double[][] key) {
//        double[][] key333 = getReverseMartrix3(key);  // 3*3
        double[][] key333 = getReverseMartrix4(key);
//        RealMatrix key_1 = inverseMatrix(key);
        RealMatrix key_1 = new Array2DRowRealMatrix(key333);
        RealMatrix matrixmiwen = new Array2DRowRealMatrix(miwen);
//        double[][] mingwen = matrixmiwen.multiply(key_1).getData();
        System.out.println("矩阵求逆为："+key_1);
        double[][] mingwen = key_1.multiply(matrixmiwen).getData();
        floodMod(mingwen);
        return mingwen;
    }

    /**
     * @return double[][]
     * @author Fever1
     * @Description 解密运算3
     * @Date 12:33 2019/1/7
     * @Param [miwen, key]
     **/
    private static double[][] decrypt3(double[][] miwen, double[][] key) {
        double[][] key333 = getReverseMartrix3(key);  // 3*3
//        double[][] key333 = getReverseMartrix4(key);
//        RealMatrix key_1 = inverseMatrix(key);
        RealMatrix key_1 = new Array2DRowRealMatrix(key333);
        RealMatrix matrixmiwen = new Array2DRowRealMatrix(miwen);
//        double[][] mingwen = matrixmiwen.multiply(key_1).getData();
        System.out.println("矩阵求逆为："+key_1);
        double[][] mingwen = key_1.multiply(matrixmiwen).getData();
        floodMod(mingwen);
        return mingwen;
    }


    /**
     * @return double[][]
     * @author Fever1
     * @Description 加密运算
     * @Date 12:34 2019/1/7
     * @Param [b, key]
     **/
    private static double[][] encrypt(double[][] b, double[][] key) {
        RealMatrix matrixb = new Array2DRowRealMatrix(b);
        RealMatrix matrixkey = new Array2DRowRealMatrix(key);
//        double[][] matrixtoarray = matrixb.multiply(matrixkey).getData();
        double[][] matrixtoarray = matrixkey.multiply(matrixb).getData();
        floodMod(matrixtoarray);
        return matrixtoarray;
    }

    /**
     * @return void
     * @author Fever1
     * @Description 求包括负数与正数的Mod运算
     * @Date 12:33 2019/1/7
     * @Param [matrixtoarray1]
     **/
    private static void floodMod(double[][] matrixtoarray1) {
        for (int i = 0; i < matrixtoarray1.length; i++) {
            for (int j = 0; j < matrixtoarray1[i].length; j++) {

                if (matrixtoarray1[i][j] > 0) {
                    while (matrixtoarray1[i][j] >= 26) {
                        matrixtoarray1[i][j] = Math.round(matrixtoarray1[i][j] - 26);
                    }
                } else {
                    while (matrixtoarray1[i][j] < 0) {
                        matrixtoarray1[i][j] = Math.round(matrixtoarray1[i][j] + 26);
                    }
                }
            }
        }
    }

    /**
     * @return void
     * @author Fever1
     * @Description 遍历打印数组
     * @Date 22:47 2019/1/6
     * @Param [array]
     **/
    private static void printArray(double[][] array) {
        for (int t = 0; t < array.length; t++) {
            for (int y = 0; y < array[t].length; y++) {
                System.out.print((int) array[t][y] + "\t");
            }
            System.out.println();
        }
    }

    /**
     * @return org.apache.commons.math3.linear.RealMatrix
     * @author Fever1
     * @Description 矩阵求逆
     * @Date 13:46 2018/12/26
     * @Param [A]
     **/
    public static RealMatrix inverseMatrix(double[][] a) {
        RealMatrix matrix = new Array2DRowRealMatrix(a);
        RealMatrix result = new LUDecomposition(matrix).getSolver().getInverse();
        return result;
    }


/*********************END****************************/



public static class DesEncrypt {
/******************DES加密 ***********************/
    /******************DES加密 ***********************/

    Key key;

    /**
     * 根据参数生成KEY
     *
     * @param strKey
     */
    public void getKey(String strKey) {
        try {
            KeyGenerator _generator = KeyGenerator.getInstance("DES");
            _generator.init(new SecureRandom(strKey.getBytes()));
            this.key = _generator.generateKey();
            _generator = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加密String明文输入,String密文输出
     *
     * @param strMing
     * @return
     */
    public String getEncString(String strMing) {
        byte[] byteMi = null;
        byte[] byteMing = null;
        String strMi = "";
        try {
            return byte2hex(getEncCode(strMing.getBytes()));

            // byteMing = strMing.getBytes("UTF8");
            // byteMi = this.getEncCode(byteMing);
            // strMi = new String( byteMi,"UTF8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            byteMing = null;
            byteMi = null;
        }
        return strMi;
    }

    /**
     * 解密 以String密文输入,String明文输出
     *
     * @param strMi
     * @return
     */
    public String getDesString(String strMi) {
        byte[] byteMing = null;
        byte[] byteMi = null;
        String strMing = "";
        try {
            return new String(getDesCode(hex2byte(strMi.getBytes())));

            // byteMing = this.getDesCode(byteMi);
            // strMing = new String(byteMing,"UTF8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            byteMing = null;
            byteMi = null;
        }
        return strMing;
    }

    /**
     * 加密以byte[]明文输入,byte[]密文输出
     *
     * @param byteS
     * @return
     */
    private byte[] getEncCode(byte[] byteS) {
        byte[] byteFina = null;
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byteFina = cipher.doFinal(byteS);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cipher = null;
        }
        return byteFina;
    }

    /**
     * 解密以byte[]密文输入,以byte[]明文输出
     *
     * @param byteD
     * @return
     */
    private byte[] getDesCode(byte[] byteD) {
        Cipher cipher;
        byte[] byteFina = null;
        try {
            cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byteFina = cipher.doFinal(byteD);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cipher = null;
        }
        return byteFina;
    }

    /**
     * 二行制转字符串
     *
     * @param b
     * @return
     */
    public static String byte2hex(byte[] b) { // 一个字节的数，
        // 转成16进制字符串
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            // 整数转成十六进制表示
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs.toUpperCase(); // 转成大写
    }

    public static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException("长度不是偶数");
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }

        return b2;
    }
}
    /******************END***********************/
/******************END ***********************/














}