package lk.ijse.scaleShop.bo.custom.Impl;

import lk.ijse.scaleShop.bo.BOFactory;
import lk.ijse.scaleShop.bo.SuperBO;
import lk.ijse.scaleShop.bo.custom.SupplierBO;
import lk.ijse.scaleShop.dao.DAOFactory;
import lk.ijse.scaleShop.dao.custom.SupplierDAO;
import lk.ijse.scaleShop.dto.SupplierDTO;
import lk.ijse.scaleShop.entity.Supplier;

import java.sql.SQLException;
import java.util.ArrayList;

public class SupplierBOImpl implements SupplierBO {
    SupplierDAO supplierDAO = (SupplierDAO)  DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.SUPPLIER);

    @Override
    public ArrayList<SupplierDTO> getData() throws SQLException, ClassNotFoundException {
        ArrayList<Supplier> data = supplierDAO.getData();
        ArrayList<SupplierDTO> detail = new ArrayList<>();

        for(Supplier s : data){
            detail.add(new SupplierDTO(s.getId(),s.getName(),s.getNumber(),s.getType(),s.getQty()));
        }
        return detail;
    }

    @Override
    public boolean save(SupplierDTO obj) throws SQLException, ClassNotFoundException {
        return supplierDAO.save(new Supplier(obj.getId(),obj.getName(),obj.getNumber(),obj.getType(),obj.getQty()));
    }

    @Override
    public SupplierDTO search(String Id) throws SQLException, ClassNotFoundException {
        Supplier s = supplierDAO.search(Id);
        return new SupplierDTO(s.getId(),s.getName(),s.getNumber(),s.getType(),s.getQty());
    }

    @Override
    public boolean update(SupplierDTO obj) throws SQLException, ClassNotFoundException {
        return supplierDAO.update(new Supplier(obj.getId(),obj.getName(),obj.getNumber(),obj.getType(),obj.getQty()));
    }

    @Override
    public boolean delete(String Id) throws SQLException, ClassNotFoundException {
        return supplierDAO.delete(Id);
    }

    @Override
    public String generateNextId() throws SQLException, ClassNotFoundException {
        return supplierDAO.generateNextId();
    }
}
