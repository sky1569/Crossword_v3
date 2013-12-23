package com.crossword.activity;

import java.util.LinkedList;

import com.crossword.R;
import com.crossword.adapter.RankAdapter;
import com.crossword.data.Rank;
import com.crossword.utils.UserUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

public class IndividualActivity extends Activity {
	private ListView rankList;
	private RankAdapter rankAdapter;
	private UserUtil    userUtil;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.individual);
		rankList = (ListView)findViewById(R.id.rank_list);
		Intent intent = getIntent();
		int volNumber = intent.getIntExtra("volNumber", 101001);
		userUtil = new UserUtil();
		LinkedList<Rank> rankedList = userUtil.getRank(volNumber);;
		
 		rankAdapter = new RankAdapter(this,rankedList);
		rankList.setAdapter(rankAdapter);
	}

}
