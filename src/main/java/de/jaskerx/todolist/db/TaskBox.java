package de.jaskerx.todolist.db;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

import static de.jaskerx.todolist.ToDoListApplication.getFormattedDate;
import static de.jaskerx.todolist.ToDoListApplication.primaryScene;

public class TaskBox extends VBox {

    public TaskBox(final String name, final String description, final LocalDateTime dateUntil, final LocalDateTime dateCreated) {
        super();

        getStyleClass().add("container_task");
        setOnMouseClicked(new EventHandler<MouseEvent>() {
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
                    if (dateUntil != null) {
                        labelUntil.setText("Bis: " + getFormattedDate(dateUntil));
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
        getChildren().addAll(hBox, labelDescription);
    }

}
