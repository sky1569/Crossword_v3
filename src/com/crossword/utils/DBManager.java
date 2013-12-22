package com.crossword.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.crossword.Crossword;
import com.crossword.data.Grid;
import com.crossword.data.GridforSaved;
import com.crossword.data.Vol;

public class DBManager {

	private DBHelper helper;
	private SQLiteDatabase db;
	

	public DBManager(Context context){
		
		helper = new DBHelper(context);
		//��ΪgetWritableDatabase�ڲ�������mContext.openOrCreateDatabase(mName,0,mFactory);
		//����Ҫȷ��context�Ѿ���ʼ�������԰�ʵ������DBManager�Ĳ������Activity��onCreate��
		//db = helper.getWritableDatabase();
	}
	
	/*public void add(List<Grid> grids){
		db.beginTransaction();
		for(Grid g:grids){
			
		}
	}*/
	
	
	//���µ�����Ϣ���뵽���ݿ���
	public void add(Vol vol){
		
		db = helper.getWritableDatabase();
		Cursor c = queryCursorByKey(Crossword.VOL_TABLE,Crossword.columnsOfVolTable,"vol_no",vol.getVolNumber());
		//�����Ϊ�վͲ����ٲ���
		if(c.getCount() != 0){
			
			return ;
		}
		
		db.beginTransaction();
		try{
			
			ContentValues cv = new ContentValues();
			cv.put("name", vol.getVolName());
			cv.put("open_date", vol.getOpenDate());
			cv.put("amount_of_levels", vol.getAmountOfLevels());
			cv.put("vol_no", vol.getVolNumber());
			cv.put("score", vol.getScore());
			//��ʼ�����VOL_TABLE����
			db.insert(Crossword.VOL_TABLE, null, cv);
			db.setTransactionSuccessful();
			
		}finally{
			db.endTransaction();
			db.close();
		}
		
	}
	
	
	
	//���µ�grid���ӵ����ݿ���
	public void add(Grid grid){

		//���Ҫ��
		db = helper.getWritableDatabase();
		Cursor c = queryCursorByKey(Crossword.GRID_TABLE,Crossword.columnsOfGridTable,"uniqueid",grid.getUniqueid());
		//�����Ϊ�վͲ����ٲ�����
		if(c.getCount() != 0){
			
			return;
		}
		db.beginTransaction();
		try{
		ContentValues cv = new ContentValues();
		cv.put("file", grid.getFilename());
		cv.put("uniqueid", grid.getUniqueid());
		cv.put("volNumber", grid.getVol());
		cv.put("level", grid.getLevel());
		cv.put("degree", grid.getDegree());
		cv.put("category", grid.getCategory());
		cv.put("islocked", grid.getIslocked());
	    cv.put("star", grid.getStar());
		cv.put("jsonData",grid.getJsonData());
		cv.put("score", grid.getScore());
		cv.put("date", grid.getDate());
		cv.put("gamename", grid.getGamename());
		cv.put("author", grid.getAunthor());
		cv.put("width", grid.getWidth());
		cv.put("height", grid.getHeight());
		db.insert(Crossword.GRID_TABLE, null, cv);
		db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
			db.close();
		}
		Log.v("��ӳɹ�", ""+grid.getVol());
		
		//db.close();
		/*try{
			db.execSQL("INSERT INTO "+Crossword.TABLE_NAME + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?)",
					new Object[]{grid.getFilename(),grid.getUniqueid(),grid.getVol(),grid.getLevel()
					,grid.getCategory(),grid.getJsonData(),grid.getScore(),grid.getDate(),grid.getGamename(),
					grid.getAunthor(),grid.getWidth(),grid.getHeight()});
		}finally{
		db.endTransaction();
		}*/
		
	}
	/**
	 * ���Ը���grid���е�ĳһ��grid��
	 * @param grid
	 */
	
	public void updateGridData(Grid grid){
		db = helper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("file", grid.getFilename());
		cv.put("uniqueid", grid.getUniqueid());
		cv.put("volNumber", grid.getVol());
		cv.put("level", grid.getLevel());
		cv.put("degree", grid.getDegree());
		cv.put("category", grid.getCategory());
		cv.put("islocked", grid.getIslocked());
		cv.put("star", grid.getStar());
		cv.put("jsonData",grid.getJsonData());
		cv.put("score", grid.getScore());
		cv.put("date", grid.getDate());
		cv.put("gamename", grid.getGamename());
		cv.put("author", grid.getAunthor());
		cv.put("width", grid.getWidth());
		cv.put("height", grid.getHeight());
		
		Log.v("����.grid.getLevel().", ""+grid.getLevel());
		Log.v("����..grid.getUniqueid()..", ""+grid.getUniqueid());
		
		Log.v("����..grid.getUniqueid()..", ""+grid.getJsonData());
		//db = helper.getWritableDatabase();
		Cursor c = queryCursorByKey(Crossword.GRID_TABLE,Crossword.columnsOfGridTable,"uniqueid",grid.getUniqueid());
		if(c.getCount() == 0)
			db.execSQL("update grid set uniqueid="+grid.getUniqueid()+" where volNumber="+grid.getVol()+" and level="+grid.getLevel());
		db.update(Crossword.GRID_TABLE, cv, "uniqueid = ?", new String[]{grid.getUniqueid().toString()});
		db.close();
	}
	
	/**
	 * ����VOL_TABLE
	 */
	
	public void updateVolData(Vol vol){
		
		db = helper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("name", vol.getVolName());
		cv.put("open_date", vol.getOpenDate());
		cv.put("amount_of_levels", vol.getAmountOfLevels());
		cv.put("vol_no", vol.getVolNumber());
		cv.put("score", vol.getScore());
		//����VOL_TABLE��
		db.update(Crossword.VOL_TABLE, cv, "vol_no = ?", new String[]{vol.getVolNumber().toString()});
		db.close();
	}
	
	/**
	 * 
	 */
	/**/
	public Grid queryGridByKey(String key,Object value,JsonUtil jsonUtil){
		db = helper.getWritableDatabase();
		Cursor c = queryCursorByKey(Crossword.GRID_TABLE,Crossword.columnsOfGridTable,key,value);
		//���û�в鵽
		if(c.getCount() == 0){
			return null;
		}
		Grid g = new Grid();
		if(c.moveToFirst()){
			String jsonData = c.getString(c.getColumnIndex("jsonData"));
			Log.v("test..ȡֵ����jsonData",jsonData);
			g = jsonUtil.parseGridJson(jsonData);
			if(g==null) Log.v("��������", "hh");
		}

		c.close();
		db.close();
		return g;
	}
	
	
	
	public LinkedList<Grid> queryGridByKey(String key,Object value){
		db = helper.getWritableDatabase();
		Cursor c = queryCursorByKey(Crossword.GRID_TABLE,Crossword.columnsOfGridTable,key,value);
		if(c.getCount() == 0){
			return null;
		}
		
		
		LinkedList<Grid> entities = new LinkedList<Grid>();
		while(c.moveToNext())
		{
			Grid g = new Grid();
			g.setVol(c.getInt(c.getColumnIndex("volNumber")));
		//	Log.v("entities.sizeg.getVol",""+c.getColumnIndex("volNumeber"));
			g.setStar(c.getInt(c.getColumnIndex("star")));
		//	Log.v("entities.sizeg.getStar",""+c.getColumnIndex("star"));
			g.setLevel(c.getInt(c.getColumnIndex("level")));
			//Log.v("entities.sizeg.getLevel()",""+g.getLevel());
			
			g.setIslocked(c.getInt(c.getColumnIndex("islocked")));
		//	Log.v("entities.sizeg.getIslocked()",""+g.getIslocked());
			g.setUniqueid(c.getInt(c.getColumnIndex("uniqueid"))==0?null:c.getInt(c.getColumnIndex("uniqueid")));
		
			Log.v("uniqueid", ""+g.getUniqueid()+"..."+c.getInt(c.getColumnIndex("uniqueid")));
			
			if(c.getString(c.getColumnIndex("jsonData"))!=null)
			g.setJsonData(c.getString(c.getColumnIndex("jsonData")));
			entities.add(g);
			Log.v("entities.size",""+entities.size());
		}

		c.close();
		db.close();
		return entities;
	}
	/**
	 * ���ݹؼ�ֵ����Vol��һ��ͨ������
	 */
	public boolean unlockNext(Object vol,int lv)
	{
		db = helper.getWritableDatabase();
		try{		
			Log.v("test dbunlock vol lv", "vol.."+vol+"lv.."+lv );
			db.execSQL("update grid set islocked="+Crossword.GRIDUNLOCKED+" where volNumber="+vol+" and level="+lv);
		}
		catch(Exception e)
		{
			   Log.v("test dbunlock", "unlock����shibai" );
			return false;
		}
		   Log.v("test dbunlock", "unlock���гɹ�" );		
			db.close();
			return true;
	}
	
	public Vol queryVolByKey(String key,Object value){
		
		db = helper.getWritableDatabase();
		Cursor c = queryCursorByKey(Crossword.VOL_TABLE,Crossword.columnsOfVolTable,key,value);
		
		//���û�в鵽
		if(c.getCount() == 0){
			
			return null;
		}
		
		Vol vol = new Vol();
		if(c.moveToFirst()){
			vol.setVolName(c.getString(c.getColumnIndex("name")));
			vol.setOpenDate(c.getString(c.getColumnIndex("open_date")));
			vol.setAmountOfLevels(c.getInt(c.getColumnIndex("amount_of_levels")));
			vol.setVolNumber(c.getInt(c.getColumnIndex("vol_no")));
			vol.setScore(c.getInt(c.getColumnIndex("score")));
	
		}
		
		c.close();
		db.close();
		return vol;
	}
	
	/**
	 * ɾ��grid
	 * @param grid
	 */
	public void deleteOldGrid(GridforSaved grid){
		
	}

	
	
	
	
	/**
	 * ��ѯgrid�������grid�����������б�
	 * @return
	 */
	/**/public List<GridforSaved> query(){
		ArrayList<GridforSaved> grids = new ArrayList<GridforSaved>();
		Cursor c = queryTheCursor(Crossword.GRID_TABLE);
		while(c.moveToNext()){
			GridforSaved g = new GridforSaved();
			g.setFilename(c.getString(c.getColumnIndex("file")));
			g.setUniqueid(c.getInt(c.getColumnIndex("uniqueid")));
			g.setVol(c.getInt(c.getColumnIndex("volNumber")));
			g.setLevel(c.getInt(c.getColumnIndex("level")));
			g.setDegree(c.getInt(c.getColumnIndex("degree")));
			g.setCategory(c.getString(c.getColumnIndex("category")));
			g.setIslocked(c.getInt(c.getColumnIndex("islocked")));
			g.setStar(c.getInt(c.getColumnIndex("star")));
			g.setJsonData(c.getString(c.getColumnIndex("jsonData")));
			g.setScore(c.getInt(c.getColumnIndex("score")));
			g.setDate(c.getString(c.getColumnIndex("date")));
			g.setGamename(c.getString(c.getColumnIndex("gamename")));
			g.setAuthor(c.getString(c.getColumnIndex("author")));
			g.setWidth(c.getInt(c.getColumnIndex("width")));
			g.setHeight(c.getInt(c.getColumnIndex("height")));
			
			
			grids.add(g);
		}
		//c.close();
		return grids;
	}
	
	
	/**
	 * ��ѯVol�б�������vol���������б�
	 */
	
	public LinkedList<Vol> queryAllExistVol(){
		db = helper.getWritableDatabase();
		LinkedList<Vol> entities = new LinkedList<Vol>();
		Cursor c = queryTheCursor(Crossword.VOL_TABLE);
		while(c.moveToNext()){
			
			Vol entity = new Vol();
			entity.setVolName(c.getString(c.getColumnIndex("name")));
			entity.setOpenDate(c.getString(c.getColumnIndex("open_date")));
			entity.setAmountOfLevels(c.getInt(c.getColumnIndex("amount_of_levels")));
			entity.setVolNumber(c.getInt(c.getColumnIndex("vol_no")));
			entity.setScore(c.getInt(c.getColumnIndex("score")));
			entities.add(entity);
		}
		db.close();
		return entities;
	}
	/*public LinkedList<Grid> queryGridOfVol(int volNumber)
	{	
		db = helper.getWritableDatabase();
		LinkedList<Grid> entities = new LinkedList<Grid>();
		Cursor c = queryCursorByKey(Crossword.GRID_TABLE,Crossword.columnsOfGridTable,"volNumber",volNumber);
		while(c.moveToNext())
		{
			Grid entity =new Grid();
			
			
		}
		return entities;
	}*/
	
	/**
	 * ��ѯgrid�������grid������һ��ָ��
	 * @return
	 */
	public Cursor queryTheCursor(String tableName){
		//db = helper.getWritableDatabase();
		Cursor c = db.rawQuery("SELECT * FROM "+tableName, null);
		//db.close();
		return c;
	}
	
	/**
	 * ��ѯ���д�����ֵ�ļ�¼
	 *
	 */
	public Cursor queryCursorByKey(String tableName,String[] columns,String key,Object value){
		//db = helper.getWritableDatabase();
		
		/*String[] columns = {"file","uniqueid","volNumber","level","degree","category","islocked",
				//"star",
				"jsonData","score",
				             "date","gamename","author","width","height"};*/
		String selection = key+"="+value;
		Cursor c = db.query(true, tableName, columns, selection, null, null, null, null, null);
		//db.close();
		return c;
	}
	
	/**
	 * �ر����ݿ�
	 */
	public void closeDB(){
		db.close();
	}
}
















