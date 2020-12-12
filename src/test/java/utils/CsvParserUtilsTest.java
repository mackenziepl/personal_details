package utils;

import domain.Person;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class CsvParserUtilsTest {

    @Test
    public void shouldCorrectlyReadFileCsvAndReturnStringList() {
        // Given // When
        List<String> list = CsvParserUtils.readFileCsv();

        System.out.println(list);
        System.out.println();

        // Then
        Assert.assertTrue(list.size() > 0);
    }

    @Test
    public void shouldReturnPersonList() {
        // Given // When
        List<String> list = CsvParserUtils.readFileCsv();
        List<Person> personList = CsvParserUtils.personList(list);

        System.out.println(personList);
        System.out.println();

        // Then
        Assert.assertEquals(3, personList.size());
        Assert.assertTrue(personList.size() > 0);
    }
}