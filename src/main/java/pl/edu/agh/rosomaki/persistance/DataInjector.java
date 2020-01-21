package pl.edu.agh.rosomaki.persistance;

import pl.edu.agh.rosomaki.model.Album;
import pl.edu.agh.rosomaki.model.Gallery;
import pl.edu.agh.rosomaki.model.Photo;
import pl.edu.agh.rosomaki.model.Tag;
import pl.edu.agh.rosomaki.utils.PhotoUtils;

import java.awt.image.BufferedImage;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

public class DataInjector {
    private static final Logger LOGGER = Logger.getGlobal();
    private static final String LOAD_ALBUMS_SQL = "SELECT * FROM ALBUM";
    private static final String LOAD_PHOTOS_SQL = "SELECT * FROM PHOTO WHERE PHOTO.ALBUM_ID = ?";
    private static final String LOAD_TAGS_SQL = "SELECT * FROM TAG WHERE TAG.PHOTO_ID = ?";

    public static Gallery injectPersistedData() {
        Gallery gallery = new Gallery();
        try {
            ResultSet loadedAlbums = QueryExecutor.read(LOAD_ALBUMS_SQL);
            while (loadedAlbums.next()) {
                Collection<Photo> photos = loadPhotos(loadedAlbums.getInt("id"));
                Album album = createAlbumFromResultSet(loadedAlbums);
                photos.forEach(album::addPhoto);
                gallery.addAlbum(album);
            }
        } catch (SQLException e) {
            LOGGER.info("Error during loading albums");
            throw new RuntimeException(e);
        }
        return gallery;
    }

    private static Collection<Photo> loadPhotos(int albumId) {
        List<Photo> photos = new ArrayList<>();
        try (PreparedStatement statement = ConnectionProvider.getConnection().prepareStatement(LOAD_PHOTOS_SQL)) {
            statement.setInt(1, albumId);
            ResultSet loadedPhotos = statement.executeQuery();
            while (loadedPhotos.next()) {
                Collection<Tag> tags = loadTags(loadedPhotos.getInt("id"));
                Photo photo = createPhotoFromResultSet(loadedPhotos);
                tags.forEach(t -> {
                    photo.addTag(t);
                });
                photos.add(photo);
            }
        } catch (SQLException e) {
            LOGGER.info("Error during loading photos");
            throw new RuntimeException(e);
        }
        return photos;
    }

    private static Collection<Tag> loadTags(int photoId) {
        List<Tag> tags = new ArrayList<>();
        try (PreparedStatement statement = ConnectionProvider.getConnection().prepareStatement(LOAD_TAGS_SQL)) {
            statement.setInt(1, photoId);
            ResultSet loadedTags = statement.executeQuery();
            while (loadedTags.next()) {
                tags.add(createTagFromResultSet(loadedTags));
            }
        } catch (SQLException e) {
            LOGGER.info("Error during loading photos");
            throw new RuntimeException(e);
        }
        return tags;
    }

    private static Tag createTagFromResultSet(ResultSet tag) throws SQLException {
        int id = tag.getInt("id");
        String name = tag.getString("name");
        int photoId = tag.getInt("photo_id");
        return new Tag(id, name, photoId);
    }

    private static Photo createPhotoFromResultSet(ResultSet photo) throws SQLException {
        String path = photo.getString("path");
        BufferedImage image = PhotoUtils.loadImage(path);
        return Photo.builder()
                .id(photo.getInt("id"))
                .name(photo.getString("name"))
                .height(photo.getInt("height"))
                .width(photo.getInt("width"))
                .date(photo.getDate("date").toLocalDate())
                .location(photo.getString("location"))
                .path(path)
                .tags(new HashSet<>())
                .albumId(photo.getInt("album_id"))
                .image(image)
                .build();
    }

    private static Album createAlbumFromResultSet(ResultSet album) throws SQLException {
        int id = album.getInt("id");
        String name = album.getString("name");
        return new Album(id, name);
    }
}
