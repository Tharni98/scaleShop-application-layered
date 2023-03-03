package lk.ijse.scaleShop.controller;

import com.jfoenix.controls.JFXTextField;
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
import lk.ijse.scaleShop.bo.custom.OwnerProductBO;
import lk.ijse.scaleShop.bo.custom.ProductBO;
import lk.ijse.scaleShop.dao.custom.Impl.OwnerProductDAOImpl;
import lk.ijse.scaleShop.db.DBConnection;
import lk.ijse.scaleShop.dto.OwnerProductDTO;
import lk.ijse.scaleShop.dto.ProductDTO;
import lk.ijse.scaleShop.entity.OwnerProduct;
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
import java.util.regex.Pattern;


public class OwnerProductFormController  implements Initializable {

    public AnchorPane pane;
    public TextField txtCode;
    public TextField txtType;
    public TextField txtDescription;
    public TextField txtPrice;
    public TextField txtQty;

    public JFXTextField txtSearch;
    public TableColumn colCode;
    public TableColumn colProductType;
    public TableColumn colDescription;
    public TableColumn colPrice;
    public TableColumn colQty;
    public TableView tblOwnerProduct;

    //ObservableList<OwnerProductDTO> cusList = FXCollections.observableArrayList();
    OwnerProductBO ownerProductBO = (OwnerProductBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.O_PRODUCT);
    private OwnerProduct clickedProductDTO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colProductType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        //Search bar
        try {
            txtCode.setText(ownerProductBO.generateNextId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        txtSearch.textProperty()
                .addListener((observable, oldValue, newValue) ->{
                    loadAllCustomers(newValue);
                });
        loadAllCustomers("");
    }

    private void loadAllCustomers(String text) {
        ObservableList<OwnerProductDTO> cusList = FXCollections.observableArrayList();

        try{
            ArrayList<OwnerProductDTO> ownerProductsDatumDTOS = ownerProductBO.getOwnerProductsData();
            for (OwnerProductDTO ownerProductDTO : ownerProductsDatumDTOS){
                if(ownerProductDTO.getCode().contains(text) || ownerProductDTO.getType().contains(text)){
                    OwnerProductDTO OP = new OwnerProductDTO(ownerProductDTO.getCode(), ownerProductDTO.getType(), ownerProductDTO.getDescription(), ownerProductDTO.getPrice(), ownerProductDTO.getQty());
                    cusList.add(OP);
                }
            }
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e);
        }

        tblOwnerProduct.setItems(cusList);
    
    }


    public void DeleteOnAction(ActionEvent event) {
        String code = txtCode.getText();
        String type = txtType.getText();
        String description = txtDescription.getText();
        Double price = Double.parseDouble(txtPrice.getText());
        int qty = Integer.parseInt(txtQty.getText());

        try{
            OwnerProductDTO ownerProductDTO = new OwnerProductDTO(code,type,description,price,qty);
            //boolean isDeleted = productDAO.delete(ownerProductDTO,code);
            boolean isDeleted = ownerProductBO.delete(code);

            if (isDeleted){
                new Alert(Alert.AlertType.CONFIRMATION, "ProductDTO Deleted Successfully!").show();
            }else {
                new Alert(Alert.AlertType.WARNING, "ProductDTO not Deleted!").show();
            }
        }catch (SQLException | ClassNotFoundException e){
            throw new RuntimeException(e);
        }

        int selectedcode = tblOwnerProduct.getSelectionModel().getSelectedIndex();
        tblOwnerProduct.getItems().remove(selectedcode);


    }

    public void SaveOnAction(ActionEvent event) {
        String code = txtCode.getText();
        String type = txtType.getText();
        String description = txtDescription.getText();
        double price=Double.parseDouble(txtPrice.getText());
        int qty = Integer.parseInt(txtQty.getText());

        String pattern="^(P0)([1-9]{1})([0-9]{0,})$";

        boolean isMatch= Pattern.matches(pattern,code);

        OwnerProductDTO ownerProductDTO = new OwnerProductDTO(code,type,description,price,qty);
        try{
            //boolean isAdded = productDAO.save(ownerProductDTO);
            boolean isAdded = ownerProductBO.save(ownerProductDTO);
            if (isAdded){
                new Alert(Alert.AlertType.CONFIRMATION, "ProductDTO Added Successfully!").show();
            }else {
                new Alert(Alert.AlertType.WARNING, "ProductDTO not Added!").show();
            }
        }catch (SQLException | ClassNotFoundException e){
            throw new RuntimeException(e);
        }

        ObservableList<OwnerProductDTO> ownerProductDTOS = tblOwnerProduct.getItems();
        ownerProductDTOS.add(ownerProductDTO);
        tblOwnerProduct.setItems(ownerProductDTOS);


    }

    public void txtCodeOnAction(ActionEvent event) {
        String code = txtCode.getText();
        try {
           // OwnerProductDTO ownerProductDTO =productDAO .search(code);
            OwnerProductDTO ownerProductDTO =ownerProductBO.search(code);
            if (ownerProductDTO != null){
                fillData(ownerProductDTO);
            }
        }catch (SQLException | ClassNotFoundException e){
            throw new RuntimeException(e);
        }


    }

    public void rowClicked(MouseEvent mouseEvent) {
        OwnerProductDTO clickedCustomer = (OwnerProductDTO) tblOwnerProduct.getSelectionModel().getSelectedItem();
        txtCode.setText(String.valueOf(clickedCustomer.getCode()));
        txtType.setText(String.valueOf(clickedCustomer.getType()));
        txtDescription.setText(String.valueOf(clickedCustomer.getDescription()));
        txtPrice.setText(String.valueOf(clickedCustomer.getPrice()));
        txtPrice.setText(String.valueOf(clickedCustomer.getPrice()));
        txtQty.setText(String.valueOf(clickedCustomer.getQty()));


    }



    public void btnUpdateOnAction(ActionEvent event) {
        String code = txtCode.getText();
        String type = txtType.getText();
        String description = txtDescription.getText();
        double price=Double.parseDouble(txtPrice.getText());
        int qty = Integer.parseInt(txtQty.getText());

        try{
            OwnerProductDTO ownerProductDTO = new OwnerProductDTO(code,type,description,price,qty);
           // boolean isUpdated = productDAO.update(ownerProductDTO, code);
            boolean isUpdated = ownerProductBO.update(ownerProductDTO);
            if (isUpdated){
                new Alert(Alert.AlertType.CONFIRMATION, "ProductDTO Updated Successfully!").show();
            }else {
                new Alert(Alert.AlertType.WARNING, "ProductDTO not Updated!").show();
            }
        }catch (SQLException | ClassNotFoundException e){
            throw new RuntimeException(e);
        }


        ObservableList<OwnerProductDTO> currentTableData = tblOwnerProduct.getItems();
        String currentOwnerProductCode = txtCode.getText();

        for(OwnerProductDTO ownerProductDTO : currentTableData){
            if(ownerProductDTO.getCode() == currentOwnerProductCode){
                ownerProductDTO.setType(txtType.getText());
                ownerProductDTO.setDescription(txtDescription.getText());
                ownerProductDTO.setPrice(Double.parseDouble(txtPrice.getText()));
                ownerProductDTO.setQty(Integer.parseInt(txtQty.getText()));

                tblOwnerProduct.setItems(currentTableData);
                tblOwnerProduct.refresh();
                break;
            }

        }

    }

        public void txtSearchOnAction(ActionEvent event) {
            String code = txtSearch.getText();
            try{
               // OwnerProductDTO ownerProductDTO = productDAO.search(code);//**********
                OwnerProductDTO ownerProductDTO = ownerProductBO.search(code);//**********
                if (ownerProductDTO != null){
                    fillData(ownerProductDTO);
                }
            }catch (SQLException | ClassNotFoundException e){
                throw new RuntimeException(e);
            }
        }



    private void fillData(OwnerProductDTO ownerProductDTO) {
            txtCode.setText(ownerProductDTO.getCode());
            txtType.setText(ownerProductDTO.getType());
            txtDescription.setText(ownerProductDTO.getDescription());
            txtPrice.setText(String.valueOf(ownerProductDTO.getPrice()));
            txtQty.setText(String.valueOf(ownerProductDTO.getPrice()));

    }

    public void btnReportOnAction(ActionEvent event) {
        InputStream resource = this.getClass().getResourceAsStream("/lk/ijse/scaleShop/view/report/product Report.jrxml");
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(resource);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException | SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.toString()).show();
        }
    }

    public void btnClearOnAction(ActionEvent event) {
        txtCode.setText("");
        txtType.setText("");
        txtDescription.setText("");
        txtPrice.setText("");
        txtQty.setText("");
    }
    public void LogoutOnAction(ActionEvent event) throws IOException {
        Navigation.navigate(Routes.LOGIN,pane);
    }

    public void DashboardOnAction(ActionEvent event) throws IOException {
        Navigation.navigate(Routes.OWNER_DASHBOARD,pane);
    }

    public void IncomeOnAction(ActionEvent event) throws IOException {
        Navigation.navigate(Routes.OWNER_INCOME,pane);
    }

    public void BillOnAction(ActionEvent event) throws IOException {
        Navigation.navigate(Routes.OWNER_BILL,pane);
    }

    public void EmployeeOnAction(ActionEvent event) throws IOException {
        Navigation.navigate(Routes.OWNER_EMPLOYEE,pane);
    }

    public void ProductOnAction(ActionEvent event) throws IOException {

    }


}


