package ru.mirea.mikhaylenkoma.movieproject.domain.usecases;

import ru.mirea.mikhaylenkoma.movieproject.domain.models.Movie;
import ru.mirea.mikhaylenkoma.movieproject.domain.repository.MovieRepository;
public class GetFavoriteFilmUseCase {
    private MovieRepository movieRepository;

    public GetFavoriteFilmUseCase(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }
    public Movie execute(){
        return movieRepository.getMovie();
    }
}