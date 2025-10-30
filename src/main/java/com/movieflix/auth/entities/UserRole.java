package com.movieflix.auth.entities;

public enum UserRole {
	USER, ADMIN;

	public String getAuthority() {
		return "ROLE_" + this.name();
	}
}