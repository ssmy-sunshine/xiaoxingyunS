package wx.biz;

import wx.db.DBExScore;
import wx.db.DBGroup;
import wx.db.DBUser;
import wx.entity.App;
import wx.entity.ExScore;


/**
 * 处理文本消息
 */
public class MsgTextBiz {
	
	/**
	 * 处理文本消息
	 */
	public static String doMsgText(String weChatID,String text,String openID) throws Exception{
		String resMsg="输入有误,内容无法识别";
		
		//去掉所有的空格
		text=text.replaceAll(" ", "");
		
		if(text.length()>1&&text.length()<6){
			resMsg=addExCode(weChatID,text,openID);//申请兑换积分 10到100000
		}else if (text.length()==1) {
			if ("1".equals(text)||"2".equals(text)||"3".equals(text)){
				resMsg=setGroupType(Integer.parseInt(text),openID);//选择美团或者大众点评
			}else if ("4".equals(text)||"5".equals(text)||"6".equals(text)
					||"7".equals(text)||"8".equals(text)||"9".equals(text)){
				resMsg="最少兑换10积分";//防止刷兑换次数
			}
			
		}else if(text.length()==11){
			resMsg=addTel(text,openID);//添加号码
			
		}else if(text.length()==12||text.contains("+")){//添加团购券
			resMsg=addGroupBuy(text,openID);
			
		}
		
		return resMsg;
	}

	/**
	 * 用户输入具体积分数的事件
	 */
	private static String addExCode(String weChatID,String text, String openID) throws Exception {
		//检查有效性 10到100000的数字
		int newExScore=0;
		try {
			newExScore=Math.abs(Integer.parseInt(text));//防止输入负数
			if (newExScore<10) {
				return "最少兑换10积分";
			}
		} catch (Exception e) {
			return "输入有误,内容无法识别";
		}
		String resMsg;
		if (App.WeChatID==weChatID) {
			resMsg=XmgBusBiz.addExCode(newExScore,openID);
		} else {
			resMsg=VipBusBiz.addExCode(weChatID,newExScore,openID);
		}
		return resMsg;
	}
	
	/**
	 * 1,美团
	 * 2,大众点评
	 * 3,其他
	 */
	private static String setGroupType(int Type, String openID) throws Exception {
		//加入团购类型
		int count=DBGroup.updateType(Type,openID);
		if (count==0) {
			return "您需重新扫描二维码,再选择团购类型.";
		}
		//检查是否输入电话号码
		if (DBUser.getTel(openID)==null) {
			return "第一次使用微信验证团购券\n系统需验证身份\n请输入手机号码...";
		}
		//回复
		if (Type==1) {
			return "请输入美团劵号:\n\n(如使用多张劵,请用加号连接:\n如 劵号1+劵号2+劵号3\n)";
		} else if (Type==2) {
			return "请输入大众点评劵号:\n\n(如使用多张劵,请用加号连接:\n如 劵号1+劵号2+劵号3\n)";
		}else{
			return "请输入团购劵号:\n\n(如使用多张劵,请用加号连接:\n如 劵号1+劵号2+劵号3\n)";
		}
	}

	/**
	 * 添加团购劵号
	 */
	private static String addGroupBuy(String text, String openID) throws Exception {
		//检查是否输入电话号码
		if (DBUser.getTel(openID)==null) {
			return "第一次使用微信验证团购券\n系统需验证身份\n请输入手机号码...";
		}
				
		if(text.length()==12){
			//添加一张团购券 检查有效性
			try {
				Long.parseLong(text);
			} catch (Exception e) {
				return "团购劵号输入有误,请检查";
			}
			
		}else{
			//添加多张团购券 检查每张劵号的有效性
			String[] codes=text.split("\\+");
			String errCode=null;
			for (String code : codes) {
				if (code.length()!=12) {
					errCode=code;
					break;
				}
				try {
					Long.parseLong(code);
				} catch (NumberFormatException e) {
					errCode=code;
					break;
				}
			}
			if (errCode!=null) {
				return errCode+"输入有误!\n\n1.检查团购劵号是否正确;\n\n2.多个劵号的格式应为\n  劵号1+劵号2+..+劵号N";
			}
		}
		//加入团购券
		int count=DBGroup.updateCode(text,openID);
		if (count==0) {
			return "您需重新扫描二维码,选择团购类型,再输入团购劵号.";
		}
		return "恭喜,您的团购券已提交成功!\n如需输入其他劵号,则重新扫描二维码";
	}

	/**
	 * 添加号码
	 */
	private static String addTel(String text, String openID) throws Exception {
		//检查是否输入的都是数字
		try {
			Long.parseLong(text);
		} catch (Exception e) {
			return "输入有误,请检查";
		}
		//存入电话号码
		DBUser.updateTel(openID,text);
		
		//查询用户是否有未兑换的积分
		ExScore unExScore=DBExScore.getNoUseScore(openID);
		if (unExScore!=null) {
			return "身份验证成功!\n恭喜,您在"+unExScore.BusName+"成功申请兑换"+unExScore.ExScore
					+"积分\n兑换码为:"+unExScore.ExCode+"\n\n您在结账时,只需出示兑换码,即可抵用相应现金";
		}
		
		//查询是否已经选择团购类型,提示输入团购券
		int Type=DBGroup.getCurType(openID);
		if (Type==1) {
			return "请输入美团劵号:\n\n(如使用多张劵,请用加号连接:\n如 劵号1+劵号2+劵号3\n)";
		} else if (Type==2) {
			return "请输入大众点评劵号:\n\n(如使用多张劵,请用加号连接:\n如 劵号1+劵号2+劵号3\n)";
		}else if (Type==3){
			return "请输入团购劵号:\n\n(如使用多张劵,请用加号连接:\n如 劵号1+劵号2+劵号3\n)";
		}
		
		return "身份验证成功!";
	}
	
}
