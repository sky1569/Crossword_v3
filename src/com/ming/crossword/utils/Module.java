package com.ming.crossword.utils;

import java.util.ArrayList;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;

import com.crossword.Crossword;
import com.crossword.adapter.GameGridAdapter;
import com.crossword.data.Grid;
import com.crossword.data.Word;

public class Module {
	 private Grid   grid;                   //从json中解析出grid的所有信息，包括关卡的信息以及所有的Word
	 private LinkedList<Word> entries  =  new LinkedList<Word>();
	 private JsonUtil jsonUtil;
	 private DBManager dbManager;
	 public void parseGrid(Context context,String filename){
		 this.grid = new Grid();
		 jsonUtil = new JsonUtil(context);
		 dbManager = new DBManager(context);
		// String jsonData = jsonUtil.readJsonDataFromFile(filename);
		 String jsonData = jsonUtil.readJsonDataFromAssets(filename);
		 this.grid =  jsonUtil.parseGridJson(jsonData);
		//解析完，将Json数据加入数据库中
		 dbManager.add(this.grid);
	 }
	 
	
	 public LinkedList<Word>  getEntry(){
		 this.entries = this.grid.getEntries();
		 return this.grid.getEntries();
	 }
	
	  public boolean isCorrect(String currentWords,String correctWords)
	    {	    	
	    	return  currentWords.equalsIgnoreCase(correctWords)==true ?true:false;
	    }
	  
	  public Word getWord(int x, int y, boolean horizontal)
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
	  
	  public void disTip()
	  {
		  
	  }
	
	  
		//保存Grid信息，先写入JSON，再写入数据库
		public void save(GameGridAdapter gridAdapter,Grid grid){
			for(Word entry:grid.getEntries()){
				String word = gridAdapter.getWord(entry.getX(), entry.getY(),entry.getLength(),entry.getHoriz());
				entry.setTmp(word);			                                                         
			}
			JSONObject jObj = jsonUtil.writeToJson(grid);
			//用以保存数据的grid类，主要是保存在数据库中，增加了jsonData字段
			grid.setJsonData(jObj.toString());
			dbManager.updateGridData(grid);	
		}
		
		
		//通过第几关查找数据库
		public Grid queryByLevel(int level){
			
			Grid grid = dbManager.queryGridByKey("level", level,this.jsonUtil);
			this.grid = grid;
			return grid;
			
		}
}