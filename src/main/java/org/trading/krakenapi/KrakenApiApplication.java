package org.trading.krakenapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class KrakenApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(KrakenApiApplication.class, args);
	}

}
