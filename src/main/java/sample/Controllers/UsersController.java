package sample.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.User;

import java.io.IOException;

public class UsersController {


    private Stage stage;

    final VBox vbox = new VBox();

    final TableView<User> table = new TableView<User>();
    final Label label = new Label("Пользователи");

    final Label actionTaken = new Label();
    final TextField newLogin = new TextField();
    final TextField newPassword = new TextField();
    final CheckBox newRights = new CheckBox();
    final Button newAdd = new Button();
    final Button back = new Button();
    private ObservableList<User> usersData = FXCollections.observableArrayList();

    private final Image deleteImage = new Image(
            "http://icons.iconarchive.com/icons/itweek/knob-toolbar/32/Knob-Cancel-icon.png"
    );

    @FXML
    public void initialize() {
        back.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                try {
                    openAdminPage();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

     /*   addUser.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("А мы тут пользователя добавили");

                // Header Text: null
                alert.setHeaderText(null);
                alert.setContentText("User");

                alert.showAndWait();


            }
        });*/

    }


    public void setStage(Stage stage) {
        this.stage = stage;
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
}
