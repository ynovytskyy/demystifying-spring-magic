package org.yny.demystify.spring.a_core.d;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.support.GenericApplicationContext;

//=== Dependency Injection - Using Spring core =======================================================================
//=== Application Context that takes care of instantiation and dependency injection ==================================
public class DependencyInjectionExample {
    public static void main(String[] args) {
        GenericApplicationContext appContext = new GenericApplicationContext();
        appContext.registerBeanDefinition("serviceBean",
                new RootBeanDefinition(ServiceImpl.class, AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR, false));
        appContext.registerBeanDefinition("clientBean",
                new RootBeanDefinition(Client.class, AutowireCapableBeanFactory.AUTOWIRE_CONSTRUCTOR, false));
        appContext.refresh();

        // can resolve service by ServiceInterface even though the implementation was registered =====================
        ServiceInterface service = appContext.getBean(ServiceInterface.class);
        int worth = service.calculateWorth("direct");
        System.out.println("Direct service call on service for [direct] is [" + worth + "]");

        // get Client with dependencies autowired by Spring ==========================================================
        Client client = appContext.getBean(Client.class);
        client.process();
    }
}

//=== code for the client and the definition of the service interface ===============================================
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

//=== code that implements the service interface =====================================================================
class ServiceImpl implements ServiceInterface {
    @Override
    public int calculateWorth(String s) {
        return s.length();
    }
}
