package wx.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InfoUtil {
	
	/**
	 * 检查字符串是否为空
	 */
	public static boolean isTextEmpty(String text){
		if(text==null||"".equals(text.trim())||"null".equals(text)||"NULL".equals(text)){
			return true;
		}
		return false;
	}
	
	/**
	 * 保留2位小数
	 */
	public static double get2Double(double d){
		return new BigDecimal(d).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	/**
	 * 转化时间为 yyyy-MM-dd HH:mm:ss
	 */
	public static String dateFormat(Date date){
		if(date==null) date=new Date();
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}
}
