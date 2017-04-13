package wx.exception;

import wx.entity.Result;

/**
 * Biz业务异常
 */
public class TKException extends AllException{
	private static final long serialVersionUID = 1L;
	
	public TKException(){
		super(Result.CODE_ERR_TK,"登录过期");
	}
	
}
