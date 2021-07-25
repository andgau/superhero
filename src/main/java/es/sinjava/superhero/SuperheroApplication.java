package es.sinjava.superhero;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import es.sinjava.superhero.audit.GatheredClient;
import lombok.extern.log4j.Log4j2;

@SpringBootApplication
@Log4j2
public class SuperheroApplication {

	public static void main(String[] args) {
		SpringApplication.run(SuperheroApplication.class, args);
	}
	
	@Bean
	public GatheredClient getAudit(@Value("${audit.url}") String urlAudit) {
		log.debug("Creado el cliente AOP contra {}", urlAudit);
		GatheredClient gc = new GatheredClient(urlAudit, GatheredClient.Strategy.GET);
		return gc;
	}

}
