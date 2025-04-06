package org.example.electricstore.DTO.user;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginDTO {
    private String username;
    private String password;
}
