package br.com.market;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication(scanBasePackages = "br.com.market")
public class MarketMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketMsApplication.class, args);
	}

}
