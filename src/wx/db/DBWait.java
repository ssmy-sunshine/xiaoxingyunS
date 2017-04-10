package wx.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import wx.entity.Wait;
import wx.util.DBUtil;
import wx.util.InfoUtil;

/**
 * 表Wait
 * ID,BusNo,UserID,WaitNo,OutDate,TimeStart,TimeEnd
 */
public class DBWait {
	
	/**
	 * 在指定商家加入新的排位号
	 */
	public static void addWaitNo(int BusNo,int WaitNo) throws Exception {
		String sq="INSERT INTO Wait (BusNo,WaitNo,TimeStart) VALUES (?,?,?)";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1,BusNo);
		pst.setInt(2,WaitNo);
		pst.setLong(3,System.currentTimeMillis());
		pst.executeUpdate();
		DBUtil.close(null, pst, conn);
	}

	/**
	 * 查询商家今天最新排位号
	 */
	public static int getWaitNo(int BusNo) throws Exception {
		int WaitNo=0;
		String sq="SELECT WaitNo FROM Wait WHERE BusNo=? AND TimeStart>=? ORDER BY id DESC Limit 1";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1,BusNo);
		pst.setLong(2,InfoUtil.getTodayMillis());
		ResultSet rs=pst.executeQuery();
		if (rs.next()) {
			WaitNo=rs.getInt("WaitNo");
		}
		DBUtil.close(rs, pst, conn);
		return WaitNo;
	}

	/**
	 * 给BusNo+WaitNo绑定UserID
	 */
	public static void bandUserID(int BusNo, int WaitNo, int UserID) throws Exception {
		String sq = "UPDATE Wait SET UserID=? WHERE BusNo=? AND WaitNo=? AND TimeStart>=?";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst = conn.prepareStatement(sq);
		pst.setInt(1,UserID);
		pst.setInt(2,BusNo);
		pst.setInt(3,WaitNo);
		pst.setLong(4,InfoUtil.getTodayMillis());
		pst.executeUpdate();
		DBUtil.close(null, pst, conn);
	}

	/**
	 * 获取在指定商家,指定排位号前面还有多少人排位
	 * WaitCount=0 要么已入座 要么轮到自己入座 则另外需再查询
	 */
	public static int getWaitCount(int BusNo, int WaitNo) throws Exception {
		int WaitCount=0;
		String sq="SELECT COUNT(id) FROM wait WHERE BusNo=? AND OutDate=false AND TimeStart>=? AND WaitNo<?";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1,BusNo);
		pst.setLong(2,InfoUtil.getTodayMillis());
		pst.setInt(3,WaitNo);
		ResultSet rs=pst.executeQuery();
		if (rs.next()) {
			WaitCount=rs.getInt(1);
		}
		DBUtil.close(rs, pst, conn);
		return WaitCount;
	}

	/**
	 * 获取指定商家指定排号的Wait对象
	 */
	public static Wait getWaitInfo(int BusNo, int WaitNo) throws Exception {
		Wait wait=null;
		String sq="SELECT*FROM wait WHERE BusNo=? AND WaitNo=?";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1,BusNo);
		pst.setInt(2,WaitNo);
		ResultSet rs=pst.executeQuery();
		if (rs.next()) {
			wait=new Wait();
			wait.ID=rs.getInt("ID");
			wait.BusNo=rs.getInt("BusNo");
			wait.UserID=rs.getInt("UserID");
			wait.WaitNo=rs.getInt("WaitNo");
			wait.OutDate=rs.getBoolean("OutDate");
			wait.TimeStart=rs.getLong("TimeStart");
			wait.TimeEnd=rs.getLong("TimeEnd");
		}
		DBUtil.close(rs, pst, conn);
		return wait;
	}

	/**
	 * 获取用户今天的Wait列表
	 */
	public static ArrayList<Wait> getWaitList(String OpenID) throws Exception {
		ArrayList<Wait> waitList=new ArrayList<Wait>();
		String sq="SELECT*FROM wait WHERE UserID=(SELECT UserID FROM User WHERE OpenID=?) AND TimeStart>?;";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setString(1,OpenID);
		pst.setLong(2,InfoUtil.getTodayMillis());
		ResultSet rs=pst.executeQuery();
		while (rs.next()) {
			Wait wait=new Wait();
			wait.ID=rs.getInt("ID");
			wait.BusNo=rs.getInt("BusNo");
			wait.UserID=rs.getInt("UserID");
			wait.WaitNo=rs.getInt("WaitNo");
			wait.OutDate=rs.getBoolean("OutDate");
			wait.TimeStart=rs.getLong("TimeStart");
			wait.TimeEnd=rs.getLong("TimeEnd");
			waitList.add(wait);
		}
		DBUtil.close(rs, pst, conn);
		return waitList;
	}

	/**
	 * 入座
	 * 更新指定商家,指定排位号的TimeEnd为当前时间
	 * upCount=0 不存在或者已经过期;
	 */
	public static int upTimeEnd(int BusNo, int WaitNo) throws Exception {
		String sq = "UPDATE Wait SET OutDate=true,TimeEnd=? WHERE BusNo=? AND WaitNo=? AND OutDate=false AND TimeStart>=?";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst = conn.prepareStatement(sq);
		pst.setLong(1,System.currentTimeMillis());
		pst.setInt(2,BusNo);
		pst.setInt(3,WaitNo);
		pst.setLong(4,InfoUtil.getTodayMillis());
		int upCount=pst.executeUpdate();
		DBUtil.close(null, pst, conn);
		return upCount;
	}

	/**
	 * 入座
	 * 把今天的指定商家指定排位号之前的所有没有过期的排位号置为过期
	 */
	public static void upOutDate(int BusNo, int WaitNo) throws Exception {
		String sq = "UPDATE Wait SET OutDate=true WHERE BusNo=? AND WaitNo<? AND OutDate=false AND TimeStart>=?";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst = conn.prepareStatement(sq);
		pst.setInt(1,BusNo);
		pst.setInt(2,WaitNo);
		pst.setLong(3,InfoUtil.getTodayMillis());
		pst.executeUpdate();
		DBUtil.close(null, pst, conn);
	}
	
}
