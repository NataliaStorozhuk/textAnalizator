package sample.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sample.DBModels.Genre;
import sample.DTO.GenreLexema;

import java.io.IOException;

public class LexemesController {

    private Stage stage;
    private Genre currentGenre;

    private final VBox vbox = new VBox();

    private final TableView<GenreLexema> table = new TableView<>();
    private final Label label = new Label("Лексемы жанра");
    private final Button back = new Button();

    private ObservableList<GenreLexema> genreData = FXCollections.observableArrayList();

    private final Image backImage = new Image(
            "http://icons.iconarchive.com/icons/itweek/knob-toolbar/32/Knob-Snapback-icon.png"
    );


    @FXML
    public void initialize() {

        label.setFont(new Font("Arial", 20));
        initData();

        drawTable();

        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(label, table, back);
        VBox.setVgrow(table, Priority.ALWAYS);


        final ImageView buttonGraphicBack = new ImageView();
        buttonGraphicBack.setImage(backImage);
        back.setGraphic(buttonGraphicBack);

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


    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setScene(new Scene(vbox));

    }

    private void openGenres() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/genres.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        stage.setTitle("Жанры");

        Scene scene = new Scene(page, 800, 600);
        stage.setScene(scene);

        // Передаём адресата в контроллер.
        GenresController controller = loader.getController();
        controller.setStage(stage);
        stage.show();


    }

    private void drawTable() {


        table.setEditable(true);

        TableColumn lexemaColumn = new TableColumn("Лексема");
        TableColumn wColumn = new TableColumn("W");

        // устанавливаем тип и значение которое должно хранится в колонке
        lexemaColumn.setCellValueFactory(new PropertyValueFactory<GenreLexema, String>("lexema"));
        wColumn.setCellValueFactory(new PropertyValueFactory<GenreLexema, String>("w"));

        table.setItems(genreData);

        table.getColumns().addAll(lexemaColumn, wColumn);
    }


    public void setGenre(Genre person) {
        currentGenre = person;
    }

    // подготавливаем данные из базы данных
    private void initData() {

        genreData.clear();
        //TODO дописать нормально тут инициализацию

        genreData.add(new GenreLexema("ntcn 1", 0.2));
        genreData.add(new GenreLexema("тест 1", 0.3));
        genreData.add(new GenreLexema("ntcn 2313", 0.444));
        genreData.add(new GenreLexema("ntcn 3", 0.113));
        /*SessionFactory sessionFactory = new Configuration().configure()
                .buildSessionFactory();
        Session session = sessionFactory.openSession();
        @SuppressWarnings("unchecked")
        List<Book> Books = session.createQuery("FROM Book book where book.idGenre =:paramGenre").setParameter("paramGenre", currentGenre).list();
        booksData.addAll(Books);
        session.close();*/
    }

}
