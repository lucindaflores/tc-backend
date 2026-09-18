package dev.lucindaflores.tcbackend.users;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
@CrossOrigin
class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    /* DTOs */
    private record UserDetails(
            long userId,
            String firstName,
            String lastName,
            String email) {
        UserDetails (User user) {
            this(user.getId(),
                    user.getFirstName() ,
                    user.getLastName(),
                    user.getEmail());
        }
    }

    /* Requests */
    // GET http://localhost:8080/USERS/{{id}}
    @GetMapping("{id}")
    UserDetails findById(@PathVariable long id) {
        return userService.findById(id)
                .map(UserDetails::new)
                .orElseThrow(UserNotFoundException::new);
    }

    // GET http://localhost:8080/users?email=test@test.com
    @GetMapping(params = "email")
    UserDetails findByEmail(@RequestParam String email) {
        return userService.findByEmail(email)
                .map(UserDetails::new)
                .orElseThrow(UserNotFoundException::new);
    }

    // POST http://localhost:8080/users
    @PostMapping
    long create(@RequestBody @Valid NewUser newUser) {
        return userService.create(newUser);
    }
/*
    {
        "email": "test@test.com",
            "firstName": "test",
            "lastName": "test"
    }
 */

}
