package com.jdbctemplate;

import com.jum.SpringbootdemoApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringbootdemoApplication.class)
public class SpringJdbcTempTest {

//    @Autowired
//    private CommonDao commonDao;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Test
    public void test01(){

//        Connection conn = null;
//        PreparedStatement  st = null;
//        int result = 0;
//        try {
//            //开始总计时
//            long bTime1 = System.currentTimeMillis();
//            SqlParaBuffer sql = new SqlParaBuffer();
//            sql.append("SELECT * FROM ml_goods_mx");
//            conn = jdbcTemplate.getDataSource().getConnection();
//            //
//            st = conn.prepareStatement(sql.getSql());
////            st = conn.prepareStatement(sql.getSql(),ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
//            st.setFetchSize(10000);
//            conn.setAutoCommit(false);
//            ResultSet goodsList  = st.executeQuery();
//            conn.commit();
//            //关闭总计时
//            long eTime1 = System.currentTimeMillis();
//            //输出
//            System.out.println("共耗时："+(eTime1-bTime1));
//        } catch (Exception e) {
//            if(conn!=null){
//                try {
//                    conn.rollback();
//                } catch (SQLException e1) {
//                    e1.printStackTrace();
//                }
//            }
//            e.printStackTrace();
//        }finally{
//            try {
//                if(null!=st){
//                    st.close();
//                }
//                if(null!=conn){
//                    conn.close();
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }

    }

    @Test
    public void test02() throws SQLException {

        //开始总计时
//        long bTime1 = System.currentTimeMillis();
//        SqlParaBuffer sql = new SqlParaBuffer();
//        sql.append("SELECT goods_id,title,series,actual_price,creat_time,sales_volume FROM ml_goods_mx");
//        System.out.println(jdbcTemplate.getFetchSize());
//        System.out.println(jdbcTemplate.getMaxRows());
//        System.out.println(jdbcTemplate.getQueryTimeout());
//        jdbcTemplate.setFetchSize(10000);
//        jdbcTemplate.setMaxRows(50000000);
//        System.out.println(jdbcTemplate.getFetchSize());
////        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.getSql());
//        Iterator<Map<String, Object>> iterator = new ResultSetIterator(jdbcTemplate.getDataSource().getConnection(), sql.getSql(),10000);
//        //关闭总计时
//        long eTime1 = System.currentTimeMillis();
//        //输出
//        System.out.println("共耗时："+(eTime1-bTime1));
//        int i = 0;
//        while (iterator.hasNext()) {
////            System.err.println(iterator.next());
//            i++;
//        }
//        System.err.println("总共+" + i);
    }


    // 支持查询
//    public Iterator<Map<String, Object>> getResultSetIterator(String querySql) {
//        try {
//            // 返回迭代器 // 初始化 Statement // 执行查询
//            return new ResultSetIterator(getConnection(), querySql);
//        } catch (SQLException e) {
//            return null;
//        }
//    }

}

