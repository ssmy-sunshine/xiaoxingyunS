package wx.exception;
/**
 * Biz业务异常
 */
public class BizException extends Exception{
	private static final long serialVersionUID = 1L;
	
	public BizException(String msg){
		super(msg);
	}
	
}
