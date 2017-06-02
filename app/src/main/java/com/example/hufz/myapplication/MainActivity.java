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
    int[] images = { R.drawable.a2, R.drawable.a3, R.drawable.a4, };
    // Message传递标志
    int SIGN = 16;
    // 照片索引
    int num = 0;
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
                        mview.setBackgroundColor(0xffff00ff);
                    }
                    if(num==1){
                        mview.setBackgroundColor(0xff00ff00);
                    }
                    if(num==2){
                        mview.setBackgroundColor(0xff0000ff);
                    }

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
        }, 2000, 1500);
    }
}