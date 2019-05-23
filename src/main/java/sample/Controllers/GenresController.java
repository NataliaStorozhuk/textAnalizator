package sample.Controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import sample.User;

import java.io.IOException;
import java.util.Comparator;

public class GenresController {

    private Stage stage;

    @FXML
    private Button back;

    @FXML
    private Button genreInfo;

    final TableView<User> table = new TableView<>();
    private ObservableList<User> usersData = FXCollections.observableArrayList();

    // инициализируем форму данными
    @FXML
    private void initialize() throws IOException {

        back.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                try {
                    openMainPage();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        initData();


    }

    // подготавливаем данные для таблицы
    // вы можете получать их с базы данных
    private void initData() {
        usersData.add(new User(1, "Alex", "qwerty", true));
        usersData.add(new User(2, "Bob", "dsfsdfw", true));
        usersData.add(new User(3, "Jeck", "dsfdsfwe", true));
        usersData.add(new User(4, "Mike", "iueern", true));
        usersData.add(new User(5, "colin", "woeirn", true));
    }

    public void setStage(Stage stage) {


        TableColumn idColumn = new TableColumn("ID пользователя");
        TableColumn loginColumn = new TableColumn("Логин");
        TableColumn rightsColumn = new TableColumn("Администратор");
        TableColumn<User, User> deleteColumn = new TableColumn("Удалить");

        // устанавливаем тип и значение которое должно хранится в колонке
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        loginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));
        rightsColumn.setCellValueFactory(new PropertyValueFactory<>("rights"));
        deleteColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, User>, ObservableValue<User>>() {
            @Override
            public ObservableValue<User> call(TableColumn.CellDataFeatures<User, User> features) {
                return new ReadOnlyObjectWrapper(features.getValue());
            }
        });
        deleteColumn.setComparator(new Comparator<User>() {
            @Override
            public int compare(User p1, User p2) {
                return p1.getLogin().compareTo(p2.getLogin());
            }
        });
        deleteColumn.setCellFactory(new Callback<TableColumn<User, User>, TableCell<User, User>>() {
            @Override
            public TableCell<User, User> call(TableColumn<User, User> btnCol) {
                return new TableCell<User, User>() {
                    //    final ImageView buttonGraphic = new ImageView();
                    final Button button = new Button();

                    {
                        //      button.setGraphic(buttonGraphic);
                        //    button.setMinWidth(130);
                        button.setText("test1");
                    }

                    @Override
                    public void updateItem(final User person, boolean empty) {
                        super.updateItem(person, empty);
                        if (person != null) {
                            button.setText(person.getLogin() + "фрукт");

                            button.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    System.out.println("Bought " + person.getLogin().toLowerCase() + " for: " +
                                            person.getPassword() + " " + person.getRights() + " " + person.getId());
                                }
                            });
                        } else {
                            button.setText("error");
                        }
                    }
                };
            }
        });


//это модно
        table.setItems(usersData);

        table.getColumns().addAll(idColumn, loginColumn, rightsColumn, deleteColumn);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(table);
        VBox.setVgrow(table, Priority.ALWAYS);

        stage.setScene(new Scene(vbox));
        stage.show();

//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/genres.fxml"));


    /*    AnchorPane page = (AnchorPane) loader.load();

        Scene scene = new Scene(page);

        ((Group) scene.getRoot()).getChildren().addAll(table);

        stage.setScene(scene);
        stage.show();*/


    /*    Callback<TableColumn<User, String>, TableCell<User, String>> cellFactory
                = //
                new Callback<TableColumn<User, String>, TableCell<User, String>>() {
                    @Override
                    public TableCell call(final TableColumn<User, String> param) {
                        final TableCell<User, String> cell = new TableCell<User, String>() {

                            final Button btn = new Button("Just Do It");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        User person = getTableView().getItems().get(getIndex());
                                        System.out.println(((User) person).getLogin()
                                                + "   " + person.getPassword());

                                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                        alert.setTitle("Test Connection");

                                        // Header Text: null
                                        alert.setHeaderText(null);
                                        alert.setContentText("Второе окошко открылось и все!!");

                                        alert.showAndWait();

                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };

        deleteColumn.setCellFactory(cellFactory);
        // заполняем таблицу данными
        table.setItems(usersData);

        table.getColumns().addAll(idColumn, loginColumn, passwordColumn, rightsColumn, deleteColumn);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/genres.fxml"));


        AnchorPane page = (AnchorPane) loader.load();

        Scene scene = new Scene(page);

        ((Group) scene.getRoot()).getChildren().addAll(table);

        stage.setScene(scene);
        stage.show();*/
        this.stage = stage;
    }

    private void openMainPage() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        stage.setTitle("Анализ текста");

        Scene scene = new Scene(page);
        stage.setScene(scene);

        // Передаём адресата в контроллер.
        MainController controller = loader.getController();
        controller.setStage(stage);
        controller.adminSettings.setVisible(true);
        stage.show();

    }

    public void setStartData() {
    }
}
