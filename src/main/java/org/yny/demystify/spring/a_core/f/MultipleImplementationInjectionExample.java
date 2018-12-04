package org.yny.demystify.spring.a_core.f;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.util.List;

public class MultipleImplementationInjectionExample {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
        String packageName = ClassUtils.getPackageName(MultipleImplementationInjectionExample.class);
        appContext.scan(packageName);
        appContext.refresh();

        Client client = appContext.getBean(Client.class);
        client.process();
    }
}

//=== Code fully decoupled from any WorthEstimatorService implementation =============================================
@Component
class Client {
    private List<WorthEstimatorService> services;

    public Client(List<WorthEstimatorService> services) {
        this.services = services;
    }

    public void process() {
        String input = "test";
        for (WorthEstimatorService service : services) {
            int worth = service.calculateWorth(input);
            System.out.println("Worth of [" + input + "] is [" + worth + "]");
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

