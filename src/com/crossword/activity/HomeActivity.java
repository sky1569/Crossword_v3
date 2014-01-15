package com.crossword.activity;

//import android.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
		
		Button bnBreakthough = (Button)findViewById(R.id.button_3);
		Button bnRank = (Button)findViewById(R.id.button_4);
		ImageButton bnSetting = (ImageButton)findViewById(R.id.set_button);
		//JsonUtil js =new JsonUtil(this);
		
		
		bnHistory.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View source) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent (HomeActivity.this,HistoryActivity.class);
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
				
				//Intent intent = new Intent (HomeActivity.this,GameActivity.class);
				//startActivity(intent);
				
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
			try{
				broadMsg = module.parseBroadFromUrl(Crossword.BROADCAST_URL);
			   }
			catch (Exception e) {
				this.ConTest();// TODO: handle exception
			}
			Button bnLive = (Button)findViewById(R.id.button_2);
			
			if(broadMsg.getIsbroad())
			{	
				this.ISBroad = true ;//全部期判断用：
				bnLive.setTextColor(getResources().getColor(R.color.home_normal_text_color));
				bnLive.setClickable(true);
			}
	
			else {
			this.ISBroad = false;
	
			bnLive.setTextColor(getResources().getColor(R.color .home_disable_text_color));
			bnLive.setClickable(false);
	
	}
			bnLive.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View source) {
					// TODO Auto-generated method stub
					
					Intent intent = new Intent();
					//根据登录状态判断该启动LoginActivity还是GridListActivity
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
		//判断登录的状态
		if(UserUtil.loginStatus == 1){
			//Toast.makeText(this, UserUtil.currAccount+","+"您好!", Toast.LENGTH_SHORT).show();
		}else if(UserUtil.loginStatus == 0){
			Toast.makeText(this, "游客登录，无法进行直播！", Toast.LENGTH_SHORT).show();
		}
		super.onResume();
	}
	
	public void ConTest()
	{
		ConnectivityManager cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo info = cwjManager.getActiveNetworkInfo(); 
		if (info != null && info.isAvailable()){ 
		//do nothing 
		} 
		else
		{
		
				Toast.makeText(this,"无网络连接", Toast.LENGTH_SHORT).show();
		}
	}
}
