package com.crossword.utils;

import java.util.LinkedList;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.crossword.Crossword;
import com.crossword.adapter.GameGridAdapter;
import com.crossword.data.Grid;
import com.crossword.data.Word;

public class Module {
	 private Grid   grid;                   //从json中解析出grid的所有信息，包括关卡的信息以及所有的Word
	 private LinkedList<Word> entries  =  new LinkedList<Word>();
	 private JsonUtil jsonUtil;
	 private DBManager dbManager;
	 private int width;
	 private int height;

	 private String[][]			area;			// Tableau repr茅sentant les lettres du joueur
	 private String[][] 		displayArea;
	 private String[][] 		correctionArea; // Tableau repr茅sentant les lettres correctes
     private Context            context;
     
     public Module(Context context){
    	 this.context = context;
    	 this.dbManager = new DBManager(context);
    	 this.jsonUtil = new JsonUtil(context);
     }
	 
	 public Grid parseGridFromUrl(Context context,String url){
		 Grid grid = new Grid();
		 //jsonUtil = new JsonUtil(context);
		 //dbManager = new DBManager(context);
		// String jsonData = jsonUtil.readJsonDataFromFile(filename);
		// String jsonData = jsonUtil.readJsonDataFromAssets(filename);
		 String jsonData = jsonUtil.readJsonFromUrl(url);
		// System.out.println(jsonData);
		 grid =  jsonUtil.parseGridJson(jsonData);
		//解析完，将Json数据加入数据库中
		 dbManager.add(grid);
	     return grid;
	 }
	 
	//初始化Module中的width,height,area等
	 public void initModule(Grid grid){
		 
		 this.width = grid.getWidth(); 
		 this.height = grid.getHeight();
		 this.area = new String[this.height][this.width];
	     this.correctionArea = new String[this.height][this.width];
	     this.displayArea = new String[this.height][this.width];
	     this.entries = grid.getEntries();
	     for(int i = 0;i < this.width;i++)
	    	 for(int j = 0;j < this.height;j++)
	    	 {
	    		 this.area[j][i]=Crossword.BLOCK;
	    		 this.displayArea[j][i]=Crossword.BLOCK;
	    	 }
	 }
	 
	 public LinkedList<Word>  getEntry(){
		 this.entries = this.grid.getEntries();
		 return this.grid.getEntries();
	 }
	
	  public boolean isCorrect(Word correctWords,String currentWords,int x,int y)
	    {	    	
	    	int currentX=correctWords.getX();
	    	int currentY=correctWords.getY();
		 // if(this.isCross(x, y))
		 // if(this.isCross(x, y))
		 // {
			  boolean Horiz = correctWords.getHoriz();
			  for(int i = 0;i < correctWords.getLength(); i++)
	    	{
	    		if(currentWords.charAt(i) != correctWords.getCap().charAt(i))
	    		{
	    			
	    		currentX=Horiz ? correctWords.getX()+i:correctWords.getX();
	    		currentY=Horiz ? correctWords.getY():correctWords.getY()+i;
	    			if(isCross(currentX, currentY))
	    			{
	    				int j = currentX-this.getCorrectWord(currentX, currentY,!correctWords.getHoriz()).getX()+currentY-this.getCorrectWord(currentX, currentY,!correctWords.getHoriz()).getY();
	    				if(currentWords.charAt(i) == 
	    					this.getCorrectWord(currentX, currentY,!correctWords.getHoriz()).getCap().charAt(j)){continue;}
	   				}
	    		    		  
	    				return false;	
	    		}
	    				
	    	}
			  return true;
		//	  }
		  //else  
		//	  return currentWords.equalsIgnoreCase(correctWords.getCap()) == true ? true:false;
		  
		  
		//  return  true;
	    
	    
	    
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
		    	System.out.println((horizontalWord != null) ? horizontalWord : verticalWord);
		    	return (horizontalWord != null) ? horizontalWord : verticalWord;}
		    else
		    	return (verticalWord != null) ? verticalWord : horizontalWord;
	    }
	  
		  public void disTip()//错误提示
		  {
			  
		  }
		  
		  public void replay() //记得调用重绘
		  {
			  for(int i = 0;i < this.width;i++)
				  for(int j = 0;j < this.height;j++)
				  {
					  if(!this.isBlock(i,j))
						//  continue;
					//  else 
						  {
						  		//this.setDisValue(i, j, Crossword.UNFILLED);
						  		//this.setValue(i, j, Crossword.UNFILLED);
						  this.area[i][j] = Crossword.UNFILLED;
						  this.displayArea[i][j] = Crossword.UNFILLED;
				  }
			  
			
			  }
		  }
		 public boolean isBlock(int x, int y)
		 {
				return (this.area[y][x].equals(Crossword.BLOCK));
		 }
	   	public boolean isChinese(int x,int y)
		{
				
			return  this.displayArea[y][x].getBytes().length == this.displayArea[y][x].length()?false:true;
		}
			
		public void setValue(int x, int y, String value) {
			if (this.area[y][x] != Crossword.BLOCK&&!this.isChinese(x,y))
			
				{
					this.area[y][x] = value.toUpperCase();
					//	System.out.println(this.area[y][x]);
				}
			}
		public void setDisValue(int x, int y, String value) {
			if (this.area[y][x] !=Crossword.BLOCK&&!this.isChinese(x,y))
			
				{
				this.displayArea[y][x] = value.toUpperCase();
				System.out.println(this.displayArea[y][x]);
				}
		}
	  
		//保存Grid信息，先写入JSON，再写入数据库
		public void save(GameGridAdapter gridAdapter,Grid grid){
			for(Word entry:grid.getEntries()){
				String word = this.getWord(entry.getX(), entry.getY(),entry.getLength(),entry.getHoriz());
				entry.setTmp(word);			                                                         
			}
			JSONObject jObj = jsonUtil.writeToJson(grid);
			//用以保存数据的grid类，主要是保存在数据库中，增加了jsonData字段
			grid.setJsonData(jObj.toString());
			dbManager.updateGridData(grid);	
		}
		
		
		
		
		//通过第几关查找数据库
		public Grid queryByUniqueid(int uniqueid){
			//无论如何先看数据库里面有符合uniqueid的项
			this.grid = dbManager.queryGridByKey("uniqueid", uniqueid,this.jsonUtil);
			//如果没有查到，则打开网络访问
			if(this.grid == null){
			   //通过URL解析Json
				//this.grid = parseGridFromUrl(this.context,Crossword.GRID_URL + uniqueid);
				if((this.grid = parseGridFromUrl(this.context,Crossword.GRID_URL + uniqueid)).getFilename() == null){
					return null;
				}
			}
			
			
			initModule(this.grid);
			return this.grid;
			
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
		    	for (int i = 0 ; i < entry.getLength(); i++) 
		    	{
		    		if (horizontal)
		    		{
		    			if (y >= 0 && y < this.height && x+i >= 0 && x+i < this.width)
		    			{
		    				this.area[y][x+i] = String.valueOf(tmp.charAt(i)).equals(Crossword.UNFILLED)?Crossword.UNFILLED:String.valueOf(tmp.charAt(i));
		    				this.displayArea[y][x+i] = String.valueOf(tmp.charAt(i)).equals(Crossword.UNFILLED)?Crossword.UNFILLED:String.valueOf(tmp.charAt(i));
		    				this.correctionArea[y][x+i] = String.valueOf(text.charAt(i));
		    			}
		    		}
		    		else
		    		{
		    			if (y+i >= 0 && y+i < this.height && x >= 0 && x < this.width)
		    			{
		    				this.area[y+i][x] =  String.valueOf(tmp.charAt(i)).equals(Crossword.UNFILLED)?Crossword.UNFILLED:String.valueOf(tmp.charAt(i));
		    				this.displayArea[y+i][x] = String.valueOf(tmp.charAt(i)).equals(Crossword.UNFILLED)?Crossword.UNFILLED:String.valueOf(tmp.charAt(i));
		    				this.correctionArea[y+i][x] = String.valueOf(text.charAt(i));
		    			}
		    		}
		    	}
		    	
		   }
			for(int currentX=0; currentX<this.width;currentX++)
		    	for(int currentY=0;currentY<this.height;currentY++)
		    		{
		    		   if(this.isBlock(currentX,currentY))
		    			  continue;
		    		   if(this.area[currentY][currentX].equals(Crossword.AREA_BLOCK))
		    			   continue;

		    		   Word currentWord = this.getCorrectWord(currentX,currentY,true);
			this.toChinese(currentX,currentY,currentWord);
			}
		}
			
			public void toChinese(int currentX,int currentY,Word currentWord)
			{
				/*for(int currentX=0; currentX<this.width;currentX++)
			    	for(int currentY=0;currentY<this.height;currentY++)
			    		{
			    		   if(this.isBlock(currentX,currentY))
			    			  continue;
			    		   if(this.area[currentY][currentX]==null)
			    			   continue;

			    		   Word currentWord = this.getCorrectWord(currentX,currentY,true);*/
			     			   if(this.isCorrect(this.getCorrectWord(currentWord.getX(), currentWord.getY(), currentWord.getHoriz()),this.getWord(currentWord.getX(),currentWord.getY(),currentWord.getLength(), currentWord.getHoriz()),currentX,currentY))
			       		    	{
			       				  for(int l = 0; l < currentWord.getLength(); l++)
			       				  {
			       					if(currentWord.getHoriz())  this.setDisValue(currentWord.getX()+l, currentWord.getY(),currentWord.getAns(l));
			       					       						
			            		    if(!currentWord.getHoriz()) this.setDisValue(currentWord.getX(), currentWord.getY()+l,currentWord.getAns(l));  
			       		            		   
			       				  }
			       		    	}
			    		    if(this.isCross(currentX,currentY))
			        		  {
			    			   if(this.isCorrect(this.getCorrectWord(currentX, currentY, !currentWord.getHoriz()),
			    					   this.getWord(this.getCorrectWord(currentX, currentY, !currentWord.getHoriz()).getX(),this.getCorrectWord(currentX, currentY, !currentWord.getHoriz()).getY(), 
			    							   this.getCorrectWord(currentX, currentY, !currentWord.getHoriz()).getLength(), 
			    							   this.getCorrectWord(currentX, currentY, !currentWord.getHoriz()).getHoriz()),currentX,currentY))
				   		    	{
				   				  for(int l = 0; l < this.getCorrectWord(currentX, currentY, !currentWord.getHoriz()).getLength(); l++)
				   				    {
				   						if(!currentWord.getHoriz()) 
				   						{

				   							this.setDisValue(this.getCorrectWord(currentX, currentY, !currentWord.getHoriz()).getX()+l,
				   									this.getCorrectWord(currentX, currentY, !currentWord.getHoriz()).getY(),this.getCorrectWord(currentX, currentY, !currentWord.getHoriz()).getAns(l));
				   						}
				   						if(currentWord.getHoriz())
				   		            	{
				   							this.setDisValue(this.getCorrectWord(currentX, currentY, !currentWord.getHoriz()).getX(),
				   									this.getCorrectWord(currentX, currentY, !currentWord.getHoriz()).getY()+l,this.getCorrectWord(currentX, currentY, !currentWord.getHoriz()).getAns(l));  

				   		            	}
				   		            }
				   		         
				   			     }   
			    	       }
			    		  		
			}
				
					
			
		
		
		public String getareaValue(int x,int y)
		
		{
			System.out.println("fanhui le zhege zhi"+this.area[y][x]);
			if(this.isBlock(x, y)) return Crossword.BLOCK;
			return this.area[y][x];
		}
		
		public String getdisplayAreaValue(int x,int y)
		
		{
			if(this.isBlock(x, y)) return Crossword.BLOCK;
			System.out.println("fanhui le zhege zhi"+this.area[y][x]);
			return this.displayArea[y][x];
		}
		
		/*public String getcorrectionAreaValue(int x,int y)
		
		{
			if(this.isBlock(x, y)) return Crossword.BLOCK;
			System.out.println("fanhui le zhege zhi"+this.area[y][x]);
			return this.correctionArea[y][x];
		}*/
		
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
		
		public String getWord(int x, int y, int length, boolean isHorizontal) {
	    	StringBuffer word = new StringBuffer();
	    	for (int i = 0; i < length; i++) {
	    		if (isHorizontal) {
	    			if (y < this.height && x+i < this.width)
	    				word.append(this.area[y][x+i].equals(Crossword.UNFILLED)?Crossword.UNFILLED:this.area[y][x+i]);
	    		}
	    		else {
	    			if (y+i < this.height && x < this.width)
	    				word.append(this.area[y+i][x].equals(Crossword.UNFILLED)?Crossword.UNFILLED:this.area[y+i][x]);
	    		}
	    	}
	    	return word.toString();
		}
		
		public boolean isACh(Activity act) {
			int filled = 0;
			int empty = 0;
			
			for (int y = 0; y < this.height; y++)
				for (int x = 0; x < this.width; x++)
					if (!this.area[y][x].equals(Crossword.BLOCK) ) {
						if (this.isChinese(x, y))
							filled++;
						else
							empty++;
					}
			
			if(filled==empty+filled) 
			{
				Toast toast=Toast.makeText(act, "通关了，你这么厉害，你家里人知道么".toString(), Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return true;
				}
			return false;//return filled * 100 / (empty + filled);
		}
}