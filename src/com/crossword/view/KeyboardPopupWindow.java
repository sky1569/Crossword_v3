package com.crossword.view;

import com.crossword.R;
import com.crossword.keyboard.KeyboardView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

public class KeyboardPopupWindow extends PopupWindow {
	
	private KeyboardView keyboard;
	public KeyboardPopupWindow(Context context){
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		keyboard = (KeyboardView) inflater.inflate(R.layout.keyboard, null);
		this.setContentView(keyboard);
		this.setWidth(LayoutParams.FILL_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		this.setAnimationStyle(R.anim.keyboard_popup_up);
	}
	
	public void onDismiss(){
		//this.dismiss();
		this.setAnimationStyle(R.anim.keyboard_popup_down);
		if(!this.isShowing()){
			this.dismiss();
		}
		
	}

}
