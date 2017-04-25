package wx.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import wx.entity.RedPacketDetail;
import wx.util.DBUtil;
import wx.util.InfoUtil;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * 表 packet_detail
 * id pass money takeuser taketime inviter
 */
public class RedPacketDetailDB {
	
	/**
	 * 添加新的红包
	 */
	public void insert(RedPacketDetail redPacketDetail) throws Exception{
		String sq="INSERT INTO packet_detail (pass,money) VALUES (?,?)";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1,redPacketDetail.getPass());
		pst.setDouble(2,redPacketDetail.getMoney());
		pst.executeUpdate();
		DBUtil.close(null, pst, conn);
	}
	
	/**
	 * 给邀请人添加奖励的红包
	 */
	public void insertForInviter(int pass,String takeuser) throws Exception{
		String sq="INSERT INTO packet_detail (pass,money,takeuser,taketime,remark) VALUES (88888888,(SELECT profit FROM packets WHERE pass=?),?,NOW(),?)";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1,pass);
		pst.setString(2,takeuser);
		pst.setString(3,"邀请奖励");
		pst.executeUpdate();
		DBUtil.close(null, pst, conn);
	}
	
	/**
	 * 根据口令查询可抢红包
	 */
	public RedPacketDetail getCanTake(int pass) throws Exception{
		RedPacketDetail redPacketDetail=null;
		String sq="SELECT b.*,a.score,a.ticketid,a.profit,a.maxprofit,a.morepass FROM packet_detail b JOIN packets a ON a.pass=b.pass WHERE b.pass=? AND b.takeuser IS NULL LIMIT 1";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1, pass);
		ResultSet rs=pst.executeQuery();
		if(rs.next()) redPacketDetail=getByResultSet(rs);
		DBUtil.close(rs, pst, conn);
		return redPacketDetail;
	}
	
	/**
	 * 根据口令查询当前用户抢的红包
	 */
	public RedPacketDetail getByTakeuser(int pass,String takeuser) throws Exception{
		RedPacketDetail redPacketDetail=null;
		String sq="SELECT b.*,a.score,a.ticketid,a.profit,a.maxprofit,a.morepass FROM packet_detail b JOIN packets a ON a.pass=b.pass WHERE b.pass=? AND b.takeuser=? LIMIT 1";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1, pass);
		pst.setString(2, takeuser);
		ResultSet rs=pst.executeQuery();
		if(rs.next()) redPacketDetail=getByResultSet(rs);
		DBUtil.close(rs, pst, conn);
		return redPacketDetail;
	}
	
	/**
	 * 加入用户信息
	 * 修改二维码为被扫码过的状态
	 */
	public boolean updateTakeUser(int id,String takeuser,String inviter) throws Exception{
		String sq = "UPDATE packet_detail SET takeuser=?,taketime=now(),inviter=? WHERE id=?";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst = conn.prepareStatement(sq);
		pst.setString(1,takeuser);
		pst.setString(2,inviter);
		pst.setInt(3,id);
		int count=pst.executeUpdate();
		DBUtil.close(null, pst, conn);
		return count>0;
	}

	/**
	 * 查询抢红包明细
	 */
	public JsonArray getTakeList(int pass) throws Exception {
		String sq="SELECT a.*,b.profit FROM packet_detail a JOIN packets b ON a.pass=b.pass WHERE a.pass=? AND a.takeuser IS NOT NULL";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1, pass);
		ResultSet rs=pst.executeQuery();
		JsonArray arr=new JsonArray();
		while(rs.next()){
			JsonObject obj=new JsonObject();
			obj.addProperty("pass", rs.getInt("pass"));
			obj.addProperty("money", rs.getDouble("money"));
			obj.addProperty("takeuser", rs.getString("takeuser"));
			obj.addProperty("taketime", InfoUtil.dateFormat(rs.getTimestamp("taketime")));
			obj.addProperty("inviter",rs.getString("inviter"));
			obj.addProperty("profit",rs.getDouble("profit"));
			arr.add(obj);
		}
		DBUtil.close(rs, pst, conn);
		return arr;
	}
	
	/**
	 * 查询当前用户领取红包的总金额,优惠券,积分总数
	 */
	public JsonObject getAllData(String takeuser) throws Exception {
		String sq="SELECT SUM(a.money) alltake,SUM(b.score) allscore,COUNT(b.ticketid) numticket FROM packet_detail a JOIN packets b ON a.pass=b.pass WHERE takeuser=?";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setString(1, takeuser);
		ResultSet rs=pst.executeQuery();
		JsonObject obj=new JsonObject();
		if(rs.next()){
			obj.addProperty("alltake", rs.getDouble("alltake"));
			obj.addProperty("allscore", rs.getInt("allscore"));
			obj.addProperty("numticket", rs.getInt("numticket"));
		}
		DBUtil.close(rs, pst, conn);
		return obj;
	}
	
	/**
	 * 查询当前用户裂变红包的奖励总金额
	 */
	public double getAllProfit(String takeuser) throws Exception {
		String sq="SELECT SUM(a.profit) allprofit FROM packet_detail b JOIN packets a WHERE b.inviter=?";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setString(1, takeuser);
		ResultSet rs=pst.executeQuery();
		double allprofit=0;
		if(rs.next()){
			allprofit=rs.getDouble("allprofit");
		}
		DBUtil.close(rs, pst, conn);
		return allprofit;
	}
	
	/**
	 * 查询当前用户领取红包的金额明细
	 */
	public JsonArray getMoneyDetail(String takeuser) throws Exception {
		String sq="SELECT money,taketime FROM packet_detail WHERE takeuser=?";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setString(1, takeuser);
		ResultSet rs=pst.executeQuery();
		JsonArray arr=new JsonArray();
		while(rs.next()){
			JsonObject obj=new JsonObject();
			obj.addProperty("money", rs.getDouble("money"));
			obj.addProperty("taketime", InfoUtil.dateFormat(rs.getTimestamp("taketime")));
			arr.add(obj);
		}
		DBUtil.close(rs, pst, conn);
		return arr;
	}
	
	/**
	 * 查询当前用户裂变红包的金额明细
	 */
	public JsonArray getProfitDetail(String takeuser) throws Exception {
		String sq="SELECT a.profit money,b.taketime taketime FROM packet_detail b JOIN packets a ON a.pass=b.pass WHERE b.inviter=?";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setString(1, takeuser);
		ResultSet rs=pst.executeQuery();
		JsonArray arr=new JsonArray();
		while(rs.next()){
			JsonObject obj=new JsonObject();
			obj.addProperty("money", rs.getDouble("money"));
			obj.addProperty("taketime", InfoUtil.dateFormat(rs.getTimestamp("taketime")));
			arr.add(obj);
		}
		DBUtil.close(rs, pst, conn);
		return arr;
	}
	
	/**
	 * 查询当前用户积分明细
	 */
	public JsonArray getScoreDetail(String takeuser) throws Exception {
		String sq="SELECT b.score,a.taketime FROM packet_detail a JOIN packets b ON a.pass=b.pass WHERE takeuser=?";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setString(1, takeuser);
		ResultSet rs=pst.executeQuery();
		JsonArray arr=new JsonArray();
		while(rs.next()){
			JsonObject obj=new JsonObject();
			obj.addProperty("score", rs.getDouble("score"));
			obj.addProperty("taketime", InfoUtil.dateFormat(rs.getTimestamp("taketime")));
			arr.add(obj);
		}
		DBUtil.close(rs, pst, conn);
		return arr;
	}
	
	/**
	 * 从ResultSet封装RedPacketDetail
	 */
	private RedPacketDetail getByResultSet(ResultSet rs) throws SQLException{
		RedPacketDetail redPacketDetail=new RedPacketDetail();
		redPacketDetail.setId(rs.getInt("id"));
		redPacketDetail.setPass(rs.getInt("pass"));
		redPacketDetail.setMoney(rs.getDouble("money"));
		redPacketDetail.setTakeuser(rs.getString("takeuser"));
		redPacketDetail.setTaketime(rs.getTimestamp("taketime"));
		redPacketDetail.setScore(rs.getInt("score"));
		redPacketDetail.setTicketId(rs.getInt("ticketid"));
		redPacketDetail.setProfit(rs.getDouble("profit"));
		redPacketDetail.setMaxprofit(rs.getDouble("maxprofit"));
		redPacketDetail.setMorepass(rs.getInt("morepass"));
		return redPacketDetail;
	}
	
}
