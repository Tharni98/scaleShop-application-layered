package lk.ijse.scaleShop.bo.custom.Impl;

import lk.ijse.scaleShop.bo.custom.OwnerEmployeeBO;
import lk.ijse.scaleShop.dao.DAOFactory;
import lk.ijse.scaleShop.dao.custom.OwnerEmployeeDAO;
import lk.ijse.scaleShop.dto.OwnerEmployeeDTO;
import lk.ijse.scaleShop.entity.OwnerEmployee;

import java.sql.SQLException;
import java.util.ArrayList;

public class OwnerEmployeeBOImpl implements OwnerEmployeeBO {

    OwnerEmployeeDAO ownerEmployeeDAO = (OwnerEmployeeDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.OWNER_EMPLOYEE);


    @Override
    public ArrayList<OwnerEmployeeDTO> getData() throws SQLException, ClassNotFoundException {
        ArrayList<OwnerEmployee> data = ownerEmployeeDAO.getData();
        ArrayList<OwnerEmployeeDTO> dto = new ArrayList<>();
        for (OwnerEmployee o : data){
            dto.add(new OwnerEmployeeDTO(o.getEmpId(),o.getName(),o.getName(),o.getSalary(),o.getRole()));
        }
        return dto;
    }

    @Override
    public boolean save(OwnerEmployeeDTO obj) throws SQLException, ClassNotFoundException {
        return ownerEmployeeDAO.save(new OwnerEmployee(obj.getEmpId(),obj.getName(),obj.getAddress(),obj.getSalary(),obj.getRole()));
    }

    @Override
    public OwnerEmployeeDTO search(String Id) throws SQLException, ClassNotFoundException {
        OwnerEmployee o = ownerEmployeeDAO.search(Id);
        return new OwnerEmployeeDTO(o.getEmpId(),o.getName(),o.getName(),o.getSalary(),o.getRole());
    }

    @Override
    public boolean update(OwnerEmployeeDTO obj) throws SQLException, ClassNotFoundException {
        return ownerEmployeeDAO.update(new OwnerEmployee(obj.getEmpId(),obj.getName(),obj.getAddress(),obj.getSalary(),obj.getRole()));
    }

    @Override
    public boolean delete(String Id) throws SQLException, ClassNotFoundException {
        return ownerEmployeeDAO.delete(Id);
    }

    @Override
    public String generateNextId() throws SQLException, ClassNotFoundException {
        return ownerEmployeeDAO.generateNextId();
    }
}
