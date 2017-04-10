package wx.entity;
/**
 * 获取access_token
 * {"access_token":"ACCESS_TOKEN","expires_in":7200}
 */
public class AcsToken {
	/**
	 * access_token
	 */
	public String access_token;
	
	/**
	 * 有效时间 秒
	 */
	public int expires_in;
}
