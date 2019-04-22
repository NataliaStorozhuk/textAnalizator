package sample;


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
import java.util.ArrayList;

//import java.awt.*;

public class AuthorizationController {


    String desktopPath = System.getProperty("user.home") + "\\" + "Desktop";

    private Stage stage;
    @FXML
    private Button enter;

    @FXML
    private Label labelLogin, labelPassword, labelError;


    ArrayList<String> fileAfterPorter1;

    @FXML
    private AnchorPane AnchorPane;

    @FXML
    public void initialize() {
        enter.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
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

    private void openFile1() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainController.class.getResource("main.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        // Создаём диалоговое окно Stage.
        //Stage dialogStage = new Stage();
        stage.setTitle("Edit Person");
        //  dialogStage.initModality(Modality.NONE);
        //  dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(page);
        stage.setScene(scene);

        // Передаём адресата в контроллер.
        MainController controller = loader.getController();
        controller.setDialogStage(stage);

        // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
        stage.show();


    }


    public void setStageAndSetupListeners(Stage primaryStage) {

        stage = primaryStage;
    }

}
