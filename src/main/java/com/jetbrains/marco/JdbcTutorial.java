package com.jetbrains.marco;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcTutorial {

    static final String JDBC_URL = "jdbc:h2:mem:;INIT=RUNSCRIPT FROM 'classpath:users.sql'";

    public static void main(String[] args) {

        // preferable to use a data source withing a connection pool
        DataSource dataSource = createDataSource();

//        try (Connection connection = DriverManager.getConnection(JDBC_URL)) {
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("connection.isValid(0) = " + connection.isValid(0));

            // CRUD

            // select

            PreparedStatement ps = connection.prepareStatement("select * from USERS where name = ?");
            ps.setString(1, "Marco");

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("id") + " - " + resultSet.getString("name"));
            }

            // inserts

            PreparedStatement insertPs = connection.prepareStatement("insert into USERS (name) values (?)");
            insertPs.setString(1, "John");
            int insertCount = insertPs.executeUpdate();
            System.out.println("insertCount = " + insertCount);

            // updates

            PreparedStatement updatePs = connection.prepareStatement("update USERS set name = ? where name = ?");
            updatePs.setString(1, "Johnny");
            updatePs.setString(2, "John");
            int updateCount = updatePs.executeUpdate();
            System.out.println("updateCount = " + updateCount);

            // deletes

            PreparedStatement deletePs = connection.prepareStatement("delete from USERS where name = ?");
            deletePs.setString(1, "Johnny");
            int deleteCount = deletePs.executeUpdate();
            System.out.println("deleteCount = " + deleteCount);


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static DataSource createDataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(JDBC_URL);
        return ds;
    }
}
