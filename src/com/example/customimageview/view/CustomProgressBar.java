package com.example.customimageview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.customimageview.R;

/**
 * 圆环交替
 * 
 * @author DELL
 *
 */
public class CustomProgressBar extends View {
	/**
	 * 第一圈的颜色
	 * 
	 */
	private int firstColor;
	/**
	 * 第二圈的颜色
	 */
	private int secondColor;
	/**
	 * 圈的宽度
	 */
	private int circleWidth;
	/**
	 * 速度
	 */
	private int speed;
	/**
	 * 进度
	 */
	private int progress;
	/**
	 * 是否开始下一个
	 */
	private boolean isNext = false;
	private Paint mPaint;

	public CustomProgressBar(Context context) {
		this(context, null);
	}

	public CustomProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomProgressBar(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.CustomProgressBar, defStyleAttr, 0);
		int count = a.getIndexCount();
		for (int i = 0; i < count; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.CustomProgressBar_firstColor:
				firstColor = a.getColor(attr, Color.BLUE);

				break;
			case R.styleable.CustomProgressBar_secondColor:
				secondColor = a.getColor(attr, Color.GREEN);
				break;
			case R.styleable.CustomProgressBar_circleWidth:
				circleWidth = a.getDimensionPixelSize(attr, (int) TypedValue
						.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20,
								getResources().getDisplayMetrics()));
				break;
			case R.styleable.CustomProgressBar_speed:
				speed = a.getInt(attr, 20);
				break;
			}
		}
		a.recycle();
		mPaint = new Paint();
		// 绘图线程

		new Thread() {
			@Override
			public void run() {

				while (true) {
					progress++;
					if (progress == 360) {
						progress = 0;
						if (!isNext) {
							isNext = true;
						} else {
							isNext = false;
						}
					}
					postInvalidate();
					try {
						Thread.sleep(speed);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		int centre = getWidth() / 2;// 获取圆心的X坐标

		int radius = centre - circleWidth / 2; // 半径

		mPaint.setStrokeWidth(circleWidth);// 设置圆环的宽度

		mPaint.setAntiAlias(true);// 消除锯齿

		mPaint.setStyle(Paint.Style.STROKE);// 设置空心
		// 用于定义的圆弧的形状和大小的界限
		RectF oval = new RectF(centre - radius, centre - radius, centre
				+ radius, centre + radius);
		if (isNext) {
			// 第一颜色的圈完整，第二颜色的圈开始跑
			mPaint.setColor(firstColor);// 设置圆环的颜色
			canvas.drawCircle(centre, centre, radius, mPaint);// 画出圆环
			mPaint.setColor(secondColor);// 设置圆弧的颜色
			canvas.drawArc(oval, -90, progress, false, mPaint);// 根据进度画弧
		} else {
			mPaint.setColor(secondColor);// 设置圆环的颜色
			canvas.drawCircle(centre, centre, radius, mPaint);// 画出圆环
			mPaint.setColor(firstColor);// 设置圆弧的颜色
			canvas.drawArc(oval, -90, progress, false, mPaint);// 根据进度画弧
		}
	}
}
