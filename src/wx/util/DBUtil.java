package wx.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 连接数据库工具
 */
public class DBUtil {
	
	/**
	 * 建立数据库连接
	 */
	public static Connection getConnection() throws ClassNotFoundException, SQLException{
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager.getConnection(
					    	"jdbc:mysql://localhost:3306/little_lucky?useUnicode=true&characterEncoding=utf8"
					    	,"root","wenju3221");
		return connection;
	}
	
	/**
	 * 关闭连接
	 */
	public static void close(ResultSet rs,Statement stmt,Connection conn){
		try {
			if (rs!=null) rs.close();
			if (stmt!=null) stmt.close();
			if (conn!=null) conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
