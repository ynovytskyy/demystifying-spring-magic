package org.yny.demystify.spring.a_core.h;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

//=== Convenience of SpringBoot as the final step of this evolutionary example =======================================
@SpringBootApplication
public class SpringBootExample {
    @Autowired private Client client;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootExample.class, args);
    }

    @PostConstruct
    public void postConstruct() {
        client.process();
    }
}

//=== Code fully decoupled from any WorthEstimatorService implementation =============================================
@Component
class Client {
    private Map<String, WorthEstimatorService> services;

    public Client(Map<String, WorthEstimatorService> services) {
        this.services = services;
    }

    public void process() {
        String input = "test";
        for (Map.Entry<String, WorthEstimatorService> service : services.entrySet()) {
            int worth = service.getValue().calculateWorth(input);
            System.out.println("Worth of [" + input + "] according to [" + service.getKey() +
                    "] is [" + worth + "]");
        }
    }
}

interface WorthEstimatorService {
    public int calculateWorth(String s);
}

//=== Multiple implementations of WorthEstimatorService =============================================================
@Component
class LengthBasedWorthEstimator implements WorthEstimatorService {
    @Override
    public int calculateWorth(String s) {
        return s.length();
    }
}

@Component
class LengthSquaredBasedWorthEstimator implements WorthEstimatorService {
    @Override
    public int calculateWorth(String s) {
        return s.length() * s.length();
    }
}

