package org.example.electricstore.exception.customer;


import lombok.Getter;

@Getter
public class CustomerException extends RuntimeException {

    private final CustomerError errorCode;
    public CustomerException(CustomerError errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
