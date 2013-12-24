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
	private Module      module;
	private ImageButton returnButton;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.individual);
		module = new Module(this);
		
		returnButton  = (ImageButton)findViewById(R.id.individual_return_button);
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
		}else{
			vol = module.queryVolByVolNumber(vol.getVolNumber());
			rankedList = userUtil.getRank(vol.getVolNumber());
			UserUtil.myScore = vol.getScore();
			
		}
		
		
 		rankAdapter = new RankAdapter(this,rankedList);
 		TextView titleRankText = (TextView)findViewById(R.id.title_rank_text);
 		TextView titleUsernameText = (TextView)findViewById(R.id.title_username_text);
 		TextView titleAccumulatePointText = (TextView)findViewById(R.id.title_accumulate_point_text);
 		rankAdapter.setTitleTextView(titleRankText, titleUsernameText, titleAccumulatePointText);
		rankList.setAdapter(rankAdapter);

		
		individualAccountText.setText(UserUtil.currAccount);
		individualRankText.setText("No."+UserUtil.myRank);
		individualAccumulatePointText.setText("已获积分："+UserUtil.myScore);
		
		
		
	}

	
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus){
		super.onWindowFocusChanged(hasFocus);

	}
}
