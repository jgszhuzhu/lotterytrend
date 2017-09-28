package com.example.gametest1.view;

import java.util.Random;

import com.example.gametest1.R;
import com.example.gametest1.util.BitmapUtil;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AnimationSet;
public class GameView1 extends View {
	private int screen_width,screen_height;//屏幕高宽
	private Context context;
	private Bitmap bitmap;
	private Point[] points;
	private Point[] start;//骰子起始位置
	public int count = 0;
	public boolean flag = true;//计时标识
	long time = 1000l;
	private Random random ;
	int[] img = {R.drawable.dice_f1,R.drawable.dice_f2,R.drawable.dice_f3,R.drawable.dice_f4};
	int[] img2 = {R.drawable.dice1,R.drawable.dice2,R.drawable.dice3,R.drawable.dice4,R.drawable.dice5
			,R.drawable.dice6};
	public Integer[] indexs = new Integer[3];
	private int index_view = -1;
	private AnimationSet animationSet;
	private SoundPool sp;//声明一个SoundPool
	private int music;//定义一个整型用load（）；来设置suondID
	float ab_x = 600;
	float ab_y = 800;
	private Handler hand;
	public GameView1(Context context, Point[] points, int screen_width, int screen_height,SoundPool sp,int index_view,Handler hand) {
		super(context);
		this.context = context;
		this.points = points;
		this.start = points;
		this.screen_width = screen_width;
		this.screen_height = screen_width;
		random = new Random();
		this.sp = sp;
		this.index_view = index_view;
	    music = sp.load(context, R.raw.location, 1); 
	    animationSet = new AnimationSet(true);
	    this.hand = hand;
//	    if(index_view == 2){//第三根线程延迟时间补偿
//	    	time = 980l;
//	    }
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(count >= 5){
//			if(index_view == 0){//第一根线程画的骰子范围
//				points = new Point[]{new Point(random.nextInt(screen_width-screen_width/2)+screen_width/7, random.nextInt(screen_height-screen_height/2)+screen_height/4)};
//			}else if(index_view == 1){
//				points = new Point[]{new Point(random.nextInt(screen_width-screen_width/3)+screen_width/3, random.nextInt(screen_height-screen_height/2)+screen_height/4)};
//			}else if(index_view == 2){
//				points = new Point[]{new Point(random.nextInt(screen_width-screen_width/3)+screen_width/3, random.nextInt(screen_height-2*screen_height/5)+screen_height/2)};
//			}
			points = new Point[]{new Point(random.nextInt(screen_width-screen_width/2)+screen_width/7, random.nextInt(screen_height-2*screen_height/5)+screen_height/4)};
		}
		count++;
		if(count > 15){
			flag = false;
		}
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		if(bitmap != null){
			bitmap.recycle();
			bitmap = null;
		}
		for(int i = 0; i < points.length; i ++){
			int index = 0;
			if(count >= 16){
				index = random.nextInt(img2.length);
				if(count == 16){
					indexs[i] = index;//将最后一次显示的骰子记下，后面再次重绘的时候直接画最后显示的几个骰子
				}else{
					index = indexs[i];
				}
				
				bitmap = getBitmap(img2[index]);
				points = start;
			}else{
				index = random.nextInt(img.length);
//				index = count%4;
				bitmap = getBitmap(img[index]);
			}
//			bitmap = BitmapUtil.resizeBitmap(bitmap, screen_width);//根据屏幕大小调整骰子大小
			canvas.drawBitmap(bitmap, points[i].x, points[i].y, paint);
		}
		
//		Log.e("重绘", "重绘第"+count+"次");
		
		
	}

	public void startThread(){
		new GameThread().start();
	}
	
	//线程来提醒UI线程不断重绘
		public class GameThread extends Thread{
			public void run(){
				
				while (flag){
//					Log.e("次数", count+"");
					
					if(count == 15){
						Message msg = new Message();  
			            msg.what = 0x12121;  
			            if(index_view == 0){
			            	hand.sendMessageDelayed(msg, 1000);
			            }
			            
					}
					try {
						Thread.sleep(100);
						postInvalidate();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	
	public Bitmap getBitmap(int id){
		return BitmapFactory.decodeResource(context.getResources(), id);
	}
	
}
