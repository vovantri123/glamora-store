package com.glamora_store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@SpringBootApplication
public class GlamoraStoreApplication {

  public static void main(String[] args) {
    SpringApplication.run(GlamoraStoreApplication.class, args);
  }
}
