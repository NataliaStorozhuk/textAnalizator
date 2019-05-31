package sample.Controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import sample.DBModels.Book;
import sample.DBModels.Genre;
import sample.Services.BookService;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BooksController {


    String desktopPath = System.getProperty("user.home") + "\\" + "Desktop";

    private Stage stage;
    private Genre currentGenre;

    private final VBox vbox = new VBox();

    private final TableView<Book> table = new TableView<>();
    private final Label label = new Label("Книги");


    private final TextField newBookName = new TextField();
    private final TextField newPath = new TextField();
    private final Button newAdd = new Button();
    private final Button back = new Button();
    private ObservableList<Book> booksData = FXCollections.observableArrayList();

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
                    openGenreInfo();
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

        newBookName.setPromptText("Название");
        newPath.setPromptText("Путь к файлу");

        newPath.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                final FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(stage);
                newPath.setText(file.getPath());
            }
        });

        newAdd.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {

            BookService bookService = new BookService();
            //TODO тут в пути не путь исходника из строки, а путь уже к лексемам в фс! Будь добра, добавь обработчик!
            Book newBook = new Book(newBookName.getText(), newPath.getText(), false, false);
            bookService.saveBook(newBook);
            booksData.add(newBook);
            table.refresh();

        });

        hBox.getChildren().addAll(back, newBookName, newPath, newAdd);
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }


    private void drawTable() {


        table.setEditable(true);

        TableColumn idColumn = new TableColumn("ID книги");
        TableColumn nameColumn = new TableColumn("Название");
        TableColumn pathColumn = new TableColumn("Путь");
        TableColumn indexedColumn = new TableColumn("Индексирована");
        TableColumn trainingColumn = new TableColumn("Обуч. выб.");
        TableColumn<sample.DBModels.Book, sample.DBModels.Book> deleteColumn = new TableColumn<>("Удалить");

        // устанавливаем тип и значение которое должно хранится в колонке
        idColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("idBook"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("name"));

        indexedColumn.setCellValueFactory(new PropertyValueFactory<Book, CheckBox>("indexed"));
        indexedColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Book, CheckBox>, ObservableValue<CheckBox>>() {

            @Override
            public ObservableValue<CheckBox> call(
                    TableColumn.CellDataFeatures<sample.DBModels.Book, CheckBox> arg0) {
                Book book = arg0.getValue();

                CheckBox checkBox = new CheckBox();

                checkBox.selectedProperty().setValue(book.getIndexed());

                checkBox.setAlignment(Pos.CENTER);

                return new SimpleObjectProperty<CheckBox>(checkBox);

            }

        });

        trainingColumn.setCellValueFactory(new PropertyValueFactory<Book, CheckBox>("training"));
        trainingColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Book, CheckBox>, ObservableValue<CheckBox>>() {

            @Override
            public ObservableValue<CheckBox> call(
                    TableColumn.CellDataFeatures<sample.DBModels.Book, CheckBox> arg0) {
                Book book = arg0.getValue();

                CheckBox checkBox = new CheckBox();

                checkBox.selectedProperty().setValue(book.getTraining());

                checkBox.setAlignment(Pos.CENTER);

                return new SimpleObjectProperty<CheckBox>(checkBox);

            }

        });

        deleteColumnCreator(deleteColumn);


        table.setItems(booksData);

        table.getColumns().addAll(idColumn, nameColumn, pathColumn, indexedColumn, trainingColumn, deleteColumn);
    }

    private void deleteColumnCreator(TableColumn<Book, Book> deleteColumn) {
        deleteColumn.setCellValueFactory(features -> new ReadOnlyObjectWrapper(features.getValue()));


        deleteColumn.setCellFactory(new Callback<TableColumn<Book, Book>,
                TableCell<Book, Book>>() {
            @Override
            public TableCell<Book, Book> call(TableColumn<Book,
                    Book> deleteColumn) {
                return new TableCell<Book, Book>() {
                    final ImageView buttonGraphic = new ImageView();
                    final Button button = new Button();

                    {
                        button.setGraphic(buttonGraphic);
                        button.setMinWidth(50);
                        button.setBorder(null);
                    }

                    @Override
                    public void updateItem(final Book person, boolean empty) {
                        deleteBook(person, empty);
                        table.setItems(booksData);
                    }

                    private void deleteBook(Book person, boolean empty) {
                        super.updateItem(person, empty);
                        if (person != null) {
                            buttonGraphic.setImage(deleteImage);
                            setGraphic(button);
                            button.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    BookService BookService = new BookService();
                                    BookService.deleteBook(BookService.findBook(person.getIdBook()));
                                    initData();
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

    // подготавливаем данные из базы данных
    private void initData() {

        booksData.clear();
        SessionFactory sessionFactory = new Configuration().configure()
                .buildSessionFactory();
        Session session = sessionFactory.openSession();
        @SuppressWarnings("unchecked")
        List<Book> Books = session.createQuery("FROM Book book where book.idGenre =:paramGenre").setParameter("paramGenre", currentGenre).list();
        booksData.addAll(Books);
        session.close();
    }

    private void openGenreInfo() throws IOException {

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

    public void setGenre(Genre person) {
        currentGenre = person;
    }
}
