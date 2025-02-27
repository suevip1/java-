package top.arhi;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DruidDataSourceAutoConfigure.class})
public class SpringbootShardingjdbcMybatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootShardingjdbcMybatisApplication.class, args);
    }

}
