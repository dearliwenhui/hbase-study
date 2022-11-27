package com.ab;

import java.sql.*;

/**
 * @description:
 * @version: 0.0.1
 * @author: liwenhui
 * @createTime: 2022-10-22 10:08
 **/
public class PhoenixConnection {

    public static void main(String[] args) throws SQLException {
         //jdbc:phoenix:[zookeeper quorum hosts]
        Connection con = DriverManager.getConnection("jdbc:phoenix:106.75.171.152:2181");
        Statement stmt = con.createStatement();

        stmt.executeUpdate("create table test (mykey integer not null primary key, mycolumn varchar)");
        stmt.executeUpdate("upsert into test values (1,'Hello')");
        stmt.executeUpdate("upsert into test values (2,'World!')");
        con.commit();

        PreparedStatement statement = con.prepareStatement("select * from test");
        ResultSet rset = statement.executeQuery();
        while (rset.next()) {
            System.out.println(rset.getString("mycolumn"));
        }
        statement.close();
        con.close();
    }

}
