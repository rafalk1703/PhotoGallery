package pl.edu.agh.rosomaki.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pl.edu.agh.rosomaki.model.Album;
import pl.edu.agh.rosomaki.model.Photo;
import pl.edu.agh.rosomaki.model.Tag;
import pl.edu.agh.rosomaki.utils.MailSender;
import pl.edu.agh.rosomaki.utils.PhotoUtils;

public class EditPhotoController {

    @FXML
    private Photo photo;

    @FXML
    private TextField tags;

    @FXML
    private TextField photoPath;

    @FXML
    private TextField name;

    @FXML
    private TextField where;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    private Stage editPhotoStage;

    private Album album;

    private ObservableList<Photo> photos;

    public void setPhoto(Photo photo) {
        this.photo = photo;
        this.name.setText(photo.getName());
        this.tags.setText(photo.getTagsString());
        this.where.setText(photo.getLocation());
        this.datePicker.setValue(photo.getDate());
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public void setEditPhotoStage(Stage editPhotoStage) {
        this.editPhotoStage = editPhotoStage;
    }

    public void setPhotos(ObservableList<Photo> photos) {
        this.photos = photos;
    }

    @FXML
    private void confirmButtonAction() throws CloneNotSupportedException {
        photo.getTags().forEach(Tag::delete);
        photo.setName(name.getText());
        photo.setDate(datePicker.getValue());
        photo.setLocation(where.getText());
        photo.setTags(PhotoUtils.createTags(tags.getText()));
        photo.update();
        updateList(photo);
        editPhotoStage.close();

    }

    @FXML
    private void cancelButtonAction() {
        editPhotoStage.close();
    }


    private void updateList(Photo p) throws CloneNotSupportedException {
        Photo tmp = (Photo) p.cloneButPublic(p);
        photos.add(tmp);
        album.addPhoto(tmp);
        album.removePhoto(tmp);
        photos.remove(tmp);
    }
}

