package wx.entity;

import java.util.Date;

import wx.util.InfoUtil;

/**
 * 红包明细: 子红包
 */
public class RedPacketDetail extends RedPacket{
	/**领取的用户id*/
	private String takeuser;
	/**领取时间*/
	private String taketime;
	/**邀请者id*/
	private String inviter;
	
	public String getTakeuser() {
		return takeuser;
	}
	public void setTakeuser(String takeuser) {
		this.takeuser = takeuser;
	}
	public String getTaketime() {
		return taketime;
	}
	public void setTaketime(Date taketime) {
		this.taketime = InfoUtil.dateFormat(taketime);
	}
	public String getInviter() {
		return inviter;
	}
	public void setInviter(String inviter) {
		this.inviter = inviter;
	}
	
}
