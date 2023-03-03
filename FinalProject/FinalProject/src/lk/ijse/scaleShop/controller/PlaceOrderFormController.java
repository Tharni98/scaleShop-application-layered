package lk.ijse.scaleShop.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.scaleShop.bo.BOFactory;
import lk.ijse.scaleShop.bo.SuperBO;
import lk.ijse.scaleShop.bo.custom.PlaceOrderBO;
import lk.ijse.scaleShop.dao.custom.Impl.*;
import lk.ijse.scaleShop.db.DBConnection;
import lk.ijse.scaleShop.dto.CustomerDTO;
import lk.ijse.scaleShop.dto.OrderDTO;
import lk.ijse.scaleShop.dto.OrderDetailDTO;

import lk.ijse.scaleShop.dto.ProductDTO;
import lk.ijse.scaleShop.util.Navigation;
import lk.ijse.scaleShop.util.Routes;
import lk.ijse.scaleShop.view.tm.PlaceOrderTM;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class PlaceOrderFormController  {


    public Label lblQty;
    public Label lblUnitPrice;
    public Label lblDescription;
    public Label lblCode;
    public Label lblOrderDate;
    public Label lblOrderId;
    public JFXTextField txtSearch;
    public TableColumn colTotal;
    public TableColumn colUnitPrice;
    public TableColumn colCode;
    public TableView<PlaceOrderTM> tblPlaceOrder;
    public ComboBox cmbCustomerId;
    public TextField txtQty;
    public ComboBox cmbCode;
    public AnchorPane pane;
    public Label lblCustName;
    public TableColumn colName;
    public TableColumn colqty;
    public TableColumn colTot;
    PlaceOrderBO placeOrderBO = (PlaceOrderBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PO);

    public void initialize(){
        loadCustomerIds();
        loadNextOrderId();
        loadProductIds();
        loadOrderDate();
        setCellValueFactory();
    }

    private void loadOrderDate() {
        lblOrderDate.setText(String.valueOf((LocalDate.now())));
    }

    public void placeOrderOnAction(ActionEvent event) {
    }

    public void SupplierOnAction(ActionEvent event) throws IOException {
        Navigation.navigate(Routes.SUPPLIER,pane);
    }

    public void ProductOnAction(ActionEvent event) throws IOException {
        Navigation.navigate(Routes.PRODUCT,pane);
    }

    public void CustomerOnAction(ActionEvent event) throws IOException {
        Navigation.navigate(Routes.CUSTOMER,pane);
    }

    public void BillOnAction(ActionEvent event) throws IOException {
        Navigation.navigate(Routes.BILL,pane);
    }

    public void IncomeOnAction(ActionEvent event) throws IOException {
        Navigation.navigate(Routes.INCOME_REPORT,pane);
    }

    public void DashboardOnAction(ActionEvent event) throws IOException {
        Navigation.navigate(Routes.ADMIN_DASHBOARD,pane);
    }

    public void PlaceOrderOnAction(ActionEvent event) {
        String orderId = lblOrderId.getText();
        String customerId = String.valueOf(cmbCustomerId.getValue());


            //double amount = Double.parseDouble(lblTotal.getText());

            ArrayList<OrderDetailDTO> orderData = new ArrayList<>();

            /* load all cart items' to orderDetails arrayList */
            for (int i = 0; i < tblPlaceOrder.getItems().size(); i++) {
                /* get each row details to (PlaceOrderTm)tm in each time and add them to the orderDetails */
                PlaceOrderTM tm = obList.get(i);
                orderData.add(new OrderDetailDTO(orderId,tm.getCode(),tm.getQty(),tm.getDescription(),tm.getUnitPrice()));
            }

            OrderDTO order = new OrderDTO(orderId,null,customerId);
            try {
                    // boolean isPlaced = PlaceOrderModal.placeOrder(order);
                    boolean isPlaced = placeOrderBO.placeOrder(order,orderData);
                    if (isPlaced) {
                        /* to clear table */
                        obList.clear();
                        loadNextOrderId();

                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Order Placed!",ButtonType.OK);
                        Optional<ButtonType> result =alert.showAndWait();
                        if(result.get()==ButtonType.OK){

                            InputStream resourses = this.getClass().getResourceAsStream("/lk/ijse/finalproject/reports/Order.jrxml");

                            HashMap<String, Object> hm = new HashMap<>();
                           /* hm.put("orderId",lblOrderId.getText());
                            hm.put("custId",lblCustID.getText());
                            hm.put("name",lblName.getText());
                            hm.put("total",Double.parseDouble(lblTotal.getText()));
                            hm.put("amount",txtAmount.getText());
                            hm.put("balance",lblBalance.getText());
*/

                            try {
                                JasperReport jasperReport = JasperCompileManager.compileReport(resourses);
                                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, hm, DBConnection.getInstance().getConnection());
                                JasperViewer.viewReport(jasperPrint,false);
                                //JasperPrintManager.printReport(jasperPrint,true);
                            } catch (JRException | SQLException | ClassNotFoundException e) {
                                System.out.println(e.toString());
                            }
                        }
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Order Not Placed!").show();
                    }

            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

      /* PlaceOrderDTO placeOrderDTO = new PlaceOrderDTO(customerId, orderId, arrayList);
        try {
            boolean isPlaced = PlaceOrderDAO.placeOrder(placeOrderDTO);
            if (isPlaced) {
                obList.clear();
                loadNextOrderId();
                new Alert(Alert.AlertType.CONFIRMATION, "OrderDTO Placed!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "OrderDTO Not Placed!").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        lblQty.setText("");*/
    }

    public void btnNewCustomerOnAction(ActionEvent event) {
    }

    public void txtQtyOnAction(ActionEvent event) {
    }

    public void cmbCustomerOnAction(ActionEvent event) {
        String code = String.valueOf(cmbCustomerId.getValue());
        try {
            //CustomerDTO cust = customerDAO.search(code);
            CustomerDTO cust = placeOrderBO.searchCustomer(code);
            fillCustFields(cust);
            txtQty.requestFocus();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private void fillCustFields(CustomerDTO customerDTO) {
        lblCustName.setText(customerDTO.getName());

    }


    public void cmbCode(ActionEvent event) {
        String code = String.valueOf(cmbCode.getValue());
        try {
           // ProductDTO productDTO = productDAO.search(code);
            ProductDTO productDTO = placeOrderBO.search(code);
            fillItemFields(productDTO);
            txtQty.requestFocus();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void fillItemFields(ProductDTO productDTO) {
        lblDescription.setText(productDTO.getDescription());
        lblUnitPrice.setText(String.valueOf(productDTO.getPrice()));
        lblQty.setText(String.valueOf(productDTO.getQty()));
    }

    public void LogoutOnAction(ActionEvent event) {
    }
    private void loadCustomerIds() {
        try {
            ObservableList<String> observableList = FXCollections.observableArrayList();
            //ArrayList<String> idList = customerDAO.loadALL();
            ArrayList<String> idList = placeOrderBO.loadALLCustomer();

            for (String id : idList) {
                observableList.add(id);
            }
            cmbCustomerId.setItems(observableList);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadProductIds() {
        try {
            ObservableList<String> observableList = FXCollections.observableArrayList();
           // ArrayList<String> idList = productDAO.loadCustomerIds();
            ArrayList<String> idList = placeOrderBO.loadALLProducts();

            for (String id : idList) {
                observableList.add(id);
            }
            cmbCode.setItems(observableList);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private void loadNextOrderId() {
        try {
            //String orderId = orderDAO.generateNextOrderId();
            String orderId = placeOrderBO.generateNextOrderId();
            lblOrderId.setText(orderId);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static ArrayList<PlaceOrderTM> arrayList=new ArrayList<>();
    private ObservableList<PlaceOrderTM> obList = FXCollections.observableArrayList();
    public void btnAddToCartOnAction(ActionEvent event) {
        String code = String.valueOf(cmbCode.getValue());
        String description = lblDescription.getText();
        double unitPrice = Double.parseDouble(lblUnitPrice.getText());
        int qty = Integer.parseInt(txtQty.getText());
        double total = unitPrice * qty;

        txtQty.setText("");
        lblDescription.setText("");
        lblUnitPrice.setText("");
        lblQty.setText("");

        PlaceOrderTM placeOrder =new PlaceOrderTM(code,description,qty,unitPrice,total);

        int num= -1;
        for (int i =0; i<arrayList.size();i++){
            if(arrayList.get(i).getCode().equals(placeOrder.getCode())){
                num=i;
            }
        }

        setCellValueFactory();

        if(num!=-1){
           qty= arrayList.get(num).getQty()+qty;

           total=qty*unitPrice;
            arrayList.remove(num);

            PlaceOrderTM newPlaceOrder=new PlaceOrderTM(code,description,qty,unitPrice,total);
            arrayList.add(newPlaceOrder);
            obList = FXCollections.observableArrayList(arrayList);
            tblPlaceOrder.setItems(obList);
        }else {
            arrayList.add(placeOrder);
            obList = FXCollections.observableArrayList(arrayList);
            tblPlaceOrder.setItems(obList);
        }

    }
    private void setCellValueFactory() {
        colCode.setCellValueFactory(new PropertyValueFactory("code"));
        colName.setCellValueFactory(new PropertyValueFactory("description"));
        colqty.setCellValueFactory(new PropertyValueFactory("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory("unitPrice"));
        colTot.setCellValueFactory(new PropertyValueFactory("total"));

    }

    public void btnRemoveOnAction(ActionEvent event) {
        int num =0;

        for (int i =0; i<arrayList.size();i++){
            if(arrayList.get(i).getCode().equals(code)){
                num=i;
            }
        }

        arrayList.remove(num);
        obList = FXCollections.observableArrayList(arrayList);
        tblPlaceOrder.setItems(obList);
    }

    public void btnRemoveAll(ActionEvent event) {
        arrayList.removeAll(arrayList);
        obList = FXCollections.observableArrayList(arrayList);
        tblPlaceOrder.setItems(obList);
    }

    static  String code;
    public void rowClicked(MouseEvent mouseEvent) {
        PlaceOrderTM orderTM= tblPlaceOrder.getSelectionModel().getSelectedItem();
        code=orderTM.getCode();
    }

}
