package com.crossword.activity;

import java.util.LinkedList;

import com.crossword.R;
import com.crossword.adapter.RankAdapter;
import com.crossword.data.Rank;
import com.crossword.data.Vol;
import com.crossword.utils.UserUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

public class IndividualActivity extends Activity {
	private ListView rankList;
	private RankAdapter rankAdapter;
	private UserUtil    userUtil;
	private TextView    individualAccountText;
	private TextView    individualRankText;
	private TextView    individualAccumulatePointText;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.individual);
		rankList = (ListView)findViewById(R.id.rank_list);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		Vol  vol = (Vol)bundle.getSerializable("currVol");
		
		
		
		individualAccountText = (TextView)findViewById(R.id.individual_account_text);
		individualRankText = (TextView)findViewById(R.id.individual_rank_text);
		individualAccumulatePointText = (TextView)findViewById(R.id.individual_accumulate_point_text);
		
		
		
		
		userUtil = new UserUtil();
		LinkedList<Rank> rankedList = new LinkedList<Rank>();
		if(vol == null){//离线模式
			rankedList = userUtil.getRank();
			UserUtil.myScore = 300;
		}else{
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
