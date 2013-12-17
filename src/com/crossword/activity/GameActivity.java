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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;

import org.xml.sax.helpers.DefaultHandler;
import com.crossword.Crossword;
import com.crossword.R;
import com.crossword.keyboard.KeyboardView;
import com.crossword.keyboard.KeyboardViewInterface;
import com.crossword.utils.Module;
import com.crossword.adapter.GameGridAdapter;
import com.crossword.data.Grid;
import com.crossword.data.Word;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends CrosswordParentActivity implements OnTouchListener, KeyboardViewInterface {

	public enum GRID_MODE {NORMAL, CHECK, SOLVE};
	public GRID_MODE currentMode = GRID_MODE.NORMAL;
	
	private GridView 		gridView;
	private KeyboardView 	keyboardView;
	private GameGridAdapter gridAdapter;
	private TextView 		txtDescription;
	private Module			module;
	private TextView        txtDescriptionHor;
	private TextView        txtDescriptionVer;
	private TextView 		keyboardOverlay;
 
	private Grid			grid;
	private LinkedList<Word> entries;		// Liste des mots
	private ArrayList<View>	selectedArea = new ArrayList<View>(); // Liste des cases selectionn茅es

	private boolean			downIsPlayable;	// false si le joueur 脿 appuy茅 sur une case noire 
	private int 			downPos;		// Position ou le joueur 脿 appuy茅
    private int 			downX;			// Ligne ou le joueur 脿 appuy茅
    private int 			downY;			// Colonne ou le joueur 脿 appuy茅
    private int				lastX;          //上一次按下的位置X
    private int             lastY;          //上一次按下的位置Y
	private int 			currentPos;		// Position actuelle du curseur
	private int 			currentX;		// Colonne actuelle du curseur
	private int 			currentY;		// Ligne actuelle du curseur
	private Word			currentWord;	// Mot actuellement selectionn茅
	private Word            currentWordHor;
	private Word            currentWordVer;
	private boolean 		horizontal;		// Sens de la selection
    private boolean 		isCross;        //判断是否是交叉点
	private String 			filename;		// Nom de la grille
    private String          url;            //下载地址
	private boolean 		solidSelection;	// PREFERENCES: Selection persistante
	private boolean			gridIsLower;	// PREFERENCES: Grille en minuscule
	
	private int width;
	private int height;

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.crossword, menu);
        return true;
    }

	@Override
	public void onPause()
	{
		module.save(this.gridAdapter,this.grid);
		super.onPause();
	}
	
	@Override
	public void onStop(){
		module.save(this.gridAdapter,this.grid);
		super.onStop();
	}
	public void onCreate(Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.game);
	    module = new Module(this);

	    //this.filename = "td.json";
	  //  this.url = Crossword.GRID_URL + 10002;
	  //  module.parseGrid(this, this.url);
	    //通过uniqueid查找grid，如果没有就会从网页下载
	    this.grid = module.queryByUniqueid(10002);

	    if (this.grid == null) {
	    	finish();
	    	return;
	    }

	    this.entries= this.grid.getEntries();
	    if (this.entries == null) {
	    	finish();
	    	return;
	    }
	
	    this.width = this.grid.getWidth();
	    this.height = this.grid.getHeight();
        this.lastX = -1;
        this.lastY = -1;
	    Display display = getWindowManager().getDefaultDisplay();
        int height = display.getHeight();
        int keyboardHeight = (int)(height / 4.4);
        this.txtDescriptionHor = (TextView)findViewById(R.id.description_horizotal);
        this.txtDescriptionVer = (TextView)findViewById(R.id.description_vertical);
        this.gridView = (GridView)findViewById(R.id.grid);
        this.gridView.setOnTouchListener(this);
        this.gridView.setNumColumns(this.width);
        android.view.ViewGroup.LayoutParams gridParams = this.gridView.getLayoutParams();
        gridParams.height = height - keyboardHeight - this.txtDescriptionHor.getLayoutParams().height;
        this.gridView.setLayoutParams(gridParams);
        this.gridView.setVerticalScrollBarEnabled(false);
		this.gridAdapter = new GameGridAdapter(this, this.entries, this.width, this.height,module);
		this.gridView.setAdapter(this.gridAdapter);

        this.keyboardView = (KeyboardView)findViewById(R.id.keyboard);
        this.keyboardView.setDelegate(this);
        android.view.ViewGroup.LayoutParams KeyboardParams = this.keyboardView.getLayoutParams();
        KeyboardParams.height = keyboardHeight;
        this.keyboardView.setLayoutParams(KeyboardParams);

        this.keyboardOverlay = (TextView)findViewById(R.id.keyboard_overlay);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            {
            	int position = this.gridView.pointToPosition((int)event.getX(), (int)event.getY());
            	View child = this.gridView.getChildAt(position);
                
            	// Si pas de mot sur cette case (= case noire), aucun traitement
            	if (child == null || child.getTag().equals(Crossword.AREA_BLOCK)) {
            		if (this.solidSelection == false) {
                        clearSelection();
                    	this.gridAdapter.notifyDataSetChanged();
            		}
            			
            		this.downIsPlayable = false;
            		return true;
            	}
        		this.downIsPlayable = true;

            	// Stocke les coordonnees d'appuie sur l'ecran
            	this.downPos = position;
                this.downX = this.downPos % this.width;
                this.downY = this.downPos / this.width;
                System.out.println("ACTION_DOWN, x:" + this.downX + ", y:" + this.downY + ", position: " + this.downPos);
                clearSelection();
            	this.gridAdapter.notifyDataSetChanged();
        		break;
            }

            case MotionEvent.ACTION_UP:
            {
            	// Si le joueur 脿 appuy茅 sur une case noire, aucun traitement 
            	if (this.downIsPlayable == false)
            		return true;
            	
                int position = this.gridView.pointToPosition((int)event.getX(), (int)event.getY());
                int x = position % this.width;
                int y = position / this.width;
                System.out.println("ACTION_DOWN, x:" + x + ", y:" + y + ", position: " + position);
                //判断输入方向
                if(x < 0 || x >= this.width || y < 0 || y>= this.height)
                	return false;
                this.horizontal = (lastY == y && Math.abs(lastX - x)>0 || (lastX == -1 && lastY == -1))?true:false;
                this.horizontal = (lastX == x && Math.abs(lastY - y)>0)?false:true;
                this.lastX = x;//获取上一次的横向位置
                this.lastY = y;//获取上一次的纵向位置
                this.currentX = x;
            	this.currentY = y;
            	this.isCross = this.module.isCross(currentX, currentY);
            	currentWord = module.getCorrectWord(currentX,currentY,this.horizontal);
              
        	    if (this.currentWord == null)
        	    	break;
        	    this.horizontal = this.currentWord.getHoriz();
        	  //在设置背景之前先重绘一遍
        		this.gridAdapter.reDrawGridBackground(this.gridView);
                if(isCross){
                	
                	this.currentWordHor = module.getCorrectWord(x, y, true);
                	this.currentWordVer = module.getCorrectWord(x, y, false);
                	this.setWordBackground(this.currentWordHor, x, y);
                	this.setWordBackground(this.currentWordVer, x, y);
                }else{
                	this.setWordBackground(currentWord, x, y);
                }
                
                
                this.setDescription(currentWordHor, currentWordVer, currentWord);
        	    this.gridAdapter.notifyDataSetChanged();
        	    break;
            }
        }
        // if you return false, these actions will not be recorded
        return true;
	}
	
	// Remet les anciennes case selectionnees dans leur etat normal
    private void clearSelection() {
    	for (View selected: selectedArea)
    		selected.setBackgroundResource(R.drawable.area_empty);
    	selectedArea.clear();
	}

	@Override
	public void onKeyDown(String value, int location[], int width) {
		System.out.println("onKeyDown: " + value + ", insert in: " + currentX + "x" + currentY);

		// Deplace l'overlay du clavier
		if (value.equals(Crossword.UNFILLED) == false) {
			int offsetX = (this.keyboardOverlay.getWidth() - width) / 2;
			int offsetY = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Crossword.KEYBOARD_OVERLAY_OFFSET, getResources().getDisplayMetrics());
			FrameLayout.LayoutParams lp = (LayoutParams)this.keyboardOverlay.getLayoutParams();
			lp.leftMargin = location[0] - offsetX;
			lp.topMargin = location[1] - offsetY;
			this.keyboardOverlay.setLayoutParams(lp);
			this.keyboardOverlay.setText(value);
			this.keyboardOverlay.clearAnimation();
			this.keyboardOverlay.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onKeyUp(String value) {
		System.out.println("onKeyUp: " + value + ", insert in: " + currentX + "x" + currentY);
		/*if(value.equals("replay"))
			{
				this.module.replay();
				this.gridAdapter.notifyDataSetChanged();
				return;
			}*/
		// Efface l'overlay du clavier
		if (value.equals(Crossword.UNFILLED) == false) {
			this.keyboardOverlay.setAnimation(AnimationUtils.loadAnimation(this, R.anim.keyboard_overlay_fade_out));
			this.keyboardOverlay.setVisibility(View.INVISIBLE);
		}

		// Si aucun mot selectionne, retour
		if (this.currentWord == null)
			return;

		// Case actuelle
		int x = this.currentX;
		int y = this.currentY;
        
		// Si la case est noire => retour
		if (this.module.isBlock(x, y))
			return;
		String areaValue=this.module.getareaValue(x, y);
	
		this.module.setValue(x, y, value);
		this.module.setDisValue(x, y,value);
		this.gridAdapter.notifyDataSetChanged();
		
		module.toChinese(x,y,this.currentWord);
		
		if(module.isACh(this)) return;
		
		if (value.equals(Crossword.UNFILLED)) {
			
			if(areaValue.equals(Crossword.UNFILLED))
			{
				x = (this.horizontal ? x - 1 : x);
				y = (this.horizontal ? y: y - 1);
			}
			
		}

		if(!value.equals(Crossword.UNFILLED))
		{
			x = (this.horizontal ? x + 1 : x);
			y = (this.horizontal ? y: y + 1);
		}
		
		// Si la case suivante est disponible, met la case en jaune, remet l'ancienne en bleu, et set la nouvelle position
		if (x >= 0 && x < this.width
				&& y >= 0 && y < this.height
				&& this.module.isBlock(x,y) == false) {
			this.currentX = x;
			this.currentY = y;
		}
		
		
		this.isCross = this.module.isCross(currentX, currentY);
       
		currentWord = module.getCorrectWord(currentX,currentY,this.horizontal);
		  
          
          this.horizontal = this.currentWord.getHoriz();
        //在设置背景之前先重绘一遍
        this.gridAdapter.reDrawGridBackground(this.gridView);
        if(this.isCross){
        	
        	this.currentWordHor = module.getCorrectWord(currentX, currentY, true);
        	this.currentWordVer = module.getCorrectWord(currentX, currentY, false);
        	
        	this.setWordBackground(this.currentWordHor, currentX, currentY);
        	this.setWordBackground(this.currentWordVer, currentX, currentY);
        }else{
        	this.setWordBackground(currentWord, currentX, currentY);
        }
        
        
        this.setDescription(currentWordHor, currentWordVer, currentWord);
        
		
	}
	
	
	
	
	

	
	
  
	
	//设置横向或纵向词对应的小格背景
	public void setWordBackground(Word word,int currX,int currY){
		
		int x = word.getX();
		int y = word.getY();
		boolean horizontal = word.getHoriz();
		int currIndex = currY*this.width + currX;
	
		for(int l = 0;l<word.getLength();l++){
			int index = y*this.width + x + l*(horizontal?1:this.width);
			View currentChild = this.gridView.getChildAt(index);
			if(currentChild != null){
				currentChild.setBackgroundResource(index == currIndex?R.drawable.area_current:R.drawable.area_selected);
			    selectedArea.add(currentChild);
			}
		}
	}
	
	
	
	//设置描述信息
	public void setDescription(Word currentWordHor,Word currentWordVer,Word currentWord){//设置提示信息
		  String descriptionHor = isCross?"横向:"+this.currentWordHor.getDesc():
              (this.horizontal?"横向:"+currentWord.getDesc():"");
          String descriptionVer = isCross?"纵向:"+this.currentWordVer.getDesc():
              (this.horizontal?"":"纵向:"+currentWord.getDesc());

          this.txtDescriptionHor.setText(descriptionHor);
          this.txtDescriptionVer.setText(descriptionVer);
		 
	}

	
}
