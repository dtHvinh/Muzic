package SummerBootTests.Data;

import com.dthvinh.Server.Lib.SummerBoot.Data.AppDataSource;
import com.dthvinh.Server.Lib.SummerBoot.Data.DatabaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DatabaseServiceTest {

    private DatabaseService databaseService;
    private DataSource dataSource;

    @BeforeEach
    void setup() {
        dataSource = new AppDataSource(
                "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
                "sa",
                ""
        );
        databaseService = new DatabaseService(dataSource);
    }

    @Test
    void initDb_shouldCreateAllTables() throws Exception {
        databaseService.initDb();

        try (Connection connection = dataSource.getConnection()) {

            assertTableExists(connection, "ARTIST");
            assertTableExists(connection, "SONG");
            assertTableExists(connection, "PLAYLIST");
            assertTableExists(connection, "PLAYLIST_SONG");
        }
    }

    private void assertTableExists(Connection connection, String tableName) throws Exception {
        try (ResultSet rs = connection.getMetaData().getTables(
                null,
                null,
                tableName,
                null
        )) {
            assertTrue(rs.next(), "Table " + tableName + " should exist");
        }
    }
}
