

package com.crossword.activity;

import java.util.ArrayList;
import java.util.LinkedList;

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
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.TextView;

import com.crossword.Crossword;
import com.crossword.R;
import com.crossword.adapter.GameGridAdapter;
import com.crossword.data.Character;
import com.crossword.data.Grid;
import com.crossword.data.Word;
import com.crossword.data.Description;
import com.crossword.keyboard.KeyboardView;
import com.crossword.keyboard.KeyboardViewInterface;
import com.crossword.logic.BoardLogic;
import com.crossword.logic.TimerTask;
import com.crossword.utils.Module;
import com.crossword.view.KeyboardPopupWindow;
import com.crossword.view.MyGridView;


public class GameActivity extends Activity implements OnTouchListener {

	//public enum GRID_MODE {NORMAL, CHECK, SOLVE};
	//public  int CurrentMode;
	private FrameLayout     girdFrameLayout;
	private MyGridView 		gridView;
	private KeyboardView 	keyboardView;
	private GameGridAdapter gridAdapter;
	private TextView 		txtDescription;
	private Module			module;
	private TextView        txtDescriptionHor;
	private TextView        txtDescriptionVer;
	private TextView 		keyboardOverlay;
    private KeyboardPopupWindow keyboardPopupWindow;
	private Grid			grid;
	//private LinkedList<Word> entities;		// Liste des mots
	private LinkedList<Character> entities;
	private ArrayList<View>	selectedArea = new ArrayList<View>(); // Liste des cases selectionn茅es

	private boolean			downIsPlayable;	// false si le joueur 脿 appuy茅 sur une case noire 
	private int 			downPos;		// Position ou le joueur 脿 appuy茅
    private int 			downX;			// Ligne ou le joueur 脿 appuy茅
    private int 			downY;			// Colonne ou le joueur 脿 appuy茅
    private int				lastX;          //上一次按下的位置X
    private int             lastY;          //上一次按下的位置Y
    private int             clickIndex = 0;
	private int 			currentPos;		// Position actuelle du curseur
	private int 			currentX;		// Colonne actuelle du curseur
	private int 			currentY;		// Ligne actuelle du curseur
	//private Word			currentWord;	// Mot actuellement selectionn茅
	//private Word            currentWordHor;
	//private Word            currentWordVer;
	private Character 		currentC;
	private Description     des;
	private boolean 		horizontal;		// Sens de la selection
    private boolean 		isCross;        //判断是否是交叉点
	private String 			filename;		// Nom de la grille
    private String          url;            //下载地址
	private boolean 		solidSelection;	// PREFERENCES: Selection persistante
	private boolean			gridIsLower;	// PREFERENCES: Grille en minuscule
	
	private int 			width;
	private int 			height;
	private ImageButton     returnButton;
	private BoardLogic 	boardLogic;
    //Handler handler;

		@Override
public void onPause()
	{
		Log.v("ss", "ss");
		//this.boardLogic.scoring();
		this.boardLogic.save2(this.gridAdapter,this.grid);
		super.onPause();
	}
	
	@Override
	public void onStop(){
		Log.v("dd", "dd");
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
		System.out.println("this.currentGrid..."+currentGrid.getUniqueid());

		this.module = new Module(this);
		this.boardLogic = new BoardLogic(this);
	    //this.filename = "td.json";
	  //  this.url = Crossword.GRID_URL + 10002;
	  //  module.parseGrid(this, this.url);
	    //通过uniqueid查找grid，如果没有就会从网页下载
		//获取girdFrameLayout
		girdFrameLayout = (FrameLayout)findViewById(R.id.girdFrameLayout);
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
	    	finish();
	    	return;
	    }

	  //  this.entities= this.grid.getEntities();
	    this.entities = this.grid.getCharacters();
	    
	    if (this.entities == null) {
	    	 Log.v("this.entities", "null");
	    	finish();
	    	return;
	    }
	    this.boardLogic.initModule(this.grid);
	    Log.v("initMoudle..this.entities", ""+ this.entities.size());
	    this.width = this.grid.getWidth();
	    this.height = this.grid.getHeight();
        this.lastX = -1;
        this.lastY = -1;
       // this.completeFlag = false;
	    Display display = getWindowManager().getDefaultDisplay();
        int height = display.getHeight();
        int weight = display.getWidth();
        int keyboardHeight = (int)(height / 4.4);
        this.txtDescriptionHor = (TextView)findViewById(R.id.description_horizotal);
        this.txtDescriptionVer = (TextView)findViewById(R.id.description_vertical);
        
        this.txtDescriptionHor.setClickable(true);
        this.txtDescriptionHor.setFocusable(true);
        this.txtDescriptionHor.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				txtDescriptionHor.setTextColor(getResources().getColor(R.color.wrong));
			    boardLogic.isHint();
				
			}
		});
        
        
        this.gridView = (MyGridView)findViewById(R.id.grid);
        this.gridView.setOnTouchListener(this);
        this.gridView.setNumColumns(this.width);
        this.gridView.setHandler(handler);

        
       android.view.ViewGroup.LayoutParams gridParams = this.gridView.getLayoutParams();
      
       gridParams.height = (height - keyboardHeight - this.txtDescriptionHor.getLayoutParams().height*3);

        gridParams.width = weight;
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
            	//int position = this.gridView.pointToPosition((int)event.getX(), (int)event.getY());
            	int position = this.gridView.pointToPosition((int)event.getX(), (int)event.getY())-firstVP;
            	if(this.gridView.pointToPosition((int)event.getX(), (int)event.getY()) ==- 1)  break;

            	TextView child = (TextView) this.gridView.getChildAt(position);
                
               // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            	InputMethodManager inputMethodManager = (InputMethodManager)
	            		   getSystemService(Context.INPUT_METHOD_SERVICE);
                
                if(child.getTag().equals(Crossword.AREA_BLOCK)){//点击灰色格子的时候隐藏键盘
                	inputMethodManager.hideSoftInputFromWindow(gridView.getWindowToken(), 0);
                
                }else{//其他情况打开键盘
               	
                	inputMethodManager.showSoftInput(v, 0);
                }
	       
	         


            	if (child == null || child.getTag().equals(Crossword.AREA_BLOCK)) {
            		if (this.solidSelection == false) {
                        clearSelection();
                    	this.gridAdapter.notifyDataSetChanged();
            		}
            		this.downIsPlayable = false;
            		return true;
            	}
        		this.downIsPlayable = true;

            	// Stocke les coordonnees d'appuie sur l'ecran
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
            	         		return true;
            		int firstVP =this.gridView.getFirstVisiblePosition();
            		int position = this.gridView.pointToPosition((int)event.getX(), (int)event.getY());
           
            		int x = position % this.width;
            		int y = position / this.width;
            		if(x < 0 || x >= this.width || y < 0 || y>= this.height)
                    	return false;
            		
            		
            		
            	    
            		
                	 currentC = this.boardLogic.getCharacterByPosition(this.currentX, this.currentY);
                	if(this.lastX == x && this.lastY == y && clickIndex < currentC.getListLength() ) clickIndex = clickIndex + 1;
            		else clickIndex = 0 ;
                	this.lastX = x;//获取上一次的横向位置
                    this.lastY = y;//获取上一次的纵向位置
                    this.currentX = x;
                	this.currentY = y;
                	this.setWordBackground(currentC,clickIndex,x, y);
               
            		this.gridAdapter.reDrawGridBackground(this.gridView);
            		this.gridAdapter.notifyDataSetChanged();
            	
            	//	
            			
            		
           // 		this.setDescription(currentWordHor, currentWordVer, currentWord);
            		this.setDescription(currentC, clickIndex, 1);
            	    this.gridAdapter.notifyDataSetChanged();
            	    break;
                }
           /* case MotionEvent.ACTION_UP:
            {
            	// Si le joueur 脿 appuy茅 sur une case noire, aucun traitement 
            	if (this.downIsPlayable == false)
            		return true;
            	 int firstVP =this.gridView.getFirstVisiblePosition();
                int position = this.gridView.pointToPosition((int)event.getX(), (int)event.getY());
           
                int x = position % this.width;
                int y = position / this.width;
                //判断输入方向
                if(x < 0 || x >= this.width || y < 0 || y>= this.height)
                	return false;
                this.horizontal = (lastY == y && Math.abs(lastX - x)>0 || (lastX == -1 && lastY == -1))?true:false;
                this.horizontal = (lastX == x && Math.abs(lastY - y)>0)?false:true;
                this.lastX = x;//获取上一次的横向位置
                this.lastY = y;//获取上一次的纵向位置
                this.currentX = x;
            	this.currentY = y;
            	this.isCross = this.boardLogic.isCross(currentX, currentY);
            	//
            	
            	currentWord = this.boardLogic.getCorrectWord(currentX,currentY,this.horizontal);
               //	Log.v("h", currentWord.getCap());
        	    if (this.currentWord == null)
        	    	return true;
        	    this.horizontal = this.currentWord.getHoriz();
        	  //在设置背景之前先重绘一遍
        		this.gridAdapter.reDrawGridBackground(this.gridView);
        		this.gridAdapter.notifyDataSetChanged();
        		
                if(isCross){
                	
                	this.currentWordHor = this.boardLogic.getCorrectWord(x, y, true);
                	this.currentWordVer = this.boardLogic.getCorrectWord(x, y, false);
                	this.setWordBackground(this.currentWordHor, x, y);
                	this.setWordBackground(this.currentWordVer, x, y);
                }else{
                	this.setWordBackground(currentWord, x, y);
                }
                
                
                this.setDescription(currentWordHor, currentWordVer, currentWord);
        	    this.gridAdapter.notifyDataSetChanged();
        	    break;
            }*/
        }
        // if you return false, these actions will not be recorded
        return true;
	}
	
	// Remet les anciennes case selectionnees dans leur etat normal
    private void clearSelection() {
    	for (View selected: selectedArea)
    		selected.setBackgroundResource(R.drawable.area_empty);
    	selectedArea.clear();
	}
/*
	@Override
	public void onKeyDown(String value, int location[], int width) {
		System.out.println("onKeyDown: " + value + ", insert in: " + currentX + "x" + currentY);

		// Deplace l'overlay du clavier
		if (value.equals(Crossword.UNFILLED) == false) {
			int offsetX = (this.keyboardOverlay.getWidth() - width) / 2;
			int offsetY = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Crossword.KEYBOARD_OVERLAY_OFFSET, getResources().getDisplayMetrics());
			FrameLayout.LayoutParams lp = (LayoutParams)this.keyboardOverlay.getLayoutParams();
			lp.leftMargin = location[0] - offsetX;
			lp.topMargin = location[1] - offsetY;
			this.keyboardOverlay.setLayoutParams(lp);
			this.keyboardOverlay.setText(value);
			this.keyboardOverlay.clearAnimation();
			this.keyboardOverlay.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onKeyUp(String value) {
		System.out.println("onKeyUp: " + value + ", insert in: " + currentX + "x" + currentY);
		
		// Efface l'overlay du clavier
		if (value.equals(Crossword.UNFILLED) == false) {
			this.keyboardOverlay.setAnimation(AnimationUtils.loadAnimation(this, R.anim.keyboard_overlay_fade_out));
			this.keyboardOverlay.setVisibility(View.INVISIBLE);
		}

		// Si aucun mot selectionne, retour
		if (this.currentWord == null)
			return;

		// Case actuelle
		int x = this.currentX;
		int y = this.currentY;
        
		// Si la case est noire => retour
		if (this.boardLogic.isBlock(x, y))
			return;
		if (this.boardLogic.getArea(x, y).equals(Crossword.UNFILLEDABLE))
		{
			return;
		}
		if (value.equals(Crossword.UNFILLED)) 
			
		{
			 this.boardLogic.replay();
			 this.gridAdapter.notifyDataSetChanged();
			 return;
		}
		

	
		this.boardLogic.setDisValue(x, y,value);
		this.gridAdapter.notifyDataSetChanged();
			
		
		
		this.boardLogic.toChinese2(x,y, value);
	  // this.boardLogic.toChinese(x,y,this.currentWord,value);
		if(this.boardLogic.getArea(x, y).equals(Crossword.UNFILLEDABLE))
			{
				String p = x+Crossword.UNFILLED+y;
				new Thread(new TimerTask(p,handler)).start();
			}
		
	    if(this.boardLogic.isComplete(this)) 
		{
		    		

			
			this.boardLogic.scoring();
			 
			    	this.unlockNext();
	
			    	return;
		    	
			
		}

		if(!value.equals(Crossword.UNFILLED))
		{
			x = (this.horizontal ? x + 1 : x);
			y = (this.horizontal ? y: y + 1);
		}
		
		
		if (x >= 0 && x < this.width
				&& y >= 0 && y < this.height
				&& this.boardLogic.isBlock(x,y) == false) {
			this.currentX = x;
			this.currentY = y;
		}
		
		
		//this.isCross = this.boardLogic.isCross(currentX, currentY);
       
		currentWord = this.boardLogic.getCorrectWord(currentX,currentY,this.horizontal);
		  
          
        this.horizontal = this.currentWord.getHoriz();
        //在设置背景之前先重绘一遍
        this.gridAdapter.reDrawGridBackground(this.gridView);
        if(this.isCross){
        	
        	this.currentWordHor = this.boardLogic.getCorrectWord(currentX, currentY, true);
        	this.currentWordVer = this.boardLogic.getCorrectWord(currentX, currentY, false);
        	
        	this.setWordBackground(this.currentWordHor, currentX, currentY);
        	this.setWordBackground(this.currentWordVer, currentX, currentY);
        }else{
        	this.setWordBackground(currentWord, currentX, currentY);
        }
        
        
        this.setDescription(currentWordHor, currentWordVer, currentWord);
        
		
	}
	
	
	
*/
	

	
	
  
	/*
	//设置横向或纵向词对应的小格背景
	public void setWordBackground(Word word,int currX,int currY){
		
		int x = word.getX();
		int y = word.getY();
		boolean horizontal = word.getHoriz();
		int currIndex = currY*this.width + currX-this.gridView.getFirstVisiblePosition();
	
		for(int l = 0;l<word.getLength();l++){
			int index = y*this.width + x + l*(horizontal?1:this.width)-this.gridView.getFirstVisiblePosition();
			View currentChild = this.gridView.getChildAt(index);
			if(currentChild==null) continue;
			if(!currentChild .equals( Crossword.BLOCK)){
				//currentChild.setBackgroundResource(index == currIndex?R.drawable.area_current:R.drawable.area_selected);
				currentChild.setBackgroundResource(index == currIndex?R.color.current_selected_color:R.color.selected_area_color);
				//currentChild.setBackgroundResource(index == currIndex?R.drawable.current_selected_area_background:R.drawable.selected_area_background);
				selectedArea.add(currentChild);
			}
		}
	}
	*/
   public void setWordBackground(Character currentC,int clickIndex ,int currX, int currY)
   {
	   int currIndex = currY*this.width + currX-this.gridView.getFirstVisiblePosition();
	   for(Character cc :this.entities)
	   {
   		for(ArrayList<Integer> arr : cc.getIndexList())
   			{
   				if(arr.get(0) == currentC.getIndexList().get(clickIndex).get(0))
   				{
   					//this.setWordBackground(clickIndex,x, y);
   					int index = cc.getY()*this.width +cc.getX() - this.gridView.getFirstVisiblePosition();
   					View currentChild = this.gridView.getChildAt(index);
   					if(currentChild==null) continue;
   					if(!currentChild .equals( Crossword.BLOCK))
   					{
   						//currentChild.setBackgroundResource(index == currIndex?R.drawable.area_current:R.drawable.area_selected);
   						currentChild.setBackgroundResource(index == currIndex?R.color.current_selected_color:R.color.selected_area_color);
   						//currentChild.setBackgroundResource(index == currIndex?R.drawable.current_selected_area_background:R.drawable.selected_area_background);
   						selectedArea.add(currentChild);
   					}
   				}
   			}
	   }
	}
   
	
	
	//设置描述信息
	/*public void setDescription(Word currentWordHor,Word currentWordVer,Word currentWord){//设置提示信息
		  String descriptionHor = isCross?"横向:"+this.currentWordHor.getDesc():
              (this.horizontal?"横向:"+currentWord.getDesc():"横向:");
          String descriptionVer = isCross?"纵向:"+this.currentWordVer.getDesc():
              (this.horizontal?"纵向:":"纵向:"+currentWord.getDesc());

          this.txtDescriptionHor.setText(descriptionHor);
          this.txtDescriptionVer.setText(descriptionVer);
		 
	}*/
	public void setDescription(Character c ,int clickIndex,int desIndex)
	{
		 if(des.getTo() == c.getIndexList().get(clickIndex).get(0))
		 {
			 if(desIndex == 1) this.txtDescriptionHor.setText(des.getDesc1());
			 if(desIndex == 2) this.txtDescriptionHor.setText(des.getDesc2());
		 }
	}
	public void unlockNext()
	{
		//if(this.isComplete())
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
    				Log.v("sss", s);
    				Log.v("weizhi", msg.obj.toString().split(Crossword.UNFILLED)[0]+"....."+msg.obj.toString().split(Crossword.UNFILLED)[1]);
    				int x = Integer.parseInt(msg.obj.toString().split(Crossword.UNFILLED)[0]);
    				int y = Integer.parseInt(msg.obj.toString().split(Crossword.UNFILLED)[1]);
    				Log.v("weizhi", msg.obj.toString()+x+"..."+y);
    				boardLogic.setArea(x, y, Crossword.UNFILLED);
    				Log.v("weizhi", boardLogic.getArea(x, y));
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
    			value = value.toUpperCase();
    			
    		//	if (currentWord == null)
    		//		return;
    			if(currentC == null)
    				return;
    			// Case actuelle
    			int x = currentX;
    			int y = currentY;
    	        
    			// Si la case est noire => retour
    			if (boardLogic.isBlock(x, y))
    				return;
    			if (boardLogic.getArea(x, y).equals(Crossword.UNFILLEDABLE))
    			{
    				return;
    			}
    			if (value.equals(Crossword.UNFILLED)) 
    				
    			{
    				 boardLogic.replay();
    				 gridAdapter.notifyDataSetChanged();
    				 return;
    			}
    			

    			boardLogic.setDisValue(x, y,value);
    			gridAdapter.notifyDataSetChanged();
    				
    			
    			
    			boardLogic.toChinese2(x, y, value);
    		    //boardLogic.toChinese(x,y,currentWord,value);
    			if(boardLogic.getArea(x, y).equals(Crossword.UNFILLEDABLE))
    				{
    					String p = x+Crossword.UNFILLED+y;
    					new Thread(new TimerTask(p,handler)).start();
    				}
    			
    		    if(boardLogic.isComplete(GameActivity.this)) 
    			{

    				boardLogic.scoring();
    				    	unlockNext();
    				    	return;
    			    	
    				
    			}

    			if(!value.equals(Crossword.UNFILLED))
    			{
    				Character cNext =  boardLogic.getCharacterByIndex(currentC.getIndexList().get(clickIndex).get(0), currentC.getIndexList().get(clickIndex).get(1)+1);
    				 if(cNext == null)
    					 return ;
    				
    			
    			
    			// Si la case suivante est disponible, met la case en jaune, remet l'ancienne en bleu, et set la nouvelle position
    			if (cNext.getX() >= 0 && cNext.getX() < width
    					&& cNext.getY() >= 0 && cNext.getY() < height
    					&& boardLogic.isBlock(cNext.getX(),cNext.getY()) == false) {
    				currentX = cNext.getX();
    				currentY = cNext.getY();
    			}
    			}
    			
    		
    			currentC = boardLogic.getCharacterByPosition(currentX, currentY);
    			gridAdapter.reDrawGridBackground(gridView);
    			setWordBackground(currentC,clickIndex,x, y);
                
        		gridAdapter.reDrawGridBackground(gridView);
        		gridAdapter.notifyDataSetChanged();
        	
        	//	
        			
        		
       // 		this.setDescription(currentWordHor, currentWordVer, currentWord);
        		setDescription(currentC, clickIndex, 1);
        		gridAdapter.notifyDataSetChanged();
        	    break;
    			
    		}
    		
    	}
    };
	
	
	
}
