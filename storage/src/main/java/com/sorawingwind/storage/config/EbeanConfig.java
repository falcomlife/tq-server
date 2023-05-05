package com.sorawingwind.storage.config;

import com.sorawingwind.storage.util.UUIDGenerator;
import io.ebean.EbeanServer;
import io.ebean.EbeanServerFactory;
import io.ebean.config.AutoTuneConfig;
import io.ebean.config.ServerConfig;
import io.ebean.config.UnderscoreNamingConvention;
import io.ebean.spring.txn.SpringJdbcTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author
 * @date 2020/09/28
 */
@Configuration
public class EbeanConfig {

    @Value("${ebean.search.packages}")
    private String packages;

    @Autowired
    private DruidConfig druidConfig;

    @Autowired
    private UUIDGenerator uuidGenerator;

    /**
     * 默认的EbeanServer
     *
     * @return
     */
    @Bean
    public EbeanServer ebeanServer() {
        ServerConfig config = new ServerConfig();

        config.setName("default");
        config.addPackage(packages);

        // load configuration from ebean.properties
        config.setDataSource(druidConfig.defaultDataSource());
        config.setAutoTuneConfig(new AutoTuneConfig());
        config.setExternalTransactionManager(new SpringJdbcTransactionManager());
        config.setNamingConvention(new UnderscoreNamingConvention());
        config.setDefaultServer(true);

        // 注册自定义的主键生成器
        config.add(uuidGenerator);

        // other programmatic configuration
        return EbeanServerFactory.create(config);

    }
}