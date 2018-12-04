package org.yny.demystify.spring.a_core.e;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

//=== Using Application Context that scans classpath and discovers beans using annotations ==========================
public class AnnotationBasedBeanDiscoveryExample {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
        String packageName = ClassUtils.getPackageName(AnnotationBasedBeanDiscoveryExample.class);
        appContext.scan(packageName);
        appContext.refresh();

        Client client = appContext.getBean(Client.class);
        client.process();
    }
}

//=== Client annotated with @Component ===============================================================================
//=== It needs ServiceInterface to be instantiated, so it gets ServiceImpl injected in the runtime ===================
//=== Can be injected itself =========================================================================================
@Component
class Client {
    private ServiceInterface service;

    public Client(ServiceInterface service) {
        this.service = service;
    }

    public void process() {
        String input = "test";
        int worth = service.calculateWorth(input);
        System.out.println("Worth of [" + input + "] is [" + worth + "]");
    }
}

interface ServiceInterface {
    public int calculateWorth(String s);
}

//=== ServiceImpl annotated with @Component therefore registered as bean =============================================
//=== Gets injected as dependency where Client needs ServiceInterface; can get dependencies injected as well =========
@Component
class ServiceImpl implements ServiceInterface {
    @Override
    public int calculateWorth(String s) {
        return s.length();
    }
}
