package cn.gxkj.system;

import cn.gxkj.IdWorker;
import cn.gxkj.utils.JwtUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--02--12 17:18:00
 */
@SpringBootApplication(scanBasePackages = "cn.gxkj")
@EntityScan("cn.gxkj.model")
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
public class SystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class, args);
    }
    @Bean
    public IdWorker idWorkker() {
        return new IdWorker(1, 1);
    }

    @Bean
    public JwtUtil jwtUtil(){
        return new JwtUtil();
    }

    //解决no session问题
    @Bean
    public OpenEntityManagerInViewFilter openEntityManagerInViewFilter(){
        return new OpenEntityManagerInViewFilter();
    }
}
