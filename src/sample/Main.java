package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
 //       Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = (Parent)loader.load();
        Controller controller = (Controller) loader.getController();
         controller.setStageAndSetupListeners(primaryStage);

        primaryStage.setTitle("Прототип программы сравнения близости текстов");
        primaryStage.setScene(new Scene(root, 1000, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);

    }


}
