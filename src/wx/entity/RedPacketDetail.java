package wx.entity;

import java.util.Date;

/**
 * 红包明细: 子红包
 */
public class RedPacketDetail {
	/**子红包id 自增长*/
	private int id;
	/**红包口令*/
	private int no;
	/**子红包金额*/
	private double money;
	/**领取的用户id*/
	private int takeuser;
	/**领取时间*/
	private Date taketime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
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
	public Date getTaketime() {
		return taketime;
	}
	public void setTaketime(Date taketime) {
		this.taketime = taketime;
	}
	
}
