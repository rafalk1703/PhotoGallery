package pl.edu.agh.rosomaki.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;
import pl.edu.agh.rosomaki.model.Album;
import pl.edu.agh.rosomaki.model.Photo;
import pl.edu.agh.rosomaki.model.Tag;
import pl.edu.agh.rosomaki.utils.MailSender;
import pl.edu.agh.rosomaki.utils.PhotoUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class AddPhotoController {

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
    private Button addButton;

    @FXML
    private Button cancelButton;

    private Stage addPhotoStage;

    private Album album;

    private ObservableList<Photo> photos;

    public void setAlbum(Album album) {
        this.album = album;
    }

    public void setAddPhotoStage(Stage addPhotoStage) {
        this.addPhotoStage = addPhotoStage;
    }

    public void setPhotos(ObservableList<Photo> photos) {
        this.photos = photos;
    }


    public void setTagSuggestions(){
        String[] tagsS =
        photos.stream()
                .flatMap(p->p.getTags().stream())
                .map(Tag::getName)
                .distinct()
                .toArray(String[]::new);

        TextFields.bindAutoCompletion(tags, tagsS);
    }

    public void setLocationSuggestions(){
        String[] locationsS =
        photos.stream()
                .map(Photo::getLocation)
                .distinct()
                .toArray(String[]::new);

        TextFields.bindAutoCompletion(where, locationsS);
    }

    @FXML
    private void addButtonAction() {


        Photo photo = Photo.builder()
                .name(name.getText())
                .path(photoPath.getText())
                .image(PhotoUtils.loadImage(photoPath.getText()))
                .date(datePicker.getValue())
                .location(where.getText())
                .tags(PhotoUtils.createTags(tags.getText()))
                .build();
        photos.add(photo);
        album.addPhoto(photo);
        photo.persist(album.getId());
        addPhotoStage.close();

        MailSender.send();
    }

    @FXML
    private void cancelButtonAction() {
        addPhotoStage.close();
    }
}
