package cn.gxkj.att;

import cn.gxkj.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--07--27 12:30:00
 */
@SpringBootApplication(scanBasePackages = "cn.gxkj")
@EntityScan("cn.gxkj.model")
@EnableEurekaClient
public class AttendanceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AttendanceApplication.class,args);
    }
    @Bean
    public IdWorker idWorker(){
        return new IdWorker();
    }

}
