package sample.Controllers;


import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.DBModels.User;
import sample.Services.UserService;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

//import java.awt.*;

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
                    openFile1();
                } catch (IOException | NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    private void openFile1() throws IOException, NoSuchAlgorithmException {

        String login = labelLogin.getText();
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
                        openUserPage();
                    }
                } else {
                    labelError.setText("Пароль введен неверно!");

                }
                break;
            } else {
                labelError.setText("Данный пользователь не зарегистрирован!");
            }

        }
      /*  FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainController.class.getResource("sample/main.fxml"));*/
     /*   FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));

        AnchorPane page = (AnchorPane) loader.load();

        //Stage dialogStage = new Stage();
        stage.setTitle("Анализ текста");

        Scene scene = new Scene(page);
        stage.setScene(scene);

        // Передаём адресата в контроллер.
        MainController controller = loader.getController();
        controller.setStage(stage);
        stage.show();

*/
    }

    private void openAdminPage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("тут админская страница");

        // Header Text: null
        alert.setHeaderText(null);
        alert.setContentText("А тут тоже админский текст");

        alert.showAndWait();

    }

    private void openUserPage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("User");

        // Header Text: null
        alert.setHeaderText(null);
        alert.setContentText("User");

        alert.showAndWait();


    }


    public void setStageAndSetupListeners(Stage primaryStage) {

        stage = primaryStage;
    }

}
