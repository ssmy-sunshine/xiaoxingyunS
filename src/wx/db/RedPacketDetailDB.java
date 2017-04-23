package wx.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import wx.entity.RedPacketDetail;
import wx.entity.TakeDetail;
import wx.util.DBUtil;
import wx.util.InfoUtil;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * 表 packet_detail
 * id pass money takeuser taketime
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
	 * 根据口令查询可抢红包
	 */
	public RedPacketDetail getCanTakeId(int pass) throws Exception{
		RedPacketDetail redPacketDetail=null;
		String sq="SELECT * FROM packet_detail WHERE pass=? AND takeuser IS NULL LIMIT 1";
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
		String sq="SELECT * FROM packet_detail WHERE pass=? AND takeuser=? LIMIT 1";
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
	 * 根据id查询红包
	 */
	public RedPacketDetail getById(int id) throws Exception{
		RedPacketDetail redPacketDetail=null;
		String sq="SELECT * FROM packet_detail WHERE id=?";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1, id);
		ResultSet rs=pst.executeQuery();
		if(rs.next()) redPacketDetail=getByResultSet(rs);
		DBUtil.close(rs, pst, conn);
		return redPacketDetail;
	}
	
	/**
	 * 加入用户信息
	 * 修改二维码为被扫码过的状态
	 */
	public boolean updateTakeUser(int id,String takeuser) throws Exception{
		String sq = "UPDATE packet_detail SET takeuser=?,taketime=now() WHERE id=?";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst = conn.prepareStatement(sq);
		pst.setString(1,takeuser);
		pst.setInt(2,id);
		int count=pst.executeUpdate();
		DBUtil.close(null, pst, conn);
		return count>0;
	}

	/**
	 * 查询抢红包明细
	 */
	public ArrayList<TakeDetail> getTakeList(int pass) throws Exception {
		ArrayList<TakeDetail> list=new ArrayList<TakeDetail>();
		String sq="SELECT * FROM packet_detail WHERE pass=? AND takeuser IS NOT NULL";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1, pass);
		ResultSet rs=pst.executeQuery();
		while (rs.next()) {
			TakeDetail mTakeDetail=new TakeDetail();
			mTakeDetail.setPass(rs.getInt("pass"));
			mTakeDetail.setMoney(rs.getDouble("money"));
			mTakeDetail.setTakeuser(rs.getString("takeuser"));
			mTakeDetail.setTaketime(rs.getTimestamp("taketime"));
//			mTakeDetail.setNickname(rs.getString("nickname"));
//			mTakeDetail.setUsericon(rs.getString("usericon"));
			list.add(mTakeDetail);
		}
		DBUtil.close(rs, pst, conn);
		return list;
	}
	
	/**
	 * 查询当前用户金额,优惠券,积分总数
	 */
	public JsonObject getAllData(String takeuser) throws Exception {
		String sq="SELECT SUM(a.money) allprice,SUM(b.score) allscore,COUNT(b.ticketid) numticket FROM packet_detail a JOIN packets b ON a.pass=b.pass WHERE takeuser=?";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setString(1, takeuser);
		ResultSet rs=pst.executeQuery();
		JsonObject obj=new JsonObject();
		if(rs.next()){
			obj.addProperty("allprice", rs.getDouble("allprice"));
			obj.addProperty("allscore", rs.getInt("allscore"));
			obj.addProperty("numticket", rs.getInt("numticket"));
		}
		DBUtil.close(rs, pst, conn);
		return obj;
	}
	
	/**
	 * 查询当前用户金额明细
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
		return redPacketDetail;
	}
	
}
