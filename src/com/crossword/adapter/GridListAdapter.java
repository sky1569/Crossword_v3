package com.crossword.adapter;

import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crossword.R;
import com.crossword.data.Vol;

public class GridListAdapter extends BaseAdapter {
	private int columnNum ;
	private int volLength;
	private Context context;
	private LinkedList<Vol> entities;
	//private RelativeLayout volGridLayout;
	private LayoutInflater inflater;
	public GridListAdapter(Context context,LinkedList<Vol> entities){
		this.context = context;
		//�����ǹ̶���3
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
	    view = inflater.inflate(R.layout.grid_item, null);
		RelativeLayout gridListLayout = (RelativeLayout) view.findViewById(R.id.gridList_layout);
		TextView volNumberText = (TextView)view.findViewById(R.id.vol_number_text);
		TextView volScoreText = (TextView)view.findViewById(R.id.vol_score_text);
		
	     if(position < volLength){//��position������volLength,����ʾ�����ڵ���Ϣ��������ʾ��һ�ں�����ֱ����
			Vol entity = entities.get(position);
			String volName = entity.getVolName();
			System.out.println(volName);
			int score = entity.getScore();
			volNumberText.setText(volName);
			//�����ǰ�Ļ�����0�������
			if(score == 0){
				gridListLayout.setBackgroundResource(R.drawable.vol_enable);
				volScoreText.setText(this.context.getResources().getText(R.string.no_current_socre));
			}else{
				gridListLayout.setBackgroundResource(R.drawable.vol_active);
				volScoreText.setText("����:"+score);	
			}
	     }else if(position == volLength){//���һ����ʾ��һ�ڵ���Ϣ
	    	 gridListLayout.setBackgroundResource(R.drawable.vol_next);
	    	 volScoreText.setText("12��10��");
	    	 volNumberText.setText(this.context.getResources().getText(R.string.next_vol_text));
	     }
			
		
		
			return view;
	}
}
