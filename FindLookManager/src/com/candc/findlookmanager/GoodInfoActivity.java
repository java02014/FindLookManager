package com.candc.findlookmanager;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.candc.service.NetService;

/**
 * 作者: Stonekity(596017443@qq.com)
 * 
 * 时间: 2014年10月10日 上午02:05
 * 
 * 描述: FindLook主列表界面
 */
public class GoodInfoActivity extends ImageSelectHelperActivity implements
		OnClickListener {

	private static final String TAG = "FindLookActivity";

	// 获取商品信息API
	private static final String URl_BASE = "http://www.liantu.com/tiaoma/query.php";

	private Good good = null;
	private GoodsHolder goodsHolder = null;

	// 条形码扫描结果
	private String result = "";
	// 网络请求返回的Json数据
	private String response = "";
	// 得到的BitMap
	private Bitmap bitmap = null;

	// 判断"添加商品"按钮是否被点击
	private Boolean isClicked = false;

	// 判断Bmob数据库中是否存在当前商品
	private Boolean isBmobDatabaseHaveTheGood = false;
	
	// 加载对话框
	private ProgressDialog progress = null;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			
			//Bmob中存在当前商品
			if(msg.what == MsgType.MSG_BMOB_DID_HAVE_THE_GOOD) {
				//从Bmob中获取商品数据
				reloadView();
			}
			
			//Bmob中不存在当前商品
			if(msg.what == MsgType.MSG_BMOB_DID_NOT_HAVE_THE_GOOD) {
				//从API中获取商品数据
				queryGoodInfoFromAPI(result);
			}

			// Json解析完成
			if (msg.what == MsgType.MSG_PARSE_JSON_FINISHED) {

				// 开始获取商品对应的名称(是图片， 不是文字)
				loadGoodNameBitmap();
				Log.i(TAG, response);
			}

			//商品名称图片加载完成
			if (msg.what == MsgType.MSG_LOAD_BITMAP_FINISHED) {
				progress.dismiss();

				// 刷新UI，适配数据
				reloadView();
			}
			
			//检查Bmob数据库中是否已经存在该商品的操作完成
			if(msg.what == MsgType.MSG_CHECK_IS_BMOB_HAVE_THE_GOOD_FINISHED) {
				if(!isBmobDatabaseHaveTheGood)
					uploadImageToBmob();
				else {
					toast("商品已经存在");
				}
			}
			
			//商品图片上传完成
			if(msg.what == MsgType.MSG_UPLOAD_BITMAP_FINISHED) {
				saveGoodInfo();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_look);

		initView();

		// 查询商品信息
		isBmobDatabaseHaveTheGood(result);
	}

	private void initView() {
		// 获取到扫描的结果
		result = getIntent().getExtras().getString("scan_res");
		// result = "6954674001235";

		progress = new ProgressDialog(this);
		progress.setCanceledOnTouchOutside(false);

		good = new Good();
		goodsHolder = new GoodsHolder();

		goodsHolder.tvGoodCode = (TextView) findViewById(R.id.tv_good_code);
		goodsHolder.tvGoodName = (EditText) findViewById(R.id.tv_good_name);
		goodsHolder.tvGoodPrice = (EditText) findViewById(R.id.tv_good_price);
		goodsHolder.tvGoodPlace = (EditText) findViewById(R.id.tv_good_place);
		goodsHolder.tvGoodNation = (EditText) findViewById(R.id.tv_good_nation);
		goodsHolder.tvComCode = (EditText) findViewById(R.id.tv_com_code);
		goodsHolder.tvComName = (EditText) findViewById(R.id.tv_com_name);
		goodsHolder.tvRegState = (EditText) findViewById(R.id.tv_reg_state);

		// --------适配老数据
		goodsHolder.tvName = (EditText) findViewById(R.id.tv_good_name_old);
		goodsHolder.tvDescription = (EditText) findViewById(R.id.tv_good_info_old);
		goodsHolder.tvValue = (EditText) findViewById(R.id.tv_good_value_old);
		goodsHolder.imgPicture = (ImageView) findViewById(R.id.ivImageSelected);
		// --------适配老数据

		goodsHolder.imgGoodName = (ImageView) findViewById(R.id.img_good_name);
		goodsHolder.btnGoodAdd = (Button) findViewById(R.id.btn_good_add);
		goodsHolder.btnGoodAdd.setOnClickListener(this);

		findViewById(R.id.button1).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						setImageSizeBoundary(400); // optional. default is 500.
						setCropOption(1, 1); // optional. default is no crop.
						// setCustomButtons(btnGallery, btnCamera, btnCancel);
						// // you can set these buttons.
						startSelectImage();
					}
				});

		getSelectedImageFile(); // extract selected & saved image file.

	}

	/**
	 * 加载商品信息， 刷新UI
	 */
	private void reloadView() {
		goodsHolder.tvGoodCode.setText(good.getGoodCode());
		goodsHolder.tvGoodName.setText(good.getGoodName());
		if (!good.getGoodPrice().equals("")) {
			//从API解析出的商品价格不含单位
			if(isBmobDatabaseHaveTheGood){
				goodsHolder.tvGoodPrice.setText(good.getGoodPrice());
			}else {
				goodsHolder.tvGoodPrice.setText("¥" + good.getGoodPrice() + "元");
			}
			goodsHolder.tvGoodPrice.setEnabled(false);
		} else {
			goodsHolder.tvGoodPrice.setHint("暂无");
			goodsHolder.tvGoodPrice.setEnabled(true);
		}

		if (!good.getGoodPlace().equals("")) {
			goodsHolder.tvGoodPlace.setText(good.getGoodPlace());
			goodsHolder.tvGoodPlace.setEnabled(false);
		} else {
			goodsHolder.tvGoodPlace.setHint("暂无");
			goodsHolder.tvGoodPlace.setEnabled(true);
		}

		if (!good.getGoodNation().equals("")) {
			goodsHolder.tvGoodNation.setText(good.getGoodNation());
			goodsHolder.tvGoodNation.setEnabled(false);
		} else {
			goodsHolder.tvGoodNation.setHint("暂无");
			goodsHolder.tvGoodNation.setEnabled(true);
		}

		if (!good.getComCode().equals("")) {
			goodsHolder.tvComCode.setText(good.getComCode());
			goodsHolder.tvComCode.setEnabled(false);
		} else {
			goodsHolder.tvComCode.setHint("暂无");
			goodsHolder.tvComCode.setEnabled(true);
		}

		if (!good.getComName().equals("")) {
			goodsHolder.tvComName.setText(good.getComName());
			goodsHolder.tvComName.setEnabled(false);
		} else {
			goodsHolder.tvComName.setHint("暂无");
			goodsHolder.tvComName.setEnabled(true);
		}

		if (!good.getRegState().equals("")) {
			goodsHolder.tvRegState.setText(good.getRegState());
			goodsHolder.tvRegState.setEnabled(false);

			goodsHolder.imgGoodName.setImageBitmap(bitmap);
			goodsHolder.imgGoodName.setVisibility(View.VISIBLE);
			goodsHolder.tvGoodName.setEnabled(false);
			goodsHolder.tvGoodName.setVisibility(View.GONE);
		} else {
			goodsHolder.tvRegState.setHint("暂无");
			goodsHolder.tvRegState.setEnabled(true);

			goodsHolder.imgGoodName.setImageBitmap(bitmap);
			goodsHolder.imgGoodName.setVisibility(View.GONE);
			goodsHolder.tvComName.setHint("商品名称");
			goodsHolder.tvGoodName.setText("");
			goodsHolder.tvGoodName.setEnabled(true);
			goodsHolder.tvGoodName.setVisibility(View.VISIBLE);
		}
		
		if (!good.getName().equals("")) {
			goodsHolder.tvName.setText(good.getName());
		} else {
			goodsHolder.tvName.setHint("暂无");
		}
		
		if(!good.getDescription().equals("")) {
			goodsHolder.tvDescription.setText(good.getDescription());
		} else {
			goodsHolder.tvDescription.setHint("暂无");
		}
		
		if(!good.getValue().equals("")) {
			goodsHolder.tvValue.setText(good.getValue());
		} else {
			goodsHolder.tvValue.setHint("暂无");
		}
			
		ImageView imageView = (ImageView) findViewById(R.id.ivImageSelected);
		//加载缩略图
		if (! (good.getPicture()==null) ) {
			good.getPicture().loadImageThumbnail(this, imageView, 100, 100, 100);
		} else {
			imageView.setBackgroundResource(R.drawable.ic_app);
		}

	}

	/**
	 * 查询对应条形码的商品信息
	 * @param code
	 */
	private void queryGoodInfoFromAPI(String code) {
		
		// API对应的URL， 使用get请求方式
		final String url = URl_BASE + "?ean=" + code;
		progress.show();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// 向服务器发出请求
					response = NetService.fetchHtml(url);

					// 开始解析获取到的Json数据， 得到一个Good对象
					parseJsonToObject(response);

					// 发送消息， 解析完成
					Message msg = new Message();
					msg.what = MsgType.MSG_PARSE_JSON_FINISHED;
					mHandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	
	/**
	 * 因为API中商品名称是图片， 不是文字， 没有办法解析， 所以只有将图片下载下来
	 */
	private void loadGoodNameBitmap() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// 向服务器请求获取到对应的图片
				bitmap = NetService.getHttpBitmap(good.getGoodName());

				// 发出消息， 图片加载完成
				Message msg = new Message();
				msg.what = MsgType.MSG_LOAD_BITMAP_FINISHED;
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	/**
	 * 解析服务器返回的Json数据
	 * 
	 * @param jsonString
	 * @throws JSONException
	 */
	private void parseJsonToObject(String jsonString) {
		try {
			JSONObject object = new JSONObject(jsonString);
			if (object.has("ean"))
				good.setGoodCode(object.getString("ean"));
			else
				good.setGoodCode("");

			if (object.has("titleSrc"))
				good.setGoodName(object.getString("titleSrc"));
			else
				good.setGoodName("");

			if (object.has("price"))
				good.setGoodPrice(object.getString("price"));
			else
				good.setGoodPrice("");

			if (object.has("supplier"))
				good.setGoodPlace(object.getString("supplier"));
			else
				good.setGoodPlace("");

			if (object.has("guobie"))
				good.setGoodNation(object.getString("guobie"));
			else
				good.setGoodNation("");

			if (object.has("faccode"))
				good.setComCode(object.getString("faccode"));
			else
				good.setComCode("");

			if (object.has("fac_name"))
				good.setComName(object.getString("fac_name"));
			else
				good.setComName("");

			if (object.has("fac_status"))
				good.setRegState(object.getString("fac_status"));
			else
				good.setRegState("");

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 检查商品信息是否完善, 检查的范围只包含自定义的属性， 扫描出来的属性不检查
	 * 
	 * @return
	 */
	private Boolean checkIsInfoCompleted() {
		if (goodsHolder.tvName.getText().equals("")
				|| goodsHolder.tvDescription.getText().equals("")
				|| goodsHolder.tvValue.getText().equals("")) {
			toast("请先完善下方商品信息后，重新添加");
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 检查Bmob数据库中是否存在当前商品
	 * @param goodCode
	 */
	private void isBmobDatabaseHaveTheGood(String goodCode) {
		progress.show();
		BmobQuery<Good> query = new BmobQuery<Good>();
		query.addWhereEqualTo("goodCode", goodCode);
		query.findObjects(this, new FindListener<Good>() {

			@Override
			public void onSuccess(List<Good> arg0) {
				if (arg0.size() > 0) {
					isBmobDatabaseHaveTheGood = true;
					//从Bmob数据库中获取到当前的对象
					if(arg0.get(0)!=null) {
						good = arg0.get(0);
					}
					
					//Bmob数据库中的确存在当前商品
					Message msg = new Message();
					msg.what = MsgType.MSG_BMOB_DID_HAVE_THE_GOOD;
					mHandler.sendMessage(msg);
				} else {
					isBmobDatabaseHaveTheGood = false;
					//Bmob数据库中不存在当前商品
					Message msg = new Message();
					msg.what = MsgType.MSG_BMOB_DID_NOT_HAVE_THE_GOOD;
					mHandler.sendMessage(msg);
				}
				progress.dismiss();
			}

			@Override
			public void onError(int arg0, String arg1) {
				toast("查询Bmob商品库失败");
				progress.dismiss();
			}
		});
		
	}

	/**
	 * 将商品信息保存到Bmob数据库中
	 * 
	 * @return
	 */
	private void saveGoodInfo() {
		progress.show();
		// 对商品信息进行检查是否为空
		if (checkIsInfoCompleted()) {
			good.setGoodCode(goodsHolder.tvGoodCode.getText().toString());
			good.setGoodName(goodsHolder.tvGoodName.getText().toString());
			good.setGoodPrice(goodsHolder.tvGoodPrice.getText().toString());
			good.setGoodPlace(goodsHolder.tvGoodPlace.getText().toString());
			good.setGoodNation(goodsHolder.tvGoodNation.getText().toString());
			good.setComCode(goodsHolder.tvComCode.getText().toString());
			good.setComName(goodsHolder.tvComName.getText().toString());
			good.setRegState(goodsHolder.tvRegState.getText().toString());

			good.setType("XX");
			good.setUrl("http://www.meituan.com");
			good.setName(goodsHolder.tvName.getText().toString());
			good.setDescription(goodsHolder.tvDescription.getText().toString());
			good.setValue(goodsHolder.tvValue.getText().toString());
			good.save(this, new SaveListener() {

				@Override
				public void onSuccess() {
					toast("添加成功");
					progress.dismiss();
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					toast("添加失败");
					progress.dismiss();
				}
			});
		} else {
			// do nothing 
		}
	}

	private void uploadImageToBmob() {
		progress.show();
		final BmobFile picture = new BmobFile(getSelectedImageFile());
		picture.uploadblock(this, new UploadFileListener() {

			@Override
			public void onSuccess() {
				good.setPicture(picture);
				Message msg = new Message();
				msg.what = MsgType.MSG_UPLOAD_BITMAP_FINISHED;
				mHandler.sendMessage(msg);
				toast("图片上传成功");
				progress.dismiss();
			}

			@Override
			public void onProgress(Integer value) {
				toast("已经上传 " + value + "%");
			}

			@Override
			public void onFailure(int code, String msg) {
				toast("上传文件失败：" + msg);
				progress.dismiss();
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (good.getGoodCode().equals("")) {
			toast("商品不存在，无法添加");
			return;
		}
		if (isClicked) {
			toast("请不要重复添加");
			return;
		}
		
		//判断当前Bmob数据库中是否存在该商品
		if(!isBmobDatabaseHaveTheGood) {
			Message msg = new Message();
			msg.what = MsgType.MSG_CHECK_IS_BMOB_HAVE_THE_GOOD_FINISHED;
			mHandler.sendMessage(msg);
		} else {
			toast("商品已经存在，请勿重复添加");
		}
		isClicked = true;
	}

	public void toast(String toast) {
		Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
	}

}
