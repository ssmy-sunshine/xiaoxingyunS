package wx.biz;

import wx.db.DBBusiness;
import wx.db.DBCurScore;
import wx.db.DBGroup;
import wx.db.DBQrCode;
import wx.db.DBQrScore;
import wx.db.DBWait;
import wx.entity.App;
import wx.entity.CurScore;
import wx.entity.Group;
import wx.entity.QrCode;
import wx.entity.QrScore;
import wx.entity.Wait;
import wx.util.InfoUtil;

/**
 * 事件消息类型
 */
public class MsgEvenBiz {
	
	/**
	 * 扫描带参二维码的事件
	 * @param 二维码带参为 数据库表QrCode的ID
	 */
	public static String doMsgEven(String WeChatID,int QrCodeID,int UserID) throws Exception{
		String resMsg="";
		
		//临时加的服务 一个微信号只能扫一次二维码领取100积分
		if (QrCodeID==99999) {
			//查询是否已领取积分; 查CurScore表 条件BusNo=?,UserID=?
			CurScore mCurScore=DBCurScore.getCurScore(168888, UserID);
			if (mCurScore==null) {
				//如果第一次充值积分 则插入
				mCurScore=new CurScore();
				mCurScore.BusNo=168888;
				mCurScore.BusName="尚品重庆鸡煲";
				mCurScore.CurScore=100;
				mCurScore.UserID=UserID;
				mCurScore.GetScore=true;
				DBCurScore.addCurScore(mCurScore);
				resMsg="欢迎您的大驾光临,么么哒!\n\n恭喜您成功领取100积分\n可抵用10元现金\n\n本店地址:\n凤凰城南门附近\n农夫烤鱼店旁边"
						+ "\n\n100积分真的可以兑换10块啊亲,您没看错呢\n\n本店地址真的就在凤凰城南门附近啊亲,过来看看吧";
			}else if (!mCurScore.GetScore){
				//如果以前充值过 现在扫传单获得的 则更新
				mCurScore.CurScore+=100;
				DBCurScore.UpdateCurScore(mCurScore.CurScore, mCurScore.ID);
				resMsg="欢迎您的大驾光临,么么哒!\n\n恭喜您成功领取100积分\n可抵用10元现金\n\n本店地址:\n凤凰城南门附近\n农夫烤鱼店旁边"
						+ "\n\n100积分真的可以兑换10块啊亲,您没看错呢\n\n本店地址真的就在凤凰城南门附近啊亲,过来看看吧";
			}else{
				//如果已领取 则回复已领取
				resMsg="一个微信号只能领取一次\n亲，您都领过了还领，太不乖啦\n\n可把二维码转赠给亲友哦\n也可使用另外的微信号领取呢"
						+ "\n\n别忘了本店地址真的就在凤凰城南门附近\n100积分真的可以兑换10块啊亲 您没看错呢";
			}
			return resMsg;
		}
		
		
		QrCode mQrCode=DBQrCode.getQrCode(QrCodeID);
		
		/*
		 * 类型
		 * Type=1; 等位码,  Data=BusNo+WaitNo
		 * Type=2; 积分码,  Data=QrScoreID 
		 * Type=3; wife码, Data=BusNo 
		 * Type=4; 团购码,  Data=BusNo+SeatNo
		 * Type=5; 积分兑换码,  Data=BusNo
		 */
		if(mQrCode.Type==1){
			//等位,绑定UserID,返回在当前商家,当前用户前面还有多少人等位
			resMsg=upWaitUser(""+mQrCode.Data,UserID);
		}else if(mQrCode.Type==2){
			//充值返回对应商家的积分
			resMsg=addScore(WeChatID,mQrCode.Data,UserID);
		}else if(mQrCode.Type==3){
			//wife密码
			String Wife=DBBusiness.getWife(mQrCode.Data);
			resMsg="敬上Wife密码:"+Wife;
		}else if(mQrCode.Type==4){
			//提示输入团购券
			resMsg=addGroupBuy(""+mQrCode.Data,UserID);
		}else if(mQrCode.Type==5){
			//扫码积分兑换码
			resMsg=addExScore(WeChatID,mQrCode.Data,UserID);
		}
		
		return "欢迎您的大驾光临,么么哒!\n\n"+resMsg;
	}
	
	/**
	 * 用户扫兑换积分码的事件
	 */
	private static String addExScore(String WeChatID,int BusNo, int UserID) throws Exception {
		String resMsg;
		if (App.WeChatID==WeChatID) {
			resMsg=XmgBusBiz.addExScore(BusNo,UserID);
		} else {
			CurScore mCurScore=DBCurScore.getCurScore(BusNo, UserID);
			resMsg=VipBusBiz.addExScore(mCurScore);
		}
		return resMsg;
	}

	/**
	 * 用户扫描二维码 提示输入团购券
	 * @param Param=BusNo+SeatNo
	 */
	public static String addGroupBuy(String Param,int UserID) throws Exception{
		//1.获取BusNo,SeatNo;
		Group mGroupBuy=new Group();
		mGroupBuy.UserID=UserID;
		mGroupBuy.BusNo=Integer.parseInt(Param.substring(0, 6));
		mGroupBuy.SeatNo=Param.substring(6);
		//2.删除用户没有输入团购密码的数据,防止用户在其他商家扫描但没输劵号;保证用户仅在一个商家输入劵号;
		DBGroup.delete(UserID);
		//3.插入新的数据 绑定BusNo,SeatNo,UserID;
		DBGroup.addGroup(mGroupBuy);
		//4.提示用户:"输入劵号:"
		return "使用美团劵,回复 1\n使用大众点评,回复 2\n其他,回复3";
	}
	
	/**
	 * 用户扫描二维码获得积分
	 * 返回用户在该商家的积分明细
	 */
	public static String addScore(String WeChatID,int QrScoreID,int UserID) throws Exception {
		//获取参数:时间 商家编码 积分数; 没有查到返回null
		QrScore mQrScore=DBQrScore.getQrScore(QrScoreID);
		if (mQrScore==null) {
			return "积分码无法识别";//防止不是经过商家生成的二维码
		}
		if (mQrScore.UserID!=0||mQrScore.ScanTime!=0) {
			return "当前积分码已经被扫描过";//防止二次扫码
		}
		//要返回的当前商家充值明细
		String scoreDetil="";
		if (UserID>0) {
			//查询当前用户 在当前商家最新扫描记录 检查今天是否已经扫过积分
			long ScanTime=DBQrScore.getScanTime(mQrScore.BusNo, UserID);
			if (ScanTime!=0&&InfoUtil.isSameDay(mQrScore.QrTime, ScanTime)) {
				//防止拿其他人的二维码扫 一天在一个商家只能扫一次
				return "一个微信号一天只能在本店领取一次积分,您今天已领取过了\n\n您可把积分码转赠给亲友或使用另外的微信号领取";
			}
			//查询总积分
			CurScore mCurScore=DBCurScore.getCurScore(mQrScore.BusNo, UserID);
			if (mCurScore==null) {
				//说明第一次扫描 则添加
				mCurScore=new CurScore();
				mCurScore.BusNo=mQrScore.BusNo;
				mCurScore.BusName=DBBusiness.getBusName(mQrScore.BusNo);
				mCurScore.UserID=UserID;
				mCurScore.CurScore=mQrScore.QrScore;
				DBCurScore.addCurScore(mCurScore);
			}else{
				//更新
				mCurScore.CurScore+=mQrScore.QrScore;
				DBCurScore.UpdateCurScore(mCurScore);
			}
			//返回的内容
			if (App.WeChatID==WeChatID) {
				scoreDetil="恭喜您在 "+mCurScore.BusName+" 获得"+mQrScore.QrScore+"积分"
						+ "\n目前您在 "+mCurScore.BusName+" 一共有"+mCurScore.CurScore+"积分\n\n下次光临即可兑换现金";
			}else{
				scoreDetil="恭喜,您获得"+mQrScore.QrScore+"积分"
						+ "\n目前您共有"+mCurScore.CurScore+"积分\n\n下次光临本店即可兑换现金";
			}
			//更新QrScore的UserID和ScanTime
			DBQrScore.upScanUser(QrScoreID,UserID);
		}
		return scoreDetil;
	}

	/**
	 * 等位:BusNo+WaitNo 绑定UserID
	 * 返回在当前商家,当前用户前面还有多少人等位
	 */
	public static String upWaitUser(String param, int UserID) throws Exception {
		//排队情况
		String resMsg="";
		//给BusNo+WaitNo绑定UserID
		int BusNo=Integer.parseInt(param.substring(0, 6));
		int WaitNo=Integer.parseInt(param.substring(6));
		DBWait.bandUserID(BusNo,WaitNo,UserID);
		//今天 在当前商家,当前用户前面还有多少人等位
		int waitCount=DBWait.getWaitCount(BusNo,WaitNo);
		String BusName=DBBusiness.getBusName(BusNo);
		if (waitCount==0) {
			//指定商家指定排号的WaitInfo对象
			Wait wait=DBWait.getWaitInfo(BusNo,WaitNo);
			if (wait.TimeEnd==0&&!wait.OutDate) {
				//1.没入座也没过期 则轮到你入座
				resMsg=BusName+" 排位"+WaitNo+"号\n恭喜,已轮到您入座,请准备.";
			}else if (wait.TimeEnd==0&&wait.OutDate) {
				//2没入座但过期了 则后面的人先入座了
				resMsg=BusName+" 排位"+WaitNo+"号\n抱歉,您没能按时入座,排号已失效.";
			}else{
				//3入座了也过期了 则已经入座
				resMsg=BusName+" 排位"+WaitNo+"号\n您已经入座了.";
			}
		}else{
			resMsg=BusName+" 排位"+WaitNo+"号\n您前面还有"+waitCount+"个人在排队.";
		}
		return resMsg;
	}
	
}
