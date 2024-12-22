package ru.mirea.mikhaylenkoma.data.data.storage;

import ru.mirea.mikhaylenkoma.data.data.storage.models.Movie;

public interface MovieStorage {
    public Movie get();
    public boolean save(Movie movie);
}