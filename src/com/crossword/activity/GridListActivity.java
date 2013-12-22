package com.crossword.activity;

import java.util.LinkedList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.GridView;
import android.widget.TextView;

import com.crossword.Crossword;
import com.crossword.R;
import com.crossword.adapter.GridListAdapter;
import com.crossword.data.BroadMsg;
import com.crossword.data.Grid;
import com.crossword.data.Vol;
import com.crossword.utils.DBManager;
import com.crossword.utils.Module;

public class GridListActivity extends Activity implements OnTouchListener{
	private Module          module;
	private GridView        gridListView;
	private LinkedList<Grid> entities; 
	private Grid 			currentGrid;
	private DBManager		db;
	private Intent intent;
	private Bundle bundle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*setContentView(R.layout.gridlist);
		module = new Module(this);
		this.intent = getIntent();
		this.bundle = intent.getExtras();
		try{
			Vol vol=(Vol)bundle.getSerializable("currentVol");		
			System.out.println("test..."+vol.getAmountOfLevels());			
			this.entities = module.getGrids(vol);			
			TextView gridListTitleText = (TextView)findViewById(R.id.gridlist_title_text);
			System.out.println(vol.getVolName());
			gridListTitleText.setText(vol.getVolName());
			gridListView = (GridView) findViewById(R.id.gridlist_grid);
			GridListAdapter gridListAdapter = new GridListAdapter(this,this.entities);
			gridListView.setAdapter(gridListAdapter);
			gridListView.setOnTouchListener(this);
		}
		catch(Exception e)
		{
			BroadMsg broad = (BroadMsg)this.bundle.getSerializable("currentBroad");
			this.entities = module.getGrids(broad);
			TextView gridListTitleText = (TextView)findViewById(R.id.gridlist_title_text);
			gridListTitleText.setText("正在直播");
			gridListView = (GridView) findViewById(R.id.gridlist_grid);
			GridListAdapter gridListAdapter = new GridListAdapter(this,this.entities);
			gridListView.setAdapter(gridListAdapter);
			gridListView.setOnTouchListener(this);
		}
	*/
		//从URL请求期数，并解析
		//entities = module.getNewestVol();
		//volGridAdapter = new VolGridAdapter(this,entities);
		//volGridView.setAdapter(volGridAdapter);
	}
    public void onResume()
    {
    	super.onResume();
    	setContentView(R.layout.gridlist);
		module = new Module(this);
		this.intent = getIntent();
		this.bundle = intent.getExtras();
    	try
    	{
    		Vol vol=(Vol)this.bundle.getSerializable("currentVol");		
			System.out.println("test..."+vol.getAmountOfLevels());			
			this.entities = module.getGrids(vol);	
			TextView gridListTitleText = (TextView)findViewById(R.id.gridlist_title_text);
			System.out.println("resume"+vol.getVolName());
			gridListTitleText.setText(vol.getVolName());
			gridListView = (GridView) findViewById(R.id.gridlist_grid);
			GridListAdapter gridListAdapter = new GridListAdapter(this,this.entities);
			gridListView.setAdapter(gridListAdapter);
			gridListView.setOnTouchListener(this);
    	}
    	catch(Exception e)
    	{
    		BroadMsg broad = (BroadMsg)bundle.getSerializable("currentBroad");
			this.entities = module.getGrids(broad);
			TextView gridListTitleText = (TextView)findViewById(R.id.gridlist_title_text);
			System.out.println("resume"+broad.getVolNumber());
			gridListTitleText.setText("正在直播");
			gridListView = (GridView) findViewById(R.id.gridlist_grid);
			GridListAdapter gridListAdapter = new GridListAdapter(this,this.entities);
			gridListView.setAdapter(gridListAdapter);
			gridListView.setOnTouchListener(this);	
    	}
    	
    }
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.history, menu);
		return true;
	}*/
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch(event.getAction()){
		 
		    case MotionEvent.ACTION_UP:
		    	int x = (int) event.getX();
		    	int y = (int) event.getY();
		    	int index = this.gridListView.pointToPosition(x, y);
		    //	if(index >= this.entities.size()) 
		    	//	break;
		    	
		    	if(index ==- 1)  break;
		    	this.currentGrid = this.entities.get(index);
		    	if(this.currentGrid.getIslocked()==Crossword.GRIDLOCKED)
		    		break;
		    	Log.v("ssssijsondata测试", ""+this.currentGrid.getJsonData());
		    	//System.out.println("index..."+index+"this.currentGrid..."+this.currentGrid==null?"t":"f");
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
	
}
