package pro.sky.bot.model;

import javax.persistence.*;

@Entity(name = "contacts")
public class DatabaseContact {

    @Id
    @Column(name = "user_id")
    private Long id;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    public DatabaseContact(Long id, String phoneNumber, String firstName, String lastName) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public DatabaseContact() {
    }
}
