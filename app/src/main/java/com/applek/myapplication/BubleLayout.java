package com.applek.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by wang_gp on 2016/12/26.
 * 从下向上的气泡  测试项目
 */

public class BubleLayout extends View {

    private boolean isStarting;
    private List<Buble> bubles = new ArrayList<>();
    private int width;
    private int height;

    public BubleLayout(Context context) {
        this(context, null);
    }

    public BubleLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    private void init() {

            isStarting = true;
            final Random random = new Random();
            new Thread(new Runnable() {
                @Override
                public void run() {

                    while (true) {
                        if(isStarting) {

                            try {
                                Thread.sleep(random.nextInt(3) * 300);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            Log.e("Sleep", "-----" + bubles.size());
                            Buble bubble = new Buble();
                            int radius = random.nextInt(30);
                            while (radius == 0) {
                                radius = random.nextInt(30);
                            }
                            float speedY = random.nextFloat() * 5;
                            while (speedY < 1) {
                                speedY = random.nextFloat() * 5;
                            }
                            bubble.setRadiu(radius);
                            bubble.setSpeedY(speedY);
                            bubble.setStartX(width / 2);
                            bubble.setStartY(height);
                            float speedX = random.nextFloat() - 0.5f;
                            while (speedX == 0) {
                                speedX = random.nextFloat() - 0.5f;
                            }
                            bubble.setSpeedX(speedX * 2);
                            bubles.add(bubble);
                        }
                    }
                }
            }).start();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Paint paint = new Paint();
        // 绘制渐变正方形
        Shader shader = new LinearGradient(0, 0, 0, height, new int[] {
                getResources().getColor(R.color.blue_bright),
                getResources().getColor(R.color.blue_light),
                getResources().getColor(R.color.blue_dark) },
                null, Shader.TileMode.REPEAT);
        paint.setShader(shader);
        canvas.drawRect(0,0,width,height,paint);
        paint.reset();
        paint.setColor(Color.WHITE);
        paint.setAlpha(200);
        ArrayList<Buble> bubles = new ArrayList<>(this.bubles);
        for (Buble buble : bubles) {
            if(buble.getStartY() - buble.getSpeedY() <= 0){
                this.bubles.remove(buble);
            }else{
                int i = bubles.indexOf(buble);
                if (buble.getStartX() + buble.getSpeedX() <= buble.getRadiu()) {
                    this.bubles.remove(buble);
                } else if (buble.getStartX() + buble.getSpeedX() >= width
                        - buble.getRadiu()) {
                    this.bubles.remove(buble);
                } else {
                    buble.setStartX(buble.getStartX() + buble.getSpeedX());
                }
                buble.setStartY(buble.getStartY() - buble.getSpeedY());
                bubles.set(i, buble);
                canvas.drawCircle(buble.getStartX(), buble.getStartY(),
                        buble.getRadiu(), paint);
            }

        }
        invalidate();

    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        isStarting = hasWindowFocus;
        super.onWindowFocusChanged(hasWindowFocus);
    }


    public class Buble {

        private float startX;
        private float startY;
        private float speedY;
        private float speedX;
        private float radiu;

        public float getStartX() {
            return startX;
        }

        public void setStartX(float startX) {
            this.startX = startX;
        }

        public float getStartY() {
            return startY;
        }

        public void setStartY(float startY) {
            this.startY = startY;
        }

        public float getSpeedY() {
            return speedY;
        }

        public void setSpeedY(float speedY) {
            this.speedY = speedY;
        }

        public float getSpeedX() {
            return speedX;
        }

        public void setSpeedX(float speedX) {
            this.speedX = speedX;
        }

        public float getRadiu() {
            return radiu;
        }

        public void setRadiu(float radiu) {
            this.radiu = radiu;
        }
    }

}
