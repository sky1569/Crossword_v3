/*
 * Copyright 2011 Alexis Lauper <alexis.lauper@gmail.com>
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of 
 * the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.crossword;

import java.io.File;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;

public class Crossword extends Application {


	public static final String      GRID_URL = "http://10.105.223.24/CrossWordPuzzlePHP/playboard.php?";
	public static final String      LOGIN_ROOT_URL = "http://10.105.223.24/CrossWordPuzzlePHP/login.php?";
	public static final String      REGISTER_ROOT_URL = "http://10.105.223.24/CrossWordPuzzlePHP/regist.php?";
	public static final String      UPLOAD_SCORE_ROOT_URL = "http://10.105.223.24/CrossWordPuzzlePHP/sendscore.php?";
	public static final String      UPLOAD_OFFLINE_ROOT_SCORE = "http://10.105.223.24/CrossWordPuzzlePHP/offlinesendscore.php?";
	public static final String      RANK_ROOT_URL = "http://10.105.223.24/CrossWordPuzzlePHP/rank.php?";
	public static final String      OFFLINE_RANK_ROOT_URL = "http://10.105.223.24/CrossWordPuzzlePHP/offlinerank.php?";
    public static final String      BROADCAST_URL = "http://10.105.223.24/CrossWordPuzzlePHP/broadcast.php";
	
	public static final String		GRID_DIRECTORY = "/data/data/crossword/data/grid";
  
	public static final String		GRIDLIST_LOCAL_PATH = "/data/data/com.crossword/gridlist.xml";
	public static final long 		GRIDLIST_LIFE_TIME = 86400000;
	public static final String      VOL_REQUEST_URL = "http://10.105.223.24/CrossWordPuzzlePHP/overview.php";//获取全部期数的URL
	public static final String 		MAIL_URL = "http://mail.alexislauper.com/";
	//public static final String 		FEEDBACK_URL = "http://isshun.github.com/Crossword/Feedback";
	
	public static final int         GRIDLOCKED = 1;
	public static final int         GRIDUNLOCKED = 0;
	public static final int 		GAMEMODEVOL = 0;
	public static final int   	 	GAMEMODELIVE =1;
	public static final int         GAMEMODEBREAK =2;
	public static final int			REQUEST_PREFERENCES = 2;
	public static final float 		KEYBOARD_OVERLAY_OFFSET = 90;
	public static final String	 	NAME = "Crossword";
	public static final int 		NOTIFICATION_DOWNLOAD_ID = 1;
	public static boolean 			DEBUG;
	//public static String[][]		area;			// Tableau repr茅sentant les lettres du joueur
	//public static String[][] 		displayArea;
	//public static String[][] 		correctionArea; // Tableau repr茅sentant les lettres correctes
	public static final String      UNFILLED = "-";
	public static final String      BLANK=" ";
	public static final String      BLOCK="#";
	public static final int 		AREA_BLOCK = -1;
	public static final int 		AREA_WRITABLE = 0;
	public static final String      COMPLETETIP="通关了，你这么厉害，你家里人知道么";
	//得分常量
	public static final int      HINT_PENALTY = 5;
	public static final int      SCORE_PER_CHARACTER = 3;
	public static final int      WORD_ERROR_PENALTY = 1 ;
	
	//定义数据库的一些项
	public static final String DATABASE_NAME = "crossword.db";
	public static final int DATABASE_VERSION = 1;
	public static final String GRID_TABLE = "Grid";
	public static final String VOL_TABLE = "vol_table";
	public static final String GRIDITEM= " file VARCHAR ,uniqueid INTEGER," +
			"volNumber INTEGER,level INTEGER,degree INTEGER,category VARCHAR,islocked INTGER," +
			"star INTGER," +
			"jsonData TEXT,score INTEGER," +
			" date VARCHAR,gamename VARCHAR,author VARCHAR,width INTEGER,height INTEGER";
	public static final String VOLITEM = "name VARCHAR,open_date VARCHAR,amount_of_levels INTEGER," +
			                              "vol_no INTEGER,score INTEGER";
	
	public static final String[] columnsOfGridTable = {"file","uniqueid","volNumber","level","degree","category","islocked",
			"star",
			"jsonData","score", "date","gamename","author","width","height"};
	public static final String[] columnsOfVolTable = {"name","open_date","amount_of_levels","vol_no","score"};
   // public static final String  BLOCK = "#";
   // public static final String  BLANK = " ";
    private static Context context;

    public void onCreate() {
        Crossword.context = getApplicationContext();
        
        Crossword.DEBUG = (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;

		File directory = new File(GRID_DIRECTORY);
		if (directory.exists() == false)
			directory.mkdir();
    }

    public static Context getAppContext() {
    	return Crossword.context;
    }
}