/**
 * 
 */
package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author rolmicw
 *
 */
public class DBClass {
	public Connection getConnection() throws ClassNotFoundException, SQLException{       
	Class.forName("com.mysql.jdbc.Driver");
	return DriverManager.getConnection("jdbc:mysql://www.db4free.net:3306/project4\", \"mikerolfe\", \"#OnTrack1!");
	}
}
