package wx.entity;
/**
 * 消费明细
 */
public class ExScore{
	
	/**
	 * ID 自增长
	 */
	public int ID;
	
	/**
	 * 商家编号
	 */
	public int BusNo;
	
	/**
	 * 商家名字
	 */
	public String BusName;
	
	/**
	 * 用户编号
	 */
	public int UserID;
	
	/**
	 * 在当前商家的 兑换的次数
	 */
	public int ExCount;
	
	/**
	 * 兑换的积分
	 */
	public int ExScore;
	
	/**
	 * 兑换码 在一个商家中 兑换码具有唯一性
	 */
	public int ExCode;
	
	/**
	 * 兑换金额
	 */
	public float ExMoney;
	
	/**
	 * 兑换的时间
	 */
	public long ExTime;
	
}
