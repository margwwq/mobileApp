package ru.mirea.mikhaylenkoma.movieproject.domain.repository;

import ru.mirea.mikhaylenkoma.movieproject.domain.models.Movie;

public interface MovieRepository {
    public boolean saveMovie(Movie movie);
    public Movie getMovie();
}