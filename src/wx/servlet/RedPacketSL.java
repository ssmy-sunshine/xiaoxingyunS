package wx.servlet;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import wx.biz.RedPacketBiz;
import wx.db.RedPacketDB;
import wx.db.RedPacketDetailDB;
import wx.entity.RedPacket;
import wx.entity.RedPacketDetail;
import wx.entity.Result;
import wx.entity.TakeDetail;
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
			ArrayList<TakeDetail> list=takedetail(request);
			return list;
			
		}else if("getalldata".equals(SLM)){
			//查询当前用户金额,优惠券,积分总数  All?SL=RedPacket&SLM=getalldata
			String takeuser=ParamUtil.getString(request, "Uid");
			JsonObject obj=new RedPacketDetailDB().getAllData(takeuser);
			return obj;
			
		}else if("moneydetail".equals(SLM)){
			//查询当前用户金额明细  All?SL=RedPacket&SLM=moneydetail
			String takeuser=ParamUtil.getString(request, "Uid");
			JsonArray obj=new RedPacketDetailDB().getMoneyDetail(takeuser);
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
		//实例化红包对象
		RedPacket redPacket=new RedPacket();
		redPacket.setMoney(totalMoney);
		redPacket.setCount(count);
		redPacket.setBusid(busid);
		redPacket.setTaketype(taketype);
		redPacket.setScore(score);
		redPacket.setTicketId(ticketId);
		redPacket.setRemark(remark);
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
		Result mResult=new RedPacketBiz().takeByPass(pass, takeuser);
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
	private ArrayList<TakeDetail> takedetail(HttpServletRequest request) throws Exception {
		int pass=ParamUtil.getInt(request, "pass");
		ArrayList<TakeDetail> list=new RedPacketDetailDB().getTakeList(pass);
		return list;
	}
}
