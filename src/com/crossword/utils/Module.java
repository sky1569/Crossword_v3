package com.crossword.utils;

import java.util.Collections;
import java.util.Comparator;
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
import com.crossword.logic.BoardLogic;

public class Module {
	 private Grid   grid;                   //从json中解析出grid的所有信息，包括关卡的信息以及所有的Word
	 private Vol    currVol;                    //当前期
	 
	 private JsonUtil jsonUtil;
	 private DBManager dbManager;
	
	 
	 
     private Context            context;
     
   //  private BoardLogic 	boardLogic;
 	 
     public Module(Context context){
    	 this.context = context;
    	 this.dbManager = new DBManager(context);
    	 this.jsonUtil = new JsonUtil(context);
    //	 this.boardLogic = new BoardLogic(context);
     }
     

	 
	 public Grid parseGridFromUrl(Context context,String url){
		 Grid grid = new Grid();
		
		 String jsonData = jsonUtil.readJsonFromUrl(url);
		 grid =  jsonUtil.parseGridJson(jsonData);
		//解析完，将Json数据加入数据库中
		 dbManager.updateGridData(grid);
	     return grid;
	 }
	 
	 //调用JsonUtil的parseVolJson函数，从json中解析期数据，但是没有积分信息，需要人工计算
	 public LinkedList<Vol> parseVolFromUrl(String url){
		 
		 LinkedList<Vol> entities = new LinkedList<Vol>();
		 String jsonData = jsonUtil.readJsonFromUrl(url);
		 //如果jsonData为空，说明没有下载成功
		 if(jsonData == null){
			 Log.v(" parseVolFromUrl", jsonData);
			 
			 return null;
		 }
		// if(jsonData.equals(Crossword.UNCONNECT))
		 
		 entities = jsonUtil.parseVolJson(jsonData);
		 //将解析到的Vol数据插入到数据库中VOLTABLE中
		 for(Vol entity:entities){
			 dbManager.add(entity);
		 }
		 return entities;
	 }
	 
	 
	 public Vol parseBroadFromUrl(String url){
		 
		 String jsonData = jsonUtil.readJsonFromUrl(url);
		 Vol broadVol = jsonUtil.parseBroad(jsonData);
		 dbManager.add(broadVol);
		 return broadVol;
		 
	 }
		//保存Grid信息，先写入JSON，再写入数据库
		
		
		
		
		public Grid queryGridByUniqueid(int vol,int lv,int uniqueid){
			//无论如何先看数据库里面有符合uniqueid的项
			Log.v("test..queryxiayibu...",Crossword.GRID_ROOT_URL +"vol="+vol+"&lv="+lv);
			this.grid = dbManager.queryGridByKey("uniqueid", uniqueid,this.jsonUtil);
			//如果没有查到，则打开网络访问
			if(this.grid == null){
			   //通过URL解析Json
				//this.grid = parseGridFromUrl(this.context,Crossword.GRID_URL + uniqueid);
				if((this.grid = parseGridFromUrl(this.context,Crossword.GRID_ROOT_URL +"vol="+vol+"&lv="+lv)).getFilename() == null){
					return null;//要提示获取失败
				}
			}
		//	Log.v("nima ",""+this.grid.getWidth());
		//	Log.v("初始化测试json",this.grid.getJsonData());
		//	boardLogic.initModule(this.grid);
			return this.grid;
			
		}
		//先遍历VOLTABLE中的所有项，为了画往期回顾，再下载
		public LinkedList<Vol> getNewestVol(){
			
			LinkedList<Vol> entities = new LinkedList<Vol>();
			
			//这段应当加一个多线程从服务器上加载最新的vol的，目前先不考虑，只做先下载后读取所有的vol
			parseVolFromUrl(Crossword.VOL_REQUEST_URL);
			entities = dbManager.queryAllExistVol();
			Comparator comp = new MyComparator();
			
         Collections.sort(entities,comp);
         //获取最新期时需要更新一下所有分数
         for(Vol entity:entities){
         	
         	updateVolScore(entity);
         }
			return entities;
		}

		
		public LinkedList<Grid> getGrids(Vol vol)
		{
			LinkedList<Grid> entities = new LinkedList<Grid>();
			Log.v("test..初始化查询地址.","..."+vol.getIsbroad()+"..."+vol.getVolNumber());
			entities = dbManager.queryGridByKey("volNumber",vol.getVolNumber() );
			Log.v("test..entities空...",entities == null?"t":"w");
			int l;
			if(entities == null) l = 0;
			else l = entities.size();
			if(l < vol.getAmountOfLevels())
			{ 
				Log.v("test..queryentities3...",""+l);
				for(int i = l;i < vol.getAmountOfLevels();i++)
				{
					Grid grid = new Grid();
					grid.setLevel(i+1);
					grid.setVol(vol.getVolNumber());			
					Log.v("grid.setVol(vol).getVolNumber() )", ""+vol.getVolNumber()+".."+vol.getVolName());
					grid.setStar(0);
					
					
					if(!vol.getIsbroad())
					{
						if(i == 0) grid.setIslocked(Crossword.GRIDUNLOCKED);
						else  grid.setIslocked(Crossword.GRIDLOCKED);
						//grid.setGameMode(Crossword.GAMEMODEVOL);
						
					}
					else {
						 //  grid.setGameMode(Crossword.GAMEMODELIVE);
						   if(i < vol.getCurLevel()) 	grid.setIslocked(Crossword.GRIDUNLOCKED);	
						   else                         grid.setIslocked(Crossword.GRIDLOCKED);
					
					}									
						
					
					dbManager.add(grid);
				}
				
					entities = dbManager.queryGridByKey("volNumber",vol.getVolNumber() );
					
			}
			Comparator comp = new MyComparator();
         Collections.sort(entities,comp);
			return entities;
		}
		
	
		//通过当前的volnumber查找当前的vol
		public Vol queryVolByVolNumber(int volNumber){
			
			Vol vol = dbManager.queryVolByKey("vol_no", volNumber);
			return vol;
		}
		
		
		//更新某一期的积分
		 public void updateVolScore(Vol vol){
			 
			LinkedList<Grid> grids = getGrids(vol);
			
			if(grids == null){
				return;
			}
			int volScore = 0;
			for(Grid grid:grids){
		        volScore +=  grid.getScore();
		        vol.setScore(volScore);
			}
			dbManager.updateVolData(vol);
			
		 }
		 public int getOfflineScore(){
				
				return 300;
			}
			 
	
}