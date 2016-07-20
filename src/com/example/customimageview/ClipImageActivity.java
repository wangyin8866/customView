package com.example.customimageview;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class ClipImageActivity extends Activity {
	private ImageView mImageView;
	private Bitmap mBitmap;
	private String urlString = "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1463038458&di=59f70c2b9f13cf89389ff693f471162a&src=http://h.hiphotos.bdimg.com/imgad/pic/item/14ce36d3d539b600b53cff8dee50352ac65cb71e.jpg";
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			mImageView.setImageBitmap(mBitmap);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.roundimage);
		mImageView = (ImageView) findViewById(R.id.id_iv);

		new Thread() {
			@Override
			public void run() {
				super.run();

				try {
					URL url = new URL(urlString);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					InputStream is = conn.getInputStream();
					mBitmap = BitmapFactory.decodeStream(is);
					mHandler.sendEmptyMessage(0x123);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();

	}

}
