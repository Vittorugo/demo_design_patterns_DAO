package application;

import java.util.Locale;
import java.util.Scanner;

import db.DB;
import model.dao.DaoFactory;
import model.dao.DepartmentDao;

public class Program {

	public static void main(String[] args) {

		Locale.setDefault(Locale.US);
		Scanner input = new Scanner(System.in);
		
		DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
				
		System.out.println(departmentDao.findById(4));

		departmentDao.findAll().forEach(d -> System.out.println(d));
		
		DB.closeConnection();
		input.close();
	}
}
