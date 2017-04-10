package wx.biz;

import java.util.ArrayList;

import wx.db.DBBusiness;
import wx.db.DBCurScore;
import wx.db.DBWait;
import wx.entity.App;
import wx.entity.CurScore;
import wx.entity.Wait;

/**
 * 点击菜单事件
 */
public class MsgClickBiz {
	
	/**
	 * 处理点击事件
	 * @param weChatID 商家开发者微信号
	 * @param key 微信按钮ID
	 * @param openID 用户openID
	 */
	public static String doClickEven(String weChatID,String key,String openID) throws Exception{
		String resultStr="";
		
		if ("exscore".equals(key)) {
			//兑换积分
			CurScore mCurScore=DBCurScore.getCurScore(weChatID, openID);
			resultStr=VipBusBiz.addExScore(mCurScore);
			
		}else if ("curscore".equals(key)) {
			//查询剩余积分
			resultStr=getCurScore(weChatID,openID);
			
		}else if ("enuse".equals(key)) {
			//查询未兑换的记录
			resultStr=VipBusBiz.getNoUseScore(weChatID,openID);
			
		}else if ("used".equals(key)) {
			//查询已兑换的记录
			resultStr=getExScore(weChatID,openID);
			
		}else if ("wait".equals(key)) {
			//查询排队等座情况
			resultStr=getWaitDetail(openID);
		}
		
		return resultStr;
	}
	
	/**
	 * 查询剩余积分
	 */
	public static String getCurScore(String weChatID,String openID) throws Exception{
		String resMsg;
		if (App.WeChatID==weChatID) {
			resMsg=XmgBusBiz.getCurScore(openID);
		} else {
			resMsg=VipBusBiz.getCurScore(weChatID,openID);
		}
		return resMsg;
	}
	
	/**
	 * 已兑换的明细
	 */
	public static String getExScore(String weChatID,String openID) throws Exception {
		String resMsg;
		if (App.WeChatID==weChatID) {
			resMsg=XmgBusBiz.getExScore(openID);
		} else {
			resMsg=VipBusBiz.getExScore(weChatID,openID);
		}
		return resMsg;
	}
	
	/**
	 * 查询排队等座情况
	 */
	public static String getWaitDetail(String openID) throws Exception{
		String waitDetail="";
		ArrayList<Wait> waitList=DBWait.getWaitList(openID);
		if (waitList.size()==0) {
			waitDetail="您今天没有等待的座位";
		}
		for (Wait wait : waitList) {
			//今天 在当前商家,当前用户前面还有多少人等位
			int waitCount=DBWait.getWaitCount(wait.BusNo,wait.WaitNo);
			String BusName=DBBusiness.getBusName(wait.BusNo);
			if (waitCount==0) {
				if (wait.TimeEnd==0&&!wait.OutDate) {
					//1.没入座也没过期 则轮到你入座
					waitDetail+=BusName+" 排位"+wait.WaitNo+"号\n恭喜,已轮到您入座,请准备.";
				}else if (wait.TimeEnd==0&&wait.OutDate) {
					//2没入座但过期了 则后面的人先入座了
					waitDetail+=BusName+" 排位"+wait.WaitNo+"号\n抱歉,您没能按时入座,排号已失效.";
				}else{
					//3入座了也过期了 则已经入座
					waitDetail+=BusName+" 排位"+wait.WaitNo+"号\n您已经入座了.";
				}
			}else{
				waitDetail+=BusName+" 排位"+wait.WaitNo+"号\n您前面还有"+waitCount+"个人在排队.";
			}
			if (waitList.size()>=2) {
				waitDetail+="\n---------------------\n";
			}
		}
		return waitDetail;
	}

}
