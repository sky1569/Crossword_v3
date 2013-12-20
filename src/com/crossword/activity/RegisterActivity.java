package com.crossword.activity;

import com.crossword.R;
import com.crossword.R.layout;
import com.crossword.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Paint;
import android.text.Html;
import android.view.Menu;
import android.widget.TextView;

public class RegisterActivity extends Activity {

	private TextView  loginNowTextView; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		loginNowTextView = (TextView)findViewById(R.id.login_now_text);
		loginNowTextView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		loginNowTextView.setText(Html.fromHtml(getResources().getString(R.string.login_now)));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

}
