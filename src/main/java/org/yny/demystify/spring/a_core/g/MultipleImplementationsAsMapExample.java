package org.yny.demystify.spring.a_core.g;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.util.Map;

public class MultipleImplementationsAsMapExample {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
        String packageName = ClassUtils.getPackageName(MultipleImplementationsAsMapExample.class);
        appContext.scan(packageName);
        appContext.refresh();

        Client client = appContext.getBean(Client.class);
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

