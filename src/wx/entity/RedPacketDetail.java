package wx.entity;

import java.util.Date;

import wx.util.InfoUtil;

/**
 * 红包明细: 子红包
 */
public class RedPacketDetail {
	/**子红包id 自增长*/
	private int id;
	/**红包口令*/
	private int pass;
	/**子红包金额*/
	private double money;
	/**领取的用户id*/
	private int takeuser;
	/**领取时间*/
	private String taketime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPass() {
		return pass;
	}
	public void setPass(int pass) {
		this.pass = pass;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public int getTakeuser() {
		return takeuser;
	}
	public void setTakeuser(int takeuser) {
		this.takeuser = takeuser;
	}
	public String getTaketime() {
		return taketime;
	}
	public void setTaketime(Date taketime) {
		this.taketime = InfoUtil.dateFormat(taketime);
	}
	
}
