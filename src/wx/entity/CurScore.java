package wx.entity;
/**
 * 充值明细
 */
public class CurScore{
	
	/**
	 * ID 自增长
	 */
	public int ID;
	
	/**
	 * 商家编号
	 */
	public int BusNo;
	
	/**
	 * 商家名称
	 */
	public String BusName;
	
	/**
	 * 用户编号
	 */
	public int UserID;
	
	/**
	 * 当前积分数
	 */
	public int CurScore=0;
	
	/*
	 * 是否领取积分
	 */
	public boolean GetScore;
	
}
