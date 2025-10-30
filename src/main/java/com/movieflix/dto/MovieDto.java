package com.movieflix.dto;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDto {

	private Integer movieId;

	@NotBlank(message = "Please provide movie's title!")
	private String title;

	@NotBlank(message = "Please provide movie's director!")
	private String director;

	@NotBlank(message = "Please provide movie's studio!")
	private String studio;

	private Set<String> movieCast;

	private Integer releaseYear;

	@NotBlank(message = "Please provide movie's poster!")
	private String poster;

	@NotBlank(message = "Please provide movie's poster URL!")
	private String posterUrl;

}
