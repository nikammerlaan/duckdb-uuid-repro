import org.duckdb.DuckDBConnection;
import org.duckdb.DuckDBResultSet;
import org.junit.jupiter.api.Test;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DuckDbTest {

    @Test
    public void testUuidParams() throws SQLException{
        var uuid = UUID.fromString("a0a34a0a-1794-47b6-b45c-0ac68cc03702");

        try(var conn = DriverManager.getConnection("jdbc:duckdb:").unwrap(DuckDBConnection.class)) {
            var sql = "SELECT ?";

            // Passing in the UUID as a string works; this test passes
            try (var stmt = conn.prepareStatement(sql)) {
                stmt.setObject(1, uuid.toString());

                try(var rs = stmt.executeQuery().unwrap(DuckDBResultSet.class)) {
                    while(rs.next()) {
                        assertEquals(uuid, rs.getUuid(1));
                    }
                }
            }

            // Passing in the UUID as a UUID breaks; this test fails
            try (var stmt = conn.prepareStatement(sql)) {
                stmt.setObject(1, uuid);

                try(var rs = stmt.executeQuery().unwrap(DuckDBResultSet.class)) {
                    while(rs.next()) {
                        assertEquals(uuid, rs.getUuid(1));
                    }
                }
            }
        }
    }

}
