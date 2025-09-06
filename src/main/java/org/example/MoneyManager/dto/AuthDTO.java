package org.example.MoneyManager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthDTO {
    private String email;
    private String password;
    private String token;
}
