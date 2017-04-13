package wx.exception;

import wx.entity.Result;

/**
 * 参数缺失异常
 */
public class ParamException extends AllException{
	private static final long serialVersionUID = 1L;
	
	/**
	 * @param paramKey 参数名
	 */
	public ParamException(String paramKey){
		super(Result.CODE_ERR_PARAM,"缺失参数:"+paramKey);
	}
}
