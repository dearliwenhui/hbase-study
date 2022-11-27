package com.ab;

import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * @description:
 * @version: 0.0.1
 * @author: liwenhui
 * @createTime: 2022-07-17 16:03
 **/
public class HBaseDDL {

    /**
     * create namespace
     *
     * @param namespace
     */
    public static void createNamespace(String namespace) throws IOException {
        //1.获取admin
        /**
         * 返回的Admin并不保证是线程安全的。应该为每个使用线程创建一个新的实例。
         * 这是一个轻量级操作。不建议对返回的Admin进行池化或缓存。
         * 调用者负责对返回的Admin实例调用Admin.close()。
         */
        Admin admin = HBaseConnection.connection.getAdmin();
        //2.创建命名空间
        NamespaceDescriptor.Builder builder = NamespaceDescriptor.create(namespace);
        //增加一个描述，只是给HBase的table起解释说明的作用，没有其他含义
        builder.addConfiguration("usage", "study");
        try {
            //捕获创建命名空间时抛出的异常
            admin.createNamespace(builder.build());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //3.关闭admin
        admin.close();
    }

    /**
     * 判断表格是否存在
     *
     * @param namespace
     * @param tableName
     * @return
     */
    public static boolean isTableExists(String namespace, String tableName) throws IOException {
        //1.获取Admin
        Admin admin = HBaseConnection.connection.getAdmin();
        boolean b = false;
        try {
            // 2.使用方法判断表格是否存在
            b = admin.tableExists(TableName.valueOf(namespace, tableName));

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //3.关闭Admin
            admin.close();
        }
        return b;
    }

    /**
     * 创建表格
     *
     * @param namespace      命名空间名称
     * @param tableName      表格名称
     * @param columnFamilies 列族名称，可以有多个
     */
    public static void createTable(String namespace, String tableName, String... columnFamilies) throws IOException {
        //验证列族是否存在
        if (columnFamilies.length == 0) {
            System.out.println("列族不能为空");
            return;
        }

        if (isTableExists(namespace, tableName)) {
            System.out.println("表格已经存在");
            return;
        }
        //1.获取Admin
        Admin admin = HBaseConnection.connection.getAdmin();
        try {
            //2.调用方法创建表格
            //2.1 创建表格描述的建造者
            TableDescriptorBuilder tableDescriptorBuilder = TableDescriptorBuilder.newBuilder(TableName.valueOf(namespace, tableName));
            //2.2添加参数
            for (String columnFamily : columnFamilies) {
                //2.3创建列族描述的建造者
                ColumnFamilyDescriptorBuilder columnFamilyDescriptorBuilder = ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(columnFamily));
                //2.4 对应的当前的列族添加参数
                //添加版本参数
                columnFamilyDescriptorBuilder.setMaxVersions(5);
                //2.5创建添加完参数的列族描述
                tableDescriptorBuilder.setColumnFamily(columnFamilyDescriptorBuilder.build());
            }
            //3.创建对应的表格描述
            admin.createTable(tableDescriptorBuilder.build());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //4.关闭Admin
            admin.close();
        }

    }

    /**
     * 修改表格中一个列族的的版本
     *
     * @param namespace
     * @param tableName
     * @param columnFamily
     * @param version
     */
    public static void modifyTable(String namespace, String tableName, String columnFamily, int version) throws IOException {
        if (!isTableExists(namespace, tableName)) {
            System.out.println("表格不存在");
            return;
        }
        //1.获取Admin
        Admin admin = HBaseConnection.connection.getAdmin();
        try {
            //2.调用方法修改表格
            //获取之前的表格描述
            TableDescriptor descriptor = admin.getDescriptor(TableName.valueOf(namespace, tableName));
            //创建一个表格描述建造者
            //如果使用填写{TableName}的方法，相当于创建了一个新的表格描述建造者，修改这个新建的TableDescriptorBuilder运行会报错
            //如果想要修改已存在的表格的信息，必须调用{TableDescriptor}方法填写一个要被修改的表格描述
            TableDescriptorBuilder tableDescriptorBuilder = TableDescriptorBuilder.newBuilder(descriptor);
            //对建造者进行表格数据的修改
            //获取待修改列族描述
            ColumnFamilyDescriptor columnFamily1 = descriptor.getColumnFamily(Bytes.toBytes(columnFamily));
            //创建列族描述建造者
            ColumnFamilyDescriptorBuilder columnFamilyDescriptorBuilder = ColumnFamilyDescriptorBuilder.newBuilder(columnFamily1);
            //修改对应版本
            columnFamilyDescriptorBuilder.setMaxVersions(version);
            tableDescriptorBuilder.modifyColumnFamily(columnFamilyDescriptorBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            admin.close();
        }

    }

    /**
     * 删除表格
     *
     * @param namespace
     * @param tableName
     * @return
     */
    public static boolean deleteTable(String namespace, String tableName) throws IOException {
        //1.判断表格是否存在
        if (!isTableExists(namespace, tableName)) {
            System.out.println("表格不存在");
            return false;
        }
        //2.获取admin
        Admin admin = HBaseConnection.connection.getAdmin();
        try {
            //3.删除表格
            TableName tableName1 = TableName.valueOf(namespace, tableName);
            //HBase删除表格之前，一定要先标记表格为不可用。(生产中不会去调用删除功能，先注释掉)
            //admin.disableTable(tableName1);
            admin.deleteTable(tableName1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            admin.close();
        }
        return true;

    }


    public static void main(String[] args) throws IOException {
        String namespace = "mystudy";
        String tableName = "student";
        //创建命名空间
//        createNamespace(namespace);
//        System.out.println("创建完成");
        //测试表格是否存在
        System.out.println(isTableExists(namespace, tableName));
        //创建表格测试
        createTable(namespace, tableName, "info", "msg");
        System.out.println(isTableExists(namespace, tableName));
        modifyTable(namespace, tableName, "info", 6);


        HBaseConnection.closeConnection();
    }

}
