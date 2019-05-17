package sample.Controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;


public class MainController {

    @FXML
    public Button analysis;
    @FXML
    public TextField filePath;
    @FXML
    public Button fileChooser;
    @FXML
    public ChoiceBox categoryList;
    @FXML
    public Label skalarResult, cosResult, persentResult, textResult;

    @FXML
    public VBox adminSettings;
    @FXML
    public Button buttonUsers, buttonGenres, buttonSettings;

    String desktopPath = System.getProperty("user.home") + "\\" + "Desktop";
    private String pathFileDetectives = desktopPath + "\\detectives\\";
    private String pathFileAnother = desktopPath + "\\another\\";

    private Stage stage;
    @FXML
    private Button testButton;

    @FXML
    private AnchorPane AnchorPane;

    @FXML
    public void initialize() {
        buttonGenres.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                try {
                    openGenres();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        buttonSettings.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                try {
                    openSettings();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        buttonUsers.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                try {
                    openUsers();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });


    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void openGenres() throws IOException {

      /*  Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Test Connection");

        // Header Text: null
        alert.setHeaderText(null);
        alert.setContentText("Второе окошко открылось и все!!");

        alert.showAndWait();
*/

       /*  FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainController.class.getResource("sample/main.fxml"));*/
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/genres.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        stage.setTitle("Жанры");

        Scene scene = new Scene(page);
        stage.setScene(scene);

        //  Передаём адресата в контроллер.
        GenresController controller = loader.getController();
        controller.setStage(stage);
        stage.show();

    }

    private void openUsers() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/users.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        stage.setTitle("Пользователи");

        Scene scene = new Scene(page);
        stage.setScene(scene);

        //  Передаём адресата в контроллер.
        UsersController controller = loader.getController();
        controller.setStage(stage);
        stage.show();

    }

    private void openSettings() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/settings.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        stage.setTitle("Настроечки");

        Scene scene = new Scene(page);
        stage.setScene(scene);

        //  Передаём адресата в контроллер.
        SettingsController controller = loader.getController();
        controller.setStage(stage);
        stage.show();

    }


}
