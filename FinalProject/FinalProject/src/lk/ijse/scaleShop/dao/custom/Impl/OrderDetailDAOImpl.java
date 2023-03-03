package lk.ijse.scaleShop.dao.custom.Impl;

import lk.ijse.scaleShop.dao.CrudUtil;
import lk.ijse.scaleShop.dao.custom.OrderDetailDAO;
import lk.ijse.scaleShop.dto.OrderDetailDTO;
import lk.ijse.scaleShop.entity.OrderDetail;

import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDetailDAOImpl implements OrderDetailDAO {

    @Override
    public ArrayList<OrderDetail> getData() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(OrderDetail obj) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO OrderDetail VALUES(?, ?, ?, ?)";
        return CrudUtil.execute(sql, obj.getOrderId(), obj.getCode(), obj.getQty(), obj.getUnitPrice());

    }

    @Override
    public OrderDetail search(String Id) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean update(OrderDetail obj) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String Id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public String generateNextId() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(ArrayList<OrderDetail> orderDetails) throws SQLException, ClassNotFoundException {
        for (OrderDetail orderDetail : orderDetails) {
            if (!save(orderDetail)) {
                return false;
            }
        }
        return true;
    }
}
