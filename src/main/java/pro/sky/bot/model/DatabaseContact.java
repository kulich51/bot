package pro.sky.bot.model;

import javax.persistence.*;

@Entity(name = "contacts")
public class DatabaseContact {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "user_id")
    private Long userId;

    public DatabaseContact(Long id, String phoneNumber, String firstName, String lastName, Long userId) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userId = userId;
    }

    public DatabaseContact() {
    }
}
