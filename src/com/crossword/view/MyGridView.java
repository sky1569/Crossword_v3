package com.crossword.view;

import com.crossword.Crossword;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnKeyListener;
import android.view.View.OnFocusChangeListener;

;
public class MyGridView extends GridView implements OnKeyListener {

	private String inputText = "";
	private InputMethodManager input;
	private Context context;
	private Handler handler;

	public MyGridView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub

	}

	public MyGridView(Context context, AttributeSet attr) {
		super(context, attr);
		this.context = context;
		this.setFocusable(false);
		this.setFocusableInTouchMode(true);
		this.input = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		this.setOnKeyListener(this);
		/*
		 * this.setOnTouchListener(new OnTouchListener(){
		 * 
		 * @Override public boolean onTouch(View arg0, MotionEvent arg1) { //
		 * TODO Auto-generated method stub input.showSoftInput(MyGridView.this,
		 * 0); return true; }
		 * 
		 * });
		 */
	}

	public MyGridView(Context context, AttributeSet attr, int defStyle) {
		super(context, attr, defStyle);
	}

	class MyInputConnection extends BaseInputConnection {

		public MyInputConnection(View targetView, boolean fullEditor) {
			super(targetView, fullEditor);

			// TODO Auto-generated constructor stub
		}

		@Override
		public boolean commitText(CharSequence text, int newCursorPosition) {
			boolean success = super.commitText(text, newCursorPosition);
			inputText = (String) text;
			// Handler handler = new Handler();
			handler.sendEmptyMessage(0x222);// 提交输入结果后需要进行显示的处理
			// Toast.makeText(context, inputText, Toast.LENGTH_SHORT).show();
			return success;
		}

	}

	@Override
	public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
		// super.onCreateInputConnection(outAttrs);
		return new MyInputConnection(this, false);

	}

	// 设置Handler，将GameActivity的Handler传递过来
	public void setHandler(Handler handler) {

		this.handler = handler;
	}

	// 获取软键盘的输入
	public String getSoftInputText() {

		return this.inputText;
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == KeyEvent.ACTION_UP
				&& event.getRepeatCount() == 0) {
			if (event.getKeyCode() == KeyEvent.KEYCODE_DEL) {

				new AlertDialog.Builder(context)
						.setTitle("删除警告")
						.setMessage("确定要删除所有内容吗？")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										inputText = Crossword.UNFILLED;
										handler.sendEmptyMessage(0x222);
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub

									}
								}).show();

			}
		}
		return true;
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(

		Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);

		super.onMeasure(widthMeasureSpec, expandSpec);

	}

}
