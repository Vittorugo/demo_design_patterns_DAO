package model.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {
	
	private Connection connection;
	
	public SellerDaoJDBC(Connection connection) {
		this.connection = connection;
	}
	
	@Override
	public void insert(Seller seller) {
				
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			preparedStatement = connection.prepareStatement("INSERT INTO seller (Name,Email,BirthDate,BaseSalary,DepartmentId) values ( ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			
			preparedStatement.setString(1, seller.getName());
			preparedStatement.setString(2, seller.getEmailString());
			preparedStatement.setDate(3, new java.sql.Date(Date.valueOf(seller.getBirthDate()).getTime())); // convert LocalDate to Date SQL.
			preparedStatement.setDouble(4, seller.getBaseSalary().doubleValue());
			preparedStatement.setInt(5, seller.getDepartment().getId());
			
			int rowsAffected = preparedStatement.executeUpdate();
			
			if(rowsAffected > 0) {
				resultSet = preparedStatement.getGeneratedKeys(); // return id seller.
				if(resultSet.next()) {
					int id = resultSet.getInt(1); // seting new id compatible to sql
					seller.setId(id);
					
					System.out.println("Inserted row: " + findById(resultSet.getInt(1)));
				}			
			} else {
				throw new DbException("Unexpected error! No rows affected!");
			}
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(preparedStatement);
			DB.closeResultSet(resultSet);
		}
	}

	@Override
	public void update(Seller seller) {
		PreparedStatement preparedStatement = null;
		
		try {
			preparedStatement = connection.prepareStatement("UPDATE seller SET Name = ?, Email = ?, BirthDate = ?,  BaseSalary= ?, DepartmentId = ? WHERE id = ?");
			
			preparedStatement.setString(1, seller.getName());
			preparedStatement.setString(2, seller.getEmailString());
			preparedStatement.setDate(3, new java.sql.Date(Date.valueOf(seller.getBirthDate()).getTime()));
			preparedStatement.setDouble(4, seller.getBaseSalary().doubleValue());
			preparedStatement.setInt(5, seller.getDepartment().getId());
			preparedStatement.setInt(6, seller.getId());
			
			int rows = preparedStatement.executeUpdate();
			
			if(rows > 0) {
				System.out.println("Successful Update!");
			}
			
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(preparedStatement);
		}
	}

	@Override
	public void deleteById(Integer id) {
		
		PreparedStatement preparedStatement = null;
		
		try {
			preparedStatement = connection.prepareStatement("DELETE FROM seller WHERE id = ?");
			preparedStatement.setInt(1, id);
			
			int rows = preparedStatement.executeUpdate();
			
			if(rows > 0) {
				System.out.println("Deleted seller!");
			} else {
				throw new DbException("Error! Unable to delete.");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(preparedStatement);
		}
	}

	@Override
	public Seller findById(Integer id) {
		
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			preparedStatement = connection.prepareStatement("SELECT seller.*, department.Name AS DepName FROM seller INNER JOIN department ON seller.DepartmentId = department.Id WHERE seller.Id = ?");
			
			preparedStatement.setInt(1, id);
			
			resultSet = preparedStatement.executeQuery(); // return search result
			
			if(resultSet.next()) { // true if result > 0
				Department department = instantiateDepartment(resultSet);
				Seller seller = instantiateSeller(resultSet,department);
				
				return seller;
			}
			
			return null;
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(preparedStatement);
			DB.closeResultSet(resultSet);
		}	
	}

	private Seller instantiateSeller(ResultSet resultSet, Department department) throws SQLException {
		Seller seller = new Seller();
		seller.setId(resultSet.getInt("Id"));
		seller.setName(resultSet.getString("Name"));
		seller.setEmailString(resultSet.getString("Email"));
		seller.setBirthDate(new java.sql.Date((resultSet.getDate("BirthDate").getTime())).toLocalDate()); // convert sql date in localdate
		seller.setBaseSalary(BigDecimal.valueOf(resultSet.getDouble("BaseSalary"))); // convert sql double in BigDecimal
		seller.setDepartment(department); // obj department
		return seller;
	}

	private Department instantiateDepartment(ResultSet resultSet) throws SQLException {
		Department department = new Department();
		department.setId(resultSet.getInt("DepartmentId")); // table return for department id instance
		department.setName(resultSet.getString("DepName")); // table return for department name instance
		return department;
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		List<Seller> resultSearch = new ArrayList<Seller>();
		
		try {
			preparedStatement = connection.prepareStatement("SELECT seller.*, department.Name AS DepName FROM seller INNER JOIN department ON seller.DepartmentId = department.Id ORDER BY Name");
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				
				Department department = instantiateDepartment(resultSet);
				Seller seller = instantiateSeller(resultSet, department);
				resultSearch.add(seller);
			}
			
			return resultSearch;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(preparedStatement);
			DB.closeResultSet(resultSet);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			preparedStatement = connection.prepareStatement("SELECT department.Name AS DepName, seller.* FROM department INNER JOIN seller ON department.Id = seller.DepartmentId where department.id = ? ORDER BY Name");
			
			preparedStatement.setInt(1, department.getId());
			resultSet = preparedStatement.executeQuery();
			
			List<Seller> resultSearch = new ArrayList<Seller>();
			Map<Integer, Department> map = new HashMap<Integer, Department>();
			
			while(resultSet.next()) {
				
				Department dep = map.get(resultSet.getInt("DepartmentId"));
				
				if(dep == null) {
					dep = instantiateDepartment(resultSet);
					map.put(department.getId(), dep);
				}
				
				Seller seller = instantiateSeller(resultSet, department);
				resultSearch.add(seller);	
			}
			
			return resultSearch;
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(preparedStatement);
			DB.closeResultSet(resultSet);
		}
		
	}

}
