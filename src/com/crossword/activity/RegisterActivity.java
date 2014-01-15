package com.crossword.activity;

import com.crossword.R;
import com.crossword.R.layout;
import com.crossword.R.menu;
import com.crossword.utils.UserUtil;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Paint;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	private TextView  loginNowTextView; 
	private EditText  accountEdText;
	private EditText  telephoneEdText;
	private EditText  passwordEdText;
	private Button    submitButton;
	private ImageButton returnButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		returnButton = (ImageButton)findViewById(R.id.register_return_button);
		returnButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RegisterActivity.this.finish();
			}
			
		});
		
		loginNowTextView = (TextView)findViewById(R.id.login_now_text);
		accountEdText = (EditText)findViewById(R.id.register_account_input_edittext);
		telephoneEdText = (EditText)findViewById(R.id.telephone_input_edittext);
		passwordEdText = (EditText)findViewById(R.id.register_password_input_edittext);
		submitButton = (Button)findViewById(R.id.register_submit_button);
		loginNowTextView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		loginNowTextView.setText(Html.fromHtml(getResources().getString(R.string.login_now)));
		
		
		
		//�����ύ�ĵ�������¼�
		submitButton.setOnClickListener(registerSubmitOnClickListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	
	
	private OnClickListener registerSubmitOnClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			UserUtil registerUserUtil = new UserUtil();
			registerUserUtil.setAccount(accountEdText.getText().toString());
			registerUserUtil.setUsername(telephoneEdText.getText().toString());
			registerUserUtil.setPassword(passwordEdText.getText().toString());
			
			//���ж��û���������Ƿ����
			if(registerUserUtil.getAccount().length() < 4){
				Toast.makeText(RegisterActivity.this, "�û������̣�", Toast.LENGTH_SHORT).show();
				return;
			}else if(registerUserUtil.getUsername().length() != 11){
				Toast.makeText(RegisterActivity.this, "�ֻ����벻����Ҫ��", Toast.LENGTH_SHORT).show();
				return;
			}else if(registerUserUtil.getPassword().length() < 6){
				Toast.makeText(RegisterActivity.this, "����������̣�", Toast.LENGTH_SHORT).show();
				return;
			}
			
			
			
			
			
			String verify = registerUserUtil.register();
			
			//��֤ע����Ϣ
			if(verify.equalsIgnoreCase("Successfully insert!")){//������ϢΪ�ɹ�����
				
				Toast.makeText(RegisterActivity.this, "ע��ɹ���", Toast.LENGTH_SHORT).show();
				RegisterActivity.this.finish();
			}else if(verify.equalsIgnoreCase("Username exists!")){//�û����Ѵ���
				Toast.makeText(RegisterActivity.this, "�û����Ѵ��ڣ�", Toast.LENGTH_SHORT).show();
			}else if(verify.equalsIgnoreCase("Error!")){
				Toast.makeText(RegisterActivity.this, "Erorr��", Toast.LENGTH_SHORT).show();
			}
		}
		
	};
}
