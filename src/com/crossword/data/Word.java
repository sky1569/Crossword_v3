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

package com.crossword.data;

import java.util.LinkedList;

public class Word {
	/*
	private int		x;
	private int		y;
	private int		length;
	private String	tmp;
	private String	text;
	private String	description;
	private String  answer;
	private boolean	horizontal = true;
	*/
	

	private String              desc;//һ����ʾ
	private String              desc2;//������ʾ

	private String              tmp;
	private boolean             horiz;
	private Integer             x;
	private Integer             y;
	private Integer             len;
	private String              chi;
	private String              cap;
	private String              mask;


	private LinkedList<Character> entities = new LinkedList<Character>();
	private int                 index;//�ʵı�־
	
	
	//���úͻ�ȡһ����ʾ

	public void     setDesc(String desc){this.desc = desc;}
	public String   getDesc(){return this.desc;}
	
	//���úͻ�ȡ������ʾ
	public void     setDesc2(String desc2){this.desc2 = desc2;}
	public String   getDesc2(){return this.desc2;}
	
	public void     setTmp(String tmp){this.tmp = tmp;}
	public String   getTmp(){return this.tmp;}
	
	public void     setMask(String mask){this.mask = mask;}
	public String   getMask(){return this.mask;}
	
	public void     setChi(String chi){this.chi = chi;}//���ú���
	public String   getChi(){return this.chi;}//��ȡ����
	
	public void     setCap(String cap){this.cap = cap;}//����ƴ��
	public String   getCap(){return this.cap;}//��ȡƴ��
	
	public void     setHoriz(boolean horiz){this.horiz = horiz;}
	public boolean  getHoriz(){return this.horiz;}
	
	public void     setX(int x){this.x = x;}
	public int      getX(){return this.x;}
	public int      getXMax(){return this.horiz?this.x + this.len - 1:this.x;}
	
	public void     setY(int y){this.y = y;}
	public int      getY(){return this.y;}
	public int      getYMax(){return this.horiz?this.y:this.y + this.len - 1;}
	
	public void     setLength(int len){this.len = len;}
	public int      getLength(){return this.len;}
	
	public String   getAns(int l){return this.chi.substring(l,l+1);}
	

	//���ô��е����飬���ظ������ߵ�
	public void setEntities(LinkedList<Character> entities)
	{
		this.entities = entities;
		
	}
	
	//��ȡ���е�����
	public LinkedList<Character> getEntities(){return this.entities;}
	
	
	//���ôʵı�־
	public void setIndex(int sign){this.index = sign;}
	
	//��ȡ�ʵı�־
	public int getIndex(){return this.index;}
	

}
