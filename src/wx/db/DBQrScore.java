package wx.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import wx.entity.QrScore;
import wx.util.DBUtil;

/**
 * 表QrScore 
 * ID BusNo QrScore QrTime UserID ScanTime
 */
public class DBQrScore {
	
	/**
	 * 添加QrScore
	 * 返回刚刚存入的ID
	 */
	public static int addQrScore(int BusNo, int Score) throws Exception{
		int ID=0;
		String sq="INSERT INTO QrScore (BusNo,QrScore,QrTime) VALUES (?,?,?)";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq,Statement.RETURN_GENERATED_KEYS);
		pst.setInt(1,BusNo);
		pst.setInt(2,Score);
		pst.setLong(3,System.currentTimeMillis());
		pst.executeUpdate();
		ResultSet rs=pst.getGeneratedKeys();
		if (rs.next()) {
			ID=rs.getInt(1);
		}
		DBUtil.close(rs, pst, conn);
		return ID;
	}
	
	/**
	 * 根据ID获取参数
	 * 没有查到返回null
	 */
	public static QrScore getQrScore(int QrScoreID) throws Exception{
		QrScore mQrScore=null;
		String sq="SELECT*FROM QrScore WHERE ID=?";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1, QrScoreID);
		ResultSet rs=pst.executeQuery();
		if (rs.next()) {
			mQrScore=new QrScore();
			mQrScore.ID=QrScoreID;
			mQrScore.BusNo=rs.getInt("BusNo");
			mQrScore.QrScore=rs.getInt("QrScore");
			mQrScore.QrTime=rs.getLong("QrTime");  
			mQrScore.UserID=rs.getInt("UserID");
			mQrScore.ScanTime=rs.getLong("ScanTime");
		}
		DBUtil.close(rs, pst, conn);
		return mQrScore;
	}
	
	/**
	 * 查询当前用户 在当前商家最新扫描记录
	 */
	public static Long getScanTime(int BusNo,int UserID) throws Exception{
		long ScanTime=0;
		String sq="SELECT ScanTime FROM QrScore WHERE BusNo=? AND UserID=? ORDER BY id DESC LIMIT 1";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1, BusNo);
		pst.setInt(2, UserID);
		ResultSet rs=pst.executeQuery();
		if (rs.next()) {
			ScanTime=rs.getLong("ScanTime");
		}
		DBUtil.close(rs, pst, conn);
		return ScanTime;
	}
	
	/**
	 * 加入用户信息
	 * 修改二维码为被扫码过的状态
	 */
	public static void upScanUser(int QrScoreID,int UserID) throws Exception{
		String sq = "UPDATE QrScore SET UserID=?,ScanTime=? WHERE ID=?";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst = conn.prepareStatement(sq);
		pst.setInt(1,UserID);
		pst.setLong(2,System.currentTimeMillis());
		pst.setInt(3,QrScoreID);
		pst.executeUpdate();
		DBUtil.close(null, pst, conn);
	}
	
}
