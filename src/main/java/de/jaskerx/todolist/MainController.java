package de.jaskerx.todolist;

import de.jaskerx.todolist.db.DbManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MainController {

    Timestamp ts = null;

    @FXML
    protected void onCategoryAddClick() {
        Stage dialog = getPopupStage("Neue Kategorie hinzufügen");
        VBox box = new VBox();
        box.setPrefWidth(300);
        box.setPrefHeight(200);
        box.setPadding(new Insets(50, 50, 0, 50));
        TextField input = new TextField();
        input.setPromptText("Kategorie");
        VBox buttonContainer = new VBox();
        buttonContainer.setPadding(new Insets(50, 65, 20, 50));
        Button button = new Button();
        button.setText("Hinzufügen");
        button.setPrefWidth(200);
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                DbManager.executeUpdate("INSERT INTO categories (name) VALUES ('" + input.getText() + "')");
                ToDoListApplication.refreshCategories();
                dialog.close();
            }
        });
        buttonContainer.getChildren().add(button);
        box.getChildren().add(input);
        box.getChildren().add(buttonContainer);
        Scene dialogScene = new Scene(box, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    @FXML
    protected void addTask(MouseEvent event) {
        if(event.getButton() == MouseButton.SECONDARY) {
            Stage dialog = getPopupStage("Aufgabe hinzufügen");
            VBox box = new VBox();
            box.setPrefWidth(300);
            box.setPrefHeight(200);
            box.setPadding(new Insets(50, 50, 0, 50));
            TextField inputName = new TextField();
            inputName.setPromptText("Aufgabe");
            TextField inputDescription = new TextField();
            inputDescription.setPromptText("Beschreibung");
            DatePicker datePicker = new DatePicker();
            datePicker.setShowWeekNumbers(true);
            datePicker.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if(datePicker.getValue() != null) {
                        LocalDateTime date = datePicker.getValue().atTime(23, 59, 59);
                        ts = Timestamp.valueOf(date);
                    } else {
                        ts = null;
                    }
                }
            });
            VBox buttonContainer = new VBox();
            buttonContainer.setPadding(new Insets(30, 65, 20, 50));
            Button button = new Button();
            button.setText("Hinzufügen");
            button.setPrefWidth(200);
            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    DbManager.executeUpdate("INSERT INTO tasks (name, description, category, created, until) VALUES ('" + inputName.getText() + "', '" + inputDescription.getText() + "', " + ToDoListApplication.selectedCategory + ", CURRENT_TIMESTAMP, '" + ts + "')");
                    ts = null;
                    ToDoListApplication.loadTasks(ToDoListApplication.selectedCategory);
                    dialog.close();
                }
            });
            buttonContainer.getChildren().add(button);
            box.getChildren().add(inputName);
            box.getChildren().add(inputDescription);
            box.getChildren().add(datePicker);
            box.getChildren().add(buttonContainer);
            Scene dialogScene = new Scene(box, 300, 200);
            dialog.setScene(dialogScene);
            dialog.show();
        }
    }

    public Stage getPopupStage(String title) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(ToDoListApplication.primaryStage);
        dialog.getIcons().add(ToDoListApplication.icon);
        dialog.setResizable(false);
        dialog.setTitle(title);
        return dialog;
    }

}