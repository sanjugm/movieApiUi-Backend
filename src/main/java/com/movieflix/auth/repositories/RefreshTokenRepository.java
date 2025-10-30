package com.movieflix.auth.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.movieflix.auth.entities.RefreshToken;
import com.movieflix.auth.entities.User;

import jakarta.transaction.Transactional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	Optional<RefreshToken> findByUser(User user);

	@Transactional
	void deleteByUser(User user);
}
