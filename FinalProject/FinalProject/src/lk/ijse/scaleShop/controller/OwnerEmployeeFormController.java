package lk.ijse.scaleShop.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.scaleShop.bo.BOFactory;
import lk.ijse.scaleShop.bo.SuperBO;
import lk.ijse.scaleShop.bo.custom.OwnerEmployeeBO;
import lk.ijse.scaleShop.dao.custom.Impl.OwnerEmployeeDAOImpl;
import lk.ijse.scaleShop.db.DBConnection;
import lk.ijse.scaleShop.dto.OwnerEmployeeDTO;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OwnerEmployeeFormController implements Initializable {

    public AnchorPane pane;
    public TextField txtEmployeeId;
    public TextField txtName;
    public TextField txtAddress;
    public TextField txtSalary;
    public TableView tblEmployee;
    public TableColumn colEmployeeId;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colJobRole;
    public TableColumn colSalary;
    public TextField txtJobRole;
    public JFXTextField txtSearch;
    public Label lblNameWarning;

    //ObservableList<OwnerEmployeeDTO> cusList = FXCollections.observableArrayList();

    OwnerEmployeeBO employeeBO = (OwnerEmployeeBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.O_EMPLOYEE);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("empId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colJobRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        try {
            txtEmployeeId.setText(employeeBO.generateNextId());
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        //Search bar
        txtSearch.textProperty()
                .addListener((observable, oldValue, newValue) ->{
                    loadAllEmployees(newValue);
                });
        loadAllEmployees("");
    }

    private void loadAllEmployees(String text) {
        ObservableList<OwnerEmployeeDTO> cusList = FXCollections.observableArrayList();

        try{
            //ArrayList<OwnerEmployeeDTO> employeesData = ownerEmployeeDAO.getData();
            ArrayList<OwnerEmployeeDTO> employeesData = employeeBO.getData();
            for (OwnerEmployeeDTO employee:employeesData){
                if(employee.getEmpId().contains(text) || employee.getName().contains(text)){
                    OwnerEmployeeDTO e = new OwnerEmployeeDTO(employee.getEmpId(), employee.getName(), employee.getAddress(), employee.getSalary(),employee.getRole());
                    cusList.add(e);
                }
            }
        }catch(SQLException | ClassNotFoundException e){
            System.out.println(e);
        }

        tblEmployee.setItems(cusList);
    }



    public void DeleteOnAction(ActionEvent event) {
        String empID = txtEmployeeId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        Double salary = Double.parseDouble(txtSalary.getText());
        String role =txtJobRole.getText();

        try{
            OwnerEmployeeDTO customer = new OwnerEmployeeDTO(empID,name,address,salary,role);
            //boolean isDeleted = ownerEmployeeDAO.delete(customer, empID);
            boolean isDeleted = employeeBO.delete(empID);
            if (isDeleted){
                new Alert(Alert.AlertType.CONFIRMATION, "Employee Deleted Successfully!").show();
            }else {
                new Alert(Alert.AlertType.WARNING, "Employee not Deleted!").show();
            }
        }catch (SQLException | ClassNotFoundException e){
            throw new RuntimeException(e);
        }

        int selectedID = tblEmployee.getSelectionModel().getSelectedIndex();
        tblEmployee.getItems().remove(selectedID);
    }

    public void SaveOnAction(ActionEvent event) {
        String empId = txtEmployeeId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        double salary = Double.parseDouble(txtSalary.getText());
        String role=txtJobRole.getText();

        /*String pattern="^(E0)([1-9]{1})([0-9]{0,})$";

        boolean isMatch= Pattern.matches(pattern,empId);*/

        OwnerEmployeeDTO employee = new OwnerEmployeeDTO(empId,name,address,salary,role);
        try{
           // boolean isAdded = ownerEmployeeDAO.save(employee);
            boolean isAdded = employeeBO.save(employee);
            if (isAdded){
                new Alert(Alert.AlertType.CONFIRMATION, "Employee Added Successfully!").show();
            }else {
                new Alert(Alert.AlertType.WARNING, "Employee not Added!").show();
            }
        }catch (SQLException | ClassNotFoundException e){
            throw new RuntimeException(e);
        }

        ObservableList<OwnerEmployeeDTO> employees = tblEmployee.getItems();
        employees.add(employee);
        tblEmployee.setItems(employees);

    }

    public void txtIDOnAction(ActionEvent event) {
        String id = txtEmployeeId.getText();
        try {
           //OwnerEmployeeDTO employee = ownerEmployeeDAO.search(id);
           OwnerEmployeeDTO employee = employeeBO.search(id);
            if (employee != null){
                fillData(employee);
            }
        }catch (SQLException | ClassNotFoundException e){
            throw new RuntimeException(e);
        }

        Pattern pattern= Pattern.compile("^(C0)([1-9]{1})([0-9]{0,})$");
        Matcher matcher=pattern.matcher(txtEmployeeId.getText());

        boolean isMatches =matcher.matches();
        if (isMatches){
            txtName.clear();
            txtName.requestFocus();
            lblNameWarning.setText("Invalid Input");

        }else {
           lblNameWarning.setText("");
        }
    }

    public void txtSearchOnAction(ActionEvent event) {
        String id = txtSearch.getText();
        try{
           // OwnerEmployeeDTO employee =ownerEmployeeDAO.search(id);
            OwnerEmployeeDTO employee = employeeBO.search(id);
            if (employee != null){
                fillData(employee);
            }
        }catch (SQLException | ClassNotFoundException e){
            throw new RuntimeException(e);
        }
    }

    public void btnUpdateOnAction(ActionEvent event) {
        String empId = txtEmployeeId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
       Double salary = Double.parseDouble(txtSalary.getText());
       String role = txtJobRole.getText();

        try{
            OwnerEmployeeDTO employee = new OwnerEmployeeDTO(empId,name,address,salary,role);
           // boolean isUpdated = ownerEmployeeDAO.update(employee, empId);
            boolean isUpdated = employeeBO.update(employee);
            if (isUpdated){
                new Alert(Alert.AlertType.CONFIRMATION, "Employee Updated Successfully!").show();
            }else {
                new Alert(Alert.AlertType.WARNING, "Employee not Updated!").show();
            }
        }catch (SQLException | ClassNotFoundException e){
            throw new RuntimeException(e);
        }


        ObservableList<OwnerEmployeeDTO> currentTableData = tblEmployee.getItems();
        String currentEmployeeID = txtEmployeeId.getText();

        for(OwnerEmployeeDTO employee : currentTableData){
            if(employee.getEmpId() == currentEmployeeID){
                employee.setName(txtName.getText());
                employee.setAddress(txtAddress.getText());
                employee.setSalary(Double.parseDouble(txtSalary.getText()));

                tblEmployee.setItems(currentTableData);
                tblEmployee.refresh();
                break;
            }
        }
    }
    public void btnClearOnAction(ActionEvent actionEvent) {
        txtEmployeeId.setText("");
        txtName.setText("");
        txtAddress.setText("");
       txtSalary.setText("");
       txtJobRole.setText("");
    }

    private void fillData(OwnerEmployeeDTO employee){
        txtEmployeeId.setText(employee.getEmpId());
        txtName.setText(employee.getName());
        txtAddress.setText(employee.getAddress());
        txtSalary.setText(String.valueOf(employee.getSalary()));
        txtJobRole.setText(employee.getRole());
    }


    public void rowClicked(MouseEvent mouseEvent) {
        OwnerEmployeeDTO clickedEmployee = (OwnerEmployeeDTO) tblEmployee.getSelectionModel().getSelectedItem();
        txtEmployeeId.setText(String.valueOf(clickedEmployee.getEmpId()));
        txtName.setText(String.valueOf(clickedEmployee.getName()));
        txtAddress.setText(String.valueOf(clickedEmployee.getAddress()));
        txtSalary.setText(String.valueOf(clickedEmployee.getSalary()));
        txtJobRole.setText(String.valueOf(clickedEmployee.getRole()));
    }
    public void btnReportOnAction(ActionEvent event) {
        InputStream resource = this.getClass().getResourceAsStream("/lk/ijse/scaleShop/view/report/Employee.jrxml");
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
        Navigation.navigate(Routes.OWNER_PRODUCT,pane);
    }


}
