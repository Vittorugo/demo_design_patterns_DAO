package application;

import java.util.List;
import java.util.Locale;

import db.DB;
import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		Locale.setDefault(Locale.US);
		
		SellerDao sellerDao = DaoFactory.createSellerDaO();
		 
		List<Seller> list = sellerDao.findAll();
		
		for (Seller s: list) {
			System.out.println(s);
		}
		
		
		DB.closeConnection();
	}
}
