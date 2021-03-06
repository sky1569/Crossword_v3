package com.crossword.activity;

import java.util.LinkedList;

import com.crossword.Crossword;
import com.crossword.R;
import com.crossword.R.layout;
import com.crossword.R.menu;
import com.crossword.adapter.VolGridAdapter;
import com.crossword.data.Vol;
import com.crossword.utils.Module;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

public class HistoryActivity extends Activity implements OnTouchListener{

	
	private LinkedList<Vol> entities;
	private GridView        volGridView;
	private VolGridAdapter  volGridAdapter;
	private Module          module;
	private Vol             currentVol;
	private ImageButton     returnButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
	
	
	
	@Override
	public void onResume(){
		
		setContentView(R.layout.history);
		volGridView = (GridView) findViewById(R.id.vol_grid);
		module = new Module(this);
		
		returnButton = (ImageButton)findViewById(R.id.history_return_button);
		returnButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				HistoryActivity.this.finish();
			}
			
		});
		
		//从URL请求期数，并解析
		entities = module.getNewestVol();
		if(entities == null){
			Toast.makeText(this, "获取数据失败", Toast.LENGTH_SHORT).show();
			HistoryActivity.this.finish();
		}
		
		volGridAdapter = new VolGridAdapter(this,entities);
		volGridView.setAdapter(volGridAdapter);
		volGridView.setOnTouchListener(this);
		super.onResume();
	}
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.history, menu);
		return true;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch(event.getAction()){
		 
		    case MotionEvent.ACTION_UP:
		    	int x = (int) event.getX();
		    	int y = (int) event.getY();
		    	int index = this.volGridView.pointToPosition(x, y);
		    	if(index ==- 1)  break;
		    	this.currentVol = this.entities.get(index);
		    	System.out.println("index..."+index);
		    	Intent intent = new Intent();
		    	intent.setClass(this, GridListActivity.class);
		    	Bundle bundle = new Bundle();
		    	bundle.putSerializable("currentVol", currentVol);
		    	intent.putExtras(bundle);
		    	startActivity(intent);
		    	break;
		}
		return true;
	}

}
