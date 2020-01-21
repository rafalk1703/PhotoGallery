package pl.edu.agh.rosomaki.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.edu.agh.rosomaki.persistance.ConnectionProvider;
import pl.edu.agh.rosomaki.persistance.QueryExecutor;

import java.awt.image.BufferedImage;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
public class Photo implements Cloneable { //żeby się dało klonować
    private int id;
    private String name;
    private int height;
    private int width;
    private LocalDate date;
    private String location;
    private String path;
    private int albumId;
    private BufferedImage image;
    private Set<Tag> tags;

    private static final String INSERT_SQL =
            "INSERT INTO PHOTO (name, height, width, date, location, path, album_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String DELETE_SQL = "DELETE FROM PHOTO WHERE PHOTO.ID = ?";
    private static final String UPDATE_SQL =
            "UPDATE PHOTO " +
                    "SET name = ?, date = ?, location = ? " +
                    "WHERE PHOTO.ID = ?";

    public void persist(int albumId) {
        this.albumId = albumId;
        try (PreparedStatement statement = ConnectionProvider.getConnection().prepareStatement(INSERT_SQL)) {
            statement.setString(1, name);
            statement.setInt(2, height);
            statement.setInt(3, width);
            statement.setDate(4, Date.valueOf(date));
            statement.setString(5, location);
            statement.setString(6, path);
            statement.setInt(7, albumId);
            int id = QueryExecutor.createAndObtainId(statement);
            this.id = id;
            tags.forEach(t -> t.persist(id));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete() {
        tags.forEach(Tag::delete);
        try (PreparedStatement statement = ConnectionProvider.getConnection().prepareStatement(DELETE_SQL)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update() {
        try (PreparedStatement statement = ConnectionProvider.getConnection().prepareStatement(UPDATE_SQL)) {
            statement.setString(1, name);
            statement.setDate(2, Date.valueOf(date));
            statement.setString(3, location);
            statement.setInt(4, id);
            statement.executeUpdate();
            tags.forEach(t -> t.persist(id));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Photo cloneButPublic(Photo photo) throws CloneNotSupportedException {
        return (Photo) photo.clone();
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    /**
     * funkcja odwrotna do createTags
     */
    public String getTagsString() {
        String toStr = tags.toString();
        return toStr.substring(1, toStr.length() - 1);
    }

    @Override
    public String toString() {
        return String.format("Photo: %s, location: %s, taken %s %s",
                name, location, date.toString(), returnTags());

    }

    private String returnTags() {
        return tags.stream()
                .map(Tag::getName)
                .reduce("", (acc, tag) -> String.format("%s #%s", acc, tag));
    }
}