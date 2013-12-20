package com.crossword.utils;

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

public class Module {
	 private Grid   grid;                   //��json�н�����grid��������Ϣ�������ؿ�����Ϣ�Լ����е�Word
	 private Vol    currVol;                    //��ǰ��
	 private LinkedList<Word> entries  =  new LinkedList<Word>();
	 private JsonUtil jsonUtil;
	 private DBManager dbManager;
	 private int width;
	 private int height;

	 private String[][]			area;			// Tableau représentant les lettres du joueur
	 private String[][] 		displayArea;
	 private String[][] 		correctionArea; // Tableau représentant les lettres correctes
     private Context            context;
     private int 				score;
     private int				filled;
     private int 				empty;
 	 private int 			hintCount ;
 	 private int 			corCount ;
 	 private int 			errCount ;
          
 	 
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
		//�����꣬��Json���ݼ������ݿ���
		 dbManager.updateGridData(grid);
	     return grid;
	 }
	 
	 //����JsonUtil��parseVolJson��������json�н��������ݣ�����û�л�����Ϣ����Ҫ�˹�����
	 public LinkedList<Vol> parseVolFromUrl(String url){
		 
		 LinkedList<Vol> entities = new LinkedList<Vol>();
		 String jsonData = jsonUtil.readJsonFromUrl(url);
		 //���jsonDataΪ�գ�˵��û�����سɹ�
		 if(jsonData == null){
			 return null;
		 }
		 entities = jsonUtil.parseVolJson(jsonData);
		 //����������Vol���ݲ��뵽���ݿ���VOLTABLE��
		 for(Vol entity:entities){
			 dbManager.add(entity);
		 }
		 return entities;
	 }
	 
	//��ʼ��Module�е�width,height,area��
	 public void initModule(Grid grid){
		 
		 this.width = grid.getWidth(); 
		 Log.v("initMoudle..grid.getwidth", ""+this.width);
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
	    this.resetScore();
	 }
	 
	/* public LinkedList<Word>  getEntry(){
		 this.entries = this.grid.getEntries();
		 return this.grid.getEntries();
	 }*/
	   
	  public boolean isCorrect(Word correctWords,String currentWords,int x,int y)
	    {	    	
	    	int currentX=correctWords.getX();
	    	int currentY=correctWords.getY();
	
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
	    		    if(this.isWordComplete(x, y,Horiz))  this.isErr();		  //�Ʒ�
	    			//if(i < correctWords.getLength())
	    				return false;	
	    		}
	    				
	    	}
		//	  if(this.isWordComplete(x, y,Horiz)) this.isCor(correctWords.getLength());	//�Ʒ�
			  return true;   	    
	    
	    }
	  
	 
	  
		  public void disTip()//������ʾ����ʱû����
		  {
			  
		  }
		  
		  public void replay() //�ǵõ����ػ�,��ʱû����
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
		 
		/*public void delete(int x,int y)
		{
			if(area[y][x].equals(Crossword.UNFILLED))
			{
				x = (this.horizontal ? x - 1 : x);
				y = (this.horizontal ? y: y - 1);
			}
		}*/
		
		
	  
		//����Grid��Ϣ����д��JSON����д�����ݿ�
		public void save(GameGridAdapter gridAdapter,Grid grid){
			for(Word entry:grid.getEntries()){
				String word = this.getWord(entry.getX(), entry.getY(),entry.getLength(),entry.getHoriz());
				entry.setTmp(word);			                                                         
			}
			JSONObject jObj = jsonUtil.writeToJson(grid);
			//���Ա������ݵ�grid�࣬��Ҫ�Ǳ��������ݿ��У�������jsonData�ֶ�
			grid.setJsonData(jObj.toString());
			dbManager.updateGridData(grid);	
		}
		
		
		
		
		//ͨ��uniqueid�������ݿ�!!!!!Ҫ�޸�һ��
		public Grid queryGridByUniqueid(int vol,int lv,int uniqueid){
			//��������ȿ����ݿ������з���uniqueid����
			Log.v("test..queryxiayibu...",Crossword.GRID_URL +"vol="+vol+"&lv="+lv);
			this.grid = dbManager.queryGridByKey("uniqueid", uniqueid,this.jsonUtil);
			//���û�в鵽������������
			if(this.grid == null){
			   //ͨ��URL����Json
				//this.grid = parseGridFromUrl(this.context,Crossword.GRID_URL + uniqueid);
				if((this.grid = parseGridFromUrl(this.context,Crossword.GRID_URL +"vol="+vol+"&lv="+lv)).getFilename() == null){
					return null;//Ҫ��ʾ��ȡʧ��
				}
			}
			
			
			initModule(this.grid);
			return this.grid;
			
		}
		//�ȱ���VOLTABLE�е������Ϊ�˻����ڻعˣ�������
		public LinkedList<Vol> getNewestVol(){
			
			LinkedList<Vol> entities = new LinkedList<Vol>();
			
			//���Ӧ����һ�����̴߳ӷ������ϼ������µ�vol�ģ�Ŀǰ�Ȳ����ǣ�ֻ�������غ��ȡ���е�vol
			parseVolFromUrl(Crossword.VOL_REQUEST_URL);
			entities = dbManager.queryAllExistVol();
			return entities;
		}
		
		
		
		public LinkedList<Grid> getGrids(int len,int vol){
			
			LinkedList<Grid> entities = new LinkedList<Grid>();
			Log.v("test..queryentities1...",Crossword.GRID_URL);
			entities = dbManager.queryGridByKey("volNumber",vol );
			Log.v("test..queryentities2...",entities == null?"t":"w");
			int l;
			if(entities == null) l = 0;
			else l = entities.size();
			if(l<len)
			{ 
				Log.v("test..queryentities3...",""+l);
				for(int i = l;i < len;i++)
				{
					Grid grid = new Grid();
					grid.setIslocked(0);
				//	System.out.println("testi..."+(i-l));
					grid.setLevel(i+1-l);
					grid.setVol(vol);					
					grid.setStar(2-i+l);
					dbManager.add(grid);
					System.out.println("testi2..."+(i-l));
				//	entities.add(grid);
				}
			}
			/*for(Grid s :entities)
			{
				System.out.println("....test+s.getlevel..."+s.getLevel());
			}*/
			entities = dbManager.queryGridByKey("volNumber",vol );
			for(Grid s :entities)
			{
				System.out.println("....test+s.getlevel..."+s.getLevel());
			}
			return entities;
		}
		
		//ͨ����ǰ��grid���ҵ�ǰ��vol
		public Vol queryVolByVolNumber(Grid grid){
			
			Vol vol = dbManager.queryVolByKey("vol_no", grid.getVol());
			
			return vol;
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
				
			     			   if(this.isCorrect(this.getCorrectWord(currentWord.getX(), currentWord.getY(), currentWord.getHoriz()),this.getWord(currentWord.getX(),currentWord.getY(),currentWord.getLength(), currentWord.getHoriz()),currentX,currentY))
			       		    	{
			       				  for(int l = 0; l < currentWord.getLength(); l++)
			       				  {
			       					if(currentWord.getHoriz())  this.setDisValue(currentWord.getX()+l, currentWord.getY(),currentWord.getAns(l));
			       					       						
			            		    if(!currentWord.getHoriz()) this.setDisValue(currentWord.getX(), currentWord.getY()+l,currentWord.getAns(l));  
			       		            		   
			       				  }
			       			//	  this.isCor();
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
				   			//	  this.isCor();
				   			     }   
			    	       }
			    		  		
			}
				
					
	  public void setValue(int x, int y, String value) 
		{
			if (this.area[y][x] != Crossword.BLOCK&&!this.isChinese(x,y))
			
				
					this.area[y][x] = value.toUpperCase();
		}
		
	  public String getAreaValue(int x,int y)
		
		{
			
			if(this.isBlock(x, y)) return Crossword.BLOCK;
			return this.area[y][x];
		}
	  
	  public void setDisValue(int x, int y, String value) {
			if (this.area[y][x] !=Crossword.BLOCK&&!this.isChinese(x,y))
			
				{
				this.displayArea[y][x] = value.toUpperCase();
			//	System.out.println(this.displayArea[y][x]);
				}
		}
		
			
		
		public String getdisplayAreaValue(int x,int y)
		
		{
			if(this.isBlock(x, y)) return Crossword.BLOCK;
			
			return this.displayArea[y][x];
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
		
		
		 public void scoring(){
			this.isCor();
			this.score=this.corCount-this.hintCount-this.errCount;	
			this.grid.setScore(this.score);	
			//Log.isLoggable("score", this.score);
			
			System.out.println("score..."+this.score);
		    //return this.score;
			
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
		
		 public boolean isBlock(int x, int y)
		 {
				return (this.area[y][x].equals(Crossword.BLOCK));
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
						if (this.isChinese(x, y))
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
		
		//���¼Ʒֹ���
		public boolean isWordComplete(int x ,int y,boolean h)
		{
			
			Word corw = this.getCorrectWord(x, y, h);
			String curw = this.getWord(x, y,corw.getLength(), corw.getHoriz());
			if(curw.contains(Crossword.UNFILLED))   return false;	
			
			return true;
		}
		//ͳ�ֲ��ԣ�����
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
			}
}