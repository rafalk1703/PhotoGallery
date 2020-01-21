package pl.edu.agh.rosomaki.model;

import lombok.Getter;
import lombok.Setter;
import pl.edu.agh.rosomaki.persistance.ConnectionProvider;
import pl.edu.agh.rosomaki.persistance.QueryExecutor;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

@Getter
@Setter
public class Tag {
    private int id;
    private String name;
    private int photoId;

    private static final String INSERT_SQL = "INSERT INTO TAG (name, photo_id) VALUES (?, ?)";
    private static final String DELETE_SQL = "DELETE FROM TAG WHERE photo_id = ?";

    public Tag(String name) {
        this.name = name;
    }

    public Tag(int id, String name, int photoId) {
        this.id = id;
        this.name = name;
        this.photoId = photoId;
    }

    public void persist(int photoId) {
        this.photoId = photoId;
        try (PreparedStatement statement = ConnectionProvider.getConnection().prepareStatement(INSERT_SQL)) {
            statement.setString(1, name);
            statement.setInt(2, photoId);
            this.id = QueryExecutor.createAndObtainId(statement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete() {
        try (PreparedStatement statement = ConnectionProvider.getConnection().prepareStatement(DELETE_SQL)) {
            statement.setInt(1, photoId);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        Tag tag = (Tag) o;
        return tag.name.equals(this.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
