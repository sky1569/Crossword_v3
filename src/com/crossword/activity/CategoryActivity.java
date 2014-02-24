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

import com.crossword.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class CategoryActivity extends Activity {
	
	private ImageButton returnButton;
	private LinearLayout movieCategoryLayout;
	private LinearLayout verseCategoryLayout;
	private LinearLayout popCategoryLayout;
	private LinearLayout randomCategoryLayout;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.category);
	    returnButton = (ImageButton)findViewById(R.id.category_return_button);
	    movieCategoryLayout = (LinearLayout)findViewById(R.id.movie_category_layout);
	    verseCategoryLayout = (LinearLayout)findViewById(R.id.verses_category_layout);
	    popCategoryLayout = (LinearLayout)findViewById(R.id.pop_category_layout);
	    randomCategoryLayout = (LinearLayout)findViewById(R.id.random_category_layout);
	    returnButton.setOnClickListener(new OnClickListener(){
       
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CategoryActivity.this.finish();
			}
	    	
	    });
	    

        movieCategoryLayout.setOnTouchListener(categoryTouchListener);
        verseCategoryLayout.setOnTouchListener(categoryTouchListener);
        popCategoryLayout.setOnTouchListener(categoryTouchListener);
        randomCategoryLayout.setOnTouchListener(categoryTouchListener);
	    
	
	}
	
	
    OnTouchListener categoryTouchListener = new OnTouchListener(){

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub

			//Ô¤Áô
			
			String category = "";
			Intent intent = new Intent(CategoryActivity.this,CategoryGridListActivity.class);
			

			
			if(event.getAction() == MotionEvent.ACTION_UP){
				
				switch(v.getId()){
				   case R.id.movie_category_layout:
					   category = getResources().getString(R.string.movie);
					   break;
				   case R.id.verses_category_layout:
					   category = getResources().getString(R.string.verses);
					   break;
				   case R.id.pop_category_layout:
					   category = getResources().getString(R.string.pop);
					   break;
				   case R.id.random_category_layout:
					   category = getResources().getString(R.string.random);
					   break;
				
				}		
				
				intent.putExtra("category", category);
				startActivity(intent);
				
			}
			
			
	
			
			
			
			/*if(event.getAction() == MotionEvent.ACTION_DOWN){
			Intent intent = new Intent(CategoryActivity.this,HistoryActivity.class);
			startActivity(intent);
			}*/
			return true;
		}
   	
   };
}
