package com.candc.findlookmanager;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 作者: Stonekity(596017443@qq.com)
 * 
 * 时间: 2014年10月28日
 * 
 * 描述: 商品实体类
 */
@SuppressWarnings("serial")
public class Good extends BmobObject {

	private String goodCode;	//商品条形码
	private String goodName;	//商品名称
	private String goodPrice;	//商品参考价格
	private String goodPlace;	//商品产地
	private String goodNation;	//商品国别
	private String comCode;		//厂商代码
	private String comName;		//厂商名称
	private String regState;	//注册状态
	
	private String type = "";
	private String name = "";
	private String description = "";
	private String value = "";
	private String url = "";
	private BmobFile picture;
	
	
	public String getGoodCode() {
		return goodCode;
	}

	public void setGoodCode(String goodCode) {
		this.goodCode = goodCode;
	}

	public String getGoodName() {
		return goodName;
	}

	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}

	public String getGoodPrice() {
		return goodPrice;
	}

	public void setGoodPrice(String goodPrice) {
		this.goodPrice = goodPrice;
	}

	public String getGoodPlace() {
		return goodPlace;
	}

	public void setGoodPlace(String goodPlace) {
		this.goodPlace = goodPlace;
	}

	public String getGoodNation() {
		return goodNation;
	}

	public void setGoodNation(String goodNation) {
		this.goodNation = goodNation;
	}

	public String getComCode() {
		return comCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	public String getComName() {
		return comName;
	}

	public void setComName(String comName) {
		this.comName = comName;
	}

	public String getRegState() {
		return regState;
	}

	public void setRegState(String regState) {
		this.regState = regState;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public BmobFile getPicture() {
		return picture;
	}

	public void setPicture(BmobFile picture) {
		this.picture = picture;
	}
	
}
