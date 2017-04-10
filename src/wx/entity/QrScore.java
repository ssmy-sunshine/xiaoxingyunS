package wx.entity;
/**
 * 积分码信息
 * ID BusNo QrScore QrTime UserID ScanTime
 */
public class QrScore{
	
	/**
	 * ID 自增长
	 */
	public int ID;
	
	/**
	 * 商家编号
	 */
	public int BusNo;
	
	/**
	 * 积分数
	 */
	public int QrScore;
	
	/**
	 * 打印生成时间
	 */
	public long QrTime;
	
	/**
	 * 用户ID
	 */
	public int UserID;
	
	/**
	 * 扫描的时间
	 */
	public long ScanTime;
}
