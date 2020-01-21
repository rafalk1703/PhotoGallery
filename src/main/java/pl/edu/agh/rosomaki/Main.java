package pl.edu.agh.rosomaki;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.edu.agh.rosomaki.controller.MainWindowController;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("view/main_window.fxml"));

        Parent root = loader.load();

        MainWindowController mainWindowController = loader.getController();
        mainWindowController.setMainStage(stage);
        stage.setTitle("Image Gallery");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
