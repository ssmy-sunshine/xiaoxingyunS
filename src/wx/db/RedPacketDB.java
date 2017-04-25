package wx.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import wx.entity.RedPacket;
import wx.util.DBUtil;

/**
 * 表 packets
 * id pass money count busid remark score ticketid taketype createtime profit maxprofit morepass
 */
public class RedPacketDB {
	
	/**
	 * 添加新的红包
	 */
	public void insert(RedPacket redPacket) throws Exception{
		String sq="INSERT INTO packets (pass,money,count,busid,remark,score,ticketid,taketype,profit,maxprofit,morepass) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1,redPacket.getPass());
		pst.setDouble(2,redPacket.getMoney());
		pst.setInt(3,redPacket.getCount());
		pst.setInt(4,redPacket.getBusid());
		pst.setString(5,redPacket.getRemark());
		pst.setInt(6,redPacket.getScore());
		pst.setInt(7,redPacket.getTicketId());
		pst.setInt(8,redPacket.getTaketype());
		pst.setDouble(9,redPacket.getProfit());
		pst.setDouble(10,redPacket.getMaxprofit());
		pst.setInt(11,redPacket.getMorepass());
		pst.executeUpdate();
		DBUtil.close(null, pst, conn);
	}
	
	/**
	 * 查询口令是否存在
	 */
	public boolean isPassExist(int pass) throws Exception{
		String sq="SELECT id FROM packets WHERE pass=?";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1, pass);
		ResultSet rs=pst.executeQuery();
		boolean isExist=rs.next();
		DBUtil.close(rs, pst, conn);
		return isExist;
	}
	
	/**
	 * 根据口令查询红包
	 */
	public RedPacket getByPass(int pass) throws Exception{
		RedPacket redPacket=null;
		String sq="SELECT * FROM packets WHERE pass=?";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1, pass);
		ResultSet rs=pst.executeQuery();
		if (rs.next()) {
			redPacket=getByResultSet(rs);
		}
		DBUtil.close(rs, pst, conn);
		return redPacket;
	}
	
	/**
	 * 根据商家查询红包列表
	 * @param busid 商家id
	 * @param no 第几页
	 * @param size 一页多少
	 */
	public ArrayList<RedPacket> getList(int busid,int no,int size) throws Exception{
		ArrayList<RedPacket> list=new ArrayList<RedPacket>();
		String sq="SELECT * FROM packets WHERE busid=? ORDER BY id DESC limit ?,?";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1, busid);
		pst.setInt(2, (no-1)*size);
		pst.setInt(3, size);
		ResultSet rs=pst.executeQuery();
		while (rs.next()) {
			RedPacket redPacket=getByResultSet(rs);
			list.add(redPacket);
		}
		DBUtil.close(rs, pst, conn);
		return list;
	}
	
	/**
	 * 从ResultSet封装RedPacketDetail
	 */
	private RedPacket getByResultSet(ResultSet rs) throws SQLException{
		RedPacket redPacket=new RedPacket();
		redPacket.setId(rs.getInt("id"));
		redPacket.setPass(rs.getInt("pass"));
		redPacket.setMoney(rs.getDouble("money"));
		redPacket.setCount(rs.getInt("count"));
		redPacket.setBusid(rs.getInt("busid"));
		redPacket.setRemark(rs.getString("remark"));
		redPacket.setScore(rs.getInt("score"));
		redPacket.setTicketId(rs.getInt("ticketid"));
		redPacket.setTaketype(rs.getInt("taketype"));
		redPacket.setCreatetime(rs.getTimestamp("createtime"));
		redPacket.setProfit(rs.getDouble("profit"));
		redPacket.setMaxprofit(rs.getDouble("maxprofit"));
		redPacket.setMorepass(rs.getInt("morepass"));
		return redPacket;
	}
}
