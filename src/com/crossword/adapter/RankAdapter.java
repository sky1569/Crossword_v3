package com.crossword.adapter;

import java.util.LinkedList;
import java.util.zip.Inflater;

import com.crossword.R;
import com.crossword.data.Rank;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RankAdapter extends BaseAdapter{

	private Context context;
	private LinkedList<Rank> rankedList;
	private int              length;
	private LayoutInflater         inflater;
	private TextView               titleRankText;//用以使得listview中的字与标题对其
	private TextView               titleUsernameText;
	private TextView               titleAccumulatePointText;
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
		View view = new View(context);
		view = inflater.inflate(R.layout.rank_list_item, null);
		ImageView crownImageView = (ImageView) view.findViewById(R.id.crown_image_view);
		TextView  rankText = (TextView)view.findViewById(R.id.rank_item_text);
		TextView  usernameText = (TextView)view.findViewById(R.id.username_item_text);
		TextView  accumulatePointText = (TextView)view.findViewById(R.id.accumulate_point_item_text);
		/*alignCenterToTitle(titleRankText,rankText);
		alignCenterToTitle(titleUsernameText,usernameText);
		alignCenterToTitle(titleAccumulatePointText,accumulatePointText);*/
		if(position == 0){
			crownImageView.setImageResource(R.drawable.king_icon_1x);
		}
		Rank rank = rankedList.get(position);
		rankText.setText(""+rank.getRank());
		usernameText.setText(rank.getUserId());
		accumulatePointText.setText(""+rank.getAccumulatePoint());
		return view;
	}
	
	

	public void setTitleTextView(TextView v1,TextView v2,TextView v3){
		
		this.titleRankText = v1;
		this.titleUsernameText = v2;
		this.titleAccumulatePointText = v3;
	}
	
	
	public void alignCenterToTitle(TextView titleText,TextView itemText){
		
		LinearLayout.LayoutParams lpTitle = (LinearLayout.LayoutParams) titleText.getLayoutParams();
		int x = titleText.getLeft() + lpTitle.width;
		LinearLayout.LayoutParams lpItem = (android.widget.LinearLayout.LayoutParams) itemText.getLayoutParams();
		int left = x - lpItem.width/2;
		Log.v("xxxx", ""+left);
		itemText.setPadding(left, 0, 0, 0);
	}

}
