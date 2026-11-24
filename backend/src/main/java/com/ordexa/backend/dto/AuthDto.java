package com.ordexa.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

public class AuthDto {

    public static class LoginRequest {
        @NotBlank(message = "Email is required")
        @Email(message = "Valid email is required")
        private String email;

        @NotBlank(message = "Password is required")
        private String password;

        public LoginRequest() {}
        public LoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class JwtResponse {
        private String token;
        private String tokenType = "Bearer";
        private UUID userId;
        private String name;
        private String email;
        private String role;
        private UUID companyId;
        private List<UUID> permittedFactoryIds;

        public JwtResponse() {}
        public JwtResponse(String token, UUID userId, String name, String email, String role, UUID companyId, List<UUID> permittedFactoryIds) {
            this.token = token;
            this.userId = userId;
            this.name = name;
            this.email = email;
            this.role = role;
            this.companyId = companyId;
            this.permittedFactoryIds = permittedFactoryIds;
        }

        public String getToken() { return token; }
        public String getTokenType() { return tokenType; }
        public UUID getUserId() { return userId; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getRole() { return role; }
        public UUID getCompanyId() { return companyId; }
        public List<UUID> getPermittedFactoryIds() { return permittedFactoryIds; }
    }
}
