/*拼接券html字符串
<div class="tk-warp tkbg1">
	<img class="tkicon" src="../img/logo.png"/>
	<span class="tkname">5积分</span>
	<p class="tktip-left">10积分=1元</p>
	<p class="tktip-right">全场通用</p>
</div>
<div class="tk-warp tkbg2">
	<img class="tkicon" src="../img/logo.png"/>
	<span class="tkname">5元代金券</span>
	<p class="tktip-left">全场通用</p>
	<p class="tktip-right">有效期至: 2015.04.06</p>
</div>
 * */

/*积分*/
function getScoreStr(score){
	var str='';
	str+='<div class="tk-warp tkbg1">';
	str+='<img class="tkicon" src="../img/logo.png"/>';
	str+='<span class="tkname">'+score+'积分</span>';
	str+='<p class="tktip-left">10积分=1元</p>';
	str+='<p class="tktip-right">全场通用</p>';
	str+='</div>';
	return str;
}

/*优惠券*/
function getTicketStr(ticket){
	var str='';
	str+='<div class="tk-warp tkbg2">';
	str+='<img class="tkicon" src="../img/logo.png"/>';
	str+='<span class="tkname">'+ticket+'元代金券</span>';
	str+='<p class="tktip-left">全场通用</p>';
	str+='<p class="tktip-right">有效期至: 2015.04.06</p>';
	str+='</div>';
	return str;
}

/*裂变红包*/
function getMoreBagStr(morepass,profit){
	var str='';
	str+='<div class="morepass tk-warp tkbg2" morepass="'+morepass+'">';
	str+='<img class="tkicon" src="../img/logo.png"/>';
	str+='<span class="tkname">裂变红包 立即邀请好友来抢 ></span>';
	str+='<p class="tktip-left">好友每抢一个红包,您将获得'+profit+'元奖励</p>';
	str+='<p class="tktip-right">数量有限</p>';
	str+='</div>';
	return str;
}