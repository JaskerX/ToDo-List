package de.jaskerx.todolist;

import de.jaskerx.todolist.db.DbManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ToDoListApplication extends Application {

    public static Image icon = new Image(ToDoListApplication.class.getResource("icon.png").toExternalForm());
    public static Stage primaryStage;
    public static Scene primaryScene;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ToDoListApplication.class.getResource("primary-view.fxml"));
        DbManager.init();
        primaryStage = stage;
        stage.setTitle("ToDo Liste");
        stage.getIcons().add(icon);
        stage.show();
        primaryScene = new Scene(fxmlLoader.load(), 320, 240);
        primaryScene.getStylesheets().add(ToDoListApplication.class.getResource("application.css").toExternalForm());
        stage.setScene(primaryScene);
        stage.setMaximized(true);
        ImageView iv = (ImageView) primaryScene.lookup("#img_logo");
        Image image = new Image(ToDoListApplication.class.getResource("logo.png").toExternalForm());
        iv.setImage(image);

        refreshCategories();
    }

    public static void refreshCategories() {
        ScrollPane scrollPane = (ScrollPane) primaryScene.lookup("#scrollpane_categories");
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        VBox container = (VBox) primaryScene.lookup("#container_categories");
        container.getChildren().clear();
        try (ResultSet rs = DbManager.executeQuery("SELECT id, name FROM categories")) {
            while(rs.next()) {
                int id = Integer.parseInt(rs.getString("id"));
                Label newLabel = new Label();
                newLabel.setText(rs.getString("name"));
                newLabel.getStyleClass().add("menuitem");
                newLabel.setOnMouseClicked(event -> loadTasks(id));
                container.getChildren().add(newLabel);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static void loadTasks(int id) {

    }

    public static void main(String[] args) {
        launch();
    }
}