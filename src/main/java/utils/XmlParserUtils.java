package utils;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import domain.Person;
import domain.Persons;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class XmlParserUtils {

    public static void prepareDataAndSave() {
        try {
            DbManager dbManager = new DbManager();
            Connection conn = dbManager.getConnection();
            String fileContents = readFileXml();
            List<Person> persons = personList(fileContents);
            RepositoryUtils.saveToDataBase(persons, conn);
            conn.close();
        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected static String readFileXml() {
        Path path = Paths.get("data/dane-osoby.xml");

        StringBuilder sb = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path.toString()))) {
            String line = bufferedReader.readLine();
            while (line != null) {
                sb.append(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            System.out.println("Problem reading the file");
            e.printStackTrace();
        }
        return sb.toString();
    }

    protected static List<Person> personList(String fileContent) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        Persons persons = xmlMapper.readValue(fileContent, Persons.class);
        persons.getPerson().forEach(Person::buildContacts);
        return persons.getPerson();
    }
}
