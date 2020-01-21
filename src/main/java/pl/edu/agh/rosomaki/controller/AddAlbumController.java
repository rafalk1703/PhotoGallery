package pl.edu.agh.rosomaki.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pl.edu.agh.rosomaki.model.Album;
import pl.edu.agh.rosomaki.model.Gallery;

import java.sql.SQLException;
import java.util.List;

public class AddAlbumController {

    @FXML
    private TextField name;

    @FXML
    private Button addButton;

    @FXML
    private Button cancelButton;

    private Stage addAlbumStage;

    private Gallery gallery;

    public void setGallery(Gallery gallery) {
        this.gallery = gallery;
    }

    public void setAddAlbumStage(Stage addAlbumStage) {
        this.addAlbumStage = addAlbumStage;
    }

    @FXML
    private void addButtonAction() {
        Album album = new Album(name.getText());
        album.persist();
        gallery.addAlbum(album);
        addAlbumStage.close();
    }

    @FXML
    private void cancelButtonAction() {
        addAlbumStage.close();
    }
}
