/*接口域名*/
var isConsole=true;//TODO 是否输出,正式项目需改为false
var Host="http://192.168.1.103:8080/LittleLucky/";//测试项目服务器地址
//var Host="http://192.168.1.103:8080/LittleLucky/";//正式项目服务器地址

/*获取地址参数,传中文需先encodeURIComponent编码,然后decodeURIComponent解码*/
function getUrlParam(e) {
	var d = new RegExp("(^|&)" + e + "=([^&]*)(&|$)", "i");
	var f = window.location.search.substr(1).match(d);
	if (f != null&&f[2]!="null") {
		return decodeURIComponent(f[2]);
	}
	return null;
}

/*获取用户头像*/
function getImgpath(imgpath) {
	if(!imgpath) return "";
	if (imgpath.indexOf("http://")!=-1) {
		return imgpath;
	} else{
		return Host+imgpath;
	}
}

/*打开界面,统一带参数sid*/
function openWindow(url,param){
	var pstr="";
	if(param && typeof param=="object"){
		for(key in param){
			pstr += key + "=" + encodeURIComponent(param[key]) + "&";
		}
	}
	if(url.indexOf("?")!=-1){
		url += "&";
	}else{
		url += "?";
	}
	url += pstr;
	window.location.href=url;
}

/*页面加载完成的事件*/
$(function () {
    //记录第一次进入的界面,返回的时候跳去首页;sessionStorage浏览器打开到关闭的生命周期;
    var keyFirstPage="firstPage";
    if (!sessionStorage.getItem(keyFirstPage)) {
    	sessionStorage.setItem(keyFirstPage,window.location.href);
    }
    //返回按钮
    $(".wb-back").click(function(){
    	var len = history.length;//历史记录列表的总数,只会越多,不会因为返回了而减少
    	if(getUrlParam("isBack")){
    		history.back();
			return false;//避免有的浏览器,返回失效;
    	}else if(window.location.search.indexOf("OpenID")!=-1) {//登录后会带openid
    		if (len<3) {
    			openWindow("../main/main.html");//没得返回了,则去首页
    		} else{
	    		history.go(-3);//如果是登录的界面,需返回三次,才能到原来的界面
    		}
    	}else if(len<=1){
			openWindow("../main/main.html");//第一次进来的界面,点返回跳去首页;
		}else{
			if (window.location.href==sessionStorage.getItem(keyFirstPage)) {
				openWindow("../main/main.html");//第一次进来的界面,点返回跳去首页;
			} else{
				history.back();
				return false;//避免有的浏览器,返回失效;
			}
		}
	});
})

/**
 * 联网
 * url 网络地址
 * success 成功回调function(data)
 * param JSON参数 {key:value,key:value};默认带Uid和TK
 * err 错误回调
 * hideWait 不显示进度条(默认显示)
 */
function ajaxData(url,success,param,err,hideWait) {
	//统一带参
	param=param||{};
	param["Uid"]=UserObj.getUid();
	param["TK"]=UserObj.getTK();
	param["device"] = "weixin";//标记是微信端的请求
	
	/*封装请求,便于重试*/
	function sendAjax(){
		//重试次数,默认3次
		if(!param.tryNum) param.tryNum=0;
		if(param.tryNum>=3) {
			layerUtil.toast("请求超时,请重试");
			return;
		}
		param.tryNum++;
		//显示进度条 默认显示hideWait==null或false
		if(!hideWait) layerUtil.showWaiting();
		//联网请求
		$.ajax({
			url: url,
			data:param,
			type:'post',
			dataType:'text',//服务器返回的类型:文本
			timeout:10000,//10秒超时
			success:function(dataStr){
				//{"Code":200,"Msg":{"no":633930},"SysTime":1492264267709}
				isConsole&&console.log("请求url--> " + url + " 参数--> " + JSON.stringify(param) + " 结果-->" + dataStr);
				//关闭进度条
				if(!hideWait) layerUtil.closeWaiting();
				//处理请求结果
				var data=JSON.parse(dataStr);
				if(data.Code==200){
					//请求成功回调
					success&&success(data.Msg,data.SysTime);
				}else if(data.Code==5003){
					//token过期 自动登录刷token
					if(!window.isGetTK){
						//一个界面只许刷一次token,避免多个请求同时刷token导致死循环
						window.isGetTK=true;
						UserObj.login(null,null,function() {
							//登录成功更新TK
							param.TK=UserObj.getTK();;
							sendAjax();//登录成功继续请求
						},function (){
							window.isGetTK=false;
						});
					}else{
						//延时3秒刷新TK后,重新请求
						setTimeout(function(){
							//isGetTK=true;其他请求已刷过TK;则更新TK,继续请求,重新请求3次
							param.TK=UserObj.getTK();;
							sendAjax();
						},3000)
					}
				}else{
					//请求异常
					var noToastErr=err&&err(data);//错误回调 返回true则不提示异常
					if (noToastErr!=true) {
						layerUtil.toast(data.Msg);
					}
				}
			},
			error:function (xhr) {
				isConsole&&console.log("请求url--> " + url + " 参数--> " + JSON.stringify(param)+" 异常--> status=" + xhr.status+";statusText="+xhr.statusText);
				//关闭进度条
				if(!hideWait) layerUtil.closeWaiting();
				//请求失败 特殊状态重新请求3次
				if(xhr.status==406||xhr.status==0){
					//延时1秒
					setTimeout(function(){
						sendAjax();
					},1000)
				}else{
					//错误回调 返回true则不提示异常
					var noToastErr=err&&err();
					if (noToastErr!=true) {
						layerUtil.toast("网速不给力,请重试."+xhr.status);
					}
				}
			}
		});
	}
	//发送请求
	sendAjax();
}


/*用户信息对象*/
var UserObj={
	/*获取本地缓存的用户Uid*/
	getUid : function() {
		var Uid=localStorage.getItem("Uid");
		if(!Uid){
			Uid=UserObj.getRandomNum(32);
			UserObj.setUid(Uid);
		}
		return Uid;
	},
	setUid : function(Uid){
		setLocalStorage("Uid",Uid);
	},
	getRandomNum : function(n) {
	    var res = "";
	    var chars = ['0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'];
	    for(var i = 0; i < n ; i ++) {
	        var id = Math.ceil(Math.random()*35);
	        res += chars[id];
	    }
	    return res;
	},
	/*获取本地缓存的用户手机号*/
	getTel : function() {
		return localStorage.getItem("Mnum");
	},
	setTel : function(tel){
		setLocalStorage("Mnum",tel);
	},
	/*获取本地缓存的用户密码Md5*/
	getPassword : function() {
		return localStorage.getItem("pwMd5");
	},
	setPassword : function(pwMd5){
		setLocalStorage("pwMd5",pwMd5);
	},
	/*获取用户头像,如果传imgpath则返回拼接的头像地址,如果不传则返回当前用户的头像地址*/
	getIcon : function(imgpath) {
		if(imgpath){
			return getImgpath(imgpath);
		}else{
			return localStorage.getItem("UserIcon")||"";
		}
	},
	setIcon : function(imgpath){
		setLocalStorage("UserIcon",UserObj.getIcon(imgpath));
	},
	/*获取用户昵称*/
	getNickname : function() {
		return localStorage.getItem("UNickName");
	},
	setNickname : function(nickname) {
		setLocalStorage("UNickName",nickname);
	},
	/*获取用户测试身份: 0普通用户,1测试人员;2开发人员;*/
	getTestTag : function() {
		return localStorage.getItem("USER_ISTEST");
	},
	setTestTag : function(type) {
		setLocalStorage("USER_ISTEST",type);
	},
	/*获取用户登录的token*/
	getTK : function() {
		return localStorage.getItem("TK");
	},
	setTK : function(token) {
		setLocalStorage("TK",token);
	},
	/*获取用户是否登录 isToLogin默认跳转登录*/
	isLogin : function(isToLogin) {
		if (UserObj.getUid()&&UserObj.getTK()) {
			return true;
		} else{
			if(isToLogin!=false) openWindow("../account/login.html");
			return false;
		}
	},
	/*账号密码登录*/
	login : function(tel,pwMd5,success,err,hideWait){
		tel = tel || UserObj.getTel();//手机号
		pwMd5 = pwMd5 || UserObj.getPassword();//密码Md5
		if (tel&&pwMd5) {
			var param={tel:tel,pass:pwMd5};
			ajaxData(Host+"DataInterface/WBAPP/WBAPPLogin.ashx", function(data) {
					//Y§2§7C68D95F90ACCB8E150BB55E18A3BA93
					//保存账户信息
					UserObj.setUid(data[1]);
					UserObj.setTK(data[2]);
					UserObj.setTel(tel);
					UserObj.setPassword(pwMd5);
					//登录成功回调
					success&&success();
			},param,function(e) {
				//登录失败回调
				err&&err(e);
			},hideWait,false);
		}else{
			//如果没有传账号密码,则去登录页
			openWindow("../account/login.html");
		}
	},
	/*获取用户信息*/
	getUserinfo : function(callback){
		if(!UserObj.isLogin(false)){
			callback&&callback();
			return;
		}
//		var param={"IFID":12,"PR":[[],[],[],[],[],[]]};
//		ajaxData(HostDataInV2, function(dataArr){
//			//{"T0":[{"UNickName":"xjkjh","3782":"/Images/WBimg/usericon-max.png","3780":"xjkjh的店铺","3785":0.00,"3789":116,"3784":0,"ShopImg":"/Images/WBimg/ShopHead.jpg","ShopID":174741,"RegisState":1,"IsTestUser":0,"UserType":"闪店主"}],"T1":[{"todayProfit":0.00}],"T2":[{"todayOrder":0}],"T3":[{"jdlNum":0}],"T4":[{"jrtjNum":0}],"T5":[{"yqsNum":0}]}
//			var data=JSON.parse(dataArr[2]);//设置用户信息
//			var user=data["T0"][0];
//			//先缓存变量,其他界面公用
//			UserObj.setIcon(user["3782"]);//头像
//			UserObj.setNickname(user.UNickName);//用户名
//			UserObj.setLevelName(user.UserType);//会员级别名称
//			UserObj.setTestTag(user.IsTestUser);//是否为测试人员,在updateBiz.js用到
//			//回调
//			callback&&callback(true);
//		},param,function(){
//			callback&&callback();
//		},true);
	}
}

/*设置localStorage,如果value不存在则移除*/
function setLocalStorage(key,value){
	if(value){
		localStorage.setItem(key,value);
	}else{
		localStorage.removeItem(key);
	}
}

/*layerUtil*/
var layerUtil={
	index:0,
	/*显示进度条*/
	showWaiting:function(){
		if(layerUtil.index==0) layerUtil.index=layer.open({type: 2,shade: false});
	},
	/*关闭进度条*/
	closeWaiting:function(){
		layer.close(layerUtil.index);
		layerUtil.index=0;
	},
	/*提示*/
	toast:function(msg){
	  layer.open({content:msg,skin: 'msg',time: 2});
	}
}

/*加载图片
 *imgObj_id 图片dom对象或者id
 * 代码拼接的需此格式:
 *<img src="../img/loading-rect.png" data-src="网络地址" onload="loadimg(this)"/>
 * 直接调用则为:loadimg(imgDom,src);
 */
function loadimg(imgObj_id,src,callback){
	var imgDom = (typeof imgObj_id == "object") ? imgObj_id : document.getElementById(imgObj_id);
	if (imgDom) {
		var temp = new Image();
		temp.onload = function() {
			imgDom.onload = null;
			if (callback) {
				callback(imgDom,src);
			} else{
				imgDom.src = temp.src;
				imgDom.classList.add("anim_opacity"); //渐变动画
			}
		};
		temp.src = src||imgDom.getAttribute("data-src");
	}
}

/*给指定元素添加拨打电话的功能*/
function addTelHref(domId,tel){
	var domTel=document.getElementById(domId);
	if(domTel){
		domTel.addEventListener("click",function () {
			//如果tel没值,则默认平台的客服电话
			window.location.href="tel:"+(tel||"4008871881");
		})
	}
}

/*数据为空的提示;
 *显示new EmptyBox().show();
 *隐藏new EmptyBox().hide();
 <div class="wb-empty-box">
	<img class="wb-empty-icon" src="../img/loading-sq.png"/>
	<p class="wb-empty-tip">提示内容</p>
	<p id="wb-empty-btn" class="wb-empty-btn">按钮</p>
</div>
 */
function EmptyBox(tip,btntext,btnCallback,src) {
	this.tip = tip==null ? "亲,暂无相关数据~" : tip;//默认提示:"亲,暂无相关数据~"
	this.btntext=btntext||"";
	this.btnCallback=btnCallback;
	this.src=src||"../img/loading-sq.png";
}
EmptyBox.prototype.show=function(id_obj,top) {
	var box = document.createElement("div");
	box.setAttribute("class", "wb-empty-box");
	box.innerHTML='<img class="wb-empty-icon" src="'+this.src+'"/><p class="wb-empty-tip">'+this.tip+'</p><p id="wb-empty-btn" class="wb-empty-btn">'+this.btntext+'</p>';
	if (id_obj) {
		var parent = typeof id_obj == "object" ? id_obj : document.getElementById(id_obj);
		parent.innerHTML="";
		parent.appendChild(box);
	} else{
		document.body.appendChild(box);
	}
	if(top) box.style.paddingTop=top;//默认是60px
	if (this.btntext) {//按钮
		var btn=document.getElementById("wb-empty-btn");
		if (btn) {
			btn.style.display="block";
			btn.onclick=this.btnCallback;
		};
	}
}
EmptyBox.prototype.hide=function() {
	var boxs=document.getElementsByClassName("wb-empty-box");
	for (var i = 0; i < boxs.length; i++) {
		boxs[i].parentNode.removeChild(boxs[i]);
	}
}

/*遮罩
*标题wb-head 9910
*底部wb-footer 9900
*遮罩wb-shadow 9920
new Shadow(function() {
	//点击遮罩的事件
	//关闭遮罩
	this.hide();
},9905).show()
 */
function Shadow(click,zIndex) {
	this.zIndex=zIndex;
	this.click=click;
}
Shadow.prototype.show=function() {
	if (document.getElementsByClassName("wb-shadow").length == 0) {
		var shadow = document.createElement("div");
		shadow.setAttribute("class", "wb-shadow");
		document.body.appendChild(shadow);
		if (this.zIndex) shadow.style.zIndex = this.zIndex;
		var self=this;
		shadow.onclick=function() {
			typeof self.click == "function" && self.click();
		}
	}
}
Shadow.prototype.hide=function() {
	var shadow=document.getElementsByClassName("wb-shadow");
	for (var i = 0; i < shadow.length; i++) {
		document.body.removeChild(shadow[0]);
	}
}

/*时间转换
 *type=1; 2013年11月06日 16:05:50
 *type=2; 2013年11月06日
 *type=3; 2013-11-06
 *type=4; 2013.11.06
 *type不传; 2013-11-06 16:05:50
 */
Date.prototype.formats = function(type) {
	var year = this.getFullYear();
	var month = this.getMonth() + 1;
	var d = this.getDate();
	var h = this.getHours();
	var m = this.getMinutes();
	var s = this.getSeconds();
	var add0 = function(num) {
		if (num < 10) {
			num = "0" + num;
		}
		return num;
	}
	if (type == 1) {
		return year + "年" + add0(month) + "月" + add0(d) + "日 " + add0(h) + ":" + add0(m) + ":" + add0(s);
	} else if (type == 2) {
		return year + "年" + add0(month) + "月" + add0(d) + "日";
	} else if(type == 3){
		return year + "-" + add0(month) + "-" + add0(d);
	} else if(type == 4){
		return year + "." + add0(month) + "." + add0(d);
	} else {
		return year + "-" + add0(month) + "-" + add0(d) + " " + add0(h) + ":" + add0(m) + ":" + add0(s);
	}
}
/*字符串时间转毫秒*/
function dateToMsec(str) {
	if(!str){
		return new Date().getTime();
	}else if(str.indexOf("Date")!=-1){
		// "\/Date(1476082500000+0800)\/"---->毫秒1476082500800 //dateStr = Date(1476082500000+0800)
		str.replace(/Date\([\d+]+\)/, function(dateStr) { eval('dateObj = new '+dateStr) });
		return dateObj.getTime();
	}else{
		// "2014/07/10 10:21:13"---->毫秒1404958873000
		return new Date(str).getTime();
	}
}
/*计算两个时间字符串相差的毫秒 (date1,date2时间字符串:2014/07/10 10:21:13)*/
function getDateDiff(date1,date2) {
	return dateToMsec(date1)-dateToMsec(date2);
}

/*导航切换,按钮变化
 *callback(i,dom)//点击导航按钮的回调,重复点击不回调,i当前点击的下标,this当前点击的dom元素
 *curTab 默认显示那个导航
 */
function initTabClick(parent,child,callback,curTab){
	var curTab=curTab||0;
	$(parent+" "+child).each(function(i,dom) {
		dom.setAttribute("i",i);
		//默认第一个变红
		if(i==curTab) dom.classList.add("tab-active");
	}).click(function() {
		//按钮变红
		var index=Number(this.getAttribute("i"));
		if (curTab!=index) {
			$(parent+" .tab-active").removeClass("tab-active");
			this.classList.add("tab-active");
			//执行回调
			callback&&callback(index,this,curTab);
			//标记
			curTab=index;
		}
	})
}

/*加入回到顶部的按钮,bottom离底部的距离,单位像素*/
function addTopBtn(bottom){
	var totopCls=document.getElementsByClassName("wb-totop");
	//未加入按钮,则加入
	if (totopCls.length==0) {
		var toTopBtn = document.createElement("div");
		toTopBtn.setAttribute("class", "iconfont icon-dingbu wb-totop");
		if(bottom) toTopBtn.style.bottom=bottom+"px";
		document.body.appendChild(toTopBtn);
		toTopBtn.onclick=function(){
			 $('body').animate({scrollTop: 0},300);
		}
	}
}

/*扩展数组方法:删除指定元素*/
Array.prototype.remove = function(val) {
    var index = this.indexOf(val);
    while(index>-1){
        this.splice(index, 1);
        index = this.indexOf(val);
    }
}