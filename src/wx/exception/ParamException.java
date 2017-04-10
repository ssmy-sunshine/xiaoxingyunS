package wx.exception;
/**
 * 参数缺失异常
 */
public class ParamException extends Exception{
	private static final long serialVersionUID = 1L;
	
	/**
	 * @param paramKey 参数名
	 */
	public ParamException(String paramKey){
		super("缺失参数:"+paramKey);
	}
}
