package lk.ijse.scaleShop.dao.custom;

import lk.ijse.scaleShop.dao.CrudDAO;
import lk.ijse.scaleShop.dto.CustomerDTO;
import lk.ijse.scaleShop.entity.Customer;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CustomerDAO extends CrudDAO<Customer> {
     ArrayList<String> loadALL() throws SQLException, ClassNotFoundException ;

}
