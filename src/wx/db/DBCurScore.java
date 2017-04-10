package wx.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import wx.entity.CurScore;
import wx.util.DBUtil;

/**
 * 商家与用户积分关系 CurScore表
 * ID BusNo BusName UserID CurScore
 */
public class DBCurScore {
	
	/**
	 * 查询 指定用户在指定商家的总积分
	 */
	public static CurScore getCurScore(int BusNo,int UserID) throws Exception{
		CurScore mCurScore=null;
		String sq="SELECT*FROM CurScore WHERE BusNo=? AND UserID=?";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1, BusNo);
		pst.setInt(2, UserID);
		ResultSet rs=pst.executeQuery();
		if (rs.next()) {
			mCurScore=new CurScore();
			mCurScore.ID=rs.getInt("ID");
			mCurScore.BusNo=rs.getInt("BusNo");
			mCurScore.BusName=rs.getString("BusName");
			mCurScore.UserID=rs.getInt("UserID");
			mCurScore.CurScore=rs.getInt("CurScore");
			mCurScore.GetScore=rs.getBoolean("GetScore");
		}
		DBUtil.close(rs, pst, conn);
		return mCurScore;
	}
	
	/**
	 * 查询 指定用户在所有商家的积分数
	 * 按照积分从大到小排列
	 */
	public static ArrayList<CurScore> getScoreList(String OpenID) throws Exception {
		ArrayList<CurScore> scoreList=new ArrayList<CurScore>();
		String sq="SELECT*FROM CurScore WHERE UserID=(SELECT UserID FROM User WHERE OpenID=?) ORDER BY CurScore DESC";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setString(1, OpenID);
		ResultSet rs=pst.executeQuery();
		while (rs.next()) {
			CurScore mCurScore=new CurScore();
			mCurScore.ID=rs.getInt("ID");
			mCurScore.BusNo=rs.getInt("BusNo");
			mCurScore.BusName=rs.getString("BusName");
			mCurScore.UserID=rs.getInt("UserID");
			mCurScore.CurScore=rs.getInt("CurScore");
			scoreList.add(mCurScore);
		}
		DBUtil.close(rs, pst, conn);
		return scoreList;
	}
	
	/**
	 * 添加
	 */
	public static void addCurScore(CurScore mCurScore) throws Exception{
		String sq="INSERT INTO CurScore VALUES(NULL,?,?,?,?,?)";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1,mCurScore.BusNo);
		pst.setString(2,mCurScore.BusName);
		pst.setInt(3,mCurScore.UserID);
		pst.setInt(4,mCurScore.CurScore);
		pst.setBoolean(5,mCurScore.GetScore);
		pst.executeUpdate();
		DBUtil.close(null, pst, conn);
	}

	/**
	 * 加入新的积分
	 */
	public static void UpdateCurScore(CurScore mCurScore) throws Exception {
		String sq = "UPDATE CurScore SET CurScore=? WHERE BusNo=? AND UserID=?";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst = conn.prepareStatement(sq);
		pst.setInt(1,mCurScore.CurScore);
		pst.setInt(2,mCurScore.BusNo);
		pst.setInt(3,mCurScore.UserID);
		pst.executeUpdate();
		DBUtil.close(null, pst, conn);
	}
	
	/**
	 * 加入新的积分
	 */
	public static void UpdateCurScore(int NewScore,int ID) throws Exception {
		String sq = "UPDATE CurScore SET CurScore=?,GetScore=true WHERE ID=?";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst = conn.prepareStatement(sq);
		pst.setInt(1,NewScore);
		pst.setInt(2,ID);
		pst.executeUpdate();
		DBUtil.close(null, pst, conn);
	}

	/**
	 * 减去已兑换的积分
	 */
	public static void UpdateCurScore(int BusNo,int UserID,int ExScore) throws Exception {
		String sq = "UPDATE CurScore SET CurScore=CurScore-? WHERE BusNo=? AND UserID=?";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst = conn.prepareStatement(sq);
		pst.setInt(1,ExScore);
		pst.setInt(2,BusNo);
		pst.setInt(3,UserID);
		pst.executeUpdate();
		DBUtil.close(null, pst, conn);
	}

	/**
	 * 查询用户在当前商家的剩余积分;
	 */
	public static CurScore getCurScore(String WeChatID, String OpenID) throws Exception {
		CurScore mCurScore=null;
		String sq="SELECT * FROM CurScore WHERE "
				+ "BusNo=(SELECT BusNo FROM Business WHERE WeChatID=?) "
				+ "AND UserID=(SELECT UserID FROM User WHERE OpenID=?)";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setString(1, WeChatID);
		pst.setString(2, OpenID);
		ResultSet rs=pst.executeQuery();
		if (rs.next()) {
			mCurScore=new CurScore();
			mCurScore.ID=rs.getInt("ID");
			mCurScore.BusNo=rs.getInt("BusNo");
			mCurScore.BusName=rs.getString("BusName");
			mCurScore.UserID=rs.getInt("UserID");
			mCurScore.CurScore=rs.getInt("CurScore");
		}
		DBUtil.close(rs, pst, conn);
		return mCurScore;
	}

}
