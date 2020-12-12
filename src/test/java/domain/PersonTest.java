package domain;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PersonTest {
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
    public void shouldReturnJabberType_whenProvidedContactWithCorrectJabberPattern() {
        //Given
        List<String> contacts = new ArrayList<>();
        contacts.add("juser@jabber.org");
        contacts.add("user@jabber.hot-chilli.eu");

        for(String contact : contacts) {
            //When
            Type type = Person.contactType(contact);

            //Then
            Assert.assertEquals("JABBER", type.toString());
        }
    }

    @Test
    public void shouldReturnPhoneType_whenProvidedContactWithCorrectPhonePattern() {
        //Given
        List<String> contacts = new ArrayList<>();
        contacts.add("+48 123 456 789");
        contacts.add("654 765 765");
        contacts.add("123-456-789");
        contacts.add("0048123456789");
        contacts.add("(48)788 765 445");

        for(String contact : contacts) {
            //When
            Type type = Person.contactType(contact);

            //Then
            Assert.assertEquals("PHONE", type.toString());
        }
    }

    @Test
    public void shouldReturnEmailType_whenProvidedContactWithCorrectEmailPattern() {
        //Given
        List<String> contacts = new ArrayList<>();
        contacts.add("kowalski@gmail.com");
        contacts.add("jan@op.pl");
        contacts.add("adam@poczta.onet.pl");
        contacts.add("kowalski@yahoo.com");
        contacts.add("kowalski@mail.uk");

        for(String contact : contacts) {
            //When
            Type type = Person.contactType(contact);

            //Then
            Assert.assertEquals("EMAIL", type.toString());
        }
    }

    @Test
    public void shouldReturnICQType_whenProvidedContactWithCorrectICQPattern() {
        //Given
        List<String> contacts = new ArrayList<>();
        contacts.add("59347709");
        contacts.add("12321");

        for(String contact : contacts) {
            //When
            Type type = Person.contactType(contact);

            //Then
            Assert.assertEquals("ICQ", type.toString());
        }
    }

    @Test
    public void shouldReturnUnknownType_whenProvidedContactWithIncorrectPattern() {
        //Given
        List<String> contacts = new ArrayList<>();
        contacts.add("wee5934709");
        contacts.add("12321ww");

        for(String contact : contacts) {
            //When
            Type type = Person.contactType(contact);

            //Then
            Assert.assertEquals("UNKNOWN", type.toString());
        }
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