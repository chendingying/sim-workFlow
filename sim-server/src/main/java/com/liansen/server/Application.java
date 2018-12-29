package com.liansen.server;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 系统启动
 * @Author: cdy
 * @Date: 2018/12/28 15:52
 * @Version 1.0
 */

@SpringBootApplication
//重点
@ServletComponentScan
@ComponentScan(basePackages = "com.liansen")
@MapperScan(basePackages = "com.liansen.interfaces.mapper")
public class Application  extends WebMvcConfigurerAdapter {
    private static Logger logger = LoggerFactory.getLogger(Application.class);
    /**
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        logger.debug("启动成功");
    }

    /**
     * 跨域问题
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").
                allowedMethods("DELETE", "POST", "GET", "PUT")
                .allowedOrigins("*");
    }

    /**
     * 加载字符过滤器
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean characterFilterRegistration() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setForceEncoding(true);
        characterEncodingFilter.setEncoding("utf-8");
        return new FilterRegistrationBean(characterEncodingFilter);
    }
}
