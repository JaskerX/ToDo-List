package de.jaskerx.todolist;

import de.jaskerx.todolist.db.CategoryLabel;
import de.jaskerx.todolist.db.DbManager;
import de.jaskerx.todolist.db.TaskBox;
import javafx.animation.*;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;


public class ToDoListApplication extends Application {

    public static Image icon = new Image(ToDoListApplication.class.getResource("icon.png").toExternalForm());
    public static Stage primaryStage;
    public static Scene primaryScene;
    public static int selectedCategory = -1;

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
        ((VBox) primaryScene.lookup("#container_tasks")).getChildren().clear();

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
                String name = rs.getString("name");
                container.getChildren().add(new CategoryLabel(name, id, container));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static void loadTasks(int id) {
        selectedCategory = id;
        VBox containerTasks = (VBox) primaryScene.lookup("#container_tasks");
        containerTasks.getChildren().clear();
        try(ResultSet rs = DbManager.executeQuery("SELECT name, description, until, created FROM tasks WHERE category = " + id + " ORDER BY until")) {
            while(rs.next()) {
                LocalDateTime dateUntil = null;
                try {
                    dateUntil = rs.getTimestamp("until").toLocalDateTime();
                } catch(SQLException ignore) {}
                LocalDateTime dateCreated = rs.getTimestamp("created").toLocalDateTime();
                String name = rs.getString("name");
                String description = rs.getString("description");
                VBox margin = new VBox();
                margin.setPrefHeight(10);
                containerTasks.getChildren().addAll(margin, new TaskBox(name, description, dateUntil, dateCreated));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getFormattedDate(LocalDateTime dateTime) {
        StringBuilder builder = new StringBuilder();
        builder.append(dateTime.getDayOfMonth() < 10 ? "0" : "")
                .append(dateTime.getDayOfMonth())
                .append(".")
                .append(dateTime.getMonthValue() < 10 ? "0" : "")
                .append(dateTime.getMonthValue())
                .append(".")
                .append(dateTime.getYear());
        return new String(builder);
    }

    public static void main(String[] args) {
        launch();
    }
}