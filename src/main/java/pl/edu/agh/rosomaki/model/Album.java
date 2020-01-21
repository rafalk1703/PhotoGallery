package pl.edu.agh.rosomaki.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.edu.agh.rosomaki.persistance.ConnectionProvider;
import pl.edu.agh.rosomaki.persistance.QueryExecutor;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Album {
    private int id;
    private String name;
    private final ObservableList<Photo> photos = FXCollections.observableArrayList();

    private static final String INSERT_SQL = "INSERT INTO ALBUM (name) VALUES (?)";
    private static final String DELETE_SQL = "DELETE FROM ALBUM WHERE id = ?";

    public Album(String name) {
        this.name = name;
    }

    public Album(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void persist() {
        try (PreparedStatement statement = ConnectionProvider.getConnection().prepareStatement(INSERT_SQL)) {
            statement.setString(1, name);
            this.id = QueryExecutor.createAndObtainId(statement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete() {
        photos.forEach(Photo::delete);
        try (PreparedStatement statement = ConnectionProvider.getConnection().prepareStatement(DELETE_SQL)) {
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void addPhoto(Photo photo) {
        photos.add(photo);
    }

    public void removePhoto(Photo photo) {
        photos.remove(photo);
    }

    @Override
    public String toString() {
        return name;
    }

    public void updateList(Photo p) throws CloneNotSupportedException {
        Photo tmp = (Photo) p.cloneButPublic(p);
        photos.add(tmp);
        photos.remove(tmp);
    }
}
