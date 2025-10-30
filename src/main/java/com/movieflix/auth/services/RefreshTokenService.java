package com.movieflix.auth.services;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.movieflix.auth.entities.RefreshToken;
import com.movieflix.auth.entities.User;
import com.movieflix.auth.repositories.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;

	private final long refreshDurationMs = 7 * 24 * 60 * 60 * 1000; // 7 days in ms

	public RefreshToken createRefreshToken(User user) {
		RefreshToken token = refreshTokenRepository.findByUser(user).orElse(new RefreshToken());

		token.setRefreshToken(UUID.randomUUID().toString());
		token.setExpiryDate(Instant.now().plusMillis(refreshDurationMs));
		token.setUser(user);

		return refreshTokenRepository.save(token);
	}
}
