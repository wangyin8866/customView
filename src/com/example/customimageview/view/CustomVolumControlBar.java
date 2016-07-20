package com.example.customimageview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.example.customimageview.R;

/**
 * ����������
 * 
 * @author DELL
 *
 */
public class CustomVolumControlBar extends View {
	/**
	 * ��һȦ����ɫ
	 */
	private int mOneColor;
	/**
	 * �ڶ�Ȧ����ɫ
	 */
	private int mTwoColor;
	/**
	 * Ȧ�Ŀ��
	 */
	private int mCricleWidth;
	/**
	 * ����
	 */
	private int mDotCount;
	/**
	 * ÿ������ļ�϶
	 */
	private int mSplitSize;
	/**
	 * �м��ͼƬ
	 */
	private Bitmap mBg;
	/**
	 * ����
	 */
	private Paint mPaint;
	/**
	 * ��ǰ����
	 */
	private int mCurrentCount = 5;
	private Rect mRect;

	public CustomVolumControlBar(Context context) {
		this(context, null);
	}

	public CustomVolumControlBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomVolumControlBar(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.CustomVolumControlBar, defStyleAttr, 0);
		int count = a.getIndexCount();
		for (int i = 0; i < count; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.CustomVolumControlBar_oneColor:
				mOneColor = a.getColor(attr, Color.GRAY);
				break;
			case R.styleable.CustomVolumControlBar_twoColor:
				mTwoColor = a.getColor(attr, Color.GREEN);
				break;
			case R.styleable.CustomVolumControlBar_width:
				mCricleWidth = a.getDimensionPixelSize(attr, (int) TypedValue
						.applyDimension(TypedValue.COMPLEX_UNIT_PX, 20,
								getResources().getDisplayMetrics()));
				break;
			case R.styleable.CustomVolumControlBar_dotCount:
				mDotCount = a.getInt(attr, 20);
				break;
			case R.styleable.CustomVolumControlBar_splitSize:
				mSplitSize = a.getInt(attr, 20);
				break;
			case R.styleable.CustomVolumControlBar_bg:
				mBg = BitmapFactory.decodeResource(getResources(),
						a.getResourceId(attr, 0));
				break;
			}

		}
		a.recycle();
		mPaint = new Paint();
		mRect = new Rect();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mPaint.setAntiAlias(true);// �������
		mPaint.setStrokeWidth(mCricleWidth);// ����Բ���Ŀ��
		mPaint.setStrokeCap(Paint.Cap.ROUND); // �����߶ζϵ���״ΪԲͷ
		mPaint.setStyle(Paint.Style.STROKE);// ���ÿ���
		int centre = getWidth() / 2;// ��ȡԲ�ĵ�X����
		int radius = centre - mCricleWidth / 2;// �뾶
		/**
		 * ����״��Բ
		 */
		drawOval(canvas, centre, radius);
		/**
		 * �������������ε�λ��
		 */
		int relRadius = radius - mCricleWidth / 2;// �����Բ�İ뾶
		/**
		 * ���������εľ��붥�� = mCircleWidth + relRadius - ��2 / 2
		 */
		mRect.left = (int) (relRadius - Math.sqrt(2) * 1.0f / 2 * relRadius)
				+ mCricleWidth;
		/**
		 * ���������εľ������ = mCircleWidth + relRadius - ��2 / 2
		 */
		mRect.top = (int) (relRadius - Math.sqrt(2) * 1.0f / 2 * relRadius)
				+ mCricleWidth;
		mRect.bottom = (int) (mRect.left + Math.sqrt(2) * relRadius);
		mRect.right = (int) (mRect.left + Math.sqrt(2) * relRadius);

		/**
		 * ���ͼƬ�Ƚ�С����ô����ͼƬ�ĳߴ���õ�������
		 */
		if (mBg.getWidth() < Math.sqrt(2) * relRadius) {
			mRect.left = (int) (mRect.left + Math.sqrt(2) * relRadius * 1.0f
					/ 2 - mBg.getWidth() * 1.0f / 2);
			mRect.top = (int) (mRect.top + Math.sqrt(2) * relRadius * 1.0f / 2 - mBg
					.getHeight() * 1.0f / 2);
			mRect.right = (int) (mRect.left + mBg.getWidth());
			mRect.bottom = (int) (mRect.top + mBg.getHeight());

		}
		// ��ͼ
		canvas.drawBitmap(mBg, null, mRect, mPaint);
	}

	private void drawOval(Canvas canvas, int centre, int radius) {
		/**
		 * ������Ҫ���ĸ����Լ���϶����ÿ��������ռ����*360
		 */
		float itemSize = (360 * 1.0f - mDotCount * mSplitSize) / mDotCount;
		RectF oval = new RectF(centre - radius, centre - radius, centre
				+ radius, centre + radius);// ���ڶ����Բ������״�ʹ�С�Ľ���
		mPaint.setColor(mOneColor);// ����Բ������ɫ
		for (int i = 0; i < mDotCount; i++) {
			canvas.drawArc(oval, i * (itemSize + mSplitSize), itemSize, false,
					mPaint);// ���ݽ��Ȼ���
		}
		mPaint.setColor(mTwoColor);// ����Բ������ɫ
		for (int i = 0; i < mCurrentCount; i++) {
			canvas.drawArc(oval, i * (itemSize + mSplitSize), itemSize, false,
					mPaint); // ���ݽ��Ȼ�Բ��
		}

	}

	/**
	 * ��ǰ����+1
	 */
	public void up() {
		mCurrentCount++;
		postInvalidate();
	}

	/**
	 * ��ǰ����-1
	 */
	public void down() {
		mCurrentCount--;
		postInvalidate();
	}

	private int xDown, xUp;

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			xDown = (int) event.getRawX();
			break;

		case MotionEvent.ACTION_UP:
			xUp = (int) event.getRawY();
			if (xUp > xDown)// �»�
			{
				down();
			} else {
				up();
			}
			break;
		}

		return true;
	}
}
