package dev.lucindaflores.tcbackend.materials;

import dev.lucindaflores.tcbackend.products.Product;
import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name="materials")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                   generator = "materialGenerator")
    @SequenceGenerator(name = "materialGenerator",
            sequenceName = "materialid",
     allocationSize = 1)
    private long id;

    String name;

    @Column(name = "name_spanish")
    String nameSpanish;
    String technique;

    @ManyToMany(mappedBy = "materials")
    private Set<Product> products = new LinkedHashSet<>();

    /* Constructor(s) */
    public Material(String name, String nameSpanish, String technique) {
        this.name = name;
        this.nameSpanish = nameSpanish;
        this.technique = technique;
    }

    protected Material() { }

    /* Getters */
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNameSpanish() {
        return nameSpanish;
    }

    public String getTechnique() {
        return technique;
    }


}
