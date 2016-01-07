package com.zhl.cbprogressbar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/**
 * 一个继承SurfaceView的绘图基类。提高绘图效率。如果不是做游戏等需要高效率绘图刷新的应用时，可以在view加载完成的时候调用stopRender(
 * )停止绘图渲染
 *
 */
public abstract class CBbaseView extends SurfaceView implements Callback,
		Runnable {
	/**绘图渲染频率 正常**/
	public static final int RENDER_RATE_NORMAL = 0;
	/** 绘图渲染频率 较慢 **/
	public static final int RENDER_RATE_SLOWLY = 1;
	/** 绘图渲染频率 较慢 **/
	public static final int RENDER_RATE_FAST = 2;
	
	private long sleepMillions = 50;
	/** 控制、访问Surface **/
	SurfaceHolder holder;
	/** 用来绘图的子线程 **/
	Thread thread;
	/** 是否要绘图 **/
	boolean isRender;
	/** 画笔 **/
	Paint paint;

	public CBbaseView(Context context) {
		this(context, null);
	}

	public CBbaseView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CBbaseView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// 这两句设置防止 sufraceview出现黑色背景
		setZOrderOnTop(true);
		// 1.获取Holder
		holder = getHolder();
		holder.setFormat(PixelFormat.TRANSLUCENT);
		// 初始化画笔
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);

		// 2.监听Surface的生命周期
		holder.addCallback(this);

	}

	/**
	 * Callback-Surface被创建
	 */
	public void surfaceCreated(SurfaceHolder holder) {
		// 3.开启子线程
		thread = new Thread(this);
		isRender = true;
		thread.start();

	}

	/**
	 * Callback-Surface的尺寸被改变
	 */
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	/**
	 * Callback-Surface被销毁
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {
		System.out.println("surfaceDestroyed");
		isRender = false;
		if (thread != null && thread.isAlive()) {
			try {
				thread.join();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 绘图
	 */
	public void run() {
		Canvas canvas = null;

		while (isRender) {
			try {
				// 4.拿到Canvas进行绘图,并且缓存到Surface
				canvas = holder.lockCanvas();
				if(canvas==null){
					break;
				}
				// 清理屏幕
				canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);

				render(canvas, paint);
			} finally {
				// 5.解锁画布，回到主线程进行渲染
				if (canvas != null) {
					holder.unlockCanvasAndPost(canvas);
				}
			}
			// 休息50ms
			try {
				Thread.sleep(sleepMillions);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 真正的绘图逻辑
	 */
	protected abstract void render(Canvas canvas, Paint piant);

	/**
	 * 停止绘图（由于本类一直循环绘图，在view不需要一直加载的情况下 可以调用此方法停止绘图，停止绘图后 重新绘图需要调用reStart()）
	 */
	public void stopRender() {
		// 停止渲染时 先睡一下 防止 之前的视图没有及时更新而停止渲染
		try {
			Thread.sleep(sleepMillions);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.isRender = false;
	}

	/**
	 * 重新开始绘图（重新开启一个绘图线程）
	 */
	public void reStartRender() {
		thread = new Thread(this);
		isRender = true;
		thread.start();
	}

	/**
	 * 是否正在进行绘图渲染
	 * 
	 * @return true/false true:可以绘图 false: 需要调用restart()才能绘图
	 */
	public boolean isRending() {
		return this.isRender;
	}

	/**
	 * 设置刷新频率
	 * 
	 * @params CBbaseView.RENDER_RATE_NORMAL,</br>
	 *         CBbaseView.RENDER_RATE_SLOWLY,</br> CBbaseView.RENDER_RATE_FAST
	 */
	public void setRenderRate(int rate) {
		if (rate < 0) {
			return;
		}
		switch (rate) {
		case RENDER_RATE_NORMAL:
			sleepMillions = 50;
			break;
		case RENDER_RATE_SLOWLY:
			sleepMillions = 100;
			break;
		case RENDER_RATE_FAST:
			sleepMillions = 30;
			break;

		default:
			
			break;
		}
	}

}
