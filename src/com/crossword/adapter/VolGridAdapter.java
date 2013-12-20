package com.crossword.adapter;

import java.util.LinkedList;

import com.crossword.R;
import com.crossword.data.Vol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class VolGridAdapter extends BaseAdapter {

	private int columnNum ;
	private int volLength;
	private Context context;
	private LinkedList<Vol> entities;
	//private RelativeLayout volGridLayout;
	private LayoutInflater inflater;
	public VolGridAdapter(Context context,LinkedList<Vol> entities){
		this.context = context;
		//列数是固定的3
		this.columnNum = 3;
		this.volLength = entities.size();
		this.entities = entities;
		this.inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return volLength+1;
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
	    view = inflater.inflate(R.layout.vol_item, null);
		RelativeLayout volGridLayout = (RelativeLayout) view.findViewById(R.id.vol_gird_layout);
		TextView volNumberText = (TextView)view.findViewById(R.id.vol_number_text);
		TextView volScoreText = (TextView)view.findViewById(R.id.vol_score_text);
		volGridLayout.setLayoutParams(new GridView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	     if(position < volLength){//若position不超过volLength,则显示所有期的信息；否则显示下一期和正在直播的
			Vol entity = entities.get(position);
			String volName = entity.getVolName();
			System.out.println(volName);
			int score = entity.getScore();
			volNumberText.setText(volName);
			//如果当前的积分是0，则计算
			if(score == 0){
				volGridLayout.setBackgroundResource(R.drawable.vol_enable);
				volScoreText.setText(this.context.getResources().getText(R.string.no_current_socre));
			}else{
				volGridLayout.setBackgroundResource(R.drawable.vol_active);
				volScoreText.setText("积分:"+score);	
			}
	     }else if(position == volLength){//最后一个显示下一期的信息
	    	 volGridLayout.setBackgroundResource(R.drawable.vol_next);
	    	 volScoreText.setText("12月10日");
	    	 volNumberText.setText(this.context.getResources().getText(R.string.next_vol_text));
	     }
			
		
		
			return view;
	}

}
