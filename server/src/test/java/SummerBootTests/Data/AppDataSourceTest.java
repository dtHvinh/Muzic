package SummerBootTests.Data;

import com.dthvinh.Server.SummerBoot.Data.AppDataSource;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AppDataSourceTest {

    @Test
    void getConnection_shouldReturnOpenConnection() throws Exception {
        DataSource dataSource = new AppDataSource(
                "jdbc:h2:mem:testDb;DB_CLOSE_DELAY=-1",
                "sa",
                ""
        );

        try (Connection connection = dataSource.getConnection()) {
            assertNotNull(connection);
            assertFalse(connection.isClosed());
        }
    }
}

