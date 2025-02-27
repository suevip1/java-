package top.arhi.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
// 启用EurekaServer
@EnableEurekaServer
@RefreshScope
public class Eureka1App {

    public static void main(String[] args) {
        SpringApplication.run(Eureka1App.class,args);
    }
}
