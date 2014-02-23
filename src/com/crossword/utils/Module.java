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
	 private Grid   grid;                   //��json�н�����grid��������Ϣ�������ؿ�����Ϣ�Լ����е�Word
	 private Vol    currVol;                    //��ǰ��
	 
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
			 Log.v(" parseVolFromUrl", jsonData);
			 
			 return null;
		 }
		// if(jsonData.equals(Crossword.UNCONNECT))
		 
		 entities = jsonUtil.parseVolJson(jsonData);
		 //����������Vol���ݲ��뵽���ݿ���VOLTABLE��
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
		//����Grid��Ϣ����д��JSON����д�����ݿ�
		
		
		
		
		public Grid queryGridByUniqueid(int vol,int lv,int uniqueid){
			//��������ȿ����ݿ������з���uniqueid����
			Log.v("test..queryxiayibu...",Crossword.GRID_ROOT_URL +"vol="+vol+"&lv="+lv);
			this.grid = dbManager.queryGridByKey("uniqueid", uniqueid,this.jsonUtil);
			//���û�в鵽������������
			if(this.grid == null){
			   //ͨ��URL����Json
				//this.grid = parseGridFromUrl(this.context,Crossword.GRID_URL + uniqueid);
				if((this.grid = parseGridFromUrl(this.context,Crossword.GRID_ROOT_URL +"vol="+vol+"&lv="+lv)).getFilename() == null){
					return null;//Ҫ��ʾ��ȡʧ��
				}
			}
		//	Log.v("nima ",""+this.grid.getWidth());
		//	Log.v("��ʼ������json",this.grid.getJsonData());
		//	boardLogic.initModule(this.grid);
			return this.grid;
			
		}
		//�ȱ���VOLTABLE�е������Ϊ�˻����ڻعˣ�������
		public LinkedList<Vol> getNewestVol(){
			
			LinkedList<Vol> entities = new LinkedList<Vol>();
			
			//���Ӧ����һ�����̴߳ӷ������ϼ������µ�vol�ģ�Ŀǰ�Ȳ����ǣ�ֻ�������غ��ȡ���е�vol
			parseVolFromUrl(Crossword.VOL_REQUEST_URL);
			entities = dbManager.queryAllExistVol();
			Comparator comp = new MyComparator();
			
         Collections.sort(entities,comp);
         //��ȡ������ʱ��Ҫ����һ�����з���
         for(Vol entity:entities){
         	
         	updateVolScore(entity);
         }
			return entities;
		}

		
		public LinkedList<Grid> getGrids(Vol vol)
		{
			LinkedList<Grid> entities = new LinkedList<Grid>();
			Log.v("test..��ʼ����ѯ��ַ.","..."+vol.getIsbroad()+"..."+vol.getVolNumber());
			entities = dbManager.queryGridByKey("volNumber",vol.getVolNumber() );
			Log.v("test..entities��...",entities == null?"t":"w");
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
		
	
		//ͨ����ǰ��volnumber���ҵ�ǰ��vol
		public Vol queryVolByVolNumber(int volNumber){
			
			Vol vol = dbManager.queryVolByKey("vol_no", volNumber);
			return vol;
		}
		
		
		//����ĳһ�ڵĻ���
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