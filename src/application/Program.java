package application;

import java.util.Locale;

import db.DB;
import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		Locale.setDefault(Locale.US);
		
		SellerDao sellerDao = DaoFactory.createSellerDaO();
		
		 Seller seller = sellerDao.findById(10);
		
		System.out.println(seller);
		
		DB.closeConnection();
	}
}
