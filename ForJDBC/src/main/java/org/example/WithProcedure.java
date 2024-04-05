package org.example;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;

public class WithProcedure {
    public static void main(String[] args) throws ClassNotFoundException {
        String userName = "root";
        String password = "0000";
        String url = "jdbc:mysql://localhost:3306/test";
        Class.forName("com.mysql.cj.jdbc.Driver");
        try(Connection connection = DriverManager.getConnection(url, userName, password);
            Statement statement = connection.createStatement()){
            CallableStatement callableStatement = connection.prepareCall("{call BooksCount(?)}");
            callableStatement.registerOutParameter(1, Types.INTEGER);//регистрируем выходную переменную
            callableStatement.execute();
            System.out.println(callableStatement.getInt(1));

            CallableStatement callableStatement1 = connection.prepareCall("{call getBooks(?)}");
            callableStatement1.setInt(1, 1); //запихиваем параметр в ?
            if (callableStatement1.execute()){
                ResultSet resultSet = callableStatement1.getResultSet();
                while(resultSet.next()){
                    System.out.println(resultSet.getInt("id"));
                    System.out.println(resultSet.getString("name"));
                }
            }

            CallableStatement callableStatement2 = connection.prepareCall("{call getCount()}");
            boolean hasResult = callableStatement2.execute();
            while (hasResult){
                ResultSet resultSet2 = callableStatement2.getResultSet();
                while(resultSet2.next()){
                    System.out.println(resultSet2.getInt(1));
                }
                hasResult = callableStatement2.getMoreResults();// у нас как раз 2 вывода
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
