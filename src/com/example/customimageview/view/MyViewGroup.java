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
		 * ��ô�ViewGroup�ϼ�����Ϊ���Ƽ��Ŀ�͸ߣ��Լ�����ģʽ
		 */
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		// ��������е�childView�Ŀ�͸�
		measureChildren(widthMeasureSpec, heightMeasureSpec);

		/**
		 * ��¼�����warp_contentʱ���õĿ�͸�
		 */
		int width = 0;
		int height = 0;
		int childCount = getChildCount();

		int cWidth = 0;
		int cHeight = 0;
		MarginLayoutParams cParams = null;
		// �û������������child�ĸ߶�

		int lHeight = 0;
		// ���ڼ����ұ�����childView�ĸ߶ȣ����ո߶�ȡ����֮���ֵ
		int rHeight = 0;
		// ���ڼ����ϱ�����child�Ŀ��
		int tWidth = 0;
		// ���ڼ����±�����child���,���տ��ȡ����֮���ֵ
		int bWidth = 0;
		/**
		 * ����childView����ĳ��Ŀ�͸ߣ��Լ����õ�margin���������Ŀ�͸ߣ���Ҫ����������warp_contentʱ
		 */
		for (int i = 0; i < childCount; i++) {
			View childView = getChildAt(i);
			cWidth = childView.getMeasuredWidth();
			cHeight = childView.getMeasuredHeight();
			cParams = (MarginLayoutParams) childView.getLayoutParams();
			// ��������child
			if (i == 0 || i == 1) {
				tWidth += cWidth + cParams.leftMargin + cParams.rightMargin;

			}
			// ��������child
			if (i == 2 || i == 3) {
				bWidth += cWidth + cParams.leftMargin + cParams.rightMargin;

			}
			// �������child
			if (i == 0 || i == 2) {
				lHeight += cHeight + cParams.topMargin + cParams.bottomMargin;

			}
			// �ұ�����child
			if (i == 1 || i == 3) {
				rHeight += cHeight + cParams.topMargin + cParams.bottomMargin;
			}

		}
		width = Math.max(tWidth, bWidth);
		height = Math.max(lHeight, rHeight);
		/**
		 * �����wrap_content����Ϊ���Ǽ����ֵ ����ֱ������Ϊ�����������ֵ
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
