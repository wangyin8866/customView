package com.example.customimageview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

import com.example.customimageview.R;

/**
 * 剪裁框和ZoomImageView结合
 * 
 * @author DELL
 *
 */
public class ClipImageLayout extends RelativeLayout {
	private ClipImageBorderView mBorderView;
	private ZoomImageView mZoomImageView;

	/**
	 * 这里测试，直接写死了大小，真正使用过程中，可以提取为自定义属性
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
		 * 这里测试，直接写死了图片，真正使用过程中，可以提取为自定义属性
		 */
		mZoomImageView.setImageDrawable(getResources().getDrawable(
				R.drawable.yongshi01));
		this.addView(mZoomImageView, lp);
		this.addView(mBorderView, lp);
		// 计算padding的px
		mHorizontalPadding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources()
						.getDisplayMetrics());
		mBorderView.setHorizontalPadding(mHorizontalPadding);
	}

	/**
	 * 对外公布设置边距的方法,单位为dp
	 * 
	 * @param mHorizontalPadding
	 */
	public void setHorizontalPadding(int mHorizontalPadding) {
		this.mHorizontalPadding = mHorizontalPadding;
	}
}
