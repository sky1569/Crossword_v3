package com.crossword.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class MyGridView extends GridView{

	public MyGridView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public MyGridView(Context context,AttributeSet attr){
		super(context,attr);
	}
	
	public MyGridView(Context context,AttributeSet attr,int defStyle){
		super(context,attr,defStyle);
	}
	
	@Override
	public void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
		
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

}
