package org.example;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;

public class Cashing {
    static String userName = "root";
    static String password = "0000";
    static String url = "jdbc:mysql://localhost:3306/test";
    static ResultSet getData() throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        try(Connection connection = DriverManager.getConnection(url, userName, password);
            Statement stat = connection.createStatement()) {

            RowSetFactory factory = RowSetProvider.newFactory();
            CachedRowSet cachedRowSet = factory.createCachedRowSet();

            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery("select * from books");
            cachedRowSet.populate(resultSet);//закэшировали, resultSet ничего не выведет теперь сам по себе
            return cachedRowSet;//он наследник resultSet
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        ResultSet resultSet = getData();
        while (resultSet.next()){// мы сдесь забираем данные из таблицы, хотя connection с ней уже закрыт
            System.out.println(resultSet.getString("name"));
        }
        CachedRowSet cachedRowSet = (CachedRowSet) resultSet;
        cachedRowSet.setUrl(url);
        cachedRowSet.setUsername(userName);
        cachedRowSet.setPassword(password);
        cachedRowSet.setCommand("select * from books where id = ?");
        cachedRowSet.setInt(1,1);//вставили в ?
        cachedRowSet.setPageSize(20);// до 20 записей
        cachedRowSet.execute();
        do {
            while (cachedRowSet.next()){
                System.out.println(cachedRowSet.getString("name"));
            }
        }while (cachedRowSet.nextPage());
        System.out.println("Теперь вставка");

        ResultSet resultSet1 = getData();
        CachedRowSet cachedRowSet1 = (CachedRowSet) resultSet1;
        cachedRowSet1.setTableName("books");
        cachedRowSet1.absolute(1);
        cachedRowSet1.updateString("name", "new inferno");
        cachedRowSet1.updateRow();
//        cachedRowSet1.deleteRow();
//        cachedRowSet1.beforeFirst();
        //теперь нужно чтобы изменения вступили в силу в самой базе, для этого 2 варианта:
        cachedRowSet1.acceptChanges(DriverManager.getConnection(url, userName, password));// ошибка тут, не знаю в чём
        //или так
//        cachedRowSet1.setUrl(url);
//        cachedRowSet1.setUsername(userName);
//        cachedRowSet1.setPassword(password);
//        cachedRowSet1.acceptChanges();
    }
}
