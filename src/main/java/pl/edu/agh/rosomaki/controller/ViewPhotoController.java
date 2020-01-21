package pl.edu.agh.rosomaki.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import pl.edu.agh.rosomaki.model.Album;
import pl.edu.agh.rosomaki.model.Photo;
import pl.edu.agh.rosomaki.utils.PhotoUtils;

import java.util.List;

public class ViewPhotoController {

    private Stage viewPhotoStage;

    private Photo photo;

    private Album album;

    @FXML
    private ImageView picture;

    @FXML
    private Label label;

    @FXML
    private Button closeWindowButton;

    @FXML
    private Button deletePhotoButton;

    private List<Photo> photos;

    public void setAlbum(Album album) {
        this.album = album;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public void setViewPhotoStage(Stage viewPhotoStage) {
        this.viewPhotoStage = viewPhotoStage;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
        this.picture.setImage(PhotoUtils.photoToFXImage(photo));
        centerImage(picture);
        this.label.setText(photo.getName());
    }

    @FXML
    private void closeWindowAction() {
        viewPhotoStage.close();
    }

    @FXML
    private void deletePhotoAction() {
        photos.remove(photo);
        album.removePhoto(photo);
        viewPhotoStage.close();
    }

    public void centerImage(ImageView imageView) {
        Image img = imageView.getImage();
        if (img != null) {
            double w = 0;
            double h = 0;

            double ratioX = imageView.getFitWidth() / img.getWidth();
            double ratioY = imageView.getFitHeight() / img.getHeight();

            double reducCoeff = 0;
            if (ratioX >= ratioY) {
                reducCoeff = ratioY;
            } else {
                reducCoeff = ratioX;
            }

            w = img.getWidth() * reducCoeff;
            h = img.getHeight() * reducCoeff;

            imageView.setX((imageView.getFitWidth() - w) / 2);
            imageView.setY((imageView.getFitHeight() - h) / 2);

        }
    }
}
