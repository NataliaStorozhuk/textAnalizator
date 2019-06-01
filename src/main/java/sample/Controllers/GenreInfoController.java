package sample.Controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
import sample.DBModels.Genre;
import sample.DTO.GenreLexema;

import java.io.IOException;

public class GenreInfoController {

    String desktopPath = System.getProperty("user.home") + "\\" + "Desktop";

    private Stage stage;
    private Genre currentGenre;

    private final VBox vbox = new VBox();

    private final TableView<GenreLexema> table = new TableView<>();
    private final Label label = new Label("Лексемы жанра");

    private final TextField newLexema = new TextField();
    private final TextField newW = new TextField();
    private final Button newAdd = new Button();
    private final Button back = new Button();

    private ObservableList<GenreLexema> genreData = FXCollections.observableArrayList();

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


    public void setStage(Stage stage) {
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

        newLexema.setPromptText("Лексема");
        newW.setPromptText("Вес");

        newAdd.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {

        /*    BookService bookService = new BookService();
            //TODO тут в пути не путь исходника из строки, а путь уже к лексемам в фс! Будь добра, добавь обработчик!
            Book newBook = new Book(newBookName.getText(), newPath.getText(), false, false);
            bookService.saveBook(newBook);
            booksData.add(newBook);
            table.refresh();*/

            genreData.add(new GenreLexema(newLexema.getText(), Double.parseDouble(newW.getText())));
            table.refresh();
        });

        hBox.getChildren().addAll(back, newLexema, newW, newAdd);
        hBox.setAlignment(Pos.CENTER);
        return hBox;
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
        TableColumn<GenreLexema, GenreLexema> deleteColumn = new TableColumn<>("Удалить");

        // устанавливаем тип и значение которое должно хранится в колонке
        lexemaColumn.setCellValueFactory(new PropertyValueFactory<GenreLexema, String>("lexema"));
        wColumn.setCellValueFactory(new PropertyValueFactory<GenreLexema, String>("w"));

        deleteColumnCreator(deleteColumn);


        table.setItems(genreData);

        table.getColumns().addAll(lexemaColumn, wColumn, deleteColumn);
    }

    private void deleteColumnCreator(TableColumn<GenreLexema, GenreLexema> deleteColumn) {
        deleteColumn.setCellValueFactory(features -> new ReadOnlyObjectWrapper(features.getValue()));


        deleteColumn.setCellFactory(new Callback<TableColumn<GenreLexema, GenreLexema>,
                TableCell<GenreLexema, GenreLexema>>() {
            @Override
            public TableCell<GenreLexema, GenreLexema> call(TableColumn<GenreLexema,
                    GenreLexema> deleteColumn) {
                return new TableCell<GenreLexema, GenreLexema>() {
                    final ImageView buttonGraphic = new ImageView();
                    final Button button = new Button();

                    {
                        button.setGraphic(buttonGraphic);
                        button.setMinWidth(50);
                        button.setBorder(null);
                    }

                    @Override
                    public void updateItem(final GenreLexema person, boolean empty) {
                        deleteLexema(person, empty);
                        table.setItems(genreData);
                    }

                    private void deleteLexema(GenreLexema person, boolean empty) {
                        super.updateItem(person, empty);
                        if (person != null) {
                            buttonGraphic.setImage(deleteImage);
                            setGraphic(button);
                            button.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                             /*       BookService BookService = new BookService();
                                    BookService.deleteBook(BookService.findBook(person.getIdBook()));
                                    initData();
                                    table.refresh();
                                }*/
                                    genreData.remove(person);
                                    table.refresh();
                                }
                            });
                        } else {
                            //   button.setText("error");
                        }     //не знаю почему, но тут все время person=null, когда таблицу обновляем
                    }
                };
            }
        });
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
