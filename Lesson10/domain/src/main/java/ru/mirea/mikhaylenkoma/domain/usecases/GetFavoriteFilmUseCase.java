package ru.mirea.mikhaylenkoma.domain.domain.usecases;

import ru.mirea.mikhaylenkoma.domain.domain.models.Movie;
import ru.mirea.mikhaylenkoma.domain.domain.repository.MovieRepository;
public class GetFavoriteFilmUseCase {
    private MovieRepository movieRepository;

    public GetFavoriteFilmUseCase(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }
    public Movie execute(){
        return movieRepository.getMovie();
    }
}
