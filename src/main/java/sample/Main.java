package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Controllers.AuthorizationController;

public class Main extends Application {

    //  http://easy-code.ru/lesson/css-javafx-form
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../resources/authorization.fxml"));
        Parent root = (Parent) loader.load();
        AuthorizationController controller = (AuthorizationController) loader.getController();
        controller.setStageAndSetupListeners(primaryStage);

        primaryStage.setTitle("Прототип программы сравнения близости текстов");
        Scene scene = new Scene(root, 800, 500);

        //    scene.getStylesheets().add((getClass().getResource("/css/style.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();


        /*
        *
        * Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        Scene scene = new Scene(root);

        stage.setScene(scene);

        stage.setTitle("Hello JavaFX");
        stage.setWidth(250);
        stage.setHeight(200);

        stage.show();
        */
    }


    public static void main(String[] args) {
        launch(args);

    }


}
