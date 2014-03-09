package com.crossword.adapter;

import java.util.LinkedList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crossword.Crossword;
import com.crossword.R;
import com.crossword.data.Grid;
import com.crossword.data.Vol;

public class GridListAdapter extends BaseAdapter {
	private int gridListLength;
	private Context context;
	private LinkedList<Grid> entities;
	private LayoutInflater inflater;
	public GridListAdapter(Context context,LinkedList<Grid> entities){
		this.context = context;
		//列数是固定的3
		this.gridListLength = entities.size();
		this.entities = entities;
		System.out.println(entities.size());
        
		this.inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return gridListLength;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		View view = new View(this.context);
	    view = inflater.inflate(R.layout.grid_item, null);
		RelativeLayout gridListLayout = (RelativeLayout) view.findViewById(R.id.gridList_layout);
		gridListLayout.setLayoutParams(new GridView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		TextView gridNumberText = (TextView)view.findViewById(R.id.grid_number_text);
		ImageView lockedView=(ImageView)view.findViewById(R.id.grid_lock_img);
	     if(position < gridListLength){//若position不超过volLength,则显示所有期的信息；否则显示下一期和正在直播的
	    	 Grid entity = entities.get(position);
	    	 
		    int gridLevel = entity.getLevel();
			gridNumberText.setText(""+gridLevel); 
			if(entity.getIslocked()==Crossword.GRIDUNLOCKED) lockedView.setImageResource(R.drawable.unlocked_icon);
				else lockedView.setImageResource(R.drawable.locked_icon);
			switch (entity.getStar())
			{
				case 1:
					gridListLayout.setBackgroundResource(R.drawable.star1_bg);
					break;
				case 2:
					gridListLayout.setBackgroundResource(R.drawable.stars2_bg);
					break;
				case 3:
					gridListLayout.setBackgroundResource(R.drawable.stars3_bg);
						break;
				default:
					gridListLayout.setBackgroundResource(R.drawable.nostar_bg);
					break;
				}
			}
	    
			
		
		
			return view;
	}
}
