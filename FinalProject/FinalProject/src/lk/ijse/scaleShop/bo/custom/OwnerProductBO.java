package lk.ijse.scaleShop.bo.custom;

import javafx.event.ActionEvent;
import lk.ijse.scaleShop.bo.SuperBO;
import lk.ijse.scaleShop.dto.OwnerProductDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface OwnerProductBO extends SuperBO {
    ArrayList<OwnerProductDTO> getOwnerProductsData() throws SQLException, ClassNotFoundException;
    boolean save(OwnerProductDTO dto) throws SQLException, ClassNotFoundException;
    boolean update (OwnerProductDTO dto) throws SQLException, ClassNotFoundException;
    OwnerProductDTO search(String code) throws SQLException, ClassNotFoundException;
    boolean delete (String code) throws SQLException, ClassNotFoundException;
    String generateNextId() throws SQLException, ClassNotFoundException;
}
