package sample.Controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class BooksController {


    String desktopPath = System.getProperty("user.home") + "\\" + "Desktop";
    private String pathFileDetectives = desktopPath + "\\detectives\\";
    private String pathFileAnother = desktopPath + "\\another\\";

    private Stage stage;
    @FXML
    private Button back;

    @FXML
    private Label booksTest;

    @FXML
    private AnchorPane AnchorPane;

    @FXML
    public void initialize() {
        back.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                try {
                    openGenreInfo();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }


    public void setStage(Stage stage, int idGenre) {
        this.stage = stage;
        booksTest.setText(String.valueOf(idGenre));


    }

    private void openGenreInfo() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/genres.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        stage.setTitle("Информация о жанре");

        Scene scene = new Scene(page);
        stage.setScene(scene);

        // Передаём адресата в контроллер.
        GenresController controller = loader.getController();
        controller.setStage(stage);
        stage.show();


    }


}
