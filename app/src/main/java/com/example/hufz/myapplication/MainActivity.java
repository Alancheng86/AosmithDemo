package com.example.hufz.myapplication;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class MainActivity extends Activity {
    // 要切换的照片，放在drawable文件夹下
    int[] images = { R.drawable.a001,R.drawable.a002, R.drawable.a003, R.drawable.a004, R.drawable.a005,R.drawable.a006,R.drawable.a007,R.drawable.a008, R.drawable.a009,  };
    // Message传递标志
    int SIGN = 16;
    // 照片索引
    int num = 0;

    int Time_set1 = 2000;
    int Time_set2 = 1500;

    View mview;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        final ImageView image = (ImageView) findViewById(R.id.image);
        mview=findViewById(R.id.mview);
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                if (msg.what == SIGN) {
                    image.setImageResource(images[num++]);

                    if (num >= images.length) {
                        num = 0;
                        Time_set1 = 2000;
                        Time_set2 = 1500;
                        mview.setBackgroundColor(0xffFFFFff);
                        //   mview.setBackgroundColor(0xffff00ff);
                    }
//                    num++;
//                    if(num==1){//////R
//                        mview.setBackgroundColor(0xffff0000);
//                    }
//                    if(num==2){//////G
//                        mview.setBackgroundColor(0xff00ff00);
//                    }
//                    if(num==3){//////B
//                        mview.setBackgroundColor(0xff0000ff);
//                    }
//                    if(num==4){//////WHITE
//                        mview.setBackgroundColor(0xffFFFFff);
//                    }
//                    if(num==5){//////BLACK
//                        mview.setBackgroundColor(0xff000000);
//                    }
//                    if(num==6){//////GRAY
//                        mview.setBackgroundColor(0xff7f7f7f);
//                    }
                    if(num>=7){//////WHITE
                        Time_set1 = 8000;////////延时看似无效果？？？参数无效，直接改回了数值输入。。
                        Time_set2 = 5000;

//                        image.setVisibility(View.VISIBLE);
////                        image.setImageResource(images[num]);
//                        mview.setBackgroundColor(0xffff00ff);
                    }
//                    if(num==8){//////WHITE
//                        image.setImageResource(images[num]);
//                        mview.setBackgroundColor(0xff00ffff);
//                        image.setVisibility(View.INVISIBLE);;
//                    }
//                    if(num==9){//////WHITE
//                        image.setImageResource(images[num]);
//                        mview.setBackgroundColor(0xff00ffff);
//
//                       // mview.setBackgroundColor(0xffFFFFff);
//                    }

                }
            }
        };

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Message msg = new Message();
                msg.what = SIGN;
                handler.sendMessage(msg);
            }
        }, 3000, 3000);
    }
}