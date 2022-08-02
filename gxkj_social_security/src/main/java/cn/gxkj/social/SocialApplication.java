package cn.gxkj.social;

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
 * @CREATE:2022--07--23 18:56:00
 */
@SpringBootApplication(scanBasePackages = "cn.gxkj")
@EntityScan(value = "cn.gxkj.model")
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
public class SocialApplication {
    public static void main(String[] args) {
        SpringApplication.run(SocialApplication.class,args);
    }

    @Bean
    public IdWorker idWorker(){
        return  new IdWorker();
    }


}
