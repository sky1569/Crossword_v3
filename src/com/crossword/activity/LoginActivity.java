package com.crossword.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.crossword.R;
import com.crossword.utils.UserUtil;

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
	private ImageButton returnButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		returnButton = (ImageButton)findViewById(R.id.login_return_button);
		returnButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LoginActivity.this.finish();
			}
			
		});
		accountEdText = (EditText)findViewById(R.id.login_account_input_edittext);
		passwordEdText = (EditText)findViewById(R.id.login_password_input_edittext);
		loginButton = (Button)findViewById(R.id.login_submit_button);
		registerNowButton = (Button)findViewById(R.id.register_now_button);
		touristLoginTextView = (TextView)findViewById(R.id.tourist_login_text);
		userUtil = new UserUtil();
		
		Intent intent = getIntent();
		bundle = intent.getExtras();
		//���õ�¼��ť�ļ����¼�
		loginButton.setOnClickListener(loginSubmitListener);
		//��������ע�ᰴť�ļ����¼�
		registerNowButton.setOnClickListener(registerNowOnClickListener);
		
		//�ο͵�¼�����¼�
		touristLoginTextView.setOnClickListener(touristLoginOnClickListener);
		
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
			//��֤��¼��Ϣ
			if(verify.equalsIgnoreCase("Success!")){
				if(UserUtil.loginStatus == 1){
					
					Toast.makeText(LoginActivity.this, "�������ظ���¼��", Toast.LENGTH_SHORT).show();
				}else{
					UserUtil.loginStatus = 1;
					//��ǰ���û���ʲô
					UserUtil.currAccount = userUtil.getAccount();
					Toast.makeText(LoginActivity.this, "��¼�ɹ���", Toast.LENGTH_SHORT).show();
					LoginActivity.this.finish();
					//Intent intent = new Intent(LoginActivity.this,GridListActivity.class);
	               // intent.putExtras(bundle);
	               // startActivity(intent);
	               // LoginActivity.this.finish();
				}
			}else if(verify.equalsIgnoreCase("Wrong password!")){//�������
				Toast.makeText(LoginActivity.this, "�������", Toast.LENGTH_SHORT).show();
				UserUtil.loginStatus = -1;
			}else if(verify.equalsIgnoreCase("Wrong username!")){//�û�������
				Toast.makeText(LoginActivity.this, "�û�������", Toast.LENGTH_SHORT).show();
				UserUtil.loginStatus = -1;
			}
		}
		
	};
	
	
	
	//�ο͵�¼�¼�
	
	private OnTouchListener touristLoginOnTouchListener = new OnTouchListener(){

		@Override
		public boolean onTouch(View arg0, MotionEvent event) {
			// TODO Auto-generated method stub
			
			switch(event.getAction()){
			
			case MotionEvent.ACTION_UP:
				
				UserUtil.loginStatus = 0;//����Ϊ�ο͵�¼
				LoginActivity.this.finish();
				break;
			
			}
			return true;
		}
		
	};
	
	
	
	private OnClickListener touristLoginOnClickListener = new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			UserUtil.loginStatus = 0;//����Ϊ�ο͵�¼
			LoginActivity.this.finish();
		}
		
	};
	
	
	
	private OnClickListener registerNowOnClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			Intent intent = new Intent();
			intent.setClass(LoginActivity.this, RegisterActivity.class);
			startActivity(intent);
			LoginActivity.this.finish();
		}
		
	};

	
	
	
	
	public void onReturnToHome(){
		
		Intent intent = new Intent();
		intent.setClass(LoginActivity.this, HomeActivity.class);
		LoginActivity.this.finish();
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode,KeyEvent event){
		
		if(keyCode == KeyEvent.KEYCODE_BACK){
			onReturnToHome();
			return true;
		}
		
		return false;
	}
	
	
	
	

}
