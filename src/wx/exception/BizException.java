package wx.exception;

import wx.entity.Result;

/**
 * Biz业务异常
 */
public class BizException extends AllException{
	private static final long serialVersionUID = 1L;
	
	public BizException(String msg){
		super(Result.CODE_ERR_BIZ,msg);
	}
	
}
