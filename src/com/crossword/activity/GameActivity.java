

package com.crossword.activity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import android.R.array;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import com.crossword.Crossword;
import com.crossword.R;
import com.crossword.adapter.GameGridAdapter;
import com.crossword.data.Character;
import com.crossword.data.Grid;
import com.crossword.data.Description;
import com.crossword.logic.BoardLogic;
import com.crossword.logic.MyTimerTask;
import com.crossword.utils.Module;
import com.crossword.view.MyGridView;



public class GameActivity extends Activity implements OnTouchListener {



	private FrameLayout 	girdFrameLayout;
	private ScrollView      gridScrollView;
	private MyGridView 		gridView;
	private GameGridAdapter gridAdapter;
	private Module			module;
	private TextView        txtDescriptionHor;
	private TextView        txtDescriptionVer;
	private Grid			grid;
	private LinkedList<Character> entities;
	private ArrayList<Integer>	index;
	private ArrayList<View>	selectedArea = new ArrayList<View>(); // 

	private boolean			downIsPlayable;	//
	private int 			downPos;		// 
	private int 			downX;			// 
	private int 			downY;			// 
	private int				lastX;          //上一次按下的位置X
	private int             lastY;          //上一次按下的位置Y
	private int             clickIndex = 1;
	private int 			currentX = -1;		// 
	private int 			currentY = -1;		// 
	private Character 		currentC;
	private boolean 		solidSelection;	//

	private int 			width;
	private int 			height;
	private ImageButton     returnButton;
	private BoardLogic 	boardLogic;
	@Override
	public void onPause()
	{
		this.boardLogic.save2(this.gridAdapter,this.grid);
		super.onPause();
	}

	@Override
	public void onStop(){
		this.boardLogic.scoring();
		this.boardLogic.save2(this.gridAdapter,this.grid);
		super.onStop();
	}
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		Intent intent2 = getIntent();
		Bundle bundle2 = intent2.getExtras();
		Grid currentGrid=(Grid)bundle2.getSerializable("currentGrid"); 


		this.module = new Module(this);
		this.boardLogic = new BoardLogic(this);
		//通过uniqueid查找grid，如果没有就会从网页下载
		//获取mainGameLayout，并设置监听
	
	
		//获取girdFrameLayout
		
		girdFrameLayout = (FrameLayout)findViewById(R.id.girdFrameLayout);
		gridScrollView = (ScrollView)findViewById(R.id.gridScrollView);
		returnButton = (ImageButton)findViewById(R.id.game_return_button);
		returnButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				GameActivity.this.finish();
			}

		});
		this.grid =this.module.queryGridByUniqueid(currentGrid.getVol(),currentGrid.getLevel(),currentGrid.getUniqueid()==null?-1:currentGrid.getUniqueid());

		this.grid.setGameMode(currentGrid.getGameMode());
		if (this.grid == null) {
			Log.v("this.grid", "null");
			finish();
			return;
		}


		this.entities = this.grid.getCharacters();

		if (this.entities == null) {
			Log.v("this.entities2", "null");
			finish();
			return;
		}
		this.boardLogic.initModule(this.grid);
		Log.v("initMoudle..this.entities", ""+ this.entities.size());
		this.width = this.grid.getWidth();
		this.height = this.grid.getHeight();
		this.lastX = -1;
		this.lastY = -1;
		Display display = getWindowManager().getDefaultDisplay();
		this.txtDescriptionHor = (TextView)findViewById(R.id.description_horizotal);
		this.txtDescriptionVer = (TextView)findViewById(R.id.description_vertical);

		this.txtDescriptionVer.setClickable(true);
		this.txtDescriptionVer.setFocusable(true);
		this.txtDescriptionVer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Log.v("currentX != -1 && currentY != -1", ""+currentX+currentY);

				boolean flag1 = currentX != -1 && currentY != -1 && boardLogic.isBlock(currentX, currentY)==false;
				boolean flag2 =	currentX != -1;
				boolean flag3 =	currentX != -1 && currentY != -1;
				boolean flag4 =	currentY != -1 && boardLogic.isBlock(currentX, currentY)==false;
				if(currentX != -1 && currentY != -1 && boardLogic.isBlock(currentX, currentY)==false)
				{
					for(Description des : grid.getDescriptions())
					{
						if(des.getTo() == index.get(0))
						{
							des.setDescSta(2);
							txtDescriptionVer.setText("二级提示："+des.getDesc2());

						}
					}
					boardLogic.isHint();
				}

			}
		});


		this.gridView = (MyGridView)findViewById(R.id.grid);
		this.gridView.setOnTouchListener(this);
		this.gridView.setNumColumns(this.width);
		this.gridView.setHandler(handler);


		android.view.ViewGroup.LayoutParams gridParams = this.gridView.getLayoutParams();

		this.gridView.setLayoutParams(gridParams);
		this.gridAdapter = new GameGridAdapter(this, this.entities, this.width, this.height,this.boardLogic);

		this.gridView.setAdapter(this.gridAdapter);


		//画标尺
		this.gridAdapter.drawRuler(this.girdFrameLayout);

	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{

		switch (event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
		{
			int firstVP =this.gridView.getFirstVisiblePosition();
			int position = this.gridView.pointToPosition((int)event.getX(), (int)event.getY())-firstVP;
			if(this.gridView.pointToPosition((int)event.getX(), (int)event.getY()) ==- 1)  break;
            
			TextView child = (TextView) this.gridView.getChildAt(position);



			if (child == null || child.getTag().equals(Crossword.AREA_BLOCK)) {
				if (this.solidSelection == false) {
					clearSelection();
					this.gridAdapter.notifyDataSetChanged();
				}
				this.downIsPlayable = false;
				return true;
			}
			this.downIsPlayable = true;

			this.downPos = position;
			this.downX = this.downPos % this.width;
			this.downY = this.downPos / this.width;
			clearSelection();
			this.gridAdapter.notifyDataSetChanged();
			break;
		}
		case  MotionEvent.ACTION_UP:
		{
			if (this.downIsPlayable == false)
			{
				currentC = null;
				this.currentX = -1;
				this.currentY = -1;
				setDescription(currentC, 0);
				return true;
			}
			int position = this.gridView.pointToPosition((int)event.getX(), (int)event.getY());

			int x = position % this.width;
			int y = position / this.width;

			TextView child = (TextView) gridView.getChildAt(position) ;
			if(child == null)
				return false;
			
			InputMethodManager inputMethodManager = (InputMethodManager)
					getSystemService(Context.INPUT_METHOD_SERVICE);

			
			if(child.getTag().equals(Crossword.AREA_BLOCK)){//点击灰色格子的时候隐藏键盘
				inputMethodManager.hideSoftInputFromWindow(gridView.getWindowToken(), 0);
 
				txtDescriptionHor.setText("一级提示：");
			}else{//其他情况打开键盘

			
				inputMethodManager.showSoftInput(v, 0);
				
	            new Thread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							Thread.sleep(300);
							handler.sendEmptyMessage(0x444);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
	            	
	            }).start();
	
			}
				

          
			if(x < 0 || x >= this.width || y < 0 || y>= this.height)
				return false;
			if(this.boardLogic.isBlock(x, y) )
				return false;


			currentC = this.boardLogic.getCharacterByPosition(x, y);
			if(currentC == null) Log.v("currentC is","null");
			if(this.lastX == x && this.lastY == y && clickIndex < currentC.getIndexList().size() ) clickIndex = clickIndex + 1;
			else clickIndex = 1 ;
			index =currentC.getIndexList().get(clickIndex-1);
			this.lastX = x;//获取上一次的横向位置
			this.lastY = y;//获取上一次的纵向位置
			this.currentX = x;
			this.currentY = y;


			this.gridAdapter.reDrawGridBackground(this.gridView);
			this.setWordBackground(currentC,index.get(0),this.currentX, this.currentY);
			this.gridAdapter.notifyDataSetChanged();

			this.setDescription(currentC,index.get(0));
			break;
		}

		}
		return true;

	}

	private void clearSelection() {
		for (View selected: selectedArea)
			selected.setBackgroundResource(R.drawable.area_empty);
		selectedArea.clear();
	}







	public void setWordBackground(Character currentC,int clickIndex ,int currX, int currY)
	{
		int currIndex = currY*this.width + currX-this.gridView.getFirstVisiblePosition();
		for(Character cc :this.entities)
		{



			for(ArrayList<Integer> arr : cc.getIndexList())
			{
				if(arr.get(0) == clickIndex)
				{
					int index = cc.getY()*this.width +cc.getX() - this.gridView.getFirstVisiblePosition();
					View currentChild = this.gridView.getChildAt(index);
					if(currentChild==null) continue;
					if(!currentChild .equals( Crossword.BLOCK))
					{
						currentChild.setBackgroundResource(index == currIndex?R.color.current_selected_color:R.color.selected_area_color);
						selectedArea.add(currentChild);
					}
				}
			}
		}
	}



	//设置描述信息


	public void setDescription(Character c ,int clickIndex)
	{
		if(c != null)
		{

			for(Description des : this.grid.getDescriptions())
			{

				if(des.getTo() == clickIndex)
				{
					if(des.getDescSta() == 1) 
					{
						this.txtDescriptionHor.setText("一级提示："+des.getDesc1());
						this.txtDescriptionVer.setText("点击显示二级提示");
					}
					if(des.getDescSta() == 2)
					{ 
						this.txtDescriptionHor.setText("一级提示："+des.getDesc1());
						this.txtDescriptionVer.setText("二级提示："+des.getDesc2());
					}

				}
			
			}
		}
		else 
		{
			this.txtDescriptionHor.setText("一级提示：");
			this.txtDescriptionVer.setText("点击显示二级提示");
		}
	}
	public void unlockNext()
	{
		if(this.grid.getGameMode() == Crossword.GAMEMODEVOL||this.grid.getGameMode() == Crossword.GAMEMODELIVE)
			this.boardLogic.unlock();
		else if(this.grid.getGameMode() == Crossword.GAMEMODEBREAK)
		{

		}
	} 


	
	

	//负责消息的接收与处理
	Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{

			switch(msg.what){
			//定时消息的处理
			case 0x123:
				try{
					String s=msg.obj.toString().split(Crossword.UNFILLED)[0];
					Log.v("weizhi", msg.obj.toString().split(Crossword.UNFILLED)[0]+"....."+msg.obj.toString().split(Crossword.UNFILLED)[1]);
					int x = Integer.parseInt(msg.obj.toString().split(Crossword.UNFILLED)[0]);
					int y = Integer.parseInt(msg.obj.toString().split(Crossword.UNFILLED)[1]);
					boardLogic.setArea(x, y, Crossword.UNFILLED);
					boardLogic.setDisValue(x, y, Crossword.UNFILLED);
				}
				catch (Exception e) 
				{

					Log.v("weizhi", msg.obj.toString());
					// TODO: handle exception
				}

				gridAdapter.notifyDataSetChanged();
				break;


				//负责输入提交后的消息处理	
			case 0x222:

				String value = gridView.getSoftInputText();
				value = value.toUpperCase().substring(0, 1);//防止输入多个字符，若输入多个字符，只取第一个字符

				if(currentC == null)
					return;
				int x = currentX;
				int y = currentY;

				if (boardLogic.isBlock(x, y))
					return;
				if (boardLogic.getArea(x, y).equals(Crossword.UNFILLEDABLE))
				{
					return;
				}
				if (value.equals(Crossword.UNFILLED)) 

				{
					boardLogic.replay();
					for(Description des :grid.getDescriptions())
					{
						des.setDescSta(1);
					}
					setDescription(currentC, 1);
					gridAdapter.notifyDataSetChanged();
					return;
				}


				boardLogic.setDisValue(x, y,value);
				gridAdapter.notifyDataSetChanged();



				boardLogic.toChinese2(x, y, value,GameActivity.this);
				if(boardLogic.getArea(x, y).equals(Crossword.UNFILLEDABLE))
				{
					String p = x+Crossword.UNFILLED+y;
					new Thread(new MyTimerTask(p,handler)).start();
				}

				if(boardLogic.isComplete(GameActivity.this)) 
				{

					unlockNext();
					return;


				}

				if(!value.equals(Crossword.UNFILLED))
				{

					Character cNext =  boardLogic.getCharacterByIndex(index.get(0),index.get(1)+1);
					if(cNext == null)
						return ;
					for(ArrayList< Integer> arr :cNext.getIndexList())
						if(arr.get(0) == index.get(0))
							index =arr;




					
					if (cNext.getX() >= 0 && cNext.getX() < width
							&& cNext.getY() >= 0 && cNext.getY() < height
							&& boardLogic.isBlock(cNext.getX(),cNext.getY()) == false)
					{
						currentX = cNext.getX();
						currentY = cNext.getY();
					}
				}


				currentC = boardLogic.getCharacterByPosition(currentX, currentY);
				gridAdapter.reDrawGridBackground(gridView);
				setWordBackground(currentC,index.get(0),currentX, currentY);

				gridAdapter.notifyDataSetChanged();


				setDescription(currentC, index.get(0));
				gridAdapter.notifyDataSetChanged();
				
				handler.sendEmptyMessage(0x444);
				break;
				
				 
			case 0x444:
				int position = currentC.getY()*width + currentC.getX();
				gridAdapter.ScrollToItem(gridScrollView, gridView.getChildAt(position));
			
				break;
				
			case Crossword.FINISH_GAME:
				GameActivity.this.finish();
				break;

			}

		}
	};



}
