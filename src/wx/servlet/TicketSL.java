package wx.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import wx.db.DBBusiness;
import wx.db.DBQrCode;
import wx.db.DBQrScore;
import wx.entity.AcsToken;
import wx.entity.App;
import wx.entity.WeChat;
import wx.util.HttpUtil;

import com.google.gson.reflect.TypeToken;

/**
 * 生成二维码的ticket
 * 二维码信息加入QrCode表
 * 二维码带参:QrCodeID
 */
public class TicketSL extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//商家编号和微信信息的键值对 默认初始化小码哥服务号信息
	private static HashMap<Integer,WeChat> WeChatList;
	static{
		WeChatList=new HashMap<Integer,WeChat>();
		WeChat mWeChat=new WeChat(App.BusNo);
		mWeChat.WeChatID=App.WeChatID;
		mWeChat.AppID=App.AppID;
		mWeChat.Secret=App.Secret;
		WeChatList.put(App.BusNo, mWeChat);
	}
	
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// 设置编码
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");

		long t=System.currentTimeMillis();
		
		// 获取请求要做的方法
		String method = request.getParameter("method");
		// 返回客户端的信息
		String ticket = "1";// 服务器响应异常
		
		try {
			
			if ("score".equals(method)) {
				/*
				 * 打印积分码
				 * Type=2; 积分,  Data=QrScoreID
				 */
				int BusNo=Integer.parseInt(request.getParameter("BusNo"));
				int Score=Integer.parseInt(request.getParameter("Score"));
				//保存:商家编码,积分数,时间 返回刚刚插入的ID
				int QrScoreID=DBQrScore.addQrScore(BusNo,Score);
				//加入QrCode表
				int QrCodeID=DBQrCode.addQrCode(2,QrScoreID);
				//获取ticket 所带参为QrCodeID
				ticket=getQrTicket(BusNo,QrCodeID,true);//临时
				
			}else if ("wife".equals(method)){
				/*
				 * 打印wife永久二维码
				 * Type=3; wife, Data=BusNo 
				 */
				int BusNo=Integer.parseInt(request.getParameter("BusNo"));
				//加入QrCode表
				int QrCodeID=DBQrCode.addQrCode(3,BusNo);
				//获取ticket 所带参为QrCodeID
				ticket=getQrTicket(BusNo,QrCodeID,false);//永久
			}else if ("group".equals(method)){
				/*
				 * 打印团购永久二维码
				 * Type=4; 团购,  Data=BusNo+SeatNo
				 */
				int BusNo=Integer.parseInt(request.getParameter("BusNo"));
				String SeatNo=request.getParameter("SeatNo");
				//加入QrCode表
				int Data=Integer.parseInt(BusNo+SeatNo);
				int QrCodeID=DBQrCode.addQrCode(4,Data);
				//获取ticket 所带参为QrCodeID
				ticket=getQrTicket(BusNo,QrCodeID,false);//永久
			}else if ("exqr".equals(method)){
				/*
				 * 打印积分兑换码
				 * Type=5; 积分兑换码,  Data=BusNo
				 */
				int BusNo=Integer.parseInt(request.getParameter("BusNo"));
				//加入QrCode表
				int QrCodeID=DBQrCode.addQrCode(5,BusNo);
				//获取ticket 所带参为QrCodeID
				ticket=getQrTicket(BusNo,QrCodeID,false);//永久
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//返回客户端的 默认是"1" 服务器响应异常
		System.out.println("方法method=" + method + "==返回ticket=" + ticket);
		System.out.println("当前时间="+System.currentTimeMillis()+ "==响应时长=" + (System.currentTimeMillis()-t));
		PrintWriter out = response.getWriter();
		out.print(ticket);
		out.flush();
		out.close();
	}


	
	/**
	 * 获取带参二维码的ticket
	 * @param param 临时二维码最大10位数且开头不大于2;永久二维码最大5位 99999
	 * @param isTem=true是临时二维码,false是永久二维码;
	 */
	public static String getQrTicket(int BusNo,int param,boolean isTem) throws Exception{
		//获取对应的商家微信信息 没有查到则从数据库拿
		WeChat mWeChat=WeChatList.get(BusNo);
		if (mWeChat==null) {
			mWeChat=DBBusiness.getWeChat(BusNo);
			WeChatList.put(BusNo, mWeChat);
		}
		//如果mWeChat没有服务号,则说明是用小码哥服务号 否则直接用商家自己的服务号
		if (mWeChat.WeChatID==null) {
			mWeChat.WeChatID=App.WeChatID;
			mWeChat.AppID=App.AppID;
			mWeChat.Secret=App.Secret;
		}
		//刷新access_token
		flushAccess(mWeChat);
		//获取ticket
		String QrcodeUrl="https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+mWeChat.access_token;
		String json=null;
		if (isTem) {
			json=HttpUtil.PostJson(getTemQrStr(param),QrcodeUrl);//临时二维码
		}else{
			json=HttpUtil.PostJson(getEverQrStr(param),QrcodeUrl);//永久二维码
		}
		System.out.println("二维码json=="+json);
		Type type=new TypeToken<TicketInfo>(){}.getType();
		TicketInfo ticketInfo=App.Gson.fromJson(json, type);
		return ticketInfo.ticket;
	}
	
	/**
	 * 用户信息地址
	 */
	public static String getUserUrl(String OpenID,String WeChatID) throws Exception{
		//获取AppID,Secret,若不是小码哥服务号 则从数据库获取;否则直接从Map集合中拿
		WeChat mWeChat=null;
		if (!App.WeChatID.equals(WeChatID)) {
			mWeChat=DBBusiness.getWeChat(WeChatID);
		}else{
			mWeChat=WeChatList.get(App.BusNo);
		}
		//联网获取 {"access_token":"ACCESS_TOKEN","expires_in":7200}
		flushAccess(mWeChat);
		return "https://api.weixin.qq.com/cgi-bin/user/info?access_token="+mWeChat.access_token+"&openid="+OpenID+"&lang=zh_CN";
	}
	
	/**
	 * 如果acction_token过期 则联网获取 {"access_token":"ACCESS_TOKEN","expires_in":7200}
	 */
	private static void flushAccess(WeChat mWeChat) throws Exception{
		if (mWeChat.access_token==null||mWeChat.create_time==0
				||System.currentTimeMillis()-mWeChat.create_time>mWeChat.expires_in) {
			System.out.println("新获取access_token=="+mWeChat.WeChatID);
			String tokenUrl="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
							+mWeChat.AppID+"&secret="+mWeChat.Secret;
			
			String json=HttpUtil.HttpGet(tokenUrl);
			Type type=new TypeToken<AcsToken>(){}.getType();
			AcsToken acsToken=App.Gson.fromJson(json, type);
			
			mWeChat.create_time=System.currentTimeMillis()-60*1000;
			mWeChat.expires_in=acsToken.expires_in*1000;
			mWeChat.access_token=acsToken.access_token;
		}
	}
	
	/**
	 * 临时二维码ticket的post数据
	 */
	private static String getTemQrStr(int scene_id){
		return "{\"expire_seconds\": 1800, \"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": "+scene_id+"}}}";
	}
	
	/**
	 * 永久二维码ticket的post数据
	 */
	private static String getEverQrStr(int scene_id){
		return "{\"action_name\": \"QR_LIMIT_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": "+scene_id+"}}}";
	}
	
	/**
	 * 获取ticket
	 * {"ticket":"TICKET","expire_seconds":1800}
	 */
	private class TicketInfo {
		private String ticket;
		//private int expire_seconds;
	}
}
