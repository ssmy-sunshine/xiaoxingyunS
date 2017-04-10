package wx.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import wx.entity.QrCode;
import wx.util.DBUtil;

/**
 * 表QrCode 
 * ID Type Data Time
 */
public class DBQrCode {
	
	/**
	 * 添加QrCode
	 * 返回刚刚存入的ID
	 */
	public static int addQrCode(int Type,int Data) throws Exception{
		int ID=0;
		String sq="INSERT INTO QrCode VALUES (NULL,?,?,?)";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq,Statement.RETURN_GENERATED_KEYS);
		pst.setInt(1,Type);
		pst.setInt(2,Data);
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
	public static QrCode getQrCode(int QrCodeID) throws Exception{
		QrCode mQrScore=null;
		String sq="SELECT*FROM QrCode WHERE ID=?";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1, QrCodeID);
		ResultSet rs=pst.executeQuery();
		if (rs.next()) {
			mQrScore=new QrCode();
			mQrScore.ID=rs.getInt("ID");
			mQrScore.Type=rs.getInt("Type");
			mQrScore.Data=rs.getInt("Data");
			mQrScore.Time=rs.getLong("Time");
		}
		DBUtil.close(rs, pst, conn);
		return mQrScore;
	}
	
}
