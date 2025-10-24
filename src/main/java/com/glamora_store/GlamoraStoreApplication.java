package com.glamora_store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GlamoraStoreApplication {

  public static void main(String[] args) {
    SpringApplication.run(GlamoraStoreApplication.class, args);
  }
}
