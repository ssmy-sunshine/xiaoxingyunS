package wx.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import wx.entity.RedPacketDetail;
import wx.entity.TakeDetail;
import wx.util.DBUtil;

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
	 * 根据口令查询可抢红包 返回红包id
	 */
	public int getCanTakeId(int pass) throws Exception{
		int canTakeId=0;
		String sq="SELECT id FROM packet_detail WHERE pass=? AND takeuser IS NULL LIMIT 1";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1, pass);
		ResultSet rs=pst.executeQuery();
		if(rs.next()) canTakeId=rs.getInt("id");
		DBUtil.close(rs, pst, conn);
		return canTakeId;
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
		if (rs.next()) {
			redPacketDetail=new RedPacketDetail();
			redPacketDetail.setId(rs.getInt("id"));
			redPacketDetail.setPass(rs.getInt("pass"));
			redPacketDetail.setMoney(rs.getInt("money"));
			redPacketDetail.setTakeuser(rs.getString("takeuser"));
			redPacketDetail.setTaketime(rs.getTimestamp("taketime"));
		}
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
			mTakeDetail.setMoney(rs.getInt("money"));
			mTakeDetail.setTakeuser(rs.getString("takeuser"));
			mTakeDetail.setTaketime(rs.getTimestamp("taketime"));
			mTakeDetail.setNickname(rs.getString("nickname"));
			mTakeDetail.setUsericon(rs.getString("usericon"));
			list.add(mTakeDetail);
		}
		DBUtil.close(rs, pst, conn);
		return list;
	}
	
}
