package com.crossword.data;

import java.util.ArrayList;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;

import com.crossword.Crossword;
import com.ming.crossword.utils.DBManager;
import com.ming.crossword.utils.JsonUtil;

public class Module {
	// private ArrayList<Word> entries;		// Liste des mots
	 private Grid   grid;                   //从json中解析出grid的所有信息，包括关卡的信息以及所有的Word
	// private Grid			grid;	
	 private LinkedList<Word> entries  =  new LinkedList<Word>();
	 private JsonUtil jsonUtil;
	 private DBManager dbManager;
	 public Grid parseGrid(Context context,String filename){
		 this.grid = new Grid();
		 jsonUtil = new JsonUtil(context);
		 dbManager = new DBManager(context);
		 String jsonData = jsonUtil.readJsonDataFromFile(filename);
		 this.grid =  jsonUtil.parseGridJson(jsonData);
		//解析完，将Json数据加入数据库中
		 dbManager.add(this.grid);
		 return this.grid;
	 }
	 
	
	 public LinkedList<Word>  getEntry(){
		 this.entries = this.grid.getEntries();
		 return this.grid.getEntries();
	 }
	/* public ArrayList<Word> getentry(String filename) throws CrosswordException{
		 	//ArrayList<Word> entries;
			//CrosswordParser crosswordParser = new CrosswordParser();
			//SAXFileHandler.read((DefaultHandler)crosswordParser, String.format(Crossword.GRID_LOCAL_PATH, filename));
			//this.entries = crosswordParser.getData();		
	
		  return this.entries;
		  
	 }
	  */
	  public boolean isCorrect(String currentWords,String correctWords)
	    {	    	
	    	return  currentWords.equalsIgnoreCase(correctWords)==true ?true:false;
	    }
	  
	  public Word getWord(int x, int y, boolean horizontal)
	    {
	        Word horizontalWord = null;
	        Word verticalWord = null;
	     //   Word nullWord=
	        //System.out.println(horizontal);
		    for (Word entry: this.entries) {
		    	if (x >= entry.getX() && x <= entry.getXMax())
		    		if (y >= entry.getY() && y <= entry.getYMax()) {
		    			//System.out.println("entry.getHorizontal()..."+entry.getHorizontal());
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
	  
	  public void disTip()
	  {
		  
	  }
	/*  
	  public void save()
	  {
			StringBuffer wordHorizontal = new StringBuffer();
	    	StringBuffer wordVertical = new StringBuffer();
	    	try
	    	{
	    		 JSONObject gameboard = new JSONObject();  
	    		 gameboard.put("file", "td.json");  
	    		 gameboard.put("uniqueid", "123456");
	    		 gameboard.put("vol", 1);
	    		 gameboard.put("level", 0);
	    		 gameboard.put("category", "诗词");
	    		 JSONArray wordList = new JSONArray();  
	    		 JSONObject words = new JSONObject();  
	    		 for (Word entry: this.entries) {
	 		    	int x = entry.getX();
	 		    	int y = entry.getY();
	 		    	//int 
	 	  
	 		    }
	    		 
	    		 
	    		 words.put();
	    	}
	    	catch(JSONException ex)
	    	{
	    		throw new RuntimeException(ex);  
	    	}

		  
		    
	  } */
	  
	  
		//保存Grid信息，先写入JSON，再写入数据库
		public void save(Grid grid){
			JSONObject jObj = jsonUtil.writeToJson(grid);
			//用以保存数据的grid类，主要是保存在数据库中，增加了jsonData字段
			grid.setJsonData(jObj.toString());
			dbManager.updateGridData(grid);	
		}
		
		
		//通过第几关查找数据库
		public Grid queryByLevel(int level){
			
			Grid grid = dbManager.queryGridByKey("level", level,this.jsonUtil);
			return grid;
			
		}
}