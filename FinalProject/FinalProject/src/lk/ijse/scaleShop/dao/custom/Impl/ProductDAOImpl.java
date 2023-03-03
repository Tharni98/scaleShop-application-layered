package lk.ijse.scaleShop.dao.custom.Impl;

import lk.ijse.scaleShop.dao.CrudUtil;
import lk.ijse.scaleShop.dao.DAOFactory;
import lk.ijse.scaleShop.dao.SuperDAO;
import lk.ijse.scaleShop.dao.custom.ProductDAO;
import lk.ijse.scaleShop.dto.OrderDetailDTO;
import lk.ijse.scaleShop.dto.ProductDTO;
import lk.ijse.scaleShop.entity.OrderDetail;
import lk.ijse.scaleShop.entity.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductDAOImpl implements ProductDAO {

    @Override
    public ArrayList<Product> getData() throws SQLException, ClassNotFoundException {
        ArrayList<Product> productsData = new ArrayList<>();

        ResultSet rs = CrudUtil.execute("SELECT * FROM Product ORDER BY CAST(SUBSTRING(code, 2) AS UNSIGNED)");
        while (rs.next()) {
            productsData.add(new Product(rs.getString("code"),
                    rs.getString("type"),
                    rs.getString("description"),
                    rs.getDouble("unitPrice"),
                    rs.getInt("QuntityOnHand")
            ));
        }
        return productsData;
    }

    @Override
    public boolean save(Product obj) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Product VALUES (?, ?, ?, ?, ?)";
        return CrudUtil.execute(sql, obj.getCode(), obj.getType(), obj.getDescription(), obj.getPrice(), obj.getQty());

    }

    @Override
    public Product search(String Id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Product WHERE code = ?";
        ResultSet result = CrudUtil.execute(sql, Id);

        if (result.next()) {
            return new Product(
                    result.getString(1),
                    result.getString(2),
                    result.getString(3),
                    result.getDouble(4),
                    result.getInt(5)
            );
        }
        return null;
    }

    @Override
    public boolean update(Product obj) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Product SET type=?, description=?, unitPrice=?, QuntityOnHand=? WHERE code=?";
        return CrudUtil.execute(sql, obj.getType(), obj.getDescription(), obj.getPrice(), obj.getQty(), obj.getCode());

    }

    @Override
    public boolean delete(String Id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Product WHERE code=?";
        return CrudUtil.execute(sql, Id);
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
        String id = "";
        if(rst.next()){
           id = rst.getString(1);
        }

        String [] split = id.split("P0");
        int temp = Integer.parseInt(split[1]);
        temp+=1;
        return "P0"+temp;
    }




    @Override
    public boolean updateQty(ArrayList<OrderDetail> details) throws SQLException, ClassNotFoundException {
        for (OrderDetail cartDetail : details) {
            if (!updateQty(new Product(cartDetail.getCode(),null, cartDetail.getDescription(), cartDetail.getUnitPrice(), cartDetail.getQty()))) {
                return false;
            }

        }
        return true;
    }
    @Override
    public boolean updateQty(Product product) throws SQLException, ClassNotFoundException{
        String sql = "UPDATE Product SET QuntityOnHand = QuntityOnHand - ? WHERE code = ?";
        return CrudUtil.execute(sql, product.getQty(), product.getCode());
    }

    @Override
    public ArrayList<String> loadALL() throws SQLException, ClassNotFoundException {
        String sql = "SELECT code FROM Product;";
        ResultSet result = CrudUtil.execute(sql);

        ArrayList<String> idList = new ArrayList<>();

        while (result.next()) {
            idList.add(result.getString(1));
        }
        return idList;
    }
}
