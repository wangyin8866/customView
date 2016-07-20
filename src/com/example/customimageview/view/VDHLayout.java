package com.example.customimageview.view;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.widget.ViewDragHelper;
import android.support.v4.widget.ViewDragHelper.Callback;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * �ؼ��ƶ�ViewDragHelper
 * 
 * @author DELL
 *
 */
public class VDHLayout extends LinearLayout {
	private ViewDragHelper mDragHelper;
	private View mDragView;
	private View mAutoBackView;
	private View mEdgeTrackerView;
	private Point mAutoBackOriginPos = new Point();

	public VDHLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mDragHelper = ViewDragHelper.create(this, 1.0f, new Callback() {

			@Override
			public boolean tryCaptureView(View child, int position) {
				// mEdgeTrackerView��ֱֹ���ƶ�
				return child == mDragView || child == mAutoBackView;
			}

			@Override
			public int clampViewPositionHorizontal(View child, int left, int dx) {
				/**
				 * ����viewֻ��ViewGroup���ڲ��ƶ�(ˮƽ����)
				 */
				final int leftBound = getPaddingLeft();
				final int rightBound = getWidth() - child.getWidth()
						- leftBound;

				final int newLeft = Math.min(Math.max(left, leftBound),
						rightBound);

				return newLeft;

			}

			@Override
			public int clampViewPositionVertical(View child, int top, int dy) {
				/**
				 * ����viewֻ��ViewGroup���ڲ��ƶ�����ֱ����
				 */
				final int topBound = getPaddingTop();
				final int bottomBound = getHeight() - child.getHeight()
						- topBound;
				final int newTop = Math.min(Math.max(top, topBound),
						bottomBound);
				return newTop;
			}

			@Override
			public void onViewReleased(View releasedChild, float xvel,
					float yvel) {
				super.onViewReleased(releasedChild, xvel, yvel);
				// mAutoBackView��ָ�ͷ�ʱ�����Զ���ȥ
				if (releasedChild == mAutoBackView) {
					mDragHelper.settleCapturedViewAt(mAutoBackOriginPos.x,
							mAutoBackOriginPos.y);
					postInvalidate();
				}

			}

			// �ڱ߽��϶�ʱ�ص�
			@Override
			public void onEdgeDragStarted(int edgeFlags, int pointerId) {
				super.onEdgeDragStarted(edgeFlags, pointerId);

				mDragHelper.captureChildView(mEdgeTrackerView, pointerId);
			}
		});
		// ���ñ߽�
		mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		// return mDragHelper.shouldInterceptTouchEvent(ev);// ���������Ƿ�Ӧ�����ص�ǰ���¼�
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mDragHelper.processTouchEvent(event);// �����¼�
		return true;
	}

	@Override
	public void computeScroll() {

		super.computeScroll();

		if (mDragHelper.continueSettling(true)) {
			postInvalidate();

		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		mAutoBackOriginPos.x = mAutoBackView.getLeft();
		mAutoBackOriginPos.y = mAutoBackView.getTop();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mDragView = getChildAt(0);
		mAutoBackView = getChildAt(1);
		mEdgeTrackerView = getChildAt(2);

	}
}
