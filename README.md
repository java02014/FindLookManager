FindLookManager
===============

通过扫描商品条形码，从联图网查询到商品的信息，本地修改数据后，上传到Bmob服务器


![github](https://github.com/Stonekity/FindLookManager/blob/master/Screenshot1.png)
![github](https://github.com/Stonekity/FindLookManager/blob/master/Screenshot2.png)


商品查询API调用
---------------

    联图网调用API: 
        "http://www.liantu.com/tiaoma/query.php"
    
    请求参数:
         ean   商品条形码编号
    
    返回Json数据:
    "ean"         商品条形码
    "name"        空
    "titleSrc"    商品名称(返回的是图片url，不是文本,需要特殊处理)
    "guobie"      商品国别
    "place"       商品产地
    "price"       参考价格
    "supplier"    供应商名称
    "sort_id"     商品类别
    "faccode"     生产厂商代码
    "fac_name"    生产厂商名称
    "fac_status"  注册状态
  

案例说明
--------

      Get请求 :  http://www.liantu.com/tiaoma/query.php?ean=6924862100033
      Response:  {"ean":"6924862100033","name":"","titleSrc":"http:\/\/www.liantu.com\
      /tiaoma\/eantitle.php?title=KzRwWk9PUThiNkhSNmpWVDlxTDRVbDd5UG5qczV5UGc=","guobie":
      "\u4e2d\u56fd","place":"\u4e2d\u56fd\u6b66\u6c49\u5e02\u4e1c\u897f\u6e56\u533a\u4e03
      \u652f\u6c9f","price":2.5,"supplier":"\u6b66\u6c49\u767e\u4e8b\u53ef\u4e50\u996e\u6599
      \u6709\u9650\u516c\u53f8","sort_id":7,"faccode":"69248621","fac_name":"\u6b66\u6c49\u767e
      \u4e8b\u53ef\u4e50\u996e\u6599\u6709\u9650\u516c\u53f8","fac_status":"\u6709\u6548"}
     
      Json解析后结果如上图"百事可乐"所示
      
  
  
