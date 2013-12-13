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

	public static final String 	GRID_URL = "http://isshun.github.com/Crossword/Grids/%s";
	//public static final String	GRID_LOCAL_PATH = "/data/data/com.crossword/grid/%s";
	public static final String	GRID_LOCAL_PATH = "/sdcard/crossword/data/grid/%s";
	public static final String	GRID_DIRECTORY = "/data/data/crossword/data/grid";
   /**/ public static final String  GRID_LOCAL_PATH1 = "/sdcard/crossword/data/%s";
	public static final String 	GRIDLIST_URL = "http://isshun.github.com/Crossword/Grids.xml";
	public static final String	GRIDLIST_LOCAL_PATH = "/data/data/com.crossword/gridlist.xml";
	public static final long 	GRIDLIST_LIFE_TIME = 86400000;
	
	public static final String 	MAIL_URL = "http://mail.alexislauper.com/";
	public static final String 	FEEDBACK_URL = "http://isshun.github.com/Crossword/Feedback";
	public static final int		REQUEST_PREFERENCES = 2;
	public static final float 	KEYBOARD_OVERLAY_OFFSET = 90;
	public static final String 	NAME = "Crossword";
	public static final int 	NOTIFICATION_DOWNLOAD_ID = 1;
	public static boolean 		DEBUG;
	
	public static final String          TMPSIGN = "#";
	public static final String          BLANK="-";
	public static final int 			AREA_BLOCK = -1;
	public static final int 			AREA_WRITABLE = 0;
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