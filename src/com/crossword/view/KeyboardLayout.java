package com.crossword.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;



public class KeyboardLayout  extends LinearLayout{
	
	public KeyboardLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}





	
	public KeyboardLayout(Context context){
		super(context);
	}

	
	public void setOnKbdStateListener(OnKybdsChangeListener listener){
		mListener = listener;
	}
	
	
	@Override
	protected void onLayout(boolean changed,int l,int t ,int r,int b){
		super.onLayout(changed, l, t, r, b);
		if(!mHasInit){
			mHasInit = true;
			mHeight = b;
			if(mListener != null){
				mListener.onKeyBoardStateChange(KEYBOARD_STATE_INIT);
			}else{
				mHeight = mHeight < b ?b:mHeight;
			}
			
			if(mHasInit && mHeight > b){
				mHasKeybord = true;
				if(mListener != null){
					mListener.onKeyBoardStateChange(KEYBOARD_STATE_SHOW);
				}
			}
			
			if(mHasInit && mHasKeybord && mHeight == b){
				mHasKeybord = false;
				if(mListener != null){
					mListener.onKeyBoardStateChange(KEYBOARD_STATE_HIDE);
				}
			}
		}
	}
	
	
	
	
	private static final String TAG = KeyboardLayout.class.getSimpleName();
	public static final byte   KEYBOARD_STATE_SHOW = -3;
	public static final byte   KEYBOARD_STATE_HIDE = -2;
	public static final byte   KEYBOARD_STATE_INIT = -1;
	
	private boolean mHasInit;
	private boolean mHasKeybord;
	private int mHeight;
	private OnKybdsChangeListener mListener;
	

	
	public interface OnKybdsChangeListener{
		public void onKeyBoardStateChange(int state);
	}
	

}
