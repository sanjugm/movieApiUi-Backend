package com.movieflix.auth.entities;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;

	@NotBlank(message = "The name field can't be blank")
	private String name;

	@NotBlank(message = "The username field can't be blank")
	@Column(unique = true, nullable = false)
	private String username;

	@NotBlank(message = "The email field can't be blank")
	@Column(unique = true, nullable = false)
	@Email(message = "Please enter email in proper format!")
	private String email;

	@NotBlank(message = "The password field can't be blank")
	@Size(min = 5, message = "The password must have at least 5 characters")
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserRole role;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private RefreshToken refreshToken;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private ForgotPassword forgotPassword;

	// 🔹 Spring Security methods
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(role.name()));
	}

	@Override
	public String getPassword() {
		return password;
	}

	// ⚠️ Important: Spring Security uses "username" for login, but you were
	// returning email earlier.
	// If you want login via email -> return email
	// If you want login via username -> return username
	@Override
	public String getUsername() {
		return email; // ✅ using email as the login credential
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
