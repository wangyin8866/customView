package com.example.customimageview.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import com.example.customimageview.R;

/**
 * Բ��ͼƬ
 * 
 * @author DELL
 *
 */
public class RoundImageView extends ImageView {

	private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
	private static final int COLORDRAWABLE_DIMENSION = 2;
	/**
	 * ͼƬ�����ͣ�Բ��orԲ��
	 */
	private int type;
	private static final int TYPE_CIRCLE = 0;
	private static final int TYPE_ROUND = 1;
	/**
	 * Բ�Ǵ�С��Ĭ��ֵ
	 */
	private static final int BODER_RADIUS_DEFAULT = 10;
	/**
	 * Բ�ǵĴ�С
	 */
	private int mBorderRadius;
	/**
	 * ��ͼ��Paint
	 */
	private Paint mBitmapPaint;
	/**
	 * Բ�ǵİ뾶
	 */
	private int mRadius;
	/**
	 * 3x3 ������Ҫ������С�Ŵ�
	 */
	private Matrix mMatrix;
	/**
	 * ��Ⱦͼ��ʹ��ͼ��Ϊ����ͼ����ɫ
	 */
	private BitmapShader mBitmapShader;
	/**
	 * view�Ŀ��
	 */
	private int mWidth;
	private RectF mRoundRect;

	public RoundImageView(Context context) {
		this(context, null);
	}

	public RoundImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mMatrix = new Matrix();
		mBitmapPaint = new Paint();
		mBitmapPaint.setAntiAlias(true);

		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.RoundImageView, defStyle, 0);

		mBorderRadius = a.getDimensionPixelSize(
				R.styleable.RoundImageView_borderRadius, (int) TypedValue
						.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
								BODER_RADIUS_DEFAULT, getResources()
										.getDisplayMetrics()));// Ĭ��Ϊ10dp
		type = a.getInt(R.styleable.RoundImageView_type, TYPE_CIRCLE);// Ĭ��ΪCircle
		a.recycle();
	}

	@SuppressLint("NewApi")
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		/**
		 * ���������Բ�Σ���ǿ�Ƹı�view�Ŀ��һ�£���СֵΪ׼
		 */
		if (type == TYPE_CIRCLE) {
			mWidth = Math.min(getMeasuredWidth(), getMaxHeight());
			mRadius = mWidth / 2;
			setMeasuredDimension(mWidth, mWidth);

		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (getDrawable() == null) {
			return;
		}
		setUpShader();

		if (type == TYPE_ROUND) {
			canvas.drawRoundRect(mRoundRect, mBorderRadius, mBorderRadius,
					mBitmapPaint);
		} else {
			canvas.drawCircle(mRadius, mRadius, mRadius, mBitmapPaint);
			// drawSomeThing(canvas);
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// Բ��ͼƬ�ķ�Χ
		if (type == TYPE_ROUND)
			mRoundRect = new RectF(0, 0, getWidth(), getHeight());
	}

	/**
	 * ״̬�Ĵ洢��ָ�
	 */
	private static final String STATE_INSTANCE = "state_instance";
	private static final String STATE_TYPE = "state_type";
	private static final String STATE_BORDER_RADIUS = "state_border_radius";

	@Override
	protected Parcelable onSaveInstanceState() {
		Bundle bundle = new Bundle();
		bundle.putParcelable(STATE_INSTANCE, super.onSaveInstanceState());
		bundle.putInt(STATE_TYPE, type);
		bundle.putInt(STATE_BORDER_RADIUS, mBorderRadius);
		return bundle;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (state instanceof Bundle) {
			Bundle bundle = (Bundle) state;
			super.onRestoreInstanceState(((Bundle) state)
					.getParcelable(STATE_INSTANCE));
			this.type = bundle.getInt(STATE_TYPE);
			this.mBorderRadius = bundle.getInt(STATE_BORDER_RADIUS);
		} else {
			super.onRestoreInstanceState(state);
		}

	}

	/**
	 * ��ʼ��BitmapShader
	 */
	private void setUpShader() {
		Drawable drawable = getDrawable();
		if (drawable == null) {
			return;
		}
		// Bitmap bmp = drawableToBitamp(drawable);
		Bitmap bmp = getBitmapFromDrawable(drawable);

		if (bmp == null) {
			invalidate();
			return;
		}
		// ��bmp��Ϊ��ɫ����������ָ�������ڻ���bmp
		mBitmapShader = new BitmapShader(bmp, TileMode.CLAMP, TileMode.CLAMP);
		float scale = 1.0f;
		if (type == TYPE_CIRCLE) {
			// �õ�bitmap���ߵ�Сֵ
			int bSize = Math.min(bmp.getWidth(), bmp.getHeight());
			scale = mWidth * 1.0f / bSize;

		} else if (type == TYPE_ROUND) {
			// ���ͼƬ�Ŀ���߸���view�Ŀ�߲�ƥ�䣬�������Ҫ���ŵı��������ź��ͼƬ�Ŀ�ߣ�һ��Ҫ��������view�Ŀ�ߣ�������������ȡ��ֵ��
			scale = Math.max(getWidth() * 1.0f / bmp.getWidth(), getHeight()
					* 1.0f / bmp.getHeight());
		}
		// shader�ı任��������������Ҫ���ڷŴ������С
		mMatrix.setScale(scale, scale);
		// ���ñ任����
		mBitmapShader.setLocalMatrix(mMatrix);
		// ����shader
		mBitmapPaint.setShader(mBitmapShader);
	}

	/**
	 * drawableתbitmap
	 * 
	 * @param drawable
	 * @return
	 */
	private Bitmap getBitmapFromDrawable(Drawable drawable) {
		if (drawable == null) {
			return null;
		}

		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}

		try {
			Bitmap bitmap;

			if (drawable instanceof ColorDrawable) {
				bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION,
						COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
			} else {
				bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(), BITMAP_CONFIG);
			}

			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
			drawable.draw(canvas);
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * �˷������ܲ��ܼ�������ͼƬ
	 * 
	 * @param borderRadius
	 */
	private Bitmap drawableToBitamp(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			BitmapDrawable bd = (BitmapDrawable) drawable;
			return bd.getBitmap();
		}
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		drawable.draw(canvas);
		return bitmap;
	}

	public void setBorderRadius(int borderRadius) {
		int pxVal = dp2px(borderRadius);
		if (this.mBorderRadius != pxVal) {
			this.mBorderRadius = pxVal;
			invalidate();
		}
	}

	public void setType(int type) {
		if (this.type != type) {
			this.type = type;
			if (this.type != TYPE_ROUND && this.type != TYPE_CIRCLE) {
				this.type = TYPE_CIRCLE;
			}
			requestLayout();
		}

	}

	public int dp2px(int dpVal) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dpVal, getResources().getDisplayMetrics());
	}
}
