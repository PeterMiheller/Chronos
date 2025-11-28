package com.example.chronos.service;

import com.example.chronos.DTO.AuthResponse;
import com.example.chronos.DTO.LoginRequest;
import com.example.chronos.model.Company;
import com.example.chronos.model.User;
import com.example.chronos.model.UserType;
import com.example.chronos.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private Company testCompany;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authService, "jwtExpiration", 3600000L);

        testCompany = new Company();
        testCompany.setId(1);
        testCompany.setName("Test Company");

        testUser = new User();
        testUser.setId(1);
        testUser.setEmail("test@example.com");
        testUser.setName("Test User");
        testUser.setPassword("password");
        testUser.setUserType(UserType.EMPLOYEE);
        testUser.setCompany(testCompany);
        testUser.setAdministratorId(2);

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");
    }

    @Test
    void authenticate_WithValidCredentials_ShouldReturnAuthResponse() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userRepository.findByEmail("test@example.com")).thenReturn(testUser);
        when(jwtService.generateToken(anyMap(), any(User.class)))
                .thenReturn("test-jwt-token");

        // Act
        AuthResponse response = authService.authenticate(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("test-jwt-token", response.getToken());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("Test User", response.getName());
        assertEquals("EMPLOYEE", response.getRole());
        assertEquals(1, response.getCompanyId());
        assertNotNull(response.getExpiresAt());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail("test@example.com");
        verify(jwtService).generateToken(anyMap(), eq(testUser));
    }

    @Test
    void authenticate_WithUserNotFound_ShouldThrowException() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userRepository.findByEmail("test@example.com")).thenReturn(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.authenticate(loginRequest));
        assertEquals("User not found", exception.getMessage());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail("test@example.com");
        verify(jwtService, never()).generateToken(any(), any());
    }

    @Test
    void authenticate_WithUserWithoutCompany_ShouldReturnNullCompanyId() {
        // Arrange
        testUser.setCompany(null);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userRepository.findByEmail("test@example.com")).thenReturn(testUser);
        when(jwtService.generateToken(anyMap(), any(User.class)))
                .thenReturn("test-jwt-token");

        // Act
        AuthResponse response = authService.authenticate(loginRequest);

        // Assert
        assertNotNull(response);
        assertNull(response.getCompanyId());
    }

    @Test
    void authenticate_WithAdministratorRole_ShouldIncludeCorrectRole() {
        // Arrange
        testUser.setUserType(UserType.ADMINISTRATOR);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userRepository.findByEmail("test@example.com")).thenReturn(testUser);
        when(jwtService.generateToken(anyMap(), any(User.class)))
                .thenReturn("test-jwt-token");

        // Act
        AuthResponse response = authService.authenticate(loginRequest);

        // Assert
        assertEquals("ADMINISTRATOR", response.getRole());
    }
}
