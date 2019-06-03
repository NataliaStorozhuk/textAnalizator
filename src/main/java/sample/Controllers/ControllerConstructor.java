package sample.Controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.DBModels.Genre;

import java.io.IOException;

class ControllerConstructor {

    final String applicationPath = "C:\\Users\\Natalia\\Desktop\\analyzer\\";
    final Image booksImage = new Image(
            "http://icons.iconarchive.com/icons/itweek/knob-toolbar/32/Knob-Favorite-icon.png"
    );

    final Image lexemsImage = new Image(
            "http://icons.iconarchive.com/icons/itweek/knob-toolbar/32/Knob-Shuffle-On-icon.png"
    );

    final Image okImage = new Image(
            "http://icons.iconarchive.com/icons/itweek/knob-toolbar/32/Knob-Valid-Green-icon.png"
    );

    final Image reloadImage = new Image(
            "http://icons.iconarchive.com/icons/itweek/knob-toolbar/32/Knob-Play-Green-icon.png"
    );

    final Image deleteImage = new Image(
            "http://icons.iconarchive.com/icons/itweek/knob-toolbar/32/Knob-Cancel-icon.png"
    );

    final Image addImage = new Image(
            "http://icons.iconarchive.com/icons/itweek/knob-toolbar/32/Knob-Add-icon.png"
    );

    final Image backImage = new Image(
            "http://icons.iconarchive.com/icons/itweek/knob-toolbar/32/Knob-Snapback-icon.png"
    );

    ImageView imageButtonAdd() {
        final ImageView buttonGraphicAdd = new ImageView();
        buttonGraphicAdd.setImage(addImage);
        return buttonGraphicAdd;
    }

    ImageView imageButtonBack() {
        final ImageView buttonGraphicBack = new ImageView();
        buttonGraphicBack.setImage(backImage);
        return buttonGraphicBack;
    }

    ImageView imageButtonOk() {
        final ImageView buttonGraphicOk = new ImageView();
        buttonGraphicOk.setImage(okImage);
        return buttonGraphicOk;
    }

    void openGenrePage(Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/genres.fxml"));
        AnchorPane page = loader.load();

        stage.setTitle("Жанры");

        Scene scene = new Scene(page, 800, 600);
        stage.setScene(scene);

        GenresController controller = loader.getController();
        controller.setStage(stage);
        stage.show();


    }

    void openAdminMainPage(Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        stage.setTitle("Анализ текста");

        Scene scene = new Scene(page, 800, 600);
        stage.setScene(scene);

        // Передаём адресата в контроллер.
        MainController controller = loader.getController();
        controller.setStage(stage);
        controller.adminSettings.setVisible(true);
        stage.show();

    }

    void openBookPage(Genre person, Stage stage) {
        //  Передаём адресата в контроллер.
        BooksController controller = new BooksController();
        controller.setGenre(person);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/books.fxml"));
        loader.setController(controller);
        AnchorPane page = null;
        try {
            page = (AnchorPane) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.setTitle("Книги жанра");

        Scene scene = new Scene(page, 800, 600);
        stage.setScene(scene);
        controller.setStage(stage);
        stage.show();
    }

    void openLexemesPage(Genre person, Stage stage) {
        LexemesController controller = new LexemesController();
        controller.setGenre(person);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/genreInfo.fxml"));
        loader.setController(controller);
        AnchorPane page = null;
        try {
            page = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.setTitle("Лексемы жанра");

        Scene scene = new Scene(page, 800, 600);
        stage.setScene(scene);
        controller.setStage(stage);
        stage.show();
    }


    void openSettings(Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/settings.fxml"));
        AnchorPane page = loader.load();

        stage.setTitle("Настройки");

        Scene scene = new Scene(page, 800, 600);
        stage.setScene(scene);

        //  Передаём адресата в контроллер.
        SettingsController controller = loader.getController();
        controller.setStage(stage);
        stage.show();

    }

    void openUsers(Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/users.fxml"));
        AnchorPane page = loader.load();

        stage.setTitle("Пользователи");

        Scene scene = new Scene(page, 800, 600);
        stage.setScene(scene);

        //  Передаём адресата в контроллер.
        UsersController controller = loader.getController();
        controller.setStage(stage);
        stage.show();

    }

}


