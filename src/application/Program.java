package application;

import java.sql.Connection;
import db.DB;
import db.DbException;

public class Program {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connection connection = DB.getConnection();
		DB.closeConnection();
	}

}
