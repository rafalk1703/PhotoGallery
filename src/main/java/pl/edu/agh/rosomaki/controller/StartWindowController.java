package pl.edu.agh.rosomaki.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pl.edu.agh.rosomaki.utils.MailSender;

public class StartWindowController {

    @FXML
    private TextField mailAddress;

    @FXML
    private Button okButton;

    private Stage startStage;

    public void setStartStage(Stage startStage) {
        this.startStage = startStage;
    }

    public void enterNameAction(){
        MailSender.setMailAddress(mailAddress.getText());
        startStage.close();
    }
}
