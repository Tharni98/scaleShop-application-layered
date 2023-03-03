package lk.ijse.scaleShop.bo;

import lk.ijse.scaleShop.bo.custom.Impl.*;

public class BOFactory {
    private static BOFactory boFactory;

    private BOFactory(){
    }

    public static BOFactory getBoFactory(){
        if(null==boFactory){
            boFactory = new BOFactory();
        }
        return boFactory;
    }

    public enum BOTypes{
        CUSTOMER, PRODUCT, OWNER_PRODUCT, PO, O_EMPLOYEE, O_PRODUCT,  SUPPLIER
    }

    //Object creation logic for BO objects
    public SuperBO getBO(BOTypes types){
        switch (types){
            case CUSTOMER:
                return new CustomerBOImpl();
            case PRODUCT:
                return new ProductBOImpl();
            case PO:
                return new PlaceOrderBOImpl();
            case O_EMPLOYEE:
                return new OwnerEmployeeBOImpl();
            case O_PRODUCT:
                return new OwnerProductBOImpl();
            case SUPPLIER:
                return new SupplierBOImpl();
            default:
                return null;
        }
    }

}
