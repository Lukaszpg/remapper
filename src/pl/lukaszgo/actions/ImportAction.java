package pl.lukaszgo.actions;

import java.io.IOException;
import java.util.List;

import pl.lukaszgo.constants.Settings;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import pl.lukaszgo.controllers.Controller;

/**
 * Created by lukaszgo on 2016-09-14.
 */

public class ImportAction extends Thread {

    private ProcessBuilder processBuilder;

    private Process process;

    private List<String> commands;

    private Controller controller;

    public ImportAction(List<String> commands) {
        super("ImportAction thread");
        this.commands = commands;
        getController();
    }

    @Override
    public void run() {
        prepareProcessBuilder();
        disableFieldsAndStartButton();
        execute();
    }

    private void execute() {
        try {
            process = processBuilder.start();
            process.waitFor();
            controller.enableStartButton();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void prepareProcessBuilder() {
        processBuilder = new ProcessBuilder(commands);
        processBuilder.redirectErrorStream(true);
    }

    private void disableFieldsAndStartButton() {
        controller.disableStartButton();
        controller.disableAllFields();
    }

    private void getController() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            Pane p = fxmlLoader.load(getClass().getResource(Settings.MAIN_FXML).openStream());
            controller = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
