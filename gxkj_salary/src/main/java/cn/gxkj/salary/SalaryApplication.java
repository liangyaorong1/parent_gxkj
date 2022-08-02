package cn.gxkj.salary;

import cn.gxkj.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--07--30 15:48:00
 */
@SpringBootApplication(scanBasePackages = "cn.gxkj")
@EntityScan("cn.gxkj.model")
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
public class SalaryApplication {
    public static void main(String[] args) {
        SpringApplication.run(SalaryApplication.class,args);
    }

    @Bean
    public IdWorker idWorker(){
        return new IdWorker();
    }
}
