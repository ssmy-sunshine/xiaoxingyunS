package wx.biz;

import java.util.ArrayList;

import wx.db.DBCurScore;
import wx.db.DBExScore;
import wx.db.DBUser;
import wx.entity.CurScore;
import wx.entity.ExScore;
import wx.util.InfoUtil;
/**
 * 小码哥服务号
 */
public class XmgBusBiz {

	/**
	 * 查询用户在所有兑换过的商家中的剩余积分数; 条件 UserID=?;
	 */
	public static String getCurScore(String openID) throws Exception {
		String resMsg="您的积分余额: 0";//没有任何记录
		ArrayList<CurScore> scoreList=DBCurScore.getScoreList(openID);
		if (!scoreList.isEmpty()) {
			if (scoreList.size()==1) {
				//只有一条记录
				CurScore mCurScore=scoreList.get(0);
				resMsg="您在 "+mCurScore.BusName+" 的积分余额: "+mCurScore.CurScore;
			}else{
				//有多条记录 TODO 如果超过5条应该跳转界面另外展示!!
				resMsg="积分明细:\n---------------------\n";
				for (int i = 1; i <= scoreList.size(); i++) {
					CurScore mCurScore=scoreList.get(i-1);
					resMsg+=i+". "+mCurScore.BusName+"   "+mCurScore.CurScore
							+"积分\n---------------------\n";
				}
			}
			//提示兑换
			resMsg+="\n如需兑换积分,请扫描商家的积分兑换码.";
		}
		return resMsg;
	}

	/**
	 * 已兑换的明细 TODO 超过5条 跳转界面
	 */
	public static String getExScore(String OpenID) throws Exception {
		ArrayList<ExScore> ExScoreList=DBExScore.getExScore(OpenID);
		String resMsg="您还没有兑换过积分";
		if (!ExScoreList.isEmpty()) {
			resMsg="已兑换的积分明细:\n------------------------\n";
			for (ExScore mExScore : ExScoreList) {
				//水沐莲清: 兑换500积分,兑换码5846\n 抵现金50元, 2014-08-27 22:55
				resMsg+=mExScore.BusName+": 兑换"+mExScore.ExScore+"积分,兑换码"+mExScore.ExCode
						+"\n 抵现金"+mExScore.ExMoney+"元  "+InfoUtil.getTime(mExScore.ExTime)+"\n------------------------\n";
			}
		}
		return resMsg;
	}

	/**
	 * 用户扫兑换积分码的事件
	 */
	public static String addExScore(int BusNo, int UserID) throws Exception {
		//查询用户在商家的剩余积分,积分小于10,则提示兑换不了;
		String resMsg;
		CurScore mCurScore=DBCurScore.getCurScore(BusNo, UserID);
		if (mCurScore==null||mCurScore.CurScore==0) {
			resMsg="您在本店的积分为 0\n暂无积分可兑换";
		}else if (mCurScore.CurScore<10) {
			resMsg="您在本店只有 "+mCurScore.CurScore+"积分\n最少要有10积分才可兑换";
		}else{
			//删除用户所有未输入和未兑换的记录
			DBExScore.delUnExScore(UserID);
			//查询用户在商家的兑换次数
			int ExCount=DBExScore.getExCount(BusNo,UserID);
			//插入新的记录
			DBExScore.addExScore(BusNo,mCurScore.BusName,UserID,ExCount+1);
			resMsg="您在 "+mCurScore.BusName+" 共有 "+mCurScore.CurScore+"积分\n请输入要兑换的积分数...";
		}
		return resMsg;
	}

	/**
	 * 用户输入具体积分数的事件
	 */
	public static String addExCode(int NewExScore,String OpenID) throws Exception {
		String resMsg;
		//查询用户等待输入的记录;每输入一次积分值,如果要修改或重新兑换,都得重新扫,重新删除未使用的;
		ExScore unExScore=DBExScore.getNoExCode(OpenID);
		if (unExScore==null) {
			resMsg="亲,您是要兑换积分吗?\n请先扫商家的积分兑换码";
		}else{
			//查询用户在商家的剩余积分,积分小于10,则提示兑换不了;
			CurScore mCurScore=DBCurScore.getCurScore(unExScore.BusNo, unExScore.UserID);
			if (mCurScore==null||mCurScore.CurScore==0) {
				resMsg="您在 "+unExScore.BusName+" 的积分为 0\n暂无积分可兑换";
			}else if (mCurScore.CurScore<10) {
				resMsg="您在 "+unExScore.BusName+" 只有"+mCurScore.CurScore+"积分\n最少要有10积分才可兑换";
			}else if (mCurScore.CurScore<NewExScore) {
				resMsg="您在 "+unExScore.BusName+" 只有"+mCurScore.CurScore+"积分";
			}else{
				//获取新的兑换码
				int NewExCode=DBExScore.getNewExCode(mCurScore.BusNo);
				if (NewExCode==0) {
					resMsg="系统繁忙,请重试...";
				}else{
					//更新ExScore,ExCode,获取更新的记录数
					int upCount=DBExScore.upExScore(NewExScore, NewExCode,mCurScore.BusNo,mCurScore.UserID);
					if (upCount==0) {
						resMsg="亲,您是要兑换积分吗?\n请先扫商家的积分兑换码";
					}else if(DBUser.getTel(mCurScore.UserID)==null){
						resMsg="第一次使用微信兑换积分需校验身份\n现请输入手机号...";
					}else{
						resMsg="恭喜,您成功申请兑换"+NewExScore+"积分\n兑换码为:"+NewExCode+"\n\n您在结账时,只需出示兑换码,即可抵用相应现金";
					}
				}
			}
		}
		return resMsg;
	}

}
