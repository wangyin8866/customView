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
 * Բ������
 * 
 * @author DELL
 *
 */
public class CustomProgressBar extends View {
	/**
	 * ��һȦ����ɫ
	 * 
	 */
	private int firstColor;
	/**
	 * �ڶ�Ȧ����ɫ
	 */
	private int secondColor;
	/**
	 * Ȧ�Ŀ��
	 */
	private int circleWidth;
	/**
	 * �ٶ�
	 */
	private int speed;
	/**
	 * ����
	 */
	private int progress;
	/**
	 * �Ƿ�ʼ��һ��
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
		// ��ͼ�߳�

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

		int centre = getWidth() / 2;// ��ȡԲ�ĵ�X����

		int radius = centre - circleWidth / 2; // �뾶

		mPaint.setStrokeWidth(circleWidth);// ����Բ���Ŀ��

		mPaint.setAntiAlias(true);// �������

		mPaint.setStyle(Paint.Style.STROKE);// ���ÿ���
		// ���ڶ����Բ������״�ʹ�С�Ľ���
		RectF oval = new RectF(centre - radius, centre - radius, centre
				+ radius, centre + radius);
		if (isNext) {
			// ��һ��ɫ��Ȧ�������ڶ���ɫ��Ȧ��ʼ��
			mPaint.setColor(firstColor);// ����Բ������ɫ
			canvas.drawCircle(centre, centre, radius, mPaint);// ����Բ��
			mPaint.setColor(secondColor);// ����Բ������ɫ
			canvas.drawArc(oval, -90, progress, false, mPaint);// ���ݽ��Ȼ���
		} else {
			mPaint.setColor(secondColor);// ����Բ������ɫ
			canvas.drawCircle(centre, centre, radius, mPaint);// ����Բ��
			mPaint.setColor(firstColor);// ����Բ������ɫ
			canvas.drawArc(oval, -90, progress, false, mPaint);// ���ݽ��Ȼ���
		}
	}
}
