package wx.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import wx.entity.RedPacket;
import wx.util.DBUtil;

/**
 * 表 packets
 * id no money count remark score ticketid taketype createtime
 */
public class RedPacketDB {
	
	/**
	 * 添加新的红包
	 */
	public void insert(RedPacket redPacket) throws Exception{
		String sq="INSERT INTO packets (no,money,count,remark,score,ticketid,taketype) VALUES (?,?,?,?,?,?,?)";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1,redPacket.getNo());
		pst.setDouble(2,redPacket.getMoney());
		pst.setInt(3,redPacket.getCount());
		pst.setString(4,redPacket.getRemark());
		pst.setInt(5,redPacket.getScore());
		pst.setInt(6,redPacket.getTicketId());
		pst.setInt(7,redPacket.getTaketype());
		pst.executeUpdate();
		DBUtil.close(null, pst, conn);
	}
	
	/**
	 * 查询口令是否存在
	 */
	public boolean isNoExist(int no) throws Exception{
		String sq="SELECT id FROM packets WHERE no=?";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1, no);
		ResultSet rs=pst.executeQuery();
		boolean isExist=rs.next();
		DBUtil.close(rs, pst, conn);
		return isExist;
	}
	
	/**
	 * 根据口令查询红包
	 */
	public RedPacket getRedPacketByNo(int no) throws Exception{
		RedPacket redPacket=null;
		String sq="SELECT * FROM packets WHERE no=?";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1, no);
		ResultSet rs=pst.executeQuery();
		if (rs.next()) {
			redPacket=new RedPacket();
			redPacket.setId(rs.getInt("id"));
			redPacket.setNo(rs.getInt("no"));
			redPacket.setMoney(rs.getInt("money"));
			redPacket.setCount(rs.getInt("count"));
			redPacket.setRemark(rs.getString("remark"));
			redPacket.setScore(rs.getInt("score"));
			redPacket.setTicketId(rs.getInt("ticketid"));
			redPacket.setTaketype(rs.getInt("taketype"));
			redPacket.setCreatetime(rs.getDate("createtime"));
		}
		DBUtil.close(rs, pst, conn);
		return redPacket;
	}
	
}
