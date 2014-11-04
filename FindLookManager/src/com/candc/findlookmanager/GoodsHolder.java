package com.candc.findlookmanager;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 作者: Stonekity(596017443@qq.com)
 * 
 * 时间: 2014年10月28日
 * 
 * 描述: 商品列表接收器
 */
public class GoodsHolder {

	public LinearLayout llGoodPrice;
	public LinearLayout llGoodPlace;
	public LinearLayout llGoodNation;
	public LinearLayout llComCode;
	public LinearLayout llComName;
	public LinearLayout llRegState;
	
	public TextView tvGoodCode;
	public EditText tvGoodName;
	public EditText tvGoodPrice;
	public EditText tvGoodPlace;
	public EditText tvGoodNation;
	public EditText tvComCode;
	public EditText tvComName;
	public EditText tvRegState;
	
	//没有考虑type和url两个字段
	public EditText tvName;
	public EditText tvDescription;
	public EditText tvValue;
	public ImageView imgPicture;
	
	public ImageView imgGoodName;
	
	public Button btnGoodAdd;

}
