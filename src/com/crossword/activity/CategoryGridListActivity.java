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
import com.crossword.Crossword;
import com.crossword.R;
import com.crossword.adapter.GridListAdapter;
import com.crossword.data.Grid;
import com.crossword.utils.DBManager;
import com.crossword.utils.Module;


public class CategoryGridListActivity extends Activity implements OnTouchListener{
	private Module          module;        
	private GridView        gridListView;
	private LinkedList<Grid> entities; 
	private Grid 			currentGrid;
	private DBManager		db;
	private Intent intent;
	private Bundle bundle;
	private String category = "";
	private ImageButton returnButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
    public void onResume()
    {
    	super.onResume();
    	setContentView(R.layout.category_grid_list);
		module = new Module(this);
		this.intent = getIntent();
		this.category = (String) intent.getCharSequenceExtra("category");
		this.bundle = intent.getExtras();
		
		returnButton  = (ImageButton)findViewById(R.id.gridlist_return_button);
		returnButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CategoryGridListActivity.this.finish();
			}
			
		});
		
		
		
		
    	try
    	{
			gridListView = (GridView) findViewById(R.id.category_gridlist_grid);
			GridListAdapter gridListAdapter = new GridListAdapter(this,this.entities);
			gridListView.setAdapter(gridListAdapter);
			gridListView.setOnTouchListener(this);

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
	
	
	
	

	
}
