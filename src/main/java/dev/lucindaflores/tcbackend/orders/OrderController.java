package dev.lucindaflores.tcbackend.orders;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("orders")
@CrossOrigin
class OrderController {

    private final OrderService orderService;

    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /* DTO(s) */
    // DTO order general info no details
    private record OrderWithoutDetails(
            long id,
            LocalDateTime orderDate,
            Status status,
            long customerId,
            String customerFirstName,
            String customerLastName) {
        public OrderWithoutDetails(Order order) {
            this(order.getId(), 
                 order.getOrderDate(),
                 order.getStatus(),
                 order.getUser().getId(),
                 order.getUser().getFirstName(),
                 order.getUser().getLastName());
        }
    }

    // DTO to show an order information with final total and product details including price by ordered units
    private record OrderWithFullDetails(
            long id,
            LocalDateTime orderDate,
            Status status,
            BigDecimal total,
            long customerId,
            String customerFirstName,
            String customerLastName,
            List<OrderedDetails>orderDetails){
        public OrderWithFullDetails(Order order) {
            this(order.getId(),
                 order.getOrderDate(),
                 order.getStatus(),

                 // Calculation Final total of the order
                 order.getOrderDetails()
                         .stream()
                         .map(orderDetail -> BigDecimal.valueOf(orderDetail.getQuantity()).multiply(orderDetail.getUnitPrice()))
                         .reduce(BigDecimal.ZERO, BigDecimal::add),

                 order.getUser().getId(),
                 order.getUser().getFirstName(),
                 order.getUser().getLastName(),

                 // Order details list
                 order.getOrderDetails()
                         .stream()
                         .map(OrderedDetails::new)
                         .toList()
            );
        }  
    }

    // DTO: List of product details to be used by DTO OrderWithFullDetails
    private record OrderedDetails(String productName,
                                  int quantity,
                                  BigDecimal unitPrice,
                                  BigDecimal value) {
        public OrderedDetails(OrderDetail orderDetails) {
            this(orderDetails.getProductName(),
                 orderDetails.getQuantity(),
                 orderDetails.getUnitPrice(),
                 // Calculation by product: quantity * unit price
                 BigDecimal.valueOf(orderDetails.getQuantity()).multiply(orderDetails.getUnitPrice())
            );
        }
    }


    /* Requests */
    // GET http://localhost:8080/orders?userId={{userId}}
    @GetMapping(params = "userId")
    List<OrderWithoutDetails> findByUserId(@RequestParam long userId) {
        return orderService.findByUserId(userId)
                .stream()
                .map(OrderWithoutDetails::new)
                .toList();
    }


    // GET http://localhost:8080/orders/{{id}}
    @GetMapping("{id}")
    OrderWithFullDetails findById(@PathVariable long id) {
        return orderService.findById(id)
                .map(OrderWithFullDetails::new)
                .orElseThrow(OrderNotFoundException::new);
    }


    // POST http://localhost:8080/orders
    @PostMapping
    long create(@RequestBody @Valid NewOrder newOrder) {
        return orderService.create(newOrder);
    }
    /*

{
  "userId": 478,
  "addressId": 276,
  "orderDetails": [{"productId": 1, "quantity": 2}]
}
     */
}
