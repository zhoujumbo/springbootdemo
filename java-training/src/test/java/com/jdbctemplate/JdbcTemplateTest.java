package com.jdbctemplate;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcTemplateTest {

    private long begin = 33112001;//起始id
    private long end = begin+100000;//每次循环插入的数据量
    private String url = "jdbc:mysql://localhost:3306/mercadolibre?useServerPrepStmts=false&rewriteBatchedStatements=true&useUnicode=true&amp;characterEncoding=UTF-8";
    private String user = "root";
    private String password = "111111";
    /**
     * JDBC直接处理（开启事务、关闭事务）
     */
    @Test
    public void jdbcBigData1() throws SQLException {
        //定义连接、statement对象
        Connection conn = null;
        PreparedStatement pstm = null;
        try {
            //加载jdbc驱动
            Class.forName("com.mysql.jdbc.Driver");
            //连接mysql
            conn = DriverManager.getConnection(url, user, password);
            //将自动提交关闭 开启事务
            conn.setAutoCommit(false);
            //编写sql
            String sql = "SELECT * FROM ml_goods_mx";
            //预编译sql
            pstm = conn.prepareStatement(sql);
            //开始总计时
            long bTime1 = System.currentTimeMillis();

            //循环10次，每次一万数据，一共10万
//            for(int i=0;i<10;i++) {
//                //开启分段计时，计1W数据耗时
//                long bTime = System.currentTimeMillis();
//                //开始循环
//                while (begin < end) {
//                    //赋值
//                    pstm.setLong(1, begin);
//                    pstm.setString(2, RandomValue.getChineseName());
//                    pstm.setString(3, RandomValue.name_sex);
//                    pstm.setInt(4, RandomValue.getNum(1, 100));
//                    pstm.setString(5, RandomValue.getEmail(4, 15));
//                    pstm.setString(6, RandomValue.getTel());
//                    pstm.setString(7, RandomValue.getRoad());
//                    //执行sql
                    pstm.execute();
//                    begin++;
//                }
//                //提交事务
////                conn.commit();
//                //边界值自增10W
////                end += 10000;
//                //关闭分段计时
////                long eTime = System.currentTimeMillis();
//                //输出
////                System.out.println("成功插入1W条数据耗时："+(eTime-bTime));
//            }
            //关闭总计时
            long eTime1 = System.currentTimeMillis();
            //提交事务
            conn.commit();
            //输出
            System.out.println("共耗时："+(eTime1-bTime1));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
    }


    /**
     * JDBC批处理（开启事务、无事务）
     */
    @Test
    public void searchBigData() {
        //定义连接、statement对象
        Connection conn = null;
        PreparedStatement pstm = null;
        try {
            //加载jdbc驱动
            Class.forName("com.mysql.jdbc.Driver");
            //连接mysql
            conn = DriverManager.getConnection(url, user, password);
            //将自动提交关闭
             conn.setAutoCommit(false);
            //编写sql
            String sql = "SELECT goods_id,title,series,actual_price,creat_time,sales_volume FROM ml_goods_mx";
            //预编译sql
            pstm = conn.prepareStatement(sql);
            // 设置游标可以上下移动
//            pstm = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            //开始总计时
            long bTime1 = System.currentTimeMillis();

            //循环10次，每次十万数据，一共1000万
//            for(int i=0;i<10;i++) {
//
//                //开启分段计时，计1W数据耗时
//                long bTime = System.currentTimeMillis();
//                //开始循环
//                while (begin < end) {
//                    //赋值
//                    pstm.setLong(1, begin);
//                    pstm.setString(2, RandomValue.getChineseName());
//                    pstm.setString(3, RandomValue.name_sex);
//                    pstm.setInt(4, RandomValue.getNum(1, 100));
//                    pstm.setString(5, RandomValue.getEmail(4, 15));
//                    pstm.setString(6, RandomValue.getTel());
//                    pstm.setString(7, RandomValue.getRoad());
//                    //添加到同一个批处理中
//                    pstm.addBatch();
//                    begin++;
//                }
//                //执行批处理
               pstm.executeQuery();
////                //提交事务
                conn.commit();
//                //边界值自增10W
//                end += 100000;
//                //关闭分段计时
//                long eTime = System.currentTimeMillis();
//                //输出
//                System.out.println("成功插入10W条数据耗时："+(eTime-bTime));
//            }
            //关闭总计时
            long eTime1 = System.currentTimeMillis();
            //输出
            System.out.println("共耗时："+(eTime1-bTime1));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
    }


    /**
     * JDBC批处理（开启事务、无事务）
     */
    @Test
    public void searchBigDataPipeline() {
        //定义连接、statement对象
        Connection conn = null;
        try {
            //加载jdbc驱动
            Class.forName("com.mysql.jdbc.Driver");
            //连接mysql
            conn = DriverManager.getConnection(url, user, password);
            //将自动提交关闭
            conn.setAutoCommit(false);
            //编写sql
            String sql = "SELECT goods_id,title,series,actual_price,creat_time,sales_volume FROM ml_goods_mx";
            //开始总计时
            long bTime1 = System.currentTimeMillis();

//            Iterator<Map<String, Object>> iterator = new ResultSetIterator(conn, sql);
////                //提交事务
            conn.commit();
            //关闭总计时
            long eTime1 = System.currentTimeMillis();
            //输出
            System.out.println("共耗时："+(eTime1-bTime1));
//            int i = 0;
//            while (iterator.hasNext()) {
////            System.err.println(iterator.next());
//                i++;
//            }
//            System.err.println("总共+" + i);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
    }

    @Test
    public void searchBigDataPipeline2() {
        //定义连接、statement对象
//        Connection conn = null;
//        try {
//            //加载jdbc驱动
//            Class.forName("com.mysql.jdbc.Driver");
//            //连接mysql
//            conn = DriverManager.getConnection(url, user, password);
//            //将自动提交关闭
//            conn.setAutoCommit(false);
//            //编写sql
//            String sql = "SELECT goods_id,title,series,actual_price,creat_time,sales_volume FROM ml_goods_mx";
//            //开始总计时
//            long bTime1 = System.currentTimeMillis();
//
//            Iterator<Map<String, Object>> iterator = new ResultSetIterator(conn, sql,10000);
//            conn.commit();
//            //输出
//            int i = 0;
//            while (iterator.hasNext()) {
////            System.err.println(iterator.next());
//                i++;
//            }
//            System.err.println("总共+" + i);
//            //关闭总计时
//            long eTime1 = System.currentTimeMillis();
//            System.out.println("共耗时："+(eTime1-bTime1));
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e1) {
//            e1.printStackTrace();
//        }
    }



}
