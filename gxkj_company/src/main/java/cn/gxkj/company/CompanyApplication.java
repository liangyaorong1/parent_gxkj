package cn.gxkj.company;

import cn.gxkj.IdWorker;
import cn.gxkj.utils.JwtUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--01--28 13:43:00
 */
@SpringBootApplication(scanBasePackages = "cn.gxkj")
@EntityScan("cn.gxkj.model")
@EnableEurekaClient
public class CompanyApplication {
    public static void main(String[] args) {
        SpringApplication.run(CompanyApplication.class, args);
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
