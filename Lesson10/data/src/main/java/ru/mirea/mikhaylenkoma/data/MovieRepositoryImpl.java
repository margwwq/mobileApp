package ru.mirea.mikhaylenkoma.data.data;

import java.time.LocalDate;

import ru.mirea.mikhaylenkoma.data.data.storage.models.Movie;
import ru.mirea.mikhaylenkoma.data.data.storage.MovieStorage;
import ru.mirea.mikhaylenkoma.domain.domain.repository.MovieRepository;

public class MovieRepositoryImpl implements MovieRepository {

    private MovieStorage movieStorage;

    public MovieRepositoryImpl(MovieStorage movieStorage) {
        this.movieStorage = movieStorage;
    }

    @Override
    public boolean saveMovie(ru.mirea.mikhaylenkoma.domain.domain.models.Movie movie) {
        movieStorage.save(mapToStorage(movie));
        return true;
    }

    @Override
    public ru.mirea.mikhaylenkoma.domain.domain.models.Movie getMovie() {
        return mapToDomain(movieStorage.get());
    }

    private Movie mapToStorage(ru.mirea.mikhaylenkoma.domain.domain.models.Movie movie){
        String name = movie.getName();
        return new Movie(2, name, LocalDate.now().toString());
    }

    private ru.mirea.mikhaylenkoma.domain.domain.models.Movie mapToDomain(Movie movie) {
        return new ru.mirea.mikhaylenkoma.domain.domain.models.Movie(movie.getId(), movie.getName());
    }

}