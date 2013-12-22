package com.crossword.activity;

import com.crossword.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class IndividualActivity extends Activity {
	private ListView rankList;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.individual);
		rankList = (ListView)findViewById(R.id.rank_list);
		
	}

}
