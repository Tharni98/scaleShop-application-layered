package lk.ijse.scaleShop.dao;

import lk.ijse.scaleShop.dao.custom.Impl.*;

public class DAOFactory {

    private static DAOFactory daoFactory;

    private DAOFactory() {
    }

    public static DAOFactory getDaoFactory() {
        return (daoFactory == null) ? daoFactory = new DAOFactory() : daoFactory;
    }
    public enum DAOTypes {
       CUSTOMER,ORDER,ORDER_DETAIL,OWNER_EMPLOYEE,OWNER_PRODUCT,PLACE_ORDER,PRODUCT,SUPPLIER
    }

    public SuperDAO getDAO(DAOTypes types){
        switch (types) {
            case CUSTOMER:
               return new CustomerDAOImpl();
            case ORDER:
                return new OrderDAOImpl();
            case ORDER_DETAIL:
                return new OrderDetailDAOImpl();
            case OWNER_EMPLOYEE:
                return new OwnerEmployeeDAOImpl();
            case OWNER_PRODUCT:
                return new OwnerProductDAOImpl();
            case PRODUCT:
                return new ProductDAOImpl();
            case SUPPLIER:
                return new SupplierDAOImpl();
            default:
                return null;
        }
    }


}
