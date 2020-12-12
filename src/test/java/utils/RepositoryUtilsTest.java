package utils;

import domain.Person;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class RepositoryUtilsTest {
    static String DB_URL = "jdbc:h2:~/test";

    @BeforeClass
    public static void createTables() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, "sa","");
        Statement stmt = conn.createStatement();

        String customers =  "CREATE TABLE IF NOT EXISTS CUSTOMERS " +
                "(ID SERIAL PRIMARY KEY, " +
                " NAME VARCHAR(50) NOT NULL, " +
                " SURNAME VARCHAR(50) NOT NULL, " +
                " AGE BIGINT UNSIGNED NULL, " +
                " CITY VARCHAR(50) NOT NULL)";
        String contacts =  "CREATE TABLE IF NOT EXISTS CONTACTS " +
                "(ID SERIAL PRIMARY KEY, " +
                " ID_CUSTOMER BIGINT UNSIGNED NOT NULL, " +
                " TYPE VARCHAR(50) check (TYPE in ('UNKNOWN', 'EMAIL', 'PHONE', 'JABBER', 'ICQ')), " +
                " CONTACT VARCHAR(50) NULL, " +
                " FOREIGN KEY (ID_CUSTOMER) REFERENCES CUSTOMERS(ID))";

        stmt.executeUpdate(customers);
        stmt.executeUpdate(contacts);
        stmt.close();
    }

    @AfterClass
    public static void tearDown() {
        try {
            clearDatabase();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Test
    public void testConnect() throws SQLException, ClassNotFoundException {
        //Given //When //Then
        Assert.assertNotNull(new DbManager().getConnection());
    }

    @Test
    public void shouldReturnCorrectSizeForTableCustomersAndContacts() throws SQLException, IOException {
        //Given
        Connection conn = DriverManager.getConnection(DB_URL,"sa","");
        Statement stmt = conn.createStatement();

        //When
        RepositoryUtils.saveToDataBase(persons(), conn);

        String sqlQueryCustomer = "SELECT COUNT(*) FROM CUSTOMERS";
        ResultSet rs = stmt.executeQuery(sqlQueryCustomer);
        int customerId = 0;
        while(rs.next()) {
            customerId = rs.getInt("COUNT(*)");
        }

        String sqlQueryContact = "SELECT COUNT(*) FROM CONTACTS";
        rs = stmt.executeQuery(sqlQueryContact);
        int contactId = 0;
        while(rs.next()) {
            contactId= rs.getInt("COUNT(*)");
        }

        //Then
        rs.close();
        stmt.close();
        assertEquals(2, customerId);
        assertEquals(15, contactId);
    }

    private List<Person> persons() throws IOException {
        return XmlParserUtils.personList(XmlParserUtils.readFileXml());
    }


    private static void clearDatabase() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, "sa","");
        Statement stmt = conn.createStatement();

        stmt.execute("SET REFERENTIAL_INTEGRITY FALSE");

        Set<String> tables = new HashSet<String>();
        ResultSet rs = stmt.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES  where TABLE_SCHEMA='PUBLIC'");

        while (rs.next()) {
            tables.add(rs.getString(1));
        }
        rs.close();

        for (String table : tables) {
            stmt.executeUpdate("TRUNCATE TABLE " + table);
        }

        Set<String> sequences = new HashSet<String>();
        rs = stmt.executeQuery("SELECT SEQUENCE_NAME FROM INFORMATION_SCHEMA.SEQUENCES WHERE SEQUENCE_SCHEMA='PUBLIC'");

        while (rs.next()) {
            sequences.add(rs.getString(1));
        }
        rs.close();

        for (String seq : sequences) {
            stmt.executeUpdate("ALTER SEQUENCE " + seq + " RESTART WITH 1");
        }

        stmt.execute("SET REFERENTIAL_INTEGRITY TRUE");
        stmt.close();
        conn.close();
    }
}