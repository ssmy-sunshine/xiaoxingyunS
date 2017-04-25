package wx.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import wx.entity.Result;
import wx.exception.AllException;
import wx.util.InfoUtil;
import wx.util.ParamUtil;

import com.google.gson.Gson;

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
			Object obj=null;
			
			if("RedPacket".equals(SL)){
				//红包业务
				obj=new RedPacketSL().todo(request, SLM);
			}
			
			res.setCode(Result.CODE_SUCCESS);
			res.setMsg(obj);
		}catch (AllException e) {
			res.setCode(e.getCode());
			res.setMsg(e.getMessage());
			e.printStackTrace();
		}catch (Exception e) {
			res.setCode(Result.CODE_ERR);
			res.setMsg("服务器异常:"+e.getMessage());
			e.printStackTrace();
		}
		
		//返回客户端的 默认是"1" 服务器响应异常
		res.setSysTime(System.currentTimeMillis());
		String resString=new Gson().toJson(res);
		PrintWriter out = response.getWriter();
		out.print(resString);
		out.flush();
		out.close();
		
		//输入日志
		System.out.println(SL+"/"+SLM+ "--> 返回reback=" + resString+"; 时间="+InfoUtil.dateFormat(null)+ "; 响应=" + (System.currentTimeMillis()-doStartTime));
	}
	
}
