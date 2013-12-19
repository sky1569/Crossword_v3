package com.crossword.activity;

import java.util.LinkedList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.crossword.R;
import com.crossword.adapter.GridListAdapter;
import com.crossword.adapter.VolGridAdapter;
import com.crossword.data.Grid;
import com.crossword.data.Vol;
import com.crossword.utils.DBManager;
import com.crossword.utils.Module;

public class GridListActivity extends Activity{
	private Module          module;
	private GridView        gridListView;
	private LinkedList<Grid> entities; 
	private Grid 			currentGrid;
	private DBManager		db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gridlist);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		Vol vol=(Vol)bundle.getSerializable("currentVol");
		System.out.println("test..."+vol.getAmountOfLevels());
		//Grid grid = new Grid();
		//DBManager db = new DBManager(this); 
		//GridView  gridListView = new GridView(this);
		module = new Module(this);
		
		this.entities = module.getGrids(vol.getAmountOfLevels(),vol.getVolNumber());
		
		TextView gridListTitleText = (TextView)findViewById(R.id.gridlist_title_text);
		System.out.println(vol.getVolName());
		gridListTitleText.setText(vol.getVolName());
		gridListView = (GridView) findViewById(R.id.gridlist_grid);
		GridListAdapter gridListAdapter = new GridListAdapter(this,this.entities);
		gridListView.setAdapter(gridListAdapter);
		//��URL����������������
		//entities = module.getNewestVol();
		//volGridAdapter = new VolGridAdapter(this,entities);
		//volGridView.setAdapter(volGridAdapter);
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
		    	this.currentGrid = this.entities.get(index);
		    	System.out.println("index..."+index);
		    	Intent intent = new Intent();
		    	intent.setClass(this, GameActivity.class);
		    	Bundle bundle2 = new Bundle();
		    	bundle2.putSerializable("currentGrid", currentGrid);
		    //	intent.putExtras(bundle);
		    	startActivity(intent);
		    	break;
		}
		return true;
	}
	
}