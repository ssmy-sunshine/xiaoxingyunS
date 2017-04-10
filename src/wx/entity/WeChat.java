package wx.entity;
/**
 *商家信息
 */
public class WeChat {
	
	public WeChat(int BusNo){
		this.BusNo=BusNo;
	}
	
	/**
	 * 商家编号
	 */
	public int BusNo;
	
	/**
	 * 微信号 gh_84f32fcca0ed
	 */
	public String WeChatID;
	
	/**
	 * 微信appid wx9193f3dfc92c1b7a
	 */
	public String AppID;
	
	/**
	 * 微信secret 34798bb56ee852db8725d5f8d73c20a9
	 */
	public String Secret;
	
	/**
	 * 微信access_token
	 */
	public String access_token;
	
	/**
	 * 微信expires_in
	 */
	public long expires_in=0;
	
	/**
	 * 微信create_time
	 */
	public long create_time=0;
}
