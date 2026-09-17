package dev.lucinda.tcbackend.origins;

import jakarta.persistence.*;

@Entity
@Table(name="origins")
public class Origin {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "originGenerator")
    @SequenceGenerator(name = "originGenerator",
            sequenceName = "originid",
            allocationSize = 1)
    private long id;

    private String name;


    /* Constructor(s) */
    Origin(String name) {
        this.name = name;
    }

    protected Origin() { }


    /* Getters */
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
