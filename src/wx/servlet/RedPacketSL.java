package wx.servlet;

import javax.servlet.http.HttpServletRequest;

import wx.biz.RedPacketBiz;
import wx.entity.RedPacket;
import wx.util.ParamUtil;

import com.google.gson.JsonObject;

/**
 * 红包业务
 */
public class RedPacketSL {
	
	/**
	 * 分发业务
	 */
	public JsonObject todo(HttpServletRequest request,String SLM) throws Exception{
		JsonObject obj=new JsonObject();
		
		if("create".equals(SLM)){
			//发红包  All?SL=RedPacket&SLM=create
			int packetNo=create(request);
			obj.addProperty("no", packetNo);//返回红包口令
		}else if("takebyno".equals(SLM)){
			takeByNo(request);
		}
		
		return obj;
	}
	
	/**发红包,返回红包口令*/
	private int create(HttpServletRequest request) throws Exception{
		double totalMoney=ParamUtil.getDouble(request, "money");
		int count=ParamUtil.getInt(request, "count");
		int taketype=ParamUtil.getInt(request, "taketype");
		int score=ParamUtil.getInt0(request, "score");
		int ticketId=ParamUtil.getInt0(request, "ticketId");
		String remark=request.getParameter("remark");
		//实例化红包对象
		RedPacket redPacket=new RedPacket();
		redPacket.setMoney(totalMoney);
		redPacket.setCount(count);
		redPacket.setTaketype(taketype);
		redPacket.setScore(score);
		redPacket.setTicketId(ticketId);
		redPacket.setRemark(remark);
		//发红包
		int packetNo=new RedPacketBiz().create(redPacket);
		return packetNo;
	}
	
	/**抢红包*/
	private void takeByNo(HttpServletRequest request) throws Exception{
		int no=ParamUtil.getInt(request, "no");
		int takeuser=ParamUtil.getInt(request, "takeuser");
		new RedPacketBiz().takeByNo(no, takeuser);
	}
	
}
