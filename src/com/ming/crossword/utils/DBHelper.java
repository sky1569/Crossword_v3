package com.ming.crossword.utils;

import com.crossword.Crossword;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{


	//CursorFactory����Ϊnull
	public DBHelper(Context context){
		super(context, Crossword.DATABASE_NAME, null, Crossword.DATABASE_VERSION);
	}
	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	//���ݿ��һ�α�����ʱonCreate�ᱻ����
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE IF NOT EXISTS "+Crossword.TABLE_NAME+
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT,"+Crossword.GRIDITEM+")");
	}
   //���DARABASE_VERSIONֵ����Ϊ2��ϵͳ���������ݿ�汾��ͬ���������onUpgrade
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("ALERT TABLE"+Crossword.TABLE_NAME +"ADD COLUMN onter STRING");
		
	}

}
