package dev.lucindaflores.tcbackend.users;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "userGenerator")
    @SequenceGenerator(name = "userGenerator",
            sequenceName = "userid",
            schema = "public",
            allocationSize = 1)
    private long id;

    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Enumerated(EnumType.STRING) private Role role;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user")
    private Set<Address> addresses = new LinkedHashSet<>();


    /* Constructor(s) */
    public User(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = Role.CUSTOMER;
        this.createdAt = LocalDateTime.now();
    }

    protected User() {
    }

    /* Getters */
    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Role getRole() {
        return role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Set<Address> getAddresses() {
        return Collections.unmodifiableSet(addresses);
    }

}
