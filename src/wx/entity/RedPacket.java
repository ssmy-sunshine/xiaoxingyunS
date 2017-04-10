package wx.entity;

public class RedPacket {
	/**自增长id*/
	private int id;
	/**红包口令*/
	private int no;
	/**红包总金额*/
	private double money;
	/**红包个数*/
	private int count;
	/**红包提示语*/
	private String tip;
	/**积分*/
	private int score;
	/**券id*/
	private int ticketId;
	
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
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getTip() {
		return tip!=null ? tip : "欢迎您的下次光临~";
	}
	public void setTip(String tip) {
		this.tip = tip;
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
	
}
