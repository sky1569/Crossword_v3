package com.crossword.adapter;

import java.util.LinkedList;
import java.util.zip.Inflater;

import com.crossword.R;
import com.crossword.data.Rank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RankAdapter extends BaseAdapter{

	private Context context;
	private LinkedList<Rank> rankedList;
	private int              length;
	private LayoutInflater         inflater;
	public RankAdapter(Context context,LinkedList<Rank> rankedList){
		
		this.context = context;
		this.rankedList = rankedList;
		this.length = rankedList.size();
		this.inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.length;
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
	public View getView(int position, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.rank_list_item, null);
		ImageView crownImageView = (ImageView) view.findViewById(R.id.crown_image_view);
		TextView  rankText = (TextView)view.findViewById(R.id.rank_item_text);
		TextView  usernameText = (TextView)view.findViewById(R.id.username_item_text);
		TextView  accumulatePointText = (TextView)view.findViewById(R.id.accumulate_point_item_text);
		if(position == 0){
			crownImageView.setImageResource(R.drawable.king_icon_1x);
		}
		Rank rank = rankedList.get(position);
		rankText.setText(rank.getRank());
		usernameText.setText(rank.getUserId());
		accumulatePointText.setText(rank.getAccumulatePoint());
		return view;
	}

}
