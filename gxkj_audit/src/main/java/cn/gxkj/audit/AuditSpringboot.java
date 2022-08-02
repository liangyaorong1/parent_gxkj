package cn.gxkj.audit;

import cn.gxkj.IdWorker;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--07--31 13:15:00
 */
@SpringBootApplication(scanBasePackages = "cn.gxkj", exclude = {SecurityAutoConfiguration.class})
@EntityScan("cn.gxkj.model")
//3.注册到eureka
@EnableEurekaClient
@EnableDiscoveryClient
public class AuditSpringboot {
    public static void main(String[] args) {
        SpringApplication.run(AuditSpringboot.class, args);
    }

    @Bean
    public IdWorker idWorker() {
        return new IdWorker();
    }

    /**
     * 为了解决activiti对象转化json的问题
     *      通过自定义json转化器的形式完成
     *          通过使用fastjson替换默认的jackson转化json数据
     *          在转化器中过滤了identityLinks属性
     */
    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        FastJsonHttpMessageConverter fjc = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(SerializerFeature.PrettyFormat);
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        filter.getExcludes().add("identityLinks");
        config.setSerializeFilters(filter);
        fjc.setFastJsonConfig(config);
        HttpMessageConverter<?> converter = fjc;
        return new HttpMessageConverters(converter);
    }
}
