package sample.Controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.Analyzer;

import java.io.IOException;

//import java.awt.*;

public class MainController {


    public Button analysis;
    public TextField filePath;
    public Button fileChooser;
    public ChoiceBox categoryList;
    public Label skalarResult;
    public Label cosResult;
    public Label persentResult;
    public Label textResult;
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


    public void setStage(Stage dialogStage) {
        this.stage = dialogStage;
    }

    private void openFile1() throws IOException {

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


        //Stage stage = new Stage();
      /*  stage.setTitle("Анализ текста");

        Scene scene = new Scene(page);
        stage.setScene(scene);

        // Передаём адресата в контроллер.
        GenresController controller = loader.getController();
        controller.setStage(stage);*/
        //     stage.show();

    }


}
