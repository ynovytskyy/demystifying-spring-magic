package org.yny.demystify.spring.a_core.c;

//=== Inversion of Control - Client does not control which which implementation of ServiceInterface it uses =========
public class InversionOfControlExample {
    public static void main(String[] args) {
        ServiceInterface service = new ServiceImpl();
        Client client = new Client(service);
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

//=== code that implements the service interface ====================================================================
class ServiceImpl implements ServiceInterface {
    @Override
    public int calculateWorth(String s) {
        return s.length();
    }
}
