package dev.lucindaflores.tcbackend.products;

import dev.lucindaflores.tcbackend.categories.Category;
import dev.lucindaflores.tcbackend.materials.Material;
import dev.lucindaflores.tcbackend.orders.OrderDetail;
import dev.lucindaflores.tcbackend.origins.Origin;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name="products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                      generator = "productGenerator")
    @SequenceGenerator(name = "productGenerator",
                    sequenceName = "productid",
    allocationSize = 1)
    private long id;

    private String code; // unique
    private String name;
    private String description;
    private BigDecimal price; // >0
    private int stock; // >=0

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_id")
    private Origin origin;

    @ManyToMany
    @JoinTable(
            name = "product_materials",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "material_id"))
    private final Set<Material> materials = new LinkedHashSet<>();

    @OneToMany(mappedBy = "product")
    private Set<OrderDetail> orderDetails;


    /* Constructor(s) */
    Product(String code,
            String name,
            String description,
            BigDecimal price,
            int stock,
            String imageUrl,
            boolean active,
            Category category,
            Origin origin) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.category = category;
        this.origin = origin;
    }

    protected Product() { }


    /* Getters */
    public long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    // @ManyToOne
    public Category getCategory() {
        return category;
    }

    // @ManyToOne
    public Origin getOrigin() {
        return origin;
    }

    // @ManyToMany
    public Set<Material> getMaterials() {
        return Collections.unmodifiableSet(materials);
    }

    /* @OneToMany */
    public Set<OrderDetail> getOrderDetails() {
        return Collections.unmodifiableSet(orderDetails);
    }

    public void setOrderDetails(Set<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }


    /* Functions */
    public void decreaseStock(int value) {
        if (this.stock < value) {
            throw new NotEnoughProductsException(this.getId());
        }

        stock -= value;
    }

    public void increaseStock(int value) {
        this.stock += value;
    }


    /* Equals & Hashcode */
    public boolean equals(Object object) {
        return object instanceof Product product && code.equalsIgnoreCase(product.code);
    }

    @Override
    public int hashCode() {
        return code.toLowerCase().hashCode();
    }

}
