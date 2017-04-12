package wx.servlet;

import javax.servlet.http.HttpServletRequest;

import wx.biz.RedPacketBiz;
import wx.util.ParamUtil;

/**
 * 红包业务
 */
public class RedPacketSL {
	
	/**
	 * 分发业务
	 */
	public void todo(HttpServletRequest request,String SLM) throws Exception{
		if("create".equals(SLM)){
			//发红包
			create(request);
		}else if("takebyno".equals(SLM)){
			takeByNo(request);
		}
	}
	
	/**发红包*/
	private void create(HttpServletRequest request) throws Exception{
		double totalMoney=ParamUtil.getDouble(request, "totalMoney");
		int count=ParamUtil.getInt(request, "count");
		new RedPacketBiz().create(totalMoney, count);
	}
	
	/**抢红包*/
	private void takeByNo(HttpServletRequest request) throws Exception{
		int no=ParamUtil.getInt(request, "no");
		int takeuser=ParamUtil.getInt(request, "takeuser");
		new RedPacketBiz().takeByNo(no, takeuser);
	}
	
}
