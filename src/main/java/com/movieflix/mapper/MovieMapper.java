package com.movieflix.mapper;

import com.movieflix.dto.MovieDto;
import com.movieflix.entities.Movie;

public class MovieMapper {

	// Map MovieDto -> Movie entity
	public static Movie toEntity(MovieDto dto) {
		return new Movie(dto.getMovieId(), dto.getTitle(), dto.getDirector(), dto.getStudio(), dto.getMovieCast(),
				dto.getReleaseYear(), dto.getPoster());
	}

	// Map Movie entity -> MovieDto
	public static MovieDto toDto(Movie movie, String baseUrl) {
		String posterUrl = baseUrl + "/file/" + movie.getPoster();
		return new MovieDto(movie.getMovieId(), movie.getTitle(), movie.getDirector(), movie.getStudio(),
				movie.getMovieCast(), movie.getReleaseYear(), movie.getPoster(), posterUrl);
	}
}
