package lk.ijse.scaleShop.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.scaleShop.bo.BOFactory;
import lk.ijse.scaleShop.bo.SuperBO;
import lk.ijse.scaleShop.bo.custom.CustomerBO;
import lk.ijse.scaleShop.bo.custom.ProductBO;
import lk.ijse.scaleShop.dao.custom.Impl.CustomerDAOImpl;
import lk.ijse.scaleShop.dao.custom.Impl.ProductDAOImpl;
import lk.ijse.scaleShop.db.DBConnection;
import lk.ijse.scaleShop.dto.CustomerDTO;
import lk.ijse.scaleShop.dto.ProductDTO;
import lk.ijse.scaleShop.util.Navigation;
import lk.ijse.scaleShop.util.Routes;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CustomerFormController implements Initializable {


    public AnchorPane pane;
    public TextField txtCustomerId;
    public TextField txtName;
    public TextField txtAddress;
    public TextField txtContactNo;
    public TableView tblCustomer;
    public TableColumn colCustomerId;
    public TableColumn colName;
    
    public TableColumn colNumber;
    public TableColumn colEmployeeId;

    public TextField txtSearch;
    public TextField txtEmployeeId;
    public TableColumn colAddress;
    public TableColumn colContact;
    public TableColumn colEmployee;
    CustomerBO customerBO = (CustomerBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CUSTOMER);
    ProductBO productBO = (ProductBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PRODUCT);
    //ObservableList<CustomerDTO> cusList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("custId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("number"));
        colEmployee.setCellValueFactory(new PropertyValueFactory<>("empId"));
        try {
            txtCustomerId.setText(customerBO.generateNextId());
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        //Search bar
        txtSearch.textProperty()
                .addListener((observable, oldValue, newValue) ->{
                    loadAllCustomers(newValue);
                });
        loadAllCustomers("");
    }

    private void loadAllCustomers(String text) {
        ObservableList<CustomerDTO> cusList = FXCollections.observableArrayList();

        try{
            //CustomerDAOImpl customerDAO = new CustomerDAOImpl();
            ArrayList<CustomerDTO> customersData = customerBO.getData();
            cusList.addAll(customersData);
//            for (CustomerDTO customerDTO :customersData){
//                if(customerDTO.getCustId().contains(text) || customerDTO.getName().contains(text)){
//                    CustomerDTO c = new CustomerDTO(customerDTO.getCustId(), customerDTO.getName(), customerDTO.getAddress(), customerDTO.getNumber(), customerDTO.getEmpId());
//                    cusList.add(c);
//                }
//            }
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e);
        }

        tblCustomer.setItems(cusList);

    }

    public void DeleteOnAction(ActionEvent event) {
        String custId = txtCustomerId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        int  number = Integer.parseInt(txtContactNo.getText());
        String empId= txtEmployeeId.getText();



        try{
           // ProductDTO productDTO = new ProductDTO(custId,name,address,number,empId);
          //  boolean isDeleted = productDAO.delete(productDTO,custId);
           boolean isDeleted = productBO.delete(custId);
            if (isDeleted){
                new Alert(Alert.AlertType.CONFIRMATION, "CustomerDTO Deleted Successfully!").show();
            }else {
                new Alert(Alert.AlertType.WARNING, "CustomerDTO not Deleted!").show();
            }
        }catch (SQLException | ClassNotFoundException e){
            throw new RuntimeException(e);
        }

        int selectedID = tblCustomer.getSelectionModel().getSelectedIndex();
        tblCustomer.getItems().remove(selectedID);


    }

    public void SaveOnAction(ActionEvent event) {
        String custId = txtCustomerId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        int number = Integer.parseInt(txtContactNo.getText());
        String empId = txtEmployeeId.getText();

        /*String pattern="^(C0)([1-9]{1})([0-9]{0,})$";

        boolean isMatch= Pattern.matches(pattern,custId);*/

        CustomerDTO customerDTO = new CustomerDTO(custId,name,address,number,empId);
        try{
           // boolean isAdded = customerDAO.save(customerDTO);
            boolean isAdded = customerBO.save(customerDTO);
            if (isAdded){
                new Alert(Alert.AlertType.CONFIRMATION, "CustomerDTO Added Successfully!").show();
            }else {
                new Alert(Alert.AlertType.WARNING, "CustomerDTO not Added!").show();
            }
        }catch (SQLException | ClassNotFoundException e){
            throw new RuntimeException(e);
        }

        ObservableList<CustomerDTO> customerDTOS = tblCustomer.getItems();
        customerDTOS.add(customerDTO);
        tblCustomer.setItems(customerDTOS);
        System.out.println(customerDTOS);
    }

    public void txtCustomerIdOnAction(ActionEvent event) {
        String id = txtSearch.getText();
        try{
            //CustomerDTO customerDTO = customerDAO.search(id);
            CustomerDTO customerDTO = customerBO.search(id);
            if (customerDTO != null){
                fillData(customerDTO);
            }
        }catch (SQLException | ClassNotFoundException e){
            throw new RuntimeException(e);
        }
    }

    public void rowClicked(MouseEvent mouseEvent) {
        CustomerDTO clickedCustomerDTO = (CustomerDTO) tblCustomer.getSelectionModel().getSelectedItem();
        txtCustomerId.setText(String.valueOf(clickedCustomerDTO.getCustId()));
        txtName.setText(String.valueOf(clickedCustomerDTO.getName()));
        txtAddress.setText(String.valueOf(clickedCustomerDTO.getAddress()));
        txtContactNo.setText(String.valueOf(clickedCustomerDTO.getNumber()));
        txtEmployeeId.setText(String.valueOf(clickedCustomerDTO.getEmpId()));
    }



    public void UpdateOnAction(ActionEvent event) {
        String custId = txtCustomerId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        int number = Integer.parseInt(txtContactNo.getText());
        String empId=txtEmployeeId.getText();

        try{
            CustomerDTO customerDTO = new CustomerDTO(custId,name,address,number,empId);
          //  boolean isUpdated = customerDAO.update(customerDTO, custId);
            boolean isUpdated = customerBO.update(customerDTO);
            if (isUpdated){
                new Alert(Alert.AlertType.CONFIRMATION, "CustomerDTO Updated Successfully!").show();
            }else {
                new Alert(Alert.AlertType.WARNING, "CustomerDTO not Updated!").show();
            }
        }catch (SQLException | ClassNotFoundException e){
            throw new RuntimeException(e);
        }


        ObservableList<CustomerDTO> currentTableData = tblCustomer.getItems();
        String currentCustomerId = txtCustomerId.getText();

        for(CustomerDTO customerDTO : currentTableData){
            if(customerDTO.getCustId() == currentCustomerId){
                customerDTO.setName(txtName.getText());
                customerDTO.setAddress(txtAddress.getText());
                customerDTO.setNumber(Integer.parseInt(txtContactNo.getText()));
                customerDTO.setEmpId(txtEmployeeId.getText());

                tblCustomer.setItems(currentTableData);
                tblCustomer.refresh();
                break;
            }
        }

    }
    public void ClearOnAction(ActionEvent event) {
        txtCustomerId.setText("");
        txtName.setText("");
        txtAddress.setText("");
        txtContactNo.setText("");
        txtEmployeeId.setText("");
    }
    private void fillData(CustomerDTO customerDTO){
        txtCustomerId.setText(customerDTO.getCustId());
        txtName.setText(customerDTO.getName());
        txtAddress.setText(customerDTO.getAddress());
        txtContactNo.setText(String.valueOf(customerDTO.getNumber()));
        txtEmployeeId.setText(customerDTO.getEmpId());

    }
    public void btnReportOnAction(ActionEvent event) {
        InputStream resource = this.getClass().getResourceAsStream("/lk/ijse/scaleShop/view/report/Customer.jrxml");
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(resource);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException | SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.toString()).show();
        }
    }


    public void LogoutOnAction(ActionEvent event) throws IOException {
        Navigation.navigate(Routes.LOGIN,pane);
    }

    public void DashboardOnAction(ActionEvent event) throws IOException {
        Navigation.navigate(Routes.ADMIN_DASHBOARD,pane);
    }

    public void IncomeOnAction(ActionEvent event) throws IOException {
        Navigation.navigate(Routes.INCOME_REPORT,pane);
    }

    public void BillOnAction(ActionEvent event) throws IOException {
        Navigation.navigate(Routes.BILL,pane);
    }

    public void CustomerOnAction(ActionEvent event) {

    }

    public void ProductOnAction(ActionEvent event) throws IOException {
        Navigation.navigate(Routes.PRODUCT,pane);
    }

    public void SupplierOnAction(ActionEvent event) throws IOException {
        Navigation.navigate(Routes.SUPPLIER,pane);
    }

    public void placeOrderOnAction(ActionEvent event) throws IOException {
        Navigation.navigate(Routes.PLACE_ORDER,pane);
    }


}
