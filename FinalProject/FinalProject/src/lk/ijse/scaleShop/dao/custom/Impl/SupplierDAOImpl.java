package lk.ijse.scaleShop.dao.custom.Impl;

import lk.ijse.scaleShop.dao.CrudUtil;
import lk.ijse.scaleShop.dao.custom.SupplierDAO;
import lk.ijse.scaleShop.dto.SupplierDTO;
import lk.ijse.scaleShop.entity.Supplier;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SupplierDAOImpl implements SupplierDAO {


    @Override
    public ArrayList<Supplier> getData() throws SQLException, ClassNotFoundException {
        ArrayList<Supplier> suppliersData = new ArrayList<>();

        ResultSet rs = CrudUtil.execute("SELECT * FROM Supplier ORDER BY CAST(SUBSTRING(supplierID, 2) AS UNSIGNED)");
        while (rs.next()){
            suppliersData.add(new Supplier(rs.getString("supplierID"),
                    rs.getString("name"),
                    rs.getInt("contactNo"),
                    rs.getString("type"),
                    rs.getInt("Quntity")));
        }
        return suppliersData;
    }

    @Override
    public boolean save(Supplier obj) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Supplier VALUES (?, ?, ?, ?, ?)";
        return CrudUtil.execute(sql, obj.getId(), obj.getName(), obj.getNumber(), obj.getType(), obj.getQty());
    }

    @Override
    public Supplier search(String Id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Supplier WHERE supplierID = ?";
        ResultSet result = CrudUtil.execute(Id);

        if(result.next()){
            return new Supplier(
                    result.getString(1),
                    result.getString(2),
                    result.getInt(3),
                    result.getString(4),
                    result.getInt(5)
            );
        }
        return null;    }

    @Override
    public boolean update(Supplier obj) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Supplier SET name=?, contactNo=?, type=?, Quntity=? WHERE  supplierID=?";
        return CrudUtil.execute(sql, obj.getName(), obj.getNumber(), obj.getType(), obj.getQty(),obj.getId());
    }

    @Override
    public boolean delete(String Id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Supplier WHERE supplierID=?";
        return CrudUtil.execute(sql, Id);
    }

    @Override
    public String generateNextId() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT supplierID FROM Supplier ORDER BY supplierID DESC LIMIT 1;");
        /*if (rst.next()) {
            String id = rst.getString("supplierID");
            int newCustomerId = Integer.parseInt(id.replace("S-", "")) + 1;
            return String.format("S-%03d", newCustomerId);
        } else {
            return "S-001";
        }*/
        String id = "";
        // SUP-01
        if(rst.next()){
            id = rst.getString(1);
        }

        String [] split = id.split("SUP-");
        int temp = Integer.parseInt(split[1]);
        temp+=1;
        return "SUP-"+temp;
    }
}
