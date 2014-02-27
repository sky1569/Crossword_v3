

package com.crossword.activity;

import java.util.ArrayList;
import java.util.LinkedList;

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
	private ArrayList<Integer>	index;
	private ArrayList<View>	selectedArea = new ArrayList<View>(); // Liste des cases selectionnées

	private boolean			downIsPlayable;	// false si le joueur à appuyé sur une case noire 
	private int 			downPos;		// Position ou le joueur à appuyé
    private int 			downX;			// Ligne ou le joueur à appuyé
    private int 			downY;			// Colonne ou le joueur à appuyé
    private int				lastX;          //��һ�ΰ��µ�λ��X
    private int             lastY;          //��һ�ΰ��µ�λ��Y
    private int             clickIndex = 1;
	private int 			currentPos;		// Position actuelle du curseur
	private int 			currentX = -1;		// Colonne actuelle du curseur
	private int 			currentY = -1;		// Ligne actuelle du curseur
	//private Word			currentWord;	// Mot actuellement selectionné
	//private Word            currentWordHor;
	//private Word            currentWordVer;
	private Character 		currentC;
	//private Description     des;
	private boolean 		horizontal;		// Sens de la selection
    private boolean 		isCross;        //�ж��Ƿ��ǽ����
	private String 			filename;		// Nom de la grille
    private String          url;            //���ص�ַ
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
	    //ͨ��uniqueid����grid�����û�оͻ����ҳ����
		//��ȡgirdFrameLayout
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
	    	Log.v("this.grid", "null");
	    	finish();
	    	return;
	    }

	  //  this.entities= this.grid.getEntities();
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
       // this.completeFlag = false;
	    Display display = getWindowManager().getDefaultDisplay();
        int height = display.getHeight();
        int weight = display.getWidth();
        int keyboardHeight = (int)(height / 4.4);
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
  				Log.v("if", ""+flag1+".."+flag2+".."+flag3+".."+flag4 );
  				if(currentX != -1 && currentY != -1 && boardLogic.isBlock(currentX, currentY)==false)
  				{
  					Log.v("how", ""+flag1+".."+flag2+".."+flag3+".."+flag4 );
  					for(Description des : grid.getDescriptions())
  					{
  						if(des.getTo() == index.get(0))
  						{
  							des.setDescSta(2);
  							//setDescription(c, keyboardHeight)
  							txtDescriptionVer.setText("������ʾ��"+des.getDesc2());
  							
  						}
  					}
  					boardLogic.isHint();
  				}
  				//else
  					
  				
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
		
		
		//�����
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
                
                if(child.getTag().equals(Crossword.AREA_BLOCK)){//�����ɫ���ӵ�ʱ�����ؼ���
                	inputMethodManager.hideSoftInputFromWindow(gridView.getWindowToken(), 0);
                	//this.setDescription(c, position)
                	txtDescriptionHor.setText("һ����ʾ��");
                }else{//��������򿪼���
               	
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
            		{
            			currentC = null;
            			this.currentX = -1;
            			this.currentY = -1;
            			setDescription(currentC, 0);
            			return true;
            		}
            		int firstVP =this.gridView.getFirstVisiblePosition();
            		int position = this.gridView.pointToPosition((int)event.getX(), (int)event.getY());
           
            		int x = position % this.width;
            		int y = position / this.width;
            		 
            		Log.v("position", ""+x+"..."+y);
            		if(x < 0 || x >= this.width || y < 0 || y>= this.height)
                    	return false;
            		if(this.boardLogic.isBlock(x, y) )
            			return false;
            		          	    
            		
                	 currentC = this.boardLogic.getCharacterByPosition(x, y);
                	 if(currentC == null) Log.v("currentC is","null");
                	if(this.lastX == x && this.lastY == y && clickIndex < currentC.getIndexList().size() ) clickIndex = clickIndex + 1;
            		else clickIndex = 1 ;
                	//int index =currentC.getIndexList().get(clickIndex).get(0);
                	index =currentC.getIndexList().get(clickIndex-1);
                	this.lastX = x;//��ȡ��һ�εĺ���λ��
                    this.lastY = y;//��ȡ��һ�ε�����λ��
                    this.currentX = x;
                	this.currentY = y;
              
               
            		this.gridAdapter.reDrawGridBackground(this.gridView);
            		this.setWordBackground(currentC,index.get(0),this.currentX, this.currentY);
            		this.gridAdapter.notifyDataSetChanged();
            	
            	//	
            			
            		
           // 		this.setDescription(currentWordHor, currentWordVer, currentWord);
            		this.setDescription(currentC,index.get(0));
            	   // this.gridAdapter.notifyDataSetChanged();
            	    break;
                }
        
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

	

	
	
  
	
   public void setWordBackground(Character currentC,int clickIndex ,int currX, int currY)
   {
	   int currIndex = currY*this.width + currX-this.gridView.getFirstVisiblePosition();
	   for(Character cc :this.entities)
	   {
		   if(cc.getIndexList() != null) 
			   Log.v("cc.getIndexList()","!= null"+cc.getIndexList());
		   if(cc.getIndexList() == null)
			   Log.v("cc.getIndexList()","== null"+cc.getIndexList());
		   for(int i=0 ; i <currentC.getIndexList().size();i++)
			   Log.v("currentC.getIndexList()", ""+currentC.getIndexList());
   		for(ArrayList<Integer> arr : cc.getIndexList())
   			{
   				if(arr.get(0) == clickIndex)
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
   
	
	
	//����������Ϣ


	public void setDescription(Character c ,int clickIndex)
	{
		//Log.v("setD c",""+c.getChi()+"..."+clickIndex);
		if(c != null)
		{
			
			for(Description des : this.grid.getDescriptions())
			{
			
				Log.v(" c.getIndexList().get(clickIndex).get(0)", ""+ clickIndex+"..des.to"+des.getTo());
				if(des.getTo() == clickIndex)
				{
					if(des.getDescSta() == 1) 
					{
						this.txtDescriptionHor.setText("һ����ʾ��"+des.getDesc1());
						this.txtDescriptionVer.setText("�����ʾ������ʾ");
					}
					Log.v("��ǰ����һ����ʾ��", ""+ des.getDesc1());
					if(des.getDescSta() == 2)
					{ 
						this.txtDescriptionHor.setText("һ����ʾ��"+des.getDesc1());
						this.txtDescriptionVer.setText("������ʾ��"+des.getDesc2());
					}
					
				}
		//	break;
			}
		}
		else 
		{
			this.txtDescriptionHor.setText("һ����ʾ��");
			this.txtDescriptionVer.setText("�����ʾ������ʾ");
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
	
	
	
	//������Ϣ�Ľ����봦��
    Handler handler = new Handler()
    {
    	public void handleMessage(Message msg)
    	{
    		
    		switch(msg.what){
    		//��ʱ��Ϣ�Ĵ���
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
    			
    			
    		//���������ύ�����Ϣ����	
    		case 0x222:
    			
    			String value = gridView.getSoftInputText();
    			value = value.toUpperCase().substring(0, 1);//��ֹ�������ַ������������ַ���ֻȡ��һ���ַ�
    			
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
    		    //boardLogic.toChinese(x,y,currentWord,value);
    			if(boardLogic.getArea(x, y).equals(Crossword.UNFILLEDABLE))
    				{
    					String p = x+Crossword.UNFILLED+y;
    					new Thread(new TimerTask(p,handler)).start();
    				}
    			
    		    if(boardLogic.isComplete(GameActivity.this)) 
    			{

    				//boardLogic.scoring();
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
    			
    				
    			
    			
    			// Si la case suivante est disponible, met la case en jaune, remet l'ancienne en bleu, et set la nouvelle position
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
                
        		//gridAdapter.reDrawGridBackground(gridView);
        		gridAdapter.notifyDataSetChanged();
        	
        	//	
        			
        		
       // 		this.setDescription(currentWordHor, currentWordVer, currentWord);
        		setDescription(currentC, index.get(0));
        		gridAdapter.notifyDataSetChanged();
        	    break;
    			
    		}
    		
    	}
    };
	
	
	
}
