package lk.ijse.scaleShop.bo.custom;

import lk.ijse.scaleShop.bo.SuperBO;
import lk.ijse.scaleShop.dto.CustomerDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CustomerBO extends SuperBO {
    public ArrayList<CustomerDTO> getData() throws SQLException, ClassNotFoundException;

    public boolean save(CustomerDTO dto) throws SQLException, ClassNotFoundException;

    public CustomerDTO search(String Id) throws SQLException, ClassNotFoundException;

    public boolean update(CustomerDTO dto) throws SQLException, ClassNotFoundException;

    public boolean delete( String Id) throws SQLException, ClassNotFoundException;

    public String generateNextId() throws SQLException, ClassNotFoundException ;
    public  ArrayList<String> loadALL() throws SQLException, ClassNotFoundException ;

}
