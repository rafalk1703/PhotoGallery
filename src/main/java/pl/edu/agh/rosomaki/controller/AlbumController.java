package pl.edu.agh.rosomaki.controller;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import pl.edu.agh.rosomaki.model.Album;
import pl.edu.agh.rosomaki.model.Photo;
import pl.edu.agh.rosomaki.model.Tag;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


public class AlbumController {

    @FXML
    private ListView<Photo> photoList;

    @FXML
    private Button addPhotoButton;

    @FXML
    private Button viewPhotoButton;

    @FXML
    private Button deletePhotoButton;

    @FXML
    private Button editPhotoButton;

    @FXML
    private Button closeWindowButton;

    @FXML
    private Button sortButton;

    @FXML
    private Button findButton;

    @FXML
    private TextField findTextField;

    @FXML
    private ChoiceBox<String> sortBox;

    @FXML
    private ChoiceBox<String> findBox;

    private Stage albumStage;

    private Album album;


    private ObservableList<Photo> photos = FXCollections.observableArrayList();

    public void setAlbum(Album album) {
        this.album = album;
        photos.addAll(album.getPhotos());
        photoList.setItems(photos);
    }

    /*public void setPhotos(List<Photo> photos) {
        this.photoList = photos;
    }*/

    public void setAlbumStage(Stage albumStage) {
        this.albumStage = albumStage;
    }

    @FXML
    private void initialize() {
        photoList.setItems(photos);
        photoList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        viewPhotoButton.disableProperty().bind(Bindings.isEmpty(photoList.getSelectionModel().getSelectedItems()));
        deletePhotoButton.disableProperty().bind(Bindings.isEmpty(photoList.getSelectionModel().getSelectedItems()));
        editPhotoButton.disableProperty().bind(Bindings.isEmpty(photoList.getSelectionModel().getSelectedItems()));

        sortBox.getItems().addAll("date", "name", "location");
        findBox.getItems().addAll("date", "name", "location", "tags", "none");
    }

    @FXML
    private void findAction(){
        ObservableList<Photo> tmpList;

        switch(findBox.getValue()){
            case "name":
                tmpList = photos.stream()
                        .filter(photo -> photo.getName().equals(findTextField.getText()))
                        .collect(Collectors.collectingAndThen(toList(), l -> FXCollections.observableArrayList(l)));
                break;
            case "location":
                tmpList = photos.stream()
                        .filter(photo -> photo.getLocation().equals(findTextField.getText()))
                        .collect(Collectors.collectingAndThen(toList(), l -> FXCollections.observableArrayList(l)));
                break;
            case "tags":
                tmpList = photos.stream()
                        .filter(photo -> photo.getTags().contains(new Tag(findTextField.getText())))
                        .collect(Collectors.collectingAndThen(toList(), l -> FXCollections.observableArrayList(l)));
                break;
            case "date":
                tmpList = photos.stream()
                        .filter(photo -> photo.getDate().equals(LocalDate.parse(findTextField.getText())))
                        .collect(Collectors.collectingAndThen(toList(), l -> FXCollections.observableArrayList(l)));
                break;
            case "none":
                tmpList = photos;
                break;
            default:
                tmpList = null;
                break;

        }

        photoList.setItems(tmpList);
    }


    @FXML
    private void sortAction(){
        Collections.sort(photoList.getItems(), (p1, p2) -> {
            switch(sortBox.getValue()){
                case "date":  return p1.getDate().compareTo(p2.getDate());
                case "name": return p1.getName().compareToIgnoreCase(p2.getName());
                case "location": return p1.getLocation().compareToIgnoreCase(p2.getLocation());
                default: throw new IllegalArgumentException("wrong value");
            }
        });
    }

    @FXML
    private void deletePhotoAction() {
        Photo photo = photoList.getSelectionModel().getSelectedItems().get(0);
        album.removePhoto(photo);
        photos.removeAll(photoList.getSelectionModel().getSelectedItems());
        photo.delete();
    }

    @FXML
    private void addPhotoAction() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/add_photo_window.fxml"));
            Parent root = loader.load();

            Stage secondStage = new Stage();
            secondStage.setTitle("Add new photo");
            secondStage.initOwner(this.albumStage);

            Scene scene = new Scene(root);
            secondStage.setScene(scene);

            AddPhotoController addPhotoController = loader.getController();
            addPhotoController.setAddPhotoStage(secondStage);
            addPhotoController.setPhotos(photos);
            addPhotoController.setAlbum(album);
            addPhotoController.setTagSuggestions();
            addPhotoController.setLocationSuggestions();

            secondStage.showAndWait();


        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    private void editPhotoAction() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/edit_photo_window.fxml"));
            Parent root = loader.load();

            Stage secondStage = new Stage();
            secondStage.setTitle("Edit photo");
            secondStage.initOwner(this.albumStage);

            Scene scene = new Scene(root);
            secondStage.setScene(scene);

            EditPhotoController editPhotoController = loader.getController();
            editPhotoController.setEditPhotoStage(secondStage);
            editPhotoController.setPhotos(photos);
            editPhotoController.setPhoto(photoList.getSelectionModel().getSelectedItems().get(0));
            editPhotoController.setAlbum(album);

            secondStage.showAndWait();


        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    private void closeWindowAction() {
        albumStage.close();
    }

    @FXML
    private void openPhotoViewAction() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/photo_view.fxml"));
            Parent root = loader.load();

            Stage secondStage = new Stage();
            secondStage.setTitle("Photo View");
            secondStage.initOwner(this.albumStage);

            Scene scene = new Scene(root);
            secondStage.setScene(scene);
            secondStage.setResizable(false);

            //if(photoList.getSelectionModel().getSelectedItems().isEmpty()) {
            ViewPhotoController viewPhotoController = loader.getController();
            viewPhotoController.setViewPhotoStage(secondStage);
            viewPhotoController.setPhoto(photoList.getSelectionModel().getSelectedItems().get(0));
            viewPhotoController.setPhotos(photos);
            viewPhotoController.setAlbum(album);
            secondStage.showAndWait();
            //}

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
