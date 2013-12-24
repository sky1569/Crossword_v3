package com.crossword.activity;

//import android.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.crossword.Crossword;
import com.crossword.R;
import com.crossword.data.Vol;
import com.crossword.utils.JsonUtil;
import com.crossword.utils.Module;
import com.crossword.utils.UserUtil;

public class HomeActivity extends Activity{
	public static  Vol broadMsg;
	public static boolean ISBroad = false;
	private Module module;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		

		module = new Module(this);
		
		Button bnHistory = (Button)findViewById(R.id.button_1);
		Button bnLive = (Button)findViewById(R.id.button_2);
		Button bnBreakthough = (Button)findViewById(R.id.button_3);
		Button bnRank = (Button)findViewById(R.id.button_4);
		ImageButton bnSetting = (ImageButton)findViewById(R.id.set_button);
		//JsonUtil js =new JsonUtil(this);
		broadMsg = module.parseBroadFromUrl(Crossword.BROADCAST_URL);

	
			if(broadMsg.getIsbroad())
				{	
					this.ISBroad = true ;//ȫ�����ж��ã�
					bnLive.setTextColor(getResources().getColor(R.color.home_normal_text_color));
					bnLive.setClickable(true);
				}
		
		    else {
				this.ISBroad = false;
		
				bnLive.setTextColor(getResources().getColor(R.color .home_disable_text_color));
				bnLive.setClickable(false);
		
		}
		
		bnHistory.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View source) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent (HomeActivity.this,HistoryActivity.class);
				startActivity(intent);
				
			}
		});
		bnLive.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View source) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent();
				//���ݵ�¼״̬�жϸ�����LoginActivity����GridListActivity
			/*	if(UserUtil.loginStatus == 1){
					intent.setClass(HomeActivity.this, GridListActivity.class);
				}else{
					intent.setClass(HomeActivity.this, LoginActivity.class);
				}*/
		    	intent.setClass(HomeActivity.this, GridListActivity.class);
		    	Bundle bundle = new Bundle();
		    	bundle.putSerializable("currentVol", broadMsg);
		    	intent.putExtras(bundle);
		    	startActivity(intent);

			}
		});
		bnBreakthough.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View source) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent (HomeActivity.this,CategoryActivity.class);
				startActivity(intent);
				
			}
		});
		bnRank.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View source) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent (HomeActivity.this,IndividualActivity.class);
				intent.putExtra("volNumber", broadMsg.getVolNumber());
				startActivity(intent);
			}
		});
		
		bnSetting.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View source) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent (HomeActivity.this,GameActivity.class);
				startActivity(intent);
				
			}
		});
		
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode,KeyEvent event){
		
		if(keyCode == KeyEvent.KEYCODE_BACK){
			UserUtil.loginStatus = -1;
			HomeActivity.ISBroad = false;
			android.os.Process.killProcess(android.os.Process.myPid());
			//HomeActivity.this.finish();
			return true;
		}
		
		return true;
	}

	
	@Override
	public void onResume(){
		
		//�жϵ�¼��״̬
		if(UserUtil.loginStatus == 1){
			Toast.makeText(this, UserUtil.currAccount+","+"����!", Toast.LENGTH_SHORT).show();
		}else if(UserUtil.loginStatus == 0){
			Toast.makeText(this, "�ο͵�¼���޷�����ֱ����", Toast.LENGTH_SHORT).show();
		}
		super.onResume();
	}
}
