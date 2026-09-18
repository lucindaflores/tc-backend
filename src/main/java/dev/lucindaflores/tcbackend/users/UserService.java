package dev.lucindaflores.tcbackend.users;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
class UserService {

    private final UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    Optional<User> findById(long id) {
        return  userRepository.findById(id);
    }

    Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    long create(NewUser newUser) {
        if (userRepository.findByEmail(newUser.email()).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        var user = new User(
                newUser.email(),
                newUser.firstName(),
                newUser.lastName()
        );

        userRepository.save(user);

        return user.getId();
    }


}
