package com.example.gametest1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.gametest1.view.GameView1;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewManager;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    Button btn_start;
    GameView1 view, view1, view2;
    private static final int SENSOR_SHAKE = 0x283723;
    private SensorManager sensorManager;

    int width, height;//屏幕宽和高
    List<Point[]> points;//骰子起始位置
    Context context;
    int count = 0;
//    private Vibrator vibrator; 

    private SoundPool sp;//声明一个SoundPool
    private int music, location;//定义一个整型用load（）；来设置suondID
    private Handler hand;
    private AnimationSet animationSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        animationSet = new AnimationSet(true);
        hand = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Integer[] num = new Integer[]{view.indexs[0] + 1, view1.indexs[0] + 1, view2.indexs[0] + 1};
                Arrays.sort(num);
                String str = num[0] + "+" + num[1] + "+" + num[2] + "=" +
                        (num[0] + num[1] + num[2]);
                Toast.makeText(context, str, 6000).show();


                TranslateAnimation hideAciton = new TranslateAnimation(
                        0, 100, 0, 100);
                hideAciton.setDuration(500);

                Animation animation = new ScaleAnimation(1, 0, 1, 0);
                animation.setDuration(500);
                animation.setFillAfter(true);

                animationSet.addAnimation(animation);
                animationSet.addAnimation(hideAciton);

                animationSet.setFillAfter(true);

                view.startAnimation(animationSet);
                view1.startAnimation(animationSet);
                view2.startAnimation(animationSet);
                sp.play(location, 1, 1, 0, 0, 1);
//				final ViewManager mViewManager = (ViewManager) getParent();
//				hand.postDelayed(new Runnable() {
//					
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						mViewManager.removeView(view);//移除view
//						mViewManager.removeView(view1);//移除view
//						mViewManager.removeView(view2);//移除view
//					}
//				}, 1000);
            }
        };
        sp = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        music = sp.load(this, R.raw.rotate, 1); //把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
        location = sp.load(context, R.raw.location, 1);
        btn_start = (Button) this.findViewById(R.id.btn_start);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);  

        DisplayMetrics dm = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(dm);

        width = dm.widthPixels;//屏幕宽度

        height = dm.heightPixels;//屏幕高度
        points = new ArrayList<Point[]>();
        points.add(new Point[]{new Point(width / 5, 5 * height / 24)});
        points.add(new Point[]{new Point(6 * width / 12, 5 * height / 24)});
        points.add(new Point[]{new Point(8 * width / 24, 9 * height / 24)});
//		points = new Point[]{new Point[]{new Point(width/5, 5*height/24)},new Point[]{new Point(8*width/24, 9*height/24)},new Point[]{new Point(6*width/12, 5*height/24)}};

//		view = new GameView(context, points.get(0),width,height,sp,0);
//		view1 = new GameView(context, points.get(1),width,height,sp,1);
//		view2 = new GameView(context, points.get(2),width,height,sp,2);
        btn_start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//				 Animation animation = AnimationUtils.loadAnimation(context, R.anim.alpha_scale_rotate);
//				 animation.setFillAfter(true);
//				 btn_start.setAnimation(animation);
//				 btn_start.startAnimation(animation);
                if (count == 0) {
                    count++;
                    sp.play(music, 1, 1, 0, 0, 1);
                    view = new GameView1(context, points.get(0), width, height, sp, 0, hand);
                    view1 = new GameView1(context, points.get(1), width, height, sp, 1, hand);
                    view2 = new GameView1(context, points.get(2), width, height, sp, 2, hand);
                    addContentView(view, new LayoutParams(-2, -2));
                    addContentView(view1, new LayoutParams(-2, -2));
                    addContentView(view2, new LayoutParams(-2, -2));
                    view.startThread();
                    view1.startThread();
                    view2.startThread();

                } else {
                    Log.e("连续点击", (view != null && !view.flag) + "");
                    if (view != null && !view.flag) {//如果上次的线程没有跑了才允许开启此次线程
                        sp.play(music, 1, 1, 0, 0, 1);
                        view = new GameView1(context, points.get(0), width, height, sp, 0, hand);
                        view1 = new GameView1(context, points.get(1), width, height, sp, 1, hand);
                        view2 = new GameView1(context, points.get(2), width, height, sp, 2, hand);
                        addContentView(view, new LayoutParams(-2, -2));
                        addContentView(view1, new LayoutParams(-2, -2));
                        addContentView(view2, new LayoutParams(-2, -2));
                        view.startThread();
                        view1.startThread();
                        view2.startThread();
                    }
                }
            }
        });


    }

//	//线程来提醒UI线程不断重绘
//	public class GameThread extends Thread{
//		public void run(){
//			while (view.flag){
//				try {
//					Thread.sleep(100);
//					view.postInvalidate();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}

    //传感器监听
    private SensorEventListener sensorEventListener = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

        // 传感器信息改变时执行该方法
        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;
            float x = values[0]; // x轴方向的重力加速度，向右为正  
            float y = values[1]; // y轴方向的重力加速度，向前为正  
            float z = values[2]; // z轴方向的重力加速度，向上为正  
            int medumValue = 16;
            if (Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue) {
//                vibrator.vibrate(200);  
                Message msg = new Message();
                msg.what = SENSOR_SHAKE;
                handler.sendMessage(msg);
            }
        }

    };

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            count++;
            if (msg.what == SENSOR_SHAKE) {
                if (count == 1) {//第一次摇
                    sp.play(music, 1, 1, 0, 0, 1);
                    view = new GameView1(context, points.get(0), width, height, sp, 0, hand);
                    view1 = new GameView1(context, points.get(1), width, height, sp, 1, hand);
                    view2 = new GameView1(context, points.get(2), width, height, sp, 2, hand);
                    addContentView(view, new LayoutParams(-2, -2));
                    addContentView(view1, new LayoutParams(-2, -2));
                    addContentView(view2, new LayoutParams(-2, -2));
//				new GameThread().start();
                    view.startThread();
                    view1.startThread();
                    view2.startThread();
//					}
                } else {
                    if (view != null && !view.flag) {//如果上次的线程没有跑了才允许开启此次线程
                        sp.play(music, 1, 1, 0, 0, 1);
                        view = new GameView1(context, points.get(0), width, height, sp, 0, hand);
                        view1 = new GameView1(context, points.get(1), width, height, sp, 1, hand);
                        view2 = new GameView1(context, points.get(2), width, height, sp, 2, hand);
                        addContentView(view, new LayoutParams(-2, -2));
                        addContentView(view1, new LayoutParams(-2, -2));
                        addContentView(view2, new LayoutParams(-2, -2));
//					new GameThread().start();
                        view.startThread();
                        view1.startThread();
                        view2.startThread();
                    }

                }
            }
        }
    };

    // 注册监听器
    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null) {
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
            // 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率  
        }
    }

    // 取消监听器
    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(sensorEventListener);
        }
    }
}
