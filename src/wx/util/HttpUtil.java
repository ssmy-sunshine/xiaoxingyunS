package wx.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class HttpUtil {
	/**
	 * POST请求
	 * 向服务器写入JSON字符串
	 */
	public static String PostJson(String str,String urlPath) throws IOException{
		 byte[] data = str.getBytes();//参数转字节数组
	     URL url = new URL(urlPath); //地址
	     URLConnection conn = url.openConnection(); //建立连接
	     conn.setRequestProperty("accept", "*/*");
	     conn.setRequestProperty("connection", "Keep-Alive");
	     conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	     conn.setDoOutput(true);// 准备写出
	     conn.setUseCaches(false); //使用Post方式不能使用缓存
	     conn.getOutputStream().write(data);// 写出数据
	     return getString(conn.getInputStream());//返回的字符串结果
	}
	
	/**
	 *  Get请求 获取服务器返回的联网对象 在获取流之后关闭
	 */
	public static String HttpGet(String urlPath) throws IOException{
		URL url=new URL(urlPath);
		URLConnection conn=url.openConnection();
		conn.setConnectTimeout(5000);//超时
		return getString(conn.getInputStream());//返回的字符串结果
	}
	
	/**
	  * 输入流转字符串
	  */
	 private static String getString(InputStream is) throws IOException {
	  BufferedReader br=new BufferedReader(new InputStreamReader(is,"utf-8"));
	  StringBuffer sb=new StringBuffer();
	  String content;
	  while ((content=br.readLine())!=null) {
	   sb.append(content);
	  }
	  return sb.toString().trim();
	 }
	 
}
