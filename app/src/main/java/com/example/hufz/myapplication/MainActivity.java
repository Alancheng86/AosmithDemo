package com.example.hufz.myapplication;

import java.io.File;

import android.app.Activity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Intent;
import android.view.WindowManager;
import android.app.AlertDialog;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.VideoView;


import java.util.ArrayList;
import android.view.ViewGroup;
import android.os.Handler;
import android.os.Message;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;




public  class MainActivity extends Activity implements  android.view.GestureDetector.OnGestureListener {
    private TextView textView1;
    private GridView gridView1;
    private static Handler handler;
    // 用于存放sdcard卡上的所有图片路径
    public static ArrayList<String> dirAllStrArr = new ArrayList<String>();

    //定义手势检测器实例
    private GestureDetector detector;
    private final String TAG = "main";
    private TempControlView tempControl;
    final private String[] areas = new String[]{"1H", "2H", "3H", "4H", "5H", "6H", "7H", "8H", "9H", "10H", "11H", "12H", "13H", "14H", "15H", "16H", "17H", "18H", "19H", "20H", "21H", "22H", "23H", "24H"};
    final private boolean[] areaState = new boolean[]{false, false, false, false, false, true, true, true, false, false, false, true, true, false, false, false, true, true, true, true, true, false, false, false};
    private ListView areaCheckListView;
    private Button Button;
    private View mContentView;
    private Button button;
    private Button Button_video;
    private boolean bcf = true;
    private VideoView videoView;
    //ActionBar actionBar;
    private boolean isPlaying = false;
    private boolean playState = false;
    private boolean BCChanged = false;
    private ScreenObserver mScreenObserver;

    private ImageView img;
    //private String[] SD_areas = new String[]{"1.png","2.png", "3.png", "4.png", "5.png", "6.png", "7.png", "8.png", "9.png", "10.png"};////可以使用png图片显示，分辨率需对应屏分辨率
    private String[] SD_areas = new String[]{"1.bmp", "2.bmp", "3.bmp", "4.bmp", "5.bmp", "6.bmp", "7.bmp", "8.bmp", "9.bmp", "10.bmp"};/////可以使用bmp图片显示，分辨率需对应屏分辨率
    int Pic_num = 0;
    private String filepath = Environment.getExternalStorageDirectory().getPath() + "/" + SD_areas[Pic_num];



    // 用于遍历sdcard卡上所有文件的类
    public static void DirAll(File dirFile) throws Exception {
        if (dirFile.exists()) {
            File files[] = dirFile.listFiles();
            for (File file : files) {
                String fileName = file.getName();
                String filePath = file.getPath();
                Message msg = new Message();
                msg.obj = "正在读取：" + filePath;
                handler.sendMessage(msg);
                if (file.isDirectory()) {


// 除sdcard上Android这个文件夹以外。
                    if (!fileName.endsWith("Android")) {
// 如果遇到文件夹则递归调用。
                        DirAll(file);
                    }
                } else {
// 如果是图片文件压入数组


                    if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")
                            || fileName.endsWith(".bmp")
                            || fileName.endsWith(".gif")
                            || fileName.endsWith(".png")) {
// 如果遇到文件则放入数组
                        if (dirFile.getPath().endsWith(File.separator)) {
                            dirAllStrArr
                                    .add(dirFile.getPath() + file.getName());
                        } else {
                            dirAllStrArr.add(dirFile.getPath() + File.separator
                                    + file.getName());
                        }
                    }
                }
            }
        }
    }


    // 图片加载的缓存工具类，安卓自带的方法
    public static BitmapFactory.Options getHeapOpts(File file) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
// 数字越大读出的图片占用的heap必须越小，不然总是溢出
        if (file.length() < 20480) { // 0-20k
            opts.inSampleSize = 1;// 这里意为缩放的大小 ，数字越多缩放得越厉害
        } else if (file.length() < 51200) { // 20-50k
            opts.inSampleSize = 2;
        } else if (file.length() < 307200) { // 50-300k
            opts.inSampleSize = 4;
        } else if (file.length() < 819200) { // 300-800k
            opts.inSampleSize = 6;
        } else if (file.length() < 1048576) { // 800-1024k
            opts.inSampleSize = 8;
        } else {
            opts.inSampleSize = 10;
        }
        return opts;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        //创建手势检测器
        detector = new GestureDetector(this, this);
/*
        Image_show();/////放置在此处是屏蔽掉其余UI的效果。
        Log.i(TAG, "Bitmap show is on");

        button = (Button) findViewById(R.id.button);
        mContentView = findViewById(R.id.fullscreen_content_controls);
        tempControl = (TempControlView) findViewById(R.id.temp_control);
        tempControl.setTemp(25, 85, 25);
        tempControl.setOnTempChangeListener(new TempControlView.OnTempChangeListener() {
            @Override
            public void change(int temp) {
                //  Toast.makeText(MainActivity.this, temp + "°", Toast.LENGTH_SHORT).show();
            }
        });
        videoView = (VideoView) findViewById(R.id.videoView);
        Button = (Button) findViewById(R.id.Button);
        Button_video = (Button) findViewById(R.id.button_video);
        // actionBar=getActionBar();
        //actionBar.hide();
        Button_video.setOnClickListener(new but_videoplay());
        Button.setOnClickListener(new CheckBoxClickListener());
        //---屏幕监听，-关闭屏幕后，停止视频播放---------------------------------------------------
        /*通过BroadcastReceiver接收广播Intent.ACTION_SCREEN_ON和Intent.ACTION_SCREEN_OFF可以判断屏
        幕状态是否锁屏，但是只有屏幕状态发生改变时才会发出广播；*/
   /*     mScreenObserver = new ScreenObserver(this);
        mScreenObserver.requestScreenStateUpdate(new ScreenObserver.ScreenStateListener() {
            @Override
            public void onScreenOn() {
                doSomethingOnScreenOn();
            }

            @Override
            public void onScreenOff() {
                doSomethingOnScreenOff();
            }
        });
*/
    /* 测试新函数开始*/
        Thread readSdcard = new Thread() {
            private String sdpath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath();// 获取sdcard的根路径
            private File dirFile = new File(sdpath);


            public void run() {
                try {
                    DirAll(dirFile);
                    Message msg = new Message();
                    msg.obj = "0";
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            ;
        };
        textView1.setVisibility(View.VISIBLE);
        readSdcard.start();


/* 遍历sdcard旗下的所有文件夹结束 */
// 不停在接受定时器的消息，根据消息的参数，进行处理


        handler = new Handler(new Handler.Callback() {// 这样写，就不弹出什么泄漏的警告了
            @Override
            public boolean handleMessage(Message msg) {
                textView1.setText(msg.obj + "");
                if (msg.obj.equals("0")) {
                    textView1.setVisibility(View.GONE);
                    gridView1.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });


        BaseAdapter baseAdapter = new BaseAdapter() {


            @Override
            public View getView(int position, View convertView, ViewGroup arg2) {
                ImageView imageView1;
                if (convertView == null) {
                    imageView1 = new ImageView(MainActivity.this);
                    imageView1.setAdjustViewBounds(true);// 自动缩放为宽高比
                    imageView1.setScaleType(ScaleType.CENTER_INSIDE);// 设置图片保持宽高比显示
                    imageView1.setPadding(5, 5, 5, 5);
                } else {
                    imageView1 = (ImageView) convertView;
                }
                String filePath = dirAllStrArr.get(position);
                File file = new File(filePath);
                Bitmap bm = BitmapFactory.decodeFile(filePath,
                        getHeapOpts(file));
                imageView1.setImageBitmap(bm);


                return imageView1;
            }


            // 获取当前选项
            @Override
            public long getItemId(int position) {
                return position;
            }


            @Override
            public Object getItem(int position) {
                return position;
            }


            // 获取数量
            @Override
            public int getCount() {
                return dirAllStrArr.size();
            }

        };
        gridView1.setAdapter(baseAdapter);// 把适配器与网格视图链接起来
       /* gridView1.setOnItemClickListener(new OnItemClickListener() {// 点击网格组件的任意一张图片时候的事件
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position,// position为点击的id
                                    long arg3) {
                Intent intent = new Intent(MainActivity.this,
                        ViewActivity.class);// 激活ViewActivity
                Bundle bundle = new Bundle();
                bundle.putString("imgPath", dirAllStrArr.get(position));// 传递点击的图片的id到ViewActivity
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });*/

    }

    private void doSomethingOnScreenOn() {
        Log.i(TAG, "Screen is on");
        if (playState) {
            play(0);
        }
    }

    private void doSomethingOnScreenOff() {
        Log.i(TAG, "Screen is off");
        stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //停止监听screen状态
        mScreenObserver.stopScreenStateUpdate();
    }

    //------------------------------------------------------------------------------------------
    //将该activity上的触碰事件交给GestureDetector处理
    public boolean onTouchEvent(MotionEvent me) {
        return detector.onTouchEvent(me);
    }

    @Override
    public boolean onDown(MotionEvent arg0) {
        return false;
    }

    /**
     * 滑屏监测
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float minMove = 120;         //最小滑动距离
        float minVelocity = 0;      //最小滑动速度
        float beginX = e1.getX();
        float endX = e2.getX();
        float beginY = e1.getY();
        float endY = e2.getY();

        if (beginX - endX > minMove && Math.abs(velocityX) > minVelocity) {   //左滑
            tempControl.setVisibility(View.INVISIBLE);
            Button.setVisibility(View.INVISIBLE);
            videoView.setVisibility(View.VISIBLE);
            //Button_video.setVisibility(View.INVISIBLE);
            button.setVisibility(View.INVISIBLE);
            playState = true;
            if (bcf) {
                mContentView.setBackgroundColor(0xff000000);
                //bcf=!bcf;
                BCChanged = true;
                Log.i(TAG, "--setBackgroundColor(0xff000000)--");
            }
            Pic_num--;
            if (Pic_num < 0) {
                Pic_num = SD_areas.length - 1;
            }
            Image_show();
            //play(0);
            //Toast.makeText(this,velocityX+"左滑",Toast.LENGTH_SHORT).show();
        } else if (endX - beginX > minMove && Math.abs(velocityX) > minVelocity) {   //右滑
            //stop();
            tempControl.setVisibility(View.VISIBLE);
            Button.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.INVISIBLE);
            // Button_video.setVisibility(View.VISIBLE);
            //button.setVisibility(View.VISIBLE);
            playState = false;
            Log.i(TAG, "----右滑，Play stoped,playstate=false");

            if (BCChanged) {
                mContentView.setBackgroundColor(0xECF4F9);
                BCChanged = !BCChanged;
                Log.i(TAG, "BCChanged,---:setBackgroundColor(0xECF4F9)---");
            }
            Pic_num++;
            if (Pic_num > SD_areas.length - 1) {
                Pic_num = 0;
            }
            Image_show();
            Log.i(TAG, "----右滑，image  show test");
            //Toast.makeText(this,velocityX+"右滑",Toast.LENGTH_SHORT).show();
        } else if (beginY - endY > minMove && Math.abs(velocityY) > minVelocity) {   //上滑
            // Toast.makeText(this,velocityX+"上滑",Toast.LENGTH_SHORT).show();
            SetBc(mContentView);
        } else if (endY - beginY > minMove && Math.abs(velocityY) > minVelocity) {   //下滑
            // Toast.makeText(this,velocityX+"下滑",Toast.LENGTH_SHORT).show();
            SetBc(mContentView);
        }

        return false;
    }

    @Override
    public void onShowPress(MotionEvent arg0) {
        // TODO Auto-generated method stub
        //Toast.makeText(this,"press",Toast.LENGTH_SHORT).show();
        SetBc(mContentView);

    }

    @Override
    public boolean onSingleTapUp(MotionEvent arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onLongPress(MotionEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float velocityX,
                            float velocityY) {

        return false;
    }


    //----------------------------------------------------------------------
    private void play(int msec) {
        // Log.i(TAG, "------------------play-------------------------------");
        String path = Environment.getExternalStorageDirectory().getPath() + "/" + "1.mp4";
        //  et_path.getText().toString().trim();
        //String path = "/mnt/sdcard/1.mp4";  //  et_path.getText().toString().trim();
        Log.i(TAG, "playPath:" + path);
        File file = new File(path);
        if (!file.exists()) {
            Toast.makeText(this, "File Not exists", Toast.LENGTH_SHORT).show();
            return;
        }

        // Log.i(TAG, "------play----文件存在");
        videoView.setVideoPath(file.getAbsolutePath());
        //Log.i(TAG, "------play----已经设定文件地址");
        videoView.seekTo(msec);
        videoView.start();
        isPlaying = true;
        playState = true;
        Log.i(TAG, "------------------play-------------------------------");
        //vv_video.onTouchEvent()
        //

        //
        //seekBar.setMax(vv_video.getDuration());

        //
		/*new Thread() {

			@Override
			public void run() {
				try {
					isPlaying = true;
					while (isPlaying) {

						int current = vv_video.getCurrentPosition();
						//seekBar.setProgress(current);

						sleep(500);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();*/
        //
        // btn_play.setEnabled(false);

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            //监控播放结束，播放结束后自动重播
            @Override
            public void onCompletion(MediaPlayer mp) {
                //
                //btn_play.setEnabled(true);
                // stop();
                //videoView.start();
                play(0);

            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                //
                play(0);
                isPlaying = false;
                return false;
            }
        });
    }

	/*@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(vv_video.isPlaying()){
			vv_video.stopPlayback();
		}
		//vv_video.release();
		//Activity销毁时停止播放，释放资源。不做这个操作，即使退出还是能听到视频播放的声音
	}*/

    /*protected void replay() {
        if (videoView != null && videoView.isPlaying()) {
            videoView.seekTo(3000);
            Toast.makeText(this, "循环", Toast.LENGTH_LONG).show();
            //btn_pause.setText("||");
            return;
        }
        isPlaying = false;
        play(0);

    }*/

    /**
     * ��ͣ�����
     */
	/*protected void pause() {
		if (btn_pause.getText().toString().trim().equals("����")) {
			btn_pause.setText("��ͣ");
			vv_video.start();
			Toast.makeText(this, "��������", 0).show();
			return;
		}
		if (vv_video != null && vv_video.isPlaying()) {
			vv_video.pause();
			btn_pause.setText("����");
			Toast.makeText(this, "��ͣ����", 0).show();
		}
	}*/

	/*
	 * ֹͣ����
	 */
    private void stop() {
        if (videoView != null && videoView.isPlaying()) {
            videoView.stopPlayback();
            videoView.setEnabled(true);
            isPlaying = false;
            // playState=false;
            // Log.i(TAG, "----------------Stop--play---Playstate=:"+playState);
        }
    }

    //---------------------------------------------------------------------------------------------
    private class but_videoplay implements OnClickListener {
        //通过按钮切换到VideoViewActivity
        public void onClick(View v) {
            Intent intent;
            intent = new Intent(MainActivity.this, VideoViewActivity.class);
            startActivity(intent);
        }
    }

/*
    // 用于遍历sdcard卡上所有文件的类
    public static void DirAll(File dirFile) throws Exception {
        if (dirFile.exists()) {
            File files[] = dirFile.listFiles();
            for (File file : files) {
                String fileName = file.getName();
                String filePath = file.getPath();
                Message msg = new Message();
                msg.obj = "正在读取：" + filePath;
                handler.sendMessage(msg);
                if (file.isDirectory()) {


// 除sdcard上Android这个文件夹以外。
                    if (!fileName.endsWith("Android")) {
// 如果遇到文件夹则递归调用。
                        DirAll(file);
                    }
                } else {
// 如果是图片文件压入数组


                    if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")
                            || fileName.endsWith(".bmp")
                            || fileName.endsWith(".gif")
                            || fileName.endsWith(".png")) {
// 如果遇到文件则放入数组
                        if (dirFile.getPath().endsWith(File.separator)) {
                            dirAllStrArr
                                    .add(dirFile.getPath() + file.getName());
                        } else {
                            dirAllStrArr.add(dirFile.getPath() + File.separator
                                    + file.getName());
                        }
                    }
                }
            }
        }
    }


    // 图片加载的缓存工具类，安卓自带的方法
    public static BitmapFactory.Options getHeapOpts(File file) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
// 数字越大读出的图片占用的heap必须越小，不然总是溢出
        if (file.length() < 20480) { // 0-20k
            opts.inSampleSize = 1;// 这里意为缩放的大小 ，数字越多缩放得越厉害
        } else if (file.length() < 51200) { // 20-50k
            opts.inSampleSize = 2;
        } else if (file.length() < 307200) { // 50-300k
            opts.inSampleSize = 4;
        } else if (file.length() < 819200) { // 300-800k
            opts.inSampleSize = 6;
        } else if (file.length() < 1048576) { // 800-1024k
            opts.inSampleSize = 8;
        } else {
            opts.inSampleSize = 10;
        }
        return opts;
    }
    */
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // textView1 = (TextView) findViewById(R.id.textView1);
       // gridView1 = (GridView) findViewById(R.id.gridView1);
        gridView1.setVisibility(View.INVISIBLE);
/* 遍历sdcard旗下的所有文件夹开始 */

/*
        Thread readSdcard = new Thread() {
            private String sdpath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath();// 获取sdcard的根路径
            private File dirFile = new File(sdpath);


            public void run() {
                try {
                    DirAll(dirFile);
                    Message msg = new Message();
                    msg.obj = "0";
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            ;
        };
        textView1.setVisibility(View.VISIBLE);
        readSdcard.start();


/* 遍历sdcard旗下的所有文件夹结束 */
// 不停在接受定时器的消息，根据消息的参数，进行处理

/*
        handler = new Handler(new Handler.Callback() {// 这样写，就不弹出什么泄漏的警告了
            @Override
            public boolean handleMessage(Message msg) {
                textView1.setText(msg.obj + "");
                if (msg.obj.equals("0")) {
                    textView1.setVisibility(View.GONE);
                    gridView1.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });


        BaseAdapter baseAdapter = new BaseAdapter() {


            @Override
            public View getView(int position, View convertView, ViewGroup arg2) {
                ImageView imageView1;
                if (convertView == null) {
                    imageView1 = new ImageView(MainActivity.this);
                    imageView1.setAdjustViewBounds(true);// 自动缩放为宽高比
                    imageView1.setScaleType(ScaleType.CENTER_INSIDE);// 设置图片保持宽高比显示
                    imageView1.setPadding(5, 5, 5, 5);
                } else {
                    imageView1 = (ImageView) convertView;
                }
                String filePath = dirAllStrArr.get(position);
                File file = new File(filePath);
                Bitmap bm = BitmapFactory.decodeFile(filePath,
                        getHeapOpts(file));
                imageView1.setImageBitmap(bm);


                return imageView1;
            }


            // 获取当前选项
            @Override
            public long getItemId(int position) {
                return position;
            }


            @Override
            public Object getItem(int position) {
                return position;
            }


            // 获取数量
            @Override
            public int getCount() {
                return dirAllStrArr.size();
            }

        };
        gridView1.setAdapter(baseAdapter);// 把适配器与网格视图链接起来
      /*  gridView1.setOnItemClickListener(new OnItemClickListener() {// 点击网格组件的任意一张图片时候的事件
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position,// position为点击的id
                                    long arg3) {
                Intent intent = new Intent(MainActivity.this,
                        ViewActivity.class);// 激活ViewActivity
                Bundle bundle = new Bundle();
                bundle.putString("imgPath", dirAllStrArr.get(position));// 传递点击的图片的id到ViewActivity
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });*/ /*
    }
*/
    // 创建menu的方法，没有该方法，不会在右上角设置菜单。






    // 对物理按钮的监听



    private void Image_show(){

        /////SD 图片读取测试
        //setContentView(R.layout.activity_main);
        img = (ImageView) findViewById(R.id.image);
        /*Pic_num++;
        if(Pic_num>=3){Pic_num=0;}*/


        filepath = Environment.getExternalStorageDirectory().getPath()+"/"+SD_areas[Pic_num];
        File file = new File(filepath);
        if (file.exists()) {
            //Log.i(TAG, "Bitmap bm = BitmapFactory.decodeFile(filepath); is on");
            Bitmap bm = BitmapFactory.decodeFile(filepath);
            //将图片显示到ImageView中
            img.setImageBitmap(bm);
        };

        /*    Intent intent;
            intent=new Intent(MainActivity.this, ImageViewActivity.class);
            startActivity(intent);
*/
        /** Called when the activity is first created. */
 /*       private ImageView img;

        private final String TAG = "main";

        //SD图片路径
        //private String filepath = "/sdcard/1.png";
        private String filepath = Environment.getExternalStorageDirectory().getPath()+"/"+"1.jpg";

        //Log.i(TAG, "------------------play-------------------------------");
        //Log.i(TAG, "playPath:"+filepath);
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            img = (ImageView) findViewById(R.id.image);
            File file = new File(filepath);
            if (file.exists()) {
                Bitmap bm = BitmapFactory.decodeFile(filepath);
                //将图片显示到ImageView中
                img.setImageBitmap(bm);
            }
        }
*/    }




    //@Override
   /* public void onClick(View v) {
        //通过按钮切换到VideoViewActivity
        Intent intent = null;
        switch (v.getId()) {
            case R.id.button_video:
                intent=new Intent(MainActivity.this, VideoViewActivity.class);
                startActivity(intent);
                break;
		*//*case R.id.btn_controller:
			intent=new Intent(MainActivity.this, ControllerActivity.class);
			startActivity(intent);
			break;*//*
            default:
                break;
        }

    }*/
    //--------------------------------------------------------------------------------

    public void SetBc (View vt){
       //改变背景色
       if(bcf) {mContentView.setBackgroundColor(0xff000000);
           bcf=!bcf;
       }
       else {mContentView.setBackgroundColor(0xECF4F9);
           bcf=!bcf;
       }
    }


    private class CheckBoxClickListener implements OnClickListener{
        @Override
        public void onClick(View v) {
            AlertDialog ad = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("运行时间")
                    .setMultiChoiceItems(areas,areaState,new DialogInterface.OnMultiChoiceClickListener(){
                        public void onClick(DialogInterface dialog, int whichButton, boolean isChecked){
                            //点击某个区域
                        }
                    }).setPositiveButton("确定",new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog,int whichButton){
                            String s = "您选择了:";
                            for (int i = 0; i < areas.length; i++){
                                if (areaCheckListView.getCheckedItemPositions().get(i)){
                                    s += i + ":"+ areaCheckListView.getAdapter().getItem(i)+ "  ";
                                }else{
                                    areaCheckListView.getCheckedItemPositions().get(i,false);
                                }
                            }
                            if (areaCheckListView.getCheckedItemPositions().size() > 0){
                                //Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
                            }else{
                                //没有选择
                            }
                            dialog.dismiss();
                        }
                    }).setNegativeButton("取消", null).create();
            areaCheckListView = ad.getListView();
            ad.show();
        }
    }



}
