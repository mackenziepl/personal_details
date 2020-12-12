package utils;

import domain.Person;
import org.apache.commons.lang3.StringUtils;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static utils.Commons.*;

public class CsvParserUtils {

    public static void prepareDataAndSave() {
        try {
            DbManager dbManager = new DbManager();
            Connection conn = dbManager.getConnection();
            List<String> fileContents = readFileCsv();
            List<Person> persons = personList(fileContents);
            RepositoryUtils.saveToDataBase(persons, conn);
            conn.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected static List<String> readFileCsv() {
        Path path = Paths.get("data/dane-osoby.csv");
        List<String> read = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path.toString()))) {
            String line = bufferedReader.readLine();
            while (line != null) {
                if(StringUtils.isNotEmpty(line)) read.add(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            System.out.println("Problem reading the file");
            e.printStackTrace();
        }
        return read;
    }

    protected static List<Person> personList(List<String> read) {
        List<Person> customers = new ArrayList<>();

        for (String line : read) {
            String[] l = line.split("[,;|]+");

            String[] contacts = StringUtils.isNumeric(l[AGE]) ? Arrays.copyOfRange(l, 4, l.length)
                    : Arrays.copyOfRange(l, 3, l.length);
            List<String> list = Arrays.asList(contacts);

            Person person = Person.builder()
                    .name(l[NAME])
                    .surname(l[SURNAME])
                    .age(StringUtils.isNumeric(l[AGE]) ? Integer.parseInt(l[AGE]) : 0)
                    .city(l[CITY])
                    .contacts(list).build();
            customers.add(person);
        }
        return customers;
    }
}
