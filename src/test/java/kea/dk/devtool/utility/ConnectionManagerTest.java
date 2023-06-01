package kea.dk.devtool.utility;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionManagerTest {
//   @Test

    // Tests er udkommenteret, da denne ville fejle ved kørsel af applikation, hvis ikke man har en DB liggende med
    // samme test data som dette.
    // Kommentar kan fjernes hvis tests vil afprøves.
    public void testGetConnection() {

        // Test data
        String DB_URL= "jdbc:mysql://localhost:3306/testdb";
        String USER_IDE="testuser";
        String PASSW="testpw";
        // Perform the test
        Connection connection = ConnectionManager.getConnection(DB_URL, USER_IDE, PASSW);

        // Assert if the connection is not null
        assertNotNull(connection, "Connection should not be null");
        try {
            /*  assertFalse forventer at condition er false.
                connection.isClosed tjekker om forbindelsen er lukket.
                ergo vil assertFalse pass, hvis forbindelsen ikke er lukket.
            */
            assertFalse(connection.isClosed(), "Connection is open");
        } catch (SQLException e) {
            fail("Exception: " + e.getMessage());
        }
    }
}