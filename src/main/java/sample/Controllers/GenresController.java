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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
import sample.User;

import java.io.IOException;

public class GenresController {

    private Stage stage;

    @FXML
    private Button back;

    @FXML
    private Button genreInfo;
    final VBox vbox = new VBox();

    final TableView<User> table = new TableView<User>();
    private ObservableList<User> usersData = FXCollections.observableArrayList();

    private final Image deleteImage = new Image(
            "http://icons.iconarchive.com/icons/itweek/knob-toolbar/32/Knob-Cancel-icon.png"
    );

    // инициализируем форму данными
    @FXML
    private void initialize() throws IOException {
        table.setEditable(true);

        final Label label = new Label("Пользователи");
        label.setFont(new Font("Arial", 20));

        final Label actionTaken = new Label();

        table.setEditable(true);

        TableColumn idColumn = new TableColumn("ID пользователя");
        TableColumn loginColumn = new TableColumn("Логин");
        TableColumn rightsColumn = new TableColumn("Администратор");
        TableColumn<User, User> deleteColumn = new TableColumn<>("Удалить");

        // устанавливаем тип и значение которое должно хранится в колонке
        idColumn.setCellValueFactory(new PropertyValueFactory<User, String>("id"));
        loginColumn.setCellValueFactory(new PropertyValueFactory<User, String>("login"));
        rightsColumn.setCellValueFactory(new PropertyValueFactory<User, String>("rights"));

        deleteColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, User>, ObservableValue<User>>() {
            @Override
            public ObservableValue<User> call(TableColumn.CellDataFeatures<User, User> features) {
                return new ReadOnlyObjectWrapper(features.getValue());
            }
        });


        deleteColumn.setCellFactory(new Callback<TableColumn<User, User>, TableCell<User, User>>() {
            @Override
            public TableCell<User, User> call(TableColumn<User, User> deleteColumn) {
                return new TableCell<User, User>() {
                    final ImageView buttonGraphic = new ImageView();
                    final Button button = new Button();

                    {
                        button.setGraphic(buttonGraphic);
                        button.setMinWidth(50);
                        button.setBorder(null);
                        //  buttonGraphic.setImage(deleteImage);
                        //        button.setText("test1");
                    }

                    @Override
                    public void updateItem(final User person, boolean empty) {
                        super.updateItem(person, empty);
                        if (person != null) {
                            buttonGraphic.setImage(deleteImage);
                            setGraphic(button);
                            button.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    actionTaken.setText("Bought " + person.getLogin().toLowerCase() + " for: " +
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

        initData();

        table.setItems(usersData);

        table.getColumns().addAll(idColumn, loginColumn, rightsColumn, deleteColumn);
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(label, table, actionTaken);
        VBox.setVgrow(table, Priority.ALWAYS);


        //  stage.show();

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

        this.stage = stage;
        stage.setScene(new Scene(vbox));

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

}
