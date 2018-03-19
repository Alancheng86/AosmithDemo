package com.example.hufz.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.View;

/**
 * Created by Administrator on 2018/3/19.
 */


public class DrawView extends View {

    public DrawView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 方法 说明 drawRect 绘制矩形 drawCircle 绘制圆形 drawOval 绘制椭圆 drawPath 绘制任意多边形
         * drawLine 绘制直线 drawPoin 绘制点
         */
        // 创建笔刷
        Paint p = new Paint();

        p.setColor(Color.BLACK);/////画出黑色的底画面；
        p.setStyle(Paint.Style.FILL);//设置填满
        canvas.drawRect(0, 0, 720, 1280, p);// 长方形，长是right-left，宽是bottom-top

        p.setColor(Color.BLUE);
        p.setStrokeWidth(4.0f);
        p.setTextSize(30f);

        canvas.drawText("画直线、斜直线及弧线：", 10, 20, p);// 画文本
        canvas.drawLine(70, 30, 120, 30, p);// 画线
        canvas.drawLine(130, 30, 140, 70, p);// 斜线

        p.setColor(Color.RED);
        canvas.drawText("画实心圆及空心圆：", 30, 100, p);//参数里定义是绘制文字的起点坐标
        canvas.drawCircle(80, 170, 50, p);// 画圆，圆心的坐标(cx,cy)和半径radius
        p.setStyle(Paint.Style.STROKE);//设置空心
        canvas.drawCircle(180, 170, 40, p);// 画圆

        //画弧线
        p.setColor(Color.YELLOW);
        p.setStyle(Paint.Style.STROKE);//设置空心
        RectF rectf=new RectF(150,20,180,40);
        canvas.drawArc(rectf, 180, 180, false, p);
        rectf.set(190, 20, 220, 40);
        canvas.drawArc(rectf, 180, 180, false, p);
        rectf.set(160, 30, 210, 60);
        canvas.drawArc(rectf, 0, 180, true, p);

        p.setColor(Color.GREEN);
        p.setStyle(Paint.Style.FILL);//设置填满
        canvas.drawText("画矩形及圆角矩形：", 10, 260, p);

        canvas.drawRect(10, 360, 180, 290, p);// 长方形，长是right-left，宽是bottom-top
        canvas.drawRect(210, 360, 280, 290, p);// 正方形，长是right-left，宽是bottom-top

        canvas.drawRoundRect(new RectF(290, 260, 350, 330), 10, 15, p);//第一个参数是RectF rect:要画的矩形，第二个参数是x半径，第三个参数是y半径

        //设置红色阴影
        //p.setShadowLayer(10, 15, 15, Color.RED);
        canvas.drawText("画扇形和椭圆:", 10, 390, p);
        // 设置渐变色 这个正方形的颜色是改变的
        Shader mShader = new LinearGradient(0, 0, 100, 100,
                new int[] { Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW,
                        Color.LTGRAY }, null, Shader.TileMode.REPEAT); // 一个材质,打造出一个线性梯度沿著一条线。
        Paint p2=new Paint();
        p2.setShader(mShader);
        RectF oval2 = new RectF(60, 400, 180, 520);// 设置个新的长方形，扫描测量
        canvas.drawArc(oval2, 180, 130, true, p2);
        // 画弧，第一个参数是RectF：该类是第二个参数是角度的开始，第三个参数是多少度，第四个参数是真的时候画扇形，是假的时候画弧线
        //画椭圆，把oval改一下
        oval2.set(210,420,350,460);
        canvas.drawOval(oval2, p);

        // 你可以绘制很多任意多边形，比如下面画六连形
        p.reset();//重置
        p.setColor(Color.LTGRAY);
        p.setStyle(Paint.Style.STROKE);//设置空心
        Path path1=new Path();
        path1.moveTo(180, 200);
        path1.lineTo(200, 200);
        path1.lineTo(210, 210);
        path1.lineTo(200, 220);
        path1.lineTo(180, 220);
        path1.lineTo(170, 210);
        path1.close();//封闭
        canvas.drawPath(path1, p);

        /** Path类封装复合(多轮廓几何图形的路径
         * 由直线段*、二次曲线,和三次方曲线，也可画以油画。drawPath(路径、油漆),要么已填充的或抚摸
         * (基于油漆的风格),或者可以用于剪断或画画的文本在路径。
         */
        // 绘制这个三角形,你可以绘制任意多边形
        p.setColor(Color.GRAY);
        canvas.drawText("通过Path画任意多边形：", 10, 500, p);
        Path path = new Path();

        path.moveTo(80, 500);// 此点为多边形的起点
        path.lineTo(10, 550);
        path.lineTo(140, 550);
        path.close(); // 使这些点构成封闭的路径记得到多边形
        canvas.drawPath(path, p);

        //画曲线
        p.reset();
        p.setColor(Color.GREEN);
        p.setStrokeWidth(4.0f);
        p.setTextSize(30f);
        canvas.drawText("画曲线和点:", 10, 570, p);
        Path path2=new Path();
        p.setStyle(Paint.Style.STROKE);
        path2.moveTo(100, 580);//设置Path的起点
        path2.quadTo(150, 610, 190, 740); //设置曲线的控制点坐标和终点坐标
        canvas.drawPath(path2, p);//画出曲线


        //画贝塞尔曲线
        canvas.drawText("画贝塞尔曲线:", 10, 310, p);
        p.reset();
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.GREEN);
        Path path3=new Path();
        path3.moveTo(100, 320);//设置Path的起点
        path3.quadTo(150, 310, 170, 400); //设置贝塞尔曲线的控制点坐标和终点坐标
        canvas.drawPath(path3, p);//画出贝塞尔曲线

        //画点
        p.setStyle(Paint.Style.FILL);
        p.setStrokeWidth(6.0f);
        canvas.drawPoint(190, 570, p);//画一个点
        p.setColor(Color.CYAN);
        canvas.drawPoints(new float[]{60,600,68,620,80,590}, p);//画多个点

        //画图片，就是贴图
        canvas.drawText("画图：", 200, 570, p);
       //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pic3);
        //canvas.drawBitmap(bitmap, 210,600, p);
    }
}
