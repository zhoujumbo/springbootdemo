package com.jum.datasource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration  // 注册到springboot容器中
@MapperScan(basePackages = "com.jum.db2.dao", sqlSessionFactoryRef = "db2SqlSessionFactory")
public class DataSourceConfigDb2 {
    /**
     * 配置test1数据库
     * @return
     */
    @Bean(name = "db2DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.db2")
    public DataSource testDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * sql会话工厂
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean(name = "db2SqlSessionFactory")
    public SqlSessionFactory testSqlSessionFactory(@Qualifier("db2DataSource") DataSource dataSource)
            throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        // mybatis 写配置文件
		bean.setMapperLocations(
				new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/db2/*.xml"));
        return bean.getObject();
    }

    /**
     * 事物管理
     * @param dataSource
     * @return
     */
    @Bean(name = "db2TransactionManager")
    public DataSourceTransactionManager testTransactionManager(@Qualifier("db2DataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     *
     * @param sqlSessionFactory
     * @return
     * @throws Exception
     */
    @Bean(name = "db2SqlSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate(
            @Qualifier("db2SqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
