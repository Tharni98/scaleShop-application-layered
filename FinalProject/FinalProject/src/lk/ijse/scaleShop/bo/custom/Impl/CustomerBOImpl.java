package lk.ijse.scaleShop.bo.custom.Impl;

import lk.ijse.scaleShop.bo.custom.CustomerBO;
import lk.ijse.scaleShop.dao.DAOFactory;
import lk.ijse.scaleShop.dao.custom.CustomerDAO;
import lk.ijse.scaleShop.dto.CustomerDTO;
import lk.ijse.scaleShop.entity.Customer;

import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerBOImpl implements CustomerBO{

    CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);

    @Override
    public ArrayList<CustomerDTO> getData() throws SQLException, ClassNotFoundException {
        ArrayList<Customer> allLoan = customerDAO.getData();
        ArrayList<CustomerDTO> loanDTO=new ArrayList<>();
        for (Customer c : allLoan) {
            loanDTO.add(new CustomerDTO(c.getCustId(),c.getName(),c.getAddress(),c.getNumber(),c.getEmpId()));
        }
        return loanDTO;
    }

    @Override
    public boolean save(CustomerDTO dto) throws SQLException, ClassNotFoundException {
        return customerDAO.save(new Customer(dto.getCustId(), dto.getName(), dto.getAddress(), dto.getNumber(), dto.getEmpId()));

    }

    @Override
    public CustomerDTO search(String Id) throws SQLException, ClassNotFoundException {
        Customer c = customerDAO.search(Id);
        return new CustomerDTO(c.getCustId(),c.getName(),c.getAddress(),c.getNumber(),c.getEmpId());
    }

    @Override
    public boolean update(CustomerDTO dto) throws SQLException, ClassNotFoundException {
        return customerDAO.update(new Customer(dto.getCustId(), dto.getName(), dto.getAddress(), dto.getNumber(), dto.getEmpId()));
    }

    @Override
    public boolean delete(String Id) throws SQLException, ClassNotFoundException {
        return customerDAO.delete(Id);
    }

    @Override
    public String generateNextId() throws SQLException, ClassNotFoundException {
        return customerDAO.generateNextId();
    }

    @Override
    public ArrayList<String> loadALL() throws SQLException, ClassNotFoundException {
        ArrayList<String> list = customerDAO.loadALL();
        return list;
    }

}

