package lk.ijse.scaleShop.bo.custom.Impl;

import javafx.event.ActionEvent;
import lk.ijse.scaleShop.bo.custom.OwnerProductBO;
import lk.ijse.scaleShop.dao.DAOFactory;
import lk.ijse.scaleShop.dao.SuperDAO;
import lk.ijse.scaleShop.dao.custom.OwnerProductDAO;
import lk.ijse.scaleShop.dto.OwnerProductDTO;
import lk.ijse.scaleShop.entity.OwnerProduct;

import java.sql.SQLException;
import java.util.ArrayList;

public class OwnerProductBOImpl implements OwnerProductBO {
    OwnerProductDAO ownerProductDAO = (OwnerProductDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.OWNER_PRODUCT);

    @Override
    public ArrayList<OwnerProductDTO> getOwnerProductsData() throws SQLException, ClassNotFoundException {
        ArrayList<OwnerProduct> detail = ownerProductDAO.getData();
        ArrayList<OwnerProductDTO> data = new ArrayList<>();

        for(OwnerProduct o : detail){
            data.add(new OwnerProductDTO(o.getCode(),o.getType(),o.getDescription(),o.getPrice(),o.getQty()));
        }
        return data;
    }

    @Override
    public boolean save(OwnerProductDTO dto) throws SQLException, ClassNotFoundException {
        return ownerProductDAO.save(new OwnerProduct(dto.getCode(),dto.getType(),dto.getDescription(),dto.getPrice(),dto.getQty()));
    }

    @Override
    public boolean update(OwnerProductDTO dto) throws SQLException, ClassNotFoundException {
        return ownerProductDAO.update(new OwnerProduct(dto.getCode(),dto.getType(),dto.getDescription(),dto.getPrice(),dto.getQty()));
    }

    @Override
    public OwnerProductDTO search(String code) throws SQLException, ClassNotFoundException {
        OwnerProduct o = ownerProductDAO.search(code);
        return new OwnerProductDTO(o.getCode(),o.getType(),o.getDescription(),o.getPrice(),o.getQty());
    }

    @Override
    public boolean delete(String code) throws SQLException, ClassNotFoundException {
        return ownerProductDAO.delete(code);
    }

    @Override
    public String generateNextId() throws SQLException, ClassNotFoundException {
        return ownerProductDAO.generateNextId();
    }
}
