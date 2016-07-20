package com.example.customimageview.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;

/**
 * ͼƬ�Ŵ���С���϶�
 * 
 * @author DELL
 *
 */
public class ZoomImageView extends ImageView implements OnScaleGestureListener,
		OnTouchListener, OnGlobalLayoutListener {

	private static final String TAG = ZoomImageView.class.getSimpleName();
	public static final float SCALE_MAX = 4.0f;
	private static final float SCALE_MID = 2.0f;
	/**
	 * ��ʼ��ʱ�����ű��������ͼƬ���ߴ�����Ļ����ֵ��С��0
	 */
	private float initScale = 1.0f;
	/**
	 * ���ڴ�ž����9��ֵ
	 */
	private static final float[] matrixValues = new float[9];
	private boolean once = true;
	/**
	 * ���ŵ�����
	 */
	private ScaleGestureDetector mScaleGestureDetector = null;
	private final Matrix mScaleMatrix = new Matrix();
	/**
	 * ����˫�����
	 */
	private GestureDetector mGestureDetector;
	private boolean isAutoScale;
	private int mTouchSlop;

	private int lastPointerCount;
	private float mLastX;
	private float mLastY;
	private boolean isCanDrag;

	private boolean isCheckTopAndBottom = true;
	private boolean isCheckLeftAndRight = true;

	public ZoomImageView(Context context) {
		this(context, null);
	}

	public ZoomImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ZoomImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		super.setScaleType(ScaleType.MATRIX);
		mScaleGestureDetector = new ScaleGestureDetector(context, this);
		mGestureDetector = new GestureDetector(context,
				new SimpleOnGestureListener() {
					@Override
					public boolean onDoubleTap(MotionEvent e) {
						if (isAutoScale == true)
							return true;

						float x = e.getX();
						float y = e.getY();
						Log.e("DoubleTap", getScale() + " , " + initScale);
						if (getScale() < SCALE_MID) {
							ZoomImageView.this.postDelayed(

							new AutoScaleRunnable(SCALE_MID, x, y), 16);
							isAutoScale = true;
						}//
							// else if (getScale() >= SCALE_MID
						// && getScale() < SCALE_MAX) {
						// ZoomImageView.this.postDelayed(
						// new AutoScaleRunnable(SCALE_MAX, x, y), 16);
						// isAutoScale = true;
						// } //
						else {
							ZoomImageView.this.postDelayed(
									new AutoScaleRunnable(initScale, x, y), 16);
							isAutoScale = true;
						}

						return true;
					}
				});
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		super.setScaleType(ScaleType.MATRIX);
		this.setOnTouchListener(this);

	}

	@Override
	public boolean onScale(ScaleGestureDetector detector) {
		float scale = getScale();
		float scaleFactor = detector.getScaleFactor();
		if (getDrawable() == null) {
			return true;
		}
		/**
		 * ���ŵķ�Χ����
		 */
		if ((scale < SCALE_MAX && scaleFactor > 1.0f)
				|| (scale > initScale && scaleFactor < 1.0f)) {
			/**
			 * ���ֵ��Сֵ�ж�
			 */
			if (scaleFactor * scale < initScale) {
				scaleFactor = initScale / scale;
			}
			if (scaleFactor * scale > SCALE_MAX) {
				scaleFactor = SCALE_MAX / scale;
			}
			/**
			 * �������ű���
			 */
			// mScaleMatrix.postScale(scaleFactor, scaleFactor, getWidth() / 2,
			// getHeight() / 2);
			// �ı���������
			mScaleMatrix.postScale(scaleFactor, scaleFactor,
					detector.getFocusX(), detector.getFocusY());

			checkBorderAndCenterWhenScale();
			setImageMatrix(mScaleMatrix);

		}
		return true;
	}

	/**
	 * ������ʱ������ͼƬ��ʾ��Χ�Ŀ���
	 */
	private void checkBorderAndCenterWhenScale() {
		RectF rect = getMatrixRectF();
		float deltaX = 0;
		float deltaY = 0;

		int width = getWidth();
		int height = getHeight();
		// ������ߴ�����Ļ������Ʒ�Χ
		if (rect.width() >= width) {
			if (rect.left > 0) {
				deltaX = -rect.left;
			}
			if (rect.right < width) {
				deltaX = width - rect.right;
			}
		}
		if (rect.height() >= height) {
			if (rect.top > 0) {
				deltaY = -rect.top;
			}
			if (rect.bottom < height) {
				deltaY = height - rect.bottom;
			}
		}
		// �������С����Ļ�����������
		if (rect.width() < width) {
			deltaX = width * 0.5f - rect.right + 0.5f * rect.width();
		}
		if (rect.height() < height) {
			deltaY = height * 0.5f - rect.bottom + 0.5f * rect.height();
		}
		Log.e(TAG, "deltaX = " + deltaX + " , deltaY = " + deltaY);

		mScaleMatrix.postTranslate(deltaX, deltaY);
	}

	/**
	 * ���ݵ�ǰͼƬ��Matrix���ͼƬ�ķ�Χ
	 * 
	 * @return
	 */
	private RectF getMatrixRectF() {
		Matrix matrix = mScaleMatrix;
		RectF rect = new RectF();
		Drawable d = getDrawable();
		if (null != d) {
			rect.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
			matrix.mapRect(rect);
		}
		return rect;
	}

	private float getScale() {
		mScaleMatrix.getValues(matrixValues);
		return matrixValues[Matrix.MSCALE_X];
	}

	@Override
	public boolean onScaleBegin(ScaleGestureDetector detector) {

		return true;
	}

	@Override
	public void onScaleEnd(ScaleGestureDetector detector) {

	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		// ���OnGlobalLayoutListener
		getViewTreeObserver().addOnGlobalLayoutListener(this);
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		// ע����OnGlobalLayoutListener
		getViewTreeObserver().removeGlobalOnLayoutListener(this);
	}

	/**
	 * ��ȡ��ͼ�ĸ߶ȺͿ��
	 */
	@Override
	public void onGlobalLayout() {

		if (once) {
			Drawable d = getDrawable();
			if (d == null) {
				return;
			}
			int width = getWidth();
			int height = getHeight();
			// �õ�ͼƬ�Ŀ�͸�
			int dw = d.getIntrinsicWidth();
			int dh = d.getIntrinsicHeight();
			float scale = 1.0f;
			// ���ͼƬ�Ŀ���߸ߴ�����Ļ������������Ļ�Ŀ���߸�
			if (dw > width && dh <= height) {
				scale = width * 1.0f / dw;
			}
			if (dh > height && dw <= width) {
				scale = height * 1.0f / dh;
			}
			// �����͸߶�������Ļ�������䰴��������Ӧ��Ļ��С
			if (dw > width && dh > height) {
				scale = Math.min(dw * 1.0f / width, dh * 1.0f / height);
			}
			initScale = scale;
			// ͼƬ�ƶ�����Ļ����
			mScaleMatrix.postTranslate((width - dw) / 2, (height - dh) / 2);
			mScaleMatrix.postScale(scale, scale, width / 2, height / 2);
			setImageMatrix(mScaleMatrix);
			once = false;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (mGestureDetector.onTouchEvent(event))
			return true;
		mScaleGestureDetector.onTouchEvent(event);
		float x = 0;
		float y = 0;
		// �õ�������ĸ���

		final int pointerCount = event.getPointerCount();
		// �õ�����������x��y��ֵ
		for (int i = 0; i < pointerCount; i++) {
			x += event.getX(i);
			y += event.getY(i);
		}
		x = x / pointerCount;
		y = y / pointerCount;
		/**
		 * ÿ�������㷢���仯ʱ������mLasX , mLastY
		 */

		if (pointerCount != lastPointerCount) {
			isCanDrag = false;
			mLastX = x;
			mLastY = y;
		}
		lastPointerCount = pointerCount;
		RectF rectF = getMatrixRectF();
		// if (rectF.width() > getWidth() || rectF.height() > getHeight()) {
		// getParent().requestDisallowInterceptTouchEvent(true);
		// }// ����VIewpager������ͻ
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (rectF.width() > getWidth() || rectF.height() > getHeight()) {
				getParent().requestDisallowInterceptTouchEvent(true);
			}// ����VIewpager������ͻ
			break;

		case MotionEvent.ACTION_MOVE:
			if (rectF.width() > getWidth() || rectF.height() > getHeight()) {
				getParent().requestDisallowInterceptTouchEvent(true);
			}// ����VIewpager������ͻ
			float dx = x - mLastX;
			float dy = y - mLastY;
			if (!isCanDrag) {
				isCanDrag = isCanDrag(dx, dy);

			}
			if (isCanDrag) {
				if (getDrawable() != null) {
					/**
					 * ��ͼƬ����߽�ʱ���������϶���ʱ���ܹ����¼���ViewPager
					 */
					// if (getMatrixRectF().left == 0 && dx > 0) {
					// getParent().requestDisallowInterceptTouchEvent(false);
					// }// ����VIewpager������ͻ
					//
					// if (getMatrixRectF().right == getWidth() && dx < 0) {
					// getParent().requestDisallowInterceptTouchEvent(false);
					// }// ����VIewpager������ͻ
					isCheckLeftAndRight = isCheckTopAndBottom = true;
					// ������С����Ļ��ȣ����ֹ�����ƶ�
					if (rectF.width() < getWidth()) {
						dx = 0;
						isCheckLeftAndRight = false;
					}
					// ����߶�С����Ļ�߶ȣ����ֹ�����ƶ�
					if (rectF.height() < getHeight()) {
						dy = 0;
						isCheckTopAndBottom = false;
					}
					mScaleMatrix.postTranslate(dx, dy);
					checkMatrixBounds();
					setImageMatrix(mScaleMatrix);
				}
			}
			mLastX = x;
			mLastY = y;
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			Log.e(TAG, "ACTION_UP");
			lastPointerCount = 0;
			break;
		}
		return true;
	}

	/**
	 * �ƶ�ʱ�����б߽��жϣ���Ҫ�жϿ��ߴ�����Ļ��
	 */
	private void checkMatrixBounds() {
		RectF rect = getMatrixRectF();

		float deltaX = 0, deltaY = 0;
		final float viewWidth = getWidth();
		final float viewHeight = getHeight();
		// �ж��ƶ������ź�ͼƬ��ʾ�Ƿ񳬳���Ļ�߽�
		if (rect.top > 0 && isCheckTopAndBottom) {
			deltaY = -rect.top;
		}
		if (rect.bottom < viewHeight && isCheckTopAndBottom) {
			deltaY = viewHeight - rect.bottom;
		}
		if (rect.left > 0 && isCheckLeftAndRight) {
			deltaX = -rect.left;
		}
		if (rect.right < viewWidth && isCheckLeftAndRight) {
			deltaX = viewWidth - rect.right;
		}
		mScaleMatrix.postTranslate(deltaX, deltaY);

	}

	/**
	 * �Ƿ����ƶ���Ϊ
	 * 
	 * @param dx
	 * @param dy
	 * @return
	 */
	private boolean isCanDrag(float dx, float dy) {
		return Math.sqrt((dx * dx) + (dy * dy)) >= mTouchSlop;
	}

	/**
	 * �Զ����ŵ�����
	 * 
	 * @author zhy
	 * 
	 */
	private class AutoScaleRunnable implements Runnable {
		static final float BIGGER = 1.07f;
		static final float SMALLER = 0.93f;
		private float mTargetScale;
		private float tmpScale;

		/**
		 * ���ŵ�����
		 */
		private float x;
		private float y;

		/**
		 * ����Ŀ������ֵ������Ŀ��ֵ�뵱ǰֵ���ж�Ӧ�÷Ŵ�����С
		 * 
		 * @param targetScale
		 */
		public AutoScaleRunnable(float targetScale, float x, float y) {
			this.mTargetScale = targetScale;
			this.x = x;
			this.y = y;
			if (getScale() < mTargetScale) {
				tmpScale = BIGGER;
			} else {
				tmpScale = SMALLER;
			}

		}

		@Override
		public void run() {
			// ��������
			mScaleMatrix.postScale(tmpScale, tmpScale, x, y);
			checkBorderAndCenterWhenScale();
			setImageMatrix(mScaleMatrix);

			final float currentScale = getScale();
			// ���ֵ�ںϷ���Χ�ڣ���������
			if (((tmpScale > 1f) && (currentScale < mTargetScale))
					|| ((tmpScale < 1f) && (mTargetScale < currentScale))) {
				ZoomImageView.this.postDelayed(this, 16);
			} else
			// ����ΪĿ������ű���
			{
				final float deltaScale = mTargetScale / currentScale;
				mScaleMatrix.postScale(deltaScale, deltaScale, x, y);
				checkBorderAndCenterWhenScale();
				setImageMatrix(mScaleMatrix);
				isAutoScale = false;
			}

		}
	}
}
