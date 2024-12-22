package ru.mirea.mikhaylenkoma.domain.domain.repository;

import ru.mirea.mikhaylenkoma.domain.domain.models.Movie;

public interface MovieRepository {
    public boolean saveMovie(Movie movie);
    public Movie getMovie();
}