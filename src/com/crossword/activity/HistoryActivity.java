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
import android.view.Menu;
import android.widget.GridView;

public class HistoryActivity extends Activity {

	
	private LinkedList<Vol> entities;
	private GridView        volGridView;
	private VolGridAdapter  volGridAdapter;
	private Module          module;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history);
		volGridView = (GridView) findViewById(R.id.vol_grid);
		module = new Module(this);
		//从URL请求期数，并解析
		entities = module.parseVolFromUrl(Crossword.VOL_REQUEST_RUL);
		volGridAdapter = new VolGridAdapter(this,entities);
		volGridView.setAdapter(volGridAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.history, menu);
		return true;
	}

}
