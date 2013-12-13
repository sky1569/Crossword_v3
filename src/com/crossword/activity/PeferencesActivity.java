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

package com.crossword.activity;

import java.io.File;

import com.crossword.Crossword;
import com.crossword.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;
import android.widget.Toast;

public class PeferencesActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		Preference btFeedback = (Preference)findPreference("feedback");
		btFeedback.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				startActivity(new Intent(PeferencesActivity.this, FeedbackActivity.class));
				return true;
			}
		});

		
		Preference btClearCache = (Preference)findPreference("clear_cache");
		btClearCache.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				new AlertDialog.Builder(PeferencesActivity.this).setMessage(R.string.preferences_clear_cache_warning)
					.setTitle(R.string.preferences_clear_cache_title)
				    .setCancelable(false)
				    .setPositiveButton(R.string.clear, new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int id) {
				        	clearCache();
				        }
				    })
				    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				    	public void onClick(DialogInterface dialog, int id) {
				    		dialog.cancel();
				    	}
				    }).create().show();
				return true;
			}
		});
	}
	
	protected void clearCache() {
	    File directoryToScan = new File(Crossword.GRID_DIRECTORY); 
	    File files[] = directoryToScan.listFiles();
	    for (File file: files)
	    	file.delete();
	    File gridlist = new File(Crossword.GRIDLIST_LOCAL_PATH);
	    if (gridlist.exists())
	    	gridlist.delete();
		Toast.makeText(this, R.string.preferences_clear_cache_toast, Toast.LENGTH_SHORT).show();
		finish();
	}

}
