package model.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

	}

	@Override
	public void update(Seller seller) {

	}

	@Override
	public void deleteById(Integer id) {

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
		return null;
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
