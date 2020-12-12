package domain;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JacksonXmlRootElement(localName = "persons")
public class Persons {
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Person> person;
}
