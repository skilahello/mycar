package com.example.newcar2022;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.HexDump;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.wechat_qrcode.WeChatQRCode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {

    protected String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};




    // Model settings of object detection
    protected String modelPath = "";
    protected String labelPath = "";
    protected String imagePath = "";
    protected int cpuThreadNum = 1;
    protected String cpuPowerMode = "";
    protected String inputColorFormat = "";
    protected long[] inputShape = new long[]{};
    protected float[] inputMean = new float[]{};
    protected float[] inputStd = new float[]{};
    protected float scoreThreshold = 0.1f;




    private YoloV5Ncnn yolov5ncnn = new YoloV5Ncnn();
    private Bitmap bitmapss;




    public static final String A_S = "com.a_s";      // 广播名称
    private TextView qualityCheckResultView = null;
    private Context mContext;
    private static final String TAG = "MainActivity";
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    public static String purecameraip = null;
    public static int usbflag = 20;
    private Activity mActivity;
    public static int usbOrwifi = 0; //   为0  默认  为   wifi
    public static String IPCar;         //小车ip
    public static Bitmap bitmap;
    public static ImageView myImageView;
    public static int state_camera = 0;
    public  static String a="";//   dayin1    底层数据
    private static byte[] usbbyte = new byte[40];
    //下面的代码实现了usb的功能
    ////------------------------------------------------------------------------------------------
    public static UsbSerialPort sPort = null;
    public final int MESSAGE_REFRESH = 101;//   开启 usb  的参数
    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    public WifiManager wifiManager; //wifi管理
    public long REFRESH_TIMEOUT_MILLIS = 5000;//   开启 usb  传入 参数

    public static int camera_mode = 0;//调整摄像头速度0快速模式 1  慢速模式
    /**
     * 光照
     */
    public long Light = 0;
    public byte[] mByte = new byte[20];

    // 显示图片
    public Handler phHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 10) {
                myImageView.setImageBitmap(bitmap);
            }
        }
    };
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private static int REQUEST_PERMISSION_CODE = 1;

    /*
     * 开启usb 线程
     */
    public Thread usb = new Thread(new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (true) {
                //  Log.e("run: ","启动全自动线程" );
                if (XcApplication.isserial == XcApplication.Mode.SOCKET) {
                    // wifiInit();
                    //  得到服务器的ip //使用   wifi   初始化
                    // Toast.makeText(MainActivity.this, "wifi初始化", Toast.LENGTH_LONG).show();
                }

                //     usb

                if (usbflag == 50) {
                    //Toast.makeText(MainActivity.this, "竞赛平台和a72通过usb转串口通信", Toast.LENGTH_LONG).show();
                    if (XcApplication.isserial == XcApplication.Mode.USB_SERIAL) {  //竞赛平台和a72通过usb转串口通信
                        mHandler.sendEmptyMessageDelayed(MESSAGE_REFRESH, REFRESH_TIMEOUT_MILLIS); //启动usb的识别和获取
                        usbflag = 0;
                    }
                    if (XcApplication.isserial != XcApplication.Mode.SOCKET) {//   使用usb
                        Intent ipintent = new Intent();
                        //ComponentName的参数1:目标app的包名,参数2:目标app的Service完整类名
                        ipintent.setComponent(new ComponentName("com.android.settings", "com.android.settings.ethernet.CameraInitService"));
                        //设置要传送的数据
                        ipintent.putExtra("purecameraip", purecameraip);//    intent  发送数据
                        startService(ipintent);   //摄像头设为静态192.168.16.20时，可以不用发送//   给 手机  传入 本地ip 地址
                    }

                }

            }
        }
    });
    /**
     * 光敏状态
     */
    long psStatus = 0;
    /**
     * 坐标
     */
    long Coordinate = 0;
    String coordinate = "";
    /**
     * 超声波
     */
    long UltraSonic = 0;

    // 接受传感器
    /**
     * 码盘值
     */
    long CodedDisk = 0;
    byte[] textByte = new byte[40];
    long buf;
    int i;
    String wb = "";
    private String IPCamera;            //摄像头IP
    private CameraCommandUtil cameraCommandUtil;
    private DhcpInfo dhcpInfo;     //服务器管理器
    private TextView textView;
    //控件
    //   private VideoView mVideoView;
    //  private WebView mWebView;
    private SectionsPagerAdapter mySectionsPagerAdapter;    //页面适配器
    private ViewPager myViewPager;                            //放置页面的容器
    private ConneceReceiveSend conneceReceiveSend;
    private TabLayout down_tablayout;
    private AppBarLayout appbar_layout;
    private CoordinatorLayout main_layout;
    private BasicControl basiccontrol;
    private ToggleButton usbswitch;//    usb wifi  选择开关
    private String Camera_show_ip = null;
    /**
     * 速度与码盘值
     */
    private int sp_n, en_n;

    //******opencv回掉方法必须加*******/
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    // mOpenCvCameraView.enableView();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    // 开启线程接受摄像头当前图片
    private Thread phThread = new Thread(new Runnable() {
        public void run() {
            Log.e("ASDASDASD", "zhixingsssss");
            // TODO Auto-generated method stub
            Looper.prepare();
            Log.e("ASDASDASD", "2222222222222222222222");
            while (true) {
                //  bitmap =  mWebView.getDrawingCache();
                bitmap = cameraCommandUtil.httpForImage(IPCamera);
                phHandler.sendEmptyMessage(10);
                switch (state_camera) {
                    case 1:
                        if(camera_mode==0)
                        {
                            cameraCommandUtil.postHttp(IPCamera, 0, 1);

                        }
                        else
                        {
                            cameraCommandUtil.postHttp(IPCamera, 0, 1);
                            // cameraCommandUtil.postHttp(IPCamera, 1, 1);
                            cameraCommandUtil.postHttp(IPCamera, 1, 1);
                        }

                        break;
                    case 2:
                        if(camera_mode==0)
                        {

                            cameraCommandUtil.postHttp(IPCamera, 2, 1);
                        }
                        else
                        {
                            cameraCommandUtil.postHttp(IPCamera, 2, 1);
                            //  cameraCommandUtil.postHttp(IPCamera, 1, 1);
                            cameraCommandUtil.postHttp(IPCamera, 3, 1);
                        }

                        break;
                    case 3:
                        if(camera_mode==0)
                        {

                            cameraCommandUtil.postHttp(IPCamera, 4, 1);
                        }
                        else
                        {
                            cameraCommandUtil.postHttp(IPCamera, 4, 1);
                            //  cameraCommandUtil.postHttp(IPCamera, 1, 1);
                            cameraCommandUtil.postHttp(IPCamera, 5, 1);
                        }

                        break;
                    case 4:
                        if(camera_mode==0)
                        {

                            cameraCommandUtil.postHttp(IPCamera, 6, 1);
                        }
                        else
                        {
                            cameraCommandUtil.postHttp(IPCamera, 6, 1);
                            //  cameraCommandUtil.postHttp(IPCamera, 1, 1);
                            cameraCommandUtil.postHttp(IPCamera, 7, 1);
                        }

                        break;
                    // /预设位1到6
                    case 5:
                        cameraCommandUtil.postHttp(IPCamera, 30, 0);//设置预设位1
                        break;
                    case 6:
                        cameraCommandUtil.postHttp(IPCamera, 32, 0);//设置预设位2设置预设位1
                        break;
                    case 7:
                        cameraCommandUtil.postHttp(IPCamera, 34, 0);//设置预设位3
                        break;
                    case 8:
                        cameraCommandUtil.postHttp(IPCamera, 36, 0);//设置预设位4
                        break;
                    case 9:
                        cameraCommandUtil.postHttp(IPCamera, 38, 0);//设置预设位5
                        break;
                    case 10:
                        cameraCommandUtil.postHttp(IPCamera, 40, 0);//设置预设位6
                        break;
                    case 11:
                        cameraCommandUtil.postHttp(IPCamera, 31, 0);//调用1
                        break;
                    case 12:
                        cameraCommandUtil.postHttp(IPCamera, 33, 0);//调用2
                        break;
                    case 13:
                        cameraCommandUtil.postHttp(IPCamera, 35, 0);//调用3
                        break;
                    case 14:
                        cameraCommandUtil.postHttp(IPCamera, 37, 0);//调用4
                        break;
                    case 15:
                        cameraCommandUtil.postHttp(IPCamera, 39, 0);//调用5
                        break;
                    case 16:
                        cameraCommandUtil.postHttp(IPCamera, 41, 0);//调用6
                        break;
                    case 17:
                        cameraCommandUtil.postHttp(IPCamera, 51, 0);//调用居中
                        break;
                    case 18:
                        cameraCommandUtil.postHttp(IPCamera, 50, 0);//设置居中
                        break;
                    default:
                        break;
                }
                state_camera = 0;
            }
        }
    });

    // 搜索进度
    private ProgressDialog progressDialog = null;
    // 广播接收器
    private BroadcastReceiver myBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context arg0, Intent arg1) {
            IPCamera = arg1.getStringExtra("IP");
            purecameraip = arg1.getStringExtra("pureip");
            Log.e("camera ip::", "  " + IPCamera);
            if (IPCamera.equals("null:81")) {
                Toast.makeText(MainActivity.this, "摄像头连接不上", Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
            // 如果是串口配置在这里提前启动摄像头驱动，否则是WiFi的话到下个界面再连接
            if (XcApplication.isserial != XcApplication.Mode.SOCKET) {
                useUartCamera();
            }
            phThread.start();
            Log.e(TAG, "IPCamera" + IPCamera);
        }
    };


    // 启动摄像头
    private void useUartCamera() {
        Intent ipintent = new Intent();
        //ComponentName的参数1:目标app的包名,参数2:目标app的Service完整类名
        ipintent.setComponent(new ComponentName("com.android.settings", "com.android.settings.ethernet.CameraInitService"));
        //设置要传送的数据
        ipintent.putExtra("purecameraip", purecameraip);
        startService(ipintent);   //摄像头设为静态192.168.16.20时，可以不用发送
    }
    // 接受显示小车发送的数据
    private Handler rehHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                mByte = (byte[]) msg.obj;
                if (mByte[0] == 0x55) {
                    //坐标信息
                    int buffer = 0;
                    Coordinate = mByte[2] & 0xff;
                    coordinate = (char)(((Coordinate & 0xe0) >> 5) + 0x40) + "";
                    coordinate += (char)(((Coordinate & 0x1c) >> 2) + 0x30);
                    buffer = (int) (Coordinate & 0x03);
                    if (buffer == 0) {
                        coordinate += "U";
                    } else if (buffer == 1) {
                        coordinate += "L";
                    } else if (buffer == 2) {
                        coordinate += "D";
                    } else if (buffer == 3) {
                        coordinate += "R";
                    }
                    // 光敏状态
                    psStatus = mByte[3] & 0xff;
                    // 超声波数据
                    UltraSonic = mByte[5] & 0xff;
                    UltraSonic = UltraSonic << 8;
                    UltraSonic += mByte[4] & 0xff;
                    // 光照强度
                    Light = mByte[7] & 0xff;
                    Light = Light << 8;
                    Light += mByte[6] & 0xff;
                    // 码盘
                    CodedDisk = mByte[9] & 0xff;
                    CodedDisk = CodedDisk << 8;
                    CodedDisk += mByte[8] & 0xff;
                    // Camera_show_ip = IPCamera.substring(0, 14);
                    if (mByte[1] == (byte) 0xaa) {
                        // 显示数据 //"WIFIIP:" + IPCar + "" + "CameraIP" + IPCamera + "\n"
                        textView.setText( "主车:" + "超声波:" + UltraSonic
                                + "mm 光照:" + Light + "lx" + " 码盘:" + CodedDisk
                                + "光敏状态:" + psStatus + "坐标信息:" + coordinate);
                    }
                    if (mByte[1] == (byte) 0x02) {

                        // 显示数据
                        textView.setText("WIFI模块IP:" + IPCar + "\n" + "从车:" + "超声波:" + UltraSonic
                                + "mm 光照:" + Light + "lx" + "码盘:" + CodedDisk
                                + "光敏状态:" + psStatus + "坐标信息:" + coordinate);
                    }
                }
            }else if(msg.what == 18){
                textView.setText(msg.obj + " ");
            }
            // textView.setText("WIFIIP:" + IPCar);
            textView.setText("WIFI模块IP:" + IPCar + "\n" + "从车:" + "超声波:" + UltraSonic
                    + "mm 光照:" + Light + "lx" + "码盘:" + CodedDisk
                    + "光敏状态:" + psStatus + "坐标信息:" + coordinate);
        }
    };
    private boolean flag_data = false;
    private final SerialInputOutputManager.Listener mListener =  //读usb数据
            new SerialInputOutputManager.Listener() {
                @Override
                public void onRunError(Exception e) {
                    Log.e(TAG, "Runner stopped.");
                }

                @Override
                public void onNewData(final byte[] data) {   //读usb数据，通过usb接收小车回传的数据
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {//   usb   接收数据 线程 .
                            Message msg = rehHandler.obtainMessage(1, data);
                            msg.sendToTarget();

                            MainActivity.this.updateReceivedData(data);
                            Log.e("qqqqqqq:", Arrays.toString(data));

                            conneceReceiveSend.Feed_Back_Handle(data);//  调用处理底层数据的方法
                            Log.e("serialdata","串口ok\n");
                            for (int i=0;i<data.length;i++ )
                            {
                                Log.e("serialdata",data[i]+"\n");
                            }
                        }
                    });
                }
            };

    private SerialInputOutputManager mSerialIoManager;
    private UsbDeviceConnection connection;
    private int num1 = 0;
    private UsbManager mUsbManager;
    private final BroadcastReceiver mUsbPermissionActionReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (null != usbDevice) {
                            afterGetUsbPermission(usbDevice);
                        }
                    } else {
                        Toast.makeText(context, String.valueOf("Permission denied for device" + usbDevice), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    };
    private List<UsbSerialPort> mEntries = new ArrayList<UsbSerialPort>();
    private Handler usbHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 2) {
                useUsbtoserial();
            }
        }
    };
    public final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_REFRESH:
                    refreshDeviceList();//扫描usb列表，获取usb设备
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }

    };
    // 得到当前摄像头的图片信息
    public void getBitmap() {
        bitmap = cameraCommandUtil.httpForImage(IPCamera);
        phHandler.sendEmptyMessage(10);
    }
    //opencv初始化
    private void initloadOpencv(){
        boolean success= OpenCVLoader.initDebug();
        if (success){
            Toast.makeText(this.getApplicationContext(),"loading",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this.getApplicationContext(),"sorry",Toast.LENGTH_LONG).show();
        }
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        basiccontrol = new BasicControl();

        setContentView(R.layout.activity_main);



        mContext = this;
        mActivity = this;


        getPermission();//安卓6.0以及以上需要获取动态权限
        initPermission();//安卓6.0以及以上需要获取动态权限

        boolean ret_initss = yolov5ncnn.InitS(getAssets());
        if (!ret_initss)
        {
            Log.e("MainActivity", "yolov5ncnn Init failed");
        }

        boolean ret_init = yolov5ncnn.Init(getAssets());
        if (!ret_init)
        {
            Log.e("MainActivity", "yolov5ncnn Init failed");
        }
        boolean ret_inits = yolov5ncnn.Initss(getAssets());
        if (!ret_inits)
        {
            Log.e("MainActivity", "yolov5ncnn Init failed");
        }

        boolean ret_initsss = yolov5ncnn.Initsss(getAssets());
        if (!ret_initsss)
        {
            Log.e("MainActivity", "yolov5ncnn Init failed");
        }

        boolean ret_initssss = yolov5ncnn.Initssss(getAssets());
        if (!ret_initssss)
        {
            Log.e("MainActivity", "yolov5ncnn Init failed");
        }

        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            // mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }

        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.camera), 1, perms);
        }

//        Thread thread=new Thread(new Runnable() {
//            @Override
//            public void run() {
//                bitmapss= BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/drawable/ab.jpg"));
//                pingmujiequ pingmujiequs=new pingmujiequ();
//                pingmujiequs.init_carpingmujiequ(bitmapss);
//            }
//        });
//
//        thread.start();


//        Thread thread=new Thread(new Runnable() {
//            @Override
//            public void run() {
//                bitmapss= BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/drawable/as.jpg"));
//                cartxsb cartxsbs=new cartxsb();
//                String ops=cartxsbs.init_cartxsb(bitmapss,"红色");
//                int sss=Way.graph_get(ops,1,"圆形");
//
//                System.out.println(sss);
//            }
//        });
//
//        thread.start();






//        Thread thread=new Thread(new Runnable() {
//            @Override
//            public void run() {
//                QR_Code qr_code=new QR_Code();
//                qr_code.QR_ccode(1);
//            }
//        });
//
//        thread.start();


//        Thread thread=new Thread(new Runnable() {
//            @Override
//            public void run() {
//                bitmapss= BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/drawable/u52.png"));
//                carjtbz carjtbzs=new carjtbz();
//                carjtbzs.init_carjtbz(bitmapss);
//            }
//        });
//
//        thread.start();
//
//        Thread threads=new Thread(new Runnable() {
//            @Override
//            public void run() {
//                bitmapss= BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/drawable/hs2.jpg"));
//                cardetect cardetects=new cardetect();
//                cardetects.init_cardete(bitmapss);
//            }
//        });
//
//        threads.start();





        initloadOpencv();


        initCamer();   //初始化摄像头
        wifiInit();///   去监听里执行 判断  使用   wifi 还是usb
        conneceReceiveSend = new ConneceReceiveSend();
        connect_thread();
        initControl(); //初始化控件





        // Clear all setting items to avoid app crashing due to the incorrect settings
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();





        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }
        /*****USB开关监听*****/
        usbswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean a) {
                usbswitchinti(a);
            }
        });
        new ControlCommand().AutoThread.start(); //开启全自动线程
        //usbD_C_Handle.start();
        new ConneceReceiveSend(
        ).reThread.start();  //底层数据
        usb.start();//  开启usb线程
        // new AutoControl(). RouteDesign.start();//  启动路线设计线程

    }

    /**
     * android 6.0 以上需要动态申请权限
     */
    public void initPermission() {
        String[] permissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.READ_PHONE_STATE
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.

            }
        }
        String[] tmpList = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }
    }
    void getPermission() {
        if (ContextCompat.checkSelfPermission(this, CAMERA) != PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, 1);
        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
            }
        }
    }
    public void usbswitchinti(boolean state) {
        ToggleButton button4 = (ToggleButton) findViewById(R.id.usbtoggleButton);
        button4.setChecked(state);
        //判断   state 的两种状态
        if (state) {
            Snackbar.make(getCurrentFocus(), "usb", Snackbar.LENGTH_LONG).show();
            usbOrwifi = 1;//使用usb
            XcApplication.isserial = XcApplication.Mode.USB_SERIAL;
            usbflag = 50;//   改变  此标志 在 线程  中监听
            //Toast.makeText(MainActivity.this, "usb", Toast.LENGTH_LONG).show();
            XcApplication.isserial = XcApplication.Mode.USB_SERIAL;
        } else {
            Snackbar.make(getCurrentFocus(), "wifi", Snackbar.LENGTH_LONG).show();
            XcApplication.isserial = XcApplication.Mode.SOCKET;
            // Toast.makeText(MainActivity.this, "wifi", Toast.LENGTH_LONG).show();
            usbOrwifi = 0;//   s使用wifi
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_3_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }
    private void connect_thread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                conneceReceiveSend.connect(rehHandler, IPCar);
            }
        }).start();
    }
    // 搜索摄像cameraIP进度条
    private void cameraSearch() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("努力搜索中。。。");
        progressDialog.show();
        startService(new Intent().setClass(MainActivity.this, SearchService.class));
    }

    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(myBroadcastReceiver);
        System.exit(0);
        unregisterReceiver(mUsbPermissionActionReceiver);
        try {
            sPort.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sPort = null;
    }
    /*********** 得到服务器的IP地址*********/
    @SuppressLint("WifiManagerLeak")
    public void wifiInit() {
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        dhcpInfo = wifiManager.getDhcpInfo();   //服务管理器
        IPCar = Formatter.formatIpAddress(dhcpInfo.gateway);
        Log.e(TAG, "wifi" + IPCar);
    }

    /*********初始化摄像头*********/
    public void initCamer() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(A_S);
        registerReceiver(myBroadcastReceiver, intentFilter);   // 注册广播
        cameraCommandUtil = new CameraCommandUtil();
        cameraSearch();    //搜索摄像头

    }
    /*********控件初始化*********/
    public void initControl() {
        myImageView = (ImageView) findViewById(R.id.image_view);
        textView = (TextView) findViewById(R.id.textView);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.e);
        final Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mySectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager()); //创建适配器
        // mWebView= (WebView) findViewById(R.id.webview);
        //   mVideoView=(VideoView) findViewById(R.id.videoview);
        usbswitch = (ToggleButton) findViewById(R.id.usbtoggleButton);
        myViewPager = (ViewPager) findViewById(R.id.container);
        myViewPager.setAdapter(mySectionsPagerAdapter);
        down_tablayout = (TabLayout) findViewById(R.id.tabs);
        appbar_layout = (AppBarLayout) findViewById(R.id.appbar);
        main_layout = (CoordinatorLayout) findViewById(R.id.main_content);
        // yanse = (TextView) main_layout.findViewById(R.id.textView);      //颜色测试
//    main_activity = (Layout).findViewById(R.id.main_content);
        /***********************************************************
         *******         界面切换监听改变整体颜色             *******
         ************************************************************/

        myViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                int[] Color = new int[]{
                        0xFFA5DEE4, 0xFFF05E1C, 0xFF66ccFF, 0xFFDB4D6D, 0xFFEEA9A9,
                        0xFFFFB11B, 0xFFB4A582, 0xFFDDD23B, 0xFF86C166,
                        0xFF7BA23F, 0xFF7B90D2, 0xFFB28FCE, 0xFF6A4C9C
                };                                           //界面的颜色组
                Random num = new Random();
                int s = num.nextInt(Color.length - 1);//生成0到Color下标的随机数
                int color = Color[s];                         //产生随机数
                Log.e("onPageSelected: ", s + "");

                Log.e("onPageSelected: ", num + "");
                switch (position) {
                    case 0:
                        down_tablayout.setBackgroundColor(color);                                 //滑动菜单栏的背景色
                        toolbar.setBackgroundColor(color);                                          //工具栏的背景色
                        appbar_layout.setBackgroundColor(color);                                  // 应用程序栏的背景色
                        main_layout.setBackgroundColor(color);                                    // main 活动界面的背景色
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setStatusBarColor(color);                                   //状态栏背景颜色
                        }
                        //  yanse.setText(num + "");
                        break;
                    case 1:
                        down_tablayout.setBackgroundColor(color);
                        toolbar.setBackgroundColor(color);
                        appbar_layout.setBackgroundColor(color);
                        main_layout.setBackgroundColor(color);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setStatusBarColor(color);
                        }
                        // yanse.setText(num + "");
                        break;
                    case 2:
                        down_tablayout.setBackgroundColor(color);
                        toolbar.setBackgroundColor(color);
                        appbar_layout.setBackgroundColor(color);
                        main_layout.setBackgroundColor(color);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setStatusBarColor(color);
                        }
                        //  yanse.setText(num + "");
                        break;
                    case 3:
                        down_tablayout.setBackgroundColor(color);
                        toolbar.setBackgroundColor(color);
                        appbar_layout.setBackgroundColor(color);
                        main_layout.setBackgroundColor(color);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setStatusBarColor(color);
                        }
                        // yanse.setText(num + "");
                        break;
                }
            }
        });
        /*******************************************************************************
         ********************************************************************************/

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(myViewPager);

        /**
         * 摄像头界面左右上下滑动调节
         */
        myImageView.setOnTouchListener(new View.OnTouchListener() { // 触摸滑动监听器
            /********MotionEvent 动作事件********/
            private final int MINLEN = 45; // 滑动的距离
            private float x1 = 0;
            private float x2 = 0;
            private float y1 = 0;
            private float y2 = 0;

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN: //event的值为0x00，向下
                        x1 = event.getX();
                        y1 = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:  //event的值为0x01，向上
                        x2 = event.getX();
                        y2 = event.getY();

                        // 比较X轴发生动作还是Y轴发生动作
                        float xx = x1 > x2 ? x1 - x2 : x2 - x1;
                        float yy = y1 > y2 ? y1 - y2 : y2 - y1;
                        if (xx > yy) { // 比较那个幅度比较大
                            if ((x1 > x2) && (xx > MINLEN)) {// left 左滑动
                                state_camera = 4;
                            } else if ((x1 < x2) && (xx > MINLEN)) {// right 右滑动
                                state_camera = 3;
                            }
                        } else {
                            if ((y1 > y2) && (yy > MINLEN)) {// down 下滑动
                                state_camera = 2;
                            } else if ((y1 < y2) && (yy > MINLEN)) {// up 上滑动
                                state_camera = 1;
                            }
                        }
                        break;
                }
                return true;
            }
        });
    }


    /**
     * 目录操作
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //打开usb设备，对usb参数进行设置。比如波特率、数据位、停止位、校验位
    protected void controlusb() {
        if (sPort == null) {
            Toast.makeText(MainActivity.this, "No serial device.", Toast.LENGTH_SHORT).show();
        } else {
            openUsbDevice();  //打开usb设备
            if (connection == null) {
                mHandler.sendEmptyMessageDelayed(MESSAGE_REFRESH, REFRESH_TIMEOUT_MILLIS);//如果打开不成功，重新获取
                Toast.makeText(MainActivity.this, "Opening device failed", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                sPort.open(connection);  //打开usb端口
                sPort.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);  //设置参数（8数据位 1停止位 无奇偶校验位）
            } catch (IOException e) {
                Toast.makeText(MainActivity.this, "Error opening device: ", Toast.LENGTH_SHORT).show();
                try {
                    sPort.close();
                } catch (IOException e2) {
                }
                sPort = null;
                return;
            }
            Toast.makeText(MainActivity.this, "Serial device: " + sPort.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
        }
        onDeviceStateChange();
        if (sPort != null)
            Toast.makeText(MainActivity.this, "配置成功，请连接usb", Toast.LENGTH_SHORT).show();
        // Transparent.dismiss();//成功设置完usb，关闭等待对话框
    }

    //  //////////打开usb，  弹出选择对话框，尝试获取usb访问权限
    private void openUsbDevice() {
        tryGetUsbPermission();
    }

    //  弹出选择对话框，尝试获取usb访问权限，否则就已经获取了usb权限，
    private void tryGetUsbPermission() {

        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(mUsbPermissionActionReceiver, filter);

        PendingIntent mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        for (final UsbDevice usbDevice : mUsbManager.getDeviceList().values()) {
            if (mUsbManager.hasPermission(usbDevice)) {  //已经获得了权限
                afterGetUsbPermission(usbDevice);   //打开usb
            } else {//还没有获取权限，弹出对话框，请求获取权限
                mUsbManager.requestPermission(usbDevice, mPermissionIntent);
            }
        }
    }

    private void afterGetUsbPermission(UsbDevice usbDevice) {
        Toast.makeText(MainActivity.this, String.valueOf("Found USB device: VID=" + usbDevice.getVendorId() + " PID=" + usbDevice.getProductId()), Toast.LENGTH_LONG).show();
        doYourOpenUsbDevice(usbDevice);
    }

    //打开usb设备
    private void doYourOpenUsbDevice(UsbDevice usbDevice) {
        connection = mUsbManager.openDevice(usbDevice);
    }

    private void stopIoManager() {
        if (mSerialIoManager != null) {
            Log.e(TAG, "Stopping io manager ..");
            mSerialIoManager.stop();
            mSerialIoManager = null;
        }
    }
    private void startIoManager() {
        if (sPort != null) {
            Log.e(TAG, "Starting io manager ..");
            mSerialIoManager = new SerialInputOutputManager(sPort, mListener); //添加监听
            mExecutor.submit(mSerialIoManager);   //在新的线程中监听串口的数据变化
        }
    }

    private void onDeviceStateChange() {
        stopIoManager();
        startIoManager();
    }

    private void updateReceivedData(byte[] data) {
        final String message = "Read " + data.length + " bytes: \n"
                + HexDump.dumpHexString(data) + "\n\n";
        Log.e("read data is :", "  " + message);
    }

    //获取usb相关的一些变量
    private void useUsbtoserial() {
        try {
            final UsbSerialPort port = mEntries.get(0);  //position =0
            final UsbSerialDriver driver = port.getDriver();
            final UsbDevice device = driver.getDevice();
            final String usbid = String.format("Vendor %s  ，Product %s",
                    HexDump.toHexString((short) device.getVendorId()),
                    HexDump.toHexString((short) device.getProductId()));
            MainActivity.sPort = port;
            if (sPort != null) {
                controlusb(); //使用usb功能   ////--------------------------------------------------
            }
        } catch (Exception e) {
            // TODO: handle exception
            Toast.makeText(MainActivity.this, "usb连接失败", Toast.LENGTH_SHORT).show();
        }
    }

    //通过异步任务AsyncTask实现usb的获取
    private void refreshDeviceList() {
        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        new AsyncTask<Void, Void, List<UsbSerialPort>>() {
            @Override
            protected List<UsbSerialPort> doInBackground(Void... params) {
                Log.e(TAG, "Refreshing device list ...");
                final List<UsbSerialDriver> drivers =
                        UsbSerialProber.getDefaultProber().findAllDrivers(mUsbManager);

                final List<UsbSerialPort> result = new ArrayList<UsbSerialPort>();
                for (final UsbSerialDriver driver : drivers) {
                    final List<UsbSerialPort> ports = driver.getPorts();
                    Log.e(TAG, String.format("+ %s: %s port%s",
                            driver, Integer.valueOf(ports.size()), ports.size() == 1 ? "" : "s"));
                    result.addAll(ports);  //保存usb端口
                }
                return result;
            }

            @Override
            protected void onPostExecute(List<UsbSerialPort> result) {
                mEntries.clear();
                mEntries.addAll(result); //保存usb端口
                usbHandler.sendEmptyMessage(2);
                Log.e(TAG, "Done refreshing, " + mEntries.size() + " entries found.");
            }
        }.execute((Void) null);
    }
}