package wx.entity;

import java.lang.reflect.Type;

import wx.db.DBUser;
import wx.servlet.TicketSL;
import wx.util.HttpUtil;

import com.google.gson.reflect.TypeToken;

/**
 * 用户信息
 * UserID OpenID UserName Sex City Tel
 */
public class User {
	/**
	 * ID
	 */
	public int UserID;
	
	/**
	 * OpenID
	 */
	public String OpenID;
	
	/**
	 * 昵称
	 */
	public String UserName;
	
	/**
	 * 性别 1男,2女,0未知
	 */
	public int Sex;
	
	/**
	 * 城市
	 */
	public String City;
	
	/**
	 * 电话
	 */
	public String Tel;
	
	/**
	 * 根据OpenID查询数据库 返回User对象
	 * 如果User==null 则联网获取User 并写入数据库
	 */
	public static int getUserID(String OpenID,String WeChatID) throws Exception{
		//查询数据库 若没有查到则联网查询
		int UserID=DBUser.getUserID(OpenID);
		if (UserID==0) {
			//联网获取User
			String userInfoUrl=TicketSL.getUserUrl(OpenID,WeChatID);
			String json=HttpUtil.HttpGet(userInfoUrl);
			System.out.println("获取的用户信息=="+json);
			Type type=new TypeToken<UserInfo>(){}.getType();
			UserInfo mUserInfo=App.Gson.fromJson(json, type);
			User mUser=new User();
			mUser.OpenID=OpenID;
			//去掉特殊字符 仅仅保留中文,数字,英文; \u4e00-\u9fa5 代表中文，\\w代表英文、数字和“_"
			//中括号:任意字符; 加号:至少出现一次
			mUser.UserName=mUserInfo.nickname.replaceAll("[^\u4e00-\u9fa5\\w]+", "");
			if (mUser.UserName.length()>=16) {
				mUser.UserName=mUser.UserName.substring(0,15);
			}
			mUser.Sex=mUserInfo.sex;
			mUser.City=mUserInfo.city;
			//保存至数据库
			UserID=DBUser.addUser(mUser);
			System.out.println("添加新用户的UserID=="+UserID);
		}
		return UserID;
	}
	
	private class UserInfo{
		private String nickname;
		private int sex;//1男,2女,0未知
		private String city;//广州
//		private int subscribe;//是否关注
//		private String openid;
//		private String language;//zh_CN
//		private String province;//广东
//		private String country;//中国
//		private String headimgurl;//头像
//		private long subscribe_time;//关注时间
	}
	
}
