package org.example.electricstore.vatidator.customer;

import org.example.electricstore.repository.IUserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Setter
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    private IUserRepository userRepository;

    @Setter
    private Integer currentUserId;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if(email == null || email.isBlank()){
            return false;
        }
            return !userRepository.existsByEmailAndUserIdNot(email, currentUserId);
    }

}
