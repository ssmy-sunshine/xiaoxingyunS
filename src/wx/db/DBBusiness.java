package wx.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import wx.entity.Business;
import wx.entity.WeChat;
import wx.util.DBUtil;

/**
 * 商家信息Business
 * BusNo BusPW BusName WaitSale ScoreRate Wife WeChatID AppID Secret
 */
public class DBBusiness {
	
	/**
	 * 根据商家编号查询名字
	 * 没有查到返回null
	 */
	public static String getBusName(int BusNo) throws Exception{
		String name=null;
		String sq="SELECT BusName FROM Business WHERE BusNo=?";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1, BusNo);
		ResultSet rs=pst.executeQuery();
		if (rs.next()) {
			name=rs.getString("BusName");
		}
		DBUtil.close(rs, pst, conn);
		return name;
	}

	/**
	 * 登陆
	 * 获取Business对象 不包含WeChatID AppID Secret
	 */
	public static Business getBusiness(int BusNo,String BusPW) throws Exception {
		Business bus=null;
		String sq="SELECT * FROM Business WHERE BusNO=? AND BusPW=?";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1, BusNo);
		pst.setString(2, BusPW);
		ResultSet rs=pst.executeQuery();
		if (rs.next()) {
			bus=new Business();
			bus.BusNo=BusNo;
			bus.BusPW=BusPW;
			bus.BusName=rs.getString("BusName");
			bus.WaitSale=rs.getString("WaitSale");
			bus.ScoreRate=rs.getString("ScoreRate");
			bus.Wife=rs.getString("Wife");
		}
		DBUtil.close(rs, pst, conn);
		return bus;
	}
	
	/**
	 * 获取wife
	 */
	public static String getWife(int BusNo) throws Exception {
		String Wife=null;
		String sq="SELECT Wife FROM Business WHERE BusNo=?";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1, BusNo);
		ResultSet rs=pst.executeQuery();
		if (rs.next()) {
			Wife=rs.getString("Wife");
		}
		DBUtil.close(rs, pst, conn);
		return Wife;
	}

	/**
	 * 更新指定商家的指定字段
	 * BusPW WaitSale ScoreRate Wife
	 */
	public static void update(int BusNo, String Columns, String NewData) throws Exception {
		String sq;
		if ("BusPW".equals(Columns)) {
			sq = "UPDATE Business SET BusPW=? WHERE BusNo=?";
		} else if ("WaitSale".equals(Columns)) {
			sq = "UPDATE Business SET WaitSale=? WHERE BusNo=?";
		} else if ("ScoreRate".equals(Columns)) {
			sq = "UPDATE Business SET ScoreRate=? WHERE BusNo=?";
		} else{
			sq = "UPDATE Business SET Wife=? WHERE BusNo=?";
		}
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setString(1,NewData);
		pst.setInt(2,BusNo);
		pst.executeUpdate();
		DBUtil.close(null, pst, conn);
	}

	/**
	 * 获取指定商家的WeChatID,AppId,Secret
	 */
	public static WeChat getWeChat(int BusNo) throws Exception {
		WeChat mWeChat=null;
		String sq="SELECT WeChatID,AppId,Secret FROM Business WHERE BusNo=?";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setInt(1, BusNo);
		ResultSet rs=pst.executeQuery();
		if (rs.next()) {
			mWeChat=new WeChat(BusNo);
			mWeChat.WeChatID=rs.getString("WeChatID");
			mWeChat.AppID=rs.getString("AppId");
			mWeChat.Secret=rs.getString("Secret");
		}
		DBUtil.close(rs, pst, conn);
		return mWeChat;
	}
	
	/**
	 * 获取指定微信号的BusNo,AppId,Secret
	 */
	public static WeChat getWeChat(String WeChatID) throws Exception {
		WeChat mWeChat=null;
		String sq="SELECT BusNo,AppId,Secret FROM Business WHERE WeChatID=?";
		Connection conn=DBUtil.getConnection();
		PreparedStatement pst=conn.prepareStatement(sq);
		pst.setString(1, WeChatID);
		ResultSet rs=pst.executeQuery();
		if (rs.next()) {
			mWeChat=new WeChat(rs.getInt("BusNo"));
			mWeChat.WeChatID=WeChatID;
			mWeChat.AppID=rs.getString("AppId");
			mWeChat.Secret=rs.getString("Secret");
		}
		DBUtil.close(rs, pst, conn);
		return mWeChat;
	}
	
}
