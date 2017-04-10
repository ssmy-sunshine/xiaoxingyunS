package wx.entity;

import com.google.gson.Gson;

/**
 * 全局变量
 */
public class App {
	
	/**
	 * 小码哥商家编号:666888
	 */
	public static final String IP="http://192.168.103/";
	
	/**
	 * 小码哥商家编号:666888
	 */
	public static final int BusNo=666888;
	
	/**
	 * 小码哥服务号:gh_a3db15b06792
	 */
	public static final String WeChatID="gh_a3db15b06792";
	
	/**
	 * 小码哥appid
	 */
	public static final String AppID="wx202bb047201f96fd";
	
	/**
	 * 小码哥secret 
	 */
	public static final String Secret="c035168489eabfe167f489f2c1cf16bb";
	
	/**
	 * GSon对象
	 */
	public static final Gson Gson=new Gson();
	
	/**
	 * 打印wife二维码所带的参数
	 */
	public static final String WellcomeMsg="欢迎您的大驾光临,么么哒!"
			+ "\n\n亲,在我们这里,排队是可以打折的,您排得越久,折扣越低!"
			+ "\n\n亲,在我们这里,每次消费都会得相应积分,而积分可直接抵用相应现金!"
			+ "\n\n还有更多精喜等你发现...";
	
	/**
	 * 兑换码长度 目前长度为4 等有一个商家兑换码记录超5000时需修改长度
	 */
	public static final int ExCodeLen=4;
	
}
