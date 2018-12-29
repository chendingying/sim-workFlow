//package com.liansen.server.database;//package com.hgsms.database;
//
//import com.alibaba.druid.pool.DruidDataSource;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//
//import javax.sql.DataSource;
//
///**
// * Created by CDZ on 2018/12/8.
// */
//@Configuration
////扫描 Mapper 接口并容器管理
//@MapperScan(basePackages = ClusterDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "clusterSqlSessionFactory")
//public class ClusterDataSourceConfig {
//    // 精确到 cluster 目录，以便跟其他数据源隔离
//    static final String PACKAGE = "com.glory.process.mapper.admin,com.glory.process.mapper.view";
//    static final String MAPPER_LOCATION = "classpath:mapper/**/*.xml";
//
//    @Value("${datasource_2.url}")
//    private String url;
//
//    @Value("${datasource_2.username}")
//    private String user;
//
//    @Value("${datasource_2.password}")
//    private String password;
//
//    @Value("${datasource_2.driver-class-name}")
//    private String driverClass;
//
//    @Bean(name = "clusterDataSource")
//    public DataSource clusterDataSource() {
//        DruidDataSource dataSource = new DruidDataSource();
//        dataSource.setDriverClassName(driverClass);
//        dataSource.setUrl(url);
//        dataSource.setUsername(user);
//        dataSource.setPassword(password);
//        return dataSource;
//    }
//
//    @Bean(name = "clusterTransactionManager")
//    public DataSourceTransactionManager clusterTransactionManager() {
//        return new DataSourceTransactionManager(clusterDataSource());
//    }
//
//    @Bean(name = "clusterSqlSessionFactory")
//    public SqlSessionFactory clusterSqlSessionFactory(@Qualifier("clusterDataSource") DataSource clusterDataSource)
//            throws Exception {
//        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
//        sessionFactory.setDataSource(clusterDataSource);
//        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
//                .getResources(ClusterDataSourceConfig.MAPPER_LOCATION));
//        return sessionFactory.getObject();
//    }
//}
