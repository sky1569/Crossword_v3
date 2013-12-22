package com.crossword.activity;

import com.crossword.R;
import com.crossword.R.layout;
import com.crossword.R.menu;
import com.crossword.data.BroadMsg;
import com.crossword.utils.UserUtil;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private EditText accountEdText;
	private EditText passwordEdText;
	private Button   loginButton;
	private Button   registerNowButton;
	private TextView touristLoginTextView;
	private String   account;
	private String   password;
	private UserUtil userUtil;
	private Bundle   bundle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		accountEdText = (EditText)findViewById(R.id.login_account_input_edittext);
		passwordEdText = (EditText)findViewById(R.id.login_password_input_edittext);
		loginButton = (Button)findViewById(R.id.login_submit_button);
		registerNowButton = (Button)findViewById(R.id.register_now_button);
		touristLoginTextView = (TextView)findViewById(R.id.tourist_login_text);
		userUtil = new UserUtil();
		
		Intent intent = getIntent();
		bundle = intent.getExtras();
		//设置登录按钮的监听事件
		loginButton.setOnClickListener(loginSubmitListener);
		//设置现在注册按钮的监听事件
		registerNowButton.setOnClickListener(registerNowOnClickListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	
	private OnClickListener loginSubmitListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			userUtil.setAccount(accountEdText.getText().toString());
			userUtil.setPassword(passwordEdText.getText().toString());
			String verify = userUtil.login();
			//验证登录信息
			if(verify.equalsIgnoreCase("Success!")){
				if(UserUtil.loginStatus == true){
					
					Toast.makeText(LoginActivity.this, "不允许重复登录！", Toast.LENGTH_SHORT).show();
				}else{
					UserUtil.loginStatus = true;
					//当前的用户是什么
					UserUtil.currAccount = userUtil.getUsername();
					Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(LoginActivity.this,GridListActivity.class);
	                intent.putExtras(bundle);
	                startActivity(intent);
	                LoginActivity.this.finish();
				}
			}else if(verify.equalsIgnoreCase("Wrong password!")){//密码错误
				Toast.makeText(LoginActivity.this, "密码错误！", Toast.LENGTH_SHORT).show();
				UserUtil.loginStatus = false;
			}else if(verify.equalsIgnoreCase("Wrong username!")){//用户名错误
				Toast.makeText(LoginActivity.this, "用户名错误！", Toast.LENGTH_SHORT).show();
				UserUtil.loginStatus = false;
			}
		}
		
	};
	
	
	private OnClickListener registerNowOnClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			Intent intent = new Intent();
			intent.setClass(LoginActivity.this, RegisterActivity.class);
			startActivity(intent);
		}
		
	};


}
