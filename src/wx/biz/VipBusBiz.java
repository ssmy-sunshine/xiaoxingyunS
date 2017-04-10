package wx.biz;

import java.util.ArrayList;

import wx.db.DBCurScore;
import wx.db.DBExScore;
import wx.db.DBUser;
import wx.entity.CurScore;
import wx.entity.ExScore;
import wx.util.InfoUtil;

/**
 * 独立服务号的商家
 */
public class VipBusBiz {

	/**
	 * 查询用户在当前商家的剩余积分
	 */
	public static String getCurScore(String WeChatID,String OpenID) throws Exception {
		return "您的积分余额: "+DBCurScore.getCurScore(WeChatID, OpenID).CurScore;
	}

	/**
	 * 已兑换的明细 TODO 超过5条 跳转界面
	 */
	public static String getExScore(String WeChatID,String OpenID) throws Exception {
		ArrayList<ExScore> ExScoreList=DBExScore.getExScore(WeChatID,OpenID);
		String resMsg="您还没有兑换过积分";
		if (!ExScoreList.isEmpty()) {
			resMsg="已兑换的积分明细:\n------------------------\n";
			for (ExScore mExScore : ExScoreList) {
				//兑换500积分,兑换码5846\n抵现金50元  2014-08-27 22:55
				resMsg+="兑换"+mExScore.ExScore+"积分, 兑换码"+mExScore.ExCode
						+"\n抵现金"+mExScore.ExMoney+"元  "+InfoUtil.getTime(mExScore.ExTime)+"\n------------------------\n";
			}
		}
		return resMsg;
	}

	/**
	 * 查询未兑换的记录
	 */
	public static String getNoUseScore(String WeChatID,String OpenID) throws Exception {
		String resMsg="您没有未兑换的积分记录";
		ExScore unExScore=DBExScore.getNoUseScore(WeChatID,OpenID);
		if (unExScore!=null) {
				resMsg="您申请兑换"+unExScore.ExScore+"积分\n兑换码为 "+unExScore.ExCode
						+"\n\n您在结账时,只需出示兑换码,即可抵用相应现金";
		}
		return resMsg;
	}

	/**
	 * 兑换积分
	 */
	public static String addExScore(CurScore mCurScore) throws Exception {
		//查询用户在本店的剩余积分,积分小于10,则提示兑换不了;
		String resMsg;
		if (mCurScore==null||mCurScore.CurScore==0) {
			resMsg="您在本店的积分为 0\n暂无积分可兑换";
		}else if (mCurScore.CurScore<10) {
			resMsg="您在本店只有 "+mCurScore.CurScore+"积分\n最少要有10积分才可兑换";
		}else{
			//查询用户在本店是否有未输入ExScore或未兑换的记录;
			ExScore unExScore=DBExScore.getUnExScore(mCurScore.BusNo,mCurScore.UserID);
			if (unExScore==null) {
				//删除用户所有未输入和未兑换的记录
				DBExScore.delUnExScore(mCurScore.UserID);
				//查询用户在商家的兑换次数
				int ExCount=DBExScore.getExCount(mCurScore.BusNo,mCurScore.UserID);
				//插入新的记录
				DBExScore.addExScore(mCurScore.BusNo,mCurScore.BusName,mCurScore.UserID,ExCount+1);
				resMsg="您在本店共有 "+mCurScore.CurScore+"积分\n请输入要兑换的积分数...";
			}else if (unExScore.ExCode==0) {
				//如果未输入的 则提示输入积分值..
				resMsg="您在本店共有 "+mCurScore.CurScore+"积分\n请输入要兑换的积分数...";
			} else {
				//如果已经输入过 则提示更新.
				resMsg="您在本店的总积分: "+mCurScore.CurScore
						+"\n\n您之前已申请兑换"+unExScore.ExScore+"积分\n兑换码为 "+unExScore.ExCode
						+"\n您只需在结账时出示兑换码\n即可抵用相应现金"
						+ "\n\n如需更改兑换的积分\n则请回复新的积分数";
			}
		}
		
		return resMsg;
	}

	/**
	 * 用户输入具体积分数的事件 需替换掉之前未使用的记录
	 */
	public static String addExCode(String WeChatID,int NewExScore,String OpenID) throws Exception {
		//查询用户在本店的剩余积分,积分小于10,则提示兑换不了;
		String resMsg;
		CurScore mCurScore=DBCurScore.getCurScore(WeChatID, OpenID);
		if (mCurScore==null||mCurScore.CurScore==0) {
			resMsg="您在本店的积分为 0\n暂无积分可兑换";
		}else if (mCurScore.CurScore<10) {
			resMsg="您在本店只有"+mCurScore.CurScore+"积分\n最少要有10积分才可兑换";
		}else if (mCurScore.CurScore<NewExScore) {
			resMsg="您在本店只有"+mCurScore.CurScore+"积分";
		}else{
			//查询未输入或未使用的积分记录
			ExScore unExScore=DBExScore.getUnExScore(mCurScore.BusNo, mCurScore.UserID);
			if (unExScore==null) {//进入微信号 直接输入数字
				resMsg="亲,您是要兑换积分吗?\n请先点击左下角的'兑换积分'按钮.";
			} else{
				String tel=DBUser.getTel(mCurScore.UserID);
				if (unExScore.ExScore==NewExScore&&tel!=null) {//重复输入同样的积分数
					resMsg="亲,您之前已申请过兑换"+NewExScore+"积分\n兑换码为:"+unExScore.ExCode+"\n\n您在结账时,只需出示兑换码,即可抵用相应现金";
				} else{
					//未输入和未使用的记录 更新新的积分数和兑换码;
					int NewExCode=DBExScore.getNewExCode(mCurScore.BusNo);
					if (NewExCode==0) {
						resMsg="系统繁忙,请重试...";
					}else{
						//更新ExScore,ExCode,获取更新的记录数
						int upCount=DBExScore.upExScore(NewExScore, NewExCode,mCurScore.BusNo,mCurScore.UserID);
						if (upCount==0) {
							resMsg="亲,您是要兑换积分吗?\n请先点击左下角的'兑换积分'按钮.";
						}else if(tel==null){
							resMsg="第一次使用微信兑换积分需校验身份\n现请输入手机号...";
						}else if(unExScore.ExCode>0){
							resMsg="恭喜,您成功申请兑换"+NewExScore+"积分\n兑换码为:"+NewExCode
									+"\n(您之前申请的"+unExScore.ExScore+"积分已失效)\n\n您在结账时,只需出示兑换码,即可抵用相应现金";
						}else{
							resMsg="恭喜,您成功申请兑换"+NewExScore+"积分\n兑换码为:"+NewExCode+"\n\n您在结账时,只需出示兑换码,即可抵用相应现金";
						}
					}
				}
			} 
		}
		return resMsg;
	}
	
}
