package wx.exception;

import wx.entity.Result;

/**
 * Biz业务异常
 */
public class AllException extends Exception{
	private static final long serialVersionUID = 1L;
	
	/**状态码*/
	private int code;
	
	public AllException(int code,String msg){
		super(msg);
		this.code=code;
	}

	public int getCode() {
		if(code==0) return Result.CODE_ERR;
		return code;
	}
	
}
