package lk.ijse.scaleShop.dao.custom;

import lk.ijse.scaleShop.dao.CrudDAO;
import lk.ijse.scaleShop.dto.OrderDetailDTO;
import lk.ijse.scaleShop.dto.ProductDTO;
import lk.ijse.scaleShop.entity.OrderDetail;
import lk.ijse.scaleShop.entity.Product;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ProductDAO extends CrudDAO<Product> {
    boolean updateQty(ArrayList<OrderDetail> details) throws SQLException, ClassNotFoundException;
    boolean updateQty(Product product) throws SQLException, ClassNotFoundException;
    ArrayList<String> loadALL() throws SQLException, ClassNotFoundException;
}
