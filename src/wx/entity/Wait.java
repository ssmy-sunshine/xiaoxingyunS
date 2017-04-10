package wx.entity;
/**
 * 等位明细
 */
public class Wait {
	/**
	 * ID 自增长
	 */
	public int ID;
	
	/**
	 * 商家编号
	 */
	public int BusNo;
	
	/**
	 * 用户编号
	 */
	public int UserID;
	
	/**
	 * 排位号
	 */
	public int WaitNo;
	
	/**
	 * 是否过期
	 */
	public boolean OutDate;
	
	/**
	 * 开始等待的时间
	 */
	public long TimeStart;
	
	/**
	 * 入座时间
	 */
	public long TimeEnd;
	
}
