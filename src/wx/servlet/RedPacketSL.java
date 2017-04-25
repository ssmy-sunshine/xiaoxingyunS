package wx.servlet;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import wx.biz.RedPacketBiz;
import wx.db.RedPacketDB;
import wx.db.RedPacketDetailDB;
import wx.entity.RedPacket;
import wx.entity.RedPacketDetail;
import wx.entity.Result;
import wx.util.ParamUtil;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * 红包业务
 */
public class RedPacketSL {
	
	/**
	 * 分发业务
	 */
	public Object todo(HttpServletRequest request,String SLM) throws Exception{
		
		if("create".equals(SLM)){
			//发红包;返回红包口令;  All?SL=RedPacket&SLM=create
			int pass=create(request);
			JsonObject obj=new JsonObject();
			obj.addProperty("pass", pass);
			return obj;
			
		}else if("getlist".equals(SLM)){
			//根据商家查询红包列表  All?SL=RedPacket&SLM=getlist
			ArrayList<RedPacket> list=getList(request);
			return list;
			
		}else if("getdetail".equals(SLM)){
			//查询红包明细  All?SL=RedPacket&SLM=getdetail
			RedPacket mRedPacket=getDetail(request);
			return mRedPacket;
			
		}else if("getbytakeuser".equals(SLM)){
			//根据口令查询当前用户抢的红包  All?SL=RedPacket&SLM=getbytakeuser
			RedPacketDetail redPacketDetail=getByTakeuser(request);
			return redPacketDetail;
			
		}else if("takebypass".equals(SLM)){
			//抢红包  All?SL=RedPacket&SLM=takebypass
			Result mResult=takeByPass(request);
			return mResult;
			
		}else if("takedetail".equals(SLM)){
			//查询抢红包明细  All?SL=RedPacket&SLM=takedetail
			JsonArray arr=takedetail(request);
			return arr;
			
		}else if("getalldata".equals(SLM)){
			//查询当前用户总金额(红包金额+裂变奖励金额),优惠券,积分总数  All?SL=RedPacket&SLM=getalldata
			String takeuser=ParamUtil.getString(request, "Uid");
			JsonObject obj=getALlData(takeuser);
			return obj;
			
		}else if("moneydetail".equals(SLM)){
			//查询当前用户金额明细  All?SL=RedPacket&SLM=moneydetail&gettype=profit
			String takeuser=ParamUtil.getString(request, "Uid");
			String gettype=request.getParameter("gettype");
			JsonArray obj=getMoneyDetail(takeuser,gettype);
			return obj;
			
		}else if("scoredetail".equals(SLM)){
			//查询当前用户积分明细  All?SL=RedPacket&SLM=scoredetail
			String takeuser=ParamUtil.getString(request, "Uid");
			JsonArray obj=new RedPacketDetailDB().getScoreDetail(takeuser);
			return obj;
			
		}else{
			return null;
		}
	}
	
	/**发红包,返回红包口令*/
	private int create(HttpServletRequest request) throws Exception{
		double totalMoney=ParamUtil.getDouble(request, "money");
		int count=ParamUtil.getInt(request, "count");
		int busid=ParamUtil.getInt(request, "Uid");
		int taketype=ParamUtil.getInt(request, "taketype");
		int score=ParamUtil.getInt0(request, "score");
		int ticketId=ParamUtil.getInt0(request, "ticketId");
		String remark=request.getParameter("remark");
		double profit=ParamUtil.getDouble0(request,"profit");//发裂变红包时有值
		double maxprofit=ParamUtil.getDouble0(request,"maxprofit");//发裂变红包时有值
		int morepass=ParamUtil.getInt0(request,"morepass");//发普通红包如果有带裂变红包,则morepass=裂变红包的pass
		//实例化红包对象
		RedPacket redPacket=new RedPacket();
		redPacket.setMoney(totalMoney);
		redPacket.setCount(count);
		redPacket.setBusid(busid);
		redPacket.setTaketype(taketype);
		redPacket.setScore(score);
		redPacket.setTicketId(ticketId);
		redPacket.setRemark(remark);
		redPacket.setProfit(profit);
		redPacket.setMaxprofit(maxprofit);
		redPacket.setMorepass(morepass);
		//发红包
		int pass=new RedPacketBiz().create(redPacket);
		return pass;
	}
	
	/**根据口令查询当前用户抢的红包*/
	private RedPacketDetail getByTakeuser(HttpServletRequest request) throws Exception {
		int pass=ParamUtil.getInt(request, "pass");
		String takeuser=ParamUtil.getString(request, "Uid");
		RedPacketDetail takeBag=new RedPacketDetailDB().getByTakeuser(pass,takeuser);
		return takeBag;
	}
	
	/**抢红包*/
	private Result takeByPass(HttpServletRequest request) throws Exception{
		int pass=ParamUtil.getInt(request, "pass");
		String takeuser=ParamUtil.getString(request, "Uid");
		String inviter=request.getParameter("inviter");
		Result mResult=new RedPacketBiz().takeByPass(pass, takeuser, inviter);
		return mResult;
	}
	
	/**根据商家查询红包列表*/
	private ArrayList<RedPacket> getList(HttpServletRequest request) throws Exception{
		int busid=ParamUtil.getInt(request, "Uid");
		int no=ParamUtil.getInt(request, "no");
		int size=ParamUtil.getInt(request, "size");
		ArrayList<RedPacket> list=new RedPacketDB().getList(busid, no, size);
		return list;
	}
	
	/**查询红包明细 */
	private RedPacket getDetail(HttpServletRequest request) throws Exception {
		int pass=ParamUtil.getInt(request, "pass");
		RedPacket mRedPacket=new RedPacketDB().getByPass(pass);
		return mRedPacket;
	}
	
	/**查询抢红包明细*/
	private JsonArray takedetail(HttpServletRequest request) throws Exception {
		int pass=ParamUtil.getInt(request, "pass");
		JsonArray arr=new RedPacketDetailDB().getTakeList(pass);
		return arr;
	}
	
	/**查询当前用户总金额(红包金额+裂变奖励金额),优惠券,积分总数*/
	private JsonObject getALlData(String takeuser) throws Exception {
		RedPacketDetailDB mRedPacketDetailDB=new RedPacketDetailDB();
		JsonObject obj=mRedPacketDetailDB.getAllData(takeuser);
		double allprofit=mRedPacketDetailDB.getAllProfit(takeuser);
		obj.addProperty("allprofit", allprofit);
		obj.addProperty("totalprice", obj.get("alltake").getAsDouble()+allprofit);
		return obj;
	}
	
	/**查询当前用户金额明细*/
	private JsonArray getMoneyDetail(String takeuser, String gettype) throws Exception {
		JsonArray obj;
		if("profit".equals(gettype)){
			obj=new RedPacketDetailDB().getProfitDetail(takeuser);
		}else{
			obj=new RedPacketDetailDB().getMoneyDetail(takeuser);
		}
		return obj;
	}
	
}
