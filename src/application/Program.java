package application;

import java.util.Locale;
import java.util.Scanner;

import db.DB;
import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program {

	public static void main(String[] args) {

		Locale.setDefault(Locale.US);
		Scanner input = new Scanner(System.in);
		
		DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
				
		departmentDao.update(new Department(3, "Tools"));
		
		departmentDao.deleteById(5);
		
		DB.closeConnection();
		input.close();
	}
}
