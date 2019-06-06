package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Controllers.AuthorizationController;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/authorization.fxml"));
        Parent root = (Parent) loader.load();
        AuthorizationController controller = (AuthorizationController) loader.getController();
        controller.setStageAndSetupListeners(primaryStage);

        primaryStage.setTitle("Прототип программы сравнения близости текстов");
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setScene(scene);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);

    }


}
