package lk.ijse.scaleShop.bo.custom;

import lk.ijse.scaleShop.bo.SuperBO;
import lk.ijse.scaleShop.dto.OwnerEmployeeDTO;
import lk.ijse.scaleShop.dto.SupplierDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface SupplierBO extends SuperBO {
    ArrayList<SupplierDTO> getData() throws SQLException, ClassNotFoundException;

    boolean save(SupplierDTO obj) throws SQLException, ClassNotFoundException;

    SupplierDTO search(String Id) throws SQLException, ClassNotFoundException;
    boolean update(SupplierDTO obj) throws SQLException, ClassNotFoundException;
    boolean delete(String Id) throws SQLException, ClassNotFoundException;
    String generateNextId() throws SQLException, ClassNotFoundException;
}
