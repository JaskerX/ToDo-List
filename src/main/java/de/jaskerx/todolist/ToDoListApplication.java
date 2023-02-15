package de.jaskerx.todolist;

import de.jaskerx.todolist.db.DbManager;
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
                Label newLabel = new Label();
                newLabel.setText(rs.getString("name"));
                newLabel.getStyleClass().add("menuitem");
                newLabel.setOnMouseClicked(event -> {
                    for(Node n : container.getChildren()) {
                        if(n.getStyleClass().contains("menuitem_selected")) {
                            n.getStyleClass().remove("menuitem_selected");
                            n.getStyleClass().add("menuitem");
                        }
                    }
                    newLabel.getStyleClass().remove("menuitem");
                    newLabel.getStyleClass().add("menuitem_selected");
                    loadTasks(id);
                });
                container.getChildren().add(newLabel);
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
                final LocalDateTime dateUntilFinal = dateUntil;
                LocalDateTime dateCreated = rs.getTimestamp("created").toLocalDateTime();
                String name = rs.getString("name");
                String description = rs.getString("description");
                VBox margin = new VBox();
                margin.setPrefHeight(10);
                VBox vbox = new VBox();
                vbox.getStyleClass().add("container_task");
                vbox.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if(event.getButton() == MouseButton.PRIMARY) {
                            Label labelName = (Label) primaryScene.lookup("#task_detail_name");
                            Label labelDescription = (Label) primaryScene.lookup("#task_detail_description");
                            Label labelUntil = (Label) primaryScene.lookup("#task_detail_until");
                            Label labelCreated = (Label) primaryScene.lookup("#task_detail_created");
                            labelName.setText(name);
                            labelDescription.setText(description);
                            labelCreated.setText("Erstellt: " + getFormattedDate(dateCreated));
                            if (dateUntilFinal != null) {
                                labelUntil.setText("Bis: " + getFormattedDate(dateUntilFinal));
                            } else {
                                labelUntil.setText("");
                            }
                            primaryScene.lookup("#container_task_detail").setVisible(true);
                        }
                    }
                });
                Label labelDescription = new Label();
                labelDescription.getStyleClass().add("task_description");
                labelDescription.setText(description == null ? "" : description);
                HBox hBox = new HBox();
                Label labelName = new Label();
                Label labelDate = new Label();
                labelName.getStyleClass().add("task_name");
                labelDate.getStyleClass().add("task_date");
                labelName.setText(name);
                String date = "";
                if(dateUntil != null) {
                    date = dateUntil.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.GERMANY).substring(0, 2) + ", ";
                    date += getFormattedDate(dateUntil);
                }
                labelDate.setText(date);
                hBox.getChildren().addAll(labelName, labelDate);
                vbox.getChildren().addAll(hBox, labelDescription);
                containerTasks.getChildren().addAll(margin, vbox);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        /*VBox label = (VBox) primaryScene.lookup("#test");
        Animation animation = new Transition() {
            {
                setCycleDuration(Duration.seconds(3));
            }

            @Override
            protected void interpolate(double progress) {
                double original = 0;
                if(progress == 0) {
                    original = label.getWidth();
                }
                double newValue = 50 + 50 * progress;
                label.setPrefHeight(newValue);
            }
        };
        animation.playFromStart();*/
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