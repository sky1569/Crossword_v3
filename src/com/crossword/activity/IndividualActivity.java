package com.crossword.activity;

import java.util.LinkedList;

import com.crossword.R;
import com.crossword.adapter.RankAdapter;
import com.crossword.data.Rank;
import com.crossword.data.Vol;
import com.crossword.utils.Module;
import com.crossword.utils.UserUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class IndividualActivity extends Activity {
	private ListView rankList;
	private RankAdapter rankAdapter;
	private UserUtil    userUtil;
	private TextView    individualAccountText;
	private TextView    individualRankText;
	private TextView    individualAccumulatePointText;
	
	private TextView titleRankText ;
	private TextView titleUsernameText ;
    private TextView titleAccumulatePointText;
    
    private ImageButton  enrollButton ;
	private Module      module;
	private ImageButton returnButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.individual);
		module = new Module(this);
		
		returnButton  = (ImageButton)findViewById(R.id.individual_return_button);
		enrollButton = (ImageButton)findViewById(R.id.enroll_button);
		enrollButton.setClickable(false);
		returnButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				IndividualActivity.this.finish();
			}
			
		});
		
		//先判断是否登录
		if(UserUtil.loginStatus != 1){
			
			Intent intent = new Intent(IndividualActivity.this,LoginActivity.class);
			startActivity(intent);
			IndividualActivity.this.finish();
		}
		
		rankList = (ListView)findViewById(R.id.rank_list);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		Vol  vol = (Vol)bundle.getSerializable("currVol");
		
		
		
		
		individualAccountText = (TextView)findViewById(R.id.individual_account_text);
		individualRankText = (TextView)findViewById(R.id.individual_rank_text);
		individualAccumulatePointText = (TextView)findViewById(R.id.individual_accumulate_point_text);
		
		
		//如果是游客登录
		/*if(UserUtil.loginStatus == 0){
			
			individualAccountText.setText("游客登录");
			individualRankText.setText("");
			individualAccumulatePointText.setText("游客登录，无积分");
			return;
		}*/

		
		userUtil = new UserUtil();
		LinkedList<Rank> rankedList = new LinkedList<Rank>();
		if(vol == null){//离线模式
			rankedList = userUtil.getRank();
			UserUtil.myScore = module.getOfflineScore();
			if(UserUtil.myRank <= 10){
				
				UserUtil.enrollAble = true;
				enrollButton.setClickable(true);
			}
		}else{
			vol = module.queryVolByVolNumber(vol.getVolNumber());
			rankedList = userUtil.getRank(vol.getVolNumber());
			UserUtil.myScore = vol.getScore();
			
		}
		
		
 		rankAdapter = new RankAdapter(this,rankedList);
 		titleRankText = (TextView)findViewById(R.id.title_rank_text);
 		titleUsernameText = (TextView)findViewById(R.id.title_username_text);
 		titleAccumulatePointText = (TextView)findViewById(R.id.title_accumulate_point_text);
 		rankAdapter.setTitleTextView(titleRankText, titleUsernameText, titleAccumulatePointText);

 		
		rankList.setAdapter(rankAdapter);
		rankList.setCacheColorHint(Color.TRANSPARENT);
        rankList.setDivider(null);
		
		individualAccountText.setText(UserUtil.currAccount);
		individualRankText.setText("No."+UserUtil.myRank);
		individualAccumulatePointText.setText("已获积分："+UserUtil.myScore);
		
		
		
		
		enrollButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}

	
	

	
	//动态设置文字的位置
	@Override
	public void onWindowFocusChanged(boolean hasFocus){
		super.onWindowFocusChanged(hasFocus);

	}
}
