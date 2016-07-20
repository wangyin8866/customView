package com.example.customimageview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.customimageview.view.ZoomImageView;

/**
 * viewPager+zoomImageview
 * 
 * @author DELL
 *
 */
public class MainActivity extends Activity {
	private ViewPager mViewPager;

	private int[] mImgs = { R.drawable.yongshi01, R.drawable.yongshi02,
			R.drawable.yongshi03 };
	private ImageView[] mImageViews = new ImageView[mImgs.length];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zoom_imageview);
		
		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
		mViewPager.setAdapter(new PagerAdapter() {

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				container.removeView(mImageViews[position]);

			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				ZoomImageView zoomImageView = new ZoomImageView(
						MainActivity.this);
				zoomImageView.setImageResource(mImgs[position]);
				container.addView(zoomImageView);
				mImageViews[position] = zoomImageView;
				return zoomImageView;
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return mImgs.length;
			}
		});
	}
}
