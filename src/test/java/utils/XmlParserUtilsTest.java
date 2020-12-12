package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import domain.Person;
import org.junit.Assert;
import org.junit.Test;;
import java.io.IOException;
import java.util.List;


public class XmlParserUtilsTest {

    @Test
    public void shouldCorrectlyReadFileXmlAndReturnString() {
        // Given // When
        String xmlFile = XmlParserUtils.readFileXml();

        System.out.println(xmlFile);
        System.out.println();

        // Then
        Assert.assertTrue(xmlFile.length() > 0);
    }

    @Test
    public void shouldReturnPersonList() throws IOException {
        // Given // When
        List<Person> personList = XmlParserUtils.personList(xmlString());

        System.out.println(personList);
        System.out.println();

        // Then
        Assert.assertEquals(2, personList.size());
        Assert.assertTrue(personList.size() > 0);
    }

    private static String xmlString() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><persons>    <person>        <name>Jan</name> " +
                "       <surname>Kowalski</surname>        <age>12</age>        <city>Lublin</city>        <contacts>        " +
                "    <phone>+48 123 456 789</phone>            <phone>654 765 765</phone>            <phone>123-456-789</phone> " +
                "           <email>kowalski@gmail.com</email>            <email>jan@gmail.com</email>        </contacts> " +
                "   </person>    <person>        <name>Adam</name>        <surname>Nowak</surname>        <city>Lublin</city>" +
                "        <contacts>            <phone>0048123456789</phone>            <email>adam@gmail.com</email>59347709 " +
                "           <icq>12321</icq>            <jabber>juser@jabber.org</jabber>            <phone>788 765 445</phone> " +
                "           <phone>(48)788 765 445</phone>            <email>nowak@gmail.com</email>            <phone>123456789</phone> " +
                "           <icq>59347709</icq>            <jabber>user@jabber.hot-chilli.eu</jabber>        </contacts>    </person></persons>";
    }
}