package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {
	
	private Connection connection = null;
	
	public DepartmentDaoJDBC(Connection connection){
		this.connection = connection;
	}
	
	@Override
	public void insert(Department department) {
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement("INSERT INTO department (Id, Name) values (?,?)");
			
			preparedStatement.setInt(1, department.getId());
			preparedStatement.setString(2, department.getName());
			
			int rowsAffected = preparedStatement.executeUpdate();
			
			if(rowsAffected > 0) {
				System.out.println("Department inserted!");
			} else {
				throw new DbException("Error! Unable insert department!");
			}
		}catch (SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(preparedStatement);
		}
	}

	@Override
	public void update(Department department) {

	}

	@Override
	public void deleteById(Integer id) {

	}

	@Override
	public Department findById(Integer id) {
		return null;
	}

	@Override
	public List<Department> findAll() {
		return null;
	}

}
