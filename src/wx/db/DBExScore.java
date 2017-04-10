package wx.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import wx.entity.App;
import wx.entity.ExScore;
import wx.util.DBUtil;
import wx.util.InfoUtil;

/**
 * 表ExScore
 * ID BusNo BusName UserID ExCount ExScore ExCode ExMoney ExTime
 */
public class DBExScore {
	
	/**
	 * 添加
	 */
	public static void addExScore(int BusNo, String BusName, int UserID,int ExCount) throws Exception {
		String sq="INSERT INTO ExScore (BusNo,BusName,UserID,ExCount) VALUES (?,?,?,?)";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1,BusNo);
		pst.setString(2,BusName);
		pst.setInt(3,UserID);
		pst.setInt(4,ExCount);
		pst.executeUpdate();
		DBUtil.close(null, pst, conn);
	}

	/**
	 * 查询用户未输入的记录
	 * 没有查到返回null
	 */
	public static ExScore getNoExCode(String OpenID) throws Exception {
		ExScore unExScore=null;
		String sq="SELECT*FROM ExScore WHERE UserID=(SELECT UserID FROM User WHERE OpenID=?) AND ExCode IS NULL";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setString(1,OpenID);
		ResultSet rs=pst.executeQuery();
		if (rs.next()) {
			unExScore=new ExScore();
			unExScore.ID=rs.getInt("ID");
			unExScore.BusNo=rs.getInt("BusNo");
			unExScore.BusName=rs.getString("BusName");
			unExScore.UserID=rs.getInt("UserID");
			unExScore.ExCount=rs.getInt("ExCount");
			unExScore.ExScore=rs.getInt("ExScore");
			unExScore.ExCode=rs.getInt("ExCode");
			unExScore.ExMoney=rs.getFloat("ExMoney");
			unExScore.ExTime=rs.getLong("ExTime");
		}
		DBUtil.close(rs, pst, conn);
		return unExScore;
	}
	
	/**
	 * 查询用户在小码哥商家的未兑换记录
	 * 没有查到返回null
	 */
	public static ExScore getNoUseScore(String OpenID) throws Exception {
		ExScore unExScore=null;
		String sq="SELECT*FROM ExScore WHERE UserID=(SELECT UserID FROM User WHERE OpenID=?)"
				+ " AND ExCode IS NOT NULL AND ExTime IS NULL";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setString(1,OpenID);
		ResultSet rs=pst.executeQuery();
		if (rs.next()) {
			unExScore=new ExScore();
			unExScore.ID=rs.getInt("ID");
			unExScore.BusNo=rs.getInt("BusNo");
			unExScore.BusName=rs.getString("BusName");
			unExScore.UserID=rs.getInt("UserID");
			unExScore.ExCount=rs.getInt("ExCount");
			unExScore.ExScore=rs.getInt("ExScore");
			unExScore.ExCode=rs.getInt("ExCode");
			unExScore.ExMoney=rs.getFloat("ExMoney");
			unExScore.ExTime=rs.getLong("ExTime");
		}
		DBUtil.close(rs, pst, conn);
		return unExScore;
	}
	
	/**
	 * 查询 当前用户在独立服务号的商家中未使用的积分记录
	 * 仅一条 没有查到返回null
	 */
	public static ExScore getNoUseScore(String WeChatID, String OpenID)throws Exception {
		ExScore unExScore=null;
		String sq="SELECT*FROM ExScore WHERE "
				+"BusNo=(SELECT BusNo FROM Business WHERE WeChatID=?) "
				+"AND UserID=(SELECT UserID FROM User WHERE OpenID=?) "
				+"AND ExCode IS NOT NULL AND ExTime IS NULL";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setString(1, WeChatID);
		pst.setString(2, OpenID);
		ResultSet rs=pst.executeQuery();
		if (rs.next()) {
			unExScore=new ExScore();
			unExScore.ID=rs.getInt("ID");
			unExScore.BusNo=rs.getInt("BusNo");
			unExScore.BusName=rs.getString("BusName");
			unExScore.UserID=rs.getInt("UserID");
			unExScore.ExCount=rs.getInt("ExCount");
			unExScore.ExScore=rs.getInt("ExScore");
			unExScore.ExCode=rs.getInt("ExCode");
			unExScore.ExMoney=rs.getFloat("ExMoney");
			unExScore.ExTime=rs.getLong("ExTime");
		}
		DBUtil.close(rs, pst, conn);
		return unExScore;
	}
	
	/**
	 * 查询用户在当前商家未输入或未兑换的记录
	 * 没有查到返回null
	 */
	public static ExScore getUnExScore(int BusNo, int UserID) throws Exception {
		ExScore unExScore=null;
		String sq="SELECT*FROM ExScore WHERE BusNo=? AND UserID=? AND ExTime IS NULL";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1, BusNo);
		pst.setInt(2, UserID);
		ResultSet rs=pst.executeQuery();
		if (rs.next()) {
			unExScore=new ExScore();
			unExScore.ID=rs.getInt("ID");
			unExScore.BusNo=rs.getInt("BusNo");
			unExScore.BusName=rs.getString("BusName");
			unExScore.UserID=rs.getInt("UserID");
			unExScore.ExCount=rs.getInt("ExCount");
			unExScore.ExScore=rs.getInt("ExScore");
			unExScore.ExCode=rs.getInt("ExCode");
			unExScore.ExMoney=rs.getFloat("ExMoney");
			unExScore.ExTime=rs.getLong("ExTime");
		}
		DBUtil.close(rs, pst, conn);
		return unExScore;
	}
	
	/**
	 * 删除用户所有未输入和未兑换的记录
	 */
	public static void delUnExScore(int UserID) throws Exception {
		String sq = "DELETE FROM ExScore WHERE UserID=? AND ExTime IS NULL";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1,UserID);
		pst.executeUpdate();
		DBUtil.close(null, pst, conn);
	}
	
	/**
	 * 查询 当前用户在所有商家的已兑换的积分列表
	 * 最新兑换的 在前面
	 */
	public static ArrayList<ExScore> getExScore(String OpenID) throws Exception {
		ArrayList<ExScore> unExScoreList=new ArrayList<ExScore>();
		String sq="SELECT*FROM ExScore WHERE UserID=(SELECT UserID FROM User WHERE OpenID=?)"
				+ " AND ExTime IS NOT NULL ORDER BY ExTime DESC";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setString(1,OpenID);
		ResultSet rs=pst.executeQuery();
		while (rs.next()) {
			ExScore unExScore=new ExScore();
			unExScore.ID=rs.getInt("ID");
			unExScore.BusNo=rs.getInt("BusNo");
			unExScore.BusName=rs.getString("BusName");
			unExScore.UserID=rs.getInt("UserID");
			unExScore.ExCount=rs.getInt("ExCount");
			unExScore.ExScore=rs.getInt("ExScore");
			unExScore.ExCode=rs.getInt("ExCode");
			unExScore.ExMoney=rs.getFloat("ExMoney");
			unExScore.ExTime=rs.getLong("ExTime");
			unExScoreList.add(unExScore);
		}
		DBUtil.close(rs, pst, conn);
		return unExScoreList;
	}
	
	/**
	 * 查询 当前用户在独立服务号的商家的已兑换的积分列表
	 * 最新兑换的 在前面
	 */
	public static ArrayList<ExScore> getExScore(String WeChatID,String OpenID) throws Exception {
		ArrayList<ExScore> unExScoreList=new ArrayList<ExScore>();
		String sq="SELECT*FROM ExScore WHERE"
				+" BusNo=(SELECT BusNo FROM Business WHERE WeChatID=?)"
				+" AND UserID=(SELECT UserID FROM User WHERE OpenID=?)"
				+ " AND ExTime IS NOT NULL ORDER BY ExTime DESC";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setString(1,WeChatID);
		pst.setString(2,OpenID);
		ResultSet rs=pst.executeQuery();
		while (rs.next()) {
			ExScore unExScore=new ExScore();
			unExScore.ID=rs.getInt("ID");
			unExScore.BusNo=rs.getInt("BusNo");
			unExScore.BusName=rs.getString("BusName");
			unExScore.UserID=rs.getInt("UserID");
			unExScore.ExCount=rs.getInt("ExCount");
			unExScore.ExScore=rs.getInt("ExScore");
			unExScore.ExCode=rs.getInt("ExCode");
			unExScore.ExMoney=rs.getFloat("ExMoney");
			unExScore.ExTime=rs.getLong("ExTime");
			unExScoreList.add(unExScore);
		}
		DBUtil.close(rs, pst, conn);
		return unExScoreList;
	}
	
	/**
	 * 获取不同的兑换码
	 * 如果算了2次都存在 则返回0
	 */
	public static int getNewExCode(int BusNo) throws Exception {
		int unExCode=0;
		boolean exist=false;
		for(int i=0;i<2;i++){//最多算2次!如果不行则提示失败 
			unExCode=InfoUtil.getRandom(App.ExCodeLen);
			String sq="SELECT ExCode FROM ExScore WHERE BusNo=? AND ExCode=?";
			Connection conn=DBUtil.getConnection();
			PreparedStatement pst=conn.prepareStatement(sq);
			pst.setInt(1,BusNo);
			pst.setInt(2,unExCode);
			ResultSet rs=pst.executeQuery();
			exist=rs.next();
			DBUtil.close(rs, pst, conn);
			if(exist){
				unExCode=0;
			}else{
				break;
			}
		}
		return unExCode;
	}
	
	/**
	 * 更新兑换的积分
	 */
	public static int upExScore(int NewExScore, int NewExCode,int BusNo,int UserID) throws Exception {
		String sq = "UPDATE ExScore SET ExScore=?,ExCode=? WHERE BusNo=? AND UserID=? AND ExTime IS NULL;";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1,NewExScore);
		pst.setInt(2,NewExCode);
		pst.setInt(3,BusNo);
		pst.setInt(4,UserID);
		int upCount=pst.executeUpdate();
		DBUtil.close(null, pst, conn);
		return upCount;
	}

	/**
	 * 查询兑换码
	 */
	public static ExScore getExScore(int BusNo, int ExCode) throws Exception {
		ExScore mExScore=null;
		String sq="SELECT*FROM ExScore WHERE BusNo=? AND ExCode=?";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1,BusNo);
		pst.setInt(2,ExCode);
		ResultSet rs=pst.executeQuery();
		if (rs.next()) {
			mExScore=new ExScore();
			mExScore.ID=rs.getInt("ID");
			mExScore.BusNo=rs.getInt("BusNo");
			mExScore.BusName=rs.getString("BusName");
			mExScore.UserID=rs.getInt("UserID");
			mExScore.ExCount=rs.getInt("ExCount");
			mExScore.ExScore=rs.getInt("ExScore");
			mExScore.ExCode=rs.getInt("ExCode");
			mExScore.ExMoney=rs.getFloat("ExMoney");
			mExScore.ExTime=rs.getLong("ExTime");
		}
		DBUtil.close(rs, pst, conn);
		return mExScore;
	}

	/**
	 * 确定兑换
	 */
	public static void upExTime(int ID,float ExMoney) throws Exception {
		String sq = "UPDATE ExScore SET ExMoney=?,ExTime=? WHERE ID=?";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setFloat(1,ExMoney);
		pst.setLong(2,System.currentTimeMillis());
		pst.setInt(3,ID);
		pst.executeUpdate();
		DBUtil.close(null, pst, conn);
	}

	/**
	 * 查询用户在商家的兑换次数
	 */
	public static int getExCount(int BusNo, int UserID) throws Exception {
		int ExCount=0;
		String sq="SELECT ExCount FROM ExScore WHERE BusNo=? AND UserID=? AND ExTime IS NOT NULL ORDER BY ExTime DESC LIMIT 1";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1,BusNo);
		pst.setInt(2,UserID);
		ResultSet rs=pst.executeQuery();
		if(rs.next()){
			ExCount=rs.getInt("ExCount");
		}
		DBUtil.close(rs, pst, conn);
		return ExCount;
	}


}
