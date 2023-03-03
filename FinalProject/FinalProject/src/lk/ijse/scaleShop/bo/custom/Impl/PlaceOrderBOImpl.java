package lk.ijse.scaleShop.bo.custom.Impl;

import lk.ijse.scaleShop.bo.BOFactory;
import lk.ijse.scaleShop.bo.custom.PlaceOrderBO;
import lk.ijse.scaleShop.dao.DAOFactory;
import lk.ijse.scaleShop.dao.SuperDAO;
import lk.ijse.scaleShop.dao.custom.CustomerDAO;
import lk.ijse.scaleShop.dao.custom.OrderDAO;
import lk.ijse.scaleShop.dao.custom.OrderDetailDAO;
import lk.ijse.scaleShop.dao.custom.ProductDAO;
import lk.ijse.scaleShop.db.DBConnection;
import lk.ijse.scaleShop.dto.CustomerDTO;
import lk.ijse.scaleShop.dto.OrderDTO;
import lk.ijse.scaleShop.dto.OrderDetailDTO;
import lk.ijse.scaleShop.dto.ProductDTO;
import lk.ijse.scaleShop.entity.Customer;
import lk.ijse.scaleShop.entity.Order;
import lk.ijse.scaleShop.entity.OrderDetail;
import lk.ijse.scaleShop.entity.Product;

import java.sql.SQLException;
import java.util.ArrayList;

public class PlaceOrderBOImpl implements PlaceOrderBO {
    CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);
    ProductDAO productDAO = (ProductDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.PRODUCT);
    OrderDAO orderDAO = (OrderDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER);
    OrderDetailDAO detailDAO = (OrderDetailDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER_DETAIL);


    @Override
    public CustomerDTO searchCustomer(String Id) throws SQLException, ClassNotFoundException {
        Customer c = customerDAO.search(Id);
        return new CustomerDTO(c.getCustId(),c.getName(),c.getAddress(),c.getNumber(),c.getEmpId());
    }

    @Override
    public ArrayList<String> loadALLCustomer() throws SQLException, ClassNotFoundException {
        ArrayList<String> list = customerDAO.loadALL();
        return list;
    }

    @Override
    public ArrayList<String> loadALLProducts() throws SQLException, ClassNotFoundException {
        ArrayList<String> list = productDAO.loadALL();
        return list;
    }

    @Override
    public ProductDTO search(String Id) throws SQLException, ClassNotFoundException {
        Product dto = productDAO.search(Id);
        return new ProductDTO(dto.getCode(),dto.getType(),dto.getDescription(),dto.getPrice(),dto.getQty());
    }

    @Override
    public String generateNextOrderId() throws SQLException, ClassNotFoundException {
        return orderDAO.generateNextOrderId();
    }

    @Override
    public boolean save(OrderDTO order, ArrayList<OrderDetailDTO> detailDTO) throws SQLException, ClassNotFoundException {
        boolean isOrderSave = orderDAO.save(new Order(order.getOrderId(),order.getDate(),order.getCustomerId()));
        ArrayList<OrderDetail> data = new ArrayList<>();
        for(OrderDetailDTO o : detailDTO){
            data.add(new OrderDetail(o.getOrderId(),o.getCode(),o.getQty(),o.getDescription(),o.getUnitPrice()));
        }

        boolean isDetailSave = detailDAO.save(data);
        if(isOrderSave && isDetailSave){
            return true;
        }
        return false;
    }

    @Override
    public boolean updateItemQty(ArrayList<OrderDetailDTO> dto) throws SQLException, ClassNotFoundException {
        ArrayList<OrderDetail> details = new ArrayList<>();
        for(OrderDetailDTO o : dto){
            details.add(new OrderDetail(o.getOrderId(),o.getCode(),o.getQty(),o.getDescription(),o.getUnitPrice()));
        }
        return productDAO.updateQty(details);
    }

    @Override
    public boolean placeOrder(OrderDTO orderDTO, ArrayList<OrderDetailDTO> detailDTO) throws SQLException, ClassNotFoundException {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            boolean isOrderAdded = save(orderDTO,detailDTO);
            if (isOrderAdded) {
                boolean isUpdated = updateItemQty(detailDTO);
                if (isUpdated) {
                    // DBConnection.getInstance().getConnection().commit();
                    return true;
                }

            }
            DBConnection.getInstance().getConnection().rollback();
            return false;
        } finally {
            DBConnection.getInstance().getConnection().setAutoCommit(true);
        }

    }
}
