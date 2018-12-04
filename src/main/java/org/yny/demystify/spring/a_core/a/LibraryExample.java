package org.yny.demystify.spring.a_core.a;

//=== This is a "Bottom Up" evolutionary example to demystify why and how Dependency Inversion, =====================
//=== Inversion Of Control and Dependency Injection are useful facilitated by Spring ================================
public class LibraryExample {
    public static void main(String[] args) {
        Client client = new Client();
        client.process();
    }
}

// ==== Client using a library - has to be written after the library =================================================
class Client {
    public void process() {
        String input = "test";
        Service service = new Service();
        int worth = service.calculateWorth(input);
        System.out.println("Worth of [" + input + "] is [" + worth + "]");
    }
}

// ==== Service is a pre-existing library used by the Client =========================================================
class Service {
    public int calculateWorth(String s) {
        return s.length();
    }
}
