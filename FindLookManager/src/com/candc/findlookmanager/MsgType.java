package com.candc.findlookmanager;


/**
 * 作者: Stonekity(596017443@qq.com)
 * 
 * 时间: 2014年10月10日 上午02:05
 * 
 * 描述: 消息定义
 */
public class MsgType {
	
	
	
	
	/**
	 * 消息标识－Json解析完成
	 */
	public static final int MSG_PARSE_JSON_FINISHED = 0;
	
	/**
	 * 消息标识－图片加载完成
	 */
	public static final int MSG_LOAD_BITMAP_FINISHED = 100;	
	
	/**
	 * 消息标识－检查Bmob中是否存在该商品操作完成
	 */
	public static final int MSG_CHECK_IS_BMOB_HAVE_THE_GOOD_FINISHED = 200;
	
	/**
	 * 消息标识－上传图片完成
	 */
	public static final int MSG_UPLOAD_BITMAP_FINISHED = 300;
	
	/**
	 * 消息标识－Bmob数据库中已经存在当前商品
	 */
	public static final int MSG_BMOB_DID_HAVE_THE_GOOD = 400;
	
	/**
	 * 消息标识－Bmob数据库中不存在当前商品
	 */
	public static final int MSG_BMOB_DID_NOT_HAVE_THE_GOOD = 500;

}
