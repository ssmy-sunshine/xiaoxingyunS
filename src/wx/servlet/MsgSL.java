package wx.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import wx.biz.MsgClickBiz;
import wx.biz.MsgEvenBiz;
import wx.biz.MsgTextBiz;
import wx.entity.App;
import wx.entity.TextMsg;
import wx.entity.User;
import wx.util.MsgUtil;

/**
 * GET 验证Token
 */
public class MsgSL extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Get请求 Token验证
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			//String result=MsgTextBiz.doMsgText("gh_84f32fcca0ed","12345678901","oQjS6jj_o7XFaWVQrZVDdjUMIBH8");
			//String result=MsgTextBiz.doMsgText("gh_a3db15b06792","100","oQjS6jj_o7XFaWVQrZVDdjUMIBH8");
			//String result=MsgClickBiz.getWaitDetail("oQjS6jj_o7XFaWVQrZVDdjUMIBH8");
			//String result=MsgEvenBiz.doMsgEven("gh_a3db15b06792",12,10);
//			String result=MsgEvenBiz.doMsgEven("gh_84f32fcca0ed",99999,10);
//			int result=User.getUserID("oQjS6jhuEDmTgXOlKN1nD67Jbu6Q", "gh_84f32fcca0ed");
//			System.out.println(result);
//			String a="";
//			if ("".equals(a)) {
//				System.out.println("\n测试完毕========");
//				return;
//			}
			// 获取请求参数
			String signature = request.getParameter("signature");
			String timestamp = request.getParameter("timestamp");
			String nonce = request.getParameter("nonce");
			String echostring = request.getParameter("echostr");
			// 你自己填写的token
			String token = "wenju";
			// 对请求参数和自己的token进行排序，并连接排序后的结果为一个字符串
			String[] strSet = new String[] { token, timestamp, nonce };
			java.util.Arrays.sort(strSet);
			String total = "";
			for (String string : strSet) {
				total = total + string;
			}
			// SHA-1加密实例
			MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
			sha1.update(total.getBytes());
			byte[] codedBytes = sha1.digest();
			// 将加密后的字节数组转换成字符串
			String codedString = new BigInteger(1, codedBytes).toString(16);
			// 将加密的结果与请求参数中的signature比对，如果相同，原样返回echostr参数内容
			if (codedString.equals(signature)) {
				PrintWriter out = response.getWriter();
				out.print(echostring);
				out.flush();
				out.close();
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}

	}

	/**
	 *POST 接收和响应消息
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// 设置编码
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		try {
			// 调用核心业务类接收消息、处理消息
			String respXmlMsg = null;
			// 默认返回的文本消息内容
			String resMsg = "";

			// xml请求解析
			Map<String, String> requestMap = MsgUtil.parseXml(request);

			// 发送方帐号（open_id）
			String OpenID = requestMap.get("FromUserName");
			// 开发者微信号
			String WeChatID = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");
			
			System.out.println("商家="+WeChatID+",用户="+OpenID+",消息类型="+msgType);

			// 回复文本消息
			TextMsg textMsg = new TextMsg();
			textMsg.ToUserName=OpenID;
			textMsg.FromUserName=WeChatID;
			textMsg.CreateTime=System.currentTimeMillis()/1000;

			try {
				/*
				 * 1.文本消息类型
				 */
				if (msgType.equals("text")) {
					resMsg=MsgTextBiz.doMsgText(WeChatID,requestMap.get("Content"),OpenID);
					
				/*
				 * 2.事件消息类型
				 */
				}else if (msgType.equals("event")) {
					String eventType = requestMap.get("Event");
					//2.1 关注事件
					if (eventType.equals("subscribe")) {
						//关注事件 检查是否已经添加OpenID 没有则入库
						int UserID=User.getUserID(OpenID,WeChatID);
						//参数
						String param=requestMap.get("EventKey");
						System.out.println("param截8字符前=="+param);
						
						if (param==null||param.length()<8) {
							//2.1.2 搜索关注
							resMsg=App.WellcomeMsg;
						}else{
							//2.1.2带参扫码关注 参数: qrscene_+时间+商家+积分值
							param=param.substring(8);
							System.out.println("带参扫码关注,param截8字符后=="+param);
							
							resMsg=MsgEvenBiz.doMsgEven(WeChatID,Integer.parseInt(param), UserID);
						}
						
					//2.2 在已经关注的提前下扫码
					}else if (eventType.equals("SCAN")){
						//参数
						String param=requestMap.get("EventKey");
						int UserID=User.getUserID(OpenID,WeChatID);
						System.out.println("在已经关注的提前下扫码paramID=="+param);
						
						resMsg=MsgEvenBiz.doMsgEven(WeChatID,Integer.parseInt(param), UserID);
						
					//2.3 点击菜单事件
					}else if (eventType.equals("CLICK")){
						resMsg=MsgClickBiz.doClickEven(WeChatID,requestMap.get("EventKey"),OpenID);
					}
				}
			} catch (Exception e) {
				System.out.println("respContent抛异常==");
				e.printStackTrace();
			}
			
			System.out.println("回复的内容=="+resMsg);
			//响应文本消息 如果是空串是收不到消息的
			textMsg.Content=resMsg;
			respXmlMsg = MsgUtil.repTextMsg(textMsg);
			PrintWriter out = response.getWriter();
			out.print(respXmlMsg);
			out.flush();
			out.close();
			
		} catch (Exception e) {
			System.out.println("解析抛异常==");
			e.printStackTrace();
		}
		
	}

}
