package lk.ijse.scaleShop.dao.custom.Impl;

import lk.ijse.scaleShop.dao.CrudUtil;
import lk.ijse.scaleShop.dao.custom.OwnerProductDAO;
import lk.ijse.scaleShop.dto.OwnerProductDTO;
import lk.ijse.scaleShop.entity.OwnerProduct;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OwnerProductDAOImpl implements OwnerProductDAO {

    @Override
    public ArrayList<OwnerProduct> getData() throws SQLException, ClassNotFoundException {
        ArrayList<OwnerProduct> OwnerProductsData = new ArrayList<>();

        ResultSet rs = CrudUtil.execute("SELECT * FROM Product ORDER BY CAST(SUBSTRING(code, 2) AS UNSIGNED)");
        while (rs.next()){
            OwnerProductsData.add(new OwnerProduct(rs.getString("code"),
                    rs.getString("type"),
                    rs.getString("description"),
                    rs.getDouble("unitPrice"),
                    rs.getInt(" QuntityOnHand")
            ));
        }
        return OwnerProductsData;
    }

    @Override
    public boolean save(OwnerProduct obj) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Product VALUES (?, ?, ?, ?, ?)";
        return CrudUtil.execute(sql, obj.getCode(), obj.getType(), obj.getDescription(), obj.getPrice(), obj.getQty());

    }

    @Override
    public OwnerProduct search(String Id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Product WHERE code = ?";
        ResultSet result = CrudUtil.execute(sql,Id);

        if(result.next()){
            return new OwnerProduct(
                    result.getString(1),
                    result.getString(2),
                    result.getString(3),
                    result.getDouble(4),
                    result.getInt(5)
            );
        }
        return null;    }

    @Override
    public boolean update(OwnerProduct obj) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Product SET type=?, description=?, unitPrice=?, QuntityOnHand=? WHERE code=?";
        return CrudUtil.execute(sql, obj.getCode(), obj.getType(), obj.getDescription(), obj.getPrice(), obj.getQty());

    }

    @Override
    public boolean delete(String Id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Product WHERE code=?";
        return CrudUtil.execute(sql,Id);

    }

    @Override
    public String generateNextId() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT code FROM Product ORDER BY code DESC LIMIT 1;");
        /*if (rst.next()) {
            String id = rst.getString("code");
            int newCustomerId = Integer.parseInt(id.replace("P-", "")) + 1;
            return String.format("P-%03d", newCustomerId);
        } else {
            return "P-001";
        }*/

        /*String id = "";

        if(rst.next()){
            id = rst.getString(1);
        }

        String [] split = id.split("P0");
        int temp = Integer.parseInt(split[1]);
        temp+=1;
        return "P0"+temp;*/

        String id = "";
        if(rst.next()){
            id = rst.getString(1);
        }

        String [] split = id.split("P0");
        int temp = Integer.parseInt(split[1]);
        temp+=1;
        return "P0"+temp;
    }
}
