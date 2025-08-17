package com.glamora_store;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
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
