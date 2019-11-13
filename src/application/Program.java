package application;

import java.util.Locale;
import java.util.Scanner;

import db.DB;
import model.dao.DaoFactory;
import model.dao.SellerDao;

public class Program {

	public static void main(String[] args) {

		Locale.setDefault(Locale.US);
		Scanner input = new Scanner(System.in);
		
		SellerDao sellerDao = DaoFactory.createSellerDaO();

		System.out.print("Enter id for delete: ");
		int id = input.nextInt();
		input.nextLine();
		
		sellerDao.deleteById(id);
		
		DB.closeConnection();
		input.close();
	}
}
