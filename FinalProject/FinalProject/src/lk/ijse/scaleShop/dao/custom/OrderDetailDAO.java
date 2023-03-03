package lk.ijse.scaleShop.dao.custom;

import lk.ijse.scaleShop.dao.CrudDAO;
import lk.ijse.scaleShop.dto.OrderDetailDTO;
import lk.ijse.scaleShop.entity.OrderDetail;

import java.sql.SQLException;
import java.util.ArrayList;

public interface OrderDetailDAO extends CrudDAO<OrderDetail> {
    boolean save(ArrayList<OrderDetail> orderDetails) throws SQLException, ClassNotFoundException ;

}
