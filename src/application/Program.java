package application;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
		 
		Seller seller = new Seller(null,"Cauan","horaDeMorfar@gmail.com", LocalDate.now(), BigDecimal.valueOf(2030.15), new Department(1,null));
		sellerDao.insert(seller);
		
		
		DB.closeConnection();
	}
}
