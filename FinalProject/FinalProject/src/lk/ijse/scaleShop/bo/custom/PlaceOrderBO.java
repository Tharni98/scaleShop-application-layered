package lk.ijse.scaleShop.bo.custom;

import lk.ijse.scaleShop.bo.SuperBO;
import lk.ijse.scaleShop.dto.CustomerDTO;
import lk.ijse.scaleShop.dto.OrderDTO;
import lk.ijse.scaleShop.dto.OrderDetailDTO;
import lk.ijse.scaleShop.dto.ProductDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface PlaceOrderBO extends SuperBO {
    CustomerDTO searchCustomer(String Id) throws SQLException, ClassNotFoundException;
    ArrayList<String> loadALLCustomer() throws SQLException, ClassNotFoundException ;
    ProductDTO search(String Id) throws SQLException, ClassNotFoundException;
    String generateNextOrderId() throws SQLException, ClassNotFoundException ;
    ArrayList<String> loadALLProducts() throws SQLException, ClassNotFoundException;
    boolean save(OrderDTO order, ArrayList<OrderDetailDTO> detailDTOS) throws SQLException, ClassNotFoundException ;
    boolean updateItemQty(ArrayList<OrderDetailDTO> dto)throws SQLException, ClassNotFoundException ;
    boolean placeOrder(OrderDTO orderDTO,ArrayList< OrderDetailDTO> detailDTO) throws SQLException, ClassNotFoundException;
}
