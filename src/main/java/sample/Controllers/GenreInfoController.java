package sample.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sample.DBModels.Genre;
import sample.DTO.GenreProfile;

import java.io.IOException;

public class GenreInfoController {

    String desktopPath = System.getProperty("user.home") + "\\" + "Desktop";

    private Stage stage;
    private Genre currentGenre;

    private final VBox vbox = new VBox();

    private final TableView<GenreProfile> table = new TableView<>();
    private final Label label = new Label("Лексемы жанра");

    private final TextField newLexema = new TextField();
    private final TextField newW = new TextField();
    private final Button newAdd = new Button();
    private final Button back = new Button();

    private ObservableList<GenreProfile> genreData = FXCollections.observableArrayList();

    private final Image deleteImage = new Image(
            "http://icons.iconarchive.com/icons/itweek/knob-toolbar/32/Knob-Cancel-icon.png"
    );

    private final Image addImage = new Image(
            "http://icons.iconarchive.com/icons/itweek/knob-toolbar/32/Knob-Add-icon.png"
    );

    private final Image backImage = new Image(
            "http://icons.iconarchive.com/icons/itweek/knob-toolbar/32/Knob-Snapback-icon.png"
    );


    @FXML
    public void initialize() {

        label.setFont(new Font("Arial", 20));
        initData();

        drawTable();

        final HBox hBox = getAddBox();

        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(label, table, hBox);
        VBox.setVgrow(table, Priority.ALWAYS);
        back.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                try {
                    openGenres();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }


    public void setStage(Stage stage, int idGenre) {
        this.stage = stage;
        stage.setScene(new Scene(vbox));

    }

    private HBox getAddBox() {
        final HBox hBox = new HBox();

        final ImageView buttonGraphicAdd = new ImageView();
        buttonGraphicAdd.setImage(addImage);
        newAdd.setGraphic(buttonGraphicAdd);

        final ImageView buttonGraphicBack = new ImageView();
        buttonGraphicBack.setImage(backImage);
        back.setGraphic(buttonGraphicBack);

        newLexema.setPromptText("Лекстема");
        newW.setPromptText("Вес");

        newAdd.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {

        /*    BookService bookService = new BookService();
            //TODO тут в пути не путь исходника из строки, а путь уже к лексемам в фс! Будь добра, добавь обработчик!
            Book newBook = new Book(newBookName.getText(), newPath.getText(), false, false);
            bookService.saveBook(newBook);
            booksData.add(newBook);
            table.refresh();*/

            //  genreData.

        });

        hBox.getChildren().addAll(back, newBookName, newPath, newAdd);
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }

    private void openGenres() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/genres.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        stage.setTitle("Жанры");

        Scene scene = new Scene(page);
        stage.setScene(scene);

        // Передаём адресата в контроллер.
        GenresController controller = loader.getController();
        controller.setStage(stage);
        stage.show();


    }

}
