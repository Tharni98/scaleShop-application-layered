package lk.ijse.scaleShop.dao;

import java.sql.SQLException;
import java.util.ArrayList;

public interface    CrudDAO<T> extends SuperDAO{
    ArrayList<T> getData() throws SQLException, ClassNotFoundException;

    boolean save(T obj) throws SQLException, ClassNotFoundException;

    T search(String Id) throws SQLException, ClassNotFoundException;

    boolean update(T obj) throws SQLException, ClassNotFoundException;

    boolean delete(String Id) throws SQLException, ClassNotFoundException;

    String generateNextId() throws SQLException, ClassNotFoundException;


}
