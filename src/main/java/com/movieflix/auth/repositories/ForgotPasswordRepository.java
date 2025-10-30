package com.movieflix.auth.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.movieflix.auth.entities.ForgotPassword;
import com.movieflix.auth.entities.User;

@Repository
public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Long> {

	Optional<ForgotPassword> findByOtpAndUser(Integer otp, User user);
}
