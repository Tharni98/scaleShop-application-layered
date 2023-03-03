package lk.ijse.scaleShop.bo.custom;

import lk.ijse.scaleShop.bo.SuperBO;
import lk.ijse.scaleShop.dto.OrderDetailDTO;
import lk.ijse.scaleShop.dto.ProductDTO;
import lk.ijse.scaleShop.entity.OrderDetail;
import lk.ijse.scaleShop.entity.Product;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ProductBO extends SuperBO {
    ArrayList<ProductDTO> getData() throws SQLException, ClassNotFoundException;
    boolean save(ProductDTO dto) throws SQLException, ClassNotFoundException;
    ProductDTO search(String Id) throws SQLException, ClassNotFoundException;
    boolean update(ProductDTO dto) throws SQLException, ClassNotFoundException;
    boolean delete(String id) throws SQLException, ClassNotFoundException;
    String generateNextId() throws SQLException, ClassNotFoundException;
    boolean updateQty(ArrayList<OrderDetailDTO> details) throws SQLException, ClassNotFoundException;
}
