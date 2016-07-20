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
 * 音量控制器
 * 
 * @author DELL
 *
 */
public class CustomVolumControlBar extends View {
	/**
	 * 第一圈的颜色
	 */
	private int mOneColor;
	/**
	 * 第二圈的颜色
	 */
	private int mTwoColor;
	/**
	 * 圈的宽度
	 */
	private int mCricleWidth;
	/**
	 * 个数
	 */
	private int mDotCount;
	/**
	 * 每个块块间的间隙
	 */
	private int mSplitSize;
	/**
	 * 中间的图片
	 */
	private Bitmap mBg;
	/**
	 * 画笔
	 */
	private Paint mPaint;
	/**
	 * 当前进度
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
		mPaint.setAntiAlias(true);// 消除锯齿
		mPaint.setStrokeWidth(mCricleWidth);// 设置圆环的宽度
		mPaint.setStrokeCap(Paint.Cap.ROUND); // 定义线段断电形状为圆头
		mPaint.setStyle(Paint.Style.STROKE);// 设置空心
		int centre = getWidth() / 2;// 获取圆心的X坐标
		int radius = centre - mCricleWidth / 2;// 半径
		/**
		 * 画块状椭圆
		 */
		drawOval(canvas, centre, radius);
		/**
		 * 计算内切正方形的位置
		 */
		int relRadius = radius - mCricleWidth / 2;// 获得内圆的半径
		/**
		 * 内切正方形的距离顶部 = mCircleWidth + relRadius - √2 / 2
		 */
		mRect.left = (int) (relRadius - Math.sqrt(2) * 1.0f / 2 * relRadius)
				+ mCricleWidth;
		/**
		 * 内切正方形的距离左边 = mCircleWidth + relRadius - √2 / 2
		 */
		mRect.top = (int) (relRadius - Math.sqrt(2) * 1.0f / 2 * relRadius)
				+ mCricleWidth;
		mRect.bottom = (int) (mRect.left + Math.sqrt(2) * relRadius);
		mRect.right = (int) (mRect.left + Math.sqrt(2) * relRadius);

		/**
		 * 如果图片比较小，那么根据图片的尺寸放置到正中心
		 */
		if (mBg.getWidth() < Math.sqrt(2) * relRadius) {
			mRect.left = (int) (mRect.left + Math.sqrt(2) * relRadius * 1.0f
					/ 2 - mBg.getWidth() * 1.0f / 2);
			mRect.top = (int) (mRect.top + Math.sqrt(2) * relRadius * 1.0f / 2 - mBg
					.getHeight() * 1.0f / 2);
			mRect.right = (int) (mRect.left + mBg.getWidth());
			mRect.bottom = (int) (mRect.top + mBg.getHeight());

		}
		// 绘图
		canvas.drawBitmap(mBg, null, mRect, mPaint);
	}

	private void drawOval(Canvas canvas, int centre, int radius) {
		/**
		 * 根据需要画的个数以及间隙计算每个块块的所占比例*360
		 */
		float itemSize = (360 * 1.0f - mDotCount * mSplitSize) / mDotCount;
		RectF oval = new RectF(centre - radius, centre - radius, centre
				+ radius, centre + radius);// 用于定义的圆弧的形状和大小的界限
		mPaint.setColor(mOneColor);// 设置圆环的颜色
		for (int i = 0; i < mDotCount; i++) {
			canvas.drawArc(oval, i * (itemSize + mSplitSize), itemSize, false,
					mPaint);// 根据进度画弧
		}
		mPaint.setColor(mTwoColor);// 设置圆环的颜色
		for (int i = 0; i < mCurrentCount; i++) {
			canvas.drawArc(oval, i * (itemSize + mSplitSize), itemSize, false,
					mPaint); // 根据进度画圆弧
		}

	}

	/**
	 * 当前数量+1
	 */
	public void up() {
		mCurrentCount++;
		postInvalidate();
	}

	/**
	 * 当前数量-1
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
			if (xUp > xDown)// 下滑
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
