package com.salaboy.springbootonk8s;

import io.dekorate.kubernetes.annotation.KubernetesApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
@KubernetesApplication
public class SpringBootOnK8sApplication implements CommandLineRunner {
    private Logger logger = LoggerFactory.getLogger(SpringBootOnK8sApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringBootOnK8sApplication.class, args);
    }

    @Value("${myConfigurationValue:from code}")
    private String myConfigurationValue;


    @Override
    public void run(String... args) throws Exception {
        logger.info("> Application Started!");
        logger.info("\t> Hello from: " + myConfigurationValue);
    }

    @GetMapping("/hello")
    public String sayHelloBasedOnConfig() {
        return "Hello from: " + myConfigurationValue;
    }
}


