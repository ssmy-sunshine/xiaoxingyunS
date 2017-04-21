/*评分点击事件*/
function StarScore(warpObj,score,showIconNo,clickAble,count) {
	this.warpObj = warpObj;
	this.score = score||0; //分数(显示已选中的星星数量)默认0
	this.showIconNo = showIconNo==null ? true : false ; //未选中的星星是否显示 默认true显示
	this.clickAble= clickAble==null ? true : false ;//是否可按 默认true可按
	this.count=count||5; //星星数量
	this.iconNo="icon-xingxing0";//未选中的星星字体名
	this.iconYes="icon-xingxing1";//已选中的星星字体名
	//初始化
	this.init();
}

/*初始化评分控件*/
StarScore.prototype.init=function(){
	var starStr='';
	for (var i = 0; i < this.count; i++) {
		if (i<this.score) {
			//选中的星星
			starStr+='<p i="'+(i+1)+'" class="iconfont icon-xingxing1"></p>';
		} else if(this.showIconNo){
			//未选中的星星
			starStr+='<p i="'+(i+1)+'" class="iconfont icon-xingxing0"></p>';
		}
	}
	this.warpObj.innerHTML=starStr;
	this.clickAble&&this.setClick();
}

/*添加点击事件*/
StarScore.prototype.setClick=function(){
	var self=this;
	var stars=self.warpObj.getElementsByClassName("iconfont");
	for (var i = 0; i < stars.length; i++) {
		stars[i].onclick=function(){
			self.score=Number(this.getAttribute("i"));
			self.init();
		}
	}
}