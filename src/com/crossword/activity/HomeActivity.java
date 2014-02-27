package com.crossword.activity;

//import android.R;
import android.app.Activity;
import android.app.ActivityManager;
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
import com.crossword.utils.ActivityUtil;
import com.crossword.utils.JsonUtil;
import com.crossword.utils.Module;
import com.crossword.utils.UserUtil;

public class HomeActivity extends Activity{
	public static  Vol broadMsg;
	public static boolean ISBroad = false;
	private Module module;
	public static int NumBroad;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		

		module = new Module(this);
		
		Button bnHistory = (Button)findViewById(R.id.button_1);
		
		Button bnBreakthough = (Button)findViewById(R.id.button_3);
		
		ImageButton bnSetting = (ImageButton)findViewById(R.id.set_button);
		//JsonUtil js =new JsonUtil(this);
		
		
		bnHistory.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View source) {
				// TODO Auto-generated method stub
				
				//Intent intent = new Intent (HomeActivity.this,HistoryActivity.class);
				//startActivity(intent);
		        ActivityUtil.startActivity(HomeActivity.this, HistoryActivity.class);
				
			}
		});
		
		bnBreakthough.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View source) {
				// TODO Auto-generated method stub
				
				//Intent intent = new Intent (HomeActivity.this,CategoryActivity.class);
				//startActivity(intent);
				ActivityUtil.startActivity(HomeActivity.this, CategoryActivity.class);
				
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
		 Button bnRank = (Button)findViewById(R.id.button_4);
		 Button bnLive = (Button)findViewById(R.id.button_2);
		 bnRank.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View source) {
					// TODO Auto-generated method stub
					
				    /*Intent intent = new Intent (HomeActivity.this,IndividualActivity.class);
					intent.putExtra("volNumber", broadMsg.getVolNumber());
					startActivity(intent);*/
					Bundle bundle = new Bundle();
					bundle.putInt("volNumber",  broadMsg.getVolNumber());
					ActivityUtil.holdOnActivity(IndividualActivity.class, bundle);
					ActivityUtil.startActivity(HomeActivity.this, IndividualActivity.class,bundle);
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
		    	//intent.setClass(HomeActivity.this, GridListActivity.class);
		    	Bundle bundle = new Bundle();
		    	bundle.putSerializable("currentVol", broadMsg);
		    	ActivityUtil.holdOnActivity(GridListActivity.class, bundle);
		    	ActivityUtil.startActivity(HomeActivity.this, GridListActivity.class,bundle);
		    	//intent.putExtras(bundle);
		    	//startActivity(intent);

			} 
		});
		
		if(!this.ConTest())
		{
			bnLive.setTextColor(getResources().getColor(R.color .home_disable_text_color));
			bnLive.setClickable(false);
			bnRank.setTextColor(getResources().getColor(R.color .home_disable_text_color));
			bnRank.setClickable(false);
		}
		else
		{
			try{
				//if(module.parseBroadFromUrl(Crossword.BROADCAST_URL)!=null)
					broadMsg = module.parseBroadFromUrl(Crossword.BROADCAST_URL);
				//if(broadMsg)
			   }
			catch (Exception e) {
		//		this.ConTest();// TODO: handle exception
				
			}
		
			if(broadMsg.getIsbroad())
			{	
				this.ISBroad = true ;//ȫ�����ж��ã�
				this.NumBroad = broadMsg.getVolNumber();
				bnLive.setTextColor(getResources().getColor(R.color.home_normal_text_color));
				bnLive.setClickable(true);
			}
	
			else
			{
				this.ISBroad = false;
		
				bnLive.setTextColor(getResources().getColor(R.color .home_disable_text_color));
				bnLive.setClickable(false);
	
			}
			
			
			
			//�жϵ�¼��״̬
			if(UserUtil.loginStatus == 1)
				{
				//Toast.makeText(this, UserUtil.currAccount+","+"����!", Toast.LENGTH_SHORT).show();
				}
			else if(UserUtil.loginStatus == 0)
				{
					Toast.makeText(this, "�ο͵�¼���޷�����ֱ����", Toast.LENGTH_SHORT).show();
				}
		
		}
		super.onResume();
	}
	
	public boolean ConTest()
	{
		ConnectivityManager cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo info = cwjManager.getActiveNetworkInfo(); 
		if (info != null && info.isAvailable()){ 
			if (info.getState() == NetworkInfo.State.CONNECTED)
		//do nothing 
			{
				Log.v("����������", info.toString());
				return true;
			}
			else return false;
		} 
		else
		{
			//	Log.v("������û��", info.toString());
				Toast.makeText(this,"����������", Toast.LENGTH_SHORT).show();
				return false;
		}
	}
}
