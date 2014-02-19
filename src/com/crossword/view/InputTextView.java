package com.crossword.view;



import android.content.Context;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

public class InputTextView extends TextView{

	private Context context;
	private InputMethodManager input;
	public InputTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
		this.context = context;
		this.input = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
	/*	this.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				input.showSoftInput(InputTextView.this, 0);
				
			}
			
		});*/
	}

	

	
	
	class MyInputConnection extends BaseInputConnection{

		public MyInputConnection(View targetView, boolean fullEditor) {
			super(targetView, fullEditor);
			// TODO Auto-generated constructor stub
		}
		@Override
		public boolean commitText(CharSequence text,int newCursorPosition){
			
			boolean success = super.commitText(text, newCursorPosition);
			InputTextView.this.setText(text);
			return success;
		}
		
	}
	
    @Override 
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) { 
    	//super.onCreateInputConnection(outAttrs); 
    	return new MyInputConnection(this,false);
        
    }
	
	
}
