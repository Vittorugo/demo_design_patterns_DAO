package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
		ResultSet resultSet = null;
		try {
			preparedStatement = connection.prepareStatement("INSERT INTO department (Id, Name) values (?,?)", Statement.RETURN_GENERATED_KEYS);
			
			preparedStatement.setInt(1, department.getId());
			preparedStatement.setString(2, department.getName());
			
			int rowsAffected = preparedStatement.executeUpdate();
						
			if(rowsAffected > 0) {
				resultSet = preparedStatement.getGeneratedKeys();
				if(resultSet.next()) {
					int row = resultSet.getInt(1);
					department.setId(row);
					
					System.out.println("Row inserted: " + findById(row));
				}
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
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			preparedStatement = connection.prepareStatement("UPDATE department SET Id = ?, Name = ? WHERE Id = ?");
			
			preparedStatement.setInt(1, department.getId());
			preparedStatement.setString(2, department.getName());
			preparedStatement.setInt(3, department.getId());
			
			int rowsAffected = preparedStatement.executeUpdate();
			
			if( rowsAffected > 0) {
				System.out.println("Update successful!");
				System.out.println("Row changed: " + findById(department.getId()));
			} else {
				throw new DbException("Update Unchanged!");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(preparedStatement);
			DB.closeResultSet(resultSet);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement("DELETE FROM department WHERE id = ?");
			
			preparedStatement.setInt(1, id);
			
			int rowsAffected = preparedStatement.executeUpdate();
			
			if(rowsAffected > 0) {
				System.out.println("Successfull DELETE Command!");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(preparedStatement);
		}
	}

	@Override
	public Department findById(Integer id) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			preparedStatement = connection.prepareStatement("SELECT department.* FROM department WHERE department.Id = ?");
			
			preparedStatement.setInt(1, id);
			
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()) {
				Department department = getDepartment(resultSet);
				return department;
			} else {
				throw new DbException("No department found for this Id!");
			}
			
		}catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(preparedStatement);
			DB.closeResultSet(resultSet);
		}
	}

	@Override
	public List<Department> findAll() {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Department> list = new ArrayList<Department>();
		
		try {
			preparedStatement = connection.prepareStatement("SELECT * FROM department");
			
			resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				Department department = getDepartment(resultSet);
				list.add(department);
			}
			
			return list;
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(preparedStatement);
			DB.closeResultSet(resultSet);
		}
	}
	
	private Department getDepartment(ResultSet resultSet) throws SQLException {
		Department department = new Department();
		
		department.setId(resultSet.getInt("Id"));
		department.setName(resultSet.getString("Name"));
		
		return department;
	}

}
