/**
 * Summary: 网络请求层封装
 * Version 1.0
 * Author: zhaomi@jugame.com.cn
 * Company: muji.com
 * Date: 13-11-5
 * Time: 下午12:38
 * Copyright: Copyright (c) 2013
 */

package com.candc.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.protocol.HTTP;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class NetService {
	
	public static String fetchHtml(String url) throws Exception {
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response = HttpClientWrapper.getHttpClient().execute(
				httpGet);
		if (response != null) {
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				InputStream is = response.getEntity().getContent();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] bytes = new byte[4096];
				int lenRead;
				while ((lenRead = is.read(bytes)) != -1) {
					if (lenRead > 0) {
						baos.write(bytes, 0, lenRead);
					}
				}
				if (baos.size() > 0) {
					return new String(baos.toByteArray(), HTTP.UTF_8);
				}
			} else {
				android.util.Log.w("NetService",
						"response code not correct-------------->"
								+ response.getStatusLine().getStatusCode());
			}
		} else {
			android.util.Log.w("NetService", "response null");
		}
		return null;
	}
	
	
	//----------------------------------------------By Stonekity-------------------------------------------
	
	/**
	* 从服务器取图片
	* @param url
	* @return
	*/
	public static Bitmap getHttpBitmap(String url) {
	     URL myFileUrl = null;
	     Bitmap bitmap = null;
	     try {
	          myFileUrl = new URL(url);
	     } catch (MalformedURLException e) {
	          e.printStackTrace();
	     }
	     try {
	          HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
	          conn.setConnectTimeout(0);
	          conn.setDoInput(true);
	          conn.connect();
	          InputStream is = conn.getInputStream();
	          bitmap = BitmapFactory.decodeStream(is);
	          is.close();
	     } catch (IOException e) {
	          e.printStackTrace();
	     }
	     return big(bitmap);
	}
	
	/**
	 * Bitmap 放大和缩小
	 * @param bitmap
	 * @return
	 */
	private static Bitmap big(Bitmap bitmap) {
		  Matrix matrix = new Matrix(); 
		  matrix.postScale(1.8f,1.8f); //长和宽放大缩小的比例
		  Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
		  return resizeBmp;
	}
	
	//----------------------------------------------By Stonekity-------------------------------------------

}