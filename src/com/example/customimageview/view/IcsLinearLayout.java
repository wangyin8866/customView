package com.example.customimageview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * ���¼���divider���Թ��ܣ�3.0���£�
 * 
 * @author DELL
 *
 */
public class IcsLinearLayout extends LinearLayout {
	private static final int[] LL = new int[] { //
	android.R.attr.divider,//
			android.R.attr.showDividers,//
			android.R.attr.dividerPadding //
	};

	private static final int LL_DIVIDER = 0;
	private static final int LL_SHOW_DIVIDER = 1;
	private static final int LL_DIVIDER_PADDING = 2;

	/**
	 * android:dividers
	 */
	private Drawable mDivider;
	/**
	 * ��Ӧ��android:showDividers
	 */
	private int mShowDividers;
	/**
	 * ��Ӧ��android:dividerPadding
	 */
	private int mDividerPadding;

	private int mDividerWidth;
	private int mDividerHeight;

	public IcsLinearLayout(Context context) {
		this(context, null);
	}

	public IcsLinearLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public IcsLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.obtainStyledAttributes(attrs, LL);
		setDividerDrawable(a.getDrawable(IcsLinearLayout.LL_DIVIDER));
		mDividerPadding = a.getDimensionPixelSize(LL_DIVIDER_PADDING, 0);
		mShowDividers = a.getInteger(LL_SHOW_DIVIDER, SHOW_DIVIDER_NONE);
		a.recycle();
	}

	/**
	 * ���÷ָ�Ԫ�أ���ʼ����ߵ�
	 */
	public void setDividerDrawable(Drawable divider) {
		if (divider == mDivider) {
			return;
		}
		mDivider = divider;
		if (divider != null) {
			mDividerWidth = divider.getIntrinsicWidth();
			mDividerHeight = divider.getIntrinsicHeight();
		} else {
			mDividerWidth = 0;
			mDividerHeight = 0;
		}
		setWillNotDraw(divider == null);
		requestLayout();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// ���ָ�Ԫ�صĿ��ת��Ϊ��Ӧ��margin
		setChildrenDivider();
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * ���ָ�Ԫ�صĿ��ת��Ϊ��Ӧ��margin
	 */
	private void setChildrenDivider() {

		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			// ����ÿ����View
			View child = getChildAt(i);
			// �õ�����
			final int index = indexOfChild(child);
			// ����
			final int orientation = getOrientation();

			final LayoutParams params = (LayoutParams) child.getLayoutParams();
			// �ж��Ƿ���Ҫ����View��߻��Ʒָ�
			if (hasDividerBeforeChildAt(index)) {
				if (orientation == VERTICAL) {
					// �����Ҫ��������topMarginΪ�ָ�Ԫ�صĸ߶ȣ���ֱʱ��
					params.topMargin = mDividerHeight;
				} else {
					// �����Ҫ��������leftMarginΪ�ָ�Ԫ�صĿ�ȣ�ˮƽʱ��
					params.leftMargin = mDividerWidth;
				}
			}
		}

	}

	/**
	 * �ж��Ƿ���Ҫ����View��߻��Ʒָ�
	 */
	private boolean hasDividerBeforeChildAt(int childIndex) {

		if (childIndex == 0 || childIndex == getChildCount()) {
			return false;
		}
		if ((mShowDividers & SHOW_DIVIDER_MIDDLE) != 0) {
			boolean hasVisibleViewBefore = false;
			for (int i = childIndex - 1; i >= 0; i--) {
				// ��ǰindex��ǰһ��Ԫ�ز�ΪGONE����Ϊ��Ҫ
				if (getChildAt(i).getVisibility() != GONE) {
					hasVisibleViewBefore = true;
					break;
				}
			}
			return hasVisibleViewBefore;
		}
		return false;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (mDivider != null) {
			if (getOrientation() == VERTICAL) {
				// ���ƴ�ֱ�����divider
				drawDividersVertical(canvas);
			} else {
				// ����ˮƽ�����divider
				drawDividersHorizontal(canvas);
			}
		}
		super.onDraw(canvas);
	}

	/**
	 * ����ˮƽ�����divider
	 * 
	 * @param canvas
	 */
	private void drawDividersHorizontal(Canvas canvas) {
		final int count = getChildCount();
		// �������е���View
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);

			if (child != null && child.getVisibility() != GONE) {
				// �����Ҫ����divider
				if (hasDividerBeforeChildAt(i)) {
					final android.widget.LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) child
							.getLayoutParams();
					// �õ���ʼ��λ�ã�getLeftΪ��ǰView����࣬�������margin������֮��Ϊdivider���ƵĿ�ʼ����
					final int left = child.getLeft() - lp.leftMargin/*
																	 * -
																	 * mDividerWidth
																	 */;
					// ����divider
					drawVerticalDivider(canvas, left);
				}
			}
		}

	}

	/**
	 * ����divider������left��ˮƽ�������
	 * 
	 * @param canvas
	 * @param left
	 */
	private void drawVerticalDivider(Canvas canvas, int left) {
		// ����divider�ķ�Χ
		mDivider.setBounds(left, getPaddingTop() + mDividerPadding, left
				+ mDividerWidth, getHeight() - getPaddingBottom()
				- mDividerPadding);
		// ����
		mDivider.draw(canvas);

	}

	private void drawDividersVertical(Canvas canvas) {
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);

			if (child != null && child.getVisibility() != GONE) {
				if (hasDividerBeforeChildAt(i)) {
					final android.widget.LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) child
							.getLayoutParams();
					final int top = child.getTop() - lp.topMargin/*
																 * -
																 * mDividerHeight
																 */;
					drawHorizontalDivider(canvas, top);
				}
			}
		}

	}

	private void drawHorizontalDivider(Canvas canvas, int top) {
		mDivider.setBounds(getPaddingLeft() + mDividerPadding, top, getWidth()
				- getPaddingRight() - mDividerPadding, top + mDividerHeight);
		mDivider.draw(canvas);

	}
}
