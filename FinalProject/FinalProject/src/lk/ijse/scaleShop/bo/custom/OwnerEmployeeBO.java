package lk.ijse.scaleShop.bo.custom;

import lk.ijse.scaleShop.bo.SuperBO;
import lk.ijse.scaleShop.dto.OwnerEmployeeDTO;
import lk.ijse.scaleShop.entity.OwnerEmployee;

import java.sql.SQLException;
import java.util.ArrayList;

public interface OwnerEmployeeBO extends SuperBO {

    ArrayList<OwnerEmployeeDTO> getData() throws SQLException, ClassNotFoundException;

    boolean save(OwnerEmployeeDTO obj) throws SQLException, ClassNotFoundException;

    OwnerEmployeeDTO search(String Id) throws SQLException, ClassNotFoundException;
    boolean update(OwnerEmployeeDTO obj) throws SQLException, ClassNotFoundException;
    boolean delete(String Id) throws SQLException, ClassNotFoundException;
    String generateNextId() throws SQLException, ClassNotFoundException;
}
