package pl.lukaszgo.actions;

import java.io.File;
import java.io.IOException;

import pl.lukaszgo.constants.Settings;
import pl.lukaszgo.controllers.Controller;

/**
 * Created by lukaszgo on 2017-01-18.
 */

public class CreateProjectAction extends Thread {

    private String omsSchemaRemapValue;

    public CreateProjectAction(String omsSchemaRemapValue) {
        super("CreateProjectAction thread");
        this.omsSchemaRemapValue = omsSchemaRemapValue;
    }

    @Override
    public void run() {
        createNewOmsProjectChangerFile();
    }

    private void createNewOmsProjectChangerFile() {
        try {
            File file = new File(buildNewProjectChangerFilePath());
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String buildNewProjectChangerFilePath() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Settings.PROJECT_CHANGER_PATH).append("\\").append(omsSchemaRemapValue);

        return stringBuilder.toString();
    }
}
