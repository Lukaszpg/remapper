package pl.lukaszgo.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import pl.lukaszgo.actions.CreateProjectAction;
import pl.lukaszgo.constants.Settings;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import pl.lukaszgo.actions.ImportAction;

/**
 * Created by lukaszgo on 2016-09-14.
 */

public class Controller {

    @FXML
    private TextField logonDetails;

    @FXML
    private TextField mainSchema;

    @FXML
    private TextField mainSchemaRemap;

    @FXML
    private TextField editSchema;

    @FXML
    private TextField editSchemaRemap;

    @FXML
    private TextField omsSchema;

    @FXML
    private TextField omsSchemaRemap;

    @FXML
    private TextField file;

    @FXML
    private Button startButton;

    @FXML
    private CheckBox editManualCheckbox;

    @FXML
    private CheckBox omsManualCheckbox;

    private List<String> commands;

    private Preferences preferences;

    @FXML
    private void handleStartButton(ActionEvent event) {
        if (validateTextFields()) {
            prepareCommand();
            prepareAndExecuteCreateProjectAction();
            prepareAndExecuteImportAction();
        }
    }

    @FXML
    private void initialize() {
        commands = new ArrayList<>();
        initializePreferences();
        addLogonDetailsTextFieldFocusListener();
        addMainSchemaTextFieldFocusListener();
        addMainRemapSchemaTextFieldFocusListener();
        addManualCheckboxListeners();
        setLogonDetailsFromPreferences();
    }

    private void initializePreferences() {
        preferences = Preferences.userRoot().node(this.getClass().getName());
    }

    private void addLogonDetailsTextFieldFocusListener() {
        logonDetails.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {
                if (!isFieldEmpty(logonDetails)) {
                    preferences.put(Settings.LOGON_DETAILS_PREFERENCE_NAME, logonDetails.getText());
                }
            }
        });
    }

    private void addMainSchemaTextFieldFocusListener() {
        mainSchema.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
            if (!newPropertyValue) {

                if (!editManualCheckbox.isSelected()) {
                    editSchema.setText(appendSuffixToTextIfTextFieldIsNotEmpty(mainSchema, "_e"));
                }

                if (!omsManualCheckbox.isSelected()) {
                    omsSchema.setText(appendSuffixToTextIfTextFieldIsNotEmpty(mainSchema, "_oms"));
                }
            }
        });
    }

    private void addMainRemapSchemaTextFieldFocusListener() {
        mainSchemaRemap.focusedProperty()
                       .addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
                           if (!newPropertyValue) {
                               editSchemaRemap.setText(appendSuffixToTextIfTextFieldIsNotEmpty(mainSchemaRemap, "_e"));
                               omsSchemaRemap.setText(appendSuffixToTextIfTextFieldIsNotEmpty(mainSchemaRemap, "_oms"));
                           }
                       });
    }

    private void addManualCheckboxListeners() {
        editManualCheckbox.selectedProperty()
                          .addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
                              if (editManualCheckbox.isSelected()) {
                                  editSchema.setDisable(false);
                                  editSchema.setEditable(true);
                              } else {
                                  editSchema.setDisable(true);
                                  editSchema.setEditable(false);
                              }
                          });

        omsManualCheckbox.selectedProperty()
                         .addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
                             if (omsManualCheckbox.isSelected()) {
                                 omsSchema.setDisable(false);
                                 omsSchema.setEditable(true);
                             } else {
                                 omsSchema.setDisable(true);
                                 omsSchema.setEditable(false);
                             }
                         });
    }

    private void setLogonDetailsFromPreferences() {
        logonDetails.setText(preferences.get(Settings.LOGON_DETAILS_PREFERENCE_NAME, ""));
    }

    private boolean validateTextFields() {
        if (isFieldEmpty(logonDetails) || isFieldEmpty(mainSchema) || isFieldEmpty(editSchema) || isFieldEmpty(omsSchema) || isFieldEmpty(
                mainSchemaRemap) || isFieldEmpty(editSchemaRemap) || isFieldEmpty(omsSchemaRemap) || isFieldEmpty(file)) {
            showEmptyFieldsError();

            return false;
        }

        return true;
    }

    private void prepareCommand() {
        commands.add("cmd.exe");
        commands.add("/C");
        commands.add("start");
        commands.add("impdp");
        commands.add(logonDetails.getText());
        commands.add(buildRemapSchemaString());
        commands.add(buildDumpfileString());
        commands.add("logfile=log.log");

        if (Settings.DEBUG) {
            System.out.println("Debug commands: " + commands.toString());
        }
    }

    private String buildRemapSchemaString() {
        StringBuilder builder = new StringBuilder();

        builder.append("remap_schema=").append(mainSchema.getText()).append(":").append(mainSchemaRemap.getText()).append(",")
               .append(editSchema.getText()).append(":").append(editSchemaRemap.getText()).append(",").append(omsSchema.getText()).append(":")
               .append(omsSchemaRemap.getText());

        return builder.toString();
    }

    private String buildDumpfileString() {
        StringBuilder builder = new StringBuilder();
        builder.append("dumpfile=").append(file.getText());

        return builder.toString();
    }

    private void prepareAndExecuteCreateProjectAction() {
        CreateProjectAction createProjectAction = new CreateProjectAction(omsSchemaRemap.getText());
        createProjectAction.start();
    }

    private void prepareAndExecuteImportAction() {
        ImportAction action = new ImportAction(commands);
        action.start();
    }

    public void disableStartButton() {
        startButton.setDisable(true);
    }

    public void disableAllFields() {
        logonDetails.setDisable(true);
        mainSchema.setDisable(true);
        mainSchemaRemap.setDisable(true);
        editSchema.setDisable(true);
        editSchemaRemap.setDisable(true);
        omsSchema.setDisable(true);
        omsSchemaRemap.setDisable(true);
        file.setDisable(true);
        editManualCheckbox.setDisable(true);
        omsManualCheckbox.setDisable(true);
    }

    public void enableStartButton() {
        startButton.setDisable(false);
    }

    private String appendSuffixToTextIfTextFieldIsNotEmpty(TextField textField, String suffix) {
        StringBuilder stringBuilder = new StringBuilder();

        if (!isFieldEmpty(textField)) {
            stringBuilder.append(textField.getText()).append(suffix);
        }

        return stringBuilder.toString();
    }

    private boolean isFieldEmpty(TextField textField) {
        return textField.getLength() == 0;
    }

    private void showEmptyFieldsError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Coś poszło nie tak :(");
        alert.setHeaderText("Oooops");
        alert.setContentText("Koleżanko lub kolego, wszystkie pola muszą być wypełnione!");
        alert.showAndWait();
    }

    public String getOmsSchemaRemapValue() {
        return omsSchemaRemap.getText();
    }
}
