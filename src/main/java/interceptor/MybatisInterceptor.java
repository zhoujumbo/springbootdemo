package interceptor;


import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.Connection;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;


@Intercepts({
		@Signature(type = Executor.class, method = "update", args = {
				MappedStatement.class, Object.class }),
		@Signature(type = Executor.class, method = "query", args = {
				MappedStatement.class, Object.class, RowBounds.class,
				ResultHandler.class }) })
@Component
public class MybatisInterceptor implements Interceptor {
	private static final Logger logger = LoggerFactory
			.getLogger(MybatisInterceptor.class);
	private Properties properties;
	 @Resource
	 private SqlSession  sqlSession;
	 
	public Object intercept(Invocation invocation) throws Throwable {
		MappedStatement mappedStatement = (MappedStatement) invocation
				.getArgs()[0];
		Object parameter = null;
		if (invocation.getArgs().length > 1) {
			parameter = invocation.getArgs()[1];
		}
		String sqlId = mappedStatement.getId();
		BoundSql boundSql = mappedStatement.getBoundSql(parameter);
		Configuration configuration = mappedStatement.getConfiguration();
		Object returnValue = null;
		long start = System.currentTimeMillis();
		returnValue = invocation.proceed();
		long end = System.currentTimeMillis();
		long time = (end - start);
//		if(time > 1){
			String sql = getSql(configuration, boundSql, sqlId, time);
//			logger.info("调用的java方法为：\n"+sql.split(":")[0]);
//			logger.info("执行的sql语句是：\n"+sql.split(":")[1]);
			logger.info("执行sql>>>：\n"+sql.split(":")[0] +"##"+sql.split(":")[1]);
//			System.out.println("执行sql>>>：\n"+sql.split(":")[0] +"##"+sql.split(":")[1]);
//			String s = sql.split(":")[0];
//			String key = s.substring(s.substring(0, s.lastIndexOf(".")).lastIndexOf(".")+1, s.length());			
//			String sqlStr = sql.split(":")[1];
//			System.out.println();
//			System.out.println("##########"+key+"\n"+sqlStr);
			
			
//			Connection conn = getConnection(); 
//			DatabaseMetaData dbMetaData = null;
//			try {
//				dbMetaData = conn.getMetaData();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			System.out.println(">>>URL:"+dbMetaData.getURL()+";");
//			System.out.println(">>>UserName:"+dbMetaData.getUserName()+";");
//			System.out.println(">>>DatabaseProductName:"+dbMetaData.getDatabaseProductName()+";");
//			System.out.println(">>>DatabaseProductVersion:"+dbMetaData.getDatabaseProductVersion()+";");
//			System.out.println(">>>DriverName:"+dbMetaData.getDriverName()+";");
//			System.out.println(">>>DriverVersion:"+dbMetaData.getDriverVersion());
//		}
		return returnValue;
	}

	public Connection getConnection(){    
        Connection conn = null;    
        try {    
            conn =  sqlSession.getConfiguration().getEnvironment().getDataSource().getConnection();    
            logger.info("===This Connection isClosed ? "+conn.isClosed());    
        } catch (Exception e) {    
            e.printStackTrace();    
        }    
        return conn;    
    }   

	public static String getSql(Configuration configuration, BoundSql boundSql,
			String sqlId, long time) {
		String sql = showSql(configuration, boundSql);
		StringBuilder str = new StringBuilder(100);
		str.append(sqlId);
		str.append(":");
		str.append(sql);
		return str.toString();
	}

	private static String getParameterValue(Object obj) {
		String value = null;
		if (obj instanceof String) {
			value = "'" + obj.toString() + "'";
		} else if (obj instanceof Date) {
			DateFormat formatter = DateFormat.getDateTimeInstance(
					DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
			value = "'" + formatter.format(new Date()) + "'";
		} else {
			if (obj != null) {
				value = obj.toString();
			} else {
				value = "";
			}
		}
		return value.replace("$", "\\$");
	}

	public static String showSql(Configuration configuration, BoundSql boundSql) {
		Object parameterObject = boundSql.getParameterObject();
		List<ParameterMapping> parameterMappings = boundSql
				.getParameterMappings();
		String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
		if (parameterMappings.size() > 0 && parameterObject != null) {
			TypeHandlerRegistry typeHandlerRegistry = configuration
					.getTypeHandlerRegistry();
			if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
				sql = sql.replaceFirst("\\?",
						getParameterValue(parameterObject));
			} else {
				MetaObject metaObject = configuration
						.newMetaObject(parameterObject);
				for (ParameterMapping parameterMapping : parameterMappings) {
					String propertyName = parameterMapping.getProperty();
					if (metaObject.hasGetter(propertyName)) {
						Object obj = metaObject.getValue(propertyName);
						sql = sql.replaceFirst("\\?", getParameterValue(obj));
					} else if (boundSql.hasAdditionalParameter(propertyName)) {
						Object obj = boundSql
								.getAdditionalParameter(propertyName);
						sql = sql.replaceFirst("\\?", getParameterValue(obj));
					}
				}
			}
		}
		return sql;
	}

	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties properties0) {
		this.properties = properties0;
	}
}
