package cn.czy15.webshirodemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.czy15.webshirodemo.mapper")
public class WebshirodemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebshirodemoApplication.class, args);
    }

}
