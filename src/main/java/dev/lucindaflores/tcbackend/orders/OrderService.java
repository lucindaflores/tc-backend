package dev.lucindaflores.tcbackend.orders;

import dev.lucindaflores.tcbackend.products.NotEnoughProductsException;
import dev.lucindaflores.tcbackend.products.ProductNotFoundException;
import dev.lucindaflores.tcbackend.products.ProductRepository;
import dev.lucindaflores.tcbackend.users.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly=true)
class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;

    OrderService(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository, AddressRepository addressRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.addressRepository = addressRepository;
    }


    /* Methods */
    Optional<Order> findById(long id) {
        return  orderRepository.findById(id);
    }

    List<Order> findByUserId(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException();
        }

        return orderRepository.findByUserId(userId);
    }

    @Transactional
    long create(NewOrder newOrder) {
        try {
            var user = userRepository.findById(newOrder.userId())
                    .orElseThrow(UserNotFoundException::new);


            var address = addressRepository.findByIdAndUserId(newOrder.addressId(), user.getId())
                    .orElseThrow(AddressNotFoundException::new);

            var order = new Order(user, address);

            // Validates that the order contains details
            if (newOrder.orderDetails().isEmpty()) {
                throw new OrderDetailEmptyException();
            }

            /* Order Detail */
            for (var detail : newOrder.orderDetails()) {
                var product = productRepository.findById(detail.productId())
                        .orElseThrow(ProductNotFoundException::new);

                // Check that there are enough items in stock
                if (product.getStock() < detail.quantity()) {
                    throw new NotEnoughProductsException(product.getId());
                }

                var orderDetail = new OrderDetail(product.getName(),
                                             detail.quantity(),
                                             product.getPrice(), //snapshot unit price
                                             order,
                                             product);

                product.decreaseStock(detail.quantity());

               order.add(orderDetail);
            }

            orderRepository.save(order);

            return order.getId();
        } catch (DataIntegrityViolationException _) {
            throw new OrderAlreadyPlacedException();
        }

    }

}
