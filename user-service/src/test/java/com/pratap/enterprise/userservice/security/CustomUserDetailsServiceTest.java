package com.pratap.enterprise.userservice.security;

import com.pratap.enterprise.userservice.entity.User;
import com.pratap.enterprise.userservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void loadUserByUsernameShouldReturnUserDetailsWhenUserExists() {
        User user = User.builder()
                .email("admin@example.com")
                .password("encoded-password")
                .role(User.Role.ADMIN)
                .status(User.Status.ACTIVE)
                .build();

        when(userRepository.findByEmail("admin@example.com"))
                .thenReturn(Optional.of(user));

        UserDetails result =
                customUserDetailsService.loadUserByUsername(
                        "admin@example.com"
                );

        assertThat(result.getUsername())
                .isEqualTo("admin@example.com");
        assertThat(result.getPassword())
                .isEqualTo("encoded-password");
        assertThat(result.getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_ADMIN");

        verify(userRepository).findByEmail("admin@example.com");
    }

    @Test
    void loadUserByUsernameShouldThrowWhenUserDoesNotExist() {
        when(userRepository.findByEmail("missing@example.com"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> customUserDetailsService.loadUserByUsername(
                        "missing@example.com"
                )
        )
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage(
                        "User not found with email: missing@example.com"
                );

        verify(userRepository).findByEmail("missing@example.com");
    }
}
