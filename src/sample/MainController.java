package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

//import java.awt.*;

public class MainController {


    String desktopPath = System.getProperty("user.home") + "\\" + "Desktop";
    private String pathFileDetectives = desktopPath + "\\detectives\\";
    private String pathFileAnother = desktopPath + "\\another\\";
    Analyzer analyzer = new Analyzer();

    private Stage stage;
    @FXML
    private Button testButton;

    @FXML
    private AnchorPane AnchorPane;

    @FXML
    public void initialize() {
        testButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                try {
                    openFile1();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    private void openFile1() throws IOException {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Test Connection");

        // Header Text: null
        alert.setHeaderText(null);
        alert.setContentText("Второе окошко открылось и все!!");

        alert.showAndWait();

    }


    public void setStageAndSetupListeners(Stage primaryStage) {

        stage = primaryStage;
    }

}
