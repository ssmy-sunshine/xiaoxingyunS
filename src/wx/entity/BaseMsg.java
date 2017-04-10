package wx.entity;

/**
 * 消息基类（公众帐号 -> 普通用户）
 */
public class BaseMsg {
	/**
	 * 接收方帐号（收到的OpenID）
	 */
	public String ToUserName;
	
	/**
	 *  开发者微信号
	 */
	public String FromUserName;
	
	/**
	 * 消息创建时间
	 */
	public long CreateTime;
	
}
