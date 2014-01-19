package com.crossword.logic;

import java.util.LinkedList;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.crossword.Crossword;
import com.crossword.adapter.GameGridAdapter;
import com.crossword.data.Grid;
import com.crossword.data.Vol;
import com.crossword.data.Word;
import com.crossword.utils.DBManager;
import com.crossword.utils.JsonUtil;
import com.crossword.utils.Module;
import com.crossword.utils.UserUtil;

public class BoardLogic {
	 private Grid   grid;                   //从json中解析出grid的所有信息，包括关卡的信息以及所有的Word
	 private Vol    currVol;                    //当前期
	 private LinkedList<Word> entries  =  new LinkedList<Word>();
	 private JsonUtil jsonUtil;
	 private DBManager dbManager;
	 //private Module module;
	 private  int width;
	 private  int height;
	 private  int starCount;
	 private String[][]			area;			// Tableau repr茅sentant les lettres du joueur
	 private String[][] 		displayArea;
	 private String[][][] 		correctionAreaCap; // Tableau repr茅sentant les lettres correctes
	 private String[][]			correctionAreaChi;
     private Context            context;
     private int 				score;
     private int				filled;
     private int 				empty;
 	 private int 			hintCount ;
 	 private int 			corCount ;
 	 private int 			errCount ;
          
 	 
     public BoardLogic(Context context){
    	 this.context = context;
    	// module = new Module(this.context);
    	 this.dbManager = new DBManager(context);
    	 this.jsonUtil = new JsonUtil(context);
     }
     

	 
   //初始化Module中的width,height,area等
   	 public void initModule(Grid grid){
   		 
   		 this.grid = grid;
   		 this.height = grid.getHeight(); 
   		 this.width = grid.getWidth(); 
   		// Log.v("initMoudle..grid.getwidth", ""+this.width);
   		 this.area = new String[this.height][this.width];
   	     this.correctionAreaCap = new String[2][this.height][this.width];
   	     this.displayArea = new String[this.height][this.width];
   	     this.correctionAreaChi = new String[this.height][this.width];
   	     this.entries = grid.getEntries();
   	     for(int i = 0;i < this.width;i++)
   	    	 for(int j = 0;j < this.height;j++)
   	    	 {
   	    		 this.area[j][i]=Crossword.BLOCK;
   	    		 this.displayArea[j][i]=Crossword.BLOCK;
   	    		 this.correctionAreaChi[j][i]= Crossword.BLOCK;
   	    		 this.correctionAreaCap[0][j][i] = Crossword.BLOCK;
   	    		 this.correctionAreaCap[1][j][i] = Crossword.BLOCK;
   	    	 }
   	    this.resetScore();
   	 }
   	 
   	
   	  
   	 public boolean isCellCorrect(String value,int x,int y)
   	 {
   		
   		 if(value.getBytes().length!=value.length())
   		{
   			
   			if(value.equals(this.correctionAreaChi[y][x]))
   			{
   			    this.setArea(x, y, Crossword.CORRECTFILLED);
   				return true;
   			}
   		}
   		else
   		{
   			
   			if(value.equals(this.correctionAreaCap[0][y][x])|value.equals(this.correctionAreaCap[1][y][x]))
   			{
   				this.setArea(x, y, Crossword.CORRECTFILLED);
   			    return true;
   			}
   					
   		
   		}
   		// Log.v("testgetArea",""+this.getArea(x, y).equals(Crossword.WRONGFILLED));
   		 if(this.getArea(x, y).equals(Crossword.WRONGFILLED)) 
   		 
   		 {
   			 this.setArea(x,y,Crossword.UNFILLEDABLE);
   			 
   			 
   			// this.offcell();
   		 }
   		 
   		else this.setArea(x, y, Crossword.WRONGFILLED);
   		 
   		 return false;
   	 }
   	  
   	 public void replay() //记得调用重绘,暂时没用上
   	  {
   		  for(int i = 0;i < this.width;i++)
   			  for(int j = 0;j < this.height;j++)
   			  {
   				  if(!this.isBlock(i,j))
   					 // continue;
   				//  else 
   					  {
   					  		//this.setDisValue(i, j, Crossword.UNFILLED);
   					  		//this.setValue(i, j, Crossword.UNFILLED);
   					  this.area[j][i] = Crossword.UNFILLED;
   					  this.displayArea[j][i] = Crossword.UNFILLED;
   			  }
   		  
   		  this.resetScore();
   		  }
   	  }

   		
   		
   	 
   		public void initentries()
   		{
   			
   			for (Word entry: this.entries)
   		    {
   		    	String tmp = entry.getTmp();
   		    	String text = entry.getCap();
   		    	
   		    	boolean horizontal = entry.getHoriz();
   		    	int x = entry.getX();
   		    	int y = entry.getY();
   		    	//System.out.println(tmp);
   		    //	Log.v("initentriesforentry", "Ok?");
   		    	for (int i = 0 ; i < entry.getLength(); i++) 
   		    	{
   		    		//Log.v("entry.getLength()", "Ok?");
   		    		if (horizontal)
   		    		{//  Log.v("this.height...", ""+this.height);
   		    			if (y >= 0 && y < this.height && x+i >= 0 && x+i < this.width)
   		    			{
   		    				//Log.v("this.height", ""+this.height);
   		    				//this.area[y][x+i] = String.valueOf(tmp.charAt(i)).equals(Crossword.UNFILLED)?Crossword.UNFILLED:String.valueOf(tmp.charAt(i));
   		    				this.displayArea[y][x+i] = String.valueOf(tmp.charAt(i)).equals(Crossword.UNFILLED)?Crossword.UNFILLED:String.valueOf(tmp.charAt(i));
   		    				this.correctionAreaCap[0][y][x+i] = String.valueOf(text.charAt(i));
   		    				this.correctionAreaChi[y][x+i] = String.valueOf(entry.getAns(i));
   		    				if( !String.valueOf(tmp.charAt(i)).equals(Crossword.UNFILLED))
   		    				{
   		    					if(this.displayArea[y][x+i].equals(this.correctionAreaChi[y][x+i])|this.displayArea[y][x+i].equals(this.correctionAreaCap[0][y][x+i])) 
   		    						this.area[y][x+i] = Crossword.CORRECTFILLED;
   		    					else this.area[y][x+i] = Crossword.WRONGFILLED;		    					
   		    				}
   		    				else this.area[y][x+i] = Crossword.UNFILLED;
   		    			}
   		    		}
   		    		else
   		    		{
   		    			if (y+i >= 0 && y+i < this.height && x >= 0 && x < this.width)
   		    			{
   		    			//	this.area[y+i][x] =  String.valueOf(tmp.charAt(i)).equals(Crossword.UNFILLED)?Crossword.UNFILLED:String.valueOf(tmp.charAt(i));
   		    				this.displayArea[y+i][x] = String.valueOf(tmp.charAt(i)).equals(Crossword.UNFILLED)?Crossword.UNFILLED:String.valueOf(tmp.charAt(i));
   		    				this.correctionAreaChi[y+i][x] = String.valueOf(entry.getAns(i));
   		    				if(this.correctionAreaCap[0][y+i][x].equals(Crossword.BLOCK))
   		    					this.correctionAreaCap[0][y+i][x] = String.valueOf(text.charAt(i));
   		    				else  this.correctionAreaCap[1][y+i][x] = String.valueOf(text.charAt(i));
   		    			}
   		    			if( !String.valueOf(tmp.charAt(i)).equals(Crossword.UNFILLED))
   	    				{
   	    					if(this.displayArea[y+i][x].equals(this.correctionAreaChi[y+i][x])|this.displayArea[y+i][x].equals(this.correctionAreaCap[0][y+i][x])|this.displayArea[y+i][x].equals(this.correctionAreaCap[1][y+i][x])) 
   	    						this.area[y+i][x] = Crossword.CORRECTFILLED;
   	    					else this.area[y+i][x] = Crossword.WRONGFILLED;		    					
   	    				}
   	    				else this.area[y+i][x] = Crossword.UNFILLED;
   		    		}
   		    	}
   		    	
   		   }
   	
   		}
   			
   	  public void toChinese(int currentX,int currentY,String value)//按字翻转
   			{
   		  		
   		  		if(this.isCellCorrect(value, currentX, currentY))
   				{
   					this.setDisValue(currentX, currentY, this.correctionAreaChi[currentY][currentX]);
   				}
   			   	
   		  	 
   			}
   	  public void toChinese(int currentX,int currentY,Word currentWord,String value)	//按词翻转
   	  {
   		  //Log.v("testto chinese",""+value+"  "+this.isCellCorrect(value, currentX, currentY) );
   		  if(this.isCellCorrect(value, currentX, currentY))
   		  { 
   			  Log.v("testto chinese2",""+this.isWordComplete(currentX, currentY, currentWord.getHoriz()));
   			  Log.v("testto chinese3",""+this.isCross(currentX, currentY));
   			  Log.v("testto chinese4",""+this.isWordComplete(currentX, currentY, !currentWord.getHoriz()));
   			  if(this.isWordComplete(currentX, currentY, currentWord.getHoriz()))
   			  {
   				  for(int l = 0; l < currentWord.getLength(); l++)
      				  {
      					if(currentWord.getHoriz())  
      						{
      							Log.v("testto1",""+currentWord.getHoriz()+currentWord.getAns(l));
      						
      							this.displayArea[currentWord.getY()][currentWord.getX()+l]=currentWord.getAns(l);
      						}
      					       						
           		    if(!currentWord.getHoriz())
           		    	{
           		    		Log.v("testto2",""+!currentWord.getHoriz()+currentWord.getAns(l));
           		    	
           		    		this.displayArea[currentWord.getY()+l][currentWord.getX()]=currentWord.getAns(l);
           		    	}
      		            		   
      				  }
   			  }
   		      if(this.isCross(currentX, currentY))
   				  if(this.isWordComplete(currentX, currentY, !currentWord.getHoriz()))
   		   		  {
   					   for(int l=0;l<this.getCorrectWord(currentX,currentY,!currentWord.getHoriz()).getLength();l++)
   					   {
   						   if(currentWord.getHoriz())
   							   this.displayArea[this.getCorrectWord(currentX,currentY,!currentWord.getHoriz()).getY()+l][this.getCorrectWord(currentX,currentY,!currentWord.getHoriz()).getX()]
   								=this.correctionAreaChi[this.getCorrectWord(currentX,currentY,!currentWord.getHoriz()).getY()+l][this.getCorrectWord(currentX,currentY,!currentWord.getHoriz()).getX()];
   						   if(!currentWord.getHoriz())
   							   this.displayArea[this.getCorrectWord(currentX,currentY,!currentWord.getHoriz()).getY()][this.getCorrectWord(currentX,currentY,!currentWord.getHoriz()).getX()+l]
   										=this.correctionAreaChi[this.getCorrectWord(currentX,currentY,!currentWord.getHoriz()).getY()][this.getCorrectWord(currentX,currentY,!currentWord.getHoriz()).getX()+l];
   					   }
   		   		  }
   		  }
   	  }		
   	 public void setArea(int x ,int y ,String value)
   	 {
   		 if (!this.area[y][x].equals(Crossword.BLOCK)&&!this.area[y][x].equals(Crossword.CORRECTFILLED))
   			 this.area[y][x] = value;
   	 }
   	  
   	 public String getArea(int x,int y )
   	 {
   		 if(this.isBlock(x, y)) return Crossword.BLOCK;
   		 
   		 return this.area[y][x];
   	 }
   	  public void setDisValue(int x, int y, String value) {
   			if (!this.area[y][x].equals(Crossword.BLOCK)&&!this.area[y][x].equals(Crossword.CORRECTFILLED)&&!this.area[y][x].equals(Crossword.UNFILLEDABLE))
   			
   				{
   					this.displayArea[y][x] = value;
   			//	System.out.println(this.displayArea[y][x]);
   				}
   		}
   		
   			
   		
   		public String getdisplayAreaValue(int x,int y)
   		
   		{
   			if(this.isBlock(x, y)) return Crossword.BLOCK;
   			
   			return this.displayArea[y][x];
   		}
   		
   		 public boolean isBlock(int x, int y)
   		 {
   				return (this.correctionAreaChi[y][x].equals(Crossword.BLOCK));
   		 }
   		
   		
   		
   	   	 public boolean isChinese(int x,int y)
   	     {
   		 		
   			return  this.displayArea[y][x].getBytes().length == this.displayArea[y][x].length()?false:true;
   		 }
   		
   		public boolean isComplete(Activity act) {
   			this.filled = 0;
   			this.empty = 0;
   			
   			for (int y = 0; y < this.height; y++)
   				for (int x = 0; x < this.width; x++)
   					if (!this.area[y][x].equals(Crossword.BLOCK) ) {
   						if (this.area[y][x].equals(Crossword.CORRECTFILLED))
   							this.filled++;
   						else
   							this.empty++;
   					}
   			
   			if(this.filled==this.empty+this.filled) 
   			{
   				Toast toast=Toast.makeText(act, Crossword.COMPLETETIP, Toast.LENGTH_SHORT);
   				toast.setGravity(Gravity.CENTER, 0, 0);
   				toast.show();
   				return true;
   				}
   			return false;//return filled * 100 / (empty + filled);
   		}
   		
   		//以下计分功能
   		public boolean isWordComplete(int x ,int y,boolean h)
   		{
   			
   			Word corw = this.getCorrectWord(x, y, h);
   			String curw = this.getWordStatus(corw.getX(), corw.getY(),corw.getLength(), corw.getHoriz());
   			Log.v("teststatus", ""+curw);
   			if(curw.contains(Crossword.UNFILLED)||curw.contains(Crossword.WRONGFILLED)||curw.contains(Crossword.UNFILLEDABLE))   return false;	
   			
   			return true;
   		}
   		
   		
   		public String getWord(int x, int y, int length, boolean isHorizontal) {
   	    	StringBuffer word = new StringBuffer();
   	    	for (int i = 0; i < length; i++) {
   	    		if (isHorizontal) {
   	    			if (y < this.height && x+i < this.width)
   	    				word.append(this.area[y][x+i].equals(Crossword.UNFILLED)?Crossword.UNFILLED:this.displayArea[y][x+i]);
   	    		}
   	    		else {
   	    			if (y+i < this.height && x < this.width)
   	    				word.append(this.area[y+i][x].equals(Crossword.UNFILLED)?Crossword.UNFILLED:this.displayArea[y+i][x]);
   	    		}
   	    	}
   	    	return word.toString();
   		}
   		public String getWordStatus(int x, int y, int length, boolean isHorizontal)
   		{
   			StringBuffer wordStatus = new StringBuffer();
   			for (int i = 0; i < length; i++) {
   	    		if (isHorizontal) {
   	    			if (y < this.height && x+i < this.width)
   	    				wordStatus.append(this.area[y][x+i]);
   	    		}
   	    		else {
   	    			if (y+i < this.height && x < this.width)
   	    				wordStatus.append(this.area[y+i][x]);
   	    		}
   	    	}
   			return wordStatus.toString();
   		}
   		
   		 public Word getCorrectWord(int x, int y, boolean horizontal)
   		    {
   		        Word horizontalWord = null;
   		        Word verticalWord = null;
   			    for (Word entry: this.entries) {
   			    	if (x >= entry.getX() && x <= entry.getXMax())
   			    		if (y >= entry.getY() && y <= entry.getYMax()) {
   		        	    	if (entry.getHoriz())
   		        	    		        	    		
   		        	    			horizontalWord = entry;
   		        	    		
   		        	    	else
   		        	    		verticalWord = entry;
   			    		}
   			    }
   			    if (horizontal)
   			    {
   			    	//System.out.println((horizontalWord != null) ? horizontalWord : verticalWord);
   			    	return (horizontalWord != null) ? horizontalWord : verticalWord;}
   			    else
   			    	return (verticalWord != null) ? verticalWord : horizontalWord;
   		    }
   		
   		
   		 public int scoring(){
   			this.isCor();
   			this.score=this.corCount-this.hintCount-this.errCount;	
   			//this.score = 20;
   			this.grid.setScore(this.score);	
   	
   			//积分上传
   			UserUtil userUtil = new UserUtil();
   			userUtil.uploadGridScore(this.grid.getUniqueid(), this.grid.getScore());
   		   //上传离线积分
   			userUtil.uploadOfflineScore(300);
   		    return this.score;
   		 }
   		
		 
		 
		
		public boolean isCross(int x,int y){
			boolean c = false;
			String lD,rD,tD,bD;
			
	        lD = x - 1 < 0 ? Crossword.BLOCK:this.area[y][x - 1];
	        rD = x + 1 >= this.width?null:this.area[y][x + 1];
	        tD = y - 1 < 0 ? Crossword.BLOCK:this.area[y - 1][x];
	        bD = y + 1 >= this.height?Crossword.BLOCK:this.area[y + 1][x];
			
	        if((lD .equals(Crossword.BLOCK)  && rD .equals(Crossword.BLOCK)) || (tD .equals(Crossword.BLOCK) && bD.equals(Crossword.BLOCK))){
	        	c = false;
	        }else{
	        	c = true;
	        }
			return c;
		}
		
		
		//统分不对！！！
		public void isCor()
		{   
			this.corCount = 0;
			//if(this.isWordComplete(x, y)){}
			for (Word entry: this.entries)
		    {
		    	String tmp = entry.getTmp();
		    	String text = entry.getCap();
		    	boolean horizontal = entry.getHoriz();
		    	int x = entry.getX();
		    	int y = entry.getY();
		    	//System.out.println(tmp);
		    	for (int i = 0 ; i < entry.getLength(); i++) 
		    	{
		    		if (horizontal)
		    		{
		    			if (y >= 0 && y < this.height && x+i >= 0 && x+i < this.width)
		    			{
		    				//this.area[y][x+i] = String.valueOf(tmp.charAt(i)).equals(Crossword.UNFILLED)?Crossword.UNFILLED:String.valueOf(tmp.charAt(i));
		    				//this.displayArea[y][x+i] = String.valueOf(tmp.charAt(i)).equals(Crossword.UNFILLED)?Crossword.UNFILLED:String.valueOf(tmp.charAt(i));
		    				//this.correctionArea[y][x+i] = String.valueOf(text.charAt(i));
		    				if(this.isChinese(x+i, y)) this.corCount = this.corCount+Crossword.SCORE_PER_CHARACTER;
		    			}
		    		}
		    		else
		    		{
		    			if (y+i >= 0 && y+i < this.height && x >= 0 && x < this.width)
		    			{
		    				if(this.isChinese(x, y+i)) this.corCount = this.corCount+Crossword.SCORE_PER_CHARACTER;
		    				//this.area[y+i][x] =  String.valueOf(tmp.charAt(i)).equals(Crossword.UNFILLED)?Crossword.UNFILLED:String.valueOf(tmp.charAt(i));
		    				//this.displayArea[y+i][x] = String.valueOf(tmp.charAt(i)).equals(Crossword.UNFILLED)?Crossword.UNFILLED:String.valueOf(tmp.charAt(i));
		    				//this.correctionArea[y+i][x] = String.valueOf(text.charAt(i));
		    			}
		    		}
		    	}
		    	
		    }//this.corCount = Crossword.SCORE_PER_CHARACTER*l+this.corCount;
		 }
		public void isErr()
		{
			//if(this.isWordComplete(x, y)){}
			this.errCount = Crossword.WORD_ERROR_PENALTY+this.errCount;
		}
		
		public void isHint()
		{
			this.hintCount++;
		}
		public void resetScore()
		{
			this.corCount = 0;
			this.errCount = 0;
			this.hintCount = 0;
			this.score =0 ;
			this.starCount=0;
			
			}
		
		
		public boolean isFillWrong (int x ,int y)
		{
			
			return false;
		}
		
		
		
		public int star(int score)
		{
			 if(score > 0) starCount = score > 9 ? 2:1;
			 else starCount =0;
			 return starCount;
		}
		public void save(GameGridAdapter gridAdapter,Grid grid)
		{
			for(Word entry:grid.getEntries()){
				String word = this.getWord(entry.getX(), entry.getY(),entry.getLength(),entry.getHoriz());
				entry.setTmp(word);		
				//entry.set
				Log.v("测试写入json，gettem", entry.getTmp());
			}
			grid.setStar(this.star(score));
			grid.setIslocked(Crossword.GRIDUNLOCKED);
			JSONObject jObj = jsonUtil.writeToJson(grid);
			//用以保存数据的grid类，主要是保存在数据库中，增加了jsonData字段
			grid.setJsonData(jObj.toString());
			dbManager.updateGridData(grid);	
			
		}
		 public void unlock()
			{
				dbManager.unlockNext(this.grid.getVol(), this.grid.getLevel()+1);
			}
		 
		 
		 
		
   	}