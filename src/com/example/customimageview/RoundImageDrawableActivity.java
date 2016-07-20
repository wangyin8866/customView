package com.example.customimageview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.customimageview.view.CircleImageDrawable;
import com.example.customimageview.view.RoundImageDrawable;

public class RoundImageDrawableActivity extends Activity {
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);

		ImageView iv = (ImageView) findViewById(R.id.id_roundIv);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.yongshi02);
		iv.setImageDrawable(new CircleImageDrawable(bitmap));

	}
}
