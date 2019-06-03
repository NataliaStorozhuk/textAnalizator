package sample.Controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
import sample.DTO.BookProfile;
import sample.FileConverter.FileToBookConverter;
import sample.FileConverter.ObjectToJsonConverter;
import sample.Services.BookService;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BooksController extends ControllerConstructor {

    private Stage stage;
    private Genre currentGenre;

    private final VBox vbox = new VBox();

    private final TableView<Book> table = new TableView<>();
    private final Label label = new Label("Книги");

    private final TextField newPath = new TextField();
    private final Button newAdd = new Button();
    private final Button back = new Button();
    private ObservableList<Book> booksData = FXCollections.observableArrayList();


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
                    openGenrePage(stage);
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

        newAdd.setGraphic(imageButtonAdd());
        back.setGraphic(imageButtonBack());

        newPath.setPromptText("Путь к файлу");
        newPath.setMinWidth(500);
        newPath.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                final FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(stage);
                newPath.setText(file.getPath());
            }
        });

        newAdd.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {

            //сделали файлик
            FileToBookConverter fileToBookConverter = new FileToBookConverter();
            BookProfile resultBook = fileToBookConverter.getBookFromFile(new File(newPath.getText()));
            String resultPath = ("C:\\Users\\Natalia\\Desktop\\analyzer\\books\\" + resultBook.getName() + ".json");
            ObjectToJsonConverter.fromObjectToJson(resultPath, resultBook);

            //записали в базочку
            BookService bookService = new BookService();
            Book newBook = new Book(resultBook.name, resultPath, false, false, currentGenre);
            bookService.saveBook(newBook);

            //обновили интерфейсик
            booksData.add(newBook);
            table.refresh();
            newPath.setText("");

        });

        hBox.getChildren().addAll(back, newPath, newAdd);
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }


    private void drawTable() {

        table.setEditable(true);

        TableColumn idColumn = new TableColumn("ID книги");
        TableColumn nameColumn = new TableColumn("Название");
        nameColumn.setMinWidth(400);
        TableColumn indexedColumn = new TableColumn("Индексирована");
        TableColumn trainingColumn = new TableColumn("Обуч. выб.");
        TableColumn<sample.DBModels.Book, sample.DBModels.Book> deleteColumn = new TableColumn<>("Удалить");

        idColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("idBook"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("name"));

        indexedColumn.setCellValueFactory(new PropertyValueFactory<Book, CheckBox>("indexed"));
        indexedColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Book, CheckBox>, ObservableValue<CheckBox>>) arg0 -> {
            Book book = arg0.getValue();

            CheckBox checkBox = new CheckBox();

            checkBox.selectedProperty().setValue(book.getIndexed());

            checkBox.setAlignment(Pos.CENTER);

            return new SimpleObjectProperty<CheckBox>(checkBox);

        });

        trainingColumn.setCellValueFactory(new PropertyValueFactory<Book, CheckBox>("training"));
        trainingColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Book, CheckBox>, ObservableValue<CheckBox>>) arg0 -> {
            Book book = arg0.getValue();

            CheckBox checkBox = new CheckBox();

            checkBox.selectedProperty().setValue(book.getTraining());

            checkBox.setAlignment(Pos.CENTER);

            return new SimpleObjectProperty<CheckBox>(checkBox);

        });

        deleteColumnCreator(deleteColumn);


        table.setItems(booksData);

        table.getColumns().addAll(idColumn, nameColumn, indexedColumn, trainingColumn, deleteColumn);
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
                            button.setOnAction(event -> {
                                BookService BookService = new BookService();
                                BookService.deleteBook(BookService.findBook(person.getIdBook()));
                                initData();
                                table.refresh();
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


    public void setGenre(Genre person) {
        currentGenre = person;
    }
}
