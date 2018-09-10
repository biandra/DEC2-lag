package com.decidir;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by biandra on 06/09/18.
 */
public class MySQLConnection {

    private static final Logger LOGGER = LoggerFactory.getLogger(MySQLConnection.class);

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    public Connection get(String user, String password, String host, String port, String database) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        String ulrjdbc = "jdbc:mysql://" + host + ":" + port + "/" + database;

        Connection connection = null;
        try {
            Class.forName(JDBC_DRIVER);
            return DriverManager.getConnection(ulrjdbc, user, password);
        } catch (Exception e) {
            LOGGER.error("cant connect with database ", e);
            throw e;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    LOGGER.info("cant close database ", e);
                }
            }
        }
    }
}
