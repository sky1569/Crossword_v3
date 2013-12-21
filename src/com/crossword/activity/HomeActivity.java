package com.crossword.activity;

//import android.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

import com.crossword.Crossword;
import com.crossword.R;
import com.crossword.data.BroadMsg;
import com.crossword.utils.JsonUtil;

public class HomeActivity extends Activity{
	private BroadMsg broadMsg;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		Button bnHistory = (Button)findViewById(R.id.button_1);
		Button bnLive = (Button)findViewById(R.id.button_2);
		Button bnBreakthough = (Button)findViewById(R.id.button_3);
		Button bnRank = (Button)findViewById(R.id.button_4);
		ImageButton bnSetting = (ImageButton)findViewById(R.id.set_button);
		
		JsonUtil js =new JsonUtil(this);
		
		String broadcastMsg = js.readJsonFromUrl(Crossword.BROADCAST_URL);
		 broadMsg = js.parseBroad(broadcastMsg);
		if(broadMsg.getIsbroad())
			{
				bnLive.setTextColor(getResources().getColor(R.color.home_normal_text_color));
				bnLive.setClickable(true);
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
				
				Intent intent = new Intent(HomeActivity.this,GridListActivity.class);
	//	    	intent.setClass(this, GridListActivity.class);
		    	Bundle bundle = new Bundle();
		    	bundle.putSerializable("currentBroad", broadMsg);
		    	intent.putExtras(bundle);
		    	startActivity(intent);
			}
		});
		bnBreakthough.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View source) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent (HomeActivity.this,GameActivity.class);
				startActivity(intent);
				
			}
		});
		bnRank.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View source) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent (HomeActivity.this,GameActivity.class);
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

}
