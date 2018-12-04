package org.yny.demystify.spring.a_core.b;

public class DependencyInversionExample {
    public static void main(String[] args) {
        Client client = new Client();
        client.process();
    }
}

// ==== code that's using the service - written after the service interface is defined ================

class Client {
    // process() is implementation agnostic
    public void process() {
        String input = "test";
        ServiceInterface service = createService();
        int worth = service.calculateWorth(input);
        System.out.println("Worth of [" + input + "] is [" + worth + "]");
    }

    // still a problem because of instantiation - client needs to knows about implementation
    // can be addressed by Factories, Resolvers ...or _dependency_injection_
    private ServiceInterface createService() {
        return new ServiceImpl();
    }
}

// ==== code that defines the service interface ====================================================
// 1. if declared in the Jar with ServiceImpl, it's a "library" approach, where "client" code is using the library
//    after it's ready; but allows for multiple implementations if client code knows how to instantiate them
// 2. if declared in the Jar with Client, it's a "framework" approach; the interface is extracted from the Service
//    and the Jar with the ServiceImpl now depends on the Jar with Client and ServiceInterface, therefore it is
//    _dependency_inversion_; more sophisticated ServiceImpl(s) instantiation strategies are required though
interface ServiceInterface {
    public int calculateWorth(String s);
}

// ==== code that implements the service interface - written after the interface is defined ================

class ServiceImpl implements ServiceInterface {
    @Override
    public int calculateWorth(String s) {
        return s.length();
    }
}
