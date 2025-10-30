package com.movieflix.auth.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.movieflix.auth.entities.User;
import com.movieflix.auth.entities.UserRole;
import com.movieflix.auth.repositories.UserRepository;
import com.movieflix.auth.utils.AuthResponse;
import com.movieflix.auth.utils.LoginRequest;
import com.movieflix.auth.utils.RegisterRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final JwtService jwtService;
	private final RefreshTokenService refreshTokenService;
	private final AuthenticationManager authenticationManager;

	public AuthResponse register(RegisterRequest registerRequest) {
		User user = User.builder().name(registerRequest.getName()).email(registerRequest.getEmail())
				.username(registerRequest.getUsername()).password(passwordEncoder.encode(registerRequest.getPassword()))
				.role(UserRole.USER) // default role
				.build();

		User savedUser = userRepository.save(user);

		String accessToken = jwtService.generateToken(savedUser);
		String refreshToken = refreshTokenService.createRefreshToken(savedUser).getRefreshToken();

		return AuthResponse.builder().accessToken(accessToken).refreshToken(refreshToken).name(savedUser.getName())
				.email(savedUser.getEmail()).username(savedUser.getUsername()). // 👈 fixed
																				// here
				build();
	}

	public AuthResponse login(LoginRequest loginRequest) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

		User user = userRepository.findByEmail(loginRequest.getEmail())
				.orElseThrow(() -> new UsernameNotFoundException("User not found!"));

		String accessToken = jwtService.generateToken(user);
		String refreshToken = refreshTokenService.createRefreshToken(user).getRefreshToken();

		return AuthResponse.builder().accessToken(accessToken).refreshToken(refreshToken).name(user.getName())
				.email(user.getEmail()).username(user.getUsername()) // 👈 added here
				.build();
	}
}
