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

package com.crossword.keyboard;

import com.crossword.Crossword;
import com.crossword.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

public class KeyboardView extends LinearLayout implements OnTouchListener {

	private KeyboardViewInterface	delegate;
	private View 					currentView;
	private String 					value;
	
	/** Constructeur
	 * 
	 * @param context
	 */
	public KeyboardView(Context context) {
		super(context);
		this.initComponent();
	}
	
	/** Constructeur
	 * 
	 * @param context
	 * @param attrs
	 */
	public KeyboardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initComponent();
	}

	/** Initialisation des composants
	 */
	public void initComponent() {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		ViewGroup keyboard = (ViewGroup) inflater.inflate(R.layout.keyboard, null);
		int size = LinearLayout.LayoutParams.FILL_PARENT;
		keyboard.setLayoutParams(new LinearLayout.LayoutParams(size, size));
		this.addView(keyboard);

		this.findViewById(R.id.buttonA).setOnTouchListener(this);
		this.findViewById(R.id.buttonB).setOnTouchListener(this);
		this.findViewById(R.id.buttonC).setOnTouchListener(this);
		this.findViewById(R.id.buttonD).setOnTouchListener(this);
		this.findViewById(R.id.buttonE).setOnTouchListener(this);
		this.findViewById(R.id.buttonF).setOnTouchListener(this);
		this.findViewById(R.id.buttonG).setOnTouchListener(this);
		this.findViewById(R.id.buttonH).setOnTouchListener(this);
		this.findViewById(R.id.buttonI).setOnTouchListener(this);
		this.findViewById(R.id.buttonJ).setOnTouchListener(this);
		this.findViewById(R.id.buttonK).setOnTouchListener(this);
		this.findViewById(R.id.buttonL).setOnTouchListener(this);
		this.findViewById(R.id.buttonM).setOnTouchListener(this);
		this.findViewById(R.id.buttonN).setOnTouchListener(this);
		this.findViewById(R.id.buttonO).setOnTouchListener(this);
		this.findViewById(R.id.buttonP).setOnTouchListener(this);
		this.findViewById(R.id.buttonQ).setOnTouchListener(this);
		this.findViewById(R.id.buttonR).setOnTouchListener(this);
		this.findViewById(R.id.buttonS).setOnTouchListener(this);
		this.findViewById(R.id.buttonT).setOnTouchListener(this);
		this.findViewById(R.id.buttonU).setOnTouchListener(this);
		this.findViewById(R.id.buttonV).setOnTouchListener(this);
		this.findViewById(R.id.buttonW).setOnTouchListener(this);
		this.findViewById(R.id.buttonX).setOnTouchListener(this);
		this.findViewById(R.id.buttonY).setOnTouchListener(this);
		this.findViewById(R.id.buttonZ).setOnTouchListener(this);
		this.findViewById(R.id.buttonDRAFT).setOnTouchListener(this);
		this.findViewById(R.id.buttonDELETE).setOnTouchListener(this);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            {
            	// Get key value
        		switch (v.getId()) {
        		case R.id.buttonA: this.value = "A"; break;
        		case R.id.buttonB: this.value = "B"; break;
        		case R.id.buttonC: this.value = "C"; break;
        		case R.id.buttonD: this.value = "D"; break;
        		case R.id.buttonE: this.value = "E"; break;
        		case R.id.buttonF: this.value = "F"; break;
        		case R.id.buttonG: this.value = "G"; break;
        		case R.id.buttonH: this.value = "H"; break;
        		case R.id.buttonI: this.value = "I"; break;
        		case R.id.buttonJ: this.value = "J"; break;
        		case R.id.buttonK: this.value = "K"; break;
        		case R.id.buttonL: this.value = "L"; break;
        		case R.id.buttonM: this.value = "M"; break;
        		case R.id.buttonN: this.value = "N"; break;
        		case R.id.buttonO: this.value = "O"; break;
        		case R.id.buttonP: this.value = "P"; break;
        		case R.id.buttonQ: this.value = "Q"; break;
        		case R.id.buttonR: this.value = "R"; break;
        		case R.id.buttonS: this.value = "S"; break;
        		case R.id.buttonT: this.value = "T"; break;
        		case R.id.buttonU: this.value = "U"; break;
        		case R.id.buttonV: this.value = "V"; break;
        		case R.id.buttonW: this.value = "W"; break;
        		case R.id.buttonX: this.value = "X"; break;
        		case R.id.buttonY: this.value = "Y"; break;
        		case R.id.buttonZ: this.value = "Z"; break;
        		case R.id.buttonDELETE: this.value = null; break;
        		//case R.id.buttonDRAFT: this.value = null; break;
        		}

        		this.currentView = v;
        		int[] location = new int[2];
        		this.currentView.getLocationOnScreen(location);
        		
        		// Change key background (selector actually doesn't work with KeyboardView)
        		if (v.getId() == R.id.buttonDELETE)
        			this.currentView.setBackgroundResource(R.drawable.btn_keyboard_delete_pressed);
        	/*	else if (v.getId() == R.id.buttonDRAFT)
        			this.currentView.setBackgroundResource(this.isDraft
        					? R.drawable.btn_keyboard_draft_pressed_lock
        							: R.drawable.btn_keyboard_draft_pressed_lock);*/
        		else
        			this.currentView.setBackgroundResource(R.drawable.btn_keyboard_pressed);
        		
            	if (this.value != null)
            		this.delegate.onKeyDown(value, location, this.currentView.getWidth());
        		break;
            }

            case MotionEvent.ACTION_UP:
            {
        		switch (v.getId()) {
        		case R.id.buttonDELETE:
            		this.delegate.onKeyUp(Crossword.BLANK);
        			break;
        	/*	case R.id.buttonDRAFT:
        			this.isDraft = !this.isDraft;
        			this.delegate.setDraft(this.isDraft);
        			break;*/
        		}
        		
        		// Change key background (selector actually doesn't work with KeyboardView)
        		if (v.getId() == R.id.buttonDELETE)
        			this.currentView.setBackgroundResource(R.drawable.btn_keyboard_delete_release);
        	/*	else if (v.getId() == R.id.buttonDRAFT)
        			this.currentView.setBackgroundResource(this.isDraft
        					? R.drawable.btn_keyboard_draft_release_lock
        							: R.drawable.btn_keyboard_draft_release_unlock);*/
        		else
        			this.currentView.setBackgroundResource(R.drawable.btn_keyboard_release);

        		if (this.value != null)
            		this.delegate.onKeyUp(value);
        		break;
            }
        }
        // if you return false, these actions will not be recorded
        return true;
	}
	
	public void setDelegate(KeyboardViewInterface delegate) {
		this.delegate = delegate; 
}
}