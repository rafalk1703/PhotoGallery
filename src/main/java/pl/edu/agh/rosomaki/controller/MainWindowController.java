package pl.edu.agh.rosomaki.controller;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import pl.edu.agh.rosomaki.model.Album;
import pl.edu.agh.rosomaki.model.Gallery;
import pl.edu.agh.rosomaki.persistance.DataInjector;

import java.io.IOException;
import java.util.Collections;

public class MainWindowController {

    @FXML
    private ListView<Album> albumList;

    @FXML
    private Button addAlbumButton;

    @FXML
    private Button setMailButton;

    @FXML
    private Button viewAlbumButton;

    @FXML
    private Button deleteAlbumButton;

    @FXML
    private Button closeWindowButton;

    @FXML
    private Button sortButton;

    private Stage mainStage;

    private final Gallery gallery = DataInjector.injectPersistedData();

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    @FXML
    private void initialize() {
        albumList.setItems(gallery.getAlbums());
        albumList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        viewAlbumButton.disableProperty().bind(Bindings.isEmpty(albumList.getSelectionModel().getSelectedItems()));
        deleteAlbumButton.disableProperty().bind(Bindings.isEmpty(albumList.getSelectionModel().getSelectedItems()));
    }

    @FXML
    private void deleteAlbumAction() {
        for (Album album : albumList.getSelectionModel().getSelectedItems()) {
            album.getPhotos().clear();
            album.delete();
        }
        gallery.getAlbums().removeAll(albumList.getSelectionModel().getSelectedItems());
    }

    @FXML
    private void sortAction(){
        Collections.sort(albumList.getItems(), (p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()));
    }

    @FXML
    private void addAlbumAction() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/add_album_window.fxml"));
            Parent root = loader.load();

            Stage secondStage = new Stage();
            secondStage.setTitle("Add new album");
            secondStage.initOwner(this.mainStage);

            Scene scene = new Scene(root);
            secondStage.setScene(scene);

            AddAlbumController addAlbumController = loader.getController();
            addAlbumController.setAddAlbumStage(secondStage);
            addAlbumController.setGallery(gallery);

            secondStage.showAndWait();


        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    private void setMailAction() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/start_window.fxml"));
            Parent root = loader.load();

            Stage secondStage = new Stage();
            secondStage.setTitle("Set MailAddress");
            secondStage.initOwner(this.mainStage);

            Scene scene = new Scene(root);
            secondStage.setScene(scene);

            StartWindowController startWindowController = loader.getController();
            startWindowController.setStartStage(secondStage);


            secondStage.showAndWait();


        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    private void closeWindowAction() {
        mainStage.close();
    }

    @FXML
    private void openAlbumViewAction() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/album_view.fxml"));
            Parent root = loader.load();

            Stage secondStage = new Stage();
            secondStage.setTitle("Album View");
            secondStage.initOwner(this.mainStage);

            Scene scene = new Scene(root);
            secondStage.setScene(scene);

            secondStage.setResizable(false);

            AlbumController albumController = loader.getController();
            albumController.setAlbumStage(secondStage);
            albumController.setAlbum(albumList.getSelectionModel().getSelectedItems().get(0));
            secondStage.showAndWait();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }


}
