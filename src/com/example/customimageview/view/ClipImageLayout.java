package com.example.customimageview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

import com.example.customimageview.R;

/**
 * ���ÿ��ZoomImageView���
 * 
 * @author DELL
 *
 */
public class ClipImageLayout extends RelativeLayout {
	private ClipImageBorderView mBorderView;
	private ZoomImageView mZoomImageView;

	/**
	 * ������ԣ�ֱ��д���˴�С������ʹ�ù����У�������ȡΪ�Զ�������
	 */
	private int mHorizontalPadding = 20;

	public ClipImageLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mBorderView = new ClipImageBorderView(context);
		mZoomImageView = new ZoomImageView(context);
		android.view.ViewGroup.LayoutParams lp = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);

		/**
		 * ������ԣ�ֱ��д����ͼƬ������ʹ�ù����У�������ȡΪ�Զ�������
		 */
		mZoomImageView.setImageDrawable(getResources().getDrawable(
				R.drawable.yongshi01));
		this.addView(mZoomImageView, lp);
		this.addView(mBorderView, lp);
		// ����padding��px
		mHorizontalPadding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources()
						.getDisplayMetrics());
		mBorderView.setHorizontalPadding(mHorizontalPadding);
	}

	/**
	 * ���⹫�����ñ߾�ķ���,��λΪdp
	 * 
	 * @param mHorizontalPadding
	 */
	public void setHorizontalPadding(int mHorizontalPadding) {
		this.mHorizontalPadding = mHorizontalPadding;
	}
}
