package com.example.customimageview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * ���ÿ�
 * 
 * @author DELL
 *
 */
public class ClipImageBorderView extends View {
	/**
	 * ˮƽ������VIew�ı߾�
	 */
	private int mHorizontalPadding = 20;
	/**
	 * ��ֱ������view�ı߾�
	 */
	private int mVerticalPadding;
	/**
	 * ���Ƶľ��εĿ��
	 */
	private int mWidth;
	/**
	 * �߿����ɫ��Ĭ������Ϊ��ɫ
	 */
	private int mBorderColor = Color.WHITE;
	/**
	 * �߿�Ŀ�� ����λdp
	 */
	private int mBorderWidth = 1;

	private Paint mPaint;

	public ClipImageBorderView(Context context) {
		this(context, null);
	}

	public ClipImageBorderView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ClipImageBorderView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		// ����padding��px
		mHorizontalPadding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources()
						.getDisplayMetrics());
		mBorderWidth = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, mBorderWidth, getResources()
						.getDisplayMetrics());
		mPaint = new Paint();

		mPaint.setAntiAlias(true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.save();
		// �����������Ŀ�
		mWidth = getWidth() - 2 * mHorizontalPadding;
		// ���������Ļ��ֱ�߽�ı߾�
		mVerticalPadding = (getHeight() - mWidth) / 2;

		mPaint.setColor(Color.parseColor("#aa000000"));
		mPaint.setStyle(Paint.Style.FILL);
		// �������
		canvas.drawRect(0, 0, mHorizontalPadding, getHeight(), mPaint);

		// �����ұ�
		canvas.drawRect(mWidth + mHorizontalPadding, 0, getWidth(),
				getHeight(), mPaint);
		// �����ϱ�
		canvas.drawRect(mHorizontalPadding, 0, getWidth() - mHorizontalPadding,
				mVerticalPadding, mPaint);
		// �����±�
		canvas.drawRect(mHorizontalPadding, mWidth + mVerticalPadding,
				getWidth() - mHorizontalPadding, getHeight(), mPaint);
		// ������߿�
		mPaint.setColor(mBorderColor);
		mPaint.setStrokeWidth(mBorderWidth);
		mPaint.setStyle(Paint.Style.STROKE);
		canvas.drawRect(mHorizontalPadding, mVerticalPadding, getWidth()
				- mHorizontalPadding, getHeight() - mVerticalPadding, mPaint);

		canvas.restore();
	}

	public void setHorizontalPadding(int mHorizontalPadding) {
		this.mHorizontalPadding = mHorizontalPadding;

	}

}
