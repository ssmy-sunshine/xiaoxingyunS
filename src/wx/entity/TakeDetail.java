package wx.entity;

import wx.util.InfoUtil;

/**
 *抢红包明细
 **/
public class TakeDetail extends RedPacketDetail{
	/**昵称*/
	private String nickname;
	/**头像地址*/
	private String usericon;
	
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		if(InfoUtil.isTextEmpty(nickname)) nickname="匿名";
		this.nickname = nickname;
	}
	public String getUsericon() {
		return usericon;
	}
	public void setUsericon(String usericon) {
		this.usericon = usericon;
	}
}
