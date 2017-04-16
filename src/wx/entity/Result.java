package wx.entity;


/**
 *请求返回的结果
 *{"Code":Code,"Msg":"Msg","SysTime":1233453556}
 */
public class Result {
	/**请求成功*/
	public static final int CODE_SUCCESS=200;
	/**服务器异常*/
	public static final int CODE_ERR=500;
	/**参数异常*/
	public static final int CODE_ERR_PARAM=5001;
	/**业务异常*/
	public static final int CODE_ERR_BIZ=5002;
	/**token过期*/
	public static final int CODE_ERR_TK=5003;
	/**Msg默认值*/
	public static final String MSG_DEFAULT="SUCCESS";
	
	/**
	 * 状态码
	 */
	private int Code;
	
	/**
	 * 内容
	 */
	private Object Msg;
	
	/**
	 * 系统时间
	 */
	private long SysTime;

	public int getCode() {
		return Code;
	}

	public void setCode(int code) {
		Code = code;
	}

	public Object getMsg() {
		return Msg;
	}

	public void setMsg(Object msg) {
		Msg = msg;
	}

	public long getSysTime() {
		return SysTime;
	}

	public void setSysTime(long sysTime) {
		SysTime = sysTime;
	}
}
