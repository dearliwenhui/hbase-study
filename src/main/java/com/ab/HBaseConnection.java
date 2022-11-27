package com.ab;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.AsyncConnection;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * @description:
 * @version: 0.0.1
 * @author: liwenhui
 * @createTime: 2022-07-17 15:02
 **/
public class HBaseConnection {

    //使用饿汉式单例创建HBase连接对象
    public static Connection connection = null;

    static {
        try {
            //创建连接，默认使用同步连接
            //如果不配置参数，HBase会去读取resources目录下的客户端配置文件hbase-site.xml
            connection = ConnectionFactory.createConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection() throws IOException {
        //判断connection是否为空
        if (connection != null) {
            connection.close();
        }
    }


    public static void main(String[] args) throws IOException {
//        //1.创建连接配置对象
//        Configuration conf = new Configuration();
//        //2.添加配置参数，HBase注册的zk地址
//        //HBase由ZK管理实现分布式，各个节点的连接信息都是注册到ZK中
//        conf.set("hbase.zookeeper.quorum","106.75.171.152:2181");
//        //3.创建连接，默认使用同步连接
//        Connection connection = ConnectionFactory.createConnection(conf);
//        //异步连接，不推荐使用
//        //CompletableFuture<AsyncConnection> asyncConnection = ConnectionFactory.createAsyncConnection(conf);
//        //4.使用连接
//        System.out.println(connection);
//        //5.关闭连接
//        connection.close();
        System.out.println(HBaseConnection.connection);
        HBaseConnection.closeConnection();
    }

}
