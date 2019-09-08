package com.salaboy.springbootonk8s;

import io.dekorate.kubernetes.annotation.KubernetesApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
@KubernetesApplication
@EnableConfigurationProperties(value = MyApplicationProperties.class)
public class SpringBootOnK8sApplication implements CommandLineRunner {
    private Logger logger = LoggerFactory.getLogger(SpringBootOnK8sApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringBootOnK8sApplication.class, args);
    }

    @Value("${myConfigurationValue:from code}")
    private String myConfigurationValue;

    @Autowired
    private MyApplicationProperties k8sProperties;

    @Autowired
    private CustomBean customBeanPerEnvironment;

    @Override
    public void run(String... args) throws Exception {
        logger.info("> Application Started!");
        logger.info("\t> Hello from: " + myConfigurationValue);
        logger.info("\t> @ConfigurationProperties - k8s String: " + k8sProperties.getMyK8sStringValue());
        logger.info("\t> @ConfigurationProperties - k8s Boolean: " + k8sProperties.isMyK8sBooleanValue());
        logger.info("\t> CustomBean" + customBeanPerEnvironment);
    }

    @GetMapping("/hello")
    public String sayHelloBasedOnConfig() {
        return "Hello from: " + myConfigurationValue;
    }
}

@ConfigurationProperties(prefix = "k8s")
class MyApplicationProperties {
    private String myK8sStringValue;
    private boolean myK8sBooleanValue;

    public String getMyK8sStringValue() {
        return myK8sStringValue;
    }

    public void setMyK8sStringValue(String myK8sStringValue) {
        this.myK8sStringValue = myK8sStringValue;
    }

    public boolean isMyK8sBooleanValue() {
        return myK8sBooleanValue;
    }

    public void setMyK8sBooleanValue(boolean myK8sBooleanValue) {
        this.myK8sBooleanValue = myK8sBooleanValue;
    }
}


@Profile("!kubernetes")
@Configuration
class DevAutoConfiguration {
    @Bean
    public CustomBean getCustomBean() {
        return new CustomBean("DEV");
    }
}


@Profile("kubernetes")
@Configuration
class KubernetesOnlyAutoConfiguration {
    @Bean
    public CustomBean getCustomBean() {
        return new CustomBean("Kubernetes");
    }
}

class CustomBean {

    private String createdFor;

    public CustomBean(String createdFor) {
        this.createdFor = createdFor;
    }

    public String getCreatedFor() {
        return createdFor;
    }

    public void setCreatedFor(String createdFor) {
        this.createdFor = createdFor;
    }

    @Override
    public String toString() {
        return "CustomBean{" +
                "createdFor ='" + createdFor + '\'' +
                '}';
    }
}

