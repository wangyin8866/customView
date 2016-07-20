package com.example.customimageview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class MyViewGroup extends ViewGroup {

	public MyViewGroup(Context context) {
		super(context);
	}

	public MyViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyViewGroup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		/**
		 * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
		 */
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		// 计算出所有的childView的宽和高
		measureChildren(widthMeasureSpec, heightMeasureSpec);

		/**
		 * 记录如果是warp_content时设置的宽和高
		 */
		int width = 0;
		int height = 0;
		int childCount = getChildCount();

		int cWidth = 0;
		int cHeight = 0;
		MarginLayoutParams cParams = null;
		// 用户计算左边两个child的高度

		int lHeight = 0;
		// 用于计算右边两个childView的高度，最终高度取二者之间大值
		int rHeight = 0;
		// 用于计算上边两个child的宽度
		int tWidth = 0;
		// 用于计算下边两个child宽度,最终宽度取二者之间大值
		int bWidth = 0;
		/**
		 * 根据childView计算的出的宽和高，以及设置的margin计算容器的宽和高，主要用于容器是warp_content时
		 */
		for (int i = 0; i < childCount; i++) {
			View childView = getChildAt(i);
			cWidth = childView.getMeasuredWidth();
			cHeight = childView.getMeasuredHeight();
			cParams = (MarginLayoutParams) childView.getLayoutParams();
			// 上面两个child
			if (i == 0 || i == 1) {
				tWidth += cWidth + cParams.leftMargin + cParams.rightMargin;

			}
			// 下面两个child
			if (i == 2 || i == 3) {
				bWidth += cWidth + cParams.leftMargin + cParams.rightMargin;

			}
			// 左边两个child
			if (i == 0 || i == 2) {
				lHeight += cHeight + cParams.topMargin + cParams.bottomMargin;

			}
			// 右边两个child
			if (i == 1 || i == 3) {
				rHeight += cHeight + cParams.topMargin + cParams.bottomMargin;
			}

		}
		width = Math.max(tWidth, bWidth);
		height = Math.max(lHeight, rHeight);
		/**
		 * 如果是wrap_content设置为我们计算的值 否则：直接设置为父容器计算的值
		 */
		setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? widthSize
				: width, (heightMode == MeasureSpec.EXACTLY) ? heightSize
				: height);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int count = getChildCount();
		int cWidth = 0;
		int cHeight = 0;
		MarginLayoutParams cParams = null;
		for (int i = 0; i < count; i++) {
			View childView = getChildAt(i);
			cWidth = childView.getMeasuredWidth();
			cHeight = childView.getMeasuredHeight();
			cParams = (MarginLayoutParams) childView.getLayoutParams();
			int cl = 0, ct = 0, cr = 0, cb = 0;

			switch (i) {
			case 0:
				cl = cParams.leftMargin;
				ct = cParams.topMargin;

				break;
			case 1:
				cl = getWidth() - cWidth - cParams.rightMargin
						- cParams.leftMargin;
				ct = cParams.topMargin;

				break;
			case 2:
				cl = cParams.leftMargin;
				ct = getHeight() - cParams.bottomMargin - cHeight
						- cParams.topMargin;

				break;
			case 3:
				cl = getWidth() - cWidth - cParams.rightMargin
						- cParams.leftMargin;
				ct = getHeight() - cParams.bottomMargin - cHeight
						- cParams.topMargin;

				break;
			}
			cr = cl + cWidth;
			cb = ct + cHeight;
			childView.layout(cl, ct, cr, cb);
		}
	}

	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new MarginLayoutParams(getContext(), attrs);
	}
}
