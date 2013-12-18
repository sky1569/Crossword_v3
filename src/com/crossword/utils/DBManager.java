package com.crossword.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.crossword.Crossword;
import com.crossword.data.Grid;
import com.crossword.data.GridforSaved;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

	private DBHelper helper;
	private SQLiteDatabase db;
	
	public DBManager(Context context){
		
		helper = new DBHelper(context);
		//��ΪgetWritableDatabase�ڲ�������mContext.openOrCreateDatabase(mName,0,mFactory);
		//����Ҫȷ��context�Ѿ���ʼ�������԰�ʵ������DBManager�Ĳ������Activity��onCreate��
		//db = helper.getWritableDatabase();
	}
	
	public void add(List<Grid> grids){
		db.beginTransaction();
		for(Grid g:grids){
			
		}
	}
	
	
	public void add(Grid grid){
		//Cursor c = queryTheCursor();
		//��ĳһ��grid�Ѿ����ڣ��򷵻�
		/*while(c.moveToNext()){
			if(c.getString(c.getColumnIndex("filename")).equals(grid.getFilename())){
				
				return;
			}
			
			break;
		}*/
		//���Ҫ��
		db = helper.getWritableDatabase();
		Cursor c = queryCursorByKey("uniqueid",grid.getUniqueid());
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
		//cv.put("star", grid.getStar());
		cv.put("jsonData",grid.getJsonData());
		cv.put("score", grid.getScore());
		cv.put("date", grid.getDate());
		cv.put("gamename", grid.getGamename());
		cv.put("author", grid.getAunthor());
		cv.put("width", grid.getWidth());
		cv.put("height", grid.getHeight());
		db.insert(Crossword.TABLE_NAME, null, cv);
		db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
			db.close();
		}
		
		
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
		//cv.put("star", grid.getStar());
		cv.put("jsonData",grid.getJsonData());
		cv.put("score", grid.getScore());
		cv.put("date", grid.getDate());
		cv.put("gamename", grid.getGamename());
		cv.put("author", grid.getAunthor());
		cv.put("width", grid.getWidth());
		cv.put("height", grid.getHeight());
		
		
		db.update(Crossword.TABLE_NAME, cv, "level = ?", new String[]{grid.getLevel().toString()});
		db.close();
	}
	
	/**
	 * 
	 */
	
	public Grid queryGridByKey(String key,Object value,JsonUtil jsonUtil){
		db = helper.getWritableDatabase();
		Cursor c = queryCursorByKey(key,value);
		//���û�в鵽
		if(c.getCount() == 0){
			return null;
		}
		Grid g = new Grid();
		if(c.moveToFirst()){
			/*g.setFilename(c.getString(c.getColumnIndex("file")));
			g.setUniqueid(c.getInt(c.getColumnIndex("uniqueid")));
			g.setVol(c.getInt(c.getColumnIndex("vol")));
			g.setLevel(c.getInt(c.getColumnIndex("level")));
			g.setCategory(c.getString(c.getColumnIndex("category")));
			g.setJsonData(c.getString(c.getColumnIndex("jsonData")));
			g.setScore(c.getInt(c.getColumnIndex("score")));
			g.setDate(c.getString(c.getColumnIndex("date")));
			g.setGamename(c.getString(c.getColumnIndex("gamename")));
			g.setAuthor(c.getString(c.getColumnIndex("author")));
			g.setWidth(c.getInt(c.getColumnIndex("width")));
			g.setHeight(c.getInt(c.getColumnIndex("height")));*/
			String jsonData = c.getString(c.getColumnIndex("jsonData"));
			g = jsonUtil.parseGridJson(jsonData);
		}

		c.close();
		db.close();
		return g;
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
		Cursor c = queryTheCursor();
		while(c.moveToNext()){
			GridforSaved g = new GridforSaved();
			g.setFilename(c.getString(c.getColumnIndex("file")));
			g.setUniqueid(c.getInt(c.getColumnIndex("uniqueid")));
			g.setVol(c.getInt(c.getColumnIndex("volNumber")));
			g.setLevel(c.getInt(c.getColumnIndex("level")));
			g.setDegree(c.getInt(c.getColumnIndex("degree")));
			g.setCategory(c.getString(c.getColumnIndex("category")));
			g.setIslocked(c.getInt(c.getColumnIndex("islocked")));
			//g.setStar(c.getInt(c.getColumnIndex("star")));
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
	 * ��ѯgrid�������grid������һ��ָ��
	 * @return
	 */
	public Cursor queryTheCursor(){
		//db = helper.getWritableDatabase();
		Cursor c = db.rawQuery("SELECT * FROM "+Crossword.TABLE_NAME, null);
		//db.close();
		return c;
	}
	
	/**
	 * ��ѯ���д�����ֵ�ļ�¼
	 *
	 */
	public Cursor queryCursorByKey(String key,Object value){
		//db = helper.getWritableDatabase();
		String[] columns = {"file","uniqueid","volNumber","level","degree","category","islocked",
				//"star",
				"jsonData","score",
				             "date","gamename","author","width","height"};
		String selection = key+"="+value;
		Cursor c = db.query(true, Crossword.TABLE_NAME, columns, selection, null, null, null, null, null);
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
















