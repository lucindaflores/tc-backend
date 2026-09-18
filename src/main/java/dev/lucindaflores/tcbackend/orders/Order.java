package dev.lucindaflores.tcbackend.orders;

import dev.lucindaflores.tcbackend.users.Address;
import dev.lucindaflores.tcbackend.users.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "orderGenerator")
    @SequenceGenerator(name = "orderGenerator",
            sequenceName = "orderid",
            allocationSize = 1)
    private long id;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING) private Status status;

    /* Relationships */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order",
               cascade = CascadeType.PERSIST) // so order detail is persisted
    private List<OrderDetail> orderDetails = new ArrayList<>();


    /* Constructor(s) */
    public Order(User user, Address address) {
        this.orderDate = LocalDateTime.now();
        this.status = Status.PLACED;
        this.user = user;

    }

    protected Order() { }


    /* Getters */
    public long getId() {
        return id;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public Status getStatus() {
        return status;
    }

    public User getUser() {
        return user;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    /* Functions */
    void add(OrderDetail detail) {
        orderDetails.add(detail);
    }


}
