

package com.crossword.adapter;

import java.util.HashMap;
import java.util.LinkedList;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.text.InputFilter;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.crossword.Crossword;
import com.crossword.R;
import com.crossword.data.Word;
import com.crossword.logic.BoardLogic;
import com.crossword.data.Character;

public class GameGridAdapter extends BaseAdapter {

	private HashMap<Integer, TextView>	views = new HashMap<Integer, TextView>();
	private Context						context;
    public static int                   GRID_WIDTH = 400;
	private int 						displayHeight;
	private int 						width;
	private int 						height;
    private int 						displayWidth;
	private BoardLogic 					boardLogic;

	public	GameGridAdapter(Activity act, LinkedList<Character> entries, int width, int height,BoardLogic boardLogic)
	{
		this.boardLogic = boardLogic;
		
		this.context = (Context)act;
		this.width = width;
		this.height = height;
        Display display = act.getWindowManager().getDefaultDisplay();

        this.displayWidth = (int)(GRID_WIDTH/this.width);
        this.displayHeight = this.displayWidth;

        this.boardLogic.initEntities2();
        this.boardLogic.isComplete(act);

	    
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

		TextView v = this.views.get(position);
		
		int y = (int)(position / this.width); 
		int x = (int)(position % this.width);
	    
		String data = (!this.boardLogic.getdisplayAreaValue(x, y).equals(Crossword.UNFILLED) ) ? this.boardLogic.getdisplayAreaValue(x, y) :Crossword.BLANK;
	    String status =(this.boardLogic.getArea(x, y));
		if (v == null)
		{
			v = new TextView(context);
			v.setText("");

			
			v.setLayoutParams(new GridView.LayoutParams(this.displayWidth, this.displayHeight));
			v.setTextSize((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4 ? 30 : 20);
			v.setGravity(Gravity.CENTER);
            if (!data .equals( Crossword.BLOCK)) {
				//设置textview的背景颜色为empty_color
			
				
					v.setBackgroundResource(R.color.empty_color);
					v.setTag(Crossword.AREA_WRITABLE);
				
			} else {
				//设置textview的背景颜色是block_color
				v.setBackgroundResource(R.color.block_color);
				v.setTag(Crossword.AREA_BLOCK);
			}
			
			this.views.put(position, v);
		}

		if(!data .equals( Crossword.BLOCK))
		{  
					
					
			if(status.equals(Crossword.WRONGFILLED))
			{
				v.setTextColor(context.getResources().getColor(R.color.wrong));
				v.setText(data.toUpperCase());
			}
			else    if(status.equals(Crossword.UNFILLEDABLE))
					{
						v.setTextColor(context.getResources().getColor(R.color.wrong));
						v.setText(data.toUpperCase());
					}
					else 
					{
						v.setTextColor(context.getResources().getColor(R.color.normal));//test
					    v.setText(data.toUpperCase());
					}
					
		}
		return v;
	}

	
	
	//在Gridview左边和上边画出标尺
	public void drawRuler(ViewGroup parent){//在父窗口中画出标尺
		
		//画出左边的格子
		for(int i = 0;i < this.height;i++){
		TextView v = new TextView(context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(25,LayoutParams.WRAP_CONTENT);
		//LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(this.displayWidth,this.displayHeight);
	    v.setLayoutParams(lp);
	    v.setPadding(0, 20+this.displayHeight/2+i*(this.displayHeight+1), 0, 0);
	    v.setGravity(Gravity.CENTER);
	    v.setText(""+(i+1));
	    v.setTextColor(Color.WHITE);
	    parent.addView(v);
		}
		
		
		
		//画出上边的格子
		for(int i = 0;i<this.width;i++){
			
			TextView v = new TextView(context);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			v.setLayoutParams(lp);
			v.setPadding(29+this.displayWidth/2+i*(this.displayWidth+1), 0, 0, 0);
			v.setGravity(Gravity.CENTER);
			v.setText(Crossword.ALPHABET[i]);
			v.setTextColor(Color.WHITE);
			parent.addView(v);
		}
		
	}
	
	
	
	//重新绘制，把小格的背景变为初始状态
	public void reDrawGridBackground(GridView v){
	
		for(int y=v.getFirstVisiblePosition()/this.width;y <this.height;y++  ){	
		for(int x = 0;x < this.width;x++){
				
				
				int index =y*this.width + x-v.getFirstVisiblePosition();
				
				String value =  this.boardLogic.getdisplayAreaValue(x,y);
				
			
			if(v.getChildAt(index)!=null)
				v.getChildAt(index).setBackgroundResource(value .equals(( Crossword.BLOCK))?
                        R.color.block_color:R.color.empty_color);

		
			}
		}
	}
	
    
	
	public void ScrollToItem(ScrollView scrollView,View child){
		
		scrollView.scrollTo(0, child.getBottom() - 2*this.displayHeight);
	}
}
