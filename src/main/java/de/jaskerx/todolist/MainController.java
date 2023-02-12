package de.jaskerx.todolist;

import de.jaskerx.todolist.db.DbManager;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController {

    @FXML
    protected void onCategoryAddClick(MouseEvent event) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(ToDoListApplication.primaryStage);
        dialog.getIcons().add(ToDoListApplication.icon);
        dialog.setResizable(false);
        VBox box = new VBox();
        box.setPrefWidth(300);
        box.setPrefHeight(200);
        box.setPadding(new Insets(50, 50, 0, 50));
        dialog.setTitle("Neue Kategorie hinzufügen");
        TextField input = new TextField();
        input.setPromptText("Kategorie");
        VBox buttonContainer = new VBox();
        buttonContainer.setPadding(new Insets(50, 65, 0, 50));
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

}