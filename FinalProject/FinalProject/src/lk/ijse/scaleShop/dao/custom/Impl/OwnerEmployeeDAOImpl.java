package lk.ijse.scaleShop.dao.custom.Impl;

import lk.ijse.scaleShop.dao.CrudUtil;
import lk.ijse.scaleShop.dao.custom.OwnerEmployeeDAO;
import lk.ijse.scaleShop.dto.OwnerEmployeeDTO;
import lk.ijse.scaleShop.entity.OwnerEmployee;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OwnerEmployeeDAOImpl implements OwnerEmployeeDAO {

    @Override
    public ArrayList<OwnerEmployee> getData() throws SQLException, ClassNotFoundException {
        ArrayList<OwnerEmployee> OwnerEmployeeData = new ArrayList<>();

        ResultSet rs = CrudUtil.execute("SELECT * FROM  Employee ORDER BY CAST(SUBSTRING(employeeID, 2) AS UNSIGNED)");
        while (rs.next()){
            OwnerEmployeeData.add(new OwnerEmployee(rs.getString("employeeID"),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getDouble("salary"),
                    rs.getString("jobRole")));
        }
        return OwnerEmployeeData;
    }

    @Override
    public boolean save(OwnerEmployee obj) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Employee VALUES (?, ?, ?, ?, ?)";
        return CrudUtil.execute(sql, obj.getEmpId(), obj.getName(), obj.getAddress(), obj.getSalary(),obj.getRole());
    }

    @Override
    public OwnerEmployee search(String Id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Employee WHERE employeeID = ?";
        ResultSet result = CrudUtil.execute(sql,Id);

        if(result.next()){
            return new OwnerEmployee(
                    result.getString(1),
                    result.getString(2),
                    result.getString(3),
                    result.getDouble(4),
                    result.getString(5)
            );
        }
        return null;
    }

    @Override
    public boolean update(OwnerEmployee obj) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Employee SET name=?, address=?, salary=?, jobRole=? WHERE employeeID=?";
        return CrudUtil.execute(sql, obj.getEmpId(), obj.getName(), obj.getAddress(), obj.getSalary(),obj.getRole());
    }

    @Override
    public boolean delete(String Id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Employee WHERE employeeID=?";
        return CrudUtil.execute(sql,Id);
    }

    @Override
    public String generateNextId() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT employeeID FROM Employee ORDER BY employeeID DESC LIMIT 1;");

        String id = "";

        if(rst.next()){
            id = rst.getString(1);
        }

        String [] split = id.split("E0");
        int temp = Integer.parseInt(split[1]);
        temp+=1;
        return "E0"+temp;
    }
}
