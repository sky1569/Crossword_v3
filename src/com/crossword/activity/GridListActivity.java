package com.crossword.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.GridView;

import com.crossword.R;
import com.crossword.utils.Module;

public class GridListActivity extends Activity{
	private Module          module;
	private GridView        gridListView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gridlist);
		gridListView = (GridView) findViewById(R.id.gridlist_grid);
		module = new Module(this);
		//从URL请求期数，并解析
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
	
}
