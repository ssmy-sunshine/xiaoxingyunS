package wx.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import wx.entity.Group;
import wx.util.DBUtil;

/**
 * 表GroupBuy 
 * ID BusNo SeatNo Type UserID Code
 */
public class DBGroup {
	
	/**
	 * 插入新的数据 绑定BusNo,SeatNo,UserID
	 */
	public static void addGroup(Group mGroup) throws Exception{
		String sq="INSERT INTO GroupBuy (BusNo,SeatNo,UserID) VALUES (?,?,?)";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1,mGroup.BusNo);
		pst.setString(2,mGroup.SeatNo);
		pst.setInt(3,mGroup.UserID);
		pst.executeUpdate();
		DBUtil.close(null, pst, conn);
	}
	
	/**
	 * 删除指定用户没有输入团购密码的数据
	 */
	public static void delete(int UserID) throws Exception {
		String sq = "DELETE FROM GroupBuy WHERE UserID=? AND Code IS NULL";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1,UserID);
		pst.executeUpdate();
		DBUtil.close(null, pst, conn);
	}

	/**
	 * 加入团购券 
	 * count=0 可能已经加入过,不能重复加,需重新扫描进入;或者可能没有选择团购类型
	 */
	public static int updateCode(String Code, String OpenID) throws Exception {
		int count=0;
		String sq = "UPDATE GroupBuy SET Code=?,Time=? WHERE UserID=(SELECT UserID FROM User WHERE OpenID=?) AND Code IS NULL AND Type IS NOT NULL";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setString(1,Code);
		pst.setLong(2,System.currentTimeMillis());
		pst.setString(3,OpenID);
		count=pst.executeUpdate();
		DBUtil.close(null, pst, conn);
		return count;
	}
	
	/**
	 * 加入团购类型 count=0说明已经加入过了 需重新扫描进入
	 */
	public static int updateType(int Type, String OpenID) throws Exception {
		int count=0;
		String sq = "UPDATE GroupBuy SET Type=? WHERE UserID=(SELECT UserID FROM User WHERE OpenID=?) AND Code IS NULL";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1,Type);
		pst.setString(2,OpenID);
		count=pst.executeUpdate();
		DBUtil.close(null, pst, conn);
		return count;
	}

	/**
	 * 查询指定商家的团购劵列表
	 */
	public static ArrayList<Group> getGroupList(int BusNo,int NewID) throws Exception {
		ArrayList<Group> groupList=new ArrayList<Group>();
		String sq="SELECT*FROM GroupBuy WHERE BusNo=? AND ID>?";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1,BusNo);
		pst.setInt(2,NewID);
		ResultSet rs=pst.executeQuery();
		while (rs.next()) {
			Group mGroup=new Group();
			mGroup.ID=rs.getInt("ID");
			mGroup.BusNo=rs.getInt("BusNo");
			mGroup.SeatNo=rs.getString("SeatNo");
			mGroup.Type=rs.getInt("Type");
			mGroup.UserID=rs.getInt("UserID");
			mGroup.Code=rs.getString("Code");
			mGroup.Time=rs.getLong("Time");
			groupList.add(mGroup);
		}
		DBUtil.close(rs, pst, conn);
		return groupList;
	}

	/**
	 * 用户输入了1或2或3选择团购类型后,检查到没有输入电话号码,
	 * 当用户输入号码后,则获取用户当前选择的团购类型
	 */
	public static int getCurType(String OpenID) throws Exception {
		int type=0;
		String sq = "SELECT Type FROM GroupBuy WHERE UserID=(SELECT UserID FROM User WHERE OpenID=?) AND Code IS NULL";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setString(1,OpenID);
		ResultSet rs=pst.executeQuery();
		if (rs.next()) {
			type=rs.getInt("Type");
		}
		DBUtil.close(null, pst, conn);
		return type;
	}

}
