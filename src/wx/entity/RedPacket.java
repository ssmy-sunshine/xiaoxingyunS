package wx.entity;


import java.sql.Timestamp;

import wx.util.InfoUtil;

public class RedPacket {
	/**自增长id*/
	private int id;
	/**红包口令*/
	private int pass;
	/**红包总金额*/
	private double money;
	/**红包个数*/
	private int count;
	/**商家id*/
	private int busid;
	/**红包提示语*/
	private String remark;
	/**积分*/
	private int score;
	/**券id*/
	private int ticketId;
	/**0拼手气;1平均分*/
	private int taketype;
	/**0拼手气;1平均分*/
	private String taketypetip;
	/**创建时间*/
	private String createtime;
	/**红包类型:普通红包,裂变红包*/
	private String bagType;
	/**裂变红包奖励*/
	private double profit;
	/**最多奖励*/
	private double maxprofit;
	/**通过哪个红包发放的*/
	private int morepass;
	
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
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getBusid() {
		return busid;
	}
	public void setBusid(int busid) {
		this.busid = busid;
	}
	public String getRemark() {
		return InfoUtil.isTextEmpty(remark) ? "欢迎您的下次光临~" : remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getTicketId() {
		return ticketId;
	}
	public void setTicketId(int ticketId) {
		this.ticketId = ticketId;
	}
	public int getTaketype() {
		return taketype;
	}
	public void setTaketype(int taketype) {
		this.taketype = taketype;
		if(taketype==1){
			setTaketypetip("平均分");
		}else{
			setTaketypetip("拼手气");
		}
	}
	public String getTaketypetip() {
		return taketypetip;
	}
	public void setTaketypetip(String taketypetip) {
		this.taketypetip = taketypetip;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Timestamp createtime) {
		this.createtime = InfoUtil.dateFormat(createtime);
	}
	public double getProfit() {
		return profit;
	}
	public void setProfit(double profit) {
		this.profit = profit;
		if(this.profit>0&&this.morepass==0){
			setBagType("裂变红包");
		}else{
			setBagType("普通红包");
		}
	}
	public double getMaxprofit() {
		return maxprofit;
	}
	public void setMaxprofit(double maxprofit) {
		this.maxprofit = maxprofit;
	}
	public int getMorepass() {
		return morepass;
	}
	public void setMorepass(int morepass) {
		this.morepass = morepass;
		if(this.profit>0&&this.morepass==0){
			setBagType("裂变红包");
		}else{
			setBagType("普通红包");
		}
	}
	public String getBagType() {
		return bagType;
	}
	public void setBagType(String bagType) {
		this.bagType = bagType;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	
}
