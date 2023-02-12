module de.jaskerx.todo.todolist {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    //requires eu.hansolo.tilesfx;

    opens de.jaskerx.todolist to javafx.fxml;
    exports de.jaskerx.todolist;
}