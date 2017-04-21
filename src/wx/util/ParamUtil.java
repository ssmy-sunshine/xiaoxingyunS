package wx.util;

import javax.servlet.http.HttpServletRequest;

import wx.exception.ParamException;
/**
 * 获取参数工具类
 * 如果参数为空则抛异常
 */
public class ParamUtil {
	
	public static String getString(HttpServletRequest request,String paramKey) throws ParamException{
		String value=request.getParameter(paramKey);
		if(InfoUtil.isTextEmpty(value)) throw new ParamException(paramKey);
		return value;
	}
	
	public static double getDouble(HttpServletRequest request,String paramKey) throws ParamException{
		String value=request.getParameter(paramKey);
		if(InfoUtil.isTextEmpty(value)) throw new ParamException(paramKey);
		return Double.parseDouble(value);
	}
	
	public static int getInt(HttpServletRequest request,String paramKey) throws ParamException{
		String value=request.getParameter(paramKey);
		if(InfoUtil.isTextEmpty(value)) throw new ParamException(paramKey);
		return Integer.parseInt(value);
	}
	
	public static int getInt0(HttpServletRequest request,String paramKey){
		String value=request.getParameter(paramKey);
		if(InfoUtil.isTextEmpty(value)){
			return 0;
		}else{
			return Integer.parseInt(value);
		}
	}
}
