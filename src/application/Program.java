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

		Seller seller = sellerDao.findById(12);
		seller.setName("Miller");
		sellerDao.update(seller);

		DB.closeConnection();
	}
}
