package com.crossword.activity;

import java.util.LinkedList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.crossword.Crossword;
import com.crossword.R;
import com.crossword.adapter.GridListAdapter;
import com.crossword.data.Grid;
import com.crossword.data.Vol;
import com.crossword.utils.DBManager;
import com.crossword.utils.Module;
import com.crossword.utils.UserUtil;

public class GridListActivity extends Activity implements OnTouchListener{
	private Module          module;
	private ImageButton     volRankButton;          
	private GridView        gridListView;
	private LinkedList<Grid> entities; 
	private Grid 			currentGrid;
	private DBManager		db;
	private Intent intent;
	private Bundle bundle;
	private Vol    vol;
	private ImageButton returnButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
    public void onResume()
    {
    	super.onResume();
    	setContentView(R.layout.gridlist);
		module = new Module(this);
		this.intent = getIntent();
		this.bundle = intent.getExtras();
		
		returnButton  = (ImageButton)findViewById(R.id.gridlist_return_button);
		returnButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GridListActivity.this.finish();
			}
			
		});
		
		volRankButton = (ImageButton)findViewById(R.id.vol_rank_button);
		volRankButton.setOnClickListener(volRankOnClickListener);
		
		
    	try
    	{
    		vol=(Vol)this.bundle.getSerializable("currentVol");		
    		module.updateVolScore(vol);
			System.out.println("test..."+vol.getAmountOfLevels());		
		    this.entities = module.getGrids(vol);	
			TextView gridListTitleText = (TextView)findViewById(R.id.gridlist_title_text);
			System.out.println("resume"+vol.getVolName());
			
			//如果正在直播
			if(vol.getIsbroad()){
				//先判断登录
				if(UserUtil.loginStatus != 1){//若未登录		
					Intent intent = new Intent(GridListActivity.this, LoginActivity.class);
					startActivity(intent);
					GridListActivity.this.finish();
				}
				
				gridListTitleText.setText("正在直播");
			}
			else gridListTitleText.setText(vol.getVolName());
			gridListView = (GridView) findViewById(R.id.gridlist_grid);
			GridListAdapter gridListAdapter = new GridListAdapter(this,this.entities);
			gridListView.setAdapter(gridListAdapter);
			gridListView.setOnTouchListener(this);
			int gameMode = vol.getIsbroad()?Crossword.GAMEMODELIVE:Crossword.GAMEMODEVOL;
			for(Grid g :entities)
			{
				g.setGameMode(gameMode);
			}
    	}
    	catch(Exception e)
    	{
    		

    	}
    	
    }

	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch(event.getAction()){
		 
		    case MotionEvent.ACTION_UP:
		    	int x = (int) event.getX();
		    	int y = (int) event.getY();
		    	int index = this.gridListView.pointToPosition(x, y);

		    	if(index ==- 1)  break;
		    	this.currentGrid = this.entities.get(index);
		    	if(this.currentGrid.getIslocked()==Crossword.GRIDLOCKED)
		    		break;
		    	Intent intent2 = new Intent();
		    	intent2.setClass(this, GameActivity.class);
		    	Bundle bundle2 = new Bundle();
		    	bundle2.putSerializable("currentGrid", this.currentGrid);
		        intent2.putExtras(bundle2);
		    	startActivity(intent2);
		    	break;
		}
		return true;
	}
	
	
	
	
	private OnClickListener volRankOnClickListener = new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(GridListActivity.this,IndividualActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("currVol", vol);
			intent.putExtras(bundle);
			startActivity(intent);
		}
		
	};
	
}
