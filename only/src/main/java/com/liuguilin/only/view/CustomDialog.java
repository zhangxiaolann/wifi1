package com.liuguilin.only.view;


import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.liuguilin.only.R;

/**
 * 自定义提示框
 */
public class CustomDialog extends Dialog {

	public CustomDialog(Context context, int layout, int style) {
		this(context, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT, layout, style,
				Gravity.CENTER);
	}

	public CustomDialog(Context context, int width, int height, int layout,
			int style, int gravity, int anim) {
		super(context, style);

		setContentView(layout);
		// set window params
		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		// set width,height by density and gravity
		// float density = getDensity(context);
		params.width = WindowManager.LayoutParams.MATCH_PARENT;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.gravity = gravity;
		window.setAttributes(params);
		window.setWindowAnimations(anim);
		//window.setWindowAnimations(R.style.pop_anim_style);
	}

	public CustomDialog(Context context, int width, int height, int layout,
			int style, int gravity) {
		this(context, width, height, layout, style, gravity,
				R.style.pop_anim_style);
		// setCanceledOnTouchOutside(true);
	}
	
}