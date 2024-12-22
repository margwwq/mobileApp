package ru.mirea.mikhaylenkoma.movieproject.domain.usecases;

import ru.mirea.mikhaylenkoma.movieproject.domain.models.Movie;
import ru.mirea.mikhaylenkoma.movieproject.domain.repository.MovieRepository;

public class SaveMovieToFavoriteUseCase {
    private MovieRepository movieRepository;

    public SaveMovieToFavoriteUseCase(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public boolean execute(Movie movie){
        return movieRepository.saveMovie(movie);
    }
}