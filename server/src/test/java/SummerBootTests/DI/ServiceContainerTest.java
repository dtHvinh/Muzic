package SummerBootTests.DI;

import com.dthvinh.Server.Lib.SummerBoot.DI.ServiceContainer;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServiceContainerTest {
    private ServiceContainer sc;

    @BeforeEach
    void setup() {
        sc = new ServiceContainer();
    }

    @Test
    void registerGetTest() {
        sc.register(Service1.class, new Service1("sv1"));
        sc.register(Service2.class, new Service2("sv2"));
        sc.register(Service3.class, new Service3("sv3"));

        Assertions.assertNotNull(sc.get(Service1.class));
        Assertions.assertNotNull(sc.get(Service2.class));
        Assertions.assertNotNull(sc.get(Service3.class));
    }

    @Test
    void injectTest_shouldBeSuccess() {
        sc.register(Service1.class, new Service1("sv1"));
        sc.register(Service2.class, new Service2("sv2"));
        sc.register(Service3.class, new Service3("sv3"));

        SC a = sc.injectThenNewInstance(SC.class);

        Assertions.assertNotNull(a);
    }

    @Test
    void injectTest_ShouldThrowException() {
        sc.register(Service1.class, new Service1("sv1"));
        sc.register(Service2.class, new Service2("sv2"));

        Assertions.assertThrows(RuntimeException.class, () -> {
            SC a = sc.injectThenNewInstance(SC.class);
        });
    }

    @AllArgsConstructor
    public static class Service1 {
        String name;
    }

    @AllArgsConstructor
    public static class Service2 {
        String name;
    }

    @AllArgsConstructor
    public static class Service3 {
        String name;
    }

    public record SC(Service1 service1, Service2 service2, Service3 service3) {
    }
}
