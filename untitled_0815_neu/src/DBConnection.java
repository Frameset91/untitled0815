import java.sql.*;

public class DBConnection {
	private static DBConnection singleton = null;
	private Connection con;

	/**
	 * privater Konstruktor
	 */
	private DBConnection() {
		connect();
	
	}

	/**
	 * Liefert die refernz auf Singleton Instanz zurueck
	 * 
	 * @return Referenz auf die SIngletonInstanz
	 */
	public static DBConnection getInstance() {
		if (singleton == null) {
			singleton = new DBConnection();
		}
		return singleton;
	}

	private void connect() {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			// Aufbauen der Verbindung zu der Datenbank
			this.con = DriverManager
					.getConnection("jdbc:hsqldb:hsql://localhost/",
							"SA", "");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendSelectStatement(String sql) {
		try{
		Statement stmnt = this.con.createStatement();
		ResultSet result = stmnt.executeQuery(sql);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void sendOtherStatement( String sql){
		try{
			Statement stmnt = this.con.createStatement();
			boolean result = stmnt.execute(sql);
			}
			catch(Exception e){
				e.printStackTrace();
			}
	}
	
	
	public static void main(String[] args){
		DBConnection bla = getInstance();
		bla.sendOtherStatement("CREATE TABLE blubblub (spalte1 INTEGER)");
	}

}
