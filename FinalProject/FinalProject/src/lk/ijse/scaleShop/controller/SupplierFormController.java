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
import lk.ijse.scaleShop.bo.custom.SupplierBO;
import lk.ijse.scaleShop.dao.custom.Impl.SupplierDAOImpl;
import lk.ijse.scaleShop.db.DBConnection;
import lk.ijse.scaleShop.dto.SupplierDTO;
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

public class SupplierFormController implements Initializable {


    public AnchorPane pane;
    public TextField txtId;
    public TextField txtName;
    public TextField txtType;
    public TextField txtQuantity;
    public TableView tblSupplier;
    public TableColumn colSupplierId;
    public TableColumn colSupplierName;
    public TableColumn colType;
    public TableColumn colQty;
    public TableColumn colNumber;
    public TextField txtNo;
    public TextField txtSearch;

    //ObservableList<SupplierDTO> cusList = FXCollections.observableArrayList();
    SupplierBO supplierBO = (SupplierBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.SUPPLIER);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colSupplierName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colNumber.setCellValueFactory(new PropertyValueFactory<>("number"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        //Search bar
        try {
            txtId.setText(supplierBO.generateNextId());
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        txtSearch.textProperty()
                .addListener((observable, oldValue, newValue) ->{
                    loadAllSupplier(newValue);
                });
        loadAllSupplier("");
    }

    private void loadAllSupplier(String text) {
        ObservableList<SupplierDTO> cusList = FXCollections.observableArrayList();

        try{
            ArrayList<SupplierDTO> suppliersData = supplierBO.getData();
            for (SupplierDTO supplierDTO :suppliersData){
                if(supplierDTO.getId().contains(text) || supplierDTO.getName().contains(text)){
                    SupplierDTO s = new SupplierDTO(supplierDTO.getId(), supplierDTO.getName(), supplierDTO.getNumber(), supplierDTO.getType(), supplierDTO.getQty());
                    cusList.add(s);
                }
            }
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e);
        }

        tblSupplier.setItems(cusList);
    }



    public void DeleteOnAction(ActionEvent event) {
        int selectedID = tblSupplier.getSelectionModel().getSelectedIndex();
        tblSupplier.getItems().remove(selectedID);

    }
    public void SaveOnAction(ActionEvent event) {
        String id = txtId.getText();
        String name = txtName.getText();
        int number = Integer.parseInt(txtNo.getText());
        String type = txtType.getText();
        int qty = Integer.parseInt(txtQuantity.getText());

        String pattern="^(S0)([1-9]{1})([0-9]{0,})$";

        boolean isMatch= Pattern.matches(pattern,id);

        SupplierDTO supplierDTO = new SupplierDTO(id,name,number,type,qty);
        try{
            boolean isAdded = supplierBO.save(supplierDTO);
            if (isAdded){
                new Alert(Alert.AlertType.CONFIRMATION, "SupplierDTO Added Successfully!").show();
                clearText();
                txtId.setText(supplierBO.generateNextId());
            }else {
                new Alert(Alert.AlertType.WARNING, "SupplierDTO not Added!").show();
            }
        }catch (SQLException | ClassNotFoundException e){
            throw new RuntimeException(e);
        }
        ObservableList<SupplierDTO> supplierDTOS = tblSupplier.getItems();
        supplierDTOS.add(supplierDTO);
        tblSupplier.setItems(supplierDTOS);
    }



    public void rowClicked(MouseEvent mouseEvent) {
       SupplierDTO clickedSupplierDTO = (SupplierDTO) tblSupplier.getSelectionModel().getSelectedItem();
        txtId.setText(String.valueOf(clickedSupplierDTO.getId()));
        txtName.setText(String.valueOf(clickedSupplierDTO.getName()));
        txtNo.setText(String.valueOf(clickedSupplierDTO.getNumber()));
        txtType.setText(String.valueOf(clickedSupplierDTO.getType()));
        txtQuantity.setText(String.valueOf(clickedSupplierDTO.getQty()));
    }


    public void btnUpdateOnAction(ActionEvent event) {
        String id = txtId.getText();
        String name = txtName.getText();
        int number = Integer.parseInt(txtNo.getText());
        String type = txtType.getText();
        int qty = Integer.parseInt(txtQuantity.getText());


        try {
            SupplierDTO supplierDTO = new SupplierDTO(id, name, number, type, qty);
            boolean isUpdated = supplierBO.update(supplierDTO);
            if (isUpdated) {
                tblSupplier.refresh();
                new Alert(Alert.AlertType.CONFIRMATION, "SupplierDTO Updated Successfully!").show();
            } else {
                new Alert(Alert.AlertType.WARNING, "SupplierDTO not Updated!").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        ObservableList<SupplierDTO> currentTableData = tblSupplier.getItems();
        String currentSupplierId = txtId.getText();

        for(SupplierDTO supplierDTO : currentTableData){
            if(supplierDTO.getId() == currentSupplierId){
                supplierDTO.setName(txtName.getText());
                supplierDTO.setNumber(Integer.parseInt(txtNo.getText()));
                supplierDTO.setType(txtType.getText());
                supplierDTO.setQty(Integer.parseInt(txtQuantity.getText()));

               tblSupplier.setItems(currentTableData);
               tblSupplier.refresh();
                break;
            }
        }
    }

    public void btnClearOnAction(ActionEvent event) {
        txtId.setText("");
        txtName.setText("");
        txtNo.setText("");
        txtType.setText("");
        txtQuantity.setText("");
    }

    public void btnReportOnAction(ActionEvent event) {
        InputStream resource = this.getClass().getResourceAsStream("/lk/ijse/scaleShop/view/report/Supplier.jrxml");
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(resource);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException | SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.toString()).show();
        }
    }

    public void txtIdOnAction(ActionEvent event) {
        String id = txtSearch.getText();
        try{
            SupplierDTO supplierDTO = supplierBO.search(id);
            if (supplierDTO != null){
                fillData(supplierDTO);
            }
        }catch (SQLException | ClassNotFoundException e){
            throw new RuntimeException(e);
        }
    }
    private void fillData(SupplierDTO supplierDTO){
        txtId.setText(supplierDTO.getId());
        txtName.setText(supplierDTO.getName());
        txtNo.setText(String.valueOf(supplierDTO.getNumber()));
        txtType.setText(supplierDTO.getType());
        txtQuantity.setText(String.valueOf(supplierDTO.getQty()));
    }

    public void txtSearchOnAction(ActionEvent event) {
        String id = txtSearch.getText();
        try{
            SupplierDTO supplierDTO = supplierBO.search(id);
            if (supplierDTO != null){
                fillData(supplierDTO);
            }
        }catch (SQLException | ClassNotFoundException e){
            throw new RuntimeException(e);
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

    public void CustomerOnAction(ActionEvent event) throws IOException {
        Navigation.navigate(Routes.CUSTOMER,pane);
    }

    public void ProductOnAction(ActionEvent event) throws IOException {
        Navigation.navigate(Routes.PRODUCT,pane);
    }

    public void SupplierOnAction(ActionEvent event) {
    }

    public void placeOrderOnAction(ActionEvent event) throws IOException {
        Navigation.navigate(Routes.PLACE_ORDER,pane);
    }

    public void clearText(){
        txtNo.clear();
        txtId.clear();
        txtName.clear();
        txtType.clear();
        txtQuantity.clear();
    }
}
