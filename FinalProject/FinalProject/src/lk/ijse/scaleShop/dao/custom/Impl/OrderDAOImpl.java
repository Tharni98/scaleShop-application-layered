package lk.ijse.scaleShop.dao.custom.Impl;

import lk.ijse.scaleShop.dao.CrudUtil;
import lk.ijse.scaleShop.dao.custom.OrderDAO;
import lk.ijse.scaleShop.dto.OrderDTO;
import lk.ijse.scaleShop.entity.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDAOImpl implements OrderDAO {


    @Override
    public ArrayList<Order> getData() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(Order obj) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Orders VALUES(?,?,?))";
        return CrudUtil.execute(sql,"2023-01-02", obj.getOrderId(), obj.getCustomerId());
    }

    @Override
    public Order search(String Id) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean update(Order obj) throws SQLException, ClassNotFoundException {
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
    public String generateNextOrderId() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT OrderId FROM Orders ORDER BY OrderId DESC LIMIT 1;");
        String id = "";

        if(rst.next()){
            id = rst.getString(1);
        }

        String [] split = id.split("O0");
        int temp = Integer.parseInt(split[1]);
        temp+=1;
        return "O0"+temp;
    }
}
