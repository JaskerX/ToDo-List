package de.jaskerx.todolist.db;

import de.jaskerx.todolist.ToDoListApplication;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class CategoryLabel extends Label {

    private int id;

    public CategoryLabel(final String name, final int id, final Pane parent) {
        super();
        this.id = id;

        setText(name);
        getStyleClass().add("menuitem");
        setOnMouseClicked(event -> {
            for(Node n : parent.getChildren()) {
                if(n.getStyleClass().contains("menuitem_selected")) {
                    n.getStyleClass().remove("menuitem_selected");
                    n.getStyleClass().add("menuitem");
                }
            }
            getStyleClass().remove("menuitem");
            getStyleClass().add("menuitem_selected");
            ToDoListApplication.loadTasks(id);
        });
    }

}
