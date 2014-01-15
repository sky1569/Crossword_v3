/*
 * Copyright 2011 Alexis Lauper <alexis.lauper@gmail.com>
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of 
 * the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.crossword.adapter;

import java.util.HashMap;
import java.util.LinkedList;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.crossword.Crossword;
import com.crossword.R;
import com.crossword.data.Word;
import com.crossword.utils.Module;

public class GameGridAdapter extends BaseAdapter {

	private HashMap<Integer, TextView>	views = new HashMap<Integer, TextView>();
	private Context						context;

	private int 						displayHeight;
	private int 						width;
	private int 						height;
    private int 						displayWidth;
	private Module						module;
	public GameGridAdapter(Activity act, LinkedList<Word> entries, int width, int height,Module module)
	{
	//	final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(act);
		this.module = module;
		
		this.context = (Context)act;
		this.width = width;
		this.height = height;
		//this.testwidth=10;
        Display display = act.getWindowManager().getDefaultDisplay();
       // this.displayHeight = (display.getHeight()/this.height)<(display.getWidth()/this.width)?(display.getHeight()/this.height):(display.getWidth()/this.width);
       // this.displayWidth  =display.getWidth()/this.width;
        this.displayHeight = display.getWidth() /this.width;
        this.module.initentries();
        this.module.isComplete(act);
	    
	}
	
	@Override
	public int getCount() {
		return this.height * this.width;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		//Log.v("positiontest", ""+position);
		TextView v = this.views.get(position);
		int y = (int)(position / this.width); 
		int x = (int)(position % this.width);
	    
		String data = (!this.module.getdisplayAreaValue(x, y).equals(Crossword.UNFILLED) ) ? this.module.getdisplayAreaValue(x, y) :Crossword.BLANK;
	    
		if (v == null)
		{
			//Log.v("positiontestif v==null", ""+position);
			v = new TextView(context);
			v.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.FILL_PARENT, this.displayHeight));
			v.setTextSize((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4 ? 30 : 20);
			v.setGravity(Gravity.CENTER);

			if (!data .equals( Crossword.BLOCK)) {
				//v.setBackgroundResource(R.drawable.area_empty);
				//设置textview的背景颜色为empty_color
				v.setBackgroundResource(R.color.empty_color);
				//v.setBackgroundResource(R.drawable.empty_area_background);
				v.setTag(Crossword.AREA_WRITABLE);
			} else {
				//v.setBackgroundResource(R.drawable.area_block1);
				//设置textview的背景颜色是block_color
				v.setBackgroundResource(R.color.block_color);
				//v.setBackgroundResource(R.drawable.block_area_background);
				v.setTag(Crossword.AREA_BLOCK);
			}
			
			this.views.put(position, v);
		}

		if(!data .equals( Crossword.BLOCK))
		{         //   System.out.println(""+this.width);
					v.setTextColor(context.getResources().getColor(R.color.normal));//test
					v.setText(data.toUpperCase());
			
		}
		return v;
	}

	
	
	//重新绘制，把小格的背景变为初始状态
	public void reDrawGridBackground(GridView v){
	
	//	for(int y = 0;y < this.height;y++){
		for(int y=v.getFirstVisiblePosition()/this.width;y <this.height;y++  ){	
		for(int x = 0;x < this.width;x++){
				
				
				//int index = y*this.width + x;
				int index =y*this.width + x-v.getFirstVisiblePosition();
			
				
				String value =  this.module.getAreaValue(x,y);
				
			
			if(v.getChildAt(index)!=null)
				v.getChildAt(index).setBackgroundResource(value .equals(( Crossword.BLOCK))?
                        R.color.block_color:R.color.empty_color);
				//v.getChildAt(index).setBackgroundResource(value .equals(( Crossword.BLOCK))?
                  //      R.drawable.block_area_background:R.drawable.empty_area_background);
		
			}
		}
	}
	

}
