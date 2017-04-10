package wx.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import wx.entity.User;
import wx.util.DBUtil;

/**
 * 表User
 * UserID OpenID UserName Sex City Tel
 */
public class DBUser {
	
	/**
	 * 根据OpenId 查询User
	 * 没有查到 返回null
	 */
	public static int getUserID(String OpenID) throws Exception{
		int UserID=0;
		String sq="SELECT UserID FROM User WHERE OpenID=?";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setString(1, OpenID);
		ResultSet rs=pst.executeQuery();
		if (rs.next()) {
			UserID = rs.getInt("UserID");
		}
		DBUtil.close(rs, pst, conn);
		return UserID;
	}
	
	/**
	 * 根据OpenID插入新的User
	 * 返回刚刚插入的UserID
	 */
	public static int addUser(User user) throws Exception{
		int UserID=0;
		String sq="INSERT INTO User VALUES (NULL,?,?,?,?,NULL)";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq,Statement.RETURN_GENERATED_KEYS);
		pst.setString(1,user.OpenID);
		pst.setString(2,user.UserName);
		pst.setInt(3,user.Sex);
		pst.setString(4,user.City);
		pst.executeUpdate();
		ResultSet rs=pst.getGeneratedKeys();
		rs.next();
		UserID=rs.getInt(1);
		DBUtil.close(rs, pst, conn);
		return UserID;
	}

	/**
	 * 查询联系方式 没有查到返回null
	 */
	public static String getTel(int UserID) throws Exception {
		String Tel=null;
		String sq="SELECT Tel FROM User WHERE UserID=?";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1, UserID);
		ResultSet rs=pst.executeQuery();
		if (rs.next()) {
			Tel=rs.getString("Tel");
		}
		DBUtil.close(rs, pst, conn);
		return Tel;
	}
	
	/**
	 * 查询联系方式
	 */
	public static String getTel(String OpenID) throws Exception {
		String Tel=null;
		String sq="SELECT Tel FROM User WHERE OpenID=?";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setString(1, OpenID);
		ResultSet rs=pst.executeQuery();
		if (rs.next()) {
			Tel=rs.getString("Tel");
		}
		DBUtil.close(rs, pst, conn);
		return Tel;
	}
	
	/**
	 * 存入电话号码
	 */
	public static void updateTel(String openID, String tel) throws Exception {
		String sq = "UPDATE User SET Tel=? WHERE OpenID=?";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setString(1,tel);
		pst.setString(2,openID);
		pst.executeUpdate();
		DBUtil.close(null, pst, conn);
	}

	public static User getUser(String openID) {
		// TODO Auto-generated method stub
		return null;
	}

}
