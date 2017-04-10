package wx.entity;
/**
 *请求返回的结果
 *{"Code":code,"Msg":"msg"}
 */
public class Result {
	/**
	 * 状态码
	 * 200 正常
	 * 500 服务器异常
	 * 5001 参数异常
	 * 5002 业务异常
	 */
	public int Code;
	
	/**
	 * 内容
	 */
	public String Msg;
	
	public String toString() {
		return "{\"Code\":"+Code+",\"Msg\":\""+Msg+"\"}";
	}
	
}
