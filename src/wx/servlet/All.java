package wx.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import wx.entity.Result;
import wx.exception.BizException;
import wx.exception.ParamException;
import wx.util.ParamUtil;

/**
 * 红包业务
 */
public class All extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		//处理起始时间
		long doStartTime=System.currentTimeMillis();
		
		//设置编码
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");

		// 返回客户端的信息
		Result res = new Result();
		
		//请求的模块SL
		String SL = "";
		//请求模块的方法
		String SLM = "";
		
		try {
			SL=ParamUtil.getString(request, "SL");
			SLM=ParamUtil.getString(request, "SLM");
			
			if("RedPacket".equals(SL)){
				//红包业务
				new RedPacketSL().todo(request, SLM);
			}
			
			res.Code=200;
			res.Msg="Success";
		}catch (ParamException e) {
			res.Code=5001;
			res.Msg=e.getMessage();
			e.printStackTrace();
		}catch (BizException e) {
			res.Code=5002;
			res.Msg=e.getMessage();
			e.printStackTrace();
		}catch (Exception e) {
			res.Code=500;
			res.Msg=e.getMessage();
			e.printStackTrace();
		}
		
		//返回客户端的 默认是"1" 服务器响应异常
		String resString=res.toString();
		PrintWriter out = response.getWriter();
		out.print(resString);
		out.flush();
		out.close();
		
		//输入日志
		System.out.println(SL+"/"+SLM+ "--> 返回reback=" + resString+"; 时间="+new Date()+ "; 响应=" + (System.currentTimeMillis()-doStartTime));
	}
	
}
