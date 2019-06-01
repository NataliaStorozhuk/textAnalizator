package sample.Controllers;


import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class AuthorizationController {


    String desktopPath = System.getProperty("user.home") + "\\" + "Desktop";

    private Stage stage;
    @FXML
    private Button enter;

    @FXML
    private TextField labelLogin;

    @FXML
    private PasswordField labelPassword;

    @FXML
    private Label labelError;


    ArrayList<String> fileAfterPorter1;

    @FXML
    private AnchorPane AnchorPane;

    @FXML
    public void initialize() {
        //  stage.getScene().getProperties().
        enter.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                try {
                    authorisation();
                } catch (IOException | NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    private void authorisation() throws IOException, NoSuchAlgorithmException {
        openMainPage();
  /*      String login = labelLogin.getText();
        String password = labelPassword.getText();

        UserService userService = new UserService();
        List<User> users = userService.findAllUsers();

        for (User user : users) {
            //Пользователь есть в системе
            if (user.getLogin().equals(login)) {
                //Если у пользователя пароль подошел

                String hashPassword = String.valueOf((labelPassword.getText()).hashCode());
                if (user.getPassword().equals(hashPassword)) {
                    //Дальше в зависимости от прав пользователя открывается админка или юзерка
                    if (user.getRights().equals(Boolean.TRUE)) {
                        openAdminPage();
                    } else {
                        openMainPage();
                    }
                } else {
                    labelError.setText("Пароль введен неверно!");

                }
                break;
            } else {
                labelError.setText("Данный пользователь не зарегистрирован!");
            }

        }*/
    }

    private void openAdminPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        stage.setTitle("Анализ текста");

        Scene scene = new Scene(page);
        stage.setScene(scene);

        // Передаём адресата в контроллер.
        MainController controller = loader.getController();
        controller.setStage(stage);
        stage.show();
    }

    private void openMainPage() throws IOException {


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        stage.setTitle("Анализ текста");

        Scene scene = new Scene(page);
        stage.setScene(scene);

        // Передаём адресата в контроллер.
        MainController controller = loader.getController();
        controller.setStage(stage);
        //  controller.adminSettings.setVisible(false);
        stage.show();

    }


    public void setStageAndSetupListeners(Stage primaryStage) {
        stage = primaryStage;
    }

}
