package wx.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import wx.entity.RedPacketDetail;
import wx.util.DBUtil;

/**
 * 表 packet_detail
 * id no money takeuser taketime
 */
public class RedPacketDetailDB {
	
	/**
	 * 添加新的红包
	 */
	public void insert(RedPacketDetail redPacketDetail) throws Exception{
		String sq="INSERT INTO packet_detail (no,money) VALUES (?,?)";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1,redPacketDetail.getNo());
		pst.setDouble(2,redPacketDetail.getMoney());
		pst.executeUpdate();
		DBUtil.close(null, pst, conn);
	}
	
	/**
	 * 根据口令查询红包
	 */
	public RedPacketDetail getRedPacketByNo(int no) throws Exception{
		RedPacketDetail redPacketDetail=null;
		String sq="SELECT * FROM packet_detail WHERE no=?";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1, no);
		ResultSet rs=pst.executeQuery();
		if (rs.next()) {
			redPacketDetail=new RedPacketDetail();
			redPacketDetail.setId(rs.getInt("id"));
			redPacketDetail.setNo(rs.getInt("no"));
			redPacketDetail.setMoney(rs.getInt("money"));
			redPacketDetail.setTakeuser(rs.getInt("takeuser"));
			redPacketDetail.setTaketime(rs.getDate("taketime"));
		}
		DBUtil.close(rs, pst, conn);
		return redPacketDetail;
	}
	
}
