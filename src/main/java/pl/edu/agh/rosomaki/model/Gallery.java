package pl.edu.agh.rosomaki.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Gallery {
    private final ObservableList<Album> albums = FXCollections.observableArrayList();
    private final Album defaultAlbum = new Album("All photos");

    public ObservableList<Album> getAlbums() {
        return albums;
    }

    public Album getDefaultAlbum() {
        return defaultAlbum;
    }

    public void addAlbum(Album album) {
        albums.add(album);
    }

    public void deleteAlbum(Album album) {
        albums.remove(album);
    }
}
