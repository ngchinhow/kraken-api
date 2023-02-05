package org.trading.krakenapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class KrakenApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(KrakenApiApplication.class, args);
	}

}
