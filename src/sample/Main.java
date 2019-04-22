package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        //       Parent root = FXMLLoader.load(getClass().getResource("authorization.fxml"));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("authorization.fxml"));
        Parent root = (Parent) loader.load();
        AuthorizationController controller = (AuthorizationController) loader.getController();
        controller.setStageAndSetupListeners(primaryStage);

        // Create MenuBar
        MenuBar menuBar = new MenuBar();

        // Create menus
        Menu trainingSetMenu = new Menu("Обучающая выборка");
        Menu settingsMenu = new Menu("Настройки");
        Menu usersv = new Menu("Пользователи");
        Menu helpMenu = new Menu("Помощь");
        Menu exitMenu = new Menu("Выход");

        // Create MenuItems
        MenuItem genreTrainingSetItem = new MenuItem("Жанры");
        MenuItem TrainingSetItem = new MenuItem("Статистика");
        MenuItem openFileItem = new MenuItem("Статистика");
        MenuItem exitItem = new MenuItem("Exit");

        // Set Accelerator for Exit MenuItem.
        exitItem.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));

        // When user click on the Exit item.
        exitItem.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });

        // Add menuItems to the Menus
        trainingSetMenu.getItems().addAll(newItem, openFileItem, exitItem);

        // Add Menus to the MenuBar
        menuBar.getMenus().addAll(fileMenu, editMenu, helpMenu);

        Scene scene = new Scene(new VBox(), 800, 500);

        ((VBox) scene.getRoot()).getChildren().addAll(menuBar);

        primaryStage.setTitle("Прототип программы сравнения близости текстов");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);

    }


}
