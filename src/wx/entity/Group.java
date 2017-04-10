package wx.entity;
/**
 * 记录用户在商家的团购密码
 * ID BusNo SeatNo Type UserID Code
 */
public class Group{
	
	/**
	 * ID 自增长 
	 */
	public int ID;
	
	/**
	 * 商家编号
	 */
	public int BusNo;
	
	/**
	 * 商家的座位号
	 * 不用int 兼容001之类的座号
	 */
	public String SeatNo;
	
	/**
	 * 类型
	 * Type=1  美团;
	 * Type=2  大众点评;
	 * Type=3  其他;
	 */
	public int Type;
	
	/**
	 * 用户的ID
	 */
	public int UserID;
	
	/**
	 * 劵号 12位
	 * 多张劵则用加号连接起来
	 * 劵号1+劵号2+劵号3
	 */
	public String Code;
	
	/**
	 * 用户输入劵号的时间
	 */
	public Long Time;
}
