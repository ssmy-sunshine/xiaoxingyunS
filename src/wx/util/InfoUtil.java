package wx.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Random;

public class InfoUtil {
	
	private static SimpleDateFormat sdf1= new SimpleDateFormat("yyyyMMdd");
	private static SimpleDateFormat sdf2= new SimpleDateFormat("yyyyMMddHHmmss");
	private static SimpleDateFormat sdf3= new SimpleDateFormat("yyyy-MM-dd HH:mm");
	 
	 /**
	  * 检查2个毫秒是否为同一天
	  */
	public static boolean isSameDay(long t1,long t2) {
	  String d1 =sdf1.format(t1);
	  String d2 =sdf1.format(t2);
	  return d1.equals(d2);
	}
	
	/**
	 * 获取今天0点的毫秒数
	 */
	public static long getTodayMillis() throws ParseException{
		String today=sdf1.format(System.currentTimeMillis())+"000000";
        return sdf2.parse(today).getTime();
	}
	
	/**
	 * 获取日期
	 * yyyy-MM-dd HH:mm
	 */
	public static String getTime(long time){
		return sdf3.format(time);
	}
	
	/**
	 * 生成随机数 不含4
	 * @param count 随机数个数
	 */
	 public static int getRandom(int count){
	        StringBuffer sb = new StringBuffer();
	        String str = "12356789";
	        Random r = new Random();
	        for(int i=0;i<count;i++){
	            int num = r.nextInt(str.length());
	            sb.append(str.charAt(num));
	            str = str.replace((str.charAt(num)+""), "");
	        }
	        return Integer.parseInt(sb.toString());
	  }
	 
}
