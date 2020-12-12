package domain;

import lombok.*;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Person {
    private String name;
    private String surname;
    private int age;
    private String city;
    private List<String> contacts;
    private List<Contact> contactList;

    public Person(String name, String surname, int age, String city, List<String> contacts) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.city = city;
        this.contacts = contacts;
    }

    public List<Contact> getContactList() {
        return contactList == null ? buildContacts() : contactList;
    }

    public List<Contact> buildContacts() {
        return this.contactList = this.contacts.stream()
                .map(contactString -> Contact.builder()
                                      .type(contactType(contactString))
                                      .contact(contactString).build()
                )
                .collect(Collectors.toList());
    }

    protected static Type contactType(String type) {
        String str = type.trim();

        if (str.matches("^.*jabber.*$")) {
            return Type.JABBER;
        } else if (str.matches("^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$")) {
            return Type.EMAIL;
        } else if (str.matches("(?<!\\w)(\\(?(\\+|00)?48\\)?)?[ -]?\\d{3}[ -]?\\d{3}[ -]?\\d{3}(?!\\w)")) {
            return Type.PHONE;
        } else if (str.matches("[0-9]+")) {
            return Type.ICQ;
        }
        return Type.UNKNOWN;
    }
}
