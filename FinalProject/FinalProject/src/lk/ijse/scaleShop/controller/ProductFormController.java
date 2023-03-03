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
import lk.ijse.scaleShop.bo.custom.ProductBO;
import lk.ijse.scaleShop.dao.custom.Impl.ProductDAOImpl;
import lk.ijse.scaleShop.db.DBConnection;
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
import java.util.regex.Pattern;

public class ProductFormController implements Initializable {
    public AnchorPane pane;
    public TableView tblProduct;
    public TableColumn colCustomerId;
    public TableColumn colName;
    public TableColumn colType;
    public TableColumn colQty;
    public TableColumn colContactNum;
    public TextField txtCode;
    public TextField txtType;
    public TextField txtDescription;
    public TextField txtPrice;
    public TextField txtQty;
    public JFXTextField txtSearch;
    public TableColumn colProductCode;
    public TableColumn colDescription;
    public TableColumn colPrice;

    //ObservableList<ProductDTO> proList = FXCollections.observableArrayList();
    ProductBO productBO = (ProductBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PRODUCT);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        colProductCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        try {
            txtCode.setText(productBO.generateNextId());
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        //Search bar
        txtSearch.textProperty()
                .addListener((observable, oldValue, newValue) ->{
                    loadAllProducts(newValue);
                });
        loadAllProducts("");
    }

    private void loadAllProducts(String text) {
        ObservableList<ProductDTO> proList = FXCollections.observableArrayList();

        try{
           // ArrayList<ProductDTO> productsData = productDAO.getData();
           ArrayList<ProductDTO> productsData = productBO.getData();

            for (ProductDTO productDTO :productsData){
                if(productDTO.getCode().contains(text) || productDTO.getType().contains(text)){
                    ProductDTO p = new ProductDTO(productDTO.getCode(), productDTO.getType(), productDTO.getDescription(), productDTO.getPrice(), productDTO.getQty());
                    proList.add(p);
                }
            }
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e);
        }

        tblProduct.setItems(proList);
    }



    public void btnDeleteOnAction(ActionEvent event) {
        String code = txtCode.getText();
        String type = txtType.getText();
        String description = txtDescription.getText();
        double price = Double.parseDouble(txtPrice.getText());
        int qty = Integer.parseInt(txtQty.getText());


        try{
            ProductDTO productDTO = new ProductDTO(code,type,description,price,qty);
           // boolean isDeleted = productDAO.delete(productDTO,code);
            boolean isDeleted = productBO.delete(code);
            if (isDeleted){
                new Alert(Alert.AlertType.CONFIRMATION, "ProductDTO Deleted Successfully!").show();
            }else {
                new Alert(Alert.AlertType.WARNING, "ProductDTO not Deleted!").show();
            }
        }catch (SQLException | ClassNotFoundException e){
            throw new RuntimeException(e);
        }

        int selectedID = tblProduct.getSelectionModel().getSelectedIndex();
        tblProduct.getItems().remove(selectedID);
    }

    public void btnSaveOnAction(ActionEvent event) {
        String code = txtCode.getText();
        String type = txtType.getText();
        String description = txtDescription.getText();
        double price = Double.parseDouble(txtPrice.getText());
        int qty = Integer.parseInt(txtQty.getText());

        String pattern="^(C0)([1-9]{1})([0-9]{0,})$";

        boolean isMatch= Pattern.matches(pattern,code);


        ProductDTO productDTO = new ProductDTO(code,type,description,price,qty);
        try{
          //  boolean isAdded = productDAO.save(productDTO);
            boolean isAdded = productBO.save(productDTO);
            if (isAdded){
                new Alert(Alert.AlertType.CONFIRMATION, "ProductDTO Added Successfully!").show();
                clearText();
                txtCode.setText(productBO.generateNextId());
            }else {
                new Alert(Alert.AlertType.WARNING, "ProductDTO not Added!").show();
            }
        }catch (SQLException | ClassNotFoundException e){
            throw new RuntimeException(e);
        }

        ObservableList<ProductDTO> productDTOS = tblProduct.getItems();
        productDTOS.add(productDTO);
        tblProduct.setItems(productDTOS);


    }



    public void btnUpdateOnAction(ActionEvent event) {
        String code = txtCode.getText();
        String type = txtType.getText();
        String description = txtDescription.getText();
        double price = Double.parseDouble(txtPrice.getText());
        int qty = Integer.parseInt(txtQty.getText());

        try{
            ProductDTO productDTO = new ProductDTO(code,type,description,price,qty);
            //boolean isUpdated = productDAO.update(productDTO, code);
            boolean isUpdated = productBO.update(productDTO);
            if (isUpdated){
                new Alert(Alert.AlertType.CONFIRMATION, "ProductDTO Updated Successfully!").show();
            }else {
                new Alert(Alert.AlertType.WARNING, "ProductDTO not Updated!").show();
            }
        }catch (SQLException | ClassNotFoundException e){
            throw new RuntimeException(e);
        }

        ObservableList<ProductDTO> currentTableData = tblProduct.getItems();
        String currentProductCode = txtCode.getText();

        for(ProductDTO productDTO : currentTableData){
            if(productDTO.getCode() == currentProductCode){
                productDTO.setType(txtType.getText());
                productDTO.setDescription(txtDescription.getText());
                productDTO.setPrice(Double.parseDouble(txtPrice.getText()));
                productDTO.setQty(Integer.parseInt(txtQty.getText()));

                tblProduct.setItems(currentTableData);
                tblProduct.refresh();
                break;
            }
        }
    }

    public void txtCodeOnAction(ActionEvent event) {
        String code = txtCode.getText();
        try {
            //ProductDTO productDTO = productDAO.search(code);
            ProductDTO productDTO = productBO.search(code);
            if (productDTO != null){
                fillData(productDTO);
            }
        }catch (SQLException | ClassNotFoundException e){
            throw new RuntimeException(e);
        }
    }

    private void fillData(ProductDTO productDTO) {
        txtCode.setText(productDTO.getCode());
        txtType.setText(productDTO.getType());
        txtDescription.setText(productDTO.getDescription());
        txtPrice.setText(String.valueOf(productDTO.getPrice()));
        txtQty.setText(String.valueOf(productDTO.getQty()));

    }

    public void btnClearOnAction(ActionEvent event) {
        txtCode.setText("");
        txtType.setText("");
        txtDescription.setText("");
        txtPrice.setText("");
        txtQty.setText("");

    }

    public void txtSearchOnAction(ActionEvent event) {
        String code = txtCode.getText();
        try {
            //ProductDTO productDTO = productDAO.search(code);
            ProductDTO productDTO = productBO.search(code);
            if (productDTO != null){
                fillData(productDTO);
            }
        }catch (SQLException | ClassNotFoundException e){
            throw new RuntimeException(e);
        }
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

    public void rowClicked(MouseEvent mouseEvent) {
        ProductDTO clickedProductDTO = (ProductDTO) tblProduct.getSelectionModel().getSelectedItem();
        txtCode.setText(String.valueOf(clickedProductDTO.getCode()));
        txtType.setText(String.valueOf(clickedProductDTO.getType()));
        txtDescription.setText(String.valueOf(clickedProductDTO.getDescription()));
        txtPrice.setText(String.valueOf(clickedProductDTO.getPrice()));
        txtQty.setText(String.valueOf(clickedProductDTO.getQty()));

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

    public void ProductOnAction(ActionEvent event) {
    }

    public void SupplierOnAction(ActionEvent event) throws IOException {
        Navigation.navigate(Routes.SUPPLIER,pane);
    }

    public void placeOrderOnAction(ActionEvent event) throws IOException {
        Navigation.navigate(Routes.PLACE_ORDER,pane);
    }

    public void clearText(){
        txtCode.clear();
        txtDescription.clear();
        txtType.clear();
        txtPrice.clear();
        txtQty.clear();
    }
}
