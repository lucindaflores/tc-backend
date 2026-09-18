package dev.lucindaflores.tcbackend.users;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("addresses")
@CrossOrigin
class AddressController {

    private final AddressService addressService;

    AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    /* DTOs */
    private record AddressDetails(
            long addressId,
            String street,
            String houseNumber,
            String bus,
            String city,
            String postalCode,
            String country,
            long userId) {
        AddressDetails(Address address) {
            this(address.getId(),
                 address.getStreet(),
                 address.getHouseNumber(),
                 address.getBus(),
                 address.getCity(),
                 address.getPostalCode(),
                 address.getCountry(),
                 address.getUser().getId()
            );
        }
    }

    // GET http://localhost:8080/addresses/{{id}}
    @GetMapping("{id}")
    AddressDetails findById(@PathVariable long id) {
        return addressService.findById(id)
                .map(AddressDetails::new)
                .orElseThrow(AddressNotFoundException::new);
    }

    // GET  http://localhost:8080/addresses?userId={{userId}}
    @GetMapping(params = "userId")
    List<AddressDetails> findByUserId(@RequestParam long userId) {
        return addressService.findByUserId(userId)
                .stream()
                .map(AddressDetails::new)
                .toList();
    }

    // POST http://localhost:8080/addresses?userId={{userId}}
    @PostMapping
    long create(@RequestParam long userId,
                @RequestBody @Valid NewAddress newAddress) {
        return addressService.create(userId, newAddress);
    }
    /*
    {
      "street": "x",
      "houseNumber": "x",
      "bus": "",
      "city": "x",
      "postalCode": "x",
      "country": "x"
    }
     */
}
