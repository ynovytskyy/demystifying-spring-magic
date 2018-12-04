package org.yny.demystify.spring.b_aop.b;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;


@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@interface LogExecutionTime { }


@SpringBootApplication
@EnableCaching // <--turns on aspect proxies creation
public class SpringAopExample {
    public static void main(String[] args) {
        SpringApplication.run(SpringAopExample.class, args);
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

    class TimingInterceptor implements MethodInterceptor {
        public Object invoke(MethodInvocation invocation) throws Throwable {
            long start = System.currentTimeMillis();
            Object proceed = invocation.proceed();
            long executionTime = System.currentTimeMillis() - start;
            System.out.println("------ " + invocation.getMethod().getName() + " executed in " + executionTime + "ms");
            return proceed;
        }
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public DefaultBeanFactoryPointcutAdvisor advisor() {
        DefaultBeanFactoryPointcutAdvisor advisor = new DefaultBeanFactoryPointcutAdvisor();
        advisor.setPointcut(new AnnotationMatchingPointcut(null, LogExecutionTime.class));
        advisor.setAdvice(new TimingInterceptor());
        return advisor;
    }

}

// Code fully decoupled from any WorthEstimatorService implementations ====================================

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
            System.out.println("Worth of [" + input + "] according to [" + service.getKey() +
                    "] is [" + worth + "]");
        }
    }
}

interface WorthEstimatorService {
    public String calculateWorth(String s);
}

// Code that can be written later =========================

@Component
class LengthBasedWorthEstimator implements WorthEstimatorService {
    @Override
    @LogExecutionTime
    public String calculateWorth(String s) {
        System.out.println("LengthBasedWorthEstimator is estimating...");
        try {
            Thread.sleep((long) (200 + 1000 * Math.random()));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Integer.toString(s.length());
    }
}

@Component
class LengthSquaredBasedWorthEstimator implements WorthEstimatorService {
    @Override
    @LogExecutionTime
    public String calculateWorth(String s) {
        System.out.println("LengthSquaredBasedWorthEstimator is estimating...");
        return Integer.toString(s.length() * s.length());
    }
}

