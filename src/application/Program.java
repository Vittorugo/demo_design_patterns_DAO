package application;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;

import db.DB;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
				
		Connection connection = DB.getConnection();
		DB.closeConnection();
	}

}
