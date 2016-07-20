package com.example.customimageview.view;

import android.content.Context;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.view.View;

public class FoldSlidingPanelLayout extends SlidingPaneLayout {

	public FoldSlidingPanelLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		View child = getChildAt(0);
		if (child != null) {
			removeView(child);
			removeView(child);

		}
	}
}
