package sample.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sample.DBModels.Genre;
import sample.DTO.GenreLexema;

import java.io.IOException;

public class LexemesController extends ControllerConstructor {

    private Stage stage;
    private Genre currentGenre;

    private final VBox vbox = new VBox();

    private final TableView<GenreLexema> table = new TableView<>();
    private final Label label = new Label("Лексемы жанра");
    private final Button back = new Button();

    private ObservableList<GenreLexema> lexemesData = FXCollections.observableArrayList();



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

        back.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {

            try {
                openGenrePage(stage);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

    }


    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setScene(new Scene(vbox));

    }


    private void drawTable() {


        table.setEditable(true);

        TableColumn lexemaColumn = new TableColumn("Лексема");
        TableColumn wColumn = new TableColumn("W");

        // устанавливаем тип и значение которое должно хранится в колонке
        lexemaColumn.setCellValueFactory(new PropertyValueFactory<GenreLexema, String>("lexema"));
        wColumn.setCellValueFactory(new PropertyValueFactory<GenreLexema, String>("w"));

        table.setItems(lexemesData);

        table.getColumns().addAll(lexemaColumn, wColumn);
    }


    public void setGenre(Genre person) {
        currentGenre = person;
    }

    // подготавливаем данные из базы данных
    private void initData() {

        lexemesData.clear();
        //TODO дописать нормально тут инициализацию

        lexemesData.add(new GenreLexema("ntcn 1", 0.2));
        lexemesData.add(new GenreLexema("тест 1", 0.3));
        lexemesData.add(new GenreLexema("ntcn 2313", 0.444));
        lexemesData.add(new GenreLexema("ntcn 3", 0.113));
        /*SessionFactory sessionFactory = new Configuration().configure()
                .buildSessionFactory();
        Session session = sessionFactory.openSession();
        @SuppressWarnings("unchecked")
        List<Book> Books = session.createQuery("FROM Book book where book.idGenre =:paramGenre").setParameter("paramGenre", currentGenre).list();
        booksData.addAll(Books);
        session.close();*/
    }

}
