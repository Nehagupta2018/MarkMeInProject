/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.MarkMeIn.DBSchema;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author hp
 */
public class DBUtility {
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
//            conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.1.24:1521:orcl", "servostreams104", "system123#");
            conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/orc1", "ORACLEDB", "Jayant@2019");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return conn;
    }

}
