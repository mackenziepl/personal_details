package utils;

import domain.Contact;
import domain.Person;
import org.apache.commons.lang3.ObjectUtils;

import java.sql.*;
import java.util.List;

import static utils.Commons.*;

public class RepositoryUtils {

    public static void saveToDataBase(List<Person> persons, Connection conn) throws SQLException {
        createTables(conn);
        persons
                .forEach(person -> {
                    try {
                        int customerId = insertToCustomersTable(person, conn);
                        List<Contact> contacts = person.getContactList();
                        insertToContactsTable(customerId, contacts, conn);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
    }

    private static int insertToCustomersTable(Person person, Connection conn) throws SQLException {
        String sqlCustomer = "INSERT INTO CUSTOMERS(NAME, SURNAME, AGE, CITY) VALUES(?,?,?,?)";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int customerId = 0;
        stmt = conn.prepareStatement(sqlCustomer, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(STMT_NAME, person.getName());
        stmt.setString(STMT_SURNAME, person.getSurname());
        stmt.setObject(STMT_AGE, person.getAge() != 0 ? person.getAge() : null);
        stmt.setString(STMT_CITY, person.getCity());
        int rowAffected = stmt.executeUpdate();

        if (rowAffected == 1) {
            rs = stmt.getGeneratedKeys();
            if (rs.next())
                customerId = rs.getInt(ID);
        }

        if (ObjectUtils.isNotEmpty(stmt)) stmt.close();
        if (ObjectUtils.isNotEmpty(rs)) rs.close();
        return customerId;
    }

    private static void insertToContactsTable(int customerId, List<Contact> contacts, Connection conn) {
        contacts
                .forEach(c -> {
                    String sqlContact = "INSERT INTO CONTACTS(ID_CUSTOMER, TYPE, CONTACT) VALUES(?,?,?)";
                    PreparedStatement stmtCon = null;
                    try {
                        stmtCon = conn.prepareStatement(sqlContact, Statement.RETURN_GENERATED_KEYS);
                        stmtCon.setInt(ID, customerId);
                        stmtCon.setObject(TYPE, c.getType().toString());
                        stmtCon.setString(CONTACT, c.getContact());

                        stmtCon.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (ObjectUtils.isNotEmpty(stmtCon)) stmtCon.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public static void showDataBases() {
        try {
            DbManager dbManager = new DbManager();
            Connection conn = dbManager.getConnection();
            createTables(conn);
            showCustomersTable(conn);
            showContactsTable(conn);
            conn.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void showCustomersTable(Connection conn) throws SQLException {
        System.out.println("CUSTOMERS TABLE DATA");
        System.out.println();

        Statement stmt = conn.createStatement();
        String sqlCustomers = "SELECT * FROM CUSTOMERS";
        ResultSet rs = stmt.executeQuery(sqlCustomers);

        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            int age = rs.getInt("age");
            System.out.print("Id: " + id);
            System.out.print(", Name: " + name);
            System.out.print(", Surname: " + surname);
            System.out.println(", Age: " + (age == 0 ? null : age));
        }

        rs.close();
        stmt.close();
    }

    private static void showContactsTable(Connection conn) throws SQLException {
        System.out.println();
        System.out.println("CONTACTS TABLE DATA");

        Statement stmt = conn.createStatement();
        String sqlContact = "SELECT * FROM CONTACTS";
        ResultSet rs = stmt.executeQuery(sqlContact);

        while (rs.next()) {
            int id = rs.getInt("id");
            int id_Customer = rs.getInt("id_customer");
            String type = rs.getString("type");
            String contact = rs.getString("contact");
            System.out.print("Id: " + id);
            System.out.print(", Id_Customer: " + id_Customer);
            System.out.print(", Type: " + type);
            System.out.println(", Contact: " + contact);
        }
        rs.close();
        stmt.close();
    }

    private static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();

        String createCustomersQuery = "CREATE TABLE IF NOT EXISTS CUSTOMERS " +
                "(ID SERIAL PRIMARY KEY, " +
                " NAME VARCHAR(50) NOT NULL, " +
                " SURNAME VARCHAR(50) NOT NULL, " +
                " AGE BIGINT UNSIGNED NULL, " +
                " CITY VARCHAR(50) NOT NULL)";
        String createContactsQuery = "CREATE TABLE IF NOT EXISTS CONTACTS " +
                "(ID SERIAL PRIMARY KEY, " +
                " ID_CUSTOMER BIGINT UNSIGNED NOT NULL, " +
                " TYPE VARCHAR(50) check (TYPE in ('UNKNOWN', 'EMAIL', 'PHONE', 'JABBER', 'ICQ')), " +
                " CONTACT VARCHAR(50) NULL, " +
                " FOREIGN KEY (ID_CUSTOMER) REFERENCES CUSTOMERS(ID))";

        stmt.executeUpdate(createCustomersQuery);
        stmt.executeUpdate(createContactsQuery);
        stmt.close();
    }
}
