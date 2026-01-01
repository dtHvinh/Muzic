package SummerBootTests.Reflection;

import com.dthvinh.Server.Lib.SummerBoot.Reflection.ClassReflectionHelper;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

class ClassReflectionHelperTest {

    @Test
    void testSingleConstructor() {
        Constructor<?> ctor = ClassReflectionHelper.getConstructorWithMostParams(SingleConstructor.class);
        assertNotNull(ctor);
        assertEquals(0, ctor.getParameterCount());
    }

    @Test
    void testMultipleConstructors() {
        Constructor<?> ctor = ClassReflectionHelper.getConstructorWithMostParams(MultipleConstructors.class);
        assertNotNull(ctor);
        assertEquals(2, ctor.getParameterCount());
    }

    @Test
    void testNullClassThrowsException() {
        assertThrows(NullPointerException.class, () -> ClassReflectionHelper.getConstructorWithMostParams(null));
    }

    static class SingleConstructor {
        public SingleConstructor() {
        }
    }

    static class MultipleConstructors {
        public MultipleConstructors() {
        }

        public MultipleConstructors(String a) {
        }

        public MultipleConstructors(String a, int b) {
        }
    }
}
