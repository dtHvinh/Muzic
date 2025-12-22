package SummerBootTests.Types;

import com.dthvinh.Server.Types.ValidationResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationResultTest {

    @Test
    void testSuccess() {
        ValidationResult result = ValidationResult.success();
        assertTrue(result.success);
        assertNull(result.errorMessage);
    }

    @Test
    void testError() {
        String msg = "Something went wrong";
        ValidationResult result = ValidationResult.error(msg);
        assertFalse(result.success);
        assertEquals(msg, result.errorMessage);
    }
}

