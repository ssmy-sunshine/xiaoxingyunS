package wx.entity;
/**
 * 记录二维码信息(Type指定那张表,Data指定表数据的ID或者具体的信息)
 * ID Type Data Time
 */
public class QrCode{
	
	/**
	 * ID 自增长 
	 * 二维码所带的参数
	 */
	public int ID;
	
	/**
	 * 类型 说明
	 * Type=1; 等位,  Data=BusNo+WaitNo
	 * Type=2; 积分,  Data=QrScoreID 
	 * Type=3; wife, Data=BusNo 
	 * Type=4; 团购,  Data=BusNo
	 */
	public int Type;
	
	/**
	 * 数据
	 */
	public int Data;
	
	/**
	 * 打印生成时间
	 */
	public long Time;
	
}
