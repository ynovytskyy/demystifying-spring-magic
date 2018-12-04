package org.yny.demystify.spring.b_aop.a;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Map;

//================= In this "Top Down" example Spring's magical handling of ==========================================
//=== By adding @EnableCaching annotation on one of the Configuration classes ========================================
//=== and @Cacheable on a bean's method one could magically
//============================ How the hell does it do it?! ==========================================================
//============== The answer is Aspect Oriented Programming using Dynamic Proxies =====================================
@SpringBootApplication
@EnableCaching //add dependency as well
public class SpringCachingExample {
    public static void main(String[] args) {
        SpringApplication.run(SpringCachingExample.class, args);
    }

    @Bean
    public CommandLineRunner appRunner(@Autowired Client client) {
        return args -> {
            System.out.println("Client is querying Worth Estimators...");
            client.process();
            System.out.println("Client is re-querying Worth Estimators...");
            client.process();
        };
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
            String worth = service.getValue().calculateWorth(input);
//            String worth = service.getValue().doCalculateWorth(input);
            System.out.println("Worth of [" + input + "] according to [" + service.getKey() +
                    "] is [" + worth + "]");
        }
    }
}

interface WorthEstimatorService {
    public String calculateWorth(String s);
    public String doCalculateWorth(String s);
}

// Code that can be written later =========================

@Component
class LengthBasedWorthEstimator implements WorthEstimatorService {
    @Override
    public String calculateWorth(String s) {
        return doCalculateWorth(s);
    }

    @Override
    @Cacheable("LengthBasedWorthCache")
    public String doCalculateWorth(String s) {
        //=== step 2 (after step 1 below) put a breakpoint here and show a call stack in debug mode
        System.out.println("LengthBasedWorthEstimator is estimating...");
        return Integer.toString(s.length());
    }
}

@Component
class LengthSquaredBasedWorthEstimator implements WorthEstimatorService {
    @Override
    public String calculateWorth(String s) {
        return doCalculateWorth(s);
    }

    @Override
    public String doCalculateWorth(String s) {
        //=== step 2 (after step 1 below) put a breakpoint here and show a call stack in debug mode
        System.out.println("LengthSquaredBasedWorthEstimator is estimating...");
        return Integer.toString(s.length() * s.length());
    }
}


