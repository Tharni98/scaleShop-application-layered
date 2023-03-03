package lk.ijse.scaleShop.dao.custom;

import lk.ijse.scaleShop.dao.CrudDAO;
import lk.ijse.scaleShop.dto.OrderDTO;
import lk.ijse.scaleShop.entity.Order;

import java.sql.SQLException;

public interface OrderDAO extends CrudDAO<Order> {


    public  String generateNextOrderId() throws SQLException, ClassNotFoundException ;

}
