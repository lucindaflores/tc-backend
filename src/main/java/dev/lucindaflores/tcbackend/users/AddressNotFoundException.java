package dev.lucindaflores.tcbackend.users;

public class AddressNotFoundException extends RuntimeException {
    public AddressNotFoundException() {
        super("The address was not found.");
    }
}
