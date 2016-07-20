package com.example.customimageview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.example.customimageview.R;

public class CustomImageView extends View {
	private String mTitleText;
	private int mTitleTextSize;
	private int mTitleTextColor;
	private Bitmap mImage;
	private int mImageScale;
	private Rect mRect;
	private Paint mPaint;
	private Rect mTextBound;
	private int mWidth;
	private int mHeight;
	private int IMAGE_SCALE_FITXY = 0;
	private int IMAGE_SCALE_CENTER = 1;

	public CustomImageView(Context context) {
		this(context, null);
	}

	public CustomImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.CustomImageView, defStyleAttr, 0);
		int count = a.getIndexCount();
		for (int i = 0; i < count; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.CustomImageView_titleText:
				mTitleText = a.getString(attr);
				break;

			case R.styleable.CustomImageView_titleTextSize:
				mTitleTextSize = a.getDimensionPixelSize(attr, (int) TypedValue
						.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12,
								getResources().getDisplayMetrics()));
				break;
			case R.styleable.CustomImageView_titleTextColor:
				mTitleTextColor = a.getColor(attr, Color.BLUE);
				break;
			case R.styleable.CustomImageView_image:
				mImage = BitmapFactory.decodeResource(getResources(),
						a.getResourceId(attr, 0));
				break;
			case R.styleable.CustomImageView_imageScaleType:
				mImageScale = a.getInt(attr, 0);
				break;
			}

		}
		a.recycle();
		mRect = new Rect();
		mPaint = new Paint();
		mPaint.setTextSize(mTitleTextSize);
		// 计算绘画字体需要的范围
		mTextBound = new Rect();
		mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mTextBound);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		/**
		 * 设置宽度
		 */
		int specMode = MeasureSpec.getMode(widthMeasureSpec);
		int specSize = MeasureSpec.getSize(widthMeasureSpec);
		if (specMode == MeasureSpec.EXACTLY) {
			mWidth = specSize;
		} else {
			// 由图片决定的宽
			int desireByImg = getPaddingLeft() + getPaddingRight()
					+ mImage.getWidth();
			// 由文字决定的宽
			int desireByTitle = getPaddingLeft() + getPaddingRight()
					+ mTextBound.width();

			if (specMode == MeasureSpec.AT_MOST) {// wrap_content
				int desire = Math.max(desireByImg, desireByTitle);
				mWidth = Math.min(specSize, desire);
			}
		}
		/**
		 * 设置高度
		 */
		specMode = MeasureSpec.getMode(heightMeasureSpec);
		specSize = MeasureSpec.getSize(heightMeasureSpec);
		if (specMode == MeasureSpec.EXACTLY) {
			mHeight = specSize;
		} else {
			// 由图片决定的高
			int desireByImg = getPaddingTop() + getPaddingBottom()
					+ mImage.getHeight();
			// 由文字决定的高
			int desireByTitle = getPaddingTop() + getPaddingBottom()
					+ mTextBound.height();
			if (specMode == MeasureSpec.AT_MOST) {// wrap_content
				int desire = Math.max(desireByImg, desireByTitle);
				mHeight = Math.min(desire, specSize);

			}
		}
		setMeasuredDimension(mWidth, mHeight);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		/**
		 * 边框
		 */
		mPaint.setStrokeWidth(4);
		mPaint.setStyle(Style.STROKE);
		mPaint.setColor(Color.BLUE);
		canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

		mRect.left = getPaddingLeft();
		mRect.right = mWidth - getPaddingRight();
		mRect.top = getPaddingTop();
		mRect.bottom = mHeight - getPaddingBottom();

		mPaint.setColor(mTitleTextColor);
		mPaint.setStyle(Style.FILL);
		// 当前设置的宽度小于字体需要的宽度，将字体改为xxx...
		if (mTextBound.width() > mWidth) {
			TextPaint textPaint = new TextPaint(mPaint);
			String msg = TextUtils.ellipsize(mTitleText, textPaint,
					(float) mWidth - getPaddingLeft() - getPaddingRight(),
					TextUtils.TruncateAt.END).toString();
			canvas.drawText(msg, getPaddingLeft(),
					mHeight - getPaddingBottom(), mPaint);
		} else {
			// 正常情况下将字体剧中
			canvas.drawText(mTitleText, mWidth / 2 - mTextBound.width() * 1.0f
					/ 2, mHeight - getPaddingBottom(), mPaint);
		}

		// 取消使用掉的块
		Log.e("tag11111111111", mRect.left + ";" + mRect.top + ";"
				+ mRect.right + ";" + mRect.bottom);
		mRect.bottom -= mTextBound.height();
		Log.e("tag22222222222", mRect.left + ";" + mRect.top + ";"
				+ mRect.right + ";" + mRect.bottom);

		if (mImageScale == IMAGE_SCALE_FITXY) {
			canvas.drawBitmap(mImage, null, mRect, mPaint);
		} else {
			// 计算居中的矩形范围
			mRect.left = mWidth / 2 - mImage.getWidth() / 2;
			mRect.top = (mHeight - mTextBound.height()) / 2
					- mImage.getHeight() / 2;
			mRect.right = mWidth / 2 + mImage.getWidth() / 2;

			mRect.bottom = (mHeight - mTextBound.height()) / 2
					+ mImage.getHeight() / 2;
			
			canvas.drawBitmap(mImage, null, mRect, mPaint);
		}

	}
}
