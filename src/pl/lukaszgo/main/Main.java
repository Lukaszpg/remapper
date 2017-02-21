package pl.lukaszgo.main;

import pl.lukaszgo.constants.Settings;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by lukaszgo on 2016-09-14.
 */

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource(Settings.MAIN_FXML));
        primaryStage.setTitle(Settings.APP_TITLE);
        primaryStage.setScene(new Scene(root, Settings.MAIN_WINDOW_WIDTH, Settings.MAIN_WINDOW_HEIGHT));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
