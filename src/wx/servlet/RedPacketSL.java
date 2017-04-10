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
			createRedPacket(request);
		}
	}
	
	/**
	 * 发红包
	 */
	private void createRedPacket(HttpServletRequest request) throws Exception{
		double totalMoney=ParamUtil.getDouble(request, "totalMoney");
		int count=ParamUtil.getInt(request, "count");
		new RedPacketBiz(totalMoney, count).create();
	}
}
