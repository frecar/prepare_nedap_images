import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DB {
	Connection conn;	
	public DB() {
		try{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");		    
			this.conn = DriverManager.getConnection("jdbc:sqlserver://10.0.6.178;databaseName=aeosdb","sa","Grolle@nedap1");
		} catch (Exception e){
			e.printStackTrace();	
		}
	}	
}