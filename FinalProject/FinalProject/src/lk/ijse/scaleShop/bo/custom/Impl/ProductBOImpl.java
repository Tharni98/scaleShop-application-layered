package lk.ijse.scaleShop.bo.custom.Impl;

import lk.ijse.scaleShop.bo.custom.ProductBO;
import lk.ijse.scaleShop.dao.DAOFactory;
import lk.ijse.scaleShop.dao.SuperDAO;
import lk.ijse.scaleShop.dao.custom.ProductDAO;
import lk.ijse.scaleShop.dto.OrderDetailDTO;
import lk.ijse.scaleShop.dto.ProductDTO;
import lk.ijse.scaleShop.entity.OrderDetail;
import lk.ijse.scaleShop.entity.Product;

import java.sql.SQLException;
import java.util.ArrayList;

public class ProductBOImpl implements ProductBO {
    ProductDAO productDAO = (ProductDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.PRODUCT);

    @Override
    public ArrayList<ProductDTO> getData() throws SQLException, ClassNotFoundException {
        ArrayList<Product> data = productDAO.getData();
        ArrayList<ProductDTO> detail = new ArrayList<>();
        for(Product d : data){
            detail.add(new ProductDTO(d.getCode(),d.getType(),d.getDescription(),d.getPrice(),d.getQty()));
        }
        return detail;
    }

    @Override
    public boolean save(ProductDTO dto) throws SQLException, ClassNotFoundException {
        return productDAO.save(new Product(dto.getCode(),dto.getType(),dto.getDescription(),dto.getPrice(),dto.getQty()));
    }

    @Override
    public ProductDTO search(String Id) throws SQLException, ClassNotFoundException {
        Product dto = productDAO.search(Id);
        return new ProductDTO(dto.getCode(),dto.getType(),dto.getDescription(),dto.getPrice(),dto.getQty());
    }

    @Override
    public boolean update(ProductDTO dto) throws SQLException, ClassNotFoundException {
        return productDAO.update(new Product(dto.getCode(),dto.getType(),dto.getDescription(),dto.getPrice(),dto.getQty()));
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return productDAO.delete(id);
    }

    @Override
    public String generateNextId() throws SQLException, ClassNotFoundException {
        return productDAO.generateNextId();
    }

    @Override
    public boolean updateQty(ArrayList<OrderDetailDTO> details) throws SQLException, ClassNotFoundException {
        ArrayList<OrderDetail> detail = new ArrayList<>();
        for(OrderDetailDTO o : details){
            detail.add(new OrderDetail(o.getOrderId(),o.getCode(),o.getQty(),o.getDescription(),o.getUnitPrice()));
        }
        return productDAO.updateQty(detail);
    }
}
