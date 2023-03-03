package lk.ijse.scaleShop.dao.custom.Impl;

import lk.ijse.scaleShop.dao.CrudUtil;
import lk.ijse.scaleShop.dao.custom.CustomerDAO;
import lk.ijse.scaleShop.db.DBConnection;
import lk.ijse.scaleShop.dto.CustomerDTO;
import lk.ijse.scaleShop.entity.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerDAOImpl implements CustomerDAO {


    @Override
    public ArrayList<Customer> getData() throws SQLException, ClassNotFoundException {
        ArrayList<Customer> customersData = new ArrayList<>();

        ResultSet rs = CrudUtil.execute("SELECT * FROM Customer ORDER BY CAST(SUBSTRING(customerID, 2) AS UNSIGNED);");
        while (rs.next()){
            customersData.add(new Customer(rs.getString("customerID"),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getInt("contactNo"),
                    rs.getString("employeeID")));
        }

        return customersData;
    }

    @Override
    public boolean save(Customer obj) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Customer VALUES (?, ?, ?, ?, ?)";
        return CrudUtil.execute(sql, obj.getCustId(),obj.getName(), obj.getAddress(), obj.getNumber(),obj.getEmpId());
    }

    @Override
    public Customer search(String Id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Customer WHERE customerID = ?";
        ResultSet result = CrudUtil.execute(sql,Id);

        if(result.next()){
            return new Customer(
                    result.getString(1),
                    result.getString(2),
                    result.getString(3),
                    result.getInt(4),
                    result.getString(5)
            );
        }
        return null;
    }

    @Override
    public boolean update(Customer obj) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Customer SET name=?, address=?,  contactNo=? , employeeID=? WHERE customerID=?";
        return CrudUtil.execute(sql, obj.getName(), obj.getAddress(), obj.getNumber(),obj.getEmpId(),obj.getCustId());
    }

    @Override
    public boolean delete(String Id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Customer WHERE customerID=?";
        return CrudUtil.execute(sql,Id);
    }

    @Override
    public String generateNextId() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT customerID FROM Customer ORDER BY customerID DESC LIMIT 1;");

        String id = "";

        if(rst.next()){
            id = rst.getString(1);
        }

        String [] split = id.split("C00");
        int temp = Integer.parseInt(split[1]);
        temp+=1;
        return "C00"+temp;
//        if (rst.next()) {
//            String id = rst.getString("customerID");
//            int newCustomerId = Integer.parseInt(id.replace("C-", "")) + 1;
//            return String.format("C-%03d", newCustomerId);
//        } else {
//            return "C-001";
//        }
    }

    @Override
    public ArrayList<String> loadALL() throws SQLException, ClassNotFoundException {

        ResultSet result = CrudUtil.execute("SELECT customerID FROM Customer;");

        ArrayList<String> idList = new ArrayList<>();

        while (result.next()) {
            idList.add(result.getString("customerID"));
        }
        return idList;
    }
}
